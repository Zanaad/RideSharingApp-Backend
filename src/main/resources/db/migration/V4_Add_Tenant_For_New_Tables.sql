
ALTER TABLE feedback
    ADD COLUMN company_id INTEGER;
ALTER TABLE feedback
    ADD CONSTRAINT fk_feedback_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE discount_code
    ADD COLUMN company_id INTEGER;
ALTER TABLE discount_code
    ADD CONSTRAINT fk_discount_code_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE trip_cancellation
    ADD COLUMN company_id INTEGER;
ALTER TABLE trip_cancellation
    ADD CONSTRAINT fk_trip_cancellation_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE login_history
    ADD COLUMN company_id INTEGER;
ALTER TABLE login_history
    ADD CONSTRAINT fk_login_history_company FOREIGN KEY (company_id) REFERENCES companies (id);

ALTER TABLE chat_message
    ADD COLUMN company_id INTEGER;
ALTER TABLE chat_message
    ADD CONSTRAINT fk_chat_message_company FOREIGN KEY (company_id) REFERENCES companies (id);


ALTER TABLE location
    ADD COLUMN company_id INTEGER;
ALTER TABLE location
    ADD CONSTRAINT fk_location_company FOREIGN KEY (company_id) REFERENCES companies (id);


ALTER TABLE driver_license
    ADD COLUMN company_id INTEGER;
ALTER TABLE driver_license
    ADD CONSTRAINT fk_driver_license_company FOREIGN KEY (company_id) REFERENCES companies (id);
