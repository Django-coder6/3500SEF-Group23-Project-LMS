# Weekly evidence

## COMP 3500SEF Group 23

| Item | Detail |
|---|---|
| Owner | R1 |
| Task | T-00-06 |
| Status | Active |

This folder holds the weekly contribution screenshot. The project plan asks for
one screenshot per week, taken before the Sunday 21:00 logbook deadline, so that
the written logbooks can be checked against an independent record of who
actually committed.

## File naming

```
docs/05-management/weekly/YYYYMMDD_contributors-N.jpg
```

The date is the day the screenshot was taken, not the last day of the week. A
screenshot taken on Monday the 12th, covering the week that ended on the 11th,
is named `20261012_contributors-1.jpg`, because that is when the evidence was
captured.

`N` numbers the screenshots taken in that session. With ten members the
Contributors panel runs to more than one screen, so two images are expected
rather than one.

If a second capture is taken in the same week, for example after a late merge,
add a descriptive suffix:

```
20261012_contributors-1.jpg
20261012_contributors-after-merge-1.jpg
```

Lower case, hyphens, no spaces, as required by `CONTRIBUTING.md`.

### On the file extension

These files are JPEG images. They are named `.jpg` rather than `.png` so that
the extension matches what the file actually is. Naming a JPEG `.png` makes the
extension a lie, and a reader who opens the file properties will notice. The
extension is not what makes a file usable; the format is.

## How to take it

1. Open the repository on GitHub.
2. Go to **Insights**, then **Contributors**.
3. Confirm the date range shown covers the week being recorded.
4. Capture the full panel, including every member.
5. Commit before Sunday 21:00.

## What the screenshot is evidence of, and what it is not

A screenshot taken at a moment in time proves three things and no more.

| It does prove | It does not prove |
|---|---|
| That all ten members were active in the repository during the period shown | That the person who committed is the person who did the thinking |
| That the commit volume is real, and can be counted independently of the logbooks | That two members' contributions are comparable in value |
| That the work was spread across the team rather than concentrated in one pair of hands | That a low commit count means a low contribution |

The figures on the Contributors panel count commits to the default branch. A
merge commit counts once. Rebased and squashed commits are folded into their
result, so the panel number is lower than a tally of every push. The panel and a
raw commit count will therefore never agree exactly, and that is expected rather
than a discrepancy to chase.

## Index

| Week | Files | Date captured | Period covered |
|---|---|---|---|
| W01 | `20261012_contributors-1.jpg`, `20261012_contributors-2.jpg` | 2026-10-12 | 2026-09-28 to 2026-10-12 |
| W02 | `20261021_contributors-1.jpg`, `20261021_contributors-2.jpg` | 2026-10-21 | 2026-10-12 to 2026-10-19 |
| W03 | `20261028_contributors-1.jpg`, `20261028_contributors-2.jpg` | 2026-10-28 | 2026-10-19 to 2026-10-28 |

### What the W01 screenshot records

| Rank | Account | Member | Commits in the period shown |
|---|---|---|---|
| 1 | Django-coder6 | R1 Liu JiaHao | 18 |
| 2 | RookieVENENO | R4 Yuan Chong Jun | 15 |
| 3 | Christine049 | R5 Tan Yuan Ting | 7 |
| 4 | ohcyc246 | R2 Chan Yin Cho | 6 |
| 5 | smcheese9731-sys | R10 Chan Sung Ming | 4 |
| 6 | 123dvfj123 | R6 Hu Qing Kai | 4 |
| 7 | RavenG7 | R9 Guo Yuhui | 3 |
| 8 | Kennethli13 | R3 Li Cheuk Fung | 2 |
| 9 | fannaodawang | R8 Wu Kehao | 1 |
| 10 | Allen06914 | R7 Huang Wei Jia | 1 |

All ten members appear. This is the figure the minutes quote when they say that
every member shipped work in week one.

The lowest counts belong to the two members who had no issues assigned in week
one, and to the member who was absent from the week 1 meeting. A low count here
is a statement about the plan, not about the people. See the column on the right
in the table above.

### What the W02 screenshot records

