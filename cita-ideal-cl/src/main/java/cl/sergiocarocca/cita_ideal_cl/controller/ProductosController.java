package cl.sergiocarocca.cita_ideal_cl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cl.sergiocarocca.cita_ideal_cl.entity.Plan;
import cl.sergiocarocca.cita_ideal_cl.service.PlanService;

@Controller
public class ProductosController {
	@Autowired
    private PlanService planService;

    @GetMapping("/productos")
    public String verPaginaDeProductos(Model model) {
        // 1. Obtenemos los datos del servicio
        List<Plan> listaPlanes = planService.listarTodos().stream().filter(Plan::isActivo).toList();
        
        // 2. Pasamos la lista al HTML usando el "model"
        // El primer parámetro "planes" es el nombre que usaremos en Thymeleaf
        model.addAttribute("listadoDePlanes", listaPlanes);
        
        // 3. Devolvemos el nombre del archivo HTML (sin el .html)
        return "productos"; 
    }
}
