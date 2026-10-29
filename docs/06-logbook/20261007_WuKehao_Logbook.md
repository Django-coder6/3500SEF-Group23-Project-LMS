# Personal Logbook - WuKehao
- Student ID: 14612273
- GitHub: fannaodawang
- Role: R8 QA Lead
- Modules: test plan, test cases, defect management, test reporting
- Period covered: W01-W08
## Weekly log

| Week | Date | Task | Description | Time | Hours | Status | Evidence | With | Problem and fix | Note |
|---|---|---|---|---|---|---|---|---|---|---|
| W01 | 2026-10-07 | T-00-05 | Read CONTRIBUTING.md and completed the first pull request | 20:00-20:30 | 0.5 | done | https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/22#issue-5732319882 | - | - | The branch and pull request workflow was new |
| W02 |  2026-10-15 | T-50-19 | Create a Form for the Defect Reporting System | 00:00-01:27 | 1.45 | done | docs/04-testing/defect-log.md | - | - | - |
| W02 |  2026-10-16 | T-50-19 | Add the defect log and test-case index, and rebuild the logbook | 00:00-02:00 | 2 | done | https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/152#issue-5871154392 | R1 | - | - |
| W02 |  2026-10-17 | T-50-19 | Add a sentence to Section 11 of `test-plan.md`: where the report is generated and who reads it | 23:58-00:34| 0.6 | done | docs/04-testing/test-plan.md | - | - | - |
| W02 |  2026-10-17 | T-50-19 | Edit test-cases/README.md, logbook| 13:00-14:00| 1 | done | docs/04-testing/test-cases/README.md | - | Corrected errors in the logbook and added some notes to test-cases/README.md | - |
| W02 |  2026-10-17 | T-50-19 | Create RTM.md | 19:33-22:40| 3.12 | done | docs/01-requirements/RTM.md | - | - | - |
| W02 |  2026-10-18 | T-50-19 | Modify RTM.md and test-cases/README.md and logbook | 23:39-00:37 | 0.97 | done | docs/01-requirements/RTM.md docs/04-testing/test-cases/README.md| - | - | - |
| W02 |  2026-10-19 | T-50-19 | Modify RTM.md and test-cases/README.md and logbook | 01:43-01:53 | 0.17 | done | docs/01-requirements/RTM.md docs/04-testing/test-cases/README.md| - | - | - |
| W02 |  2026-10-19 | T-50-19 | Modify RTM.md and test-cases/README.md and logbook | 02:20-02:31 | 0.18 | done | docs/01-requirements/RTM.md docs/04-testing/test-cases/README.md| - | - | - |
| W03 |  2026-10-23 | T-42-08 | README added TC-PARC-006/007; TC-PARC-008 (blocked). RTM FR-18 updated; README/RTM bumped to v1.1/v0.4.) | 23:37-00:00 | 0.38 | done | docs/01-requirements/RTM.md docs/04-testing/test-cases/README.md| - | - | - |
| W03 |  2026-10-24 | T-42-08 | Closed #171 by reserving identifiers for the two new FR-18 scenarios in the RTM and listing them in test-cases/README.md with a level and a sprint, leaving no TBD in the Level column and marking the duplicate-waybill row as blocked on R6's ruling rather than leaving it empty | 00:07-01:00 | 0.89 | done | PR #193, issue #171| R3, R6 | The duplicate-waybill case could not be specified until the contract ruled on it, so the honest option was to write "blocked on R6" rather than guess a level or leave the cell blank | Four acceptance criteria, all met; the blocked row was resolved once CR-026 landed |
| W03 |  2026-10-24 | T-41-04 / #182 | Reviewed the four TC-AUTH cases against the acceptance criteria and registered them in the RTM, then closed #182. The tests themselves landed the following day in R6's #201| 13:30-14:17 | 0.78 | done | PR #193, PR #201 | R6 | #182 was closed on the strength of the test-case registration, but three of its seven criteria were not met at that point: the methods are not named after their identifiers, jacoco.xml is not produced because no JaCoCo plugin is bound to verify, and the RTM names a directory rather than a test class. The first is arguably a better choice than the criterion assumed; the other two are still open | Recorded rather than left as a closed issue that reads as complete |
| W03 |  2026-10-26 | T-41-04 | Updated the RTM to v0.4 (#171: FR-18 links TC-PARC-006/007 and blocked TC-PARC-008; GAP 13 added for #168; reserved identifier count corrected to 62) and test-cases/README.md to v1.1 (added TC-PARC-006/007/008) | 22:32-23:32 | 1.00 | done | PR #193| - | The RTM still names backend/.../security/ rather than AuthSecurityIntegrationTest for the four TC-AUTH rows, and FR-19/FR-20 still say Should (approved — CR-004) instead of Could (approved — CR-023) | Follow-up v0.5 and the class-name substitution are still owed|
| W04 |  2026-10-28 | T-42-08 | Measure coverage and generate reports | 22:47-23:40 | 0.88 | done | docs/04-testing/reports/Coverage-Report1.md| - | - | - |
| W04 |  2026-10-29 | T-42-08 | test-plan.md updated to v0.5 | 00:03-00:12 | 0.15 |done| docs/04-testing/test-plan.md | - | - | - |
| W04 | 2026-10-29 | T-42-08 | Writing a test framework | 14:17-14:53 | 0.6 | done | PR #249 | - | - | The framework was written against src/main/java by mistake, which puts JUnit on the production classpath and fails the build. It is withdrawn here and returns with T-42-03 and T-42-04, the two packages it needs |

---

