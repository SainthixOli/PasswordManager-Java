package util; 

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class Criptografia { 

    private static final String ALGORITMO_CRIPTOGRAFIA = "AES/CBC/PKCS5Padding";
    private static final String ALGORITMO_CHAVE = "AES";
    private static final int TAMANHO_IV_BYTES = 16;

    
    private static final String CHAVE_MESTRA_FIXA_STRING = "MinhaChave123456"; 

    private static SecretKey getChaveSecreta() {
        byte[] chaveBytes = CHAVE_MESTRA_FIXA_STRING.getBytes(StandardCharsets.UTF_8);
        // Validação do tamanho da chave para AES
        if (chaveBytes.length != 16 && chaveBytes.length != 24 && chaveBytes.length != 32) {
            
            throw new IllegalArgumentException("A chave de criptografia DEVE ter 16, 24 ou 32 bytes.");
        }
        return new SecretKeySpec(chaveBytes, ALGORITMO_CHAVE);
    }

    /**
     * Cifra os dados fornecidos usando AES.
     * O IV (Vetor de Inicialização) é gerado aleatoriamente e prefixado ao texto cifrado.
     *
     * @param dadosEmTextoPlano A string a ser cifrada.
     * @return Uma string Base64 contendo o IV + dados cifrados.
     * @throws RuntimeException Se ocorrer um erro durante a cifragem.
     */
    public static String cifrar(String dadosEmTextoPlano) {
        try {
            SecretKey chaveSecreta = getChaveSecreta();
            Cipher cifrador = Cipher.getInstance(ALGORITMO_CRIPTOGRAFIA);

            byte[] iv = new byte[TAMANHO_IV_BYTES];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            cifrador.init(Cipher.ENCRYPT_MODE, chaveSecreta, ivParameterSpec);
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
     * Decifra os dados fornecidos (Base64, contendo IV + dados cifrados).
     *
     * @param dadosCifradosComIvBase64 A string Base64 a ser decifrada.
     * @return A string original em texto plano.
     * @throws RuntimeException Se ocorrer um erro durante a decifragem.
     */
    public static String decifrar(String dadosCifradosComIvBase64) {
        try {
            SecretKey chaveSecreta = getChaveSecreta();
            byte[] ivMaisDadosCifrados = Base64.getDecoder().decode(dadosCifradosComIvBase64);

            byte[] iv = new byte[TAMANHO_IV_BYTES];
            System.arraycopy(ivMaisDadosCifrados, 0, iv, 0, iv.length);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

            int tamanhoDadosCifrados = ivMaisDadosCifrados.length - TAMANHO_IV_BYTES;
            byte[] dadosCifradosBytes = new byte[tamanhoDadosCifrados];
            System.arraycopy(ivMaisDadosCifrados, TAMANHO_IV_BYTES, dadosCifradosBytes, 0, tamanhoDadosCifrados);

            Cipher cifrador = Cipher.getInstance(ALGORITMO_CRIPTOGRAFIA);
            cifrador.init(Cipher.DECRYPT_MODE, chaveSecreta, ivParameterSpec);
            byte[] dadosDecifradosBytes = cifrador.doFinal(dadosCifradosBytes);

            return new String(dadosDecifradosBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("UTILS.CRIPTOGRAFIA: Erro ao decifrar dados: " + e.getMessage());
            throw new RuntimeException("Erro durante a decifragem.", e);
        }
    }

    // Construtor privado para impedir instanciação de classe utilitária
    private Criptografia() {}
}
