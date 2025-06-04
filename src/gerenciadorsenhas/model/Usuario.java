package model;

import service.Generate;
import java.util.ArrayList;

import java.util.List;
import java.util.Iterator;

public class Usuario {
    private String nomeDeUsuario;
    private SenhaInicial senhaDaConta;
    private List<Senha> senhasDeServicos;

    public Usuario(String nomeDeUsuario, String senhaInicialParaConta) {
        this.nomeDeUsuario = nomeDeUsuario;
        this.senhaDaConta = new SenhaInicial(nomeDeUsuario, senhaInicialParaConta);
        this.senhasDeServicos = new ArrayList<>();
    }

    public Usuario() {
        this.senhasDeServicos = new ArrayList<>();
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
        if (this.senhasDeServicos == null) {
            this.senhasDeServicos = new ArrayList<>();
        }
        return this.senhasDeServicos;
    }

    public void setSenhasDeServicos(List<Senha> senhasDeServicos) {
        this.senhasDeServicos = senhasDeServicos;
    }

    public void adicionarSenhaDeServico(Senha senha) {
        if (this.senhasDeServicos == null) {
            this.senhasDeServicos = new ArrayList<>();
        }
        this.senhasDeServicos.add(senha);
    }

    public void gerarNovoPinParaServico(String nomeDoServico, int tamanhoDaSenha, Generate gerador) {
        if (gerador == null) {
            System.err.println("Erro: Instância do gerador não fornecida.");
            return;
        }
        gerador.gerarSenhaParaUsuarioESalvar(this, nomeDoServico, tamanhoDaSenha);
    }

    public void mostrarPinsDeServicos() {
        System.out.println("\n--- PINs/Senhas de Serviços para " + nomeDeUsuario + " ---");
        List<Senha> senhasAtuais = this.senhasDeServicos;
        if (senhasAtuais == null || senhasAtuais.isEmpty()) {
            System.out.println("Nenhuma senha de serviço cadastrada.");
        } else {
            for (Senha senha : senhasAtuais) {
                System.out.println(senha.toString());
            }
        }
        System.out.println("------------------------------------");
    }

    public void deletarSenhaDeServico(String nomeDoServico) {
        if (this.senhasDeServicos == null) {
            return;
        }
        @SuppressWarnings("unused")
        boolean removido = false;
        for (Iterator<Senha> iterator = this.senhasDeServicos.iterator(); iterator.hasNext();) {
            Senha senha = iterator.next();
            if (senha.getNomeServico().equalsIgnoreCase(nomeDoServico)) {
                iterator.remove();
                removido = true;
                break;
            }
        }
        // Removi os System.out daqui para deixar a classe mais focada no modelo
    }

    public void deletarCadastroDeServico(String nomeDoServico) {
        deletarSenhaDeServico(nomeDoServico);
    }

    public boolean autenticarUsuario(String tentativaDeSenha) {
        if (this.senhaDaConta == null) return false;
        return this.senhaDaConta.verificarSenha(tentativaDeSenha);
    }
}