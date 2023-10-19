import java.util.concurrent.Callable;

public class MergeSortCallable implements Callable<Void> {

    int[] list;
    public MergeSortCallable(int[] _list) {
        this.list = _list;
    }
    @Override
    public Void call() {
        MergeSort.doMergeSort(list);
        return null;
    }
}
