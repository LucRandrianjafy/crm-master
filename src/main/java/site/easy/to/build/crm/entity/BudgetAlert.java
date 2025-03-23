package site.easy.to.build.crm.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.groups.Default;
import site.easy.to.build.crm.customValidations.employee.UniqueEmail;

@Entity
@Table(name = "budget_alerts")
public class BudgetAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long id;
    
    @Column(name = "customer_id")
    private Integer customerId;
    
    @Column(name = "expense_id")
    private Integer expenseId;
    
    @Column(name = "ticket_id")
    private Integer ticketId;
    
    @Column(name = "lead_id")
    private Integer leadId;
    
    @Column(name = "message")
    private String message;
    
    @Column(name = "alert_date")
    private LocalDateTime alertDate;
    
    @Column(name = "is_read")
    private Boolean isRead;

    @Transient
    private String formattedAlertDate;

    public String getFormattedAlertDate() {
        return formattedAlertDate;
    }

    public void setFormattedAlertDate(String formattedAlertDate) {
        this.formattedAlertDate = formattedAlertDate;
    }
    
    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getExpenseId() {
        return expenseId;
    }

    public void setExpenseId(Integer expenseId) {
        this.expenseId = expenseId;
    }

    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getLeadId() {
        return leadId;
    }

    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(LocalDateTime alertDate) {
        this.alertDate = alertDate;
    }

    public Boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(Boolean isRead) {
        this.isRead = isRead;
    }
}

