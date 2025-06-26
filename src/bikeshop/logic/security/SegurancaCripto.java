package bikeshop.logic.security;

import org.mindrot.jbcrypt.BCrypt;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

public class  SegurancaCripto {

    // Gera o hash de uma string (senha, token, etc.)
    public String hash(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

    // Verifica se o texto corresponde ao hash armazenado
    public boolean verificar(String plainText, String hashed) {
        return BCrypt.checkpw(plainText, hashed);
    }
    //Criptografia
    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String AES_KEY_TYPE = "AES";
    private static final int IV_LENGTH = 32;

    private static final SecretKey secretKey;

    // Chave vinda do ambiente, ou definida de forma segura
    static {
        String keyEnv = System.getenv("AES_SECRET_KEY");

        if (keyEnv == null || keyEnv.length() < 32) {
            throw new RuntimeException("A chave AES deve ter no mínimo 16 caracteres e ser configurada como variável de ambiente AES_SECRET_KEY.");
        }

        byte[] keyBytes = keyEnv.getBytes(StandardCharsets.UTF_8);
        byte[] finalKey = new byte[32]; // AES 128 bits
        System.arraycopy(keyBytes, 0, finalKey, 0, Math.min(keyBytes.length, finalKey.length));
        secretKey = new SecretKeySpec(finalKey, AES_KEY_TYPE);
    }

    public static String criptografar(String texto) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);

            // Gerar IV aleatório
            byte[] iv = new byte[IV_LENGTH];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);

            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encrypted = cipher.doFinal(texto.getBytes(StandardCharsets.UTF_8));

            // Concatenar IV + criptografado
            byte[] ivAndEncrypted = new byte[IV_LENGTH + encrypted.length];
            System.arraycopy(iv, 0, ivAndEncrypted, 0, IV_LENGTH);
            System.arraycopy(encrypted, 0, ivAndEncrypted, IV_LENGTH, encrypted.length);

            return Base64.getEncoder().encodeToString(ivAndEncrypted);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criptografar: " + e.getMessage(), e);
        }
    }

    public static String descriptografar(String criptografado) {
        try {
            byte[] ivAndEncrypted = Base64.getDecoder().decode(criptografado);

            byte[] iv = new byte[IV_LENGTH];
            byte[] encrypted = new byte[ivAndEncrypted.length - IV_LENGTH];

            System.arraycopy(ivAndEncrypted, 0, iv, 0, IV_LENGTH);
            System.arraycopy(ivAndEncrypted, IV_LENGTH, encrypted, 0, encrypted.length);

            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

            byte[] decrypted = cipher.doFinal(encrypted);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao descriptografar: " + e.getMessage(), e);
        }
    }
}
