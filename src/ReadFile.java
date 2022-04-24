import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Oleksandr Kokhaniuk
 * @created 4/22/2022, 9:47 PM
 */
public class ReadFile implements Callable<List<Integer>>, Runnable {
    private String fileName;
    private ConcurrentLinkedDeque<Integer> deque;
    AtomicInteger counterInput;

    private List<Integer> inputList = new ArrayList<>();

    public ReadFile(String fileName, ConcurrentLinkedDeque<Integer> deque, AtomicInteger counterInput) {
        this.fileName = fileName;
        this.deque = deque;
        this.counterInput = counterInput;
    }

    public ReadFile(String fileName, AtomicInteger counterInput) {
        this.fileName = fileName;
        this.counterInput = counterInput;
    }

    public void run() {
        toReadToDeque(fileName);
    }

    @Override
    public List<Integer> call() throws Exception {
        return toReadList(fileName);
    }

    private List<Integer> toReadList(String fileName) {
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
                    inputList.add(input);
                } catch (NumberFormatException e) {
                }
                line = reader.readLine();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputList;
    }

    private void toReadToDeque(String fileName) {
        Integer input;

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
