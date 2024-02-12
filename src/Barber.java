public class Barber implements Runnable {
    private WaitingRoom waitingRoom;
    private boolean isSleeping;

    public Barber(WaitingRoom waitingRoom) {
        this.waitingRoom = waitingRoom;
        this.isSleeping = true;
    }

    public void run() {
        while (true) {
            if (waitingRoom.isEmpty()) {
                isSleeping = true;
                System.out.println("Who is sleeping?");
            } else {
                isSleeping = false;
                System.out.println("Who is cutting hair?");
                waitingRoom.remove();
            }
        }
    }

    public boolean isSleeping() {
        return isSleeping;
    }
}
