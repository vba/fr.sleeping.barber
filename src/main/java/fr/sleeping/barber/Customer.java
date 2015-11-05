package fr.sleeping.barber;

public class Customer implements Runnable {
    private static int id = 1;

    private String customerName;

    private BarberShop barberShop;

    public Customer(BarberShop barberShop, String name) {
        customerName = name + id;
        this.barberShop = barberShop;
        id++;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void run() {
        barberShop.sitInWorkspace(this);
    }
}
