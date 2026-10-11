

- Scenario ID: `Staff-01`
- Participant profile: participant Staff-01, staff member with about 18 months of experience at a staffed point.
- Method: 15-minute interview
- Date created: 2026-10-11
- Context: Illustrative scenario: 150–200 handled parcels on a typical busy day

## interview dialogue (five pre-planned questions)

### Q1. Describe the process from parcel arrival and registration to shelving and handover confirmation.

**Fictional participant:** We receive bags from several couriers, check labels, register each parcel in our current tool, and place it on a shelf. When a resident arrives, we ask for a pickup code or phone suffix, search the records, locate the parcel, and confirm handover. The problem is that the shelf field is sometimes skipped when we are busy.

### Q2. How many parcels are processed each day, and what problems occur at peak times?

**Fictional participant:** On a normal day, perhaps 150 to 200 parcels pass through. The evening rush gets difficult because one employee is answering questions while another searches, and parcels waiting to be shelved pile up. We sometimes forget to update a location until later.

### Q3. How are storage locations assigned and searched, and what happens when a parcel is misplaced?

**Fictional participant:** We use shelf letters and numbers. If the wrong location is entered, we check nearby shelves and the most recent incoming records. It can take several minutes. A clear location field and a way to report a location mismatch would help.

### Q4. Which parts of manual recording or your existing system consume the most time or cause errors?

**Fictional participant:** Repeating tracking numbers by hand is frustrating, and handwriting can be difficult to read. Even with scanning, switching between the registration screen and the customer search screen wastes time. We need fewer steps and clear error warnings.

### Q5. What do you need for proxy pickup verification, exception handling, and operation logs?

**Fictional participant:** We should be able to validate the person collecting the parcel without seeing more personal information than needed. Missing and damaged parcels should have a status we can track. When there is a dispute, it would be useful to know which staff account confirmed the handover and when.

## Illustrative analysis 

**Potential pain points:**
- Shelf location fields omitted or wrong under pressure
- Duplicate/manual tracking number entry
- Poor visibility for exceptions and disputed handovers

**Candidate functional/non-functional requirements to validate:**
- Barcode intake with required storage location field
- Quick search by tracking number, pickup code or permitted phone suffix
- Exception case recording and action audit trail
- Role-based access for staff

**Glossary concepts to define consistently:** parcel intake, shelving, storage location, exception parcel, operation log.

