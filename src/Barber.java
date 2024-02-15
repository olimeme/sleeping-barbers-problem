import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Barber implements Runnable {
    public static final int MEAN_CUT_TIME = 1000;
    public static final int STANDARD_DEVIATION_CUT_TIME = 100;
    private int id;
    private Semaphore barberAvailable;
    private Semaphore customerAvailable;

    public Barber(int id, Semaphore barberAvailable, Semaphore customerAvailable) {
        this.barberAvailable = barberAvailable;
        this.customerAvailable = customerAvailable;
        this.id = id;
    }

    @Override
    public void run() {
        while (true) {
            try {
                customerAvailable.acquire();
                System.out.println("Customer sat down in the chair  " + this.id);
                Thread.sleep(generateArrivalTime(MEAN_CUT_TIME, STANDARD_DEVIATION_CUT_TIME));
                System.out.println("Customer got hair cut and barber " + this.id + " went to sleep");
                barberAvailable.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static int generateArrivalTime(int mean, int deviation) {
        return ThreadLocalRandom.current().nextInt(mean, mean + deviation);

    }

}
