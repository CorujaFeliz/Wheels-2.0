package bikeshop.ui;

import bikeshop.logic.Payment;
import bikeshop.model.Bike;
import bikeshop.model.Customer;
import bikeshop.model.Hire;

import java.time.LocalDate;

public class IssueBikeUI {
    private Bike chosenBike = null;
    private Customer customer = null;
    private Payment payment = null;
    private Hire hire = null;
    private int numberOfDays = 0;

    public void showBikeDetails(int bikeNum) {
        chosenBike = Bike.findBikeByNumber(bikeNum);
        if (chosenBike != null) {
            chosenBike.showDetails();
        } else {
            System.out.println("Bike not found.");
        }
    }

    public void calculateCost(int numDays) {
        this.numberOfDays = numDays;
        int cost = chosenBike.calculateCost(numDays);
        System.out.printf("Estimated cost for %d day(s): %d%n", numDays, cost);
    }

    public void createCustomer(String name, String postcode, int telephone) {
        this.customer = new Customer(name, postcode, telephone);
        this.payment = new Payment(customer);
        this.hire = new Hire(LocalDate.now(), numberOfDays, chosenBike, customer);
    }

    public void calculateTotalPayment() {
        if (payment != null && hire != null) {
            payment.calculateTotalPayment(hire);
        } else {
            System.out.println("Cannot calculate payment: customer or hire not initialized.");
        }
    }
}
