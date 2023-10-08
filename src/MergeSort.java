import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeSort {


    public static int[] doMergeSort(int[] a) {
        if(a.length == 1) {
            return a;
        }

        int[] arrayOne = Arrays.copyOfRange(a, 0, a.length / 2);
        int[] arrayTwo = Arrays.copyOfRange(a, a.length / 2, a.length);


        arrayOne = doMergeSort(arrayOne);
        arrayTwo = doMergeSort(arrayTwo);

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
