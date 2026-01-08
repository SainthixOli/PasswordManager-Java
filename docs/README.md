# 🛡️ Selo
> **Segurança Máxima. Simplicidade Absoluta.**

<p align="center">
  <img src="./img/selo.png" alt="Logo Selo" width="300">
</p>

![Status](https://img.shields.io/badge/status-ativo-blue) ![Version](https://img.shields.io/badge/version-2.0-purple)

**Selo** é um gerenciador de senhas moderno, seguro e local. Desenvolvido em Java com Spring Boot, ele oferece uma interface web premium com criptografia militar para proteger suas credenciais.

---

## ✨ Funcionalidades

* **🔒 Criptografia de Ponta:** Utiliza **AES-256** para criptografar suas senhas e **SHA-512** com Salt para proteger sua senha mestra.
* **🌐 Interface Premium:** Design moderno "Glassmorphism" com tema escuro, responsivo e intuitivo.
* **⚡ Gerador de Senhas:** Crie senhas inquebráveis de até 256 caracteres com um clique.
* **📂 Organização:** Barra lateral intuitiva para navegação entre seu Cofre e ferramentas.
* **🚀 Execução Silenciosa:** Roda em segundo plano no seu computador, acessível via navegador.
* **📋 Área de Transferência Inteligente:** Copie senhas sem revelá-las na tela.

---

## 🛠️ Tecnologias

* **Backend:** Java 21, Spring Boot 3.x
* **Frontend:** HTML5, CSS3 (Glassmorphism), JavaScript (Vanilla), Thymeleaf.
* **Segurança:** Java Cryptography Architecture (JCA), PBKDF2, AES/CBC/PKCS5Padding.
* **Build:** Maven.

---

## 🚀 Como Executar

### Pré-requisitos
* Java 21+ instalado.
* Maven instalado.

### Execução Rápida (Mac/Linux)
Utilize o script automatizado na raiz do projeto:

```bash
./run_app.command
```
Ou, se você criou o atalho:
1. Clique duas vezes em **PasswordManager.command** na sua Área de Trabalho.
(O nome do arquivo do atalho pode ser atualizado para `Selo.command` se desejar).

Acesse em seu navegador: `http://localhost:8080`

Para encerrar o servidor, execute o `PararApp.command`.

---

## 👤 Autor

**Oliver Arthur Souza Pinheiro**

---

## 📜 Licença

Este projeto está sob a licença MIT.