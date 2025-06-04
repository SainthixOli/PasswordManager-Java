package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Senha {
    private String valor;
    private String nomeServico;
    private LocalDateTime dataCriacao;

    // Formatter pode ser estático para reutilização
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public Senha(String valor, String nomeServico) {
        this.valor = valor;
        this.nomeServico = nomeServico;
        this.dataCriacao = LocalDateTime.now();
    }

    // Construtor padrão para desserialização (ex: Jackson)
    public Senha() {}

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) { // Setter para desserialização ou manipulação interna
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
               ", Senha: [PROTEGIDO]" + // Em memória está em texto plano, mas o toString protege
               ", Criada em: " + (dataCriacao != null ? dataCriacao.format(formatter) : "N/A");
    }
}
