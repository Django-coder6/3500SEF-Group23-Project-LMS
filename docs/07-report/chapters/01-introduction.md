# 1. The Problem and the Context

## 1.1 Background and Problem Statement
Community parcel collection points are essential nodes in the last-mile delivery network. In these points, couriers drop parcels at the counter, staff record and shelve them, and residents collect them later. Many collection points still rely on paper logs or basic spreadsheets. This manual approach creates several critical issues: parcels are easily lost due to unstructured storage, the handover process is slow because identity verification is manual, and billing disputes frequently occur over overdue fees. 

## 1.2 Objectives
The objective of this project is to design and develop a comprehensive, web-based Community Parcel Collection Point Management System (LMS). The system aims to digitalize the intake, shelving, and handover processes. By automating storage location recommendations, secure pickup code generation, and overdue fee calculations, the system eliminates human errors, improves operational efficiency, and enhances the experience for both staff and residents.

## 1.3 Scope
**In Scope:**
- Role-Based Access Control (RBAC) for Residents, Staff, Site Admins, and Couriers.
- Parcel intake and automated storage location recommendation.
- Secure pickup code generation and idempotent handover.
- Automated overdue fee calculation based on retention periods.
- Administrative dashboards for site management and monthly reporting.

**Out of Scope:**
- Physical hardware control (the system does not control smart locker doors).
- Integration with third-party courier platforms and payment gateways.
