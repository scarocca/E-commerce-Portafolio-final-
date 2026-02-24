package cl.sergiocarocca.cita_ideal_cl.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import cl.sergiocarocca.cita_ideal_cl.entity.Usuario;





@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByUsername(String username);
	 
}
