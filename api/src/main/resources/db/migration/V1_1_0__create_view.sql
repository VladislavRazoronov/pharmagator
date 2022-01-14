CREATE MATERIALIZED VIEW advanced_search_view AS
SELECT p.medicine_id, p.pharmacy_id, p.price, m.title AS medicine, ph.name AS pharmacy
FROM prices p
LEFT JOIN medicines m ON m.id = p.medicine_id
LEFT JOIN pharmacies ph ON ph.id = p.pharmacy_id;