Captured on **2026-10-21**, covering **2026-10-12 to 2026-10-19**. The two images were taken in
one session, because ten members do not fit in one screen.

| Rank | Account | Member | Commits in the period shown | Insertions | Deletions |
|---|---|---|---|---|---|
| 1 | RookieVENENO | R4 Yuan Chong Jun | 58 | 8,834 | 694 |
| 2 | Django-coder6 | R1 Liu JiaHao | 44 | 3,131 | 353 |
| 3 | fannaodawang | R8 Wu Kehao | 42 | 550 | 168 |
| 4 | ohcyc246 | R2 Chan Yin Cho | 21 | 414 | 146 |
| 5 | smcheese9731-sys | R10 Chan Sung Ming | 20 | 297 | 127 |
| 6 | 123dvfj123 | R6 Hu Qing Kai | 17 | 1,840 | 189 |
| 7 | Allen06914 | R7 Huang Wei Jia | 11 | 733 | 11 |
| 8 | RavenG7 | R9 Guo Yuhui | 8 | 275 | 58 |
| 9 | Christine049 | R5 Tan Yuan Ting | 6 | 294 | 78 |
| 10 | Kennethli13 | R3 Li Cheuk Fung | 6 | 429 | 51 |

**Totals: 233 commits, 16,797 insertions, 1,875 deletions.** Every member appears.

#### This capture is two days late, and why that is not the thing the note below warns against

The deadline was Sunday 19 October at 21:00. The screenshots were taken on Tuesday 21 October.

The note at the foot of this file says not to go back and retake an earlier week's screenshot.
That rule protects the evidence from being quietly improved after the fact. It does not forbid a
late capture, because the Contributors panel buckets commits into weeks and **a closed week's
bucket cannot change afterwards**: the bar labelled 12 to 19 October read on the 21st is the same
bar that would have been read on the 19th. Nothing has been added to it, and nothing can be.

What the rule does require is that the real capture date appears above. It does, so a reader can
see for themselves that the evidence was gathered late rather than assume it was gathered on time.

The gap this leaves is not in the figures but in the process: the weekly capture happened after
the logbook deadline it exists to check. That is worth saying in the week 3 stand-up, because the
screenshot is only independent evidence if it predates the thing it verifies.

#### What moved between the two weeks

| | W01 | W02 | Change |
|---|---|---|---|
| Commits from all ten members | 61 | 233 | **nearly four times as many** |
| Highest single count | 18 (R1) | 58 (R4) | |
| Lowest single count | 1 (R7, R8) | 6 (R3, R5) | **the floor rose sixfold** |
| Members below 5 commits | 6 | 0 | |

The floor is the more interesting number. In week 1 six members committed fewer than five times,
because the plan had given them little to do; in week 2 nobody did. The lowest count is now six
commits, and it belongs to two members whose week 2 work was review rather than authorship.

**Rank order changed a great deal, and that is not a ranking of people:**

| Member | W01 rank | W02 rank | What changed |
|---|---|---|---|
| R4 Yuan Chong Jun | 2 | 1 | the frontend skeleton and the journey maps are large single-file additions |
| R1 Liu JiaHao | 1 | 2 | coordination, merges and the management documents |
| R8 Wu Kehao | 9 | 3 | from 1 commit to 42: the traceability matrix, the test plan, the test-case register and three review rounds |
| R2 Chan Yin Cho | 4 | 4 | the product backlog and the requirement confirmation |
| R10 Chan Sung Ming | 5 | 5 | Docker Compose, the CI pipeline and the tooling |
| R6 Hu Qing Kai | 6 | 6 | the ERD, the contract and the architecture records |
| R7 Huang Wei Jia | 10 | 7 | from 1 commit to 11 |
| R9 Guo Yuhui | 7 | 8 | the persona review and the UI review |
| R5 Tan Yuan Ting | 3 | 9 | 7 commits to 6: the same volume, a lower rank because everyone else rose |
| R3 Li Cheuk Fung | 8 | 10 | 2 commits to 6: up in volume, down in rank for the same reason |

**R8 going from the bottom of the table to third is the clearest signal in the data**, and it is
the one the logbooks should be checked against: a member who commits once in week 1 and forty-two
times in week 2 has either been given work that finally suited them, or was under-recorded in
week 1. Both are worth knowing.

