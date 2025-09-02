package main;


import login.Login;
import model.Usuario;
import service.Generate;
import storage.StorageService;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import model.Senha;



public class Main {

    // Serviços e ferramentas que a aplicação vai usar
    private static StorageService storageService;
    private static Login loginService;
    private static Generate geradorDeSenha;
    private static Scanner scanner;
    private static List<Usuario> usuariosDoSistema;

    public static void main(String[] args) {
        // 1. Inicialização dos componentes
        storageService = new StorageService(); // Usa a Criptografia internamente
        loginService = new Login();
        geradorDeSenha = new Generate();
        scanner = new Scanner(System.in);
        usuariosDoSistema = new ArrayList<>();

        // 2. Carregar usuários existentes do arquivo JSON
        try {
            usuariosDoSistema = storageService.carregarUsuarios();
            System.out.println("Dados carregados. " + usuariosDoSistema.size() + " usuário(s) encontrado(s).");
        } catch (IOException e) {
            System.err.println("!!! ERRO CRÍTICO ao carregar os dados dos usuários: " + e.getMessage());
            System.out.println("O programa iniciará com uma base de dados vazia.");
        }

        // 3. Loop do menu principal (Login / Criar Conta / Sair)
        loopPrincipal();

        // 4. Salvar tudo antes de fechar
        salvarDados();
        System.out.println("Programa finalizado. Dados salvos.");
        scanner.close();
    }

