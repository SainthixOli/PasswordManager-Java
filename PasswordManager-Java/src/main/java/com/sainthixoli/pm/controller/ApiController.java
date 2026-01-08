package com.sainthixoli.pm.controller;

import com.sainthixoli.pm.model.Usuario;
import com.sainthixoli.pm.service.StorageService;
import com.sainthixoli.pm.util.Criptografia;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    private final StorageService storageService;

    public ApiController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/decrypt")
    public ResponseEntity<?> decryptPassword(@RequestBody Map<String, String> payload, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("user");
        if (usuario == null) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Sessão expirada"));
        }

        String serviceName = payload.get("serviceName");
        if (serviceName == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Nome do serviço obrigatório"));
        }

        return usuario.getSenhasDeServicos().stream()
                .filter(s -> s.getNomeServico().equalsIgnoreCase(serviceName))
                .findFirst()
                .map(senha -> {
                    try {
                        String decrypted = Criptografia.decifrar(senha.getValor(), usuario.getChaveSessao());
                        return ResponseEntity.ok(Collections.singletonMap("password", decrypted));
                    } catch (Exception e) {
                        return ResponseEntity.internalServerError()
                                .body(Collections.singletonMap("error", "Erro ao descriptografar"));
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPassword(@RequestBody Map<String, String> payload, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("user");
        if (usuario == null) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Sessão expirada"));
        }

        String serviceName = payload.get("serviceName");
        String plainPassword = payload.get("password");

        if (serviceName == null || plainPassword == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Dados incompletos"));
        }

        try {
            // Criptografar e adicionar
            usuario.gerarNovoPinParaServico(serviceName, plainPassword, usuario.getChaveSessao());

            // Salvar no disco
            // NOTA: Em app real, idealmente só salvaríamos este usuário, mas o
            // StorageService salva a lista toda.
            // Precisamos carregar a lista, atualizar este usuário, e salvar.
            // Como 'usuario' é uma referência que veio da lista (se carregado via
            // AuthService),
            // precisamos garantir que o estado persistido esteja sincronizado.
            // Para simplificar: recarregamos tudo, achamos o user, atualizamos e salvamos.

            var allUsers = storageService.carregarUsuarios();
            for (int i = 0; i < allUsers.size(); i++) {
                if (allUsers.get(i).getNomeDeUsuario().equals(usuario.getNomeDeUsuario())) {
                    // Substituir pelo usuário da sessão que tem a nova senha
                    // (Mas o usuário da sessão tem a chaveSessao que é transient, nao será salva, o
                    // que é bom)
                    allUsers.set(i, usuario);
                    break;
                }
            }
            storageService.salvarUsuarios(allUsers);

            return ResponseEntity.ok(Collections.singletonMap("message", "Senha salva com sucesso"));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Collections.singletonMap("error", "Erro ao salvar no disco"));
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deletePassword(@RequestBody Map<String, String> payload, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("user");
        if (usuario == null) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", "Sessão expirada"));
        }

        String serviceName = payload.get("serviceName");
        if (serviceName == null)
            return ResponseEntity.badRequest().build();

        usuario.deletarSenhaDeServico(serviceName);

        try {
            var allUsers = storageService.carregarUsuarios();
            for (int i = 0; i < allUsers.size(); i++) {
                if (allUsers.get(i).getNomeDeUsuario().equals(usuario.getNomeDeUsuario())) {
                    allUsers.set(i, usuario);
                    break;
                }
            }
            storageService.salvarUsuarios(allUsers);
            return ResponseEntity.ok(Collections.singletonMap("message", "Removido"));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Collections.singletonMap("error", "Erro ao salvar"));
        }
    }
}
