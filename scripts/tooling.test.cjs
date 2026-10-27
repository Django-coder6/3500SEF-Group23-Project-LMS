const { test } = require('node:test')
const assert = require('node:assert/strict')
const { spawnSync } = require('node:child_process')
const path = require('node:path')
const { changedPaths, checksFor } = require('./pre-push.cjs')

const zero = '0'.repeat(40)
const local = 'a'.repeat(40)
const remote = 'b'.repeat(40)
const ref = (before = remote, after = local) => `refs/heads/work ${after} refs/heads/work ${before}\n`

test('existing branches compare the pushed tips, preserving filenames with spaces', () => {
  const paths = changedPaths(ref(), 'origin', args => {
    assert.deepEqual(args, ['diff', '--name-only', '--no-renames', '-z', remote, local, '--'])
    return 'frontend/src/a file.vue\0docs/note.md\0'
  })
  assert.deepEqual(checksFor(paths), { frontend: true, backend: false })
})

test('new branches inspect unpushed commits including root and merge commits', () => {
  const calls = []
  const paths = changedPaths(ref(zero), 'origin', (args, input) => {
    calls.push(args)
    if (args[0] === 'rev-list') return `${local}\n`
    assert.equal(input, `${local}\n`)
    return 'backend/src/main/java/Example.java\0'
  })
  assert.deepEqual(calls, [
    ['rev-list', local, '--not', '--remotes=origin'],
    ['diff-tree', '--stdin', '--root', '-m', '-r', '--no-commit-id', '--name-only', '-z'],
  ])
  assert.deepEqual(checksFor(paths), { frontend: false, backend: true })
})

test('deletions and already published new branches require no lint', () => {
  assert.deepEqual(changedPaths(ref(remote, zero), 'origin', () => assert.fail('unexpected git call')), [])
  assert.deepEqual(changedPaths(ref(zero), 'origin', () => ''), [])
})

test('documentation pushes skip frontend lint; Checkstyle changes trigger Maven', () => {
  assert.deepEqual(checksFor(['docs/note.md']), { frontend: false, backend: false })
  assert.deepEqual(checksFor(['checkstyle.xml']), { frontend: false, backend: true })
})

test('multiple refs combine changes and git failures reject the push', () => {
  let count = 0
  assert.deepEqual(checksFor(changedPaths(ref() + ref(), 'origin', () =>
    ++count === 1 ? 'frontend/src/App.vue\0' : 'backend/pom.xml\0')),
  { frontend: true, backend: true })
  assert.throws(() => changedPaths(ref(), 'origin', () => { throw new Error('missing remote tip') }), /missing remote tip/)
})

test('commitlint uses conventional rules without imposing scope or task IDs', () => {
  assert.deepEqual(require('../commitlint.config.js'), {
    extends: ['@commitlint/config-conventional'],
  })
  const cli = path.resolve(__dirname, '../node_modules/@commitlint/cli/cli.js')
  const accepted = [
    'feat(backend): add parcel intake endpoint [TASK-012]',
    'chore(tooling): add quality checks [T-30-05]',
    'feat: add checks',
    'ci(tooling): add checks',
    'chore: refresh dependencies',
    'Merge develop into the Flyway branch so CI can run the migrations [T-30-03]',
    "Merge branch 'develop' into feature/tooling-quality-checks",
    'Merge pull request #230 from group/tooling',
  ]
  const rejected = ['update files', 'Update README.md', 'feat:', 'unknown: add checks']
  for (const message of [...accepted, ...rejected]) {
    const result = spawnSync(process.execPath, [cli], { input: message + '\n', encoding: 'utf8' })
    assert.ifError(result.error)
    assert.equal(result.status === 0, accepted.includes(message), message + '\n' + result.stdout + '\n' + result.stderr)
  }
})
