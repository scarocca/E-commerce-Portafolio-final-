package cl.sergiocarocca.cita_ideal_cl.controller;

import java.util.Collections;
import java.util.List;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import cl.sergiocarocca.cita_ideal_cl.entity.Consulta;
import cl.sergiocarocca.cita_ideal_cl.entity.Reserva;
import cl.sergiocarocca.cita_ideal_cl.entity.Usuario;
import cl.sergiocarocca.cita_ideal_cl.repository.ConsultaRepository;
import cl.sergiocarocca.cita_ideal_cl.service.ReservaService;
import cl.sergiocarocca.cita_ideal_cl.service.UsuarioService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    
    private final ConsultaRepository consultaRepository;
    private final ReservaService reservaService;
   
    private final UsuarioService usuarioService;
    

    public AdminController(ConsultaRepository consultaRepository, ReservaService reservaService,
			UsuarioService usuarioService) {
		super();
		this.consultaRepository = consultaRepository;
		this.reservaService = reservaService;
		this.usuarioService = usuarioService;
	}
	@GetMapping("/consultas")
    public String listarConsultas(Model model) {
        // Obtenemos todas las consultas, idealmente las más recientes primero
        List<Consulta> lista = consultaRepository.findAll();
        // Invertimos la lista o podrías usar un Sort en el repository
        Collections.reverse(lista); 
        
        model.addAttribute("consultas", lista);
        return "admin/consultas-lista";
    }
    @GetMapping("/reservas")
    public String listarReservas(Model model) {
        List<Reserva> listaReservas = reservaService.listarTodas();
        model.addAttribute("reservas", listaReservas);
        model.addAttribute("seccion", "reservas"); // Útil para resaltar el menú
        return "admin/dashboard-reservas"; 
    }
   
    
    
    
    
    @GetMapping("/usuarios")
    public String listarUsuarios(Model model) {
        
        List<Usuario> lista = usuarioService.listartodo();
        
       
        
        model.addAttribute("usuarios", lista);
        return "admin/usuario-lista";
    }
    
    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable("id") Long id, RedirectAttributes flash) {
        try {
            usuarioService.eliminar(id);
            flash.addFlashAttribute("success", "Usuario eliminado con éxito.");
        } catch (Exception e) {
            flash.addFlashAttribute("error", "No se pudo eliminar el usuario.");
        }
        // Redirigimos a la lista de usuarios para ver los cambios
        return "redirect:/admin/usuarios";
    }
}