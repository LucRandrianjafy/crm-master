package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.easy.to.build.crm.entity.CustomerBudget;

import java.util.List;

@Repository
public interface CustomerBudgetRepository extends JpaRepository<CustomerBudget, Long> {
    List<CustomerBudget> findByCustomerId(Long customerId);
}