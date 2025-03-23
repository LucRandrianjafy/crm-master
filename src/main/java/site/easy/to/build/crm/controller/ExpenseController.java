package site.easy.to.build.crm.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import site.easy.to.build.crm.service.expense.ExpenseService;
import site.easy.to.build.crm.service.customer.CustomerBudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.expense.BudgetAlertService;

import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.BudgetAlert;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.dao.DataIntegrityViolationException;

@Controller
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private TicketService ticketService;
    
    @Autowired
    private LeadService leadService;
    
    @Autowired
    private BudgetAlertService budgetAlertService;


    @GetMapping("/expenses")
    public String listExpenses(Model model) {
        model.addAttribute("expenses", expenseService.getAllExpenses());
        return "expense/list";
    }

    @GetMapping("/expense/new-ticket")
    public String ticketForm(Model model) {
        model.addAttribute("tickets", ticketService.findAll());
        return "expense/ticket-form";
    }

    @GetMapping("/expense/new-lead")
    public String leadForm(Model model) {
        model.addAttribute("leads", leadService.findAll());
        return "expense/lead-form";
    }

    @PostMapping("/expense/save-ticket")
    public String saveTicket(@RequestParam("ticketId") int ticketId,
                            @RequestParam("montant") BigDecimal montant,
                            @RequestParam("date") LocalDate date,
                            RedirectAttributes redirectAttributes) {
        try {
            // Récupérer le ticket
            Ticket ticket = ticketService.findByTicketId(ticketId);
            
            // Créer et enregistrer la dépense
            Expense expense = new Expense();
            expense.setTicket(ticket);
            expense.setMontant(montant);
            expense.setDate(date);
            expenseService.save(expense);
            
            // Vérifier la dernière alerte pour ce ticket
            BudgetAlert latestAlert = budgetAlertService.findLatestAlertByTicketId(ticketId);
            
            if (latestAlert != null) {
                String message = latestAlert.getMessage();
                System.out.println("Message d'alerte récupéré: " + message); // Debug
                
                // Ajouter le message de l'alerte aux attributs de redirection
                redirectAttributes.addFlashAttribute("alertMessage", message);
                
                // Vérifier si c'est une alerte de dépassement ou d'atteinte du budget
                if (message != null) {
                    if (message.contains("ALERTE:") || message.contains("DEPASSE")) {
                        redirectAttributes.addFlashAttribute("alertType", "danger");
                    } else {
                        redirectAttributes.addFlashAttribute("alertType", "info");
                    }
                }
            } else {
                // Si aucune alerte, afficher un message de succès
                redirectAttributes.addFlashAttribute("alertMessage", "Dépense enregistrée avec succès !");
                redirectAttributes.addFlashAttribute("alertType", "success");
            }
        } catch (Exception e) {
            // Gérer les exceptions
            redirectAttributes.addFlashAttribute("alertMessage", "Une erreur est survenue lors de l'enregistrement : " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertType", "error");
        }
        return "redirect:/expenses";
    }


    @PostMapping("/expense/save-lead")
    public String saveLeadExpense(@RequestParam("leadId") int leadId,
                                @RequestParam("montant") BigDecimal montant,
                                @RequestParam("date") LocalDate date,
                                RedirectAttributes redirectAttributes) {
        try {
            Lead lead = leadService.findByLeadId(leadId);
            
            Expense expense = new Expense();
            expense.setLead(lead);
            expense.setMontant(montant);
            expense.setDate(date);
            expenseService.save(expense);
            
            // Vérifier la dernière alerte pour ce lead
            BudgetAlert latestAlert = budgetAlertService.findLatestAlertByLeadId(leadId);
            
            if (latestAlert != null) {
                String message = latestAlert.getMessage();
                System.out.println("Message d'alerte récupéré: " + message); // Debug
                
                redirectAttributes.addFlashAttribute("alertMessage", message);

                // Vérifier si c'est une alerte de dépassement ou d'atteinte du budget
                if (message != null) {
                    if (message.contains("ALERTE:") || message.contains("DEPASSE")) {
                        redirectAttributes.addFlashAttribute("alertType", "danger");
                    } else {
                        redirectAttributes.addFlashAttribute("alertType", "info");
                    }
                }
            } else {
                redirectAttributes.addFlashAttribute("alertMessage", "Dépense enregistrée avec succès !");
                redirectAttributes.addFlashAttribute("alertType", "success");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("alertMessage", "Une erreur est survenue : " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertType", "error");
        }
        return "redirect:/expenses";
    }

    @PostMapping("/expense/delete")
    public String deleteExpense(@RequestParam("expenseId") Long expenseId, @RequestParam("alertId") Long alertId, RedirectAttributes redirectAttributes) {
        try {
            expenseService.delete(expenseId);
            budgetAlertService.delete(alertId);
            
            redirectAttributes.addFlashAttribute("alertMessage", "Dépense annulée !");
            redirectAttributes.addFlashAttribute("alertType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("alertMessage", "Une erreur est survenue lors de la suppression : " + e.getMessage());
            redirectAttributes.addFlashAttribute("alertType", "error");
        }
        return "redirect:/expenses";
    }

    
}