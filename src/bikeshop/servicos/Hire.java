package bikeshop.servicos;

import bikeshop.model.Bike;
import bikeshop.model.Customer;

import java.time.LocalDate;

public class Hire {

    private static int hireCount = 1;

    private final int hireId;
    private final LocalDate startDate;
    private final int numberOfDays;
    private final Bike bike;
    private final Customer customer;

    public Hire(LocalDate startDate, int numberOfDays, Bike bike, Customer customer) {
        this.hireId = hireCount++;
        this.startDate = startDate;
        this.numberOfDays = numberOfDays;
        this.bike = bike;
        this.customer = customer;
    }

    public int getHireId() {
        return hireId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public Bike getBike() {
        return bike;
    }

    public Customer getCustomer() {
        return customer;
    }

    public int calculateTotalCost() {
        return bike.calculateCost(numberOfDays);
    }

    public void printSummary() {
        System.out.printf("Hire #%d | Bike #%d rented by %s for %d day(s) starting on %s%n",
                hireId, bike.getBikeNumber(), customer.getNome(), numberOfDays, startDate);
        System.out.printf("Total cost: %d%n", calculateTotalCost());
    }
}
