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

        return merge(arrayOne, arrayTwo);
    }

    private static int[] merge(int [] arrayA, int[] arrayB) {
        List<Integer> arrayC = new ArrayList<Integer>();

        while(arrayA.length > 0 && arrayB.length > 0) {
            if(arrayA[0] > arrayB[0]) {
                arrayC.add(arrayB[0]);
                arrayB = Arrays.copyOfRange(arrayB, 1, arrayB.length);
            }
            else {
                arrayC.add(arrayA[0]);
                arrayA = Arrays.copyOfRange(arrayA, 1, arrayA.length);
            }
        }

        List<Integer> listA = arrayToList(arrayA);
        List<Integer> listB = arrayToList(arrayB);

        while(!listA.isEmpty()) {
            arrayC.add(listA.get(0));
            listA.remove(0);
        }
        while(!listB.isEmpty()) {
            arrayC.add(listB.get(0));
            listB.remove(0);
        }

        int[] arr = new int[arrayC.size()];
        for(int i = 0; i < arrayC.size(); i++) {
            arr[i] = arrayC.get(i);
        }
        return arr;
    }

    public static List<Integer> arrayToList(int[] arr) {
        List<Integer> newList = new ArrayList<Integer>();
        for(int i: arr) {
            newList.add(i);
        }
        return newList;
    }
}
