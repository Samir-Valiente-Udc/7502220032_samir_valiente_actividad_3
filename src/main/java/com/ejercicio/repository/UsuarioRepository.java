package com.ejercicio.repository;

import com.ejercicio.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    Optional<Usuario> findByUsername(String username);
    
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByUsernameAndRespuestaSeguridadIgnoreCase(String username, String respuestaSeguridad);
}