import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.util.concurrent.ForkJoinPool;

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

    private JLabel elapsedTimeLabel;

    private long elapsedTime = 0;

    private SortingAlgorithm sortingAlgorithm = SortingAlgorithm.NONE;
    public View(){
        panel = new Panel();
        panel.setBackground(Color.DARK_GRAY);
        this.setContentPane(panel);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setTitle("Algoritmos de ordenamiento");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(600, 410);

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

        elapsedTimeLabel = new JLabel("");
        elapsedTimeLabel.setBounds(150, 175, 400, 25);
        elapsedTimeLabel.setForeground(Color.RED);

        scrollPane2 = new JScrollPane(sortedTextPane);
        scrollPane2.setBounds(150, 200, 400, 150);

        panel.add(mergeButton);
        panel.add(arraySizeTextField);
        panel.add(forkJoinButton);
        panel.add(executeButton);
        panel.add(scrollPane1);
        panel.add(scrollPane2);
        panel.add(startButton);
        panel.add(clearButton);
        panel.add(selectedAlgorithmLabel);
        panel.add(elapsedTimeLabel);

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
                elapsedTimeLabel.setText("");
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(arraySizeTextField.getText().isEmpty()) {
                    elapsedTimeLabel.setText("Introduzca un tamaño valido!");
                    return;
                }
                int arrayLength = Integer.parseInt(arraySizeTextField.getText());
                int[] unsortedArray = ArrayUtils.createRandomArray(arrayLength);
                unsortedTextPane.setText(ArrayUtils.getArrayString(unsortedArray));
                String sortedArray = "";
                switch (sortingAlgorithm) {
                    case NONE -> {
                        elapsedTimeLabel.setText("Seleccione un algoritmo!");
                        elapsedTime = 0;
                        return;
                    }
                    case MERGESORT -> {
                        long start = System.nanoTime();
                        int[] arr = MergeSort.doMergeSort(unsortedArray);
                        long finish = System.nanoTime();
                        sortedArray = ArrayUtils.getArrayString(arr);
                        elapsedTime = finish - start;
                    }
                    case FORKJOIN -> {
                        ForkJoinPool forkJoinPool = new ForkJoinPool();
                        ForkJoinMergeSort task = new ForkJoinMergeSort(unsortedArray);
                        long start = System.nanoTime();
                        int[] arr = forkJoinPool.invoke(task);
                        long finish = System.nanoTime();
                        sortedArray = ArrayUtils.getArrayString(arr);
                        elapsedTime = finish - start;
                        forkJoinPool.close();

                    }
                    case EXECUTE -> {
                        // TODO Implementar algoritmo Execute
                        sortedArray = "Algoritmo no implementado";
                        elapsedTime = 0;
                    }
                }

                DecimalFormat formatter = new DecimalFormat("#,###");
                elapsedTimeLabel.setText("Tiempo: " + formatter.format(elapsedTime / 1000000) + " milisegundos");
                sortedTextPane.setText(sortedArray);
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
