import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
    public static final int NUM_BARBERS = 3;
    public static final int WAITING_ROOM_CAPACITY = 15;
    public static final int MEAN_CUSTOMER_ARRIVAL_TIME = 10000;
    public static final int STANDARD_DEVIATION_ARRIVAL_TIME = 1000;

    public static void main(String[] args) throws Exception {

        AtomicInteger waitingRoomCapacity = new AtomicInteger(WAITING_ROOM_CAPACITY);
        AtomicInteger customerCount = new AtomicInteger(0);
        final Semaphore barbersSemaphore = new Semaphore(NUM_BARBERS, true);
        final Semaphore customersSemaphore = new Semaphore(0, true);
        ExecutorService openUp = Executors.newFixedThreadPool(NUM_BARBERS);

        Barber[] barbers = new Barber[NUM_BARBERS];

        for (int i = 0; i < NUM_BARBERS; i++) {
            barbers[i] = new Barber(i + 1, barbersSemaphore, customersSemaphore);
            openUp.execute(barbers[i]);
        }

        while (true) {
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(MEAN_CUSTOMER_ARRIVAL_TIME,
                        MEAN_CUSTOMER_ARRIVAL_TIME + STANDARD_DEVIATION_ARRIVAL_TIME)); // Sleep until next person gets
                                                                                        // in
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (waitingRoomCapacity.get() >= 0) {
                System.out.println("Customer walks in - " + waitingRoomCapacity.get() + " seats available");
                customerCount.incrementAndGet();
                new Thread(new Customer(customerCount, waitingRoomCapacity, barbersSemaphore, customersSemaphore))
                        .start();
            } else {
                System.out.println("No seats available, we lost a customer!");
            }
        }

    }
}
