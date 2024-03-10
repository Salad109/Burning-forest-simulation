import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BurningForestSimulation extends JFrame {
    static int size;
    static float forestation = 0.75f;
    static char[][] map;
    JPanel gridPanel;
    JLabel valueLabel;
    static int burnt, total;
    static double dead;
    boolean progress;

    public void forest_initialization() {
        size = 100;
        burnt = 0;
        total = 0;
        dead = 0;
        map = new char[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                map[i][j] = randomizerXT(forestation);
            }
        }
    }

    public static char randomizerXT(float forestation) {
        if (Math.random() < forestation) {
            total += 1;
            return 'T';
        } else return 'X';
    }

    public void map_initialization() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Burning forest simulation");

        gridPanel = new JPanel(new GridLayout(size, size));

        add(gridPanel);

        JButton fireButton = new JButton("Start the Fire");
        //noinspection Convert2Lambda
        fireButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fire_initialization();
                print_map();
            }
        });

        JButton randomizeButton = new JButton("Randomize map");
        //noinspection Convert2Lambda
        randomizeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                forest_initialization();
                print_map();
            }
        });

        JButton restartButton = new JButton("Randomize & Burn");
        //noinspection Convert2Lambda
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                forest_initialization();
                fire_initialization();
            }
        });

        valueLabel = new JLabel();
        valueLabel.setText("Size: " + size + "x" + size + " | Forestation: " + Math.round(forestation * 100.0 * 100.0) / 100.0 + "% | Total: " + total + " | Burnt: " + burnt + " or " + dead + "%");


        JTextField forestationInput = new JTextField();
        forestationInput.setPreferredSize(new Dimension(100, 30));
        JButton forestationButton = new JButton("Set forestation percentage");
        //noinspection Convert2Lambda
        forestationButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                forestation = Float.parseFloat(forestationInput.getText()) / 100.0F;
                valueLabel.setText("Size: " + size + "x" + size + " | Forestation: " + Math.round(forestation * 100.0 * 100.0) / 100.0 + "% | Total: " + total + " | Burnt: " + burnt + " or " + dead + "%");
            }
        });

        Box superbox = Box.createVerticalBox();
        Box buttonbox = Box.createHorizontalBox();
        Box forestationbox = Box.createHorizontalBox();

        buttonbox.add(fireButton);
        buttonbox.add(randomizeButton);
        buttonbox.add(restartButton);
        buttonbox.add(valueLabel);

        forestationbox.add(forestationButton);
        forestationbox.add(forestationInput);

        superbox.add(buttonbox);
        superbox.add(forestationbox);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(superbox);

        add(buttonPanel, BorderLayout.SOUTH);

        print_map();

        setSize(1500, 1000);
        setLocationRelativeTo(null);
        revalidate();
        repaint();
        setVisible(true);
    }

    public void print_map() {
        // Clear the gridPanel before updating
        gridPanel.removeAll();

        // Populate the grid with JLabels displaying characters
        //noinspection ForLoopReplaceableByForEach
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                JLabel label = new JLabel(String.valueOf(map[i][j]), SwingConstants.CENTER);
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                if (map[i][j] == 'X') label.setBackground(Color.white);
                else if (map[i][j] == 'T') label.setBackground(Color.green);
                else if (map[i][j] == 'B') label.setBackground(Color.red);

                label.setOpaque(true);

                gridPanel.add(label);
            }
        }

        try {
            dead = ((double) burnt / (double) total);
            dead *= 100;
            dead = Math.round(dead * 100.0) / 100.0;
        } catch (ArithmeticException e) {
            dead = 100;
        }
        valueLabel.setText("Size: " + size + "x" + size + " | Forestation: " + Math.round(forestation * 100.0 * 100.0) / 100.0 + "% | Total: " + total + " | Burnt: " + burnt + " or " + dead + "%");


        // Refresh the JFrame
        revalidate();
        repaint();
    }

    public void fire_initialization() {
        for (int i = 0; i < map.length; i++) {
            if (map[0][i] == 'T') {
                map[0][i] = 'B';
                burnt += 1;
            }
        }
        fire_simulation();
    }

    public void fire_simulation() {
        progress = true;
        while (progress) {
            progress = false;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    spread_fire(i, j);
                }
            }
        }
        print_map();
    }

    public void spread_fire(int row, int column) {
        if (map[row][column] == 'B') {
            // Above
            if (row - 1 >= 0) {
                if (map[row - 1][column] == 'T') {
                    map[row - 1][column] = 'B';
                    progress = true;
                    burnt += 1;
                }
            }
            // Below
            if (row + 1 < size) {
                if (map[row + 1][column] == 'T') {
                    map[row + 1][column] = 'B';
                    progress = true;
                    burnt += 1;
                }
            }
            // Left
            if (column - 1 >= 0) {
                if (map[row][column - 1] == 'T') {
                    map[row][column - 1] = 'B';
                    progress = true;
                    burnt += 1;
                }
            }
            // Right
            if (column + 1 < size) {
                if (map[row][column + 1] == 'T') {
                    map[row][column + 1] = 'B';
                    progress = true;
                    burnt += 1;
                }
            }
        }
    }

    public static void main(String[] args) {
        BurningForestSimulation simulation = new BurningForestSimulation();
        simulation.forest_initialization();
        simulation.map_initialization();
    }
}