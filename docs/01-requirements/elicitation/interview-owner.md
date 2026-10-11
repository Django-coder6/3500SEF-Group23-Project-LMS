# T-10-02 — Owner/manager of a staffed parcel collection point



- Scenario ID: `Owner-01`
- Participant profile: participant Owner-01, manages a small collection point with 3 staff members.
- Method: 15-minute interview
- Date created: 2026-10-11
- Context: Illustrative scenario: cost-sensitive independent point using existing mobile phones and a desktop computer.

## interview dialogue (five pre-planned questions)

### Q1. Approximately how many staff work here, and how many parcels are processed daily?

**Fictional participant:** We have three people including me. On an ordinary weekday we handle about 180 parcels, but promotion periods can be much busier. The workload changes a lot depending on courier schedules.

### Q2. What tools do you use for parcel intake, shelving, searching, and handover confirmation? What are the main costs?

**Fictional participant:** We mainly use a basic app, a barcode scanner and some paper labels. I do not want to buy expensive new terminals. Training and time lost during setup are real costs, even if the software itself is free.

### Q3. How do you investigate incorrect collections, missing parcels, overdue parcels, or disputes?

**Fictional participant:** We look up the parcel number, ask the employee on shift and compare our notes. Sometimes we have to check messages or camera footage. I want each status change to show who made it and when, with a simple way to mark an unresolved case.

### Q4. What costs, training requirements, or device compatibility concerns would affect adoption of a new system?

**Fictional participant:** It must run on the devices we already have and be easy for new workers. We also need a way to recover records if an internet connection drops. I would be cautious about anything that sends paid SMS without limits.

### Q5. Which three features must be included in the first release, and which can wait?

**Fictional participant:** My first priorities are accurate intake, reliable shelf-based search and confirmed handover with history. Reports could be basic at first. Delivery booking, loyalty programs and smart-locker integration can wait.

## Illustrative analysis — not validated findings

**Potential pain points:**
- Cost and training barriers for adoption
- Weak evidence trail for disputes
- Need to operate using existing devices

**Candidate functional/non-functional requirements to validate:**
- Web-based responsive interface on common devices
- Record parcel lifecycle transitions with account/time metadata
- Basic reporting; postpone smart-locker and logistics platform integrations
- Document data recovery and offline/network failure behaviour for later design

**Glossary concepts to define consistently:** collection point, parcel lifecycle, handover confirmation, audit trail, out of scope.

