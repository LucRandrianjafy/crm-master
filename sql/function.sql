DELIMITER //

DROP TRIGGER IF EXISTS check_budget_alert //

CREATE TRIGGER check_budget_alert AFTER INSERT ON expenses FOR EACH ROW
BEGIN
    DECLARE total_expenses DECIMAL(10,2);
    DECLARE customer_budget DECIMAL(10,2);
    DECLARE alert_threshold DECIMAL(10,2);
    DECLARE budget_percentage DECIMAL(7,2);
    DECLARE customer_id_val INT UNSIGNED;
    DECLARE alert_message VARCHAR(255);
    DECLARE seuil_taux DECIMAL(10,2);
    
    -- Récupérer la valeur du seuil d'alerte la plus récente
    SELECT taux INTO seuil_taux 
    FROM seuil 
    ORDER BY date_seuil DESC 
    LIMIT 1;
    
    -- Valeur par défaut si aucun seuil n'est défini
    IF seuil_taux IS NULL THEN
        SET seuil_taux = 0.20;
    END IF;
    
    -- Déterminer le customer_id selon le type de dépense
    IF NEW.ticket_id IS NOT NULL THEN
        SELECT customer_id INTO customer_id_val 
        FROM trigger_ticket 
        WHERE ticket_id = NEW.ticket_id;
    ELSE
        SELECT customer_id INTO customer_id_val 
        FROM trigger_lead 
        WHERE lead_id = NEW.lead_id;
    END IF;
    
    -- Récupérer le budget CUMULÉ du client jusqu'à la date de la dépense
    SELECT SUM(montant_budget) INTO customer_budget
    FROM customer_budgets
    WHERE customer_id = customer_id_val
    AND date <= NEW.date;  -- Seulement les budgets jusqu'à la date de la dépense
    
    -- Vérifier si le client a un budget défini
    IF customer_budget IS NULL OR customer_budget = 0 THEN
        SET alert_message = CONCAT('Attention: Aucun budget défini pour le client #', customer_id_val,
                               ' - Ticket/Lead ID: ', COALESCE(NEW.ticket_id, NEW.lead_id));
        
        -- Supprimer toutes les alertes dans la table budget_alerts avant d'en insérer une nouvelle
        DELETE FROM budget_alerts;
        
        -- Insérer l'alerte avec l'expense_id et la date d'alerte
        INSERT INTO budget_alerts (customer_id, expense_id, ticket_id, lead_id, message, alert_date)
        VALUES (customer_id_val, NEW.expense_id, NEW.ticket_id, NEW.lead_id, alert_message, NEW.date);
    ELSE
        -- Calcul du seuil d'alerte
        SET alert_threshold = customer_budget * seuil_taux;
        
        -- Calcul des dépenses du client jusqu'à la date de la nouvelle dépense
        SELECT COALESCE(SUM(e.montant), 0) INTO total_expenses
        FROM expenses e
        LEFT JOIN trigger_ticket tt ON e.ticket_id = tt.ticket_id
        LEFT JOIN trigger_lead tl ON e.lead_id = tl.lead_id
        WHERE (
            (tt.customer_id = customer_id_val AND e.ticket_id IS NOT NULL) 
            OR 
            (tl.customer_id = customer_id_val AND e.lead_id IS NOT NULL)
        )
        AND e.date <= NEW.date;  -- Seulement les dépenses jusqu'à la date de la nouvelle dépense
        
        -- Éviter la division par zéro
        IF customer_budget > 0 THEN
            SET budget_percentage = LEAST((total_expenses / customer_budget) * 100, 999.99);
        ELSE
            SET budget_percentage = 0;
        END IF;
        
        -- Vérifier si le total des dépenses atteint ou dépasse le seuil
        IF total_expenses >= alert_threshold THEN
            IF total_expenses > alert_threshold THEN
                -- Dépassement du budget
                SET alert_message = CONCAT('ALERTE: Vous #', customer_id_val,
                                    ' avez DEPASSE votre budget.');
            ELSE
                -- Budget atteint
                SET alert_message = CONCAT('ATTENTION: Vous #', customer_id_val,
                                    ' avez ATTEINT votre budget.');
            END IF;
            
            -- Supprimer toutes les alertes dans la table budget_alerts avant d'en insérer une nouvelle
            DELETE FROM budget_alerts;
            
            -- Insérer l'alerte dans la table budget_alerts
            INSERT INTO budget_alerts (customer_id, expense_id, ticket_id, lead_id, message, alert_date)
            VALUES (customer_id_val, NEW.expense_id, NEW.ticket_id, NEW.lead_id, alert_message, NEW.date);
        END IF;
    END IF;
END //

DELIMITER ;

-- 3. Procédure pour vérifier les alertes (à appeler après chaque insertion)
DELIMITER //

CREATE PROCEDURE check_latest_alerts()
BEGIN
    SELECT * FROM budget_alerts
    ORDER BY alert_date DESC
    LIMIT 5;
END //

DELIMITER ;

-- 4 DELETE
DELIMITER $$