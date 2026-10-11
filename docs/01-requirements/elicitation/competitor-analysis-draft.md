# T-10-03 — Competitor Analysis (Desk Research Draft)

**Research date:** 2026-10-11  
**Method:** Public official product pages, app descriptions, and support pages. No onsite visits or comprehensive hands-on app testing have been conducted.

**Scope:** LinYi is intended for staffed parcel collection points with shelves and storage locations. Hive Box is primarily a smart-locker service and is included as an adjacent model, not as a requirement to build locker hardware.

**Labels:** Confirmed = directly described in cited public source; To verify = insufficient public evidence; Different context = functionality is not directly comparable.

| Comparison dimension | Cainiao Post / Cainiao App | Kuaibao Collection Point | Hive Box / Fengchao |
|---|---|---|---|
| Pickup method | QR scanning, one-tap collection, and pickup codes described in app materials (Confirmed) [1] | Mini-program barcode scanning, photo and one-tap outbound processing (Confirmed) [2][3] | App one-tap locker opening or pickup code (Confirmed; smart-locker context) [4] |
| Notification channels | Arrival notifications and in-app parcel information (Confirmed) [1] | SMS, voice calls, WeChat / Alipay and other channels (Confirmed) [2][3] | WeChat, Alipay and SMS notifications (Confirmed) [4] |
| Overdue storage fees | Collection-point-specific policies not verified | Staffed-point overdue fee policies not verified; locker-related app features may not apply [3] | Locker overtime charging is discussed in third-party material; verify current official fees, which are not directly applicable to staffed shelving [5] |
| Proxy pickup | Family/friend-linked parcel management mentioned; rules for single-use authorization codes to verify [1] | Errand/proxy pickup mentioned in app notes; secure delegation workflow to verify [3] | A pickup code may allow third-party access; dedicated revocable authorization process not verified [4] |
| Exception handling | Staff-side missing/damaged parcel process not verified | Parcel entry and exit records, photo evidence and returns mentioned; complete exceptions workflow to verify [3] | Tracking and customer support shown; not comparable to staff exception workflows [4][6] |
| Usability | Core send/search/collect access is highlighted; task completion times not tested [1] | Numerous intake, lookup, notification and team functions; actual learning curve untested [2][3] | One-tap locker opening and code entry highlighted, but operation differs from staffed collection points [4] |
| Storage and parcel search | Exact shelf allocation methods to verify | Search by waybill, phone suffix or recipient name, plus operation records (Confirmed) [3] | Compartment-based storage logic; different context [6] |
| Audit and traceability | Back-office audit trail not verified | Intake/outbound records and photo proof (Confirmed) [3] | Parcel lookup available; staffed-point user action logging not verified [4] |

## What LinYi should build

These are **proposed MVP priorities**, subject to alignment with the team and the SRS:

1. Parcel intake registration with parcel identifier, collection point, status and timestamp.
2. Shelf **storage location** management and fast parcel lookup.
3. Handover confirmation and operation logs to reduce duplicate or disputed handovers.
4. Notification status tracking; real SMS/WeChat sending depends on integration cost and scope.
5. Exception flagging and search for missing, unmatched or inconsistent parcel records.
6. Single-use, time-limited proxy pickup authorization as a candidate requirement requiring user and security validation.

## What LinYi should not build in the first version

Smart-locker hardware control, complex membership or fee systems, network-wide logistics tracking across couriers, home shipment booking platforms, and autonomous delivery. These exceed the core scope of a staffed parcel collection point and would increase integration cost.

## Limitations

This is public-source desk research only. Product variants, local fee policies, security rules and actual usability have not all been verified. Unverified does **not** mean a competitor lacks a feature. App screenshots and first-hand checks should be added when available.

## Sources

[1] Cainiao App official information: https://www.guoguo-app.com/  
[2] Kuaibao collection point product information: https://m.kuaidihelp.com/help/yzMobile  
[3] Kuaibao App Store feature/release description: https://apps.apple.com/cn/app/id1351573483  
[4] Hive Box product information: https://www-sit2.fcbox.com/pages/product/use.html  
[5] Dangdang third-party help article (fee verification required): https://help.dangdang.com/details/page262  
[6] Hive Box official website: https://fcbox.com/index.html
