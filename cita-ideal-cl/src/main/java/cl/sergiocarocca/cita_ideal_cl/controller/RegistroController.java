package cl.sergiocarocca.cita_ideal_cl.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.sergiocarocca.cita_ideal_cl.entity.Usuario;
import cl.sergiocarocca.cita_ideal_cl.service.UsuarioService;


@Controller
public class RegistroController {

   
    private final UsuarioService usuarioService;
    
    

    public RegistroController(UsuarioService usuarioService) {
		super();
		this.usuarioService = usuarioService;
	}

	// Muestra el formulario de registro
    @GetMapping("/registro")
    public String mostrarFormulario(Model model) {
        // Pasamos un objeto Usuario vacío para que Thymeleaf lo llene
        model.addAttribute("usuario", new Usuario());
        return "/registro";
    }

    // Procesa los datos enviados desde el HTML
    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute("usuario") Usuario usuario,
    		Model model,
    		RedirectAttributes redirect) {
    	try {
    		 
            usuarioService.registrarUsuarioPublico(usuario);
            
            redirect.addFlashAttribute("mensajeExito", "¡Registro exitoso!Bienvenido a Tu Cita Ideal");
           
          
            return "redirect:/login";
            
        } catch (IllegalArgumentException e) {
            // Capturamos el mensaje "Las contraseñas no coinciden"
            model.addAttribute("error", e.getMessage());
            // Devolvemos al formulario sin perder los datos ya escritos
            return "registro"; 
        } catch (Exception e) {
            model.addAttribute("error", "Usuario registrado");
            return "registro";
        }
    }
}