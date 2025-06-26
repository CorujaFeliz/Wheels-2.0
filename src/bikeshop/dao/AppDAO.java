package bikeshop.dao;

import bikeshop.db.DataConnection;
import bikeshop.logic.security.SegurancaCripto;
import bikeshop.model.Bike;
import bikeshop.model.Customer;
import bikeshop.model.Hire;
import bikeshop.model.Token;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO unificado: todas as operações de persistência em um só lugar.
 */
public class AppDAO {

    private final SegurancaCripto cripto;

    public AppDAO(SegurancaCripto cripto) {
        this.cripto = cripto;
    }

    // ------- BIKE -------

    public List<Bike> findAvailableBikes() {
        String sql = "SELECT bike_number, modelo, disponivel, deposit, rate FROM bikes WHERE disponivel = true";
        List<Bike> list = new ArrayList<>();
        try (Connection c = DataConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql);
             ResultSet rs = s.executeQuery()) {
            while (rs.next()) {
                list.add(new Bike(
                        rs.getInt("bike_number"),
                        rs.getString("modelo"),
                        rs.getBoolean("disponivel"),
                        rs.getInt("deposit"),
                        rs.getInt("rate")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar bikes: " + e.getMessage());
        }
        return list;
    }

    public int countAvailableBikes() {
        String sql = "SELECT COUNT(*) FROM bikes WHERE disponivel = true";
        try (Connection c = DataConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql);
             ResultSet rs = s.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("Erro ao contar bikes: " + e.getMessage());
        }
        return 0;
    }

    public boolean markBikeRented(int bikeNumber) {
        String sql = "UPDATE bikes SET disponivel = false WHERE bike_number = ?";
        try (Connection c = DataConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, bikeNumber);
            return s.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao alugar bike: " + e.getMessage());
            return false;
        }
    }

