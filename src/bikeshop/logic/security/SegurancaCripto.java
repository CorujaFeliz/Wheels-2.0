package bikeshop.logic.security;

import org.mindrot.jbcrypt.BCrypt;

public class  SegurancaCripto {

    // Gera o hash de uma string (senha, token, etc.)
    public String hash(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt());
    }

    // Verifica se o texto corresponde ao hash armazenado
    public boolean verificar(String plainText, String hashed) {
        return BCrypt.checkpw(plainText, hashed);
    }
}
