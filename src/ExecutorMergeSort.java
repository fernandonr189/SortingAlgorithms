import java.util.Arrays;
import java.util.concurrent.*;

public class ExecutorMergeSort {

    public static void mergeSort(int[] list, ExecutorService executor) {
        if(list.length == 1) {
            return;
        }

        int[] arrayOne = Arrays.copyOfRange(list, 0, list.length / 4);
        int[] arrayTwo = Arrays.copyOfRange(list, list.length / 4, list.length / 2);
        int[] arrayThree = Arrays.copyOfRange(list, list.length / 2, list.length * 3 / 4);
        int[] arrayFour = Arrays.copyOfRange(list, list.length * 3 / 4, list.length);

        Future<?> futureOne = executor.submit(() -> MergeSort.doMergeSort(arrayOne));
        Future<?> futureTwo = executor.submit(() -> MergeSort.doMergeSort(arrayTwo));
        Future<?> futureThree = executor.submit(() -> MergeSort.doMergeSort(arrayThree));
        Future<?> futureFour = executor.submit(() -> MergeSort.doMergeSort(arrayFour));

  
        try {
            futureOne.get();
            futureTwo.get();
            futureThree.get();
            futureFour.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    
        
        Future<int[]> resultOne = executor.submit(() -> sort(arrayOne, arrayTwo));
        Future<int[]> resultTwo = executor.submit(() -> sort(arrayThree, arrayFour));

        Future<?> result = executor.submit(() -> {
            try {
                sort(resultOne.get(), resultTwo.get(), list);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
        
        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
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

    private static void sort(int[] arrayA, int[] arrayB, int[] arrayC) {
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
    }
}
