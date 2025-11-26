#!/bin/bash

# --- Script para Compilar e Executar o PasswordManager-Java (Maven) ---

echo "-------------------------------------------"
echo "Iniciando o processo de build e execução com Maven..."
echo "-------------------------------------------"

# Verifica se o Maven está instalado
if ! command -v mvn &> /dev/null
then
    echo "Maven não encontrado. Por favor, instale o Maven para continuar."
    exit 1
fi

# Limpa e executa a aplicação usando o plugin do JavaFX
echo "[MAVEN] Executando clean javafx:run..."
mvn clean javafx:run

if [ $? -ne 0 ]; then
    echo ""
    echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    echo "ERRO: A execução falhou."
    echo "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"
    exit 1
fi

echo "-------------------------------------------"
echo "Programa finalizado."