**R5 and R3 moved down without doing less.** R5 committed six times against seven, and R3 went
from two to six. They fell because the rest of the team's volume rose, which is exactly why the
table at the foot of this file says a low count is a statement about the plan, not about the
person.

#### Reading these numbers honestly

The three qualifications in the table below still apply, and one of them matters more this week
than last.

- **The insertion count is dominated by two members.** R4 alone accounts for 8,834 of 16,797
  insertions, and R6 for 1,840, mostly in the ERD and the contract. A generated file, a large
  document or a lockfile can produce thousands of insertions in one commit. Insertions measure
  how much text was written, not how much thought went into it.
- **A merge commit counts once, and squashed work is folded into its result**, so these figures
  are lower than a tally of every push. The panel and `git log` will never agree exactly.
- **Nothing here says the person who committed is the person who did the thinking.** The logbooks
  are the record of that, and the two are meant to be read together: this folder is the
  independent half.

### What the W03 capture records

Captured on **2026-10-28**, covering **2026-10-19 to 2026-10-28**. Two images, one session.

#### This week the record is the pull requests, not the panel

**Twenty pull requests were merged between 19 and 28 October, and every one of the ten members merged
at least one.** The panel cannot say that this week, so the table below does.

| Member | Pull requests merged | What they carried |
|---|---|---|
| **R1** Liu JiaHao | `#219`, `#220`, `#231` | the week 3 logbook; `RSK-11` closed with the `docker-smoke` evidence; the close-out that deferred `T-42-02` and settled how a record number is taken |
| **R2** Chan Yin Cho | `#226` | the missing W03 scope-ruling row, after `#196` |
| **R3** Li Cheuk Fung | `#222`, `#223` | the week 1–2 logbook records; `CR-023`–`CR-025` applied to the SRS |
| **R4** Yuan Chong Jun | `#207`, `#221` | the Sprint 2 finding outcomes reconciled; the report's delivered-status correction |
| **R5** Tan Yuan Ting | `#202`, `#205`, `#218` | login, JWT session and route guards; the location board and the staff intake screen; the week 3 logbook |
| **R6** Hu Qing Kai | `#197`, `#201` | the backend and full-stack CI jobs; login, JWT and four-role RBAC |
| **R7** Huang Wei Jia | `#198`, `#206` | the Flyway baseline and seed data; the audit interceptor with phone masking |
| **R8** Wu Kehao | `#229` | JaCoCo bound to `verify`, the `jacoco-report` artefact, `RTM.md` to v0.5 |
| **R9** Guo Yuhui | `#224`, `#227`, `#228` | chapter 1 at 128 words; chapter 2 at 146; the W03 logbook rows |
| **R10** Chan Sung Ming | `#230` | Checkstyle, Spotless, commitlint, the Git hooks and the `commitlint` CI job |

**A pull request's author is whoever opened it, recorded against their account.** That is why this
table can say every member delivered while the panel cannot — the two are counting different things.

#### What the panel showed, and why it disagrees with the table above

| Rank | Account | Member | Commits in the period shown | Insertions | Deletions |
|---|---|---|---|---|---|
| 1 | fannaodawang | R8 Wu Kehao | 37 | 233 | 124 |
| 2 | Django-coder6 | R1 Liu JiaHao | 19 | 1,634 | 237 |
| 3 | ohcyc246 | R2 Chan Yin Cho | 18 | 283 | 197 |
| 4 | RavenG7 | R9 Guo Yuhui | 14 | 98 | 116 |
| 5 | RookieVENENO | R4 Yuan Chong Jun | 9 | 1,504 | 165 |
| 6 | 123dvfj123 | R6 Hu Qing Kai | 5 | 2,134 | 9 |
| 7 | Kennethli13 | R3 Li Cheuk Fung | 4 | 76 | 11 |
| 8 | smcheese9731-sys | R10 Chan Sung Ming | 1 | 2,047 | 0 |
| 9 | Christine049 | R5 Tan Yuan Ting | 1 | 11 | 5 |
| — | **Allen06914** | **R7 Huang Wei Jia** | **does not appear** | — | — |

