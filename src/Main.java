import java.util.ArrayList;
import java.util.Random;

public class Main {
    private static int hits = 0;
    private static Object sync = new Object();

    public static void main(String[] args) {
        int num_threads = Integer.parseInt(args[0]);
        int num_it = Integer.parseInt(args[1]);

        Main main = new Main();
        main.math(num_threads, num_it);
    }

    private void math(int num_threads, int num_it) {
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < num_threads; i++) {
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    Random rand = new Random();
                    int localHits = 0;

                    for (int i = 0; i < num_it; i++) {
                        double x = rand.nextDouble();
                        double y = rand.nextDouble();
                        if (x * x + y * y <= 1.0) {
                            localHits++;
                        }
                    }

                    synchronized (sync) {
                        hits += localHits;
                    }
                }
            });
            threads.add(thread);
        }

        for (Thread t : threads) {
            t.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        double pi = 4.0 * hits / (num_threads * num_it);
        System.out.printf("Estimate of pi: %.10f\n", pi);
    }
}



