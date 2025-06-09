package bikeshop.model;

public class Customer {
    private int id;
    private String nome;
    private String postcode;
    private String email;
    private String telefone;

    public Customer(String nome, String postcode,String email, String telefone) {
        this.nome = nome;
        this.postcode = postcode;
        this.email = email;
        this.telefone = telefone;
    }

    public Customer(int id, String nome, String postcode,String email, String telefone) {
        this.id = id;
        this.nome = nome;
        this.postcode = postcode;
        this.email = email;
        this.telefone = telefone;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getPostcode() { return postcode; }
    public String getEmail(){return email;}
    public String getTelefone() { return telefone; }

    public void setId(int id) { this.id = id; }
}