**Totals for the nine rows above: 108 commits, 8,020 insertions, 864 deletions.**

**The panel counts commits, and it attributes a commit to an account only when the author email is an
address on that account.** Two members committed from a `users.noreply.github.com` address and their
rows are complete. The rest are not, and the reason is visible in the commits themselves.

#### R7's two pull requests contain commits that belong to no account

`#206` holds **one** commit. `#198` holds two. Their authors:

| Pull request | Commit | Author as recorded | Attributed to `@Allen06914` |
|---|---|---|---|
| `#206` | `9bbc3da` | `HWeijia <hwj@HWeijiadeMacBook-Pro.local>` | **no** — a machine-local address, which can never be verified |
| `#198` | `c980572` | `HWeijia <hwj@HWeijiadeMacBook-Pro.local>` | **no** — same address |
| `#198` | `fb1781a` | `Django <s1366245@live.hkmu.edu.hk>` | **counted, but for R1** — it is the merge of `develop` into the branch so CI could run |

**So R7's two pull requests add nothing to his panel row and one commit to R1's.** The panel has counted
his work, and the tally landed on the wrong person.

`git log` on `develop` records **thirteen commits** under his work, across two identities:

| Author name in git | Author email | Commits | Recoverable |
|---|---|---:|---|
| `Momoka17` | `hwj914@outlook.com` | 11 | **yes** — add the address to the account and the history fills in |
| `HWeijia` | `hwj@HWeijiadeMacBook-Pro.local` | 2 | **no** — a machine-local address is not a mailbox |

**The two unrecoverable ones are the Flyway baseline and the audit interceptor**, because those are
the two he committed from a laptop that had its hostname as the git address.

#### The same cause understates three other rows

| Member | Panel | Author identities in git | What the gap is |
|---|---:|---|---|
| R7 | **absent** | `Momoka17` 11, `HWeijia` 2 | neither email is on the account |
| R5 | 1 | `Christine049` 13, `Christine-tan49` 10 | two identities under two addresses |
| R6 | 5 | `Hu Qingkae` 23 | the name differs from the account; the email is the question |
| R1 | 19 | `Django` 45, `Django-coder6` 23 | one address, two display names — this one does attribute |

**Do not read these rows as the contribution.** R5's row of one commit and eleven insertions sits
beside `#202` (login, JWT session and route guards) and `#205` (location board and staff intake),
both merged by her. The panel counted the commits it could attribute, not the work that was done.

#### The fix, and what it cannot fix

**The fix is one setting per person**, and it applies backwards, because attribution is recomputed
from the commit email:

1. Open **Settings**, then **Emails**, and add the address you commit with.
2. For the future, commit from the `users.noreply.github.com` address, which is always attributed.

**Two of R7's commits cannot be recovered** — `hwj@HWeijiadeMacBook-Pro.local` is a machine-local
address, not a mailbox, so no account can claim it. The eleven from the outlook address can be
claimed the moment it is added.

**If a second capture is taken after that, name it with the suffix this file already defines:**

```
20261028_contributors-after-email-fix-1.jpg
```

**Both captures stay.** The first records what the panel showed on the 28th; the second records a
correction. Neither is a reconstruction, which is what the rule at the foot of this file forbids.

#### This capture is three days late, and the same reasoning applies as for W02

The logbook deadline was Sunday 25 October at 21:00. The screenshots were taken on Wednesday 28
October. The bars labelled 19 and 26 October are closed buckets and cannot gain commits afterwards,
so reading them late reads the same figures that reading them on the 25th would have. What the rule
requires is that the real capture date is recorded, which it is.

**The process gap is now two weeks running.** The screenshot exists to check the logbooks against an
independent record, and it only does that if it is captured before the deadline it checks. Two
consecutive late captures is the thing to raise at the week 4 stand-up; the figures are unaffected,
the independence is.

## A note on not re-taking an old screenshot

A screenshot of a live panel is a dated artefact, not a maintained document. Do
not go back and retake an earlier week's screenshot to make the numbers look
better; the point of the evidence is that it was captured at the time.

If a screenshot was genuinely missed, record the gap in the index above rather
than reconstructing it.
