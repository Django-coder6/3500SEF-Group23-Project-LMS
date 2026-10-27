# Personal Logbook - Chan Sung Ming William
- Student ID: 14480597
- GitHub: @smcheese9731-sys
- Role: R10 DevOps and integration
- Modules: TBD
- Period covered: W01-W08
## Weekly log

| Week | Date | Task | Description | Time | Hours | Status | Evidence | With | Problem and fix | Note |
|---|---|---|---|---|---|---|---|---|---|---|
| W01 | 2026-10-09 | - | Finish reading CONTRIBUTING.md and commit for the first time | 16:45-17:15 | 0.5 | Done | [#25](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/25) | - | - | Use PR process for the first time |
| W01 | 2026-10-10 | T-00-03 | Check CI workflow | 15:45-15:51 | 0.1 | Done | - | - | - | Confirmed all workflows in CI are ticked and reported the CI status in comment section |
| W02 | 2026-10-13 | T-30-01 | Added docker compose with PostgreSQL 16, Redis, backend and frontend, plus multi-stage Dockerfiles and an env example | 16:00-18:30, 00 | 2.5 | done | docker-compose.yml, backend/Dockerfile, [PR #93](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/93) | R6, R7 | The backend had no build file yet, so the backend image could not be built until the Maven wrapper landed | Verified on a second machine |
| W02 | 2026-10-14 | T-30-01 | Fix docker compose issues | 00:00-01:45 | 1.75 | Done | [PR #93](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/93) | - | Backend build failed due to missing pom.xml (T-30-02 pending), so I placed the backend behind a full profile. This caused an 'undefined service' error because the frontend still depended on it. Fixed by removing the depends_on: backend constraint from the frontend. Also translated Dockerfile comments to English to meet repository standards | Verified on a clean machine. Nginx /api proxy setup is deferred until R4 finalizes the frontend base URL in Issue #82 |
| W02 | 2026-10-15 | T-30-06 | Enabled CI frontend job and unified the Node.js version across all environments | 16:00-18:45 | 2.75 | Done | [PR #136](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/136) | R1 | Problem: The CI pipeline failed due to Node version conflicts, meaning the previous "red" run was an environment error, not a valid test failure. The branch was also 4 commits behind develop. Fix: Merged `origin/develop`. Unified the environment by updating `node-version` to `24` in `ci.yml` and `frontend/Dockerfile`. Added `"engines": { "node": ">=22.22.2" }` to `frontend/package.json` and created `frontend/.nvmrc` with content `24`. Finally, executed a Green > Red > Green sequence by deliberately breaking a test to prove the CI pipeline actively blocks broken code | Both the red and green pipeline run URLs were pasted into the PR #136 description as evidence of CI gatekeeping |
