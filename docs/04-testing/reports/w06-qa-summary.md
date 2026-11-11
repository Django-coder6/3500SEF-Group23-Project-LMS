# W06 QA Summary

Task: T-60-08 (#329)  
Owner: R8 Wu Kehao  
Date: 2026-11-12

## Evidence lock

- Code/evidence commit: `3359c3942d739aa9372e31102152096448b5dd76`
- CI run: [43009104923](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/actions/runs/43009104923)
- Result: success
- Jobs: sanity / commitlint / frontend / backend / docker-smoke

## Test numbers

| Suite | Result |
|---|---|
| Backend tests | 90 tests; 0 failures, 0 errors, 0 skipped |
| Frontend tests | 68 tests / 11 files; all passed |
| Testcontainers | 35 tests / 7 classes; all passed |
| Line coverage | 88.79% (808 / 910) |
| Branch coverage | 64.64% (170 / 263) |

## Acceptance

- Acceptance evidence: 3/3 rerun.
- Acceptance times are estimated, not measured.
- Manual walkthrough is not treated as an acceptance PASS.

## Defects and gaps

- `defect-log.md` v1.5: 6 defects reconciled; no open P0; no closed item without evidence.
- `DEF-DOC-001` is documentation evidence, not an automated test.
- `DEF-UI-001` cold-cache 68/68 local run is supplementary; automated frontend evidence is CI run `43009104923`.
- FR-11 `GET /parcels` is a Sprint 3 implementation gap (#289), not implemented.
- Resident read paths are contract-only under CR-013.

## Evidence

- [Final test summary](final-test-summary-W06.md)
- [Defect log](../defect-log.md)
- [Issue #329](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/issues/329)
- [PR #341](https://github.com/Django-coder6/3500SEF-Group23-Project-LMS/pull/341)
