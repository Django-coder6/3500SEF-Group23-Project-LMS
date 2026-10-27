-- T-30-03: generated demonstration data.
-- Every person, phone number and address below is fictional. The password hash
-- is for the demonstration password "password"; it is not a real credential.

INSERT INTO collection_points (id, code, name, address, active, created_at, updated_at)
VALUES
    (
        '10000000-0000-4000-8000-000000000001',
        'DEMO-A',
        'Demo Collection Point A',
        '1 Example Street, Demo District',
        TRUE,
        '2026-10-01T09:00:00Z',
        '2026-10-01T09:00:00Z'
    ),
    (
        '10000000-0000-4000-8000-000000000002',
        'DEMO-B',
        'Demo Collection Point B',
        '2 Example Street, Demo District',
        TRUE,
        '2026-10-01T09:00:00Z',
        '2026-10-01T09:00:00Z'
    );

INSERT INTO users (
    id,
    username,
    password_hash,
    full_name,
    role,
    phone,
    active,
    collection_point_id,
    created_at,
    updated_at
)
VALUES
    (
        '20000000-0000-4000-8000-000000000001',
        'demo_owner',
        '$2y$10$Fi/plXMIzoQ9prpzspD27uqbYIT.TV8A/XYd8Zl2vaB.UvyNZp0qO',
        'Demo Owner',
        'owner',
        '00000001',
        TRUE,
        NULL,
        '2026-10-02T09:00:00Z',
        '2026-10-02T09:00:00Z'
    ),
    (
        '20000000-0000-4000-8000-000000000002',
        'demo_staff_1',
        '$2y$10$Fi/plXMIzoQ9prpzspD27uqbYIT.TV8A/XYd8Zl2vaB.UvyNZp0qO',
        'Demo Staff One',
        'staff',
        '00000002',
        TRUE,
        '10000000-0000-4000-8000-000000000001',
        '2026-10-02T09:00:00Z',
        '2026-10-02T09:00:00Z'
    ),
    (
        '20000000-0000-4000-8000-000000000003',
        'demo_staff_2',
        '$2y$10$Fi/plXMIzoQ9prpzspD27uqbYIT.TV8A/XYd8Zl2vaB.UvyNZp0qO',
        'Demo Staff Two',
        'staff',
        '00000003',
        TRUE,
        '10000000-0000-4000-8000-000000000002',
        '2026-10-02T09:00:00Z',
        '2026-10-02T09:00:00Z'
    ),
    (
        '20000000-0000-4000-8000-000000000004',
        'demo_resident_1',
        '$2y$10$Fi/plXMIzoQ9prpzspD27uqbYIT.TV8A/XYd8Zl2vaB.UvyNZp0qO',
        'Demo Resident One',
        'resident',
        '00000011',
        TRUE,
        NULL,
        '2026-10-02T09:00:00Z',
        '2026-10-02T09:00:00Z'
    ),
    (
        '20000000-0000-4000-8000-000000000005',
        'demo_resident_2',
        '$2y$10$Fi/plXMIzoQ9prpzspD27uqbYIT.TV8A/XYd8Zl2vaB.UvyNZp0qO',
        'Demo Resident Two',
        'resident',
        '00000012',
        TRUE,
        NULL,
        '2026-10-02T09:00:00Z',
        '2026-10-02T09:00:00Z'
    ),
    (
        '20000000-0000-4000-8000-000000000006',
        'demo_resident_3',
        '$2y$10$Fi/plXMIzoQ9prpzspD27uqbYIT.TV8A/XYd8Zl2vaB.UvyNZp0qO',
        'Demo Resident Three',
        'resident',
        '00000013',
        TRUE,
        NULL,
        '2026-10-02T09:00:00Z',
        '2026-10-02T09:00:00Z'
    );

