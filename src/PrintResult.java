import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Oleksandr Kokhaniuk
 * @created 4/23/2022, 10:38 AM
 */
public class PrintResult implements Runnable {
    private String fileName;
    private AtomicInteger counterInput;
    private ConcurrentMap<Integer, Integer> mapOfInput;
    private ConcurrentMap<Integer, BigInteger> mapOfFactorial;

    public PrintResult(String fileName,
                       ConcurrentMap<Integer, Integer> mapOfInput,
                       ConcurrentMap<Integer, BigInteger> mapOfFactorial,
                       AtomicInteger counterInput
    ) {
        this.mapOfFactorial = mapOfFactorial;
        this.mapOfInput = mapOfInput;
        this.fileName = fileName;
        this.counterInput = counterInput;
    }

    @Override
    public void run() {

        FileWriter fileWriter;
        File file = new File(fileName);
        synchronized (this) {
            try {
                this.wait(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            if (file.exists()) {
                Files.delete(Path.of(fileName));
            }
            fileWriter = new FileWriter(fileName, true);
            fileWriter.write(String.valueOf(printMap()));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private StringBuilder printMap() {
        StringBuilder stb = new StringBuilder();
        int inputResult;
        BigInteger factorial;
        for (int i = 0; i <= counterInput.get(); i++) {
            if (mapOfInput.containsKey(i)) {
                inputResult = mapOfInput.get(i);
                factorial = mapOfFactorial.get(i);
                stb.append(inputResult + "=" + factorial + "\n");
            }
        }
        return stb;
    }

}

