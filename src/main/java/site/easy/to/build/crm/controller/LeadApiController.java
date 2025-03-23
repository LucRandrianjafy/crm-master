package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.entity.Lead;

import java.util.List;

@RestController
public class LeadApiController {

    @Autowired
    private LeadService leadService;

    @GetMapping("/api/leads")
    public List<Lead> getAllLeads() {
        return leadService.findAll();
    }
}
