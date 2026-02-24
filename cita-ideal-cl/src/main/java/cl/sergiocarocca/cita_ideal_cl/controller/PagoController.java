package cl.sergiocarocca.cita_ideal_cl.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cl.sergiocarocca.cita_ideal_cl.entity.ItemCarrito;
import cl.sergiocarocca.cita_ideal_cl.entity.Reserva;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/pago")
public class PagoController {

   
    @PostMapping("/procesar")
    public String mostrarPasarela(@ModelAttribute Reserva reserva, 
                                 @RequestParam Long planId,
                                 @RequestParam String fechaSolo,
                                 @RequestParam String horaFija,
                                 Model model) {
        
        model.addAttribute("reserva", reserva);
        model.addAttribute("planId", planId);
        // Enviamos ambos por separado o puedes unirlos aquí mismo
        model.addAttribute("fechaSolo", fechaSolo);
        model.addAttribute("horaFija", horaFija);
        
        return "public/pasarela-simulada";
    }

    // Paso 2: Recibe los datos de la tarjeta y muestra el spinner de carga
    @PostMapping("/confirmar-final")
    public String procesarEspera(@ModelAttribute Reserva reserva, 
                                @RequestParam Long planId,
                                @RequestParam String fechaSolo,
                                @RequestParam String horaFija,
                                Model model) {
        // Pasamos los datos que el formulario invisible en 'pago-procesando.html' necesitará
        model.addAttribute("reserva", reserva);
        model.addAttribute("planId", planId);
        model.addAttribute("fechaSolo", fechaSolo);
        model.addAttribute("horaFija", horaFija);
        
        return "public/pago-procesando"; 
    }
    @PostMapping("/procesar-carrito")
    public String mostrarPasarelaCarrito(@ModelAttribute Reserva reserva, 
    		                             @RequestParam String fechaSolo,
                                         @RequestParam String horaFija,
                                        HttpSession session,
                                        Model model) {
        
        
    	// Recuperamos el carrito para mostrar el total en la pasarela si quisiéramos
    	@SuppressWarnings("unchecked")
    	List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        
        model.addAttribute("reserva", reserva);
        model.addAttribute("fechaSolo", fechaSolo);
        model.addAttribute("horaFija", horaFija);
        model.addAttribute("esCarrito", true); // Bandera para saber que son varios items
        
        return "public/pasarela-simulada";
    }
    @PostMapping("/confirmar-final-carrito")
    public String procesarEsperaCarrito(@ModelAttribute Reserva reserva, 
    		                            @RequestParam String fechaSolo,
                                        @RequestParam String horaFija,
                                       Model model) {
        
        // Pasamos los datos al spinner
        model.addAttribute("reserva", reserva);
        model.addAttribute("fechaSolo", fechaSolo);
        model.addAttribute("horaFija", horaFija);
        model.addAttribute("esCarrito", true); // Indicamos que es una reserva múltiple
        
        return "public/pago-procesando"; 
    }
}