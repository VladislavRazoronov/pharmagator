ALTER TABLE prices ADD CONSTRAINT mecicine_id_key
FOREIGN KEY (medicine_id) REFERENCES medicines(id);

ALTER TABLE prices ADD CONSTRAINT pharmacy_id_key
FOREIGN KEY (pharmacy_id) REFERENCES pharmacies(id);