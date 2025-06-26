package bikeshop.model;

/**
 * Modelo que representa uma bicicleta disponível para aluguel.
 */
public class Bike {

    private int bikeNumber;     // identificador único da bike
    private String modelo;      // nome ou modelo da bike
    private boolean disponivel; // true se estiver disponível para aluguel
    private int deposit;        // valor de caução
    private int rate;           // valor da diária

    /**
     * Construtor padrão para frameworks/DAO.
     */
    public Bike() {}

    /**
     * Construtor completo.
     *
     * @param bikeNumber identificador único da bike
     * @param modelo     modelo ou nome da bike
     * @param disponivel disponibilidade (true = livre para alugar)
     * @param deposit    valor de caução
     * @param rate       valor da diária
     */
    public Bike(int bikeNumber,
                String modelo,
                boolean disponivel,
                int deposit,
                int rate) {
        this.bikeNumber = bikeNumber;
        this.modelo     = modelo;
        this.disponivel = disponivel;
        this.deposit    = deposit;
        this.rate       = rate;
    }

    // Getters e Setters

    public int getBikeNumber() {
        return bikeNumber;
    }

    public void setBikeNumber(int bikeNumber) {
        this.bikeNumber = bikeNumber;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public boolean isDisponivel() {
        return disponivel;
    }

    public void setDisponivel(boolean disponivel) {
        this.disponivel = disponivel;
    }

    public int getDeposit() {
        return deposit;
    }

    public void setDeposit(int deposit) {
        this.deposit = deposit;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    /**
     * Calcula o custo total com base no número de dias.
     *
     * @param numberOfDays quantidade de dias de aluguel
     * @return custo total = deposit + rate * numberOfDays
     */
    public int calculateCost(int numberOfDays) {
        return deposit + (rate * numberOfDays);
    }
}
