
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.*;

enum SortingAlgorithm {
    NONE,
    MERGESORT,
    FORKJOIN,
    EXECUTE,
}

public class View extends JFrame {

    private Panel panel;
    private JButton mergeButton;
    private JButton forkJoinButton;
    private JButton executeButton;
    private JTextPane unsortedTextPane;
    private JTextPane sortedTextPane;
    private JTextField arraySizeTextField;
    private JButton startButton;
    private JButton clearButton;

    private JLabel selectedAlgorithmLabel;

    private JScrollPane scrollPane1;
    private JScrollPane scrollPane2;

    private JLabel elapsedTimeMergeSort;

    private JLabel elapsedTimeForkJoin;

    private JLabel elapsedTimeExecute;

    private long elapsedTime = 0;

    private final ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();

    private final DecimalFormat formatter = new DecimalFormat("#,###");

    private SortingAlgorithm sortingAlgorithm = SortingAlgorithm.NONE;
    public View(){
        panel = new Panel();
        panel.setBackground(Color.DARK_GRAY);
        this.setContentPane(panel);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setTitle("Algoritmos de ordenamiento");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 480);

        mergeButton = new JButton("MergeSort");
        mergeButton.setBounds(25, 60, 100, 25);

        forkJoinButton = new JButton("ForkJoin");
        forkJoinButton.setBounds(25, 95, 100, 25);

        executeButton = new JButton("Execute");
        executeButton.setBounds(25, 130, 100, 25);

        arraySizeTextField = new JTextField();
        arraySizeTextField.setBounds(25, 25, 100, 25);
        arraySizeTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                arraySizeTextField.setEditable(e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || e.getKeyChar() == '\u0008');
            }
        });

        unsortedTextPane = new JTextPane();
        unsortedTextPane.setEditable(false);

        sortedTextPane = new JTextPane();
        sortedTextPane.setEditable(false);

        startButton = new JButton("Comenzar");
        startButton.setBounds(25, 200, 100, 25);

        clearButton = new JButton("Borrar");
        clearButton.setBounds(25, 235, 100, 25);

        selectedAlgorithmLabel = new JLabel("<html>Algoritmo<br>selecciónado:<br>Ninguno</html>");
        selectedAlgorithmLabel.setBounds(25, 270, 100, 50);
        selectedAlgorithmLabel.setForeground(Color.WHITE);

        scrollPane1 = new JScrollPane(unsortedTextPane);
        scrollPane1.setBounds(150, 25, 400, 150);

        elapsedTimeMergeSort = new JLabel("");
        elapsedTimeMergeSort.setBounds(25, 390, 400, 25);
        elapsedTimeMergeSort.setForeground(Color.RED);

        scrollPane2 = new JScrollPane(sortedTextPane);
        scrollPane2.setBounds(150, 200, 400, 150);

        elapsedTimeForkJoin = new JLabel("");
        elapsedTimeForkJoin.setBounds(150, 390, 400, 25);
        elapsedTimeForkJoin.setForeground(Color.RED);

        elapsedTimeExecute = new JLabel("");
        elapsedTimeExecute.setBounds(275, 390, 400, 25);
        elapsedTimeExecute.setForeground(Color.RED);


        panel.add(mergeButton);
        panel.add(arraySizeTextField);
        panel.add(forkJoinButton);
        panel.add(executeButton);
        panel.add(scrollPane1);
        panel.add(scrollPane2);
        panel.add(startButton);
        panel.add(clearButton);
        panel.add(selectedAlgorithmLabel);
        panel.add(elapsedTimeMergeSort);
        panel.add(elapsedTimeForkJoin);
        panel.add(elapsedTimeExecute);

        mergeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectSortingAlgorithm(SortingAlgorithm.MERGESORT);
            }
        });

        forkJoinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectSortingAlgorithm(SortingAlgorithm.FORKJOIN);
            }
        });

        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectSortingAlgorithm(SortingAlgorithm.EXECUTE);
            }
        });

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectSortingAlgorithm(SortingAlgorithm.NONE);
                arraySizeTextField.setText("");
                sortedTextPane.setText("");
                unsortedTextPane.setText("");
                elapsedTimeMergeSort.setText("");
                elapsedTimeForkJoin.setText("");
                elapsedTimeExecute.setText("");
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(arraySizeTextField.getText().isEmpty()) {
                    sortedTextPane.setText("Introduzca un tamaño valido!");
                    return;
                }
                int arrayLength = Integer.parseInt(arraySizeTextField.getText());
                int[] unsortedArray = ArrayUtils.createRandomArray(arrayLength);
                //unsortedTextPane.setText(ArrayUtils.getArrayString(unsortedArray));
                String sortedArray = "";
                switch (sortingAlgorithm) {
                    case NONE -> {
                        sortedTextPane.setText("Seleccione un algoritmo!");
                        elapsedTime = 0;
                        return;
                    }
                    case MERGESORT -> {
                        long start = System.nanoTime();
                        MergeSort.doMergeSort(unsortedArray);
                        long finish = System.nanoTime();
                        elapsedTime = finish - start;
                        sortedArray = ArrayUtils.getArrayString(unsortedArray);
                        elapsedTimeMergeSort.setText("Merge: " + formatter.format(elapsedTime / 1000) + " us");
                    }
                    case FORKJOIN -> {
                        ForkJoinMergeSort task = new ForkJoinMergeSort(unsortedArray);
                        long start = System.nanoTime();
                        forkJoinPool.invoke(task);
                        long finish = System.nanoTime();
                        sortedArray = ArrayUtils.getArrayString(unsortedArray);
                        elapsedTime = finish - start;
                        forkJoinPool.close();
                        elapsedTimeForkJoin.setText("Fork: " + formatter.format(elapsedTime / 1000) + " us");
                    }
                    case EXECUTE -> {
                        ExecutorService executor = Executors.newWorkStealingPool();
                        ExecutorMergeSort executorMergeSort = new ExecutorMergeSort(unsortedArray, executor);
                        long start = System.nanoTime();
                        Future<int[]> future = executor.submit(executorMergeSort);
                        try {
                            unsortedArray = future.get();
                        } catch (InterruptedException | ExecutionException ex) {
                            throw new RuntimeException(ex);
                        }
                        long finish = System.nanoTime();
                        elapsedTime = finish - start;
                        executor.shutdown();
                        sortedArray = ArrayUtils.getArrayString(unsortedArray);
                        elapsedTimeExecute.setText("Execute: " + formatter.format(elapsedTime / 1000) + " us");
                    }
                }
                //sortedTextPane.setText(sortedArray);
            }
        });
    }

    private void selectSortingAlgorithm(SortingAlgorithm selected) {
        this.sortingAlgorithm = selected;
        switch (sortingAlgorithm) {
            case NONE -> selectedAlgorithmLabel.setText("<html>Algoritmo<br>selecciónado:<br>Ninguno</html>");
            case MERGESORT -> selectedAlgorithmLabel.setText("<html>Algoritmo<br>selecciónado:<br>MergeSort</html>");
            case FORKJOIN -> selectedAlgorithmLabel.setText("<html>Algoritmo<br>selecciónado:<br>ForkJoin</html>");
            case EXECUTE -> selectedAlgorithmLabel.setText("<html>Algoritmo<br>selecciónado:<br>Execute</html>");
        }
    }
}
