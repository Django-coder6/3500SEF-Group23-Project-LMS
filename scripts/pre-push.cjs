const { execFileSync, spawnSync } = require('node:child_process')
const { readFileSync, existsSync } = require('node:fs')
const path = require('node:path')

function changedPaths(input, remote, git) {
  const paths = new Set()
  for (const line of input.trim().split(/\r?\n/).filter(Boolean)) {
    const [, localSha, , remoteSha] = line.split(/\s+/)
    if (!localSha || !remoteSha) throw new Error('Invalid pre-push ref input')
    if (/^0+$/.test(localSha)) continue // Deleting a branch does not push code.
    let names
    if (/^0+$/.test(remoteSha)) {
      // A new branch has no remote tip. Inspect commits not on this remote,
      // including root commits and merge changes; do not compare to an empty SHA.
      const commits = git(['rev-list', localSha, '--not', `--remotes=${remote}`])
      names = commits.trim()
        ? git(['diff-tree', '--stdin', '--root', '-m', '-r', '--no-commit-id', '--name-only', '-z'], commits)
        : ''
    } else {
      names = git(['diff', '--name-only', '--no-renames', '-z', remoteSha, localSha, '--'])
    }
    for (const name of names.split('\0').filter(Boolean)) paths.add(name)
  }
  return [...paths]
}

function checksFor(paths) {
  return {
    frontend: paths.some(name => name.startsWith('frontend/')),
    backend: paths.some(name => name.startsWith('backend/') || name === 'checkstyle.xml'),
  }
}

function main() {
  const root = execFileSync('git', ['rev-parse', '--show-toplevel'], { encoding: 'utf8' }).trim()
  process.chdir(root)
  const git = (args, input) => execFileSync('git', args, { input, encoding: 'utf8' })
  const checks = checksFor(changedPaths(readFileSync(0, 'utf8'), process.argv[2] || 'origin', git))
  if (checks.frontend) {
    if (!existsSync('frontend/node_modules/.bin/eslint')) {
      throw new Error('Install frontend dependencies first: npm --prefix frontend ci')
    }
    console.log('Checking frontend changes with ESLint...')
    // Call Node directly so this also works in Git Bash on Windows without .cmd quoting.
    run(process.execPath, [path.resolve('frontend/node_modules/eslint/bin/eslint.js'), '.'], 'frontend')
  }
  if (checks.backend) {
    if (!existsSync('backend/pom.xml')) throw new Error('Missing backend/pom.xml')
    console.log('Checking backend changes with Maven verify...')
    if (!existsSync('backend/mvnw')) throw new Error('Missing backend/mvnw; restore the tracked Maven wrapper.')
    run('sh', ['./mvnw', '-B', 'verify'], 'backend')
  }
  if (!checks.frontend && !checks.backend) console.log('No frontend or backend changes to check.')
}

function run(command, args, cwd) {
  const result = spawnSync(command, args, { cwd, stdio: 'inherit' })
  if (result.error) throw new Error(`Cannot run ${command}: ${result.error.message}. See CONTRIBUTING.md for setup.`)
  if (result.status !== 0) throw new Error(`${command} failed (${result.status ?? result.signal}); push rejected.`)
}

if (require.main === module) {
  try { main() } catch (error) {
    console.error(`pre-push: ${error.message}`)
    process.exitCode = 1
  }
}

module.exports = { changedPaths, checksFor }
