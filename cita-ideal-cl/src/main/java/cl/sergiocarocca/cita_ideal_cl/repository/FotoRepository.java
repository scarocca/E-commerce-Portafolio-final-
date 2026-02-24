package cl.sergiocarocca.cita_ideal_cl.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import cl.sergiocarocca.cita_ideal_cl.entity.Foto;

import java.util.List;

public interface FotoRepository extends JpaRepository<Foto, Long> {
    // Ordenar para que las últimas fotos subidas aparezcan primero
    List<Foto> findAllByOrderByFechaCargaDesc();
}
