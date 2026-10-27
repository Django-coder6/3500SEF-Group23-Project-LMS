# Personal Logbook - Chan Sung Ming William
- Student ID: 14480597
- GitHub: @smcheese9731-sys
- Role: R10 DevOps and integration
- Modules: TBD
- Period covered: W01-W08

## Week boundary

A note on how the weeks are labelled, because the sprint numbering and the
calendar week do not line up in this project.

| Logbook week | Calendar dates | What it covers |
|---|---|---|
| W01 | 2026-10-02 to 2026-10-09 | Sprint 0: repository setup |
| W02 | 2026-10-10 to 2026-10-19 | Sprint 1: Docker Environment Configuration and Frontend CI Pipeline Setup |
| W03 | from 2026-10-20 | Sprint 2:  |

## Weekly log

| Week | Date | Task | Description | Time | Hours | Status | Evidence | With | Problem and fix | Note |
|---|---|---|---|---|---|---|---|---|---|---|
| W01 | 2026-10-09 | - | Finish reading CONTRIBUTING.md and commit for the first time | 16:45-17:15 | 0.5 | Done | [#25](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/25) | - | - | Use PR process for the first time |
| W01 | 2026-10-10 | T-00-03 | Check CI workflow | 15:45-15:51 | 0.1 | Done | - | - | - | Confirmed all workflows in CI are ticked and reported the CI status in comment section |
| W02 | 2026-10-13 | T-30-01 | Added docker compose with PostgreSQL 16, Redis, backend and frontend, plus multi-stage Dockerfiles and an env example | 16:00-18:30, 00 | 2.5 | done | docker-compose.yml, backend/Dockerfile, [PR #93](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/93) | R6, R7 | The backend had no build file yet, so the backend image could not be built until the Maven wrapper landed | Verified on a second machine |
| W02 | 2026-10-14 | T-30-01 | Fix docker compose issues | 00:00-01:45 | 1.75 | Done | [PR #93](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/93) | - | Backend build failed due to missing pom.xml (T-30-02 pending), so I placed the backend behind a full profile. This caused an 'undefined service' error because the frontend still depended on it. Fixed by removing the depends_on: backend constraint from the frontend. Also translated Dockerfile comments to English to meet repository standards | Verified on a clean machine. Nginx /api proxy setup is deferred until R4 finalizes the frontend base URL in Issue #82 |
| W03 | 2026-10-26 | T-30-05 | Added `checkstyle.xml` and `backend/pom.xml` to run Checkstyle and Spotless during Maven verify | 14:30-15:55 | 1.42 | Done | [PR #230](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/230) | - | Backend had no POM; added a minimal Java 17 build configuration | Tested on JDK 21 targeting Java 17; wrapper integration awaits R6 |
| W03 | 2026-10-26 | T-30-05 | Added root `package.json` and `package-lock.json` for pinned tooling dependencies and hook installation | 15:55-16:50 | 0.92 | Done | [PR #230](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/230) | - | Repository tooling had no dependency manifest; added one | Root npm installation now installs Husky hooks |
| W03 | 2026-10-26 | T-30-05 | Added `commitlint.config.js` to check commit types, scopes, subjects and task IDs | 16:50-17:20 | 0.5 | Done | [PR #230](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/230) | - | Existing and current task ID formats differed; accepted both | Valid messages pass; invalid formats are rejected |
| W03 | 2026-10-26 | T-30-05 | Added Husky `commit-msg`, `pre-push` and `scripts/pre-push.cjs` for local commit and push checks | 17:20-19:35 | 2.25 | Done | [PR #230](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/230) | - | New branches have no remote tip; added detection using unpushed commits | Frontend lint runs only when pushed changes affect frontend |
| W03 | 2026-10-26 | T-30-05 | Updated `ci.yml` with commitlint and tooling test steps | 21:00-22:30 | 1.5 | Done | [PR #230](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/230) | - | Local hooks can be skipped; added CI commit-message validation | GitHub execution pending; backend job remains assigned to R6 |
| W03 | 2026-10-26 - 2026-10-27 | T-30-05 | Updated `CONTRIBUTING.md` with hook installation, quality commands and backend integration instructions | 22:40-01:00 | 2.33 | Done | [PR #230](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/230) | - | Hooks require local installation; documented the root npm setup | Included Maven wrapper paths and R6 handover details |
| W03 | 2026-10-27 | T-30-05 | Added `scripts/tooling.test.cjs` and verified hooks, formatting failures and documentation checks | 13:15-16:15 | 3 | Done | [PR #230](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/230) | - | Windows test harness and Python alias issues were resolved during validation | Six tests pass; fresh-clone hook installation also verified |
| W03 | 2026-10-27 | T-30-05 | Audited the seven acceptance criteria. Tested hook installation in a fresh local clone with pending changes overlaid, commit-message rejection, Spotless formatting failures and fixes, and Checkstyle rejection. Ran six tooling tests and checked pre-push dispatch using controlled Git and lint inputs. | 16:15-21:45 | 4.5 | Done | [PR #230](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/230) | - | Corrected the audit script to recognise Maven's abbreviated plugin names. Identified the missing Maven wrapper and incomplete CI coverage; these remain unresolved. | Testing completed: five criteria passed, one was partial, and one failed. Java checks used JDK 21 targeting Java 17. No repository files were changed during the audit; nothing was committed or pushed. |
| W03 | 2026-10-27 - 2026-10-28 | T-30-05 | Addressed the PR #230 review. Integrated current develop while preserving Spring Boot dependencies, the Maven wrapper and four existing CI jobs. Added the required braces and public-class Javadoc checks, adopted Palantir formatting, and updated 37 Java files through formatting or documentation. Removed custom commit-header restrictions, updated tooling tests and hook guidance, extended CI to all branch pushes, and recorded proposed CR-027. | 21:50-01:05 | 3.25 | Done | [PR #230](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/230); local staged diff against develop `727db2f` | - | The original branch used an outdated snapshot and conflicted with the existing backend. Prepared a resolved merge with additive quality plugins. Updated commitlint push ranges to exclude historical commits imported from integration branches. | Passed 24 backend tests on JDK 17, 68 frontend tests, six tooling tests, style rejection checks, fresh-clone hook installation and Docker login checks. Corrections are staged; commit and push await approval. |

## Running total

| Week | Dates covered | Hours |
|---|---|---|
| W01 | 2026-10-02 to 2026-10-11 | 0.6 |
| W02 | 2026-10-12 to 2026-10-19 | 4.25 |
| W03 | 2026-10-20 to 2026-10-25 | 19.67 |
| **Total to date** | | **24.52** |