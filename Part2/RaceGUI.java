import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class RaceGUI extends JFrame {
    private JPanel welcomePanel;
    private JPanel horsePanel;
    private JPanel controlPanel;
    private JPanel raceOutputPanel;
    private JPanel options;
    private JButton startButton;
    private JTextArea outputRaceArea;

    private Race race;
    private ArrayList<Horse> horses;

    public RaceGUI(Race race) {
        this.race = race;
        this.horses = race.getHorses();

        // Set up the frame
        setTitle("Horse Race Simulator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Horse Sim welcome panel
        welcomePanel = new JPanel();
        welcomePanel.setBackground(new Color(29, 112, 180)); // Dark Blue
        welcomePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel welcomeLabel = new JLabel("Welcome to the Horse Racing Simulator!");
        welcomeLabel.setForeground(Color.WHITE); // White text
        welcomePanel.add(welcomeLabel);

        // Set up the Horse Stats panel
        horsePanel = new JPanel();
        horsePanel.setBackground(new Color(243, 235, 233)); // Light Gray
        horsePanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        for (int i = 0; i < horses.size(); i++) {
            JLabel horseName = new JLabel(horses.get(i).getName());
            JLabel horseSymbol = new JLabel(String.valueOf(horses.get(i).getSymbol()));
            JLabel horseConfidence = new JLabel(String.valueOf(horses.get(i).getConfidence()));

            gbc.gridx = 0;
            gbc.gridy = i;
            horsePanel.add(horseName, gbc);

            gbc.gridx = 1;
            gbc.gridy = i;
            horsePanel.add(horseSymbol, gbc);

            gbc.gridx = 2;
            gbc.gridy = i;
            horsePanel.add(horseConfidence, gbc);
        }



        // Set up the control panel
        controlPanel = new JPanel();
        startButton = new JButton("Start Race");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    startButton.setEnabled(false);
                    race.startRace();
                    updateHorseLabels();
                    startButton.setEnabled(true);
                }).start();
            }
        });
        controlPanel.add(startButton);

        // Set up the Race Output Panel
        raceOutputPanel = new JPanel();
        raceOutputPanel.setBackground(new Color(243, 235, 233)); // Light Gray
        raceOutputPanel.setLayout(new BorderLayout());
        //text area size based on how many horses are there
        outputRaceArea = new JTextArea((horses.size() + 4), 30);
        outputRaceArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputRaceArea.setEditable(false);
        outputRaceArea.setBackground(new Color(224, 224, 224)); // Light Gray
        JScrollPane scrollPane = new JScrollPane(outputRaceArea);
        raceOutputPanel.add(scrollPane, BorderLayout.CENTER);

        // Redirect System.out to outputRaceArea
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // Convert the int to a byte array
                byte[] bytes = new byte[] {(byte) b};
                // Call the write method that you've already overridden
                write(bytes, 0, bytes.length);
            }

            @Override
            public void write(byte[] b) throws IOException {
                // Call the write method that you've already overridden
                write(b, 0, b.length);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                // Convert the byte array to a string
                String str = new String(b, off, len, StandardCharsets.UTF_8);
                // Append the string to the text area
                outputRaceArea.append(str);
                // Scroll the text area to the end of data
                outputRaceArea.setCaretPosition(outputRaceArea.getDocument().getLength());
            }
        }, true));

        // Design center panel
        // Create a new panel with BoxLayout
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Wrap horsePanel and controlPanel in a Box and add them to the centerPanel
        centerPanel.add(Box.createVerticalBox().add(horsePanel));
        centerPanel.add(Box.createVerticalBox().add(controlPanel));

        /* add menu functionality*/

        // Create a JMenuBar
        JMenuBar menuBar = new JMenuBar();

        // Create a new Edit menu
        JMenu editMenu = new JMenu("Edit");

        // Create a menu item for the Edit menu
        JMenuItem changeRaceLength = new JMenuItem("Edit Race Length");
        JMenuItem addHorse = new JMenuItem("Add horse");

        // Add action listener to the "Edit Race Length" menu item
        changeRaceLength.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Prompt the user to enter a new race length
                String input = JOptionPane.showInputDialog(RaceGUI.this, "Enter new race length:");
                try {
                    int newRaceLength = Integer.parseInt(input);
                    if (newRaceLength > 0 && newRaceLength < 100) {
                        // Update the race length
                        race.setRaceLength(newRaceLength);
                    } else {
                        JOptionPane.showMessageDialog(RaceGUI.this, "Invalid input. Race length must be in the range 0-100.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(RaceGUI.this, "Change to race length cancelled.");
                }
            }
        });

        // Add action listener to the "Add Horse" menu item
        addHorse.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Prompt the user to enter details for the new horse
                String name = JOptionPane.showInputDialog(RaceGUI.this, "Enter horse name:");
                char symbol = JOptionPane.showInputDialog(RaceGUI.this, "Enter horse symbol:").charAt(0);
                double confidence = 0.5;
                // Add the new horse to the race
                race.addHorse(new Horse(symbol, name, confidence), horses.size() + 1);
                // Rebuild the horsePanel to reflect the changes
                rebuildHorsePanelAndRaceText();
            }
        });


        // Add the "Race Length" menu item to the Edit menu
        editMenu.add(changeRaceLength);
        editMenu.add(addHorse);


        menuBar.add(editMenu);
        setJMenuBar(menuBar);


        // Add panels to the frame
        add(welcomePanel, BorderLayout.NORTH);
