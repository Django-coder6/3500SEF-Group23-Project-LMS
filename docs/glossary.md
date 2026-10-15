# Glossary

## Community Parcel Collection Point Management System

| Item | Detail |
|---|---|
| Task | T-00-07 |
| Owner | R9, reviewed by R2 |
| Version | v1.2 |
| Status | Active |

This glossary is the single source of truth for vocabulary in the project. It is used by the code, the commit messages, the documents and the report.

**How to use it.** Before naming a class, a field, a page, a heading or a diagram, check the term here. If the term you need is not listed, add it to this file in the same pull request that introduces it.

Terms are grouped by kind. Each entry gives the English term, the Chinese equivalent used in conversation, and what it means in this project.

---

## 1. Domain terms

These are the words that describe what the system does. They appear in the requirements, the database and the user interface.

| English | Chinese | Meaning in this project |
|---|---|---|
| collection point | 代收點 | A place where parcels are received from couriers and handed to residents. One collection point is one site. The system supports several. |
| storage location | 庫位 | A numbered position on a shelf where one parcel waits to be collected. It is a database record, not a physical locker. |
| zone | 分區 | A named group of storage locations, for example large-item, small-item, perishable or long-stay. |
| shelving | 上架 | Placing a parcel at a storage location after it has been recorded at the counter. |
| handover | 核銷 | Confirming that a parcel has been given to the correct resident, or to a person the resident authorised. |
| parcel | 包裹 | An item addressed to a resident and held at a collection point. |
| waybill number | 運單號 | The tracking number printed by the courier company. Recognised by its prefix. |
| carrier | 快遞公司 | The company that delivered the parcel, for example SF or ZTO. |
| pickup code | 取件碼 | A short code issued to the resident and used to verify a handover. It is one-time use. |
| delegated collection code | 代取授權碼 | A one-time code a resident can forward to another person so that person can collect the parcel. |
| retention period | 免費保管期 | How many days a parcel may be kept before an overdue fee starts. Three days by default, configurable per point. |
| overdue parcel | 逾期包裹 | A parcel held longer than the retention period. |
| overdue fee | 超期費用 | The amount charged once a parcel passes the retention period. |
| exception parcel | 異常件 | A parcel that cannot follow the normal flow. The exception types are listed in section 1.3. |
| exception ticket | 異常件工單 | The record that tracks one exception parcel from report to resolution. |
| stocktake | 盤點 | Counting what is physically on the shelves and comparing it with the system record. |
| difference | 盤點差異 | A stocktake result where the shelf and the record disagree, either missing or unrecorded. |
| receiver sheet | 交接清單 | A list of parcels delivered by a courier in one drop. |
| intake | 收件入庫 | The whole step of recording a parcel at the counter and assigning it a storage location. |

### 1.1 Parcel statuses

These are the allowed values for the parcel status field. They must match the state machine, the ERD, and the API contract.

| English | Chinese | Meaning |
|---|---|---|
| registered | 已登記 | The parcel has been recorded at intake but not yet placed on a shelf. |
| shelved | 已上架 | The parcel has been assigned a storage location and is ready for pickup. |
| collected | 已取件 | The parcel has been handed over to the resident. |

### 1.2 Storage location statuses

These are the allowed values for the storage location status field.

| English | Chinese | Meaning |
|---|---|---|
| available | 可用 | The storage location is empty and can accept a parcel. |
| full | 已滿 | The storage location currently holds a parcel. |
| blocked | 封鎖 | The storage location is out of service and must not be used. |

### 1.3 Exception types

These are the allowed values for the exception type field. This list is the single source of truth; other documents must reference it, not restate it (CR-012).

| English | Chinese | Meaning |
|---|---|---|
| overdue | 逾期 | The parcel has remained uncollected beyond the allowed period. |
| damaged | 損毀 | The parcel was received or found damaged. |
| wrong_recipient | 錯領 | The parcel was handed to or claimed by the wrong person. |
| refused | 拒收 | The resident refused to accept the parcel. |
| lost | 遺失 | The parcel cannot be located at the collection point. |
| other | 其他 | Any exception that does not match the categories above. |

---

## 2. Roles

The system has exactly four login roles. Anything else is an external actor, not a system role.

| English | Chinese | Meaning in this project |
|---|---|---|
| resident | 居民 | The person a parcel is addressed to. Uses the self-service pages. |
| staff | 代收點員工 | Works at the counter. Records parcels, shelves them and hands them over. |
| owner | 站點老闆 | Owns one or more collection points. Sees figures and manages staff accounts for those points. |
| system administrator | 系統管理員 | Maintains the whole system: all users, all collection points, configuration limits. |

### External actors

These people interact with the collection point but do not log in. They must not be given system accounts.

