package cl.sergiocarocca.cita_ideal_cl.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cl.sergiocarocca.cita_ideal_cl.entity.Foto;
import cl.sergiocarocca.cita_ideal_cl.service.FotoService;

@Controller
public class GaleriaController {

	@Autowired // <-- Agrega esto
    private FotoService fotoService;
    public GaleriaController(FotoService fotoService) {
        this.fotoService = fotoService;
    }

    @GetMapping("/galeria")
    public String verGaleria(Model model) {
        // Asegúrate de que fotoService esté inyectado con @Autowired
        List<Foto> lista = fotoService.listarTodas();
        
        // Si la lista es null, Thymeleaf falla al intentar hacer el th:each
        if (lista == null) {
            lista = new ArrayList<>();
        }
        
        model.addAttribute("fotos", lista); // Este nombre "fotos" debe ser igual al del th:each
        return "galeria"; 
    }
}
