package com.ejercicio.controller;

import com.ejercicio.model.Contrato;
import com.ejercicio.model.Usuario;
import com.ejercicio.service.ContratoService;
import com.ejercicio.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/contratos")
public class ContratoController {

    private final ContratoService contratoService;
    private final UsuarioService usuarioService; 

    @ModelAttribute("usuarioSesion")
    public Usuario addUsuarioToModel(HttpSession session) {
        return (Usuario) session.getAttribute("usuarioGlobal");
    }

    @Autowired
    public ContratoController(ContratoService contratoService, UsuarioService usuarioService) {
        this.contratoService = contratoService;
        this.usuarioService = usuarioService;
    }

    private boolean noHaySesion(HttpSession session) {
        return session.getAttribute("usuarioGlobal") == null;
    }

    
    @GetMapping("/")
    public String menuContratos(HttpSession session) {
        if (noHaySesion(session)) return "redirect:/login";
        return "contratos/index"; 
    }

    
    @GetMapping("/agregar")
    public String mostrarFormularioAgregarContrato(Model model, HttpSession session) {
        if (noHaySesion(session)) return "redirect:/login";
        model.addAttribute("contrato", new Contrato());
        model.addAttribute("usuarios", usuarioService.listarTodosLosUsuarios()); 
        return "contratos/agregar";
    }

    
    @PostMapping("/agregar")
    public String agregarContrato(@Valid @ModelAttribute("contrato") Contrato contrato,
                                  BindingResult result,
                                  @RequestParam("empleadoId") Long empleadoId,
                                  RedirectAttributes redirectAttributes, Model model, HttpSession session) {
        if (noHaySesion(session)) return "redirect:/login";

        Optional<Usuario> empleadoOpt = usuarioService.obtenerUsuarioPorId(empleadoId);
        if (empleadoOpt.isEmpty()) {
            result.rejectValue("empleado", "error.contrato", "Empleado seleccionado no es válido.");
        } else {
            contrato.setEmpleado(empleadoOpt.get());
        }

        if (result.hasErrors()) {
            model.addAttribute("usuarios", usuarioService.listarTodosLosUsuarios());
            model.addAttribute("error", "Por favor corrige los errores del formulario.");
            return "contratos/agregar";
        }
        try {
            contratoService.guardarContrato(contrato);
            redirectAttributes.addFlashAttribute("mensaje", "Contrato agregado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agregar contrato: " + e.getMessage());
        }
        
        return "redirect:/contratos/agregar";
    }

    
    @GetMapping("/listar")
    public String listarContratos(Model model, HttpSession session) {
        if (noHaySesion(session)) return "redirect:/login";
        List<Contrato> contratos = contratoService.listarTodosLosContratos();
        model.addAttribute("contratos", contratos);
        
        return "contratos/listar";
    }

    
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditarContrato(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (noHaySesion(session)) return "redirect:/login";
        Optional<Contrato> contratoOpt = contratoService.obtenerContratoPorId(id);
        if (contratoOpt.isPresent()) {
            model.addAttribute("contrato", contratoOpt.get());
            model.addAttribute("usuarios", usuarioService.listarTodosLosUsuarios());
            session.setAttribute("contratoParaEditar", contratoOpt.get()); 
            return "contratos/editar";
        } else {
            redirectAttributes.addFlashAttribute("error", "Contrato no encontrado.");
            return "redirect:/contratos/listar";
        }
    }

    
    @PostMapping("/editar")
    public String editarContrato(@Valid @ModelAttribute("contrato") Contrato contratoForm,
                                 BindingResult result,
                                 @RequestParam("empleadoId") Long empleadoId,
                                 HttpSession session,
                                 RedirectAttributes redirectAttributes, Model model) {
        if (noHaySesion(session)) return "redirect:/login";

        Contrato contratoEnSesion = (Contrato) session.getAttribute("contratoParaEditar");
        if (contratoEnSesion == null || !contratoEnSesion.getId().equals(contratoForm.getId())) {
            redirectAttributes.addFlashAttribute("error", "Error de sesión o ID no coincide.");
            return "redirect:/contratos/listar";
        }

        Optional<Usuario> empleadoOpt = usuarioService.obtenerUsuarioPorId(empleadoId);
        if (empleadoOpt.isEmpty()) {
            result.rejectValue("empleado", "error.contrato", "Empleado seleccionado no es válido.");
        } else {
            contratoForm.setEmpleado(empleadoOpt.get());
        }

        if (result.hasErrors()) {
            model.addAttribute("usuarios", usuarioService.listarTodosLosUsuarios());
            model.addAttribute("contrato", contratoForm); 
            return "contratos/editar";
        }

        try {
            Contrato contratoActualizado = contratoService.guardarContrato(contratoForm);
            session.setAttribute("contratoParaEditar", contratoActualizado); 
            redirectAttributes.addFlashAttribute("mensaje", "Contrato actualizado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar contrato: " + e.getMessage());
        }
        return "redirect:/contratos/editar/" + contratoForm.getId(); 
    }

    
    @GetMapping("/eliminar_confirmar/{id}")
    public String confirmarEliminarContrato(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (noHaySesion(session)) return "redirect:/login";
        Optional<Contrato> contratoOpt = contratoService.obtenerContratoPorId(id);
        if (contratoOpt.isPresent()) {
            session.setAttribute("contratoAEliminar", contratoOpt.get()); 
            model.addAttribute("contratoAEliminar", contratoOpt.get());
            return "contratos/eliminar"; 
        } else {
            redirectAttributes.addFlashAttribute("error", "Contrato no encontrado.");
            return "redirect:/contratos/listar";
        }
    }

    
    @GetMapping("/eliminar/{id}")
    public String eliminarContrato(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (noHaySesion(session)) return "redirect:/login";
        
        
        
        
        
        
        
        
        
        
        try {
            contratoService.eliminarContrato(id);
            redirectAttributes.addFlashAttribute("mensaje", "Contrato eliminado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el contrato.");
        }
        return "redirect:/contratos/listar"; 
    }

    
    @GetMapping("/buscar")
    public String mostrarFormularioBuscarContrato(Model model, HttpSession session){
        if (noHaySesion(session)) return "redirect:/login";
        session.removeAttribute("contratoEncontrado");
        model.addAttribute("contratoBusqueda", new Contrato());
        return "contratos/buscar";
    }

    
    @PostMapping("/buscar") 
    public String buscarContrato(@RequestParam(required = false) Long id,
                                 @RequestParam(required = false) String empresa,
                                 HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        if (noHaySesion(session)) return "redirect:/login";
        Optional<Contrato> contratoOpt = Optional.empty();
        List<Contrato> contratosEncontrados = null;

        if (id != null) {
            contratoOpt = contratoService.obtenerContratoPorId(id);
            if(contratoOpt.isPresent()){
                session.setAttribute("contratoEncontrado", contratoOpt.get()); 
                redirectAttributes.addFlashAttribute("mensaje", "Contrato encontrado por ID.");
            }
        } else {
            redirectAttributes.addFlashAttribute("error", "Debe proporcionar un ID o nombre de Empresa para buscar.");
            return "redirect:/contratos/buscar";
        }

        if (contratoOpt.isPresent() || (contratosEncontrados != null && !contratosEncontrados.isEmpty())) {
            return "redirect:/contratos/buscar_resultado";
        } else {
            session.removeAttribute("contratoEncontrado");
            session.removeAttribute("contratosEncontradosLista");
            redirectAttributes.addFlashAttribute("error", "Contrato(s) no encontrado(s).");
            return "redirect:/contratos/buscar";
        }
    }

    @GetMapping("/buscar_resultado")
    public String mostrarResultadoBusquedaContrato(Model model, HttpSession session){
        if (noHaySesion(session)) return "redirect:/login";
        Contrato contratoEncontrado = (Contrato) session.getAttribute("contratoEncontrado");
        List<Contrato> contratosEncontradosLista = (List<Contrato>) session.getAttribute("contratosEncontradosLista");

        if (contratoEncontrado != null) {
            model.addAttribute("contratoEncontrado", contratoEncontrado);
        }
        if (contratosEncontradosLista != null) {
            model.addAttribute("contratosEncontradosLista", contratosEncontradosLista);
        }
        model.addAttribute("contratoBusqueda", new Contrato()); 
        return "contratos/buscar"; 
    }
}