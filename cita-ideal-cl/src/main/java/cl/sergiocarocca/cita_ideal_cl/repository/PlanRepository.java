package cl.sergiocarocca.cita_ideal_cl.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.sergiocarocca.cita_ideal_cl.entity.Plan;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    // Aquí podrías crear métodos personalizados, por ejemplo:
    // List<Plan> findByNombreContaining(String nombre);
}
