# Personal Logbook - Yuan Chong Jun

- Student ID: 13705584
- GitHub: @RookieVENENO
- Role: R4 Frontend Lead + Product Architect A
- Modules: frontend architecture, UI/UX, design system, API integration, code review
- Period covered: W01-W16

## Weekly log

| Week | Date | Task | Description (output produced) | Time | Hours | Status | Evidence (links) | With | Problem and fix | Note |
|---|---|---|---|---|---|---|---|---|---|---|
| W01 | 2026-09-30 | T-00-01 | Attended the topic-selection and role-division meeting. Helped confirm the Web project scope, user roles and page boundaries. | 20:00-22:00 | 2.0 | done | [Form submission, meeting minutes or PR link] | R1, R2, R3, R6 | The first interface idea followed a generic parcel flow; after discussion, it was changed to a community collection-point management system. I revised the navigation and page structure. | This meeting time matches the backend lead's Week 1 kickoff slot. |
| W01 | 2026-09-30 | W06-UI-01 | Prepared a reference frontend interface for the staff/admin workflow, covering the dashboard, parcel records, station status, exceptions, users, reports and settings. | 22:00-23:00 | 1.0 | done | [Prototype path or screenshot link] | R5 | APIs were not available yet, so I used mock data to validate the layout and interactions. | The reference interface is for design discussion, not the final Vue implementation. |
| W01 | 2026-09-30 | W06-UI-02 | Documented the main screens and expected interactions for review by the team. | 23:00-23:30 | 0.5 | done | [document or PR link] | R1, R5, R6 | The users were initially unclear; the team confirmed that collection-point staff and admins were the primary users. | No production frontend code was claimed in Week 1. |
| W02 | 2026-10-10 | T-00-02 / T-00-05 | Reviewed the R4 responsibilities, project plan, repository structure and current Sprint 0 issues. | 19:00-20:00 | 1.0 | done | [README](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/blob/develop/README.md), [CONTRIBUTING](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/blob/develop/CONTRIBUTING.md), [weekly plan](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/blob/develop/docs/05-management/plans/README.md) | R1 | Identified that requirements, design and API baselines are not yet merged, so formal frontend implementation should not start. | Aligned with the backend lead's Week 2 review slot. |
| W02 | 2026-10-10 | W05-W06-FE-PREP | Prepared the frontend prerequisites: ADR-003, page map and role access, component boundaries, API dependency list, mock-data strategy, and CI/development environment dependencies. | 20:00-21:30 | 1.5 | done | Issue #30, [PR #40](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/40) | R5, R6, R10 | The frontend cannot freeze API fields before R6 publishes the OpenAPI draft. I recorded these as dependencies instead of inventing an interface. | The 20:30-21:30 period overlaps with R6's backend preparation time. |
| W02 | 2026-10-11 | T-30-04 | Checked R6's backend design baseline in PR #38, aligned the frontend ADR/page map/API dependency list with the merged OpenAPI draft, and prepared the frontend skeleton issue and plan update. | 19:00-20:00 | 1.0 | done | [PR #38](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/38), [Issue #30](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/issues/30), [PR #40](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/40), [PR #41](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/41), docs/03-api/frontend-api-dependencies.md | R5, R6 | R6's API draft is still provisional, so I did not freeze field names. | PR #40 is merged; PR #41 is awaiting review. |

## AI use

OpenAI Codex was used for project-scope discussion, front-end prototype code and UI text, and drafting/refining logbook entries. AI output was reviewed before use. Project decisions remained with the team.

## Next planned work

- Wait for PR #41 review and R6's final API/OpenAPI confirmation.
- Start T-30-04 frontend skeleton after dependencies are approved.
- Coordinate with R5 on /login, /dashboard, shared layout and route guards.
- Review the approved page map before feature-page implementation.
