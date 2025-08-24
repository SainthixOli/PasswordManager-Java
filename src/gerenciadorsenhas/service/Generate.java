package service; // Ou gerenciadorsenhas.service, conforme sua estrutura

import model.Senha;
import model.Usuario;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Generate {

    private static final String MINUSCULAS = "abcdefghijklmnopqrstuvwxyz";
    private static final String MAIUSCULAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMEROS = "0123456789";
    private static final String CARACTERES_ESPECIAIS = "!@#$%^&*+=-?<>()"; // Sua string
    private static final String TODOS_OS_CARACTERES = MINUSCULAS + MAIUSCULAS + NUMEROS + CARACTERES_ESPECIAIS;

    private static final SecureRandom random = new SecureRandom();

    /**
     * Gera uma string de senha aleatória com o tamanho especificado, garantindo
     * a inclusão de caracteres minúsculos, maiúsculos, números e especiais.
     *
     * @param tamanho O comprimento desejado para a senha. Deve ser no mínimo 4.
     * @return A string da senha gerada, ou null se o tamanho for inválido.
     */
    public String gerarNovaStringDeSenha(int tamanho) {
        if (tamanho < 4) {
            // System.err.println("SERVICE.GENERATE: Tamanho da senha deve ser pelo menos 4. Solicitado: " + tamanho);
            // Considerar lançar uma IllegalArgumentException em vez de retornar null
            // throw new IllegalArgumentException("Tamanho da senha deve ser pelo menos 4. Solicitado: " + tamanho);
            return null;
        }

        List<Character> listaCaracteresSenha = new ArrayList<>();

        // Garante pelo menos um caractere de cada conjunto
        listaCaracteresSenha.add(MINUSCULAS.charAt(random.nextInt(MINUSCULAS.length())));
        listaCaracteresSenha.add(MAIUSCULAS.charAt(random.nextInt(MAIUSCULAS.length())));
        listaCaracteresSenha.add(NUMEROS.charAt(random.nextInt(NUMEROS.length())));
        listaCaracteresSenha.add(CARACTERES_ESPECIAIS.charAt(random.nextInt(CARACTERES_ESPECIAIS.length())));

        // Preenche o restante da senha
        for (int i = 4; i < tamanho; i++) {
            listaCaracteresSenha.add(TODOS_OS_CARACTERES.charAt(random.nextInt(TODOS_OS_CARACTERES.length())));
        }

        Collections.shuffle(listaCaracteresSenha, random);

        StringBuilder senhaGeradaStr = new StringBuilder(tamanho);
        for (Character caractere : listaCaracteresSenha) {
            senhaGeradaStr.append(caractere);
        }
        return senhaGeradaStr.toString();
    }


    /**
     * Gera uma nova senha para um serviço e a adiciona ao objeto Usuario fornecido.
     * Este método utiliza {@link #gerarNovaStringDeSenha(int)} para criar a senha.
     * O nome deste método agora corresponde ao que a classe Usuario estava tentando chamar.
     *
     * @param usuario O objeto Usuario ao qual a nova senha será associada.
     * @param nomeServico O nome/descrição do serviço para o qual a senha está sendo gerada.
     * @param tamanho O tamanho desejado para a senha.
     */
    public Senha gerarSenhaParaUsuarioESalvar(Usuario usuario, String nomeServico, int tamanho) {
        if (usuario == null || nomeServico == null || nomeServico.trim().isEmpty()) {
            System.err.println("SERVICE.GENERATE: Erro - Usuário ou nome do serviço inválido.");
            return null;
        }

        String senhaGeradaTextoPlano = gerarNovaStringDeSenha(tamanho);

        if (senhaGeradaTextoPlano != null) {
            Senha novaSenhaDeServico = new Senha(senhaGeradaTextoPlano, nomeServico);
            usuario.adicionarSenhaDeServico(novaSenhaDeServico);
            
            // MUDANÇA 2: Retorna o objeto Senha que foi criado
            return novaSenhaDeServico;
        } else {
            System.err.println("SERVICE.GENERATE: Não foi possível gerar a string da senha para o serviço '" + nomeServico + "'.");
            return null; // Retorna null em caso de falha
        }
    }
      
}
