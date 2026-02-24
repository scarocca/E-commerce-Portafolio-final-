package cl.sergiocarocca.cita_ideal_cl.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import cl.sergiocarocca.cita_ideal_cl.entity.Usuario;
import cl.sergiocarocca.cita_ideal_cl.repository.UsuarioRepository;




@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Buscamos el usuario en la BD por su nombre de usuario
        Usuario usuario = usuarioRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // 2. Convertimos tus Roles (Entidades) en GrantedAuthority (lo que entiende Spring)
        // Usamos streams para mapear cada rol a una autoridad de Spring Security
        List<GrantedAuthority> autoridades = usuario.getRoles().stream()
                .map(rol -> new SimpleGrantedAuthority(rol.getNombre()))
                .collect(Collectors.toList());

        // 3. Retornamos un objeto User de Spring Security (clase propia del framework)
        // Este objeto contiene: username, password (cifrado) y la lista de roles
        return new org.springframework.security.core.userdetails.User(
                usuario.getUsername(),
                usuario.getPassword(),
                autoridades
        );
    }
}
