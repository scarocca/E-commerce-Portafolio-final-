package cl.sergiocarocca.cita_ideal_cl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.sergiocarocca.cita_ideal_cl.entity.Consulta;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
	List<Consulta> findAllByOrderByFechaEnvioDesc();
}
