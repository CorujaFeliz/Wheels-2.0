package bikeshop.dao;

import bikeshop.model.Customer;
import bikeshop.db.DataConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CustomerDAO {
    public void insert(Customer customer){
        String sql = "INSERT INTO Customer (nome,postcode,email,telefone) VALUES(?,?,?,?)";
        try(Connection conn= DataConnection.getConnetion();
         PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1,customer.getNome());
            stmt.setString(2,customer.getPostcode());
            stmt.setString(3,customer.getEmail());
            stmt.setString(4,customer.getTelefone());

            stmt.executeUpdate();
            System.out.println("Client inserido com sucesso");

        }catch (SQLException e) {
            System.out.println("Erro ao inserir cliente: " + e.getMessage());
        }
}
    public List<Customer> Achou(){
        List<Customer> customers = new ArrayList<>();
        String sql= "SELECT * FROM Customer";
        try(Connection conn = DataConnection.getConnetion();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)){
            while(rs.next()){
                Customer customer = new Customer(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("postcode"),
                        rs.getString("email"),
                        rs.getString("telefone"));
                customers.add(customer);

            }
        }catch (SQLException e) {
            System.out.println("Erro ao buscar clientes: " + e.getMessage());
        }
        return customers;
    }

    public Customer login(String email, String senha){
        String sql= "SELECT * FROM customers WHERE email = ?";
        try (Connection conn = DataConnection.getConnetion();
        PreparedStatement stmt = conn.prepareStatement(sql)){
        stmt.setString(1,email);
        ResultSet rs = stmt.executeQuery();

            if (rs.next()){
            String senhaHash = rs.getString("senha");
            boolean emailVerificado = rs.getBoolean("emailVerificado");
                if (senha.equals(senhaHash)){
                    if(!emailVerificado){
                        System.out.println("Email ainda não verificado.");
                        return null;
                    }
                    return new Customer(rs.getString("nome")
                    ,rs.getString("postcode"),email, rs.getString("telefone"),senhaHash,true);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
return null;
}
}
