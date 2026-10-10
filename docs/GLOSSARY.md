# Glossary

Terms used across the code, the documentation and the report. If a term is
not in this list, add it here before using it elsewhere so the whole team
stays consistent.

## Domain terms

| English | Chinese | Meaning |
|---|---|---|
| collection point | 代收點 | A shop or counter where couriers leave parcels for residents to pick up. One collection point is one site. |
| storage location | 庫位 | A numbered slot on a shelf where a parcel waits to be collected. This is a record in the database, not a physical locker. |
| shelving | 上架 | Putting a parcel onto its storage location after it has been recorded at the counter. |
| handover | 核銷 | Confirming that a parcel has been given to the resident, or to somebody authorised by the resident. |
| pickup code | 取件碼 | A six digit code sent to the resident. It can be used once and expires. |
| authorisation code | 代取授權碼 | A one time code a resident can forward to somebody else so that person can collect the parcel. |
| stocktake | 盤點 | Counting what is actually on the shelves and comparing it with what the system says is there. |
| retention period | 免費保管期 | How long a parcel can stay before a storage fee starts. Three days by default. |
| overdue fee | 超期費用 | The fee charged once a parcel has been held longer than the retention period. |
| exception parcel | 異常件 | A parcel that cannot follow the normal flow, for example damaged, misdelivered, refused or lost. |
| intake | 收件入庫 | The whole step of recording a parcel at the counter and assigning it a storage location. |

## Roles

| English | Chinese | Meaning |
|---|---|---|
| resident | 居民 | The person a parcel is addressed to. Uses the self service pages. |
| staff | 代收點員工 | Works at the counter. Records parcels, shelves them and hands them over. |
| site admin | 站點管理員 | Manages one collection point: storage locations, staff accounts and fee rules. |
| courier | 快遞員 | Delivers parcels in bulk to the counter. |

## System terms

| English | Chinese | Meaning |
|---|---|---|
| recommendation algorithm | 庫位推薦演算法 | Chooses which storage location a parcel should go to. |
| billing engine | 計費規則引擎 | Works out the overdue fee. Reads its rules from configuration so the rules can change without a code change. |
| idempotent | 冪等 | Running the same operation twice has the same result as running it once. Handover must behave this way. |
| audit log | 操作審計日誌 | An append only record of who changed which parcel or storage location, and when. |
| RBAC | 角色權限控制 | Role based access control. What a user can do depends on their role. |
| backlog | 待辦清單 | The ordered list of work still to be done. |
| sprint | 迭代 | A fixed two week block of work. |
| stand-up | 站會 | The short regular meeting where each person says what they finished and what is next. |
| WBS | 工作分解結構 | The breakdown of the project into numbered tasks, for example T-42-04. |

## Project management terms

| English | Chinese | Meaning |
|---|---|---|
| Agile Development | 敏捷開發 | An iterative and incremental software development approach. |
| Scrum | Scrum | An Agile framework that delivers work in fixed-length iterations called Sprints. |
| Product Owner (PO) | 產品負責人 | The person responsible for the Product Backlog and requirement priorities. |
| Scrum Master | Scrum Master | The person who removes obstacles and facilitates the Scrum process. |
| Product Backlog | 產品待辦清單 | A prioritized list of requirements. |
| Sprint Backlog | Sprint 待辦清單 | The list of tasks to be completed in the current Sprint. |
| Acceptance Criteria | 驗收標準 | Verifiable conditions that determine whether a requirement is complete. |
| Definition of Done (DoD) | 完成定義 | The team's shared standard for what "done" means. |
| Milestone | 里程碑 | An important checkpoint in the project. |
| Risk Register | 風險登記冊 | A record of risks, likelihood, impact, and mitigation measures. |
| Change Log | 變更日誌 | A record of requirement, design, or scope changes. |
| Retrospective | 回顧會議 | A meeting held after a Sprint to review and improve the process. |
| Daily Stand-up | 每日站會 | A short daily meeting to synchronize progress. |
| Velocity | 速度 | The amount of work completed by the team in a Sprint. |
| Burndown Chart | 燃盡圖 | A chart showing remaining work over time. |
| Gantt Chart | 甘特圖 | A chart showing task schedules and time relationships. |
| Progress Review | 進度評審 | A staged classroom review of project progress. |
| Logbook | 個人日誌 | An individual weekly work and evidence record for each member. |
| Contribution Heatmap | 貢獻熱力圖 | A visualization of GitHub contribution activity. |

## Requirements and design terms

