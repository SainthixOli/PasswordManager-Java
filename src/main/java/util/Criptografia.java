package util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Criptografia {

    private static final String ALGORITMO_CRIPTOGRAFIA = "AES/CBC/PKCS5Padding";
    private static final String ALGORITMO_CHAVE = "AES";
    private static final String ALGORITMO_DERIVACAO = "PBKDF2WithHmacSHA256";
    private static final int TAMANHO_IV_BYTES = 16;
    private static final int ITERACOES = 65536;
    private static final int TAMANHO_CHAVE_BITS = 256;

    /**
     * Deriva uma chave secreta a partir da senha e do sal.
     * 
     * @param senha A senha mestra do usuário.
     * @param sal   O sal do usuário (deve ser o mesmo usado para o hash da senha ou
     *              um específico para criptografia).
     * @return A chave secreta AES.
     */
    public static SecretKey derivarChave(String senha, byte[] sal) {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITMO_DERIVACAO);
            KeySpec spec = new PBEKeySpec(senha.toCharArray(), sal, ITERACOES, TAMANHO_CHAVE_BITS);
            SecretKey tmp = factory.generateSecret(spec);
            return new SecretKeySpec(tmp.getEncoded(), ALGORITMO_CHAVE);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao derivar chave: " + e.getMessage(), e);
        }
    }

    /**
     * Cifra os dados fornecidos usando AES e a chave fornecida.
     *
     * @param dadosEmTextoPlano A string a ser cifrada.
     * @param chave             A chave secreta derivada.
     * @return Uma string Base64 contendo o IV + dados cifrados.
     */
    public static String cifrar(String dadosEmTextoPlano, SecretKey chave) {
        try {
            Cipher cifrador = Cipher.getInstance(ALGORITMO_CRIPTOGRAFIA);

            byte[] iv = new byte[TAMANHO_IV_BYTES];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            cifrador.init(Cipher.ENCRYPT_MODE, chave, ivParameterSpec);
            byte[] dadosCifradosBytes = cifrador.doFinal(dadosEmTextoPlano.getBytes(StandardCharsets.UTF_8));

            byte[] ivMaisDadosCifrados = new byte[iv.length + dadosCifradosBytes.length];
            System.arraycopy(iv, 0, ivMaisDadosCifrados, 0, iv.length);
            System.arraycopy(dadosCifradosBytes, 0, ivMaisDadosCifrados, iv.length, dadosCifradosBytes.length);

            return Base64.getEncoder().encodeToString(ivMaisDadosCifrados);
        } catch (Exception e) {
            System.err.println("UTILS.CRIPTOGRAFIA: Erro ao cifrar dados: " + e.getMessage());
            throw new RuntimeException("Erro durante a cifragem.", e);
        }
    }

    /**
     * Decifra os dados fornecidos usando a chave fornecida.
     *
     * @param dadosCifradosComIvBase64 A string Base64 a ser decifrada.
     * @param chave                    A chave secreta derivada.
     * @return A string original em texto plano.
     */
    public static String decifrar(String dadosCifradosComIvBase64, SecretKey chave) {
        try {
            byte[] ivMaisDadosCifrados = Base64.getDecoder().decode(dadosCifradosComIvBase64);

            byte[] iv = new byte[TAMANHO_IV_BYTES];
            System.arraycopy(ivMaisDadosCifrados, 0, iv, 0, iv.length);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            int tamanhoDadosCifrados = ivMaisDadosCifrados.length - TAMANHO_IV_BYTES;
            byte[] dadosCifradosBytes = new byte[tamanhoDadosCifrados];
            System.arraycopy(ivMaisDadosCifrados, TAMANHO_IV_BYTES, dadosCifradosBytes, 0, tamanhoDadosCifrados);

            Cipher cifrador = Cipher.getInstance(ALGORITMO_CRIPTOGRAFIA);
            cifrador.init(Cipher.DECRYPT_MODE, chave, ivParameterSpec);
            byte[] dadosDecifradosBytes = cifrador.doFinal(dadosCifradosBytes);

            return new String(dadosDecifradosBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("UTILS.CRIPTOGRAFIA: Erro ao decifrar dados: " + e.getMessage());
            throw new RuntimeException("Erro durante a decifragem (Senha incorreta ou dados corrompidos).", e);
        }
    }

    // Construtor privado para impedir instanciação de classe utilitária
    private Criptografia() {
    }
}
