# Where to put your work each week

## Folder by type of output

| Output | Folder | Example filename |
|---|---|---|
| Code (backend) | `backend/src/main/java/com/linyi/<module>/` | `StorageLocationService.java` |
| Code (frontend) | `frontend/src/views/` | `LocationBoard.vue` |
| Requirements | `docs/01-requirements/specification/` | `SRS.md` |
| Survey and interview notes | `docs/01-requirements/elicitation/` | `20260305_survey-results.xlsx` |
| UML and ER diagrams | `docs/02-design/uml/`, `docs/02-design/database/` | `usecase-intake.puml` |
| Design decisions | `docs/02-design/adr/` | `ADR-002-spring-boot-backend.md` |
| API specification | `docs/03-api/` | `openapi.yaml` |
| Test cases and reports | `docs/04-testing/` | `test-cases-pickup.xlsx` |
| Meeting minutes | `docs/05-management/minutes/` | `20260305_weekly-meeting.md` |
| Weekly contributor screenshot | `docs/05-management/weekly/` | `W02_contributors.png` |
| Personal logbook | `docs/06-logbook/` | `20260000_name_Logbook.md` |
| Report chapters | `docs/07-report/chapters/` | `03-requirements.md` |

## Weekly routine for members

```bash
git switch develop
git pull

git switch -c docs/W02-srs-requirements

git add docs/01-requirements/specification/SRS.md
git commit -m "docs(srs): add functional requirements FR-2.x [T-10-06]"

git push -u origin docs/W02-srs-requirements
```

Then open a pull request on GitHub with `develop` as the base branch.

## Weekly routine for the project manager

| When | Action |
|---|---|
| Monday | hold the 20 minute stand-up, update the project board, open this week's issues |
| Daily | check for pull requests that are waiting for review |
| Thursday | 45 minute group session to clear blockers and conflicts |
| Sunday 20:00 | post the weekly logbook reminder issue |
| Sunday 21:30 | check that everyone committed something, save the contributors screenshot |
| Sunday 22:00 | update the risk register and the change log |
