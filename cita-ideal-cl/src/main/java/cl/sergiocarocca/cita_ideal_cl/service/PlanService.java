package cl.sergiocarocca.cita_ideal_cl.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.sergiocarocca.cita_ideal_cl.entity.Plan;
import cl.sergiocarocca.cita_ideal_cl.repository.PlanRepository;

@Service
public class PlanService {
    @Autowired
    private PlanRepository planRepository;

    // Definimos la ruta de los planes
    private final String carpetaPlanes = "src/main/resources/static/assets/img/planes/";

    

    public Plan buscarPorId(Long id) {
        return planRepository.findById(id).orElse(null);
    }
	
    public void guardar(Plan plan) {
        planRepository.save(plan);
    }

    public void eliminar(Long id) {
        Plan plan = buscarPorId(id);
        
        if (plan != null) {
            // Solo intentamos borrar el archivo si imagenUrl no es nulo ni está vacío
            if (plan.getImagenUrl() != null && !plan.getImagenUrl().isEmpty()) {
                try {
                    Path ruta = Paths.get(carpetaPlanes)
                            .toAbsolutePath()
                            .resolve(plan.getImagenUrl());
                    
                    // deleteIfExists es genial porque no lanza error si el archivo no está
                    Files.deleteIfExists(ruta);
                } catch (IOException e) {
                    // Solo logeamos el error, pero dejamos que la ejecución siga
                    System.err.println("No se pudo eliminar el archivo, pero seguiremos con la DB: " + e.getMessage());
                }
            }
            
            // Finalmente borramos de la base de datos
            planRepository.deleteById(id);
        }
    }

    public List<Plan> listarTodos() {
        return planRepository.findAll();
    }
    public void ocultarPlan(Long id) {
        Plan plan = buscarPorId(id);
        if (plan != null) {
            plan.setActivo(false); // Lo "apagamos"
            planRepository.save(plan);
        }
    }
    public void activarPlan(Long id) {
        Plan plan = buscarPorId(id);
        if (plan != null) {
            plan.setActivo(true); // Cambiamos a true
            planRepository.save(plan);
        }
    }
    public List<Plan> listarPlanesActivos() {
        return planRepository.findAll().stream()
                             .filter(p -> p.isActivo())
                             .toList();
    }
}
