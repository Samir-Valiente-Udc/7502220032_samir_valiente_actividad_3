package com.ejercicio.repository;

import com.ejercicio.model.Contrato;
import com.ejercicio.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    List<Contrato> findByEmpleado(Usuario empleado);
    
    List<Contrato> findByEmpresaContainingIgnoreCase(String empresa);
}