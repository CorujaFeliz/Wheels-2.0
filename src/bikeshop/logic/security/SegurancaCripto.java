package bikeshop.logic.security;

import org.mindrot.jbcrypt.BCrypt;

public class  SegurancaCripto {

    // Gera o hash de uma string (senha, token, etc.)
    public static String hash(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

    // Verifica se o texto corresponde ao hash armazenado
    public static boolean verify(String plainText, String hashedText) {
        return BCrypt.checkpw(plainText, hashedText);
    }
}
