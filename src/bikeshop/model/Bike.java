package bikeshop.model;

import java.util.ArrayList;
import java.util.List;

public class Bike {

    private static final List<Bike> bikeList = new ArrayList<>();

    private int deposit;
    private int rate;
    private int bikeNumber;

    // Bloco de inicialização estático
    static {
        for (int i = 0; i < 5; i++) {
            bikeList.add(new Bike(10 + i, i, (i + 1) * 100));
        }
    }

    public Bike(int deposit, int rate, int bikeNumber) {
        this.deposit = deposit;
        this.rate = rate;
        this.bikeNumber = bikeNumber;
    }

    public int getDeposit() {
        return deposit;
    }

    public int getRate() {
        return rate;
    }

    public int getBikeNumber() {
        return bikeNumber;
    }

    public static Bike findBikeByNumber(int bikeNum) {
        return bikeList.stream()
                .filter(bike -> bike.getBikeNumber() == bikeNum)
                .findFirst()
                .orElse(null);
    }

    public void showDetails() {
        System.out.printf("Details for bike number %d%n", bikeNumber);
        System.out.printf("DEPOSIT: %d%n", deposit);
        System.out.printf("RATE: %d%n", rate);
    }

    public int calculateCost(int numberOfDays) {
        return deposit + (rate * numberOfDays);
    }

    //Métdo auxiliar para debug/testes
    public static void listAllBikes() {
        bikeList.forEach(Bike::showDetails);
    }
}
