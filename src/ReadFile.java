import java.io.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Oleksandr Kokhaniuk
 * @created 4/22/2022, 9:47 PM
 */
public class ReadFile implements Runnable {
    private String fileName;
    private ConcurrentLinkedDeque<Integer> deque;
    AtomicInteger counterInput;

    public ReadFile(String fileName, ConcurrentLinkedDeque<Integer> deque, AtomicInteger counterInput) {
        this.fileName = fileName;
        this.deque = deque;
        this.counterInput = counterInput;
    }

    public void run() {

        Integer input;
        int counter = 0;
        try {
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                try {
                    input = Integer.valueOf(line);
                    counterInput.getAndIncrement();
                    deque.add(input);
                } catch (NumberFormatException e) {
                }
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
