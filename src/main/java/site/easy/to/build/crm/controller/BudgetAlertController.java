package site.easy.to.build.crm.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import site.easy.to.build.crm.entity.OAuthUser;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.service.expense.BudgetAlertService;
import site.easy.to.build.crm.entity.BudgetAlert;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class BudgetAlertController {
    
    private final BudgetAlertService budgetAlertService;
    
    @Autowired
    public BudgetAlertController(BudgetAlertService budgetAlertService) {
        this.budgetAlertService = budgetAlertService;
    }
    
    @GetMapping("/alerts")
    public String showAlerts(Model model) {
        List<BudgetAlert> alerts = budgetAlertService.findLatestAlerts();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        alerts.forEach(alert -> {
            if (alert.getAlertDate() != null) {
                alert.setFormattedAlertDate(alert.getAlertDate().format(formatter));
            }
        });
        model.addAttribute("alerts", alerts);
        return "alert/list";
    }
    
}