-- Parcels are inserted without their circular location and pickup-code links.
INSERT INTO parcels (
    id,
    tracking_no,
    carrier,
    recipient_name,
    recipient_phone,
    size,
    status,
    location_id,
    registered_by_id,
    pickup_code_id,
    expected_version,
    collection_point_id,
    created_at,
    updated_at
)
VALUES
    (
        '40000000-0000-4000-8000-000000000001',
        'DEMO-WAYBILL-0001',
        'Demo Carrier A',
        'Demo Resident One',
        '00000011',
        'small',
        'registered',
        NULL,
        '20000000-0000-4000-8000-000000000002',
        NULL,
        0,
        '10000000-0000-4000-8000-000000000001',
        '2026-10-22T09:00:00Z',
        '2026-10-22T09:00:00Z'
    ),
    (
        '40000000-0000-4000-8000-000000000002',
        'DEMO-WAYBILL-0002',
        'Demo Carrier B',
        'Demo Resident Two',
        '00000012',
        'medium',
        'shelved',
        NULL,
        '20000000-0000-4000-8000-000000000002',
        NULL,
        1,
        '10000000-0000-4000-8000-000000000001',
        '2026-10-21T09:00:00Z',
        '2026-10-21T10:00:00Z'
    ),
    (
        '40000000-0000-4000-8000-000000000003',
        'DEMO-WAYBILL-0003',
        'Demo Carrier C',
        'Demo Resident Three',
        '00000013',
        'large',
        'shelved',
        NULL,
        '20000000-0000-4000-8000-000000000003',
        NULL,
        1,
        '10000000-0000-4000-8000-000000000002',
        '2026-10-20T10:00:00Z',
        '2026-10-20T11:00:00Z'
    ),
    (
        '40000000-0000-4000-8000-000000000004',
        'DEMO-WAYBILL-0004',
        'Demo Carrier A',
        'Demo Resident One',
        '00000011',
        'medium',
        'registered',
        NULL,
        '20000000-0000-4000-8000-000000000003',
        NULL,
        0,
        '10000000-0000-4000-8000-000000000002',
        '2026-10-23T09:00:00Z',
        '2026-10-23T09:00:00Z'
    ),
    (
        '40000000-0000-4000-8000-000000000005',
        'DEMO-WAYBILL-0005',
        'Demo Carrier B',
        'Demo Resident Two',
        '00000012',
        'small',
        'collected',
        NULL,
        '20000000-0000-4000-8000-000000000002',
        NULL,
        2,
        '10000000-0000-4000-8000-000000000001',
        '2026-10-18T09:00:00Z',
        '2026-10-22T08:30:00Z'
    );

INSERT INTO locations (
    id,
    code,
    zone,
    size,
    status,
    parcel_id,
    collection_point_id,
    updated_at
)
VALUES
    (
        '30000000-0000-4000-8000-000000000001',
        'DEMO-A-01-1',
        'A',
        'small',
        'reserved',
        '40000000-0000-4000-8000-000000000001',
        '10000000-0000-4000-8000-000000000001',
        '2026-10-22T09:05:00Z'
    ),
    (
        '30000000-0000-4000-8000-000000000002',
        'DEMO-A-01-2',
        'A',
        'medium',
        'occupied',
        '40000000-0000-4000-8000-000000000002',
        '10000000-0000-4000-8000-000000000001',
        '2026-10-21T10:00:00Z'
    ),
    (
        '30000000-0000-4000-8000-000000000003',
        'DEMO-A-01-3',
        'A',
        'small',
        'available',
        NULL,
        '10000000-0000-4000-8000-000000000001',
        '2026-10-20T09:00:00Z'
    ),
    (
        '30000000-0000-4000-8000-000000000004',
        'DEMO-A-02-1',
        'A',
        'large',
        'disabled',
        NULL,
        '10000000-0000-4000-8000-000000000001',
        '2026-10-19T09:00:00Z'
    ),
    (
        '30000000-0000-4000-8000-000000000005',
        'DEMO-B-01-1',
        'B',
        'large',
        'occupied',
        '40000000-0000-4000-8000-000000000003',
        '10000000-0000-4000-8000-000000000002',
        '2026-10-20T11:00:00Z'
    ),
    (
        '30000000-0000-4000-8000-000000000006',
        'DEMO-B-01-2',
        'B',
        'medium',
        'available',
        NULL,
        '10000000-0000-4000-8000-000000000002',
        '2026-10-20T09:00:00Z'
    ),
    (
        '30000000-0000-4000-8000-000000000007',
        'DEMO-B-02-1',
        'B',
        'small',
        'disabled',
        NULL,
        '10000000-0000-4000-8000-000000000002',
        '2026-10-19T09:00:00Z'
    ),
    (
        '30000000-0000-4000-8000-000000000008',
        'DEMO-B-02-2',
        'B',
        'medium',
        'reserved',
        '40000000-0000-4000-8000-000000000004',
        '10000000-0000-4000-8000-000000000002',
        '2026-10-23T09:05:00Z'
    );

