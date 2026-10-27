-- T-30-03: baseline schema for the community parcel collection point system.
-- Source of truth: docs/02-design/database/ERD.md v1.3.

CREATE TYPE user_role AS ENUM (
    'resident',
    'staff',
    'owner',
    'admin'
);

CREATE TYPE parcel_status AS ENUM (
    'registered',
    'shelved',
    'collected'
);

CREATE TYPE parcel_size AS ENUM (
    'small',
    'medium',
    'large'
);

CREATE TYPE location_status AS ENUM (
    'available',
    'reserved',
    'occupied',
    'disabled'
);

CREATE TYPE pickup_code_status AS ENUM (
    'active',
    'used',
    'expired',
    'revoked'
);

CREATE TYPE exception_type AS ENUM (
    'overdue',
    'damaged',
    'wrong_recipient',
    'refused',
    'lost'
);

CREATE TYPE exception_status AS ENUM (
    'open',
    'processing',
    'resolved'
);

CREATE TYPE handover_result AS ENUM (
    'collected',
    'returned',
    'exception'
);

CREATE TABLE collection_points (
    id UUID PRIMARY KEY,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    address VARCHAR(500) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_collection_points_code UNIQUE (code)
);

CREATE TABLE users (
    id UUID PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(255) NOT NULL,
    role user_role NOT NULL,
    phone VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    collection_point_id UUID,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_users_username UNIQUE (username),
    CONSTRAINT ck_users_collection_point_is_staff_only
        CHECK (collection_point_id IS NULL OR role = 'staff')
);

CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    actor_id UUID NOT NULL,
    action VARCHAR(100) NOT NULL,
    target_type VARCHAR(100) NOT NULL,
    target_id UUID NOT NULL,
    before JSONB,
    after JSONB,
    collection_point_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Parcels are created before locations and pickup codes because those tables
-- reference each other. The parcel-side foreign keys are added after both
-- tables exist.
CREATE TABLE parcels (
    id UUID PRIMARY KEY,
    tracking_no VARCHAR(100) NOT NULL,
    carrier VARCHAR(100) NOT NULL,
    recipient_name VARCHAR(255) NOT NULL,
    recipient_phone VARCHAR(50) NOT NULL,
    size parcel_size NOT NULL,
    status parcel_status NOT NULL,
    location_id UUID,
    registered_by_id UUID NOT NULL,
    pickup_code_id UUID,
    expected_version BIGINT NOT NULL DEFAULT 0,
    collection_point_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_parcels_tracking_no UNIQUE (tracking_no),
    CONSTRAINT uq_parcels_pickup_code_id UNIQUE (pickup_code_id),
    CONSTRAINT ck_parcels_expected_version CHECK (expected_version >= 0)
);

CREATE TABLE locations (
    id UUID PRIMARY KEY,
    code VARCHAR(100) NOT NULL,
    zone VARCHAR(100) NOT NULL,
    size parcel_size NOT NULL,
    status location_status NOT NULL DEFAULT 'available',
    parcel_id UUID,
    collection_point_id UUID NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_locations_code UNIQUE (code),
    CONSTRAINT ck_locations_occupied_has_parcel
        CHECK (status <> 'occupied' OR parcel_id IS NOT NULL),
    CONSTRAINT ck_locations_empty_status_has_no_parcel
        CHECK (status NOT IN ('available', 'disabled') OR parcel_id IS NULL)
);

-- PostgreSQL unique constraints consider NULL values distinct. A partial
-- unique index is still required because the rule is explicit and protects the
-- one-active-parcel invariant if the column definition changes later.
CREATE UNIQUE INDEX uq_locations_parcel_id_not_null
    ON locations (parcel_id)
    WHERE parcel_id IS NOT NULL;

CREATE TABLE pickup_codes (
    id UUID PRIMARY KEY,
    parcel_id UUID NOT NULL,
    code VARCHAR(32) NOT NULL,
    status pickup_code_status NOT NULL DEFAULT 'active',
    expires_at TIMESTAMPTZ NOT NULL,
    used_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_pickup_codes_parcel_id UNIQUE (parcel_id),
    CONSTRAINT uq_pickup_codes_code UNIQUE (code),
    CONSTRAINT ck_pickup_codes_used_at
        CHECK (status <> 'used' OR used_at IS NOT NULL)
);

CREATE TABLE exception_records (
    id UUID PRIMARY KEY,
    parcel_id UUID NOT NULL,
    type exception_type NOT NULL,
    status exception_status NOT NULL DEFAULT 'open',
    note TEXT NOT NULL DEFAULT '',
    assignee_id UUID NOT NULL,
    resolved_at TIMESTAMPTZ,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT ck_exception_records_resolved_at
        CHECK (status <> 'resolved' OR resolved_at IS NOT NULL)
);

