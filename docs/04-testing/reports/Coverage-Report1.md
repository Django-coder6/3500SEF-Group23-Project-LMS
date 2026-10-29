# Test Coverage Report

| Field | Detail |
|---|---|
| Date | 2026-10-28 |
| Baseline Commit | `develop` (`0fdb727` + #229) |
| Tool | JaCoCo 0.8.12 |
| Command | `./mvnw -B verify` (CI artefact: `jacoco-report`) |

## 1. Measured Coverage

- **Line Coverage: 87.75%** (351 of 400 lines covered, 49 missed)
- Branch Coverage: **62%** (75 of 120 branches covered)
- Instruction Coverage: **89%** (1,476 of 1,654 instructions covered)

## 2. Module Breakdown & The "Missing Denominator"

| Package | Line Coverage | Status |
|---|:--:|---|
| `com.group23.auth` | 100% | Fully covered |
| `com.group23.config` | 100% | Fully covered |
| `com.group23.security` | 94.6% | Heavily tested |
| `com.group23.user` | 88.5% | Heavily tested |
| `com.group23.audit` | 80.3% | Tested (largest current module) |
| `parcel`, `location`, `pickup` | — | **0 lines in build (Not yet implemented)** |

**The Catch:** The current total codebase is only **400 lines**. The core business logic (`parcel`, `location`, `pickup`) currently contributes 0 lines to this report because those modules are not yet in the compiled build. The 87.75% is heavily skewed by the foundational modules (Auth, Security, User, Audit) which are well-tested.

## 3. Is the 60% target correct?

**Conclusion: The 60% target is mathematically conservative today, but practically necessary for tomorrow.**

Here is the honest engineering assessment:

1. **Right now, 60% is too low.** We have achieved 87.75% line coverage. If we only hit 60% in the future, we would be regressing significantly in the currently well-tested modules.
2. **The real risk is the denominator.** When `parcel`, `location`, and `pickup` are merged (which will likely add hundreds of lines), if we do not write tests for them, the overall coverage will plummet below 60%. 
3. **Branch coverage is the weak point.** At 62%, we are barely passing our threshold. This indicates that edge cases in `audit` (56%) and `user` (58%) are not fully tested. 

## 4. Recommendations

- **Keep the 60% CI threshold** as a hard floor. It acts as a safety net to prevent future regressions when core modules are added.
- **Do NOT artificially inflate the numbers.** We explicitly reject writing trivial tests for `UserRole` getters or enums just to pad the coverage score. The metric must reflect genuine functional testing.
- **Aspirational Goal:** Target **80% line** and **70% branch** coverage once the `parcel` and `pickup` modules are integrated. 
- **Action for R9 / Sprint 3:** When core modules are merged, they must arrive with their own integration tests to keep the score above 60%.

---
*Report generated based on JaCoCo `jacoco.xml` and HTML report attached to CI build for commit `0fdb727`.*
