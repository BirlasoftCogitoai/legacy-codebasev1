-- Legacy EGP Database Schema
-- Target: PostgreSQL 9.x / Oracle 11g

-- Drop tables if they exist (for clean reinstall)
DROP TABLE IF EXISTS egp_notes CASCADE;
DROP TABLE IF EXISTS egp_case_records CASCADE;
DROP TABLE IF EXISTS egp_customers CASCADE;
DROP TABLE IF EXISTS egp_users CASCADE;
DROP TABLE IF EXISTS egp_audit_log CASCADE;

-- Users table (legacy authentication)
CREATE TABLE egp_users (
    user_id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL, -- MD5 hash (legacy!)
    email VARCHAR(100),
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    role VARCHAR(20) DEFAULT 'USER', -- USER, ADMIN, SUPERVISOR
    active BOOLEAN DEFAULT TRUE,
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- Customers table
CREATE TABLE egp_customers (
    customer_id SERIAL PRIMARY KEY,
    customer_number VARCHAR(20) NOT NULL UNIQUE,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    date_of_birth DATE,
    ssn VARCHAR(11), -- Legacy: stored as plain text (security issue!)
    email VARCHAR(100),
    phone VARCHAR(20),
    address_line1 VARCHAR(100),
    address_line2 VARCHAR(100),
    city VARCHAR(50),
    state VARCHAR(2),
    zip_code VARCHAR(10),
    status VARCHAR(20) DEFAULT 'ACTIVE', -- ACTIVE, INACTIVE, SUSPENDED
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER REFERENCES egp_users(user_id),
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_by INTEGER REFERENCES egp_users(user_id)
);

-- Case Records table
CREATE TABLE egp_case_records (
    case_id SERIAL PRIMARY KEY,
    case_number VARCHAR(20) NOT NULL UNIQUE,
    customer_id INTEGER NOT NULL REFERENCES egp_customers(customer_id),
    case_type VARCHAR(30) NOT NULL, -- BENEFITS, COMPLAINT, INQUIRY, APPEAL
    priority VARCHAR(10) DEFAULT 'MEDIUM', -- LOW, MEDIUM, HIGH, URGENT
    status VARCHAR(20) DEFAULT 'OPEN', -- OPEN, IN_PROGRESS, CLOSED, CANCELLED
    subject VARCHAR(200) NOT NULL,
    description TEXT,
    assigned_to INTEGER REFERENCES egp_users(user_id),
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER REFERENCES egp_users(user_id),
    modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    modified_by INTEGER REFERENCES egp_users(user_id),
    closed_date TIMESTAMP,
    resolution TEXT
);

-- Notes table (case notes)
CREATE TABLE egp_notes (
    note_id SERIAL PRIMARY KEY,
    case_id INTEGER NOT NULL REFERENCES egp_case_records(case_id),
    note_text TEXT NOT NULL,
    note_type VARCHAR(20) DEFAULT 'GENERAL', -- GENERAL, INTERNAL, CUSTOMER_CONTACT
    created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by INTEGER REFERENCES egp_users(user_id),
    is_private BOOLEAN DEFAULT FALSE
);

-- Audit Log table (for compliance)
CREATE TABLE egp_audit_log (
    audit_id SERIAL PRIMARY KEY,
    table_name VARCHAR(50) NOT NULL,
    record_id INTEGER NOT NULL,
    action VARCHAR(10) NOT NULL, -- INSERT, UPDATE, DELETE
    old_values TEXT, -- JSON or serialized data
    new_values TEXT, -- JSON or serialized data
    user_id INTEGER REFERENCES egp_users(user_id),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    user_agent TEXT
);

-- Indexes for performance
CREATE INDEX idx_customers_number ON egp_customers(customer_number);
CREATE INDEX idx_customers_name ON egp_customers(last_name, first_name);
CREATE INDEX idx_customers_ssn ON egp_customers(ssn);
CREATE INDEX idx_cases_number ON egp_case_records(case_number);
CREATE INDEX idx_cases_customer ON egp_case_records(customer_id);
CREATE INDEX idx_cases_status ON egp_case_records(status);
CREATE INDEX idx_cases_assigned ON egp_case_records(assigned_to);
CREATE INDEX idx_notes_case ON egp_notes(case_id);
CREATE INDEX idx_audit_table_record ON egp_audit_log(table_name, record_id);
CREATE INDEX idx_audit_timestamp ON egp_audit_log(timestamp);

-- Legacy sequences (for Oracle compatibility)
-- CREATE SEQUENCE egp_users_seq START WITH 1000;
-- CREATE SEQUENCE egp_customers_seq START WITH 10000;
-- CREATE SEQUENCE egp_cases_seq START WITH 100000;
-- CREATE SEQUENCE egp_notes_seq START WITH 1000000;
-- CREATE SEQUENCE egp_audit_seq START WITH 1;