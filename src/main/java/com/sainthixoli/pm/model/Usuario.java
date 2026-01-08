package com.sainthixoli.pm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sainthixoli.pm.util.Criptografia;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String nomeDeUsuario;
    private SenhaInicial senhaDaConta;
    private List<Senha> senhasDeServicos = new ArrayList<>();

    @JsonIgnore
    private transient SecretKey chaveSessao;

    public Usuario() {
        this.senhasDeServicos = new ArrayList<>();
    }

    public Usuario(String nomeDeUsuario, String senhaInicialParaConta) {
        this.nomeDeUsuario = nomeDeUsuario;
        this.senhaDaConta = new SenhaInicial(nomeDeUsuario, senhaInicialParaConta);
        this.senhasDeServicos = new ArrayList<>();
    }

    public void adicionarSenhaDeServico(Senha senha) {
        if (this.senhasDeServicos == null) {
            this.senhasDeServicos = new ArrayList<>();
        }
        this.senhasDeServicos.add(senha);
    }

    public Senha gerarNovoPinParaServico(String nomeDoServico, String senhaPlana, SecretKey chaveSessao) {
        if (senhaPlana == null || chaveSessao == null) {
            return null;
        }

        String senhaCifrada;
        try {
            senhaCifrada = Criptografia.cifrar(senhaPlana, chaveSessao);
        } catch (Exception e) {
            System.err.println("Erro ao cifrar senha: " + e.getMessage());
            return null;
        }

        Senha novaSenha = new Senha(senhaCifrada, nomeDoServico);
        adicionarSenhaDeServico(novaSenha);
        return novaSenha;
    }

    public void deletarSenhaDeServico(String nomeDoServico) {
        if (this.senhasDeServicos == null) {
            return;
        }
        this.senhasDeServicos.removeIf(senha -> senha.getNomeServico().equalsIgnoreCase(nomeDoServico));
    }

    public boolean autenticarUsuario(String tentativaDeSenha) {
        if (this.senhaDaConta == null)
            return false;
        return this.senhaDaConta.verificarSenha(tentativaDeSenha);
    }

    public String getNomeDeUsuario() {
        return nomeDeUsuario;
    }

    public void setNomeDeUsuario(String nomeDeUsuario) {
        this.nomeDeUsuario = nomeDeUsuario;
    }

    public SenhaInicial getSenhaDaConta() {
        return senhaDaConta;
    }

    public void setSenhaDaConta(SenhaInicial senhaDaConta) {
        this.senhaDaConta = senhaDaConta;
    }

    public List<Senha> getSenhasDeServicos() {
        if (senhasDeServicos == null) {
            senhasDeServicos = new ArrayList<>();
        }
        return senhasDeServicos;
    }

    public void setSenhasDeServicos(List<Senha> senhasDeServicos) {
        this.senhasDeServicos = senhasDeServicos;
    }

    @JsonIgnore
    public SecretKey getChaveSessao() {
        return chaveSessao;
    }

    public void setChaveSessao(SecretKey chaveSessao) {
        this.chaveSessao = chaveSessao;
    }
}