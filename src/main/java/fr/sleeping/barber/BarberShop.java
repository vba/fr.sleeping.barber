package fr.sleeping.barber;

import java.util.Queue;
import java.util.LinkedList;

public class BarberShop {
    public static final int NUM_CHAIRS = 3;

    public static final int WORK_TIME = 10000;

    private Customer barberWorkspace;

    private enum BarberState {
        SLEEP, WORK, SCREW_AROUND
    }

    BarberState barberState;

    private int servedCustomersCount;

    private Barber barber;

    private Queue<Customer> customers = new LinkedList<Customer>();

    public BarberShop() {
        servedCustomersCount = 0;

        barber = new Barber();
    }

    public Barber getBarber() {
        return barber;
    }
    private void sitInWaitingRoom(Customer customer) {
        if( customers.size() < NUM_CHAIRS ) {
            customers.add(customer);
            System.out.println(customer.getCustomerName() + " takes the place in waiting room");
        } else {
            System.out.printf("%s leaves the shop, no more places, clients waiting for barber: %d%n%n", customer.getCustomerName(), customers.size());
        }
    }
    public synchronized void sitInWorkspace(Customer customer) {
        if( checkBarber(customer) == BarberState.SLEEP ) {
            System.out.println(customer.getCustomerName() + " wakes up the barber and sit to the chair");
            barberWorkspace = customer;
            barberState = BarberState.WORK;
        } else {
            sitInWaitingRoom(customer);
        }

        try {
            notify();
            wait();
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }

    }
    public BarberState checkBarber(Customer customer) {
        System.out.print(customer.getCustomerName() + " checks barber's state:");

        if( barberState == BarberState.SLEEP ){
            System.out.println(" barber sleeps");
        } else {
            System.out.println(" barber works");
        }

        return barberState;
    }
    public synchronized boolean checkCustomers() {
        System.out.printf("Barber check clients in the queue (%d of %d)%n%n", customers.size(), NUM_CHAIRS);
        return !customers.isEmpty();
    }
    public synchronized void work() {
        while( isWorkspaceEmpty() ) {
            try {
                sleep();
                wait();
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
        }

        while( !isWorkspaceEmpty() ) {
            if( barberState != BarberState.WORK)
                barberState = BarberState.WORK;

            System.out.printf("Barber starts with: %s%n%n", barberWorkspace.getCustomerName());

            try {
                wait(WORK_TIME);
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }

            System.out.printf("Barber ends with: %s%n%n", barberWorkspace.getCustomerName());
            barberState = BarberState.SCREW_AROUND;
            servedCustomersCount++;
            System.out.printf("Berber successfully served %d client(s)%n%n", servedCustomersCount);
            resetBarberWorkspace();
            callCustomer();
        }
    }
    //---------------------------------------------
    public synchronized void sleep() {
        if( barberState != BarberState.SLEEP ) {
            barberState = BarberState.SLEEP;
            System.out.println("Barber sleeps (zzzzzzz)");
        }
    }
    private boolean isWorkspaceEmpty() {
        return barberWorkspace == null;
    }
    private void resetBarberWorkspace() {
        barberWorkspace = null;
    }
    private synchronized void callCustomer() {
        if( checkCustomers() ) {
            barberWorkspace = customers.poll();
        }
    }
    public class Barber implements Runnable {
        public Barber() {
            barberState = BarberState.SCREW_AROUND;
        }

        public void run() {
            while(true) {
                work();
            }
        }

    }

}
