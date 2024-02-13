import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.stream.Collectors.toList;

public class App {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 3; i++) {
            Barber barber = new Barber();
            new Thread(barber).start();
        }
    }
}
