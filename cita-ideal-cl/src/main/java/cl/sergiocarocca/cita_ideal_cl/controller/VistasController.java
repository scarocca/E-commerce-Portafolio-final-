package cl.sergiocarocca.cita_ideal_cl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model; // <--- VERIFICA ESTA IMPORTACIÓN
import org.springframework.web.bind.annotation.GetMapping;
import cl.sergiocarocca.cita_ideal_cl.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class VistasController {

    @Autowired
    private PlanService planService;

    @GetMapping("/")
    public String index(Model model) { // Spring inyectará el Model automáticamente aquí
        model.addAttribute("planes", planService.listarTodos());
        return "index";
    }
}