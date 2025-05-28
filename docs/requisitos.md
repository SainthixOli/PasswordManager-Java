# 📋 Requisitos do Projeto: Password Manager em Java

## 1. Requisitos Funcionais (RF)

### RF01 - Tela de Login
- O sistema deve permitir o acesso apenas a usuários autenticados.
- Deve existir um mecanismo de cadastro de ID de conta e senha principal.
- A senha de login deve ser criptografada.

### RF02 - Geração de Senhas Seguras
- O sistema deve gerar senhas seguras de forma aleatória.
- As senhas devem conter uma mistura de letras maiúsculas, minúsculas, números e caracteres especiais.
- O gerador deve evitar a repetição frequente de padrões semelhantes.

### RF03 - Armazenamento Local das Senhas
- O sistema deve armazenar todas as senhas criadas localmente no computador do usuário.
- Cada senha deve estar associada a:
  - Um identificador (nome da plataforma/serviço)
  - A senha gerada
  - A data de criação

### RF04 - Gerenciador de Senhas
- O sistema deve exibir ao usuário uma lista com:
  - Todas as senhas geradas
  - Local/plataforma de uso
  - Data de criação
- Deve haver uma contagem de quantas senhas foram criadas.

### RF05 - Criptografia das Senhas
- Todas as senhas armazenadas devem ser criptografadas localmente.
- Deve haver um mecanismo de criptografia simétrica para proteger os dados.

---

## 2. Requisitos Não Funcionais (RNF)

### RNF01 - Plataforma
- O sistema deve ser desenvolvido 100% em Java.

### RNF02 - Armazenamento Local
- Nenhuma senha será armazenada em servidores ou nuvem.
- Os dados serão salvos em arquivos locais (ex: `.dat`, `.json` ou `.txt` criptografados).

### RNF03 - Interface Simples
- A interface poderá ser via console inicialmente.
- A futura implementação gráfica poderá utilizar JavaFX (opcional).

### RNF04 - Licença
- O sistema deve ser de código aberto, utilizando a licença MIT.

---

## 3. Regras de Negócio (RN)

### RN01 - Acesso Seguro
- O sistema só pode ser acessado com login válido.
- O usuário é responsável por guardar sua senha principal.

### RN02 - Proteção de Dados
- Senhas só podem ser visualizadas mediante autenticação.
- As senhas jamais devem ser exibidas em texto plano no código-fonte ou nos arquivos.

---

## 4. Restrições

- ✅ O projeto será desenvolvido em Java puro, sem frameworks externos (Spring, etc.).
- ✅ O armazenamento será feito localmente, sem uso de banco de dados externo.
- ✅ As bibliotecas utilizadas devem ser nativas do Java SE.

---

> ⚠️ **Observação:** Este documento pode evoluir com o avanço do projeto. Novos requisitos podem ser adicionados ou refinados conforme necessário.