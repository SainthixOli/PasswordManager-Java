package model;

import java.time.format.DateTimeFormatter;
import service.Generate;
import java.util.ArrayList;

import java.util.List;
import java.util.Iterator;
import javax.crypto.SecretKey;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Usuario {
    private String nomeDeUsuario;
    private SenhaInicial senhaDaConta;
    private List<Senha> senhasDeServicos;

    @JsonIgnore
    private transient SecretKey chaveSessao;

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

    public Senha gerarNovoPinParaServico(String nomeDoServico, int tamanhoDaSenha, Generate gerador) {
        if (gerador == null) {
            System.err.println("Erro: Instância do gerador não fornecida.");
            return null;
        }

        String senhaPlana = gerador.gerarNovaStringDeSenha(tamanhoDaSenha);
        if (senhaPlana == null)
            return null;

        String senhaCifrada;
        try {
            if (this.chaveSessao == null) {
                throw new IllegalStateException("Chave de sessão não inicializada. Faça login novamente.");
            }
            senhaCifrada = util.Criptografia.cifrar(senhaPlana, this.chaveSessao);
        } catch (Exception e) {
            System.err.println("Erro ao cifrar senha: " + e.getMessage());
            return null;
        }

        Senha novaSenha = new Senha(senhaCifrada, nomeDoServico);
        this.adicionarSenhaDeServico(novaSenha);
        return novaSenha;
    }

    public void mostrarPinsDeServicos() {
        // Método legado de console, pode ser mantido ou removido.
        // Se mantido, não vai mostrar senhas decifradas a menos que usemos a chave.
        System.out.println("Método de console descontinuado. Use a interface gráfica.");
    }

    public void deletarSenhaDeServico(String nomeDoServico) {
        if (this.senhasDeServicos == null) {
            return;
        }
        for (Iterator<Senha> iterator = this.senhasDeServicos.iterator(); iterator.hasNext();) {
            Senha senha = iterator.next();
            if (senha.getNomeServico().equalsIgnoreCase(nomeDoServico)) {
                iterator.remove();
                break;
            }
        }
    }

    public void deletarCadastroDeServico(String nomeDoServico) {
        deletarSenhaDeServico(nomeDoServico);
    }

    public boolean autenticarUsuario(String tentativaDeSenha) {
        if (this.senhaDaConta == null)
            return false;
        return this.senhaDaConta.verificarSenha(tentativaDeSenha);
    }

    @JsonIgnore
    public SecretKey getChaveSessao() {
        return chaveSessao;
    }

    @JsonIgnore
    public void setChaveSessao(SecretKey chaveSessao) {
        this.chaveSessao = chaveSessao;
    }
}