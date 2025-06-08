package com.ejercicio.controller;

import com.ejercicio.model.Usuario;
import com.ejercicio.service.impl.UsuarioServiceImpl; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.servlet.http.HttpSession; 


@Controller
public class IndexController {
    @Autowired
    private UsuarioServiceImpl usuarioService; 

    @GetMapping("/index")
    public String showIndexPage(Model model, HttpSession session) {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        
        if (authentication != null && authentication.isAuthenticated() &&
                !(authentication.getPrincipal() instanceof String && authentication.getPrincipal().equals("anonymousUser"))) {

            Object principal = authentication.getPrincipal();
            String username = null;

            if (principal instanceof UserDetails) {
                username = ((UserDetails) principal).getUsername();
            } else if (principal instanceof String) {
                username = (String) principal;
            }

            if (username != null) {
                
                
                Usuario usuario = usuarioService.findByUsername(username); 

                if (usuario != null) {
                    
                    model.addAttribute("usuarioSesion", usuario);
                    
                    session.setAttribute("usuarioSesion", usuario);
                    session.setAttribute("usuarioGlobal", usuario);
                }
            }
        }
        return "index"; 
    }

}

