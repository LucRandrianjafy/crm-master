package site.easy.to.build.crm.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.io.StringWriter;
import java.io.PrintWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import site.easy.to.build.crm.service.database.CsvService;
import site.easy.to.build.crm.service.employee.EmployeeService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.dto.EmployeeDto;
import site.easy.to.build.crm.entity.Employee;
import site.easy.to.build.crm.dto.UsersDto;
import site.easy.to.build.crm.entity.User;

@Controller
public class ImportController {
    
    @Autowired
    private CsvService csvService;
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private UserService userService;

    @RequestMapping("/import/test")
    public String handleImportTest() {
        return "import/test";
    }
    
    @PostMapping("/database/import")
    public String importEspace(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Erreur : Aucun fichier sélectionné.");
            return "import/test";
        }

        try {
            Path tempFile = Files.createTempFile("csv_upload_", file.getOriginalFilename());
            Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

            List<EmployeeDto> importedData = csvService.importCsv(tempFile.toString(), EmployeeDto.class);
            List<Employee> savedEmployees = new ArrayList<>();

            for (EmployeeDto employeeDto : importedData) {
                Employee newEmployee = new Employee();
                newEmployee.setEmail(employeeDto.getEmail());
                newEmployee.setEmployeeId(employeeDto.getId());
                newEmployee.setFirstName(employeeDto.getFirstName());
                newEmployee.setLastName(employeeDto.getLastName());
                newEmployee.setPassword(employeeDto.getPassword());
                newEmployee.setProvider(employeeDto.getProvider());
                newEmployee.setUsername(employeeDto.getUsername());
                savedEmployees.add(employeeService.save(newEmployee));
            }

            Files.deleteIfExists(tempFile);

            model.addAttribute("message", "Importation réussie : " + savedEmployees.size() + " employés ajoutés.");

        } catch (Exception e) {
            model.addAttribute("message", "Erreur lors de l'importation : " + e.getMessage());
        }
        return "import/test";
    }

    // @PostMapping("/database/import")
    // public String importUsers(@RequestParam("file") MultipartFile file, Model model) {
    //     if (file.isEmpty()) {
    //         model.addAttribute("message", "Erreur : Aucun fichier sélectionné.");
    //         return "import/test";
    //     }

    //     try {
    //         Path tempFile = Files.createTempFile("csv_upload_", file.getOriginalFilename());
    //         Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

    //         List<UsersDto> importedData = csvService.importCsv(tempFile.toString(), UsersDto.class);
    //         List<User> savedUsers = new ArrayList<>();

    //         for (UsersDto userDto : importedData) {
    //             User newUser = new User();
    //             newUser.setEmail(userDto.getEmail());
    //             newUser.setId(userDto.getId());
    //             newUser.setUsername(userDto.getUsername());
    //             newUser.setPassword(userDto.getPassword());
    //             newUser.setStatus(userDto.getStatus());
    //             newUser.setToken(userDto.getToken());
    //             newUser.setIsPasswordSet(userDto.getIsPasswordSet());

    //             savedUsers.add(userService.save(newUser));
    //         }

    //         Files.deleteIfExists(tempFile);

    //         model.addAttribute("message", "Importation réussie : " + savedUsers.size() + " utilisateurs ajoutés.");

    //     } catch (Exception e) {
    //         StringWriter sw = new StringWriter();
    //         e.printStackTrace(new PrintWriter(sw));
    //         String exceptionAsString = sw.toString();
    //         model.addAttribute("message", "Erreur lors de l'importation : " + exceptionAsString);
    //     }
    //     return "import/test";
    // }


}
