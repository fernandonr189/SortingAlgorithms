import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ForkJoinMergeSort extends RecursiveTask<int[]>{


    private int[] arr;
    public ForkJoinMergeSort(int[] _arr) {
        this.arr = _arr;
    }

    @Override
    protected int[] compute() {
        if(this.arr.length == 1) {
            return this.arr;
        }

        int[] arrayOne = Arrays.copyOfRange(this.arr, 0, this.arr.length / 2);
        int[] arrayTwo = Arrays.copyOfRange(this.arr, this.arr.length / 2, this.arr.length);

        ForkJoinMergeSort arrayOneTask = new ForkJoinMergeSort(arrayOne);
        ForkJoinMergeSort arrayTwoTask = new ForkJoinMergeSort(arrayTwo);

        arrayTwoTask.fork();
        arrayOne = arrayOneTask.compute();
        arrayTwo = arrayTwoTask.join();

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
