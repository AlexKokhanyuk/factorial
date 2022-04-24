import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Oleksandr Kokhaniuk
 * @created 4/22/2022, 9:12 PM
 */


public class Main {

    public static void main(String[] args) throws InterruptedException {

        ConcurrentLinkedDeque<Integer> deque = new ConcurrentLinkedDeque<>();
        List<Integer> inputList = new ArrayList<>();
        AtomicInteger counterInput = new AtomicInteger(0);
        AtomicInteger counterOutput = new AtomicInteger(0);

        String fileNameInput = ".\\src\\resources\\input.txt";
        String fileNameOutput = ".\\src\\resources\\output.txt";
        int numberOfThreads = 5;

        System.out.println("Please enter the number of Threads between 1 to 100");

        Scanner console = new Scanner(System.in);
        try {
            int inputNumber = console.nextInt();
            if (inputNumber > 0 & inputNumber < 100) {
                numberOfThreads = inputNumber;
            } else {
                System.out.println("The entered number does not match the range, the default number of threads is 5.");
            }
        } catch (InputMismatchException e) {
            System.out.println("You must enter a number, the default number of threads is 5.");
        }
        ExecutorService pool = Executors.newFixedThreadPool(numberOfThreads);
        if (numberOfThreads < 50) {
            ReadFile readFile = new ReadFile(fileNameInput, deque, counterInput);
            PrintResult printResult = new PrintResult(fileNameOutput, deque, counterOutput);
            pool.execute(readFile);
            pool.execute(printResult);
        } else {
            ReadFile readFile = new ReadFile(fileNameInput, counterInput);
            Future<List<Integer>> future = pool.submit((Callable<List<Integer>>) readFile);
            try {
                inputList = future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            PrintResult printResult = new PrintResult(fileNameOutput, counterOutput, inputList);
            pool.execute(printResult);
        }

        pool.shutdown();
        while (!pool.isTerminated()) {
        }
        System.out.println("Finished all threads");
        System.out.println("Read " + counterInput + " line containing numbers.");
        System.out.println("Wrote " + counterOutput + " line.");
    }
}