package cl.sergiocarocca.cita_ideal_cl.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import cl.sergiocarocca.cita_ideal_cl.entity.Consulta;
import cl.sergiocarocca.cita_ideal_cl.repository.ConsultaRepository;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;

    
    public ConsultaService(ConsultaRepository consultaRepository) {
        this.consultaRepository = consultaRepository;
    }

    public List<Consulta> listarTodas() {
        return consultaRepository.findAll();
    }

    public void guardar(Consulta consulta) {
    	consulta.setFechaEnvio(LocalDateTime.now());
        consultaRepository.save(consulta);
    }

    public void eliminar(Long id) {
        consultaRepository.deleteById(id);
    }
    
    public long contarTodas() {
        return consultaRepository.count();
    }
}
