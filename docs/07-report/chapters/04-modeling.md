# 4. System Modeling

System modeling is anchored by the parcel lifecycle state machine. The model defines three states: `registered → shelved → collected`, with strict transitions enforced by the service layer (`CR-006`). The original seven-state enumeration was reduced because `ready_for_pickup` and `out_for_delivery` described courier logistics stages that the SRS places outside the system boundary.

Exceptions are not modeled as parcel states. Instead, a separate `ParcelException` flow (`open → processing → resolved`) records damage, loss, or wrong-recipient cases without altering the parcel's physical status. Illegal transitions are rejected with a 422 error.
