package com.ejercicio.controller;

import com.ejercicio.model.Usuario;
import com.ejercicio.service.UsuarioService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@SessionAttributes("usuarioSesion") 
public class UsuarioController {

    private final UsuarioService usuarioService;

    @ModelAttribute("usuarioSesion")
    public Usuario addUsuarioToModel(HttpSession session) {
        
        return (Usuario) session.getAttribute("usuarioGlobal");
    }

    @Autowired
    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    
    @ModelAttribute("usuarioSesion")
    public Usuario getUsuarioSesion() {
        return new Usuario(); 
    }


    
    @PostMapping("/login")
    public String iniciarSesion(@RequestParam String username,
                                @RequestParam String password,
                                HttpSession session,
                                RedirectAttributes redirectAttributes,
                                Model model) {
        Optional<Usuario> usuarioAutenticado = usuarioService.autenticarUsuario(username, password);
        System.out.println("Nombre " + username);
        System.out.println("password " + password);

        if (usuarioAutenticado.isPresent()) {
            session.setAttribute("usuarioGlobal", usuarioAutenticado.get()); 
            model.addAttribute("usuarioSesion", usuarioAutenticado.get()); 
            return "redirect:/"; 
        } else {
            redirectAttributes.addFlashAttribute("error", "Cédula o contraseña incorrecta.");
            return "redirect:/login"; 
        }
    }

    
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session, RedirectAttributes redirectAttributes) {
        session.removeAttribute("usuarioGlobal"); 
        session.invalidate(); 
        redirectAttributes.addFlashAttribute("mensaje", "Has cerrado sesión exitosamente.");
        return "redirect:/login"; 
    }

    
    @GetMapping("/recovery-password")
    public String mostrarFormularioRecoveryPass() {
        return "recoverypass";
    }

    
    @PostMapping("/recovery-password")
    public String recordarPass(@RequestParam String username,
                               @RequestParam String respuestaSeguridad, 
                               Model model,
                               RedirectAttributes redirectAttributes) {
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorUsername(username);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            
            if (usuario.getRespuestaSeguridad() != null && usuario.getRespuestaSeguridad().equalsIgnoreCase(respuestaSeguridad)) {
                
                
                String infoPassword = "Instrucciones para el reseteo (o la contraseña si así lo requiere el ejercicio) han sido enviadas a su correo.";
                
                
                redirectAttributes.addFlashAttribute("passwordRecuperada", infoPassword);
                redirectAttributes.addFlashAttribute("mensaje", "Se ha procesado tu solicitud. Revisa tu correo o sigue las instrucciones.");
                
                return "redirect:/recoverypass"; 
            } else {
                model.addAttribute("mensaje", "La respuesta de seguridad es incorrecta.");
                model.addAttribute("tipoMensaje", "error");
            }
        } else {
            model.addAttribute("mensaje", "Usuario (cédula) no encontrado.");
            model.addAttribute("tipoMensaje", "error");
        }
        return "recoverypass"; 
    }


    
    @GetMapping("/change-password")
    public String mostrarFormularioChangePass(Model model, HttpSession session) {
        Usuario usuarioEnSesion = (Usuario) session.getAttribute("usuarioGlobal");
        if (usuarioEnSesion == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuarioActual", usuarioEnSesion);
        return "changepass"; 
    }

    
    @PostMapping("/change-password")
    public String cambiarPass(@RequestParam String oldPassword,
                              @RequestParam String newPassword,
                              @RequestParam String confirmPassword,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        Usuario usuarioEnSesion = (Usuario) session.getAttribute("usuarioGlobal");
        if (usuarioEnSesion == null) {
            return "redirect:/login";
        }

        if (!usuarioService.verificarPassword(usuarioEnSesion, oldPassword)) {
            redirectAttributes.addFlashAttribute("error", "La contraseña actual es incorrecta.");
            return "redirect:/change-password";
        }
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "Las nuevas contraseñas no coinciden.");
            return "redirect:/change-password";
        }

        boolean cambiado = usuarioService.cambiarPassword(usuarioEnSesion.getUsername(), newPassword);
        if (cambiado) {
            
            Optional<Usuario> usuarioActualizado = usuarioService.obtenerUsuarioPorUsername(usuarioEnSesion.getUsername());
            usuarioActualizado.ifPresent(u -> session.setAttribute("usuarioGlobal", u));

            redirectAttributes.addFlashAttribute("mensaje", "Contraseña cambiada exitosamente.");
            
            
            return "redirect:/"; 
        } else {
            redirectAttributes.addFlashAttribute("error", "No se pudo cambiar la contraseña.");
            return "redirect:/change-password";
        }
    }


    
    
    @GetMapping("/usuarios/")
    public String menuUsuarios(HttpSession session) {
        if (session.getAttribute("usuarioGlobal") == null) return "redirect:/login";
        return "usuarios/index"; 
    }

    
    @GetMapping("/usuarios/agregar")
    public String mostrarFormularioAgregarUsuario(Model model, HttpSession session) {
        if (session.getAttribute("usuarioGlobal") == null) return "redirect:/login";
        model.addAttribute("usuario", new Usuario());
        return "usuarios/agregar";
    }

    
    @PostMapping("/usuarios/agregar")
    public String agregarUsuario(@Valid @ModelAttribute("usuario") Usuario usuario,
                                 BindingResult result,
                                 RedirectAttributes redirectAttributes, Model model, HttpSession session) {
        if (session.getAttribute("usuarioGlobal") == null) return "redirect:/login";
        if (result.hasErrors()) {
            model.addAttribute("error", "Por favor corrige los errores del formulario.");
            return "usuarios/agregar";
        }
        try {
            usuarioService.guardarUsuario(usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario agregado exitosamente.");
        } catch (Exception e) { 
            redirectAttributes.addFlashAttribute("error", "Error al agregar usuario: " + e.getMessage());
        }
        return "redirect:/usuarios/agregar"; 
    }

    
    @GetMapping("/usuarios/listar")
    public String listarUsuarios(Model model, HttpSession session) {
        if (session.getAttribute("usuarioGlobal") == null) return "redirect:/login";
        model.addAttribute("usuarios", usuarioService.listarTodosLosUsuarios());
        
        
        
        
        
        return "usuarios/listar";
    }

    
    @GetMapping("/usuarios/editar/{id}")
    public String mostrarFormularioEditarUsuario(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioGlobal") == null) return "redirect:/login";
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(id);
        if (usuarioOpt.isPresent()) {
            model.addAttribute("usuario", usuarioOpt.get());
            
            session.setAttribute("usuarioParaEditar", usuarioOpt.get());
            return "usuarios/editar";
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/usuarios/listar";
        }
    }

    @PostMapping("/usuarios/editar")
    public String editarUsuario(@Valid @ModelAttribute("usuario") Usuario usuarioForm,
                                @RequestParam(value = "newPassword", required = false) String newPassword,
                                BindingResult result,
                                HttpSession session,
                                RedirectAttributes redirectAttributes, Model model) {
        if (session.getAttribute("usuarioGlobal") == null) return "redirect:/login";

        Usuario usuarioEnSesionParaEditar = (Usuario) session.getAttribute("usuarioParaEditar");
        if (usuarioEnSesionParaEditar == null || !usuarioEnSesionParaEditar.getId().equals(usuarioForm.getId())) {
            redirectAttributes.addFlashAttribute("error", "Error de sesión o ID no coincide.");
            return "redirect:/usuarios/listar";
        }

        if (result.hasErrors()) {
            
            
            model.addAttribute("usuario", usuarioForm); 
            return "usuarios/editar";
        }

        
        if (newPassword == null || newPassword.trim().isEmpty()) {
            usuarioForm.setPassword(usuarioEnSesionParaEditar.getPassword()); 
        } else {
            
            
            usuarioForm.setPassword(newPassword); 
        }
        
        usuarioForm.setPreguntaSeguridad(usuarioForm.getPreguntaSeguridad());
        usuarioForm.setRespuestaSeguridad(usuarioForm.getRespuestaSeguridad());


        try {
            Usuario usuarioActualizado = usuarioService.guardarUsuario(usuarioForm);
            session.setAttribute("usuarioParaEditar", usuarioActualizado); 
            redirectAttributes.addFlashAttribute("mensaje", "Usuario actualizado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al actualizar el usuario: " + e.getMessage());
        }
        return "redirect:/usuarios/editar/" + usuarioForm.getId(); 
    }

    @GetMapping("/usuarios/eliminar_confirmar/{id}")
    public String confirmarEliminarUsuario(@PathVariable Long id, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioGlobal") == null) return "redirect:/login";
        Optional<Usuario> usuarioOpt = usuarioService.obtenerUsuarioPorId(id);
        if (usuarioOpt.isPresent()) {
            
            session.setAttribute("usuarioAEliminar", usuarioOpt.get());
            model.addAttribute("usuarioAEliminar", usuarioOpt.get());

            return "usuarios/eliminar"; 
        } else {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/usuarios/listar";
        }
    }

    
    
    
    
    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioGlobal") == null) return "redirect:/login";

        
        
        
        
        
        
        
        
        
        
        
        
        try {
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario eliminado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al eliminar el usuario.");
        }
        
        
        
        return "redirect:/usuarios/listar"; 
    }


    
    
    
    @GetMapping("/usuarios/buscar")
    public String mostrarFormularioBuscarUsuario(Model model, HttpSession session) {
        if (session.getAttribute("usuarioGlobal") == null) return "redirect:/login";

        
        if (!model.containsAttribute("usuarioEncontrado")) {
            session.removeAttribute("usuarioEncontrado");
        }

        model.addAttribute("usuarioBusqueda", new Usuario());
        return "usuarios/buscar";
    }

    @PostMapping("/usuarios/buscar")
    public String buscarUsuario(@RequestParam(required = false) Long id,
                                @RequestParam(required = false) String username,
                                HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioGlobal") == null) return "redirect:/login";

        Optional<Usuario> usuarioOpt = Optional.empty();

        if (id != null) {
            usuarioOpt = usuarioService.obtenerUsuarioPorId(id);
        } else if (username != null && !username.isEmpty()) {
            usuarioOpt = usuarioService.obtenerUsuarioPorUsername(username);
        } else {
            redirectAttributes.addFlashAttribute("error", "Debe proporcionar un ID o Username para buscar.");
            return "redirect:/usuarios/buscar";
        }

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            session.setAttribute("usuarioEncontrado", usuario);
            redirectAttributes.addFlashAttribute("usuarioEncontrado", usuario);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario encontrado.");
        } else {
            session.removeAttribute("usuarioEncontrado");
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
        }

        return "redirect:/usuarios/buscar";
    }

    @GetMapping("/usuarios/buscar_resultado")
    public String mostrarResultadoBusquedaUsuario(Model model, HttpSession session){
        if (session.getAttribute("usuarioGlobal") == null) return "redirect:/login";
        Usuario usuarioEncontrado = (Usuario) session.getAttribute("usuarioEncontrado");
        if (usuarioEncontrado != null) {
            model.addAttribute("usuarioEncontrado", usuarioEncontrado);
            model.addAttribute("usuarioBusqueda", new Usuario()); 
        }
        
        return "usuarios/buscar"; 
    }

}