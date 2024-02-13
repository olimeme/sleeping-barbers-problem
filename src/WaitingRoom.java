import java.util.concurrent.ArrayBlockingQueue;

public class WaitingRoom {
    private ArrayBlockingQueue<Customer> customers;

    public WaitingRoom(int capacity) {
        this.customers = new ArrayBlockingQueue<>(capacity);
    }

    public void addCustomer(Customer customer) {
        try {
            this.customers.put(customer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Customer nextCustomer() {
        return this.customers.poll();
    }

    public boolean isEmpty() {
        return this.customers.isEmpty();
    }
}
