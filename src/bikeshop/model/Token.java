package bikeshop.model;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

public class Token {

    private final String tokenValue;
    private final LocalDateTime expiration;
    private boolean used;

    /** Construtor para gerar um token novo com TTL em minutos */
    public Token(int minutesToExpire) {
        this.tokenValue = generateSecureToken();
        this.expiration = LocalDateTime.now().plusMinutes(minutesToExpire);
        this.used       = false;
    }

    /** Construtor para reidratar do banco (tokenValue, expiration, used) */
    public Token(String tokenValue, LocalDateTime expiration, boolean used) {
        this.tokenValue = tokenValue;
        this.expiration = expiration;
        this.used       = used;
    }

    private String generateSecureToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
    public static Token generateForCustomer(int customerId) {
        // O customerId é só para manter assinatura; não é necessário para gerar o valor do token
        // Se quiser customizar o TTL, altere o valor abaixo (exemplo: 15 minutos)
        int TTL_MINUTES = 15;
        return new Token(TTL_MINUTES);
    }
    public String getTokenValue() {
        return tokenValue;
    }

    public LocalDateTime getExpiration() {
        return expiration;
    }

    /** Getter necessário para TokenDAO.marcarUsed() */
    public boolean isUsed() {
        return used;
    }

    /** Verifica se já ultrapassou o expiration */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiration);
    }

    /** Marca o token como consumido (used = true) */
    public void markAsUsed() {
        this.used = true;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenValue='" + tokenValue + '\'' +
                ", expiration=" + expiration +
                ", used=" + used +
                '}';
    }
}
