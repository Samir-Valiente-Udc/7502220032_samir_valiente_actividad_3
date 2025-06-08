package com.ejercicio.service.impl;

import com.ejercicio.model.Contrato;
import com.ejercicio.model.Usuario;
import com.ejercicio.repository.ContratoRepository;
import com.ejercicio.service.ContratoService;
import org.hibernate.Hibernate; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; 

import java.util.List;
import java.util.Optional;

@Service
public class ContratoServiceImpl implements ContratoService {

    private final ContratoRepository contratoRepository;

    @Autowired
    public ContratoServiceImpl(ContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
    }

    @Override
    @Transactional
    public Contrato guardarContrato(Contrato contrato) {
        
        
        return contratoRepository.save(contrato);
    }

    @Override
    @Transactional(readOnly = true) 
    public Optional<Contrato> obtenerContratoPorId(Long id) {
        Optional<Contrato> contratoOptional = contratoRepository.findById(id);
        contratoOptional.ifPresent(contrato -> {
            
            
            
            if (contrato.getEmpleado() != null) {
                Hibernate.initialize(contrato.getEmpleado());
            }
        });
        return contratoOptional;
    }

    @Override
    @Transactional(readOnly = true) 
    public List<Contrato> listarTodosLosContratos() {
        List<Contrato> contratos = contratoRepository.findAll();
        
        
        contratos.forEach(contrato -> {
            if (contrato.getEmpleado() != null) {
                Hibernate.initialize(contrato.getEmpleado());
            }
        });
        return contratos;
    }

    @Override
    @Transactional
    public void eliminarContrato(Long id) {
        contratoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true) 
    public List<Contrato> listarContratosPorEmpleado(Usuario empleado) {
        List<Contrato> contratos = contratoRepository.findByEmpleado(empleado);
        
        
        contratos.forEach(contrato -> {
            if (contrato.getEmpleado() != null) {
                Hibernate.initialize(contrato.getEmpleado());
            }
        });
        return contratos;
    }
}