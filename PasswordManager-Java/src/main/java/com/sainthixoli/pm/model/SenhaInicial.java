package com.sainthixoli.pm.model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

public class SenhaInicial {
    private String nomeUsuarioReferencia;
    private String hashDaSenhaArmazenado;
    private String salBase64;
    private LocalDateTime dataCriacao;

    private static final int TAMANHO_SAL_BYTES = 16;
    private static final String ALGORITMO_HASH = "SHA-512";

    public SenhaInicial() {
    }

    public SenhaInicial(String nomeUsuario, String senhaEmTextoPlano) {
        this.nomeUsuarioReferencia = nomeUsuario;
        this.dataCriacao = LocalDateTime.now();
        byte[] salBytes = gerarSal();
        this.salBase64 = Base64.getEncoder().encodeToString(salBytes);
        this.hashDaSenhaArmazenado = hashearSenha(senhaEmTextoPlano, salBytes);
    }

    public boolean verificarSenha(String tentativaDeSenhaEmTextoPlano) {
        if (this.salBase64 == null || this.hashDaSenhaArmazenado == null) {
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
            md.update(sal);
            byte[] bytesDoHash = md.digest(senha.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(bytesDoHash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erro ao hashear senha.", e);
        }
    }

    public String getNomeUsuarioReferencia() {
        return nomeUsuarioReferencia;
    }

    public void setNomeUsuarioReferencia(String nomeUsuarioReferencia) {
        this.nomeUsuarioReferencia = nomeUsuarioReferencia;
    }

    public String getHashDaSenhaArmazenado() {
        return hashDaSenhaArmazenado;
    }

    public void setHashDaSenhaArmazenado(String hashDaSenhaArmazenado) {
        this.hashDaSenhaArmazenado = hashDaSenhaArmazenado;
    }

    public String getSalBase64() {
        return salBase64;
    }

    public void setSalBase64(String salBase64) {
        this.salBase64 = salBase64;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public String toString() {
        return "SenhaInicial para: " + nomeUsuarioReferencia;
    }
}