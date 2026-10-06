# Personal logbook

## Naming

    YYYYMMDD_name_Logbook.md

For example `20260000_ZhangSan_Logbook.md`.

## Rules

1. Write the entry within 48 hours of doing the work, and push it. The commit
   timestamp is what shows the record was written at the time rather than at
   the end of the semester.
2. A record without a link is not evidence. Every entry needs a pull request,
   commit, issue or document path.
3. No empty weeks. From week 1 to week 8, any week with work in it needs at
   least one row.
4. Update your file before 21:00 every Sunday.

## Template

```markdown
# Personal logbook - Zhang San (R7, backend developer)

- Student ID:
- GitHub: @zhangsan
- Modules: M3 location management, M7 notifications, M9 stocktake
- Period covered: W01-W16

## Weekly log

| Week | Date | Task | Description | Time | Hours | Status | Evidence | With | Problem and fix | Note |
|---|---|---|---|---|---|---|---|---|---|---|
| W08 | 2026-04-14 | T-42-03 | Implemented the storage location state machine and rejected illegal transitions | 19:00-22:00 | 3.0 | done | PR #57 | reviewed by R8 | two rows could claim the same location under load | moved the check into a transaction and added a unique constraint |
```

## Manager spot checks

At the end of each sprint the project manager picks two logbooks at random and
checks that the links in them exist. The result goes into the meeting minutes
under `docs/05-management/minutes/`.