INSERT INTO pickup_codes (
    id,
    parcel_id,
    code,
    status,
    expires_at,
    used_at,
    created_at
)
VALUES
    (
        '50000000-0000-4000-8000-000000000001',
        '40000000-0000-4000-8000-000000000002',
        '48210001',
        'active',
        '2026-11-01T10:00:00Z',
        NULL,
        '2026-10-21T10:00:00Z'
    ),
    (
        '50000000-0000-4000-8000-000000000002',
        '40000000-0000-4000-8000-000000000003',
        '48210002',
        'active',
        '2026-11-02T11:00:00Z',
        NULL,
        '2026-10-20T11:00:00Z'
    ),
    (
        '50000000-0000-4000-8000-000000000003',
        '40000000-0000-4000-8000-000000000005',
        '48210003',
        'used',
        '2026-10-25T09:00:00Z',
        '2026-10-22T08:30:00Z',
        '2026-10-18T09:00:00Z'
    );

UPDATE parcels
SET location_id = CASE id
        WHEN '40000000-0000-4000-8000-000000000001'
            THEN '30000000-0000-4000-8000-000000000001'::UUID
        WHEN '40000000-0000-4000-8000-000000000002'
            THEN '30000000-0000-4000-8000-000000000002'::UUID
        WHEN '40000000-0000-4000-8000-000000000003'
            THEN '30000000-0000-4000-8000-000000000005'::UUID
        WHEN '40000000-0000-4000-8000-000000000004'
            THEN '30000000-0000-4000-8000-000000000008'::UUID
        ELSE NULL::UUID
    END,
    pickup_code_id = CASE id
        WHEN '40000000-0000-4000-8000-000000000002'
            THEN '50000000-0000-4000-8000-000000000001'::UUID
        WHEN '40000000-0000-4000-8000-000000000003'
            THEN '50000000-0000-4000-8000-000000000002'::UUID
        WHEN '40000000-0000-4000-8000-000000000005'
            THEN '50000000-0000-4000-8000-000000000003'::UUID
        ELSE NULL::UUID
    END
WHERE id IN (
    '40000000-0000-4000-8000-000000000001',
    '40000000-0000-4000-8000-000000000002',
    '40000000-0000-4000-8000-000000000003',
    '40000000-0000-4000-8000-000000000004',
    '40000000-0000-4000-8000-000000000005'
);

INSERT INTO exception_records (
    id,
    parcel_id,
    type,
    status,
    note,
    assignee_id,
    resolved_at,
    created_at
)
VALUES
    (
        '60000000-0000-4000-8000-000000000001',
        '40000000-0000-4000-8000-000000000002',
        'damaged',
        'open',
        'Demonstration record: packaging inspection requested.',
        '20000000-0000-4000-8000-000000000002',
        NULL,
        '2026-10-21T11:00:00Z'
    ),
    (
        '60000000-0000-4000-8000-000000000002',
        '40000000-0000-4000-8000-000000000003',
        'overdue',
        'processing',
        'Demonstration record: resident contact attempted.',
        '20000000-0000-4000-8000-000000000003',
        NULL,
        '2026-10-22T11:00:00Z'
    ),
    (
        '60000000-0000-4000-8000-000000000003',
        '40000000-0000-4000-8000-000000000005',
        'lost',
        'resolved',
        'Demonstration record: parcel later located and collected.',
        '20000000-0000-4000-8000-000000000002',
        '2026-10-22T08:20:00Z',
        '2026-10-22T08:00:00Z'
    );

INSERT INTO audit_logs (
    id,
    actor_id,
    action,
    target_type,
    target_id,
    before,
    after,
    collection_point_id,
    created_at
)
VALUES
    (
        '70000000-0000-4000-8000-000000000001',
        '20000000-0000-4000-8000-000000000002',
        'parcel.register',
        'parcel',
        '40000000-0000-4000-8000-000000000001',
        NULL,
        '{"status":"registered","collectionPointCode":"DEMO-A"}'::JSONB,
        '10000000-0000-4000-8000-000000000001',
        '2026-10-22T09:00:00Z'
    ),
    (
        '70000000-0000-4000-8000-000000000002',
        '20000000-0000-4000-8000-000000000002',
        'parcel.shelve',
        'parcel',
        '40000000-0000-4000-8000-000000000002',
        '{"status":"registered"}'::JSONB,
        '{"status":"shelved","locationCode":"DEMO-A-01-2"}'::JSONB,
        '10000000-0000-4000-8000-000000000001',
        '2026-10-21T10:00:00Z'
    ),
    (
        '70000000-0000-4000-8000-000000000003',
        '20000000-0000-4000-8000-000000000002',
        'handover.confirm',
        'parcel',
        '40000000-0000-4000-8000-000000000005',
        '{"status":"shelved"}'::JSONB,
        '{"status":"collected"}'::JSONB,
        '10000000-0000-4000-8000-000000000001',
        '2026-10-22T08:30:00Z'
    );

