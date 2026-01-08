# 🧠 Planejamento do Projeto: PasswordManager-Java

## 🎯 Objetivo

Criar uma aplicação Java local para **gerenciar e gerar senhas seguras**, com acesso restrito por autenticação, criptografia de dados e estrutura organizada. O projeto será feito com os recursos nativos da linguagem Java.

---

## 🔧 Funcionalidades Planejadas

1. **Gerador de Senhas Seguras**
   - Geração aleatória de senhas misturando letras, números e símbolos.
   - Garantia de aleatoriedade para evitar senhas repetidas.

2. **Gerenciador de Senhas**
   - Armazenamento local das senhas criadas.
   - Informações armazenadas: senha gerada, local/cadastro associado e data de criação.
   - Visualização segura das senhas salvas.

3. **Tela de Login**
   - Interface de autenticação com ID e senha para acesso ao sistema.
   - Impede acesso não autorizado às senhas salvas.

4. **Criptografia**
   - Criptografia das senhas salvas e das credenciais de login do usuário.
   - Segurança local dos dados mesmo com acesso ao arquivo físico.

---

## 🗂️ Estrutura de Pastas
PasswordManager-Java/
│
├── src/                        # Código-fonte Java
│   └── gerenciadorsenhas/
│       ├── login/             # Lógica de autenticação
│       ├── model/             # Modelos de dados (Usuário, Senha, etc.)
│       ├── service/           # Regras de negócio (geração, criptografia)
│       ├── storage/           # Leitura/escrita dos arquivos locais
│       └── utils/             # Funções auxiliares (validações, helpers)
│
├── data/                      # Arquivos locais de dados criptografados
│
├── docs/                      # Documentação do projeto
│   └── planejamento.md        # Este arquivo
|   ├── requisitos.md          # Requisitos funcionais e não funcionais
│   ├── notas.md               # Anotações soltas, ideias, TODOs
│   └── README.md              # Introdução principal do projeto (pode ser espelhado no GitHub)
│
└── .gitignore                 # Arquivos a serem ignorados pelo Git

---

## 💡 Tecnologias e Recursos

- **Java SE 17+**
- Criptografia com bibliotecas nativas (ex: `javax.crypto`)
- Armazenamento local com leitura/escrita de arquivos (`FileReader`, `BufferedWriter`, etc.)

---

## 📅 Etapas de Desenvolvimento

1. [ ] Estruturação das pastas e classes base
2. [ ] Criação da interface de login
3. [ ] Implementação do gerador de senhas
4. [ ] Sistema de criptografia
5. [ ] Sistema de armazenamento seguro
6. [ ] Validações e tratamento de erros
7. [ ] Testes manuais e ajustes finais

---

## 👤 Desenvolvedor

- **Nome:** (Oliver Arthur Souza Pinheiro)
- **GitHub:** [@SainthixOli](https://github.com/SainthixOli)

---

## 📌 Observações Finais

Este é um projeto pessoal com fins educacionais, planejado para fixar os conhecimentos aprendidos no estudo de Java. A ideia é aplicar os fundamentos da linguagem de forma prática e organizada.