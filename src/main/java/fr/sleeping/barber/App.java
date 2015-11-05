package fr.sleeping.barber;

public class App {

    private static final int tick = 10000;

    public static void main(String args[]) {
        BarberShop barberShop = new BarberShop();

        Thread barberThread = new Thread(barberShop.getBarber());
        barberThread.start();

        while(true) {
            Thread customerThread = new Thread(new Customer(barberShop, "Client"));
            customerThread.start();

            try {
                Thread.sleep((int)(Math.random()* tick));
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
