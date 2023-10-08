import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeSort {


    public static ArrayList<Integer> doMergeSort(ArrayList<Integer> a) {
        if(a.size() == 1) {
            return a;
        }
        ArrayList<Integer> listOne = new ArrayList<>(a.subList(0, a.size() / 2));
        ArrayList<Integer> listTwo = new ArrayList<>(a.subList(a.size() / 2, a.size()));

        listOne = doMergeSort(listOne);
        listTwo = doMergeSort(listTwo);

        return sort(listOne, listTwo);
    }

    private static ArrayList<Integer> sort(ArrayList<Integer> arrayA, ArrayList<Integer> arrayB) {
        ArrayList<Integer> arrayC = new ArrayList<Integer>();

        while(!arrayA.isEmpty() && !arrayB.isEmpty()) {
            if(arrayA.get(0) > arrayB.get(0)) {
                arrayC.add(arrayB.get(0));
                arrayB.remove(0);
            }
            else {
                arrayC.add(arrayA.get(0));
                arrayA.remove(0);
            }
        }

        while(!arrayA.isEmpty()) {
            arrayC.add(arrayA.get(0));
            arrayA.remove(0);
        }
        while(!arrayB.isEmpty()) {
            arrayC.add(arrayB.get(0));
            arrayB.remove(0);
        }

        return arrayC;
    }
}
