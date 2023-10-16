import java.util.Arrays;

public class MergeSort {


    public static void doMergeSort(int[] a) {
        if(a.length == 1) {
            return;
        }

        int[] arrayOne = Arrays.copyOfRange(a, 0, a.length / 2);
        int[] arrayTwo = Arrays.copyOfRange(a, a.length / 2, a.length);

        doMergeSort(arrayOne);
        doMergeSort(arrayTwo);

        sort(arrayOne, arrayTwo, a);
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
