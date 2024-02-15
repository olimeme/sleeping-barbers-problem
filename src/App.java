import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class App {
    public static final int NUM_BARBERS = 3;
    public static final int WAITING_ROOM_CAPACITY = 15;
    public static final int MEAN_CUSTOMER_ARRIVAL_TIME = 5000;
    public static final int STANDARD_DEVIATION_ARRIVAL_TIME = 100;

    public static void main(String[] args) throws Exception {

        AtomicInteger waitingRoomCapacity = new AtomicInteger(WAITING_ROOM_CAPACITY);
        AtomicInteger customerCount = new AtomicInteger(0);
        final Semaphore barbersSemaphore = new Semaphore(NUM_BARBERS, true);
        final Semaphore customersSemaphore = new Semaphore(0, true);

        ExecutorService service = Executors.newFixedThreadPool(NUM_BARBERS);
        Barber[] barbers = new Barber[NUM_BARBERS];

        for (int i = 0; i < NUM_BARBERS; i++) {
            barbers[i] = new Barber(i + 1, barbersSemaphore, customersSemaphore);
            service.execute(barbers[i]);
        }

        while (true) {
            Thread.sleep(generateArrivalTime(MEAN_CUSTOMER_ARRIVAL_TIME, STANDARD_DEVIATION_ARRIVAL_TIME));
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

    private static int generateArrivalTime(int mean, int deviation) {
        return ThreadLocalRandom.current().nextInt(mean, mean + deviation);

    }
}
