package cl.sergiocarocca.cita_ideal_cl.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import cl.sergiocarocca.cita_ideal_cl.entity.ItemCarrito;
import cl.sergiocarocca.cita_ideal_cl.entity.Plan;
import cl.sergiocarocca.cita_ideal_cl.entity.Reserva;
import cl.sergiocarocca.cita_ideal_cl.service.PlanService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private PlanService planService; // Tu servicio para buscar planes en la BD

    @GetMapping("/agregar/{id}")
    public String agregarAlCarrito(@PathVariable Long id, HttpSession session) {
        // 1. Obtener el carrito de la sesión. Si no existe, creamos una lista nueva.
    	@SuppressWarnings("unchecked")
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        
        

        // 2. Buscar el plan en la base de datos
        Plan planDeseado = planService.buscarPorId(id);

        if (planDeseado != null) {
            // 3. Verificar si el plan ya está en el carrito para solo aumentar la cantidad
            boolean existe = false;
            for (ItemCarrito item : carrito) {
                if (item.getPlan().getId().equals(id)) {
                    item.setCantidad(item.getCantidad() + 1);
                    existe = true;
                    break;
                }
            }

            // 4. Si no existe, lo agregamos como nuevo item
            if (!existe) {
                carrito.add(new ItemCarrito(planDeseado, 1));
            }
        }

        // 5. Guardar la lista actualizada en la sesión
        session.setAttribute("carrito", carrito);

        // 6. Redirigir a la vista del carrito
        return "redirect:/carrito/ver";
    }

    @GetMapping("/ver")
    public String verCarrito(Model model, HttpSession session) {
    	@SuppressWarnings("unchecked")
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
        }
        
        int itemsCount = (carrito != null) ? carrito.size() : 0;
        model.addAttribute("itemsCount", itemsCount);

        // Calcular el Gran Total sumando los BigDecimal
        BigDecimal total = carrito.stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("carrito", carrito);
        model.addAttribute("totalCarrito", total);

        return "carrito-vista"; // nombre de tu archivo HTML
    }
    
    @GetMapping("/eliminar/{id}")
    public String eliminarDelCarrito(@PathVariable Long id, HttpSession session) {
    	@SuppressWarnings("unchecked")
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        
        if (carrito != null) {
            // Usamos removeIf para eliminar el item cuyo plan tenga el ID recibido
            carrito.removeIf(item -> item.getPlan().getId().equals(id));
            
            // Actualizamos la sesión con la lista modificada
            session.setAttribute("carrito", carrito);
        }
        
        return "redirect:/carrito/ver";
    }
    
    @GetMapping("/vaciar")
    public String vaciarCarrito(HttpSession session) {
        // Podemos simplemente remover el atributo de la sesión
        session.removeAttribute("carrito");
        
        // Opcional: También podrías hacer carrito.clear() si prefieres mantener la lista vacía
        
        return "redirect:/carrito/ver";
    }
    @GetMapping("/checkout")
    public String irACheckout(HttpSession session, Model model) {
    	@SuppressWarnings("unchecked")
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");

        if (carrito == null || carrito.isEmpty()) {
            return "redirect:/productos";
        }

        BigDecimal total = carrito.stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("reserva", new Reserva()); 
        model.addAttribute("carrito", carrito); 
        model.addAttribute("totalCarrito", total);
        
        // AQUÍ: Apuntamos al nuevo archivo que creamos
        return "public/reserva-confirmacion-carrito"; 
    }
}
