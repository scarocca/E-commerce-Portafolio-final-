package cl.sergiocarocca.cita_ideal_cl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cl.sergiocarocca.cita_ideal_cl.entity.Foto;
import cl.sergiocarocca.cita_ideal_cl.service.FotoService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/admin/galeria")
public class AdminGaleriaController {

    @Autowired
    private FotoService fotoService;

    // Método para mostrar el formulario
    @GetMapping("/nuevo")
    public String formularioSubida() {
        return "admin/galeria-subir";
    }

    @PostMapping("/guardar")
    public String guardarFoto(@RequestParam("titulo") String titulo,
                             @RequestParam("archivoImagen") MultipartFile archivo,
                             RedirectAttributes flash) {
        
        if (archivo.isEmpty()) {
            flash.addFlashAttribute("error", "Por favor, selecciona un archivo.");
            return "redirect:/admin/galeria/nuevo";
        }

        try {
            // 1. Definir la ruta relativa al proyecto
            String carpetaRelativa = "src/main/resources/static/assets/img/galeria/";
            String nombreUnico = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename();
            
            // 2. Obtener la ruta absoluta completa para que Java no se pierda
            Path rutaAbsoluta = Paths.get(carpetaRelativa).toAbsolutePath();
            
            // Crear la carpeta si no existe (por seguridad)
            if (!Files.exists(rutaAbsoluta)) {
                Files.createDirectories(rutaAbsoluta);
            }

            // 3. Escribir el archivo físicamente
            byte[] bytes = archivo.getBytes();
            Path rutaArchivoFinal = rutaAbsoluta.resolve(nombreUnico);
            Files.write(rutaArchivoFinal, bytes);

            // 4. Guardar en Base de Datos (solo el nombre del archivo)
            Foto nuevaFoto = new Foto();
            nuevaFoto.setTitulo(titulo);
            nuevaFoto.setArchivo(nombreUnico);
            fotoService.guardar(nuevaFoto);

            flash.addFlashAttribute("success", "¡Foto subida con éxito!");

        } catch (IOException e) {
            e.printStackTrace();
            flash.addFlashAttribute("error", "Error crítico al guardar: " + e.getMessage());
        }

        return "admin/galeria-gestion";
    }
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        fotoService.eliminarFotoCompleta(id);
        flash.addFlashAttribute("success", "La foto y su archivo han sido eliminados.");
        return "redirect:/admin/galeria/gestion"; // O a donde tengas tu lista de fotos
    }
    @GetMapping("/gestion")
    public String gestionarGaleria(Model model) {
        // Reutilizamos el servicio para traer todas las fotos
        model.addAttribute("fotos", fotoService.listarTodas());
        // También pasamos el total para el contador del Sidebar si lo usas
        model.addAttribute("totalFotos", fotoService.listarTodas().size());
        return "admin/galeria-gestion";
    }
}