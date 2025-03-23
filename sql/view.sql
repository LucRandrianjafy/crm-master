CREATE OR REPLACE VIEW v_expenses_lead AS
SELECT 
    e.expense_id,
    e.montant,
    e.date,
    tl.lead_id,
    tl.customer_id,
    tl.name AS lead_name,
    tl.status AS lead_status,
    tl.created_at AS lead_created_at,
    c.name AS customer_name,
    u.id AS user_id,
    MONTH(e.date) AS expense_month,
    YEAR(e.date) AS expense_year,
    DATEDIFF(e.date, tl.created_at) AS days_from_creation
FROM 
    expenses e
    INNER JOIN trigger_lead tl ON e.lead_id = tl.lead_id
    LEFT JOIN customer c ON tl.customer_id = c.customer_id
    LEFT JOIN users u ON tl.user_id = u.id
WHERE 
    e.lead_id IS NOT NULL
ORDER BY 
    e.date DESC;


-- MOYENNE par CLIENT
SELECT customer_name, AVG(montant) AS avg_expense, SUM(montant) AS total_expenses
FROM v_expenses_lead
GROUP BY customer_id, customer_name
ORDER BY total_expenses DESC;