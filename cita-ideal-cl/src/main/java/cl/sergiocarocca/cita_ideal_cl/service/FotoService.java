package cl.sergiocarocca.cita_ideal_cl.service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.nio.file.Files;

import org.springframework.stereotype.Service;

import cl.sergiocarocca.cita_ideal_cl.entity.Foto;
import cl.sergiocarocca.cita_ideal_cl.repository.FotoRepository;

@Service
public class FotoService {

    private final FotoRepository fotoRepository;
    
    private final String carpetaRelativa = "src/main/resources/static/assets/img/galeria/";

    public FotoService(FotoRepository fotoRepository) {
        this.fotoRepository = fotoRepository;
    }

    public List<Foto> listarTodas() {
        return fotoRepository.findAllByOrderByFechaCargaDesc();
    }

    public void guardar(Foto foto) {
        fotoRepository.save(foto);
    }

    public void eliminarFotoCompleta(Long id) {
        // 1. Buscamos la foto en la DB para saber cómo se llama el archivo
        Optional<Foto> fotoOpt = fotoRepository.findById(id);
        
        if (fotoOpt.isPresent()) {
            Foto foto = fotoOpt.get();
            String nombreArchivo = foto.getArchivo();

            try {
                // 2. Intentar borrar el archivo físico
                Path ruta = Paths.get(carpetaRelativa).toAbsolutePath().resolve(nombreArchivo);
                Files.deleteIfExists(ruta);
                
                // 3. Si el archivo se borró (o no existía), borramos el registro de la DB
                fotoRepository.deleteById(id);
                
            } catch (IOException e) {
                e.printStackTrace();
                // Aquí podrías decidir si borrar de la DB aunque falle el archivo
            }
        }
    }
}
