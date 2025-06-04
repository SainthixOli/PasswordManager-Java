package storage; // Ou gerenciadorsenhas.storage, conforme sua estrutura

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import model.Usuario;
import model.Senha;
import util.Criptografia; // Certifique-se que o pacote de Criptografia está correto (ex: gerenciadorsenhas.util)

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StorageService {

    // --- ADICIONADO: Constantes para caminho e nome do arquivo ---
    private static final String CAMINHO_PASTA_DADOS = "data"; // Nome da pasta de dados
    private static final String NOME_ARQUIVO_CONTAS = "accounts.json"; // Nome do arquivo JSON

    // --- ADICIONADO: Campo para o arquivo JSON ---
    private final File arquivoDeContas;
    private final ObjectMapper objectMapper;

    public StorageService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // --- ADICIONADO: Inicialização do campo arquivoDeContas ---
        // Cria o caminho completo para o arquivo
        this.arquivoDeContas = new File(CAMINHO_PASTA_DADOS, NOME_ARQUIVO_CONTAS);
    }

    public void salvarUsuarios(List<Usuario> usuarios) throws IOException {
        // Garante que o diretório de dados exista
        File pastaDados = this.arquivoDeContas.getParentFile(); // Pega a pasta 'data'
        if (!pastaDados.exists()) {
            if (!pastaDados.mkdirs()) {
                throw new IOException("Não foi possível criar o diretório de dados: " + pastaDados.getAbsolutePath());
            }
        }

        List<Usuario> usuariosParaSalvar = new ArrayList<>();
        for (Usuario usuarioOriginal : usuarios) {
            Usuario usuarioCopia = new Usuario(); // Usa o construtor padrão
            // Agora os setters e getters devem existir em Usuario:
            usuarioCopia.setNomeDeUsuario(usuarioOriginal.getNomeDeUsuario());
            usuarioCopia.setSenhaDaConta(usuarioOriginal.getSenhaDaConta()); // Usa o getter

            List<Senha> senhasCifradas = new ArrayList<>();
            if (usuarioOriginal.getSenhasDeServicos() != null) {
                for (Senha senhaOriginal : usuarioOriginal.getSenhasDeServicos()) {
                    String valorCifrado = Criptografia.cifrar(senhaOriginal.getValor());
                    Senha senhaCopia = new Senha(); // Construtor padrão de Senha
                    senhaCopia.setNomeServico(senhaOriginal.getNomeServico());
                    senhaCopia.setValor(valorCifrado);
                    senhaCopia.setDataCriacao(senhaOriginal.getDataCriacao());
                    senhasCifradas.add(senhaCopia);
                }
            }
            usuarioCopia.setSenhasDeServicos(senhasCifradas); // Usa o setter
            usuariosParaSalvar.add(usuarioCopia);
        }
        objectMapper.writeValue(this.arquivoDeContas, usuariosParaSalvar);
    }

    public List<Usuario> carregarUsuarios() throws IOException {
        if (!this.arquivoDeContas.exists()) {
            return new ArrayList<>();
        }
        List<Usuario> usuariosCarregados = objectMapper.readValue(this.arquivoDeContas, new TypeReference<List<Usuario>>() {});

        for (Usuario usuario : usuariosCarregados) {
            if (usuario.getSenhasDeServicos() != null) {
                for (Senha senha : usuario.getSenhasDeServicos()) {
                    // Verifica se o valor não é o placeholder de erro antes de tentar decifrar
                    if (senha.getValor() != null && !senha.getValor().equals("[DADOS_CRIPTOGRAFADOS_ILEGIVEIS]")) {
                        try {
                            String valorDecifrado = Criptografia.decifrar(senha.getValor());
                            senha.setValor(valorDecifrado);
                        } catch (Exception e) {
                            System.err.println("STORAGE.SERVICE: Não foi possível decifrar a senha para o serviço '" +
                                               senha.getNomeServico() + "' do usuário '" + usuario.getNomeDeUsuario() + "'.");
                            senha.setValor("[DADOS_CRIPTOGRAFADOS_ILEGIVEIS]");
                        }
                    }
                }
            }
        }
        return usuariosCarregados;
    }
}
