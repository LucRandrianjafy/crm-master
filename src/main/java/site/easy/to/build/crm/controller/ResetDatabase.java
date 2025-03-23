package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import site.easy.to.build.crm.service.database.DatabaseResetter;

@Controller
@RequestMapping("/database/settings")
public class ResetDatabase {

    @Autowired
    private DatabaseResetter databaseResetter;

    @GetMapping("/reset-database")
    public String resetDatabase(RedirectAttributes redirectAttributes) {
        databaseResetter.resetDatabase();
        redirectAttributes.addFlashAttribute("message", "Les données ont été réinitialisées !");
        return "redirect:/database/settings/reset"; 
    }

    @GetMapping("/reset")
    public String showResetPage() {
        return "data_managing/reset";
    }
}