INSERT INTO handovers (
    id,
    parcel_id,
    pickup_code_id,
    operator_id,
    result,
    audit_id,
    created_at
)
VALUES (
    '80000000-0000-4000-8000-000000000001',
    '40000000-0000-4000-8000-000000000005',
    '50000000-0000-4000-8000-000000000003',
    '20000000-0000-4000-8000-000000000002',
    'collected',
    '70000000-0000-4000-8000-000000000003',
    '2026-10-22T08:30:00Z'
);

-- The rules documents use these per-collection-point values. Keeping them in
-- Settings prevents later billing and recommendation code from hard-coding
-- the accepted defaults.
INSERT INTO settings (
    id,
    key,
    value,
    description,
    updated_by_id,
    collection_point_id,
    updated_at
)
SELECT
    setting_id,
    setting_key,
    setting_value,
    description,
    '20000000-0000-4000-8000-000000000001',
    collection_point_id,
    '2026-10-23T09:00:00Z'
FROM (
    VALUES
        (
            '90000000-0000-4000-8000-000000000001'::UUID,
            '10000000-0000-4000-8000-000000000001'::UUID,
            'overdue.retention_days',
            '3',
            'Free retention period in days.'
        ),
        (
            '90000000-0000-4000-8000-000000000002'::UUID,
            '10000000-0000-4000-8000-000000000001'::UUID,
            'overdue.daily_fee_minor_units',
            '200',
            'Daily overdue fee in HKD minor units.'
        ),
        (
            '90000000-0000-4000-8000-000000000003'::UUID,
            '10000000-0000-4000-8000-000000000001'::UUID,
            'overdue.cap_minor_units',
            '2000',
            'Maximum overdue fee in HKD minor units.'
        ),
        (
            '90000000-0000-4000-8000-000000000004'::UUID,
            '10000000-0000-4000-8000-000000000001'::UUID,
            'overdue.currency',
            'HKD',
            'Currency for overdue fee values.'
        ),
        (
            '90000000-0000-4000-8000-000000000005'::UUID,
            '10000000-0000-4000-8000-000000000001'::UUID,
            'location_recommender.cold_zone_max_ratio',
            '0.20',
            'Inclusive upper occupancy ratio for a cold zone.'
        ),
        (
            '90000000-0000-4000-8000-000000000006'::UUID,
            '10000000-0000-4000-8000-000000000001'::UUID,
            'location_recommender.hot_zone_min_ratio',
            '0.80',
            'Inclusive lower occupancy ratio for a hot zone.'
        ),
        (
            '90000000-0000-4000-8000-000000000007'::UUID,
            '10000000-0000-4000-8000-000000000002'::UUID,
            'overdue.retention_days',
            '3',
            'Free retention period in days.'
        ),
        (
            '90000000-0000-4000-8000-000000000008'::UUID,
            '10000000-0000-4000-8000-000000000002'::UUID,
            'overdue.daily_fee_minor_units',
            '200',
            'Daily overdue fee in HKD minor units.'
        ),
        (
            '90000000-0000-4000-8000-000000000009'::UUID,
            '10000000-0000-4000-8000-000000000002'::UUID,
            'overdue.cap_minor_units',
            '2000',
            'Maximum overdue fee in HKD minor units.'
        ),
        (
            '90000000-0000-4000-8000-000000000010'::UUID,
            '10000000-0000-4000-8000-000000000002'::UUID,
            'overdue.currency',
            'HKD',
            'Currency for overdue fee values.'
        ),
        (
            '90000000-0000-4000-8000-000000000011'::UUID,
            '10000000-0000-4000-8000-000000000002'::UUID,
            'location_recommender.cold_zone_max_ratio',
            '0.20',
            'Inclusive upper occupancy ratio for a cold zone.'
        ),
        (
            '90000000-0000-4000-8000-000000000012'::UUID,
            '10000000-0000-4000-8000-000000000002'::UUID,
            'location_recommender.hot_zone_min_ratio',
            '0.80',
            'Inclusive lower occupancy ratio for a hot zone.'
        )
) AS seed_settings (
    setting_id,
    collection_point_id,
    setting_key,
    setting_value,
    description
);
