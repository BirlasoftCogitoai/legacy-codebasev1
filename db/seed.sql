-- Legacy EGP Database Seed Data
-- Sample data for development and testing

-- Insert sample users (passwords are MD5 hashed - legacy security!)
INSERT INTO egp_users (username, password_hash, email, first_name, last_name, role, active) VALUES
('admin', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'admin@egp.gov', 'System', 'Administrator', 'ADMIN', true),
('jsmith', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'john.smith@egp.gov', 'John', 'Smith', 'SUPERVISOR', true),
('mjohnson', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'mary.johnson@egp.gov', 'Mary', 'Johnson', 'USER', true),
('bwilson', '8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92', 'bob.wilson@egp.gov', 'Bob', 'Wilson', 'USER', true),
('slee', 'e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855', 'susan.lee@egp.gov', 'Susan', 'Lee', 'USER', false);

-- Insert sample customers
INSERT INTO egp_customers (customer_number, first_name, last_name, date_of_birth, ssn, email, phone, 
                          address_line1, city, state, zip_code, status, created_by, modified_by) VALUES
('CUST-001001', 'Alice', 'Anderson', '1975-03-15', '123-45-6789', 'alice.anderson@email.com', '555-0101', 
 '123 Main St', 'Springfield', 'IL', '62701', 'ACTIVE', 2, 2),
('CUST-001002', 'Robert', 'Brown', '1982-07-22', '234-56-7890', 'robert.brown@email.com', '555-0102', 
 '456 Oak Ave', 'Springfield', 'IL', '62702', 'ACTIVE', 2, 2),
('CUST-001003', 'Carol', 'Davis', '1968-11-08', '345-67-8901', 'carol.davis@email.com', '555-0103', 
 '789 Pine St', 'Decatur', 'IL', '62521', 'ACTIVE', 3, 3),
('CUST-001004', 'David', 'Evans', '1990-01-30', '456-78-9012', 'david.evans@email.com', '555-0104', 
 '321 Elm Dr', 'Peoria', 'IL', '61601', 'INACTIVE', 3, 3),
('CUST-001005', 'Emma', 'Foster', '1955-09-12', '567-89-0123', 'emma.foster@email.com', '555-0105', 
 '654 Maple Ln', 'Rockford', 'IL', '61101', 'SUSPENDED', 4, 4);

-- Insert sample case records
INSERT INTO egp_case_records (case_number, customer_id, case_type, priority, status, subject, description, 
                             assigned_to, created_by, modified_by) VALUES
('CASE-2024-001', 1, 'BENEFITS', 'HIGH', 'OPEN', 'Disability Benefits Application', 
 'Customer applying for disability benefits due to work-related injury', 2, 2, 2),
('CASE-2024-002', 2, 'COMPLAINT', 'MEDIUM', 'IN_PROGRESS', 'Service Delay Complaint', 
 'Customer complaining about delays in processing previous application', 3, 3, 3),
('CASE-2024-003', 1, 'INQUIRY', 'LOW', 'CLOSED', 'General Information Request', 
 'Customer requesting information about available programs', 4, 4, 4),
('CASE-2024-004', 3, 'APPEAL', 'URGENT', 'OPEN', 'Benefits Denial Appeal', 
 'Customer appealing denial of unemployment benefits', 2, 2, 2),
('CASE-2024-005', 4, 'BENEFITS', 'MEDIUM', 'CANCELLED', 'Housing Assistance Application', 
 'Application cancelled due to incomplete documentation', 3, 3, 3);

-- Insert sample notes
INSERT INTO egp_notes (case_id, note_text, note_type, created_by, is_private) VALUES
(1, 'Initial application received and reviewed. Medical documentation required.', 'GENERAL', 2, false),
(1, 'Medical records requested from Dr. Johnson office.', 'INTERNAL', 2, true),
(1, 'Customer called to check status. Informed about pending medical records.', 'CUSTOMER_CONTACT', 3, false),
(2, 'Complaint logged. Investigating processing delays in department X.', 'GENERAL', 3, false),
(2, 'Spoke with department supervisor. Issue identified and being resolved.', 'INTERNAL', 3, true),
(3, 'Provided customer with program brochures and eligibility requirements.', 'CUSTOMER_CONTACT', 4, false),
(3, 'Case closed - information provided successfully.', 'GENERAL', 4, false),
(4, 'Appeal documentation received. Scheduling review meeting.', 'GENERAL', 2, false),
(4, 'URGENT: Customer facing eviction. Expedite review process.', 'INTERNAL', 2, true),
(5, 'Missing required income verification documents.', 'GENERAL', 3, false),
(5, 'Customer failed to provide documents within 30-day deadline.', 'GENERAL', 3, false);

-- Insert sample audit log entries
INSERT INTO egp_audit_log (table_name, record_id, action, old_values, new_values, user_id, ip_address, user_agent) VALUES
('egp_customers', 1, 'INSERT', NULL, '{"customer_number":"CUST-001001","first_name":"Alice","last_name":"Anderson"}', 2, '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'),
('egp_case_records', 1, 'INSERT', NULL, '{"case_number":"CASE-2024-001","customer_id":1,"case_type":"BENEFITS"}', 2, '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'),
('egp_case_records', 1, 'UPDATE', '{"status":"OPEN"}', '{"status":"IN_PROGRESS"}', 2, '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'),
('egp_notes', 1, 'INSERT', NULL, '{"case_id":1,"note_text":"Initial application received"}', 2, '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)'),
('egp_customers', 4, 'UPDATE', '{"status":"ACTIVE"}', '{"status":"INACTIVE"}', 3, '192.168.1.101', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64)');

-- Update timestamps to be more realistic
UPDATE egp_users SET created_date = CURRENT_TIMESTAMP - INTERVAL '6 months', 
                     last_login = CURRENT_TIMESTAMP - INTERVAL '1 day' WHERE user_id <= 4;

UPDATE egp_customers SET created_date = CURRENT_TIMESTAMP - INTERVAL '3 months',
                        modified_date = CURRENT_TIMESTAMP - INTERVAL '1 week';

UPDATE egp_case_records SET created_date = CURRENT_TIMESTAMP - INTERVAL '2 weeks',
                           modified_date = CURRENT_TIMESTAMP - INTERVAL '2 days';

UPDATE egp_notes SET created_date = CURRENT_TIMESTAMP - INTERVAL '1 week';

-- Set some cases as closed with resolution
UPDATE egp_case_records SET status = 'CLOSED', 
                           closed_date = CURRENT_TIMESTAMP - INTERVAL '3 days',
                           resolution = 'Customer inquiry resolved. Information provided via email and postal mail.'
WHERE case_id = 3;