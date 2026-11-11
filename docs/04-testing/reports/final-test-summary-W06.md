# Final Test Summary — W06 T-60-08

## Evidence lock

- Final evidence commit: 3359c3942d739aa9372e31102152096448b5dd76
- CI run: 43009104923
- URL: https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/actions/runs/43009104923
- Result: success
- Jobs: sanity / commitlint / frontend / backend / docker-smoke — all success
- Superseded run: 41658317363 (commit b6011b5). It is stale because b6011b5..3359c39 includes backend code and test changes (ParcelShelvingIntegrationTest +193 lines, ParcelShelvingService, ParcelRepository, LocationRepository).
- Current develop HEAD: 3359c39 (#321 merged as 4716815 on 2026-11-11).

## Test counts (run 43009104923)

| Suite | Count | Result | Source |
|---|---|---|---|
| Backend test methods | 90 | 0 failures, 0 errors, 0 skipped | backend job log |
| Frontend tests | 68 (11 test files) | all passed | frontend job log |
| Testcontainers | 35 tests / 7 classes | all passed | surefire-reports artifact |

## Coverage (jacoco-report artifact, run 43009104923)

| Metric | Value | Raw |
|---|---|---|
| Line coverage | 88.79% | 808 / 910 |
| Branch coverage | 64.64% | 170 / 263 |

**Canonical coverage: 88.79% line / 64.64% branch.**  
`Coverage-Report3.md` (807/909 = 88.78%) is superseded and must not be cited as current.

## Acceptance

- Acceptance evidence: 3/3 rerun.
- Caveat: acceptance times are **estimated**, not measured.
- Manual walkthrough is not treated as acceptance PASS.

## Defect log

- `defect-log.md` v1.5: 6 defects reconciled; no open P0; no closed item without evidence.
- DEF-DOC-001: documentation evidence, not an automated test.
- DEF-UI-001: original cold-cache 68/68 local run is supplementary; automated evidence is the frontend job success in run 43009104923.

## Skipped tests

- None. Backend log: `Skipped: 0`. Frontend: 68 passed, 0 failed.

## Caveats

- DEF-DOC-001 is documentation evidence, not an automated test.
- DEF-UI-001 cold-cache 68/68 local run on 2026-11-03 is supplementary; automated evidence is the frontend job success in run 43009104923.
- Manual walkthrough is not acceptance PASS.
- Acceptance times are estimated, not measured.
- No 100% coverage or zero defects claim.

## Evidence links

- Final test summary: `docs/04-testing/reports/final-test-summary-W06.md`
- Defect log v1.5: `docs/04-testing/defect-log.md`
- One-page QA summary: `docs/04-testing/reports/w06-qa-summary.md`
- CI run: https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/actions/runs/43009104923
- jacoco-report artifact ID: 13850140493
- surefire-reports artifact ID: 13849906462
