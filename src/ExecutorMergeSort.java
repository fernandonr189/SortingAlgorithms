import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorMergeSort {

    public static int[] mergeSort(int[] list, ExecutorService executor, int chunks) {

        int[][] arrays = new int[chunks][];
        for(int i = 0; i < chunks; i++) {
            arrays[i] = Arrays.copyOfRange(list, list.length * i / chunks, list.length * (i + 1) / chunks);
        }

        List<Callable<Void>> tasks = new ArrayList<>();
        for(int i = 0; i < chunks; i++) {
            tasks.add(new MergeSortCallable(arrays[i]));
        }
        try {
            executor.invokeAll(tasks);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        List<Future<int[]>> futures = new ArrayList<>();
        for(int i = 0; i < chunks / 2; i++) {
            int finalI = i;
            futures.add(executor.submit(() -> sort(arrays[finalI * 2], arrays[(finalI * 2) + 1])));
        }

        while(futures.size() > 1) {
            List<Future<int[]>> newList = new ArrayList<>();
            List<Future<int[]>> finalFutures = futures;
            if(futures.size() == 3) {
                Future<int[]> last = executor.submit(() -> sort(finalFutures.get(0).get(), finalFutures.get(1).get()));
                newList.add(executor.submit(() -> sort(last.get(), finalFutures.get(2).get())));
            }
            else {
                for(int i = 0; i < futures.size() / 2; i++) {
                    int finalI = i;
                    newList.add(executor.submit(() -> sort(finalFutures.get(finalI * 2).get(), finalFutures.get((finalI * 2) + 1).get())));
                }
            }
            futures = newList;
        }

        try {
            return futures.get(0).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
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
