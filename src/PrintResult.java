import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Oleksandr Kokhaniuk
 * @created 4/23/2022, 10:38 AM
 */
public class PrintResult implements Runnable {
    private String fileName;
    private ConcurrentLinkedDeque<Integer> deque;
    private AtomicInteger counterOutput;

    public PrintResult(String fileName, ConcurrentLinkedDeque<Integer> deque, AtomicInteger counterOutput) {
        this.deque = deque;
        this.fileName = fileName;
        this.counterOutput = counterOutput;
    }

    @Override
    public void run() {

        FileWriter fileWriter;
        File file = new File(fileName);
        StringBuilder stringRezult = new StringBuilder();
        int inputResult;
        BigInteger factorial;

        synchronized (this) {
            while (deque.isEmpty()) {
                try {
                    this.wait(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            while (!deque.isEmpty()) {
                inputResult = deque.pollFirst();
                factorial = getFactorial(inputResult);
                stringRezult.append(inputResult + "=" + factorial + "\n");
                counterOutput.getAndIncrement();
            }
            try {
                if (file.exists()) {
                    Files.delete(Path.of(fileName));
                }
                fileWriter = new FileWriter(fileName, true);
                fileWriter.write(String.valueOf(stringRezult));
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private BigInteger getFactorial(int f) {
        if (f <= 1) {
            return BigInteger.valueOf(1);
        } else {
            return BigInteger.valueOf(f).multiply(getFactorial(f - 1));
        }
    }
}
