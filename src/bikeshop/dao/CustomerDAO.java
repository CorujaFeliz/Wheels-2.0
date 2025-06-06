package bikeshop.dao;

import bikeshop.model.Customer;
import bikeshop.db.DataConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class CustomerDAO {
    public void insert(Customer customer){
        String sql = "INSERT INTO Customer (nome,postcode,telefone) VALUES(?,?,?)";
        try(Connection conn= DataConnection.getConnetion();
         PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1,customer.getNome());
            stmt.setString(2,customer.getPostcode());
            stmt.setInt(3,customer.getTelefone());

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
                        rs.getInt("telefone"));
                customers.add(customer);

            }
        }catch (SQLException e) {
            System.out.println("Erro ao buscar clientes: " + e.getMessage());
        }
        return customers;
    }

}
