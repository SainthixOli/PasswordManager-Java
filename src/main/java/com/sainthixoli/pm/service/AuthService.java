package com.sainthixoli.pm.service;

import com.sainthixoli.pm.model.Usuario;
import com.sainthixoli.pm.util.Criptografia;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    private final StorageService storageService;

    public AuthService(StorageService storageService) {
        this.storageService = storageService;
    }

    public List<Usuario> getAllUsers() {
        try {
            return storageService.carregarUsuarios();
        } catch (IOException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public Usuario authenticate(String username, String password) {
        System.out.println("Tentando autenticar usuário: " + username);
        List<Usuario> usuarios = getAllUsers();
        System.out.println("Total de usuários carregados: " + usuarios.size());

        Optional<Usuario> usuarioOpt = usuarios.stream()
                .filter(u -> u.getNomeDeUsuario().equalsIgnoreCase(username))
                .findFirst();

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            System.out.println("Usuário encontrado: " + usuario.getNomeDeUsuario());
            if (usuario.autenticarUsuario(password)) {
                System.out.println("Senha correta!");
                // Sucesso: Derivar a chave e retornar o usuário
                // Precisamos do sal. O sal está no SenhaInicial em Base64.
                String salBase64 = usuario.getSenhaDaConta().getSalBase64();
                byte[] sal = Base64.getDecoder().decode(salBase64);

                SecretKey sessionKey = Criptografia.derivarChave(password, sal);
                usuario.setChaveSessao(sessionKey);

                return usuario;
            } else {
                System.out.println("Senha incorreta.");
            }
        } else {
            System.out.println("Usuário não encontrado.");
        }
        return null; // Falha na autenticação
    }

    public Usuario register(String username, String password) throws IOException {
        List<Usuario> usuarios = getAllUsers();
        if (usuarios.stream().anyMatch(u -> u.getNomeDeUsuario().equalsIgnoreCase(username))) {
            throw new IllegalArgumentException("Usuário já existe.");
        }

        Usuario novoUsuario = new Usuario(username, password);

        // Já prepara a chave de sessão para ele não precisar relogar
        String salBase64 = novoUsuario.getSenhaDaConta().getSalBase64();
        byte[] sal = Base64.getDecoder().decode(salBase64);
        SecretKey sessionKey = Criptografia.derivarChave(password, sal);
        novoUsuario.setChaveSessao(sessionKey);

        usuarios.add(novoUsuario);
        storageService.salvarUsuarios(usuarios);

        return novoUsuario;
    }
}
