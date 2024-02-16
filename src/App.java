import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
    public static final int NUM_BARBERS = 3;
    public static final int WAITING_ROOM_CAPACITY = 15;
    public static final int MEAN_CUSTOMER_ARRIVAL_TIME = 500;
    public static final int STANDARD_DEVIATION_ARRIVAL_TIME = 100;

    public static void main(String[] args) throws Exception {

        AtomicInteger waitingRoomCapacity = new AtomicInteger(WAITING_ROOM_CAPACITY);
        AtomicInteger customerCount = new AtomicInteger(0);
        final Semaphore barbersSemaphore = new Semaphore(NUM_BARBERS, true);
        final Semaphore customersSemaphore = new Semaphore(0, true);

        ExecutorService service = Executors.newFixedThreadPool(NUM_BARBERS);

        for (int i = 0; i < NUM_BARBERS; i++)
            service.execute(
                    new Barber(i + 1).setBarberSemaphore(barbersSemaphore).setCustomerSemaphore(customersSemaphore));

        while (true) {
            Thread.sleep(generateArrivalTime(MEAN_CUSTOMER_ARRIVAL_TIME, STANDARD_DEVIATION_ARRIVAL_TIME));
            if (waitingRoomCapacity.get() >= 0) {
                System.out.println(
                        "Customer " + customerCount.incrementAndGet() + " walks in - " + waitingRoomCapacity.get()
                                + " seats available");
                new Thread(new Customer(waitingRoomCapacity, barbersSemaphore, customersSemaphore))
                        .start();
            } else {
                System.out.println("No seats available, we lost a customer!");
            }
        }

    }

    private static int generateArrivalTime(int mean, int deviation) {
        return ThreadLocalRandom.current().nextInt(mean, mean + deviation);

    }
}

class Barber implements Runnable {
    public static final int MEAN_CUT_TIME = 10000;
    public static final int STANDARD_DEVIATION_CUT_TIME = 100;
    private int id;
    private Semaphore barberAvailable;
    private Semaphore customerAvailable;

    public Barber(int id) {
        this.id = id;
    }

    public Barber setBarberSemaphore(Semaphore barberAvailable) {
        this.barberAvailable = barberAvailable;
        return this;
    }

    public Barber setCustomerSemaphore(Semaphore customerAvailable) {
        this.customerAvailable = customerAvailable;
        return this;
    }

    @Override
    public void run() {
        while (true) {
            try {
                customerAvailable.acquire();
                System.out.println("Customer sat down in the chair  " + this.id);
                Thread.sleep(generateArrivalTime(MEAN_CUT_TIME, STANDARD_DEVIATION_CUT_TIME));
                barberAvailable.release();
                System.out.println("Customer got hair cut and barber " + this.id + " went to sleep");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static int generateArrivalTime(int mean, int deviation) {
        return ThreadLocalRandom.current().nextInt(mean, mean + deviation);

    }

}

class Customer implements Runnable {
    private Semaphore barberAvailable;
    private Semaphore customerAvailable;
    private AtomicInteger availableSeats;

    public Customer(AtomicInteger seats, Semaphore barberAvailable,
            Semaphore customerAvailable) {
        this.barberAvailable = barberAvailable;
        this.customerAvailable = customerAvailable;
        this.availableSeats = seats;
    }

    @Override
    public void run() {
        try {
            customerAvailable.release();

            if (barberAvailable.hasQueuedThreads()) {
                availableSeats.decrementAndGet();
                barberAvailable.acquire();
                availableSeats.incrementAndGet();
            } else {
                barberAvailable.acquire();
                System.out.println("Customer woke up barber");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}