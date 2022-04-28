import java.math.BigInteger;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Oleksandr Kokhaniuk
 * @created 4/22/2022, 9:12 PM
 */


public class Main {
    public static CopyOnWriteArraySet<String> threadsName = new CopyOnWriteArraySet<>();

    public static void main(String[] args) {
        ConcurrentMap<Integer, BigInteger> mapOfFactorial = new ConcurrentHashMap<>();
        ConcurrentMap<Integer, Integer> mapOfInput = new ConcurrentHashMap<>();
        AtomicInteger counterInput = new AtomicInteger(0);
        AtomicInteger counterCalculate = new AtomicInteger(0);
        String fileNameInput = ".\\src\\resources\\input.txt";
        String fileNameOutput = ".\\src\\resources\\output.txt";
        int numberOfThreads = 5;

        System.out.println("Please enter the number of Threads between 1 to 10");

        Scanner console = new Scanner(System.in);
        try {
            int inputNumber = console.nextInt();
            if (inputNumber > 0 & inputNumber < 10) {
                numberOfThreads = inputNumber;
            } else {
                System.out.println("The entered number does not match the range, the default number of threads is 5.");
            }
        } catch (InputMismatchException e) {
            System.out.println("You must enter a number, the default number of threads is 5.");
        }
        ExecutorService pool = Executors.newFixedThreadPool(numberOfThreads);
        ReadFile readFile = new ReadFile(fileNameInput, mapOfInput, counterInput);
        CalculateFactorial calculateFactorial = new CalculateFactorial(counterInput, counterCalculate, mapOfInput, mapOfFactorial);
        PrintResult printResult = new PrintResult(fileNameOutput, mapOfInput, mapOfFactorial, counterInput);
        Future<ConcurrentMap<Integer, Integer>> future = pool.submit((Callable<ConcurrentMap<Integer, Integer>>) readFile);
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 20; i++) {
            pool.execute(calculateFactorial);
        }
        pool.execute(printResult);

        pool.shutdown();
        while (!pool.isTerminated()) {
        }
        System.out.println("Treads:");
        threadsName.forEach(System.out::println);
        System.out.println("- are calculated Factorial.");
        System.out.println("Read " + counterInput + " line containing numbers.");
        System.out.println("Finished all threads");


    }
}