| English | Chinese | Note |
|---|---|---|
| courier | 快遞員 | Delivers parcels in bulk to the counter. Has no account. The staff member records the parcels. |
| the person collecting | 代取人 | Somebody collecting on behalf of a resident, using a delegated collection code. Not a login role. |

In a small collection point one person may hold both the owner and the staff role. The permissions are combined in that case.

---

## 3. System and engineering terms

| English | Chinese | Meaning in this project |
|---|---|---|
| requirement | 需求 | Something the system must do or must have. Every requirement has an identifier, for example FR-19 or NFR-4. |
| functional requirement | 功能需求 | A behaviour the system must provide. Prefixed FR. |
| non-functional requirement | 非功能需求 | A measurable quality target, for example a response time. Prefixed NFR. |
| MoSCoW | MoSCoW 優先級 | The priority scheme: must have, should have, could have, will not have this time. |
| backlog | 待辦清單 | The ordered list of work not yet done. |
| acceptance criteria | 驗收標準 | The condition that must hold for a task to count as complete. |
| traceability matrix, RTM | 需求追蹤矩陣 | The table that maps each requirement to its design, code and test cases. |
| use case | 用例 | A description of one interaction between an actor and the system, with its primary flow and exceptions. |
| sprint | 迭代 | A fixed block of work. This project runs for eight weeks in five sprints: Sprint 0 and Sprint 1 are one week each, Sprint 2 to Sprint 4 are two weeks each. |
| stand-up | 站會 | The short regular meeting where each person reports progress and blockers. |
| retrospective | 回顧 | The short meeting at the end of a sprint where the team agrees what to change. |
| increment | 增量 | The working part of the system produced by one sprint. |
| definition of done | 完成定義 | The five conditions in the project plan that make a task count as finished. |
| risk register | 風險登記冊 | The list of things that could go wrong, with owners and triggers. |
| change log | 變更日誌 | The record of every change to scope, requirements or design, with its reason. |
| requirement tracking | 需求追溯 | Being able to go from a commit message back to the requirement it implements. |
| idempotent | 冪等 | Running the same operation twice has the same result as running it once. Handover must behave this way. |
| optimistic locking | 樂觀鎖 | Detecting a conflict by comparing a version number instead of locking the row. Used for parcel status changes. |
| audit log | 操作審計日誌 | An append-only record of who changed which parcel or storage location, and when. |
| RBAC | 角色權限控制 | Role-based access control. What a user may do depends on their role. |
| seed data | 種子資料 | The sample data loaded into a fresh database so the system can be demonstrated. |
| migration | 資料庫遷移 | A versioned script that changes the database structure. Applied by Flyway. |
| design system | 設計系統 | The single source of truth for the interface: the tokens and component rules in docs/02-design/design-system.md. |
| design token | 設計變數 | A named visual value (colour, size, spacing, radius). A page uses tokens, never raw values. |

---

## 4. Technology terms

The stack is fixed by ADR-003 and ADR-005. Use these exact names.

| English | Chinese | Note |
|---|---|---|
| Vue 3 | Vue 3 | Frontend framework. Do not write "VueJS". |
| Vite | Vite | Frontend build tool. |
| Pinia | Pinia | Frontend state management. |
| Element Plus | Element Plus | Frontend component library. Do not write "Element UI". |
| Java 17 | Java 17 | Backend language version. Always state the version. |
| Spring Boot | Spring Boot | Backend framework. Do not write "SpringBoot". |
| Spring Data JPA | Spring Data JPA | Persistence layer. |
| Hibernate | Hibernate | The JPA implementation underneath. |
| Flyway | Flyway | Database migration tool. |
| PostgreSQL | PostgreSQL | Database. Version 16. Do not write "Postgres" in documents. |
| Redis | Redis | Cache, used for pickup code checks. |
| Spring Security | Spring Security | Authentication and authorisation. |
| JWT | JWT | JSON Web Token, used for the session. |
| SpringDoc OpenAPI | SpringDoc OpenAPI | Generates the API documentation from the controllers. |
| Maven | Maven | Build tool. Always invoked through the wrapper, ./mvnw. |
| JUnit 5 | JUnit 5 | Backend unit test framework. |
| MockMvc | MockMvc | Tests a controller without starting a full server. |
| Vitest | Vitest | Frontend unit test framework. |
| Playwright | Playwright | Browser-based end-to-end test framework. |
| Docker Compose | Docker Compose | Starts the local environment. |
| CI | 持續整合 | GitHub Actions runs lint, tests and build on every push. |

---

## 5. Project structure terms

