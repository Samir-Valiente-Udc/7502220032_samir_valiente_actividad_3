package com.ejercicio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "contratos")
@Data
@NoArgsConstructor
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La fecha de firma es obligatoria.")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_firma", nullable = false)
    private Date fechaFirma;

    @NotNull(message = "La fecha de inicio es obligatoria.")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_inicio", nullable = false)
    private Date fechaInicio;

    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_fin")
    private Date fechaFin;

    @NotNull(message = "El nombre de la empresa es obligatorio.")
    @Column(nullable = false, length = 150)
    private String empresa;

    
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "empleado_id") 
    private Usuario empleado;

    @Lob 
    @Column(columnDefinition = "TEXT")
    private String funciones;

    @PositiveOrZero(message = "El monto debe ser un valor positivo o cero.")
    @Column(precision = 10, scale = 2) 
    private BigDecimal monto;

    @Column(name = "frecuencia_de_pago", length = 50)
    private String frecuenciaDePago;

}