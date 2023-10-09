import java.util.Arrays;
import java.util.concurrent.RecursiveTask;

public class ForkJoinMergeSort extends RecursiveTask<int[]>{

    private final int[] list;

    public ForkJoinMergeSort(int[] _list) {
        this.list = _list;
    }
    @Override
    protected int[] compute() {
        if(list.length == 1) {
            return list;
        }

        int[] arrayOne = Arrays.copyOfRange(list, 0, list.length / 2);
        int[] arrayTwo = Arrays.copyOfRange(list, list.length / 2, list.length);

        ForkJoinMergeSort taskOne = new ForkJoinMergeSort(arrayOne);
        ForkJoinMergeSort taskTwo = new ForkJoinMergeSort(arrayTwo);

        taskTwo.fork();
        arrayOne = taskOne.compute();
        arrayTwo = taskTwo.join();

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
