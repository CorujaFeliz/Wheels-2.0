package bikeshop.servicos;

import bikeshop.dao.AppDAO;
import bikeshop.model.Token;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Serviço responsável pela criação, persistência e validação de Tokens.
 * Agora, usa apenas AppDAO como dependência.
 */
public class TokenServicos {

    private final AppDAO dao;
    private final int ttlMinutes;

    /**
     * @param dao        DAO unificado para persistência
     * @param ttlMinutes Tempo de vida do token em minutos
     */
    public TokenServicos(AppDAO dao, int ttlMinutes) {
        this.dao        = Objects.requireNonNull(dao, "dao não pode ser nulo");
        this.ttlMinutes = ttlMinutes;
    }

    /**
     * Cria um token para o cliente, persiste no banco e retorna o objeto.
     * @param customerId ID do cliente
     * @return token recém-gerado
     */
    public Token createTokenForCustomer(int customerId) {
        Token token = new Token(ttlMinutes);
        dao.insertToken(customerId, token);
        return token;
    }

    /**
     * Valida e consome o token dado, marcando-o como usado.
     * @param tokenValue valor do token
     * @return true se válido e consumido; false caso contrário
     */
    public boolean validateAndConsume(String tokenValue) {
        Token token = dao.findValidToken(tokenValue);
        if (token == null || token.isExpired()) {
            return false;
        }
        dao.markTokenUsed(tokenValue);
        return true;
    }

    /**
     * Retorna o token válido sem consumi-lo.
     * @param tokenValue valor do token
     * @return Token se válido; null caso contrário
     */
    public Token getValidToken(String tokenValue) {
        Token token = dao.findValidToken(tokenValue);
        if (token == null || token.isExpired()) {
            return null;
        }
        return token;
    }

    /**
     * Expira tokens antigos (utilitário para limpeza).
     * @return número de tokens expirados removidos
     */
    public int expireTokens() {
        return dao.deleteExpiredTokens();
    }
}
