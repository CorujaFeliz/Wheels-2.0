package bikeshop.model;

public class Customer {
    private int id;
    private String nome;
    private String postcode;
    private int telefone;

    public Customer(String nome, String postcode, int telefone) {
        this.nome = nome;
        this.postcode = postcode;
        this.telefone = telefone;
    }

    public Customer(int id, String nome, String postcode, int telefone) {
        this.id = id;
        this.nome = nome;
        this.postcode = postcode;
        this.telefone = telefone;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getPostcode() { return postcode; }
    public int getTelefone() { return telefone; }

    public void setId(int id) { this.id = id; }
}