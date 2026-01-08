package com.sainthixoli.pm.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Senha {
    private String valor;
    private String nomeServico;
    private LocalDateTime dataCriacao;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public Senha() {
    }

    public Senha(String valor, String nomeServico) {
        this.valor = valor;
        this.nomeServico = nomeServico;
        this.dataCriacao = LocalDateTime.now();
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getNomeServico() {
        return nomeServico;
    }

    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public String toString() {
        return "Serviço: " + nomeServico +
                ", Senha: [PROTEGIDO]" +
                ", Criada em: " + (dataCriacao != null ? dataCriacao.format(formatter) : "N/A");
    }
}
