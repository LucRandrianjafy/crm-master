package site.easy.to.build.crm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.BudgetAlert;
import site.easy.to.build.crm.entity.Customer;

import java.util.List;

public interface BudgetAlertRepository extends JpaRepository<BudgetAlert, Long> {
    List<BudgetAlert> findTop5ByOrderByAlertDateDesc();
    
    BudgetAlert findTopByTicketIdOrderByAlertDateDesc(Integer ticketId);
    
    BudgetAlert findTopByLeadIdOrderByAlertDateDesc(Integer leadId);
    
    List<BudgetAlert> findByCustomerIdOrderByAlertDateDesc(Integer customerId);
    
    List<BudgetAlert> findByIsReadFalseOrderByAlertDateDesc();
}