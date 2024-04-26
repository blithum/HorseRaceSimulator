import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

public class RaceGUI extends JFrame {
    private JPanel welcomePanel;
    private JPanel horsePanel;
    private JPanel controlPanel;
    private JPanel raceOutputPanel;
    private JButton startButton;
    private JLabel[] horseLabels;
    private JTextArea outputRaceArea;

    private Race race;
    private ArrayList<Horse> horses;

    public RaceGUI(Race race) {
        this.race = race;
        this.horses = race.getHorses();
        this.horseLabels = new JLabel[race.getHorses().size()];

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

        // Set up the racetrack panel
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
        startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> {
                    startButton.setEnabled(false);
                    race.startRace();
                    startButton.setEnabled(true);
                }).start();
            }
        });
        horsePanel.add(startButton);

        // Set up the Race Output Panel
        raceOutputPanel = new JPanel();
        raceOutputPanel.setBackground(new Color(243, 235, 233)); // Light Gray
        raceOutputPanel.setLayout(new BorderLayout());
        outputRaceArea = new JTextArea(10, 30);
        outputRaceArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputRaceArea.setEditable(false);
        outputRaceArea.setBackground(new Color(224, 224, 224)); // Light Gray
        JScrollPane scrollPane = new JScrollPane(outputRaceArea);
        raceOutputPanel.add(scrollPane, BorderLayout.CENTER);

        // Redirect System.out to outputRaceArea
        System.setOut(new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // redirects data to the text area
                outputRaceArea.append(String.valueOf((char)b));
                // scrolls the text area to the end of data
                outputRaceArea.setCaretPosition(outputRaceArea.getDocument().getLength());
            }
        }, true));

        // Create a new panel with BoxLayout
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        // Add horsePanel and controlPanel to the new panel
        centerPanel.add(horsePanel);
        centerPanel.add(controlPanel);

        // Add panels to the frame
        add(welcomePanel, BorderLayout.NORTH);
//        add(horsePanel, BorderLayout.CENTER);
//        add(controlPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        add(raceOutputPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateRaceTrack() {
        for (int i = 0; i < race.getHorses().size(); i++) {
            Horse horse = race.getHorses().get(i);
            horseLabels[i].setText(" ".repeat(horse.getDistanceTravelled()) + horse.getName());
        }
    }

    public static void main(String[] args) {
        Race race = new Race(20); // Initialize the race
        race.addHorse(new Horse('A', "Horse A", 0.8), 1);
        race.addHorse(new Horse('B', "Horse B", 0.7), 2);
        race.addHorse(new Horse('C', "Horse C", 0.9), 3);
        SwingUtilities.invokeLater(() -> new RaceGUI(race));
    }
}
