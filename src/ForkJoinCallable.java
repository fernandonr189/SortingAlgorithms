import java.util.concurrent.Callable;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinCallable implements Callable<Void> {

    private int[] list;
    private ForkJoinPool pool;
    public ForkJoinCallable(int[] _list, ForkJoinPool _pool) {
        this.list = _list;
        this.pool = _pool;
    }
    @Override
    public Void call() {
        pool.invoke(new ForkJoinMergeSort(list));
        return null;
    }
}
