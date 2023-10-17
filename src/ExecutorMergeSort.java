import java.util.Arrays;
import java.util.concurrent.*;

public class ExecutorMergeSort implements Callable<int[]> {


    private int[] list;
    private ExecutorService executor;
    public ExecutorMergeSort(int[] _list, ExecutorService _executor) {
        this.list = _list;
        this.executor = _executor;
    }

    @Override
    public int[] call() {
        if(list.length == 1) {
            return list;
        }

        int[] arrayOne = Arrays.copyOfRange(list, 0, list.length / 2);
        int[] arrayTwo = Arrays.copyOfRange(list, list.length / 2, list.length);

        Future<int[]> futureOne = executor.submit(new ExecutorMergeSort(arrayOne, executor));
        Future<int[]> futureTwo = executor.submit(new ExecutorMergeSort(arrayTwo, executor));

        try {
            arrayOne = futureOne.get();
            arrayTwo = futureTwo.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        return sort(arrayOne, arrayTwo);
    }


    private static int[] sort(int[] arrayA, int[] arrayB) {
        int[] arrayC = new int[arrayA.length + arrayB.length];
        int bCounter = 0;
        int aCounter = 0;
        int cCounter = 0;

        while(arrayA.length > aCounter && arrayB.length > bCounter) {
            if(arrayA[aCounter] > arrayB[bCounter]) {
                arrayC[cCounter] = arrayB[bCounter];
                bCounter++;
            }
            else {
                arrayC[cCounter] = arrayA[aCounter];
                aCounter++;
            }
            cCounter++;
        }

        while(arrayA.length > aCounter) {
            arrayC[cCounter] = arrayA[aCounter];
            aCounter++;
            cCounter++;
        }
        while(arrayB.length > bCounter) {
            arrayC[cCounter] = arrayB[bCounter];
            bCounter++;
            cCounter++;
        }

        return arrayC;
    }
}