| English | Chinese | Meaning |
|---|---|---|
| Software Requirements Specification (SRS) | 軟體需求規格說明書 | A document that fully describes system requirements. |
| Functional Requirement (FR) | 功能需求 | A function the system must provide. |
| Non-Functional Requirement (NFR) | 非功能需求 | Quality requirements such as performance, security, and usability. |
| Requirements Traceability Matrix (RTM / RIM) | 需求追蹤矩陣 | A mapping from requirements to design, code, and test cases. |
| User Story | 使用者故事 | A short requirement description from the user's perspective. |
| Persona | 使用者畫像 | A fictional character representing a target user. |
| Journey Map | 使用者旅程圖 | A diagram of how a user completes a goal in the system. |
| Use Case Diagram | 用例圖 | A UML diagram showing actors and system functions. |
| Sequence Diagram | 時序圖 | A UML diagram showing the order of interactions between objects. |
| Class Diagram | 類別圖 | A UML diagram showing classes, attributes, and relationships. |
| Activity Diagram | 活動圖 | A UML diagram showing workflows and branches. |
| State Machine Diagram | 狀態機圖 | A UML diagram showing state transitions. |
| Entity-Relationship Diagram (ERD) | 實體關係圖 | A database design diagram showing tables and relationships. |
| Architecture Decision Record (ADR) | 架構決策記錄 | A record of important architecture decisions and their rationale. |
| API Contract | 介面契約 | An agreed API specification between frontend and backend. |
| OpenAPI Specification | OpenAPI 規格 | A standard format for describing REST APIs. |
| Unified Modeling Language (UML) | 統一建模語言 | A standard graphical language for software modeling. |
| Design System | 設計系統 | Standards for colors, fonts, spacing, and components. |
| Style Guide | 風格指南 | Visual and component usage guidelines. |
| High-Fidelity Prototype | 高保真原型 | A clickable prototype close to the final visual design. |
| Usability Review | 可用性評審 | A review to check whether the interface is easy to use. |
| Heuristic Evaluation | 啟發式評估 | A usability check based on Nielsen's ten principles. |

## Development and operations terms

| English | Chinese | Meaning |
|---|---|---|
| Continuous Integration (CI) | 持續整合 | Automatically building and testing on every commit. |
| Continuous Deployment (CD) | 持續部署 | Automatically deploying to an environment. |
| Containerization | 容器化 | Packaging an application and its environment with Docker. |
| Database Migration | 資料庫遷移 | Versioned changes to the database schema. |
| Seed Data | 種子資料 | Initial test data. |
| Authentication | 身分認證 | Verifying user identity. |
| Authorization | 授權 | Verifying user permissions. |
| Role-Based Access Control (RBAC) | 角色型存取控制 | Assigning permissions based on roles. |
| JSON Web Token (JWT) | JSON Web Token | A token commonly used for login authentication. |
| Concurrency Safety | 併發安全 | Avoiding data errors under multiple requests. |
| Atomic Allocation | 原子分配 | Allocation of a storage location that cannot be interrupted or duplicated. |
| Unique Index | 唯一索引 | A database-level constraint that prevents duplicates. |
| Heartbeat Reporting | 心跳上報 | Periodic status reporting from a device or service. |
| Unlock Command | 開箱指令 | A command that controls the opening of a locker door. |
| In-App Notification | 站內信 | A notification message inside the system. |
| Real-Time Push | 即時推送 | Real-time updates via Socket.IO or similar technology. |
| Scheduled Task / Cron Job | 定時任務 | A task executed on a regular schedule. |
| Mock Service / Fake Service | 模擬服務 | A test service that simulates an external system. |

## Testing and quality terms

| English | Chinese | Meaning |
|---|---|---|
| Unit Testing | 單元測試 | Testing a single function or module. |
| Integration Testing | 整合測試 | Testing interactions between modules. |
| System Testing | 系統測試 | Testing the system as a whole. |
| User Acceptance Testing (UAT) | 驗收測試 | Acceptance testing by users. |
| End-to-End Testing (E2E) | 端到端測試 | Automated testing that simulates real workflows. |
| Performance Testing | 效能測試 | Testing speed and throughput. |
| Security Testing | 安全測試 | Testing for unauthorized access, injection, XSS, etc. |
| Regression Testing | 回歸測試 | Confirming that existing features still work after changes. |
| Defect / Bug | 缺陷 | A system error. |
| Test Coverage | 測試覆蓋率 | The proportion of code covered by tests. |
| Contract Validation | 契約驗證 | Checking that the implementation matches OpenAPI. |
| Plagiarism Check | 查重 | Checking the report for duplicated content. |

## Names to avoid

These words were used in an earlier draft of the project. They no longer
describe anything in this system, so do not use them in code, comments,
documents or the report.

| Do not use | Use instead |
|---|---|
| locker cabinet, 櫃機 | collection point |
| compartment, 格口 | storage location |
| door opening, 開箱 | handover |
| scan to store, 掃碼入櫃 | intake |
| hardware, 硬體 | (not applicable - this system controls no equipment) |
| picking code, 取貨碼 | pickup code |

## Naming conventions

### Files and folders

- Folders and files are lower case.
- Words inside a file name are separated by a hyphen, for example
  `test-cases-pickup.md`, `risk-register.md`.
- Use the `.md` extension for documents.
- Do not use spaces in file names inside the repository.

### Dated documents

Meeting minutes, interview notes, survey results and weekly reports start with
the date in `YYYYMMDD` form:
