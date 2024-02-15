import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class Customer implements Runnable {
    private Semaphore barberAvailable;
    private Semaphore customerAvailable;
    private AtomicInteger customerCount;
    private AtomicInteger availableSeats;

    public Customer(AtomicInteger customerCount, AtomicInteger seats, Semaphore barberAvailable,
            Semaphore customerAvailable) {
        this.barberAvailable = barberAvailable;
        this.customerAvailable = customerAvailable;
        this.availableSeats = seats;
        this.customerCount = customerCount;
    }

    @Override
    public void run() {
        try {
            // Customer is available
            customerAvailable.release();

            if (barberAvailable.hasQueuedThreads()) {
                availableSeats.decrementAndGet();
                barberAvailable.acquire();
                System.out.println("Customer " + this.customerCount + " in waiting area");
                availableSeats.incrementAndGet();
            } else {
                System.out.println("Customer woke up barber");
                barberAvailable.acquire();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
