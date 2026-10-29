# Test Coverage Report

| Field | Detail |
|---|---|
| Date | 2026-10-29 |
| Baseline Commit | `develop` (`512efc9`) |
| Tool | JaCoCo 0.8.12 |
| Command | `./mvnw -B verify` (CI artefact: `jacoco-report`) |

## 1. Measured Coverage

- **Line Coverage: 85.92%** (470 of 547 lines covered, 77 missed)
- Branch Coverage: **62%** (102 of 163 branches covered)
- Instruction Coverage: **87%** (2,060 of 2,355 instructions covered)

## 2. Module Breakdown & The "Missing Denominator"

| Package | Line Coverage | Status |
|---|:--:|---|
| `com.group23.auth` | 100% | Fully covered |
| `com.group23.config` | 100% | Fully covered |
| `com.group23.security` | 94.3% | Heavily tested |
| `com.group23.user` | 88.0% | Heavily tested |
| `com.group23.parcel` | 88.0% | **Newly added, heavily tested** |
| `com.group23.audit` | 83.3% | Tested |
| `com.group23.common` | 54.3% | Needs attention |
| `location`, `pickup` | — | **0 lines in build (Not yet implemented)** |

**The Catch:** The current total codebase is now **547 lines** (up from 400). The core business logic `parcel` has landed with strong test coverage (88%), which prevented the overall score from plummeting. However, `location` and `pickup` still contribute 0 lines to this report. The 85.92% is still skewed by foundational modules, and the denominator will grow further when the remaining core modules are merged.

## 3. Is the 60% target correct?

**Conclusion: The 60% target is currently met (85.92%), but the "denominator risk" has officially started to materialize.**

Here is the honest engineering assessment:

1. **The score dipped slightly.** As predicted, adding `parcel` increased the denominator by 147 lines, causing the overall line coverage to drop from 87.75% to 85.92%. Because `parcel` arrived with its own tests, the drop was minor. If `parcel` had 0% coverage, the total would have dropped below 70%.
2. **The remaining risk is `location` and `pickup`.** When these two modules are implemented, the total line count will likely exceed 800+. If they do not arrive with tests, the overall coverage will experience a severe drop. The 60% target will be truly tested in Sprint 3.
3. **Branch coverage remains the weak point.** At 62%, it is barely passing the threshold. The `common` module (30% branch coverage) and `audit` (62%) suggest that edge cases and conditional logic need more negative testing.

## 4. Recommendations

- **Keep the 60% CI threshold** as a hard floor. It acts as a safety net.
- **Do NOT artificially inflate the numbers.** We explicitly reject writing trivial tests just to pad the coverage score.
- **Aspirational Goal:** Target **80% line** and **70% branch** coverage overall.
- **Action for Sprint 3:** The `location` and `pickup` modules must arrive with their own integration tests (similar to how `parcel` did in #250) to keep the score above 60%.

---
*Report generated based on JaCoCo `jacoco.xml` and HTML report attached to CI build for commit `1c8e488`.*