| English | Chinese | Meaning |
|---|---|---|
| WBS | 工作分解結構 | Work breakdown structure. Every task has an identifier such as T-42-04. |
| task ID | 任務編號 | The identifier of one task, used in commit messages and pull request titles. |
| module | 模組 | One functional area of the system. M1 to M10, listed in the project plan. |
| sprint board | 迭代看板 | The GitHub Projects board with columns Backlog, This Week, In Progress, In Review, Done. |
| pull request, PR | 合併請求 | A request to merge one branch into another. Needs one approval. |
| review | 程式碼審查 | Reading somebody else's change and commenting on it. |
| issue | 議題 | A tracked piece of work or a defect. |
| milestone | 里程碑 | A dated target, for example the end of a sprint. |
| logbook | 個人日誌 | The weekly record each member keeps of their own work. |

---

## 6. Naming rules

The rules below are enforced in `CONTRIBUTING.md`. They are repeated here because names are vocabulary too.

### Files and folders

- Lower case, words separated by a hyphen. No spaces.
- `test-cases-pickup.md`, not `Test Cases Pickup.md`.
- Documents that record a dated event start with `YYYYMMDD`, so that the folder listing sorts chronologically: `20261015_weekly-meeting.md`.
- Personal logbooks: `docs/06-logbook/YYYYMMDD_Name_Logbook.md`.

### Diagrams

A diagram title states the business scope in brackets, not a sequence number:

- `Use Case Diagram(Staff Intake Parcel)`
- `Sequence Diagram(Resident Pickup Handover)`
- `ER Diagram(Collection Point Database)`
- `State Diagram(Parcel Lifecycle)`

In the report, figures and tables are numbered by chapter: `Figure 4-1`, `Table 3-2`. A figure caption goes below the figure, a table caption above it.

### Code

| Kind | Convention | Example |
|---|---|---|
| Java package | lower case, single word per module | `com.group23.pickup` |
| Java class | UpperCamelCase | `PickupCodeService` |
| Java method and field | lowerCamelCase | `verifyPickupCode` |
| Database table | snake case, plural | `storage_locations` |
| Database column | snake case | `retention_days` |
| API path | lower case, hyphenated, plural | `/api/storage-locations` |
| JSON field | lowerCamelCase | `pickupCode` |
| Vue component | UpperCamelCase, `.vue` | `LocationBoard.vue` |
| Frontend folder | lower case | `frontend/src/components` |

---

## 7. Terms we no longer use

The project was first described as a smart locker system. That framing was dropped because the system controls no hardware. These words must not appear in code, comments, commits, documents or the report.

**Exemption.** Archived artefacts under `docs/02-design/prototype/` are the historical record of the week 1 interface exploration. They are not live documents and are exempt from this rule; rewriting them would destroy the evidence that the interface design was explored rather than assumed. See `docs/02-design/prototype/README.md`.

| Do not use | Use instead | Why |
|---|---|---|
| locker cabinet, 櫃機 | collection point | The system manages sites, not cabinets. |
| compartment, 格口 | storage location | A shelf position, not a physical locker compartment. |
| door opening, 開箱 | handover | The system records a handover, it does not open anything. |
| scan to store, 掃碼入櫃 | intake | No scanner is integrated. Staff enter data by hand. |
| hardware, 硬體 | not applicable | The system controls no equipment. Do not describe it as if it does. |
| smart locker, 智能櫃 | collection point | Implies hardware control. |
| picking code, 取貨碼 | pickup code | Use the agreed term. |
| VueJS, SpringBoot, Postgres, Element UI | Vue 3, Spring Boot, PostgreSQL, Element Plus | Use the exact product names. |
| LinYi | resident persona / staff persona / owner persona / system administrator persona | Placeholder persona name removed (CR-009). |

---

## 8. Abbreviations

| Abbreviation | Expansion |
|---|---|
| ADR | Architecture decision record |
| CI | Continuous integration |
| ER, ERD | Entity relationship, entity relationship diagram |
| FR | Functional requirement |
| JWT | JSON Web Token |
| MoSCoW | Must have, should have, could have, will not have |
| NFR | Non-functional requirement |
| PR | Pull request |
| RBAC | Role-based access control |
| RTM | Requirements traceability matrix |
| SRS | Software requirements specification |
| WBS | Work breakdown structure |
| UAT | User acceptance testing |

---

## Revision history

| Version | Date | Change | Author |
|---|---|---|---|
| v1.0 | Week 1 | Initial glossary, including the list of terms no longer used | R9 |
| v1.1 | Week 2 | Added design system and design token, so the glossary stays the single source of terminology (CR-012) | R4 |
| v1.2 | Week 2 | Added parcel statuses, storage location statuses, and formalised the exception type list (including `other`); added `LinYi` to the retired terms list | R9 |
