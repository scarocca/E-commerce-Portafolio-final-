package cl.sergiocarocca.cita_ideal_cl.controller;

import cl.sergiocarocca.cita_ideal_cl.entity.ItemCarrito;
import cl.sergiocarocca.cita_ideal_cl.entity.Plan;
import cl.sergiocarocca.cita_ideal_cl.entity.Reserva;
import cl.sergiocarocca.cita_ideal_cl.service.PlanService;
import cl.sergiocarocca.cita_ideal_cl.service.ReservaService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private PlanService planService;

    // 1. Mostrar el formulario de reserva para un plan específico
    @GetMapping("/nuevo/{planId}")
    public String mostrarFormularioReserva(@PathVariable Long planId, Model model, RedirectAttributes flash) {
        Plan planEncontrado = planService.buscarPorId(planId);
        
        // Si el ID no existe en la BD, evitamos el crash
        if (planEncontrado == null) {
            flash.addFlashAttribute("mensajeError", "El plan seleccionado no existe.");
            return "redirect:/productos";
        }

        Reserva reserva = new Reserva();
        reserva.setPlan(planEncontrado); // Vinculamos el plan a la reserva
        
        // PASO VITAL: Enviamos ambos objetos con nombres claros
        model.addAttribute("plan", planEncontrado); 
        model.addAttribute("reserva", reserva);
        
        return "public/reserva-form";
    }

    @PostMapping("/guardar")
    public String guardarReserva(@ModelAttribute Reserva reserva, 
                                @RequestParam(value = "planId", required = false) Long planId, // Cambiado a required = false
                                @RequestParam("fechaSolo") String fechaSolo, // Nuevo: recibe YYYY-MM-DD
                                @RequestParam("horaFija") String horaFija,
                                Model model,
                                RedirectAttributes flash) {
        try {
            // Si el planId es null, significa que venimos del flujo de carrito
            if (planId == null) {
                // Podrías redirigir al método de confirmar-todo si algo salió mal
                return "redirect:/reservas/confirmar-todo"; 
            }

            Plan plan = planService.buscarPorId(planId);
            reserva.setPlan(plan);

            // UNIÓN VITAL: Combinamos los dos parámetros con una "T" al medio
            String fechaCompleta = fechaSolo + "T" + horaFija; 
            reserva.setFechaCita(LocalDateTime.parse(fechaCompleta));
            
            reservaService.crearReserva(reserva);
            
            model.addAttribute("reserva", reserva);
            model.addAttribute("nombreCliente", reserva.getNombreCliente());
            
            return "public/reserva-exito";
            
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/productos";
        }
    }
    @PostMapping("/confirmar-todo")
    public String confirmarTodo(HttpSession session, 
                                @ModelAttribute Reserva datosCliente,
                                @RequestParam("fechaSolo") String fechaSolo, 
                                @RequestParam("horaFija") String horaFija,    
                                Model model,
                              RedirectAttributes flash) {
    	@SuppressWarnings("unchecked")
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        
        try {
        	String fechaCompleta = fechaSolo + "T" + horaFija;
            LocalDateTime fecha = LocalDateTime.parse(fechaCompleta);
            
            // El servicio ahora nos devolverá la lista de reservas creadas
            List<Reserva> reservasRealizadas = reservaService.guardarReservaMultiple(carrito, fecha, datosCliente);
            
            session.removeAttribute("carrito");
            
            // Pasamos las reservas al modelo para la página de éxito
            model.addAttribute("reservas", reservasRealizadas);
            model.addAttribute("nombreCliente", datosCliente.getNombreCliente());
            
            return "public/reserva-exito"; // Nueva página
            
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", e.getMessage());
            return "redirect:/carrito/checkout";
        }
    }
 // 4. Listar todas las reservas (Panel de Administración)
    @GetMapping("/admin/listar")
    public String listarReservas(Model model) {
        List<Reserva> lista = reservaService.listarTodas();
        model.addAttribute("reservas", lista);
        return "admin/reservas-list"; // Asegúrate de tener esta vista
    }

    // 5. Eliminar una reserva
    @GetMapping("/admin/eliminar/{id}")
    public String eliminarReserva(@PathVariable Long id, RedirectAttributes flash) {
        try {
            reservaService.eliminar(id);
            flash.addFlashAttribute("mensajeExito", "La reserva ha sido eliminada correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al intentar eliminar: " + e.getMessage());
        }
        return "redirect:/reservas/admin/listar";
    }
}