    private static void loopPrincipal() {
        while (true) {
            System.out.println("\n--- Password Manager ---");
            System.out.println("1. Login");
            System.out.println("2. Criar Nova Conta");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");

            int escolha = lerOpcao();

            switch (escolha) {
                case 1:
                    fazerLogin();
                    break;
                case 2:
                    criarNovaConta();
                    break;
                case 3:
                    return; // Sai do loopPrincipal e encerra o programa
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    /**
     * Pausa a execução e espera o usuário pressionar Enter para continuar.
     */
    private static void pressioneEnterParaContinuar() {
        System.out.println("\nPressione Enter para continuar...");
        scanner.nextLine();
    }

    private static void fazerLogin() {
        System.out.print("Digite o nome de usuário: ");
        String nomeUsuario = scanner.nextLine();
        System.out.print("Digite a senha: ");
        String senha = scanner.nextLine();

        Usuario usuarioLogado = loginService.autenticar(nomeUsuario, senha, usuariosDoSistema);

        if (usuarioLogado != null) {
            // MUDANÇA 1: Limpa a tela ANTES de entrar no menu do usuário
            limparTela();
            System.out.println("Login bem-sucedido! Bem-vindo, " + usuarioLogado.getNomeDeUsuario() + ".");
            
            menuDoUsuarioLogado(usuarioLogado); // Entra na sessão do usuário

            // MUDANÇA 2: Limpa a tela DEPOIS que o usuário desloga (quando o método acima termina)
            limparTela();
            System.out.println("Logout realizado com sucesso.");

        } else {
            System.out.println("Usuário ou senha inválidos. Tente novamente.");
            // Opcional: Adicionar uma pausa aqui também
            pressioneEnterParaContinuar();
        }
    }

    private static void criarNovaConta() {
        System.out.print("Digite o nome para o novo usuário: ");
        String nomeUsuario = scanner.nextLine();

        // Verifica se o usuário já existe
        boolean usuarioJaExiste = usuariosDoSistema.stream()
                .anyMatch(u -> u.getNomeDeUsuario().equalsIgnoreCase(nomeUsuario));

        if (usuarioJaExiste) {
            System.out.println("Este nome de usuário já está em uso. Tente outro.");
            return;
        }

        System.out.print("Digite a senha MESTRA para a nova conta: ");
        String senha = scanner.nextLine();
        
        // Adiciona validações de força de senha aqui se desejar

        Usuario novoUsuario = new Usuario(nomeUsuario, senha);
        usuariosDoSistema.add(novoUsuario);
        System.out.println("Usuário '" + nomeUsuario + "' criado com sucesso!");

        // Salva imediatamente a nova conta no arquivo
        salvarDados();
    }

   private static void menuDoUsuarioLogado(Usuario usuario) {
        while (true) {
            System.out.println("\n--- Menu do Usuário: " + usuario.getNomeDeUsuario() + " ---");
            System.out.println("1. Gerar e salvar nova senha para um serviço");
            System.out.println("2. Ver senhas salvas");
            System.out.println("3. Deletar senha de um serviço");
            System.out.println("4. Adicionar Senha Existente");
            System.out.println("5. Copiar senha de um serviço"); 
            System.out.println("6. Logout");
            System.out.print("Escolha uma opção: ");

            int escolha = lerOpcao();

            switch (escolha) {
                case 1:
                    System.out.print("Para qual serviço é a senha? (ex: Email, Facebook): ");
                    String nomeServico = scanner.nextLine();
                    System.out.print("Qual o tamanho desejado para a senha? (mín. 8): ");
                    int tamanho = lerOpcao();
                    if (tamanho < 8) {
                        System.out.println("Tamanho inválido. Usando o mínimo de 8 caracteres.");
                        tamanho = 8;
                    }

                    Senha novaSenha = usuario.gerarNovoPinParaServico(nomeServico, tamanho, geradorDeSenha);

                    if (novaSenha != null) {
                        System.out.println("\n✅ Senha gerada com sucesso!");
                        System.out.println("------------------------------------");
                        System.out.println("Serviço: " + novaSenha.getNomeServico());
                        System.out.println("Sua nova senha é: " + novaSenha.getValor());
                        System.out.println("------------------------------------");
                        System.out.println("(A senha já foi salva e criptografada no seu cofre.)");
                    } else {
                        System.out.println("❌ Falha ao gerar a senha.");
                    }
                    
                    salvarDados();
                    pressioneEnterParaContinuar();
                    break;

                case 2:
                    usuario.mostrarPinsDeServicos();
                    pressioneEnterParaContinuar();
                    break;

                case 3:
                    System.out.print("Digite o nome do serviço da senha que deseja deletar: ");
                    String servicoParaDeletar = scanner.nextLine();
                    usuario.deletarSenhaDeServico(servicoParaDeletar);
                    System.out.println("Tentativa de deleção para o serviço '" + servicoParaDeletar + "' concluída.");
                    salvarDados();
                    pressioneEnterParaContinuar();
                    break;
                case 4:
                    adicionarSenhaExistente(usuario);
                    break;    
                case 5:
                    
                    copiarSenhaDoUsuario(usuario);
                    break;
                case 6:
                    System.out.println("Fazendo logout...");
                    return;
                default:
                    System.out.println("Opção inválida.");
                    pressioneEnterParaContinuar();
                    break;
            }
        }
    }

    private static void salvarDados() {
        try {
            storageService.salvarUsuarios(usuariosDoSistema);
        } catch (IOException e) {
            System.err.println("!!! ERRO CRÍTICO ao salvar os dados: " + e.getMessage());
        }
    }
    
    // Função auxiliar para ler um inteiro do scanner de forma segura
    private static int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Retorna um valor inválido se não for um número
        }
    }
    /**
     * Limpa a tela do console. Tenta usar "clear" (para Linux/macOS) e
     * "cls" (para Windows).
     */
    private static void limparTela() {
        try {
            // Verifica o sistema operacional para usar o comando correto
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (final Exception e) {
            // Se falhar (o que é raro), apenas imprime várias linhas em branco
            // para simular uma tela limpa.
            for (int i = 0; i < 50; ++i) {
                System.out.println();
            }
        }
    }
    
    private static void adicionarSenhaExistente(Usuario usuario) {
        System.out.print("Digite o nome do serviço para o qual deseja adicionar a senha: ");
        String nomeServico = scanner.nextLine();

        System.out.print("Digite a senha em texto plano que deseja adicionar: ");
        String valorSenha = scanner.nextLine();

        if (nomeServico == null || nomeServico.trim().isEmpty() || valorSenha == null || valorSenha.trim().isEmpty()) {
            System.out.println("Nome do serviço ou valor da senha inválido. Operação cancelada.");
            return;
        }

        Senha novaSenha = new Senha(valorSenha, nomeServico);
        usuario.adicionarSenhaDeServico(novaSenha);
        System.out.println("Senha para o serviço '" + nomeServico + "' adicionada com sucesso.");

        salvarDados();
        pressioneEnterParaContinuar();
    }

    /**
     * Copia o texto fornecido para a área de transferência do sistema.
     * @param texto O texto a ser copiado.
     */
    private static void copiarParaAreaDeTransferencia(String texto) {
        // StringSelection é o objeto que "empacota" o nosso texto
        StringSelection stringSelection = new StringSelection(texto);
        
        // Acessamos a área de transferência do sistema operacional
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        
        // Definimos o conteúdo da área de transferência com o nosso texto empacotado
        clipboard.setContents(stringSelection, null);
        
        System.out.println("\n✅ Senha copiada para a área de transferência!");
        System.out.println("(Cuidado: a senha ficará disponível para ser colada. Cole-a em um local seguro.)");
    }

    private static void copiarSenhaDoUsuario(Usuario usuarioLogado) {
        List<Senha> senhasDoUsuario = usuarioLogado.getSenhasDeServicos();
        
        if (senhasDoUsuario == null || senhasDoUsuario.isEmpty()) {
            System.out.println("\nVocê não tem senhas salvas para copiar.");
            pressioneEnterParaContinuar();
            return;
        }

        System.out.println("\n--- Suas Senhas Salvas ---");
        // Mostra a lista de senhas, cada uma com um número
        for (int i = 0; i < senhasDoUsuario.size(); i++) {
            // (i + 1) para começar a numerar do 1 em vez do 0
            System.out.println((i + 1) + ". " + senhasDoUsuario.get(i).getNomeServico());
        }
        System.out.println("-------------------------");
        System.out.print("Digite o NÚMERO da senha que deseja copiar (ou 0 para cancelar): ");
        int numeroEscolhido = lerOpcao();

        // Valida a escolha do usuário
        if (numeroEscolhido > 0 && numeroEscolhido <= senhasDoUsuario.size()) {
            // Pega a senha escolhida da lista (lembrando que a lista começa no índice 0)
            Senha senhaParaCopiar = senhasDoUsuario.get(numeroEscolhido - 1);
            
            // Chama nosso novo método para copiar o VALOR da senha
            copiarParaAreaDeTransferencia(senhaParaCopiar.getValor());
        } else if (numeroEscolhido != 0) {
            System.out.println("Número inválido.");
        } else {
            System.out.println("Operação cancelada.");
        }
        
        pressioneEnterParaContinuar();
    }
}