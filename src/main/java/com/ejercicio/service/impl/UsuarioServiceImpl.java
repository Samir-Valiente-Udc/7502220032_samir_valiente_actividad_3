package com.ejercicio.service.impl;

import com.ejercicio.model.Usuario;
import com.ejercicio.repository.UsuarioRepository;
import com.ejercicio.service.UsuarioService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService, UserDetailsService { 

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario guardarUsuario(Usuario usuario) {
        
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    @Override
    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public List<Usuario> listarTodosLosUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public boolean verificarPassword(Usuario usuario, String password) {
        return passwordEncoder.matches(password, usuario.getPassword());
    }

    @Override
    public Optional<Usuario> autenticarUsuario(String username, String password) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(username);
        System.out.println("Datos de " + usuarioOptional);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            if (passwordEncoder.matches(password, usuario.getPassword())) {
                return Optional.of(usuario);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<String> obtenerPasswordPorUsernameYRespuesta(String username, String respuesta) {
        return usuarioRepository.findByUsernameAndRespuestaSeguridadIgnoreCase(username, respuesta)
                .map(Usuario::getPassword);
    }

    @Override
    public boolean cambiarPassword(String username, String nuevaPassword) {
        return usuarioRepository.findByUsername(username)
                .map(usuario -> {
                    usuario.setPassword(passwordEncoder.encode(nuevaPassword));
                    usuarioRepository.save(usuario);
                    return true;
                })
                .orElse(false);
    }

    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        
        return new User(usuario.getUsername(), usuario.getPassword(), new ArrayList<>()); 
    }

    @Override
    
    public Usuario findByUsername(String username) {
        return usuarioRepository.findByUsername(username).orElse(null);
    }

}