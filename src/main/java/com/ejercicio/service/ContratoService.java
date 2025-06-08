package com.ejercicio.service;

import com.ejercicio.model.Contrato;
import com.ejercicio.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface ContratoService {
    Contrato guardarContrato(Contrato contrato);
    Optional<Contrato> obtenerContratoPorId(Long id);
    List<Contrato> listarTodosLosContratos();
    void eliminarContrato(Long id);
    List<Contrato> listarContratosPorEmpleado(Usuario empleado);
}