CREATE TABLE handovers (
    id UUID PRIMARY KEY,
    parcel_id UUID NOT NULL,
    pickup_code_id UUID NOT NULL,
    operator_id UUID NOT NULL,
    result handover_result NOT NULL,
    audit_id UUID NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_handovers_pickup_code_id UNIQUE (pickup_code_id),
    CONSTRAINT uq_handovers_audit_id UNIQUE (audit_id)
);

CREATE TABLE settings (
    id UUID PRIMARY KEY,
    key VARCHAR(150) NOT NULL,
    value TEXT NOT NULL,
    description TEXT NOT NULL DEFAULT '',
    updated_by_id UUID NOT NULL,
    collection_point_id UUID NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_settings_collection_point_key
        UNIQUE (collection_point_id, key)
);

ALTER TABLE users
    ADD CONSTRAINT fk_users_collection_point
    FOREIGN KEY (collection_point_id)
    REFERENCES collection_points (id)
    ON DELETE RESTRICT;

ALTER TABLE parcels
    ADD CONSTRAINT fk_parcels_location
    FOREIGN KEY (location_id)
    REFERENCES locations (id)
    ON DELETE SET NULL,
    ADD CONSTRAINT fk_parcels_registered_by
    FOREIGN KEY (registered_by_id)
    REFERENCES users (id)
    ON DELETE RESTRICT,
    ADD CONSTRAINT fk_parcels_pickup_code
    FOREIGN KEY (pickup_code_id)
    REFERENCES pickup_codes (id)
    ON DELETE SET NULL,
    ADD CONSTRAINT fk_parcels_collection_point
    FOREIGN KEY (collection_point_id)
    REFERENCES collection_points (id)
    ON DELETE RESTRICT;

ALTER TABLE locations
    ADD CONSTRAINT fk_locations_parcel
    FOREIGN KEY (parcel_id)
    REFERENCES parcels (id)
    ON DELETE SET NULL,
    ADD CONSTRAINT fk_locations_collection_point
    FOREIGN KEY (collection_point_id)
    REFERENCES collection_points (id)
    ON DELETE RESTRICT;

ALTER TABLE pickup_codes
    ADD CONSTRAINT fk_pickup_codes_parcel
    FOREIGN KEY (parcel_id)
    REFERENCES parcels (id)
    ON DELETE RESTRICT;

ALTER TABLE exception_records
    ADD CONSTRAINT fk_exception_records_parcel
    FOREIGN KEY (parcel_id)
    REFERENCES parcels (id)
    ON DELETE RESTRICT,
    ADD CONSTRAINT fk_exception_records_assignee
    FOREIGN KEY (assignee_id)
    REFERENCES users (id)
    ON DELETE RESTRICT;

ALTER TABLE audit_logs
    ADD CONSTRAINT fk_audit_logs_actor
    FOREIGN KEY (actor_id)
    REFERENCES users (id)
    ON DELETE RESTRICT,
    ADD CONSTRAINT fk_audit_logs_collection_point
    FOREIGN KEY (collection_point_id)
    REFERENCES collection_points (id)
    ON DELETE RESTRICT;

ALTER TABLE handovers
    ADD CONSTRAINT fk_handovers_parcel
    FOREIGN KEY (parcel_id)
    REFERENCES parcels (id)
    ON DELETE RESTRICT,
    ADD CONSTRAINT fk_handovers_pickup_code
    FOREIGN KEY (pickup_code_id)
    REFERENCES pickup_codes (id)
    ON DELETE RESTRICT,
    ADD CONSTRAINT fk_handovers_operator
    FOREIGN KEY (operator_id)
    REFERENCES users (id)
    ON DELETE RESTRICT,
    ADD CONSTRAINT fk_handovers_audit
    FOREIGN KEY (audit_id)
    REFERENCES audit_logs (id)
    ON DELETE RESTRICT;

ALTER TABLE settings
    ADD CONSTRAINT fk_settings_updated_by
    FOREIGN KEY (updated_by_id)
    REFERENCES users (id)
    ON DELETE RESTRICT,
    ADD CONSTRAINT fk_settings_collection_point
    FOREIGN KEY (collection_point_id)
    REFERENCES collection_points (id)
    ON DELETE RESTRICT;

CREATE INDEX idx_parcels_collection_point_status
    ON parcels (collection_point_id, status);

CREATE INDEX idx_locations_collection_point_status
    ON locations (collection_point_id, status);

CREATE INDEX idx_pickup_codes_status_expires_at
    ON pickup_codes (status, expires_at);

CREATE INDEX idx_exception_records_parcel_id
    ON exception_records (parcel_id);

CREATE INDEX idx_audit_logs_collection_point_created_at
    ON audit_logs (collection_point_id, created_at DESC);

CREATE INDEX idx_audit_logs_target
    ON audit_logs (target_type, target_id);
