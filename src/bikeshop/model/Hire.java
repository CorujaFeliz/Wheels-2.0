package bikeshop.model;

import java.time.LocalDate;

public class Hire {
    private final int id;
    private final LocalDate startDate;
    private final int numberOfDays;
    private final Bike bike;
    private final Customer customer;
    private LocalDate endDate;  // nullable until the rental ends

    public Hire(int id,
                LocalDate startDate,
                int numberOfDays,
                Bike bike,
                Customer customer) {
        this.id = id;
        this.startDate = startDate;
        this.numberOfDays = numberOfDays;
        this.bike = bike;
        this.customer = customer;
    }

    // getters
    public int getId() { return id; }
    public LocalDate getStartDate() { return startDate; }
    public int getNumberOfDays() { return numberOfDays; }
    public Bike getBike() { return bike; }
    public Customer getCustomer() { return customer; }
    public LocalDate getEndDate() { return endDate; }

    // when the rental ends:
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
