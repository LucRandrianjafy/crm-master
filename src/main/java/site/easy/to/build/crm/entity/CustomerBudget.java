package site.easy.to.build.crm.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "customer_budgets")
public class CustomerBudget {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "budget_id")
    private Long budgetId;
    
    @Column(name = "customer_id", nullable = false)
    private Long customerId;
    
    @ManyToOne
    @JoinColumn(name = "customer_id", insertable = false, updatable = false)
    private Customer customer;
    
    @Column(name = "montant_budget")
    private BigDecimal montantBudget;
    
    @Column(name = "date")
    private LocalDate date;
}