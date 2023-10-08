import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class ForkJoinMergeSort extends RecursiveTask<ArrayList<Integer>>{

    private final ArrayList<Integer> list;

    public ForkJoinMergeSort(ArrayList<Integer> _list) {
        this.list = _list;
    }
    @Override
    protected ArrayList<Integer> compute() {
        if(list.size() == 1) {
            return list;
        }

        ArrayList<Integer> listOne = new ArrayList<>(list.subList(0, list.size() / 2));
        ArrayList<Integer> listTwo = new ArrayList<>(list.subList(list.size() / 2, list.size()));

        ForkJoinMergeSort taskOne = new ForkJoinMergeSort(listOne);
        ForkJoinMergeSort taskTwo = new ForkJoinMergeSort(listTwo);

        taskTwo.fork();
        listOne = taskOne.compute();
        listTwo = taskTwo.join();

        return sort(listOne, listTwo);
    }


    private static ArrayList<Integer> sort(List<Integer> arrayA, List<Integer> arrayB) {
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
