import java.util.Arrays;
import java.util.concurrent.RecursiveAction;

public class ForkJoinMergeSort extends RecursiveAction{

    private final int[] list;

    public ForkJoinMergeSort(int[] _list) {
        this.list = _list;
    }

    @Override
    protected void compute() {
        if(list.length == 1) {
            return;
        }

        int[] arrayOne = Arrays.copyOfRange(list, 0, list.length / 2);
        int[] arrayTwo = Arrays.copyOfRange(list, list.length / 2, list.length);

        ForkJoinMergeSort taskOne = new ForkJoinMergeSort(arrayOne);
        ForkJoinMergeSort taskTwo = new ForkJoinMergeSort(arrayTwo);

        invokeAll(taskOne);
        invokeAll(taskTwo);

        sort(arrayOne, arrayTwo, list);
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
