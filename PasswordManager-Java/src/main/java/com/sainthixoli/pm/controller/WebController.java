package com.sainthixoli.pm.controller;

import com.sainthixoli.pm.model.Usuario;
import com.sainthixoli.pm.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    private final AuthService authService;

    public WebController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/")
    public String index(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/vault";
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/vault";
        }
        return "login";
    }

    @PostMapping("/login")
    public String doLogin(@RequestParam String username, @RequestParam String password, HttpSession session,
            Model model) {
        Usuario usuario = authService.authenticate(username, password);
        if (usuario != null) {
            session.setAttribute("user", usuario);
            // Session timeout is handled by application.properties or container,
            // but we can also set it per session. Default is usually good (30m), user asked
            // for 15m.
            session.setMaxInactiveInterval(15 * 60);
            return "redirect:/vault";
        }
        model.addAttribute("error", "Usuário ou senha inválidos.");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/vault";
        }
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String username, @RequestParam String password,
            @RequestParam String confirmPassword, Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "As senhas não conferem.");
            return "register";
        }
        try {
            authService.register(username, password);
            return "redirect:/login?registered=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "Erro ao criar conta.");
            return "register";
        }
    }

    @GetMapping("/vault")
    public String vault(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("user");
        if (usuario == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuario);
        return "vault";
    }
}