    public boolean markBikeReturned(int bikeNumber) {
        String sql = "UPDATE bikes SET disponivel = true WHERE bike_number = ?";
        try (Connection c = DataConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, bikeNumber);
            return s.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao retornar bike: " + e.getMessage());
            return false;
        }
    }

    public Bike findBikeByNumber(int bikeNumber) {
        String sql = "SELECT bike_number, modelo, disponivel, deposit, rate FROM bikes WHERE bike_number = ?";
        try (Connection c = DataConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, bikeNumber);
            try (ResultSet rs = s.executeQuery()) {
                if (rs.next()) {
                    return new Bike(
                            rs.getInt("bike_number"),
                            rs.getString("modelo"),
                            rs.getBoolean("disponivel"),
                            rs.getInt("deposit"),
                            rs.getInt("rate")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro em findBikeByNumber: " + e.getMessage());
        }
        return null;
    }

    // ------- CUSTOMER -------

    public void insertCustomer(Customer customer) {
        String sql = "INSERT INTO customer (nome, postcode, email, telefone, senha, email_verificado) VALUES (?, ?, ?, ?, ?, ?)";
        try (var conn = DataConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, customer.getNome());
            stmt.setString(2, customer.getPostcode());
            stmt.setString(3, customer.getEmail());
            stmt.setString(4, customer.getTelefone());
            stmt.setString(5, customer.getSenhaHash());
            stmt.setBoolean(6, customer.isEmailVerificado());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao inserir cliente: " + e.getMessage());
        }
    }

    public Customer findCustomerByEmail(String email) {
        String sql = "SELECT id, nome, postcode, email, telefone, senha, email_verificado FROM customer WHERE email = ?";
        try (var conn = DataConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("postcode"),
                            rs.getString("email"),
                            rs.getString("telefone"),
                            rs.getString("senha"),
                            rs.getBoolean("email_verificado")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro em findCustomerByEmail: " + e.getMessage());
        }
        return null;
    }

    public Customer findCustomerById(int id) {
        String sql = "SELECT id, nome, postcode, email, telefone, senha, email_verificado FROM customer WHERE id = ?";
        try (var conn = DataConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("postcode"),
                            rs.getString("email"),
                            rs.getString("telefone"),
                            rs.getString("senha"),
                            rs.getBoolean("email_verificado")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro em findCustomerById: " + e.getMessage());
        }
        return null;
    }

    public Customer loginCustomer(String email, String senhaPlain) {
        Customer c = findCustomerByEmail(email);
        if (c == null) {
            System.out.println("Cliente não encontrado.");
            return null;
        }
        if (!cripto.verificar(senhaPlain, c.getSenhaHash())) {
            System.out.println("Senha inválida.");
            return null;
        }
        if (!c.isEmailVerificado()) {
            System.out.println("Email ainda não verificado.");
            return null;
        }
        return c;
    }

    public boolean updateCustomerEmailVerified(int customerId, boolean verified) {
        String sql = "UPDATE customer SET email_verificado = ? WHERE id = ?";
        try (var conn = DataConnection.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, verified);
            stmt.setInt(2, customerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro em updateCustomerEmailVerified: " + e.getMessage());
        }
        return false;
    }

    // ------- HIRE / RENTAL -------

    public Hire insertHire(int customerId, int bikeId, LocalDate startDate, int numberOfDays) {
        String sql = "INSERT INTO rentals (customer_id, bike_id, start_date, number_of_days) VALUES (?, ?, ?, ?)";
        try (Connection c = DataConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            s.setInt(1, customerId);
            s.setInt(2, bikeId);
            s.setDate(3, Date.valueOf(startDate));
            s.setInt(4, numberOfDays);
            s.executeUpdate();

            try (ResultSet keys = s.getGeneratedKeys()) {
                if (keys.next()) {
                    int hireId = keys.getInt(1);
                    Bike bike = findBikeByNumber(bikeId);
                    Customer cust = findCustomerById(customerId);
                    return new Hire(hireId, startDate, numberOfDays, bike, cust);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao registrar aluguel: " + e.getMessage());
        }
        return null;
    }

    public Hire findHireById(int hireId) {
        String sql = "SELECT customer_id, bike_id, start_date, number_of_days, end_date FROM rentals WHERE id = ?";
        try (Connection c = DataConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, hireId);
            try (ResultSet rs = s.executeQuery()) {
                if (rs.next()) {
                    LocalDate start = rs.getDate("start_date").toLocalDate();
                    int days = rs.getInt("number_of_days");
                    int bikeId = rs.getInt("bike_id");
                    int custId = rs.getInt("customer_id");
                    Date rawEndDate = rs.getDate("end_date");
                    LocalDate endDate = rawEndDate != null ? rawEndDate.toLocalDate() : null;

                    Bike bike = findBikeByNumber(bikeId);
                    Customer cust = findCustomerById(custId);
                    Hire hire = new Hire(hireId, start, days, bike, cust);
                    if (endDate != null) hire.setEndDate(endDate);
                    return hire;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar aluguel: " + e.getMessage());
        }
        return null;
    }

    public boolean endHire(int hireId) {
        String sql = "UPDATE rentals SET end_date = NOW() WHERE id = ?";
        try (Connection c = DataConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, hireId);
            return s.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao encerrar aluguel: " + e.getMessage());
            return false;
        }
    }

    public int deleteOldCompletedHires(int daysThreshold) {
        String sql = "DELETE FROM rentals WHERE end_date IS NOT NULL AND end_date < DATE_SUB(NOW(), INTERVAL ? DAY)";
        try (Connection c = DataConnection.getConnection();
             PreparedStatement s = c.prepareStatement(sql)) {
            s.setInt(1, daysThreshold);
            return s.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao limpar alugueis antigos: " + e.getMessage());
            return 0;
        }
    }

    // ------- TOKEN -------

    public void insertToken(int customerId, Token token) {
        String sql = "INSERT INTO tokens (customer_id, token_value, expiration, used) VALUES (?, ?, ?, ?)";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.setString(2, token.getTokenValue());
            stmt.setTimestamp(3, Timestamp.valueOf(token.getExpiration()));
            stmt.setBoolean(4, token.isUsed());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao inserir token: " + e.getMessage());
        }
    }

    public Token findValidToken(String tokenValue) {
        String sql = "SELECT token_value, expiration, used FROM tokens WHERE token_value = ? AND used = false AND expiration >= ?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tokenValue);
            stmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String value = rs.getString("token_value");
                    LocalDateTime exp = rs.getTimestamp("expiration").toLocalDateTime();
                    boolean used = rs.getBoolean("used");
                    return new Token(value, exp, used);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar token válido: " + e.getMessage());
        }
        return null;
    }

    public void markTokenUsed(String tokenValue) {
        String sql = "UPDATE tokens SET used = true WHERE token_value = ?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, tokenValue);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao marcar token como usado: " + e.getMessage());
        }
    }

    public int deleteExpiredTokens() {
        String sql = "DELETE FROM tokens WHERE expiration < ?";
        try (Connection conn = DataConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            return stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Erro ao deletar tokens expirados: " + e.getMessage());
        }
        return 0;
    }
}