//        add(horsePanel, BorderLayout.CENTER);
//        add(controlPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        add(raceOutputPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateHorseLabels() {
        for (int i = 0; i < horses.size(); i++) {
            // Get the existing labels from the GUI
            Component[] components = horsePanel.getComponents();
            JLabel horseName = (JLabel) components[i * 3];
            JLabel horseSymbol = (JLabel) components[i * 3 + 1];
            JLabel horseConfidence = (JLabel) components[i * 3 + 2];

            // Update the labels
            horseName.setText(horses.get(i).getName());
            horseSymbol.setText(String.valueOf(horses.get(i).getSymbol()));
            //round confidence to 2 decimal places
            horseConfidence.setText(String.format("%.2f", (horses.get(i).getConfidence())));
        }
    }

    private void rebuildHorsePanelAndRaceText() {
        // Clear the existing components in the horsePanel
        horsePanel.removeAll();

        // Re-add the horse labels based on the updated list of horses
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        for (int i = 0; i < horses.size(); i++) {
            JLabel horseName = new JLabel(horses.get(i).getName());
            JLabel horseSymbol = new JLabel(String.valueOf(horses.get(i).getSymbol()));
            JLabel horseConfidence = new JLabel(String.valueOf(horses.get(i).getConfidence()));

            gbc.gridx = 0;
            gbc.gridy = i;
            horsePanel.add(horseName, gbc);

            gbc.gridx = 1;
            gbc.gridy = i;
            horsePanel.add(horseSymbol, gbc);

            gbc.gridx = 2;
            gbc.gridy = i;
            horsePanel.add(horseConfidence, gbc);
        }

        // Recalculate the preferred size of the text area based on the number of horses
        int numRows = horses.size() + 4; // Adjust based on your preference
        int numColumns = 30; // Adjust based on your preference
        outputRaceArea.setColumns(numColumns);
        outputRaceArea.setRows(numRows);

        // Revalidate and repaint the raceOutputPanel to reflect the changes
        raceOutputPanel.revalidate();
        raceOutputPanel.repaint();

        // Revalidate and repaint the horsePanel to reflect the changes
        horsePanel.revalidate();
        horsePanel.repaint();
    }


    public static void main(String[] args) {
        Race race = new Race(20); // Initialize the race
        race.addHorse(new Horse('A', "Horse A", 0.5), 1);
        race.addHorse(new Horse('B', "Horse B", 0.5), 2);
        race.addHorse(new Horse('C', "Horse C", 0.5), 3);
        SwingUtilities.invokeLater(() -> new RaceGUI(race));
    }
}
