public class Barber implements Runnable {
    private boolean sleeping;
    private WaitingRoom waitingRoom;

    public Barber() {
        this.sleeping = false;
    }

    @Override
    public void run() {
        try {
            this.cut();
        } catch (InterruptedException e) {
            System.out.println("barber has finished his job");
        }
    }

    public void sleep() {
        this.sleeping = true;
    }

    public void wakeUp() {
        this.sleeping = false;
    }

    public boolean isSleeping() {
        return sleeping;
    }

    public void checkWaitingRoom() {
        if (waitingRoom.isEmpty()) {
            this.sleep();
        } else {
            this.wakeUp();
        }
    }
    // public void sitNextCustomer() throws InterruptedException {
    // double mc = 4; // mean calling time
    // double sdc = 1; // standard deviation calling time
    // double timeCalling = mc + sdc * Math.random();
    // try {
    // System.out.println("Next customer arrived in " + timeCalling + " seconds");
    // Thread.sleep((long) timeCalling);
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
    // }

    public void cut() throws InterruptedException {
        double mh = 1; // mean shaving time
        double sdh = 1; // standard deviation shaving time
        double timeCutting = mh + sdh * Math.random();
        try {
            System.out.println("Customer got a haircut in " + timeCutting + " seconds");
            Thread.sleep((long) timeCutting);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
