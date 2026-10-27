# Personal logbook

| Item | Detail |
|---|---|
| Task | T-00-05 |
| Owner | R1 |
| Version | v1.1 |
| Status | Active |

This file is the template. Copy it, keep the header fields and the eleven
columns, and replace the placeholders.

## Naming

    YYYYMMDD_Name_Logbook.md

The date is the day you created the file, and it must be a real date. For example
`20261002_LiuJiaHao_Logbook.md`.

`20260000_Name_Logbook.md` is **not** a valid name. `000000` is not a date; it
sorts the file to the top of the folder and reads as a placeholder that was never
finished. Two files were created with it before this line was written.

## Period covered

Every logbook covers the whole project, so the line is the same in all of them:

    - Period covered: W01-W08

**Eight weeks, not sixteen.** `PLAN.md` says "Eight weeks, five sprints", and rule
3 below says the same. A logbook that says `W16` or `W8` is wrong; the period
starts at week 1 and ends at week 8.

## Rules

1. Write the entry within 48 hours of doing the work, and push it. The commit
   timestamp is what shows the record was written at the time rather than at
   the end of the semester.
2. A record without a link is not evidence. Every entry needs a pull request,
   commit, issue or document path.
3. No empty weeks. From week 1 to week 8, any week with work in it needs at
   least one row.
4. Update your file before 21:00 every Sunday.
5. Every row carries a task ID. A row with an empty task column cannot be checked
   against `PLAN.md`.

## Template

```markdown
# Personal logbook - Liu JiaHao (R1, project manager)

- Student ID: 13000000
- GitHub: @Django-coder6
- Modules: M1 users and permissions, M0 management
- Period covered: W01-W08

## Weekly log

| Week | Date | Task | Description | Time | Hours | Status | Evidence | With | Problem and fix | Note |
|---|---|---|---|---|---|---|---|---|---|---|
| W08 | 2026-11-23 | T-00-07 | Closed the remaining report chapters and checked every figure caption against its figure | 19:00-22:00 | 3.0 | done | PR #57 | reviewed by R8 | two figures shared a caption number, so a reader could not tell which figure a sentence described | renumbered the captions by chapter and added the check to the consistency list |
```

The example date is inside week 8 of this project, which runs from 2 October, so
the row can be copied and filled in rather than invented.

## Revision history

| Version | Date | Change | Author |
|---|---|---|---|
| v1.0 | Week 1 | The template as ten logbooks were copied from it: `W01-W16`, a lower-case `name` in the pattern, a `000000` example date, and an example row dated 2026-04-14 | R1 |
| v1.1 | Week 2 | Period corrected to `W01-W08` in its own section that names the two wrong forms, the name pattern aligned with `CONTRIBUTING.md`, the example made a real date, `000000` called out as invalid, rule 5 added for task IDs, and a version header with this history added so the check covers the file | R1 |

## Manager spot checks

At the end of each sprint the project manager picks two logbooks at random and
checks that the links in them exist. The result goes into the meeting minutes
under `docs/05-management/minutes/`.
