# 🔒 PasswordManager-Java

<p align="center">
  <img src="./img/LogoPM.png" alt="Logo do Projeto" width="200">
</p>

![Status do Projeto](https://img.shields.io/badge/status-concluído-brightgreen)

Um gerenciador de senhas de console, seguro e local, construído puramente em Java como um projeto de estudo para aprofundar conhecimentos em conceitos fundamentais da linguagem e arquitetura de software.

---

## 🎯 Objetivo

O objetivo principal deste projeto foi criar uma aplicação de console funcional e segura para gerar e armazenar senhas. Serviu como um campo de aprendizado prático para os seguintes tópicos:
-   Programação Orientada a Objetos em Java.
-   Manipulação de arquivos (leitura e escrita com JSON).
-   Implementação de algoritmos de segurança (Hashing e Criptografia).
-   Estruturação de um projeto em pacotes com responsabilidades bem definidas (model, service, storage, etc.).
-   Interação com o sistema operacional (área de transferência).

---

## ✨ Funcionalidades

* **Geração de Senhas Seguras:** Cria senhas aleatórias customizáveis, misturando letras maiúsculas, minúsculas, números e símbolos.
* **Armazenamento Local e Criptografado:**
    * As senhas de serviços são salvas em um arquivo `JSON` local.
    * A senha mestra do usuário é protegida com **Hashing SHA-512 + Sal**, garantindo que a senha original nunca seja armazenada.
    * As senhas de serviços são criptografadas com **AES** antes de serem salvas, protegendo os dados mesmo que o arquivo seja acessado.
* **Sistema de Autenticação:** Acesso ao cofre de senhas protegido por uma conta de usuário e senha mestra.
* **Gerenciamento de Senhas:**
    * Adicionar senhas existentes manualmente.
    * Visualizar senhas salvas.
    * Deletar senhas de serviços específicos.
* **Copiar para a Área de Transferência:** Funcionalidade para copiar uma senha diretamente para o clipboard do sistema operacional, facilitando o uso.

---

## 🛠️ Tecnologias Utilizadas

* **Java SE 17+**
* **Jackson:** Biblioteca para manipulação (serialização e desserialização) de dados no formato JSON.
* **JCE (Java Cryptography Extension):** Utilizada para as implementações de segurança com os algoritmos SHA-512 e AES.

---

## 🚀 Como Executar o Projeto

Siga os passos abaixo para compilar e executar a aplicação localmente.

### Pré-requisitos

* [Java JDK 17](https://www.oracle.com/java/technologies/downloads/#java17) ou superior.
* [Git](https://git-scm.com/downloads).
* `curl` (geralmente já vem instalado em macOS e Linux) para baixar as dependências via terminal.

### Passos

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/SainthixOli/PasswordManager-Java.git](https://github.com/SainthixOli/PasswordManager-Java.git)
    cd PasswordManager-Java
    ```

2.  **Baixe as dependências (Jackson):**
    O projeto precisa da biblioteca Jackson para funcionar. O script abaixo cria a pasta `lib` e baixa os arquivos `.jar` necessários.
    ```bash
    # Cria a pasta e entra nela
    mkdir lib && cd lib

    # Baixa os 4 JARs necessários do Jackson
    curl -O [https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.17.1/jackson-databind-2.17.1.jar](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.17.1/jackson-databind-2.17.1.jar)
    curl -O [https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.17.1/jackson-core-2.17.1.jar](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.17.1/jackson-core-2.17.1.jar)
    curl -O [https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.17.1/jackson-annotations-2.17.1.jar](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.17.1/jackson-annotations-2.17.1.jar)
    curl -O [https://repo1.maven.org/maven2/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.17.1/jackson-datatype-jsr310-2.17.1.jar](https://repo1.maven.org/maven2/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.17.1/jackson-datatype-jsr310-2.17.1.jar)

    # Volta para a raiz do projeto
    cd ..
    ```

3.  **Execute o Script:**
    O projeto inclui um script (`run.sh`) que compila e executa a aplicação automaticamente. Primeiro, dê a ele permissão de execução:
    ```bash
    chmod +x run.sh
    ```
    Agora, rode o script:
    ```bash
    ./run.sh
    ```
    A aplicação será iniciada no seu terminal.

---

## 🗂️ Estrutura do Projeto

O código-fonte está organizado em pacotes com responsabilidades claras:

Perfeito! Vendo essa estrutura, atualizei a seção "Estrutura do Projeto" no seu README.md para refletir exatamente o que você tem aí, incluindo os novos arquivos na pasta data e os nomes dos JARs na pasta lib.

O restante do README (objetivos, funcionalidades, como executar, etc.) já estava alinhado com o que construímos.

Aqui está a versão final e atualizada. Pode copiar e colar no seu arquivo README.md.

Markdown

# 🔒 PasswordManager-Java

<p align="center">
  <img src="docs/img/LogoPM.png" alt="Logo do Projeto" width="200">
</p>

![Status do Projeto](https://img.shields.io/badge/status-concluído-brightgreen)

Um gerenciador de senhas de console, seguro e local, construído puramente em Java como um projeto de estudo para aprofundar conhecimentos em conceitos fundamentais da linguagem e arquitetura de software.

---

## 🎯 Objetivo

O objetivo principal deste projeto foi criar uma aplicação de console funcional e segura para gerar e armazenar senhas. Serviu como um campo de aprendizado prático para os seguintes tópicos:
-   Programação Orientada a Objetos em Java.
-   Manipulação de arquivos (leitura e escrita com JSON).
-   Implementação de algoritmos de segurança (Hashing e Criptografia).
-   Estruturação de um projeto em pacotes com responsabilidades bem definidas (model, service, storage, etc.).
-   Interação com o sistema operacional (área de transferência).

---

## ✨ Funcionalidades

* **Geração de Senhas Seguras:** Cria senhas aleatórias customizáveis, misturando letras maiúsculas, minúsculas, números e símbolos.
* **Armazenamento Local e Criptografado:**
    * Os dados dos usuários e senhas são salvos em um arquivo `JSON` local (`accounts.json`).
    * A senha mestra do usuário é protegida com **Hashing SHA-512 + Sal**, garantindo que a senha original nunca seja armazenada.
    * As senhas de serviços são criptografadas com **AES** antes de serem salvas, protegendo os dados mesmo que o arquivo seja acessado.
* **Sistema de Autenticação:** Acesso ao cofre de senhas protegido por uma conta de usuário e senha mestra.
* **Gerenciamento de Senhas:**
    * Adicionar senhas existentes manualmente.
    * Visualizar senhas salvas.
    * Deletar senhas de serviços específicos.
* **Copiar para a Área de Transferência:** Funcionalidade para copiar uma senha diretamente para o clipboard do sistema operacional, facilitando o uso.

---

## 🛠️ Tecnologias Utilizadas

* **Java SE 17+**
* **Jackson:** Biblioteca para manipulação (serialização e desserialização) de dados no formato JSON.
* **JCE (Java Cryptography Extension):** Utilizada para as implementações de segurança com os algoritmos SHA-512 e AES.

---

## 🚀 Como Executar o Projeto

Siga os passos abaixo para compilar e executar a aplicação localmente.

### Pré-requisitos

* [Java JDK 17](https://www.oracle.com/java/technologies/downloads/#java17) ou superior.
* [Git](https://git-scm.com/downloads).
* `curl` (geralmente já vem instalado em macOS e Linux) para baixar as dependências via terminal.

### Passos

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/SainthixOli/PasswordManager-Java.git](https://github.com/SainthixOli/PasswordManager-Java.git)
    cd PasswordManager-Java
    ```

2.  **Baixe as dependências (Jackson):**
    O projeto precisa da biblioteca Jackson para funcionar. O script abaixo cria a pasta `lib` e baixa os arquivos `.jar` necessários.
    ```bash
    # Cria a pasta e entra nela
    mkdir -p lib && cd lib

    # Baixa os 4 JARs necessários do Jackson
    curl -O [https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.17.1/jackson-databind-2.17.1.jar](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.17.1/jackson-databind-2.17.1.jar)
    curl -O [https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.17.1/jackson-core-2.17.1.jar](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.17.1/jackson-core-2.17.1.jar)
    curl -O [https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.17.1/jackson-annotations-2.17.1.jar](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-annotations/2.17.1/jackson-annotations-2.17.1.jar)
    curl -O [https://repo1.maven.org/maven2/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.17.1/jackson-datatype-jsr310-2.17.1.jar](https://repo1.maven.org/maven2/com/fasterxml/jackson/datatype/jackson-datatype-jsr310/2.17.1/jackson-datatype-jsr310-2.17.1.jar)

    # Volta para a raiz do projeto
    cd ..
    ```

3.  **Execute o Script:**
    O projeto inclui um script (`run.sh`) que compila e executa a aplicação automaticamente. Primeiro, dê a ele permissão de execução:
    ```bash
    chmod +x run.sh
    ```
    Agora, rode o script:
    ```bash
    ./run.sh
    ```
    A aplicação será iniciada no seu terminal.

---

## 🗂️ Estrutura do Projeto

A estrutura de pastas foi organizada para separar as responsabilidades de cada parte do código, seguindo boas práticas de arquitetura de software.

```
PasswordManager-Java/
├── bin/                  # Contém os arquivos .class compilados (gerado automaticamente)
├── data/
│   ├── accounts.json       # Arquivo principal com os dados dos usuários (criptografado)
│   ├── senhas.json         # (Arquivo legado ou para uso futuro)
│   └── usuarios.json       # (Arquivo legado ou para uso futuro)
├── docs/
│   ├── img/
│   │   └── LogoPM.png
│   ├── notas.md
│   ├── planejamento.md
│   └── requisitos.md
├── lib/
│   ├── jackson-annotations-2.17.1.jar
│   ├── jackson-core-2.17.1.jar
│   ├── jackson-databind-2.17.1.jar
│   └── jackson-datatype-jsr310-2.17.1.jar
├── LICENSE
├── README.md
├── run.sh                  # Script para compilar e executar o projeto
└── src/
    └── gerenciadorsenhas/
        ├── login/
        │   └── LoginService.java
        ├── main/
        │   └── Main.java
        ├── model/
        │   ├── Senha.java
        │   ├── SenhaInicial.java
        │   └── Usuario.java
        ├── service/
        │   └── Generate.java
        ├── storage/
        │   └── StorageService.java
        ├── test/
        │   ├── model/
        │   ├── service/
        │   └── util/
        └── util/
            └── Criptografia.java
```

---

## 👤 Autor

**Oliver Arthur Souza Pinheiro**

| Foto                                                                 | Desenvolvido por                                                                                                                              |
| :-------------------------------------------------------------------: | --------------------------------------------------------------------------------------------------------------------------------------------- |
| <img src="https://github.com/SainthixOli.png" width="100" alt="Foto"> | **Oliver Arthur Souza Pinheiro** <br/><br/> [LinkedIn](https://www.linkedin.com/in/SEU-PERFIL-AQUI) <br/> [GitHub](https://github.com/SainthixOli) |

---

## 📜 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.