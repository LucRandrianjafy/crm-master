package site.easy.to.build.crm.controller.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.util.AuthenticationUtils;
import site.easy.to.build.crm.util.AuthorizationUtil;
import site.easy.to.build.crm.entity.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardApiController {

    private final TicketService ticketService;
    private final LeadService leadService;
    private final AuthenticationUtils authenticationUtils;

    @Autowired
    public DashboardApiController(TicketService ticketService, LeadService leadService,
                                 AuthenticationUtils authenticationUtils) {
        this.ticketService = ticketService;
        this.leadService = leadService;
        this.authenticationUtils = authenticationUtils;
    }

    @GetMapping("/counts")
    public ResponseEntity<?> getDashboardCounts(Authentication authentication) {
        try {
            int userId = authenticationUtils.getLoggedInUserId(authentication);
            Map<String, Object> response = new HashMap<>();

            List<Ticket> tickets;
            List<Lead> leads;

            // Vérification du rôle de l'utilisateur
            if (AuthorizationUtil.hasRole(authentication, "ROLE_CUSTOMER")) {
                String email = authenticationUtils.getOAuthUserFromAuthentication(authentication).getEmail();
                
                // Récupérer tous les tickets et leads
                tickets = ticketService.findAll();
                leads = leadService.findAll();
            } else {
                tickets = ticketService.findAll();
                leads = leadService.findAll();
            }

            // Mettre toutes les données dans la réponse
            response.put("totalTickets", tickets.size());
            response.put("totalLeads", leads.size());
            response.put("tickets", tickets);  // Envoi de tous les tickets
            response.put("leads", leads);      // Envoi de tous les leads

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                .body("Error retrieving data: " + e.getMessage());
        }
    }

}