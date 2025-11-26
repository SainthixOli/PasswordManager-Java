package storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import model.Usuario;

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
        // Salva os usuários como estão (assumindo que as senhas já estão criptografadas
        // nos objetos)
        objectMapper.writeValue(this.arquivoDeContas, usuarios);
    }

    public List<Usuario> carregarUsuarios() throws IOException {
        if (!this.arquivoDeContas.exists()) {
            return new ArrayList<>();
        }
        // Carrega os usuários (as senhas virão criptografadas do JSON)
        return objectMapper.readValue(this.arquivoDeContas, new TypeReference<List<Usuario>>() {
        });
    }
}
