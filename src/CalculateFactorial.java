import java.math.BigInteger;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Oleksandr Kokhaniuk
 * @created 4/27/2022, 8:27 PM
 */
public class CalculateFactorial implements Runnable {
    private ConcurrentMap<Integer, BigInteger> mapOfFactorial;
    private ConcurrentMap<Integer, Integer> mapOfInput;
    private AtomicInteger counterInput;
    private AtomicInteger counterCalculate;

    public CalculateFactorial(
            AtomicInteger couterInput,
            AtomicInteger couterCalculate,
            ConcurrentMap<Integer, Integer> mapOfInput,
            ConcurrentMap<Integer, BigInteger> mapOfFactorial) {
        this.counterInput = couterInput;
        this.counterCalculate = couterCalculate;
        this.mapOfInput = mapOfInput;
        this.mapOfFactorial = mapOfFactorial;
    }

    @Override
    public void run() {
        synchronized (this) {
            while (mapOfInput == null) {
                try {
                    this.wait(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        int key = counterCalculate.getAndIncrement();
        if (mapOfFactorial.containsKey(key)) {
            key += 10000;
        }
        while (key <= counterInput.get()) {
            if (mapOfInput.containsKey(key)) {
                int arg = mapOfInput.get(key);
                BigInteger res = getFactorial(arg);
                mapOfFactorial.put(key, res);
                Main.threadsName.add(Thread.currentThread().getName());
            }
            key = counterCalculate.getAndIncrement();
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
