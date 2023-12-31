public class ArrayUtils {
    public static String getArrayString(int[] arr) {
        StringBuilder printableArr = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if(arr[i] < 10) {
                printableArr.append("[0").append(arr[i]).append("]");
            }
            else {
                printableArr.append("[").append(arr[i]).append("]");
            }
            if((i + 1) % 15 == 0) {
                printableArr.append("\n");
            }
        }
        return printableArr.toString();
    }
    public static int[] createRandomArray(int size) {
        int[] arr = new int[size];
        for(int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (99) + 1);
        }
        return arr;
    }

}