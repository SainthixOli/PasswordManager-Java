package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64; // Usaremos para converter byte[] para String e vice-versa
import java.time.format.DateTimeFormatter;

public class SenhaInicial {
    private String nomeUsuarioReferencia;
    private String hashDaSenhaArmazenado; // Hash da senha + sal, em Base64
    private String salBase64;             // Sal em Base64
    private LocalDateTime dataCriacao;

    private static final int TAMANHO_SAL_BYTES = 16;
    private static final String ALGORITMO_HASH = "SHA-512";

    /**
     * Cria uma nova instância de SenhaInicial.
     * A senha em texto plano é combinada com um sal gerado aleatoriamente
     * e então hasheada usando SHA-512.
     *
     * @param nomeUsuario O nome do usuário associado a esta senha.
     * @param senhaEmTextoPlano A senha fornecida pelo usuário em texto plano.
     */
    public SenhaInicial(String nomeUsuario, String senhaEmTextoPlano) {
        this.nomeUsuarioReferencia = nomeUsuario;
        this.dataCriacao = LocalDateTime.now();

        // 1. Gerar um Sal Aleatório
        byte[] salBytes = gerarSal();
        this.salBase64 = Base64.getEncoder().encodeToString(salBytes);

        // 2. Hashear a senha com o sal
        this.hashDaSenhaArmazenado = hashearSenha(senhaEmTextoPlano, salBytes);

        // System.out.println("MODEL.SENHAINICIAL: Senha inicial para " + nomeUsuario + " criada com hash SHA-512 e sal.");
    }

    /**
     * Verifica se a tentativa de senha corresponde à senha armazenada.
     *
     * @param tentativaDeSenhaEmTextoPlano A senha fornecida para verificação.
     * @return true se a senha corresponder, false caso contrário.
     */
    public boolean verificarSenha(String tentativaDeSenhaEmTextoPlano) {
        if (this.salBase64 == null || this.hashDaSenhaArmazenado == null) {
            // Caso de objeto não inicializado corretamente ou dados corrompidos
            System.err.println("Erro: Sal ou Hash não presentes para verificação.");
            return false;
        }
        byte[] salBytes = Base64.getDecoder().decode(this.salBase64);
        String hashDaTentativa = hashearSenha(tentativaDeSenhaEmTextoPlano, salBytes);
        return this.hashDaSenhaArmazenado.equals(hashDaTentativa);
    }

    private byte[] gerarSal() {
        SecureRandom random = new SecureRandom();
        byte[] sal = new byte[TAMANHO_SAL_BYTES];
        random.nextBytes(sal);
        return sal;
    }

    private String hashearSenha(String senha, byte[] sal) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITMO_HASH);
            md.update(sal); // Adiciona o sal ao digest
            byte[] bytesDoHash = md.digest(senha.getBytes(StandardCharsets.UTF_8)); // Adiciona a senha e calcula o hash
            return Base64.getEncoder().encodeToString(bytesDoHash);
        } catch (NoSuchAlgorithmException e) {
            // Em um app real, tratar essa exceção de forma mais robusta.
            // Aqui, estamos assumindo que SHA-512 estará sempre disponível.
            throw new RuntimeException("Erro ao hashear senha: Algoritmo " + ALGORITMO_HASH + " não encontrado.", e);
        }
    }

    public String getNomeUsuarioReferencia() {
        return nomeUsuarioReferencia;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    // Getters para o hash e o sal são necessários para persistência (ex: salvar em JSON)
    // Não exponha o hash ou o sal de forma que sugira que podem ser usados para reverter a senha.
    public String getHashDaSenhaArmazenado() {
        return hashDaSenhaArmazenado;
    }

    public String getSalBase64() {
        return salBase64;
    }

    // Construtor padrão para frameworks de desserialização (ex: Jackson para JSON)
    // Se os campos forem `final`, este construtor pode não ser necessário ou
    // os campos precisarão ser setados via setters ou construtor com argumentos.
    // Por enquanto, vamos adicionar setters se precisarmos para JSON.
    public SenhaInicial() {}

    // Setters podem ser necessários para desserialização JSON se não usar construtor com todos os args
    public void setNomeUsuarioReferencia(String nomeUsuarioReferencia) {
        this.nomeUsuarioReferencia = nomeUsuarioReferencia;
    }

    public void setHashDaSenhaArmazenado(String hashDaSenhaArmazenado) {
        this.hashDaSenhaArmazenado = hashDaSenhaArmazenado;
    }

    public void setSalBase64(String salBase64) {
        this.salBase64 = salBase64;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }


    @Override
    public String toString() {
        return "SenhaInicial para: " + nomeUsuarioReferencia +
               " (Criada em: " + dataCriacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) +
               ") - Hash: [PROTEGIDO_COM_SHA-512_E_SAL]";
    }
}