package cl.sergiocarocca.cita_ideal_cl.service;


import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import cl.sergiocarocca.cita_ideal_cl.entity.Role;
import cl.sergiocarocca.cita_ideal_cl.entity.Usuario;
import cl.sergiocarocca.cita_ideal_cl.repository.RoleRepository;
import cl.sergiocarocca.cita_ideal_cl.repository.UsuarioRepository;



@Service
public class UsuarioService {

    
    private final UsuarioRepository usuarioRepo;
    private final RoleRepository roleRepo;
    private final BCryptPasswordEncoder passwordEncoder;
    

    public UsuarioService(UsuarioRepository usuarioRepo, RoleRepository roleRepo,
			BCryptPasswordEncoder passwordEncoder) {
		super();
		this.usuarioRepo = usuarioRepo;
		this.roleRepo = roleRepo;
		this.passwordEncoder = passwordEncoder;
	}


	public void registrarUsuarioPublico(Usuario usuario) {
		
		if (usuario.getConfirmacionPassword() == null || 
		        !usuario.getPassword().equals(usuario.getConfirmacionPassword())) {
		        throw new IllegalArgumentException("Las contraseñas no coinciden");
		    }
        // 1. Cifrar password
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // 2. Buscar el rol "ROLE_USER" en la BD
        Role userRole = roleRepo.findByNombre("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("El rol ROLE_USER no existe en la BD"));

        // 3. Asignar el rol y guardar
        usuario.añadirRol(userRole);
        usuarioRepo.save(usuario);
    }
    
    public List<Usuario> listartodo(){
    return usuarioRepo.findAll();
    	
    }
    public void eliminar(Long id) {
        // Verificamos si existe antes de intentar borrar
        if (usuarioRepo.existsById(id)) {
            usuarioRepo.deleteById(id);
        } else {
            throw new RuntimeException("Usuario no encontrado con el ID: " + id);
        }
    }
}
