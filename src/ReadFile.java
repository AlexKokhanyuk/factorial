import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Oleksandr Kokhaniuk
 * @created 4/22/2022, 9:47 PM
 */
public class ReadFile implements Callable {
    private String fileName;
    private ConcurrentMap<Integer, Integer> mapOfInput;
    private AtomicInteger counterInput;

    public ReadFile(
            String fileName,
            ConcurrentMap<Integer, Integer> mapOfInput,
            AtomicInteger counterInput) {
        this.fileName = fileName;
        this.mapOfInput = mapOfInput;
        this.counterInput = counterInput;

    }


    private void toReadToMap(String fileName) {
        int input;
        int key;

        try {
            File file = new File(fileName);
            FileReader fr = new FileReader(file);
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                try {
                    input = Integer.valueOf(line);
                    key = counterInput.getAndIncrement();
                    mapOfInput.put(key, input);
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

    @Override
    public Object call() throws Exception {
        toReadToMap(fileName);
        return mapOfInput;
    }
}
