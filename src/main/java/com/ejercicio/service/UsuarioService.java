package com.ejercicio.service;

import com.ejercicio.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    Usuario guardarUsuario(Usuario usuario);
    Optional<Usuario> obtenerUsuarioPorId(Long id);
    Optional<Usuario> obtenerUsuarioPorUsername(String username);
    Optional<Usuario> obtenerUsuarioPorEmail(String email);
    List<Usuario> listarTodosLosUsuarios();
    void eliminarUsuario(Long id);
    boolean verificarPassword(Usuario usuario, String passwordPlana);
    Optional<Usuario> autenticarUsuario(String username, String password);
    Optional<String> obtenerPasswordPorUsernameYRespuesta(String username, String respuestaSeguridad);
    boolean cambiarPassword(String username, String nuevaPassword);

    Usuario findByUsername(String username);
}