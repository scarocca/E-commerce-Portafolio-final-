package cl.sergiocarocca.cita_ideal_cl.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.sergiocarocca.cita_ideal_cl.entity.Plan;
import cl.sergiocarocca.cita_ideal_cl.service.ConsultaService;
import cl.sergiocarocca.cita_ideal_cl.service.PlanService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/planes")
public class AdminPlanesController {

    @Autowired
    private PlanService planService;
    @Autowired
    private ConsultaService consultaService;
    
 // Ruta donde se guardarán las imágenes de los planes
    private final String carpetaPlanes = "src/main/resources/static/assets/img/planes/";

    // 1. Listar todos los planes para el admin
    @GetMapping
    public String listarPlanes(Model model) {
    	model.addAttribute("planes", planService.listarTodos());
        //model.addAttribute("planes", planService.obtenerTodosLosPlanes());
    	long totalConsultas = consultaService.listarTodas().size(); 
        model.addAttribute("totalConsultas", totalConsultas);
        return "admin/planes-lista";
    }

    // 2. Mostrar formulario de nuevo plan
    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("plan", new Plan());
        return "admin/plan-form";
    }

   
 // 4. Mostrar formulario para editar un plan existente
    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable Long id, Model model) {
        Plan plan = planService.buscarPorId(id);
        if (plan != null) {
            model.addAttribute("plan", plan);
            return "admin/plan-form"; // Usamos el mismo formulario que para el "nuevo"
        }
        return "redirect:/admin/planes";
    }

    // 5. Eliminar un plan
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        planService.eliminar(id); 
        return "redirect:/admin/planes";
    }
    @PostMapping("/guardar")
    public String guardarPlan(@Valid @ModelAttribute("plan") Plan plan,
    		                  BindingResult result,	
                             @RequestParam("archivoImagen") MultipartFile archivo,
                             RedirectAttributes flash,Model model) {
    	
    	if (result.hasErrors()) {
            return "admin/plan-form"; 
        }
    	
    	if (plan.getId() == null && archivo.isEmpty()) {
            model.addAttribute("errorImagen", "La imagen es obligatoria para nuevos planes");
            return "admin/plan-form";
        }
    	
        
        if (!archivo.isEmpty()) {
            try {
                // 1. Asegurar que la carpeta exista
                Path rutaAbsoluta = Paths.get(carpetaPlanes).toAbsolutePath();
                if (!Files.exists(rutaAbsoluta)) {
                    Files.createDirectories(rutaAbsoluta);
                }

                // 2. Crear nombre único con UUID para evitar duplicados
                String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
                
                // 3. Guardar el archivo físicamente en el servidor
                byte[] bytes = archivo.getBytes();
                Path rutaArchivoFinal = rutaAbsoluta.resolve(nombreArchivo);
                Files.write(rutaArchivoFinal, bytes);

                // 4. Guardar el nombre del archivo en el objeto Plan (campo imagenUrl)
                plan.setImagenUrl(nombreArchivo);

            } catch (IOException e) {
                e.printStackTrace();
                flash.addFlashAttribute("error", "Error crítico al subir la imagen: " + e.getMessage());
                return "redirect:/admin/planes/nuevo";
            }
        }

        // 5. Guardar el registro en la base de datos
        planService.guardar(plan);
        flash.addFlashAttribute("success", "¡El plan ha sido guardado exitosamente!");
        
        return "redirect:/admin/planes";
    }

    @PostMapping("/editar/{id}")
    public String actualizarPlan(@PathVariable Long id, 
                                @ModelAttribute("plan") Plan planModificado,
                                @RequestParam("archivoImagen") MultipartFile archivo,
                                RedirectAttributes flash) {
        try {
            // 1. Buscamos el plan original en la DB para no perder la imagen si no se sube una nueva
            Plan planExistente = planService.buscarPorId(id);

            if (!archivo.isEmpty()) {
                // --- CASO A: EL USUARIO SUBIÓ UNA FOTO NUEVA ---
                String carpetaPlanes = "src/main/resources/static/assets/img/planes/";
                
                // A. Borrar la foto vieja físicamente (si existe)
                if (planExistente.getImagenUrl() != null) {
                    Path rutaFotoVieja = Paths.get(carpetaPlanes).toAbsolutePath().resolve(planExistente.getImagenUrl());
                    Files.deleteIfExists(rutaFotoVieja);
                }

                // B. Guardar la foto nueva
                String nombreUnico = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
                Path rutaNueva = Paths.get(carpetaPlanes).toAbsolutePath().resolve(nombreUnico);
                Files.write(rutaNueva, archivo.getBytes());

                // C. Actualizar el nombre en el objeto
                planModificado.setImagenUrl(nombreUnico);
            } else {
                // --- CASO B: EL USUARIO NO SUBIÓ FOTO NUEVA ---
                // Mantenemos la que ya tenía para que no quede vacía
                planModificado.setImagenUrl(planExistente.getImagenUrl());
            }

            planService.guardar(planModificado);
            flash.addFlashAttribute("success", "¡Plan actualizado con éxito!");

        } catch (IOException e) {
            flash.addFlashAttribute("error", "Error al procesar la imagen");
        }

        return "redirect:/admin/planes";
    }
    @GetMapping("/ocultar/{id}")
    public String ocultarPlan(@PathVariable Long id, RedirectAttributes flash) {
        planService.ocultarPlan(id);
        flash.addFlashAttribute("success", "El plan ha sido ocultado de la vista pública.");
        return "redirect:/admin/planes";
    }
    @GetMapping("/activar/{id}")
    public String activarPlan(@PathVariable Long id, RedirectAttributes flash) {
        planService.activarPlan(id);
        flash.addFlashAttribute("mensajeExito", "El plan ahora es visible nuevamente en el catálogo.");
        return "redirect:/admin/planes";
    }
}
