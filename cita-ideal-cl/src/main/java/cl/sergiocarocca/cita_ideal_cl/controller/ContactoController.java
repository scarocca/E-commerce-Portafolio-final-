package cl.sergiocarocca.cita_ideal_cl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.sergiocarocca.cita_ideal_cl.entity.Consulta;
import cl.sergiocarocca.cita_ideal_cl.repository.ConsultaRepository;
import cl.sergiocarocca.cita_ideal_cl.service.PlanService;

@Controller
public class ContactoController {

    @Autowired
    private ConsultaRepository consultaRepository;
    
    @Autowired
    private PlanService planService;

    @PostMapping("/contacto/enviar")
    public String procesarConsulta(@RequestParam String nombre,
                                   @RequestParam String email,
                                   @RequestParam(required = false) Long planId,
                                   @RequestParam String mensaje,
                                   RedirectAttributes redirect) {
        
        Consulta nuevaConsulta = new Consulta();
        nuevaConsulta.setNombre(nombre);
        nuevaConsulta.setEmail(email);
        nuevaConsulta.setMensaje(mensaje);

        // Si seleccionó un plan, lo buscamos y lo asociamos
        if (planId != null && planId != 0) {
            nuevaConsulta.setPlan(planService.buscarPorId(planId));
        }

        consultaRepository.save(nuevaConsulta);

        // Mensaje de éxito para el usuario
        redirect.addFlashAttribute("mensajeExito", "¡Gracias! Tu consulta ha sido enviada. Te contactaremos pronto.");
        
        return "redirect:/";
    }
}
