package login;

import model.Usuario;
import java.util.List;
import java.util.Optional;

public class Login {

    /**
     * Autentica um usuário verificando suas credenciais contra uma lista de usuários cadastrados.
     *
     * @param nomeUsuario O nome de usuário fornecido para login.
     * @param senhaDigitada A senha em texto plano fornecida para login.
     * @param usuariosCadastrados A lista de todos os usuários carregados do sistema.
     * @return O objeto Usuario se a autenticação for bem-sucedida, ou null caso contrário.
     */
    public Usuario autenticar(String nomeUsuario, String senhaDigitada, List<Usuario> usuariosCadastrados) {
        if (nomeUsuario == null || senhaDigitada == null || usuariosCadastrados == null) {
            return null;
        }

        // Procura pelo usuário na lista
        Optional<Usuario> usuarioEncontrado = usuariosCadastrados.stream()
                .filter(u -> u.getNomeDeUsuario().equalsIgnoreCase(nomeUsuario))
                .findFirst();

        // Se o usuário foi encontrado, verifica a senha
        if (usuarioEncontrado.isPresent()) {
            Usuario usuario = usuarioEncontrado.get();
            // O método autenticarUsuario na classe Usuario usa o hash + sal para verificar
            if (usuario.autenticarUsuario(senhaDigitada)) {
                return usuario; // Sucesso! Retorna o objeto do usuário logado.
            }
        }

        return null; // Falha (usuário não encontrado ou senha incorreta)
    }
}
