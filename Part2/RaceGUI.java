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
    //main panel
    private JPanel welcomePanel;
    private JPanel horsePanel;
    private JPanel controlPanel;
    private JPanel raceOutputPanel;
    private JPanel options;
    private JButton startButton;
    private JTextArea outputRaceArea;

    //statistics
    private JButton statsButton;

    //Betting System
    private JButton betButton;
    private int wins;
    private int losses;
    private int bankTotal = 10;
    private int betAmount;
    private ArrayList<Horse> selectedHorses = new ArrayList<>();
    private Horse winningHorse;


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
        gbc.insets = new Insets(5, 15, 5, 15);

        for (int i = 0; i < horses.size(); i++) {
            JLabel horseLane = new JLabel("Lane " + (i + 1));
            JLabel horseName = new JLabel("Horse Name: " + horses.get(i).getName());
            JLabel horseSymbol = new JLabel("Horse Symbol: " + horses.get(i).getSymbol());
            JLabel horseConfidence = new JLabel("Horse Confidence: " + horses.get(i).getConfidence());

            // Create the Edit Horse button for each horse
            JButton editButton = new JButton("Edit Horse");
            final int horseIndex = i;
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Create an array of options
                    Object[] options = {"Edit Horse Name", "Edit Horse Symbol"};
                    // Display a dialog with the options
                    int choice = JOptionPane.showOptionDialog(RaceGUI.this, "What would you like to edit?",
                            "Edit Horse", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    // Handle the user's choice
                    switch (choice) {
                        case 0: // Edit Horse Name
                            String newName = JOptionPane.showInputDialog(RaceGUI.this, "Enter new name for the horse:");
                            if (newName == null || newName.isEmpty()){
                                // User closed the dialog, do nothing
                                rebuildHorsePanelAndRaceText();
                                return;
                            }
                            // Update the horse's name
                            else {
                                Horse horse = horses.get(horseIndex);
                                horse.resetName(newName);
                                break;
                            }
                        case 1: // Edit Horse Symbol
                            String newSymbol = JOptionPane.showInputDialog(RaceGUI.this, "Enter new symbol for the horse:");
                            if (newSymbol == null) {
                                // User closed the dialog, do nothing
                                rebuildHorsePanelAndRaceText();
                                return;
                            }
                            if (newSymbol != null && newSymbol.length() == 1) {
                                // Update the horse's symbol
                                Horse horse = horses.get(horseIndex);
                                horse.setSymbol(newSymbol.charAt(0));
                            }
                            else {
                                JOptionPane.showMessageDialog(RaceGUI.this, "Invalid input. Please enter a single character for the horse symbol.");
                            }
                            break;
                    }
                    // Rebuild the horsePanel to reflect the changes
                    rebuildHorsePanelAndRaceText();
                }
            });

            gbc.gridx = 0;
            gbc.gridy = i;
            horsePanel.add(horseLane, gbc);

            gbc.gridx = 1;
            gbc.gridy = i;
            horsePanel.add(horseName, gbc);

            gbc.gridx = 2;
            gbc.gridy = i;
            horsePanel.add(horseSymbol, gbc);

            gbc.gridx = 3;
            gbc.gridy = i;
            horsePanel.add(horseConfidence, gbc);

            gbc.gridx = 4;
            gbc.gridy = i;
            horsePanel.add(editButton, gbc);
        }

        // Set up the control panel
        controlPanel = new JPanel();
        startButton = new JButton("Start Race");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    startButton.setEnabled(false);
                    betButton.setEnabled(false);
                    race.startRace();
                    updateHorseLabels();
                    startButton.setEnabled(true);

                    winningHorse = race.getWinningHorse();
                    if(!selectedHorses.isEmpty()) {
                        if (winningHorse == null) {
                            bankTotal -= betAmount;
                            JOptionPane.showMessageDialog(RaceGUI.this, "You lost "+ betAmount + " coins :(");
                        } else if (selectedHorses.contains(winningHorse)) {
                            bankTotal += betAmount;
                            JOptionPane.showMessageDialog(RaceGUI.this, "You won "+ betAmount + " coins!");
                        } else {
                            bankTotal -= betAmount;
                            JOptionPane.showMessageDialog(RaceGUI.this, "You lost "+ betAmount + " coins :(");
                        }
                    }
                    //resets bets
                    selectedHorses.clear();
                    betButton.setEnabled(true);

                }).start();
            }
        });

        statsButton = new JButton("View Statistics");
        statsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame statsFrame = new JFrame("Statistics");
                statsFrame.setSize(400, 400);
                statsFrame.setLayout(new BorderLayout());
                statsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JTextArea statsArea = new JTextArea();
                statsArea.setEditable(false);

                statsArea.setText("");
                ArrayList<String> raceResults = race.getRaceResults();
                for (String result : raceResults) {
                    statsArea.append(result);
                }
                statsFrame.add(new JScrollPane(statsArea), BorderLayout.CENTER);
                statsFrame.setVisible(true);
            }
        });

        betButton = new JButton("Place Bet");
        betButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                JFrame betFrame = new JFrame("Place Bet");
                betFrame.setSize(500, 400);
                betFrame.setLayout(new BorderLayout());
                betFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                // Title and win/loss count
                JLabel titleLabel = new JLabel("Place your bet!         Bank Total: "+ bankTotal);
                betFrame.add(titleLabel, BorderLayout.NORTH);

                // Race odds
                JTextArea betArea = new JTextArea();
                betArea.setEditable(false);

                //rough calculation for horse chance of winning
                double totalMoves = 0.0;
                double totalFalls = 0.0;
                for (Horse horse : horses) {
                    double averageMoves = race.getRaceLength() * horse.getConfidence();
                    double averageFalls = race.getRaceLength() * 0.1 * Math.pow(horse.getConfidence(), 2);
                    totalMoves += averageMoves;
                    totalFalls += averageFalls;
                }

                StringBuilder oddsText = new StringBuilder();
                for (Horse horse : horses) {
                    double averageMoves = race.getRaceLength() * horse.getConfidence();
                    double averageFalls = race.getRaceLength() * 0.1 * Math.pow(horse.getConfidence(), 2);
                    double winningChance = (averageMoves - averageFalls) / (totalMoves - totalFalls) * 100;
                    oddsText.append(horse.getName()).append(" winning chances: ").append(String.format("%.2f", winningChance)).append("% Total bets made so far: ").append(horse.getTotalBets()).append("\n");
                }

                // Set the text of betArea to the calculated odds
                betArea.setText(oddsText.toString());
                betFrame.add(new JScrollPane(betArea), BorderLayout.CENTER);

                // List of horses
                JPanel horsePanel = new JPanel(new GridLayout(horses.size(), 1));
                ButtonGroup group = new ButtonGroup();
                ArrayList<JRadioButton> radioButtons = new ArrayList<>();
                for (Horse horse : horses) {
                    JRadioButton radioButton = new JRadioButton(horse.getName());
                    radioButtons.add(radioButton);
                    group.add(radioButton);
                    horsePanel.add(radioButton);
                }
                betFrame.add(horsePanel, BorderLayout.WEST);

                // Submit button
                JButton submitButton = new JButton("Submit");
                submitButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String betAmountStr = JOptionPane.showInputDialog(RaceGUI.this, "Enter your bet amount (0-10):");
                        betAmount = 0;
                        try {
                            betAmount = Integer.parseInt(betAmountStr);
                            if (betAmount < 0 || betAmount > 10) {
                                JOptionPane.showMessageDialog(RaceGUI.this, "Invalid bet amount. It should be between 0 and 10.");
                                return;
                            }
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(RaceGUI.this, "Invalid input. Please enter a number between 0 and 10.");
                            return; // If the input is invalid, return from the action listener without creating the JFrame
                        }
                        // Store the selected horse
                        selectedHorses = new ArrayList<>();
                        for (int i = 0; i < radioButtons.size(); i++) {
                            if (radioButtons.get(i).isSelected()) {
                                Horse selectedHorse = horses.get(i);
                                selectedHorses.add(selectedHorse);
                                selectedHorse.increaseTotalBets();
                                break;
                            }
                        }
                        betButton.setEnabled(false);
                        // Display a "Good luck" message and close the betting frame
                        JOptionPane.showMessageDialog(betFrame, "Good luck!");
                        betFrame.dispose();


                    }
                });
                betFrame.add(submitButton, BorderLayout.SOUTH);

                betFrame.setVisible(true);
            }
        });

        controlPanel.add(startButton);
        controlPanel.add(statsButton);
        controlPanel.add(betButton);

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

        /* menu functionality*/

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
            JLabel horseLane = (JLabel) components[i * 5]; // Adjusted index
            JLabel horseName = (JLabel) components[i * 5 + 1]; // Adjusted index
            JLabel horseSymbol = (JLabel) components[i * 5 + 2]; // Adjusted index
            JLabel horseConfidence = (JLabel) components[i * 5 + 3]; // Adjusted index

            // Update the labels
            horseLane.setText("Lane " + (i + 1));
            horseName.setText("Horse Name: " + horses.get(i).getName());
            horseSymbol.setText("Horse Symbol: " + horses.get(i).getSymbol());
            //round confidence to 2 decimal places
            horseConfidence.setText("Horse Confidence: " + String.format("%.2f", horses.get(i).getConfidence()));
        }
    }

    private void rebuildHorsePanelAndRaceText() {
        // Clear the existing components in the horsePanel
        horsePanel.removeAll();

        // Re-add the horse labels based on the updated list of horses
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 15, 5, 15);

        for (int i = 0; i < horses.size(); i++) {
            JLabel horseLane = new JLabel("Lane " + (i + 1));
            JLabel horseName = new JLabel("Horse Name: " + horses.get(i).getName());
            JLabel horseSymbol = new JLabel("Horse Symbol: " + horses.get(i).getSymbol());
            JLabel horseConfidence = new JLabel("Horse Confidence: " + horses.get(i).getConfidence());

            // Create the Edit Horse button for each horse
            JButton editButton = new JButton("Edit Horse");
            final int horseIndex = i;
            editButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Create an array of options
                    Object[] options = {"Edit Horse Name", "Edit Horse Symbol"};
                    // Display a dialog with the options
                    int choice = JOptionPane.showOptionDialog(RaceGUI.this, "What would you like to edit?",
                            "Edit Horse", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    // Handle the user's choice
                    switch (choice) {
                        case 0: // Edit Horse Name
                            String newName = JOptionPane.showInputDialog(RaceGUI.this, "Enter new name for the horse:");
                            if (newName == null || newName.isEmpty()){
                                // User closed the dialog, do nothing
                                return;
                            }
                            // Update the horse's name
                            else {
                                Horse horse = horses.get(horseIndex);
                                horse.resetName(newName);
                                break;
                            }
                        case 1: // Edit Horse Symbol
                            String newSymbol = JOptionPane.showInputDialog(RaceGUI.this, "Enter new symbol for the horse:");
                            if (newSymbol == null) {
                                // User closed the dialog, do nothing
                                return;
                            }
                            if (newSymbol != null && newSymbol.length() == 1) {
                                // Update the horse's symbol
                                Horse horse = horses.get(horseIndex);
                                horse.setSymbol(newSymbol.charAt(0));
                            }
                            else {
                                JOptionPane.showMessageDialog(RaceGUI.this, "Invalid input. Please enter a single character for the horse symbol.");
                            }
                            break;
                    }
                    // Rebuild the horsePanel to reflect the changes
                    rebuildHorsePanelAndRaceText();
                }
            });

            gbc.gridx = 0;
            gbc.gridy = i;
            horsePanel.add(horseLane, gbc);

            gbc.gridx = 1;
            gbc.gridy = i;
            horsePanel.add(horseName, gbc);

            gbc.gridx = 2;
            gbc.gridy = i;
            horsePanel.add(horseSymbol, gbc);

            gbc.gridx = 3;
            gbc.gridy = i;
            horsePanel.add(horseConfidence, gbc);

            gbc.gridx = 4;
            gbc.gridy = i;
            horsePanel.add(editButton, gbc);
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
