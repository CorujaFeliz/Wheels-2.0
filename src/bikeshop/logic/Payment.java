package bikeshop.logic;

import bikeshop.model.Customer;
import bikeshop.model.Hire;

public class Payment {

    private static int paymentCount = 1;

    private final int paymentId;
    private final Customer customer;

    public Payment(Customer customer) {
        this.customer = customer;
        this.paymentId = paymentCount++;
    }

    public int getPaymentId() { return paymentId; }
    public Customer getCustomer() { return customer; }

    public void calculateTotalPayment(Hire hire) {
        issueReceipt(hire);
        int totalCost = hire.getBike().calculateCost(hire.getNumberOfDays());
        System.out.printf("Total payment due: %d%n", totalCost);
    }

    private void issueReceipt(Hire hire) {
        String name = hire.getCustomer().getNome();
        String postcode = hire.getCustomer().getPostcode();
        int days = hire.getNumberOfDays();
        int bikeNumber = hire.getBike().getBikeNumber();

        System.out.println("===== Receipt =====");
        System.out.printf("Customer: %s%n", name);
        System.out.printf("Postcode: %s%n", postcode);
        System.out.printf("Bike Number: %d%n", bikeNumber);
        System.out.printf("Days Hired: %d%n", days);
    }
}