# 2. System Overview

## 2.1 Stakeholders
The system serves four distinct roles through a Role-Based Access Control (RBAC) model:
- **Resident:** Receives parcels, views status, and uses pickup or authorisation codes.
- **Staff:** Operates the collection point, records parcel intake, shelves parcels, and executes handovers.
- **Site Admin:** Manages the collection point, including storage location configurations and fee rules.
- **Courier:** Delivers parcels in bulk to the collection point.

## 2.2 System Architecture
The system adopts a standard 3-tier architecture:
- **Frontend:** Built with Vue 3, Vite, and Pinia. It provides responsive interfaces for residents and staff.
- **Backend:** A Java 17 and Spring Boot 3.3 service. It handles core workflows, including the storage location recommendation algorithm, idempotent handover logic, and the billing engine.
- **Database:** PostgreSQL 16, managed via Flyway migrations. It ensures transactional integrity to prevent race conditions during concurrent allocation and handover operations.

## 2.3 Core Workflows
- **Intake:** Staff records the parcel. The system recommends an available storage location based on parcel size and proximity.
- **Handover:** Staff verifies the resident's pickup code. The system checks the retention period, calculates any overdue fees, and securely marks the parcel as handed over. The service is designed to be idempotent to prevent duplicate processing.
- **State Management:** Domain objects (Parcel, Storage Location, Pickup Code) follow defined state machines to handle lifecycle transitions accurately.
