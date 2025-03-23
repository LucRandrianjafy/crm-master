package site.easy.to.build.crm.service.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import site.easy.to.build.crm.entity.CustomerBudget;
import site.easy.to.build.crm.repository.CustomerBudgetRepository;
import site.easy.to.build.crm.repository.CustomerRepository;

import java.util.List;

@Service
public class CustomerBudgetService {
    
    @Autowired
    private CustomerBudgetRepository customerBudgetRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    public List<CustomerBudget> getAllBudgets() {
        return customerBudgetRepository.findAll();
    }
    
    public CustomerBudget getBudgetById(Long id) {
        return customerBudgetRepository.findById(id).orElse(null);
    }
    
    public List<CustomerBudget> getBudgetsByCustomerId(Long customerId) {
        return customerBudgetRepository.findByCustomerId(customerId);
    }
    
    public CustomerBudget saveBudget(CustomerBudget customerBudget) {
        return customerBudgetRepository.save(customerBudget);
    }
    
    public void deleteBudget(Long id) {
        customerBudgetRepository.deleteById(id);
    }
}