package bikeshop.model;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

public class Token {
    private final String tokenValue;
    private final LocalDateTime expiration;
    private final boolean isUsed;

    public  Token(int minutesToExpire){
        this.tokenValue =generateSecureToken();
        this.expiration = LocalDateTime.now().plusMinutes(minutesToExpire);
        this.isUsed = false;
    }
    private String generateSecureToken(){
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    public String getTokenValue(){
        return tokenValue;
    }
    public boolean isExpired(){
        return LocalDateTime.now().isAfter(expiration);
    }
    public LocalDateTime getExpiration(){
        return expiration;
    }
    public boolean isUsed(){
        return isUsed;
    }
    @Override
    public String toString(){
        return "Token{" +
                "tokenValue='"+ tokenValue+'\''+
                ", expiration=" +expiration+
                ", used=" +isUsed+
                "}";
    }
}
