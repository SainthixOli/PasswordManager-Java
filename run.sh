#!/bin/bash

# --- Script para Compilar e Executar o PasswordManager-Java ---

# Mensagem inicial
echo "-------------------------------------------"
echo "Iniciando o processo de build e execução..."
echo "-------------------------------------------"

# Passo 1: Limpar compilações antigas para garantir um build limpo.
echo "[PASSO 1/3] Limpando compilações antigas (pasta bin)..."
rm -rf bin
mkdir bin
echo "Pasta 'bin' recriada."
echo ""

# Passo 2: Compilar todos os arquivos .java.
# O classpath (-cp) aponta para a pasta 'lib' onde os JARs do Jackson estão.
# Os arquivos compilados (.class) serão colocados na pasta 'bin'.
echo "[PASSO 2/3] Compilando o código-fonte Java..."
javac -d bin -cp "lib/*" $(find src -name "*.java")

# Passo 2.1: Verificar se a compilação foi bem-sucedida.
# Se o último comando (javac) retornou um código de erro (diferente de 0),
# a compilação falhou. O script vai parar aqui.
if [ $? -ne 0 ]; then
    echo ""
    echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    echo "ERRO: A compilação falhou. Verifique as mensagens de erro acima."
    echo "O programa não será executado."
    echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    exit 1 # Sai do script com um código de erro.
fi

echo "Compilação concluída com sucesso!"
echo ""

# Passo 3: Executar o programa.
# O classpath (-cp) agora inclui a pasta 'bin' (nosso código) e a pasta 'lib' (bibliotecas).
echo "[PASSO 3/3] Executando o programa..."
echo "-------------------------------------------"
java -cp "bin:lib/*" main.Main

echo "-------------------------------------------"
echo "Programa finalizado."