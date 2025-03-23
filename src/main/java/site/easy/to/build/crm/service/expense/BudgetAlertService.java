package site.easy.to.build.crm.service.expense;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.repository.CrudRepository;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import site.easy.to.build.crm.entity.BudgetAlert;
import site.easy.to.build.crm.repository.ExpenseRepository;
import site.easy.to.build.crm.repository.BudgetAlertRepository;
import site.easy.to.build.crm.repository.CustomerRepository;

@Service
public class BudgetAlertService {
    
    @Autowired
    private BudgetAlertRepository budgetAlertRepository;
    
    public List<BudgetAlert> findLatestAlerts() {
        return budgetAlertRepository.findTop5ByOrderByAlertDateDesc();
    }
    
    public BudgetAlert findLatestAlertByTicketId(Integer ticketId) {
        return budgetAlertRepository.findTopByTicketIdOrderByAlertDateDesc(ticketId);
    }
    
    public BudgetAlert findLatestAlertByLeadId(Integer leadId) {
        return budgetAlertRepository.findTopByLeadIdOrderByAlertDateDesc(leadId);
    }
    
    public void delete(Long alertId) {
        Optional<BudgetAlert> alert = budgetAlertRepository.findById(alertId);
        if (alert.isPresent()) {
            budgetAlertRepository.delete(alert.get());
        } else {
            throw new EntityNotFoundException("Alerte non trouvée pour l'ID : " + alertId);
        }
    }
}