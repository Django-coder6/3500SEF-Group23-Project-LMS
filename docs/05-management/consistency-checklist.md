# Consistency Checklist

| Item | Detail |
|---|---|
| Task | T-00-08 |
| Owner | R9 (Documentation & Quality) |
| Version | v1.0 |
| Status | Active |

**Purpose:** Run this before opening a pull request that touches a document. It turns consistency from something we have to remember into something we can check.

> **Why this exists:** This project has already hit the same consistency problem four times (glossary, change-log, risk-register). R4 alone caught 14 consistency issues. This checklist is the process improvement that prevents a fifth.

---

## 1. Version and history

- [ ] If the document has a version header and a revision history, they agree (CI: `scripts/check_doc_versions.py`)
- [ ] A new revision row was added for this change
- [ ] The version number was incremented, not reused

## 2. File and folder naming

- [ ] File and folder names use lower case, with words separated by a hyphen
- [ ] No spaces in any file or folder name
- [ ] Dated documents start with `YYYYMMDD` (e.g. `20261015_weekly-meeting.md`)
- [ ] Personal logbooks follow `docs/06-logbook/YYYYMMDD_Name_Logbook.md`
      *(This is the one exception to the hyphen rule: logbooks use underscores, as defined in `CONTRIBUTING.md`.)*
- [ ] The path in any reference matches the actual case (`glossary.md`, not `GLOSSARY.md`)

## 3. Links

- [ ] Every relative link resolves with the exact case (a Windows checkout hides case errors; GitHub does not)
- [ ] Relative paths are correct for the file's depth (e.g. `../../CONTRIBUTING.md` from `docs/05-management/`)
- [ ] No broken links remain from renamed or moved files

## 4. Terms

- [ ] New terms are added to `docs/glossary.md`, which is the single source of truth
- [ ] Retired terms do not appear (see the glossary's retired list)
      *(archived artefacts under `docs/02-design/prototype/` are exempt)*
- [ ] If a term is defined in the glossary, other documents reference it instead of redefining it (CR-012)

## 5. Single-sourced lists

- [ ] A list that exists elsewhere is referenced, not restated (CR-012)
      *Example: the five exception types live in the glossary; do not re-list them in other documents*

## 6. Cross-document agreement

- [ ] Status names match `design-system.md` 5.3, `ERD.md`, `openapi.yaml` and `backend-state-machines.md`
- [ ] Exception type names match the glossary list (`overdue`, `damaged`, `wrong_recipient`, `refused`, `lost`)
- [ ] Every P0 page in `frontend-page-map.md` has a contract endpoint, or is annotated as pending
      *⭐ This is the `/my-parcels` gap that took several review rounds to find. Checking it here stops it recurring.*
- [ ] The `operatorId` is taken from the session, not sent by the client (CR-007)

## 7. Pull request hygiene

- [ ] Branch name uses one of the prefixes in `CONTRIBUTING.md` (`feature/`, `fix/`, `docs/`, `release/`, `hotfix/`)
- [ ] Commit message format is `<type>(<scope>): <subject> [TASK-ID]`
- [ ] PR title includes the task ID
- [ ] PR description links the relevant issue (`Closes #<n>`)
- [ ] At least one non-author has been requested to review

---

## Revision history

| Version | Date | Change | Author |
|---|---|---|---|
| v1.0 | 2026-10-15 | Initial checklist, created from four repeated consistency failures and R4's 14 caught issues | R9 |
