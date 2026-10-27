module.exports = {
  extends: ['@commitlint/config-conventional'],
  plugins: [
    {
      rules: {
        'project-header': ({ header }) => {
          const match = /^(feat|fix|docs|test|refactor|chore|style)\([a-z][a-z0-9-]*\): (.+) \[(?:TASK-\d+|T-\d{2}-\d{2})\]$/.exec(header || '')
          const subject = match?.[2] || ''
          return [
            Boolean(match) && /^[a-z][\x20-\x7e]*$/.test(subject) &&
              subject === subject.toLowerCase() && !/[.\s]$/.test(subject),
            'Use type(scope): lower case English subject [TASK-012] or [T-30-05]; start with a verb and omit the final period.',
          ]
        },
      },
    },
  ],
  rules: {
    'project-header': [2, 'always'],
    'type-enum': [2, 'always', ['feat', 'fix', 'docs', 'test', 'refactor', 'chore', 'style']],
    // The project rule checks the subject separately from the upper case task ID.
    'subject-case': [0],
    'header-max-length': [0],
    'body-max-line-length': [0],
    'footer-max-line-length': [0],
  },
}
