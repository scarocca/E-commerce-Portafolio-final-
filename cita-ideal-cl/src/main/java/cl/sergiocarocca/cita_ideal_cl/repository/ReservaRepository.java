package cl.sergiocarocca.cita_ideal_cl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.sergiocarocca.cita_ideal_cl.entity.Reserva;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {

    // Método clave: Verifica si hay choques de horario
    // Retorna true si ya existe una reserva CONFIRMADA para ese plan en esa hora
    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.plan.id = :planId " +
           "AND r.fechaCita = :fecha " +
           "AND r.estado = 'CONFIRMADA'")
    boolean existeReservaEnEsaFecha(@Param("planId") Long planId, 
                                    @Param("fecha") LocalDateTime fecha);

    // Para el panel administrativo: Buscar reservas por email o código
    List<Reserva> findByEmailCliente(String email);
    
    Reserva findByCodigoSeguimiento(String codigo);
    
 // En ReservaRepository.java
    List<Reserva> findAllByOrderByFechaCitaDesc();
}