-- FR-17: the V1 baseline already creates uq_parcels_tracking_no. This
-- idempotent guard records the registration invariant in the T-42-01
-- migration set and repairs an environment where that constraint is missing.
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'uq_parcels_tracking_no'
          AND conrelid = 'parcels'::regclass
    ) THEN
        ALTER TABLE parcels
            ADD CONSTRAINT uq_parcels_tracking_no UNIQUE (tracking_no);
    END IF;
END
$$;
