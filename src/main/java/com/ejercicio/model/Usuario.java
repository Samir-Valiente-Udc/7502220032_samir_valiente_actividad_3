package com.ejercicio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Entity
@Table(name = "usuarios")
@Data 
@NoArgsConstructor 
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre de usuario no puede estar vacío.")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres.")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @NotEmpty(message = "La contraseña no puede estar vacía.")
    
    @Column(nullable = false)
    private String password;

    @NotEmpty(message = "El nombre completo no puede estar vacío.")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotEmpty(message = "El email no puede estar vacío.")
    @Email(message = "Debe ser una dirección de correo electrónico válida.")
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "pregunta_seguridad", length = 255)
    private String preguntaSeguridad;

    @Column(name = "respuesta_seguridad", length = 255)
    private String respuestaSeguridad;

    
    @OneToMany(mappedBy = "empleado", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Contrato> contratos;

    
    public Usuario(String username, String password, String nombre, String email) {
        this.username = username;
        this.password = password;
        this.nombre = nombre;
        this.email = email;
    }
}