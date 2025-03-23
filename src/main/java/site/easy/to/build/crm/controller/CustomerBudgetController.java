package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.CustomerBudget;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.service.customer.CustomerBudgetService;

@Controller
@RequestMapping("/budgets")
public class CustomerBudgetController {
    
    @Autowired
    private CustomerBudgetService customerBudgetService;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @GetMapping
    public String listBudgets(Model model) {
        model.addAttribute("budgets", customerBudgetService.getAllBudgets());
        return "budget/list";
    }
    
    @GetMapping("/new")
    public String showNewBudgetForm(Model model) {
        model.addAttribute("budget", new CustomerBudget());
        model.addAttribute("customers", customerRepository.findAll());
        return "budget/form";
    }
    
    @PostMapping("/save")
    public String saveBudget(@ModelAttribute("budget") CustomerBudget budget) {
        customerBudgetService.saveBudget(budget);
        return "redirect:/budgets";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditBudgetForm(@PathVariable Long id, Model model) {
        CustomerBudget budget = customerBudgetService.getBudgetById(id);
        model.addAttribute("budget", budget);
        model.addAttribute("customers", customerRepository.findAll());
        return "budget/form";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteBudget(@PathVariable Long id) {
        customerBudgetService.deleteBudget(id);
        return "redirect:/budgets";
    }
}