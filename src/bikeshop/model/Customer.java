package bikeshop.model;

/**
 * Modelo para entidade Customer.
 */
public class Customer {

    private int id;
    private String nome;
    private String postcode;
    private String email;
    private String telefone;
    private String senhaHash;
    private Boolean emailVerificado;

    /**
     * Construtor padrão (necessário para frameworks e reidratação via DAO).
     */
    public Customer() {
    }

    /**
     * Criar novo Customer sem ID (antes de persistir).
     */
    public Customer(String nome,
                    String postcode,
                    String email,
                    String telefone,
                    String senhaHash,
                    Boolean emailVerificado) {
        this.nome = nome;
        this.postcode = postcode;
        this.email = email;
        this.telefone = telefone;
        this.senhaHash = senhaHash;
        this.emailVerificado = emailVerificado;
    }

    /**
     * Reidratar Customer com ID (após persistência).
     */
    public Customer(int id,
                    String nome,
                    String postcode,
                    String email,
                    String telefone,
                    String senhaHash,
                    Boolean emailVerificado) {
        this.id = id;
        this.nome = nome;
        this.postcode = postcode;
        this.email = email;
        this.telefone = telefone;
        this.senhaHash = senhaHash;
        this.emailVerificado = emailVerificado;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public Boolean isEmailVerificado() {
        return emailVerificado;
    }

    public void setEmailVerificado(Boolean emailVerificado) {
        this.emailVerificado = emailVerificado;
    }
}
