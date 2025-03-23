package site.easy.to.build.crm.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.entity.Expense;
import site.easy.to.build.crm.service.expense.ExpenseService;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseApiController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseApiController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public ResponseEntity<?> listExpenses() {
        try {
            // Récupérer la liste des dépenses depuis le service
            List<Expense> expenses = expenseService.getAllExpenses();

            if (expenses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No expenses found");
            }

            // Retourner la liste des dépenses dans la réponse
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            // Si une erreur survient, retourner une réponse d'erreur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving expenses: " + e.getMessage());
        }
    }
}
