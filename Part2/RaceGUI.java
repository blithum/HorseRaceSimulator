import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class RaceGUI extends JFrame {
    private JPanel welcomePanel;
    private JPanel raceTrackPanel;
    private JPanel controlPanel;
    private JButton startButton;
    private JLabel[] horseLabels;
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

        // Set up the race track panel
        raceTrackPanel = new JPanel();
        raceTrackPanel.setBackground(new Color(243, 235, 233)); // Light Gray
        raceTrackPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        for (int i = 0; i < horses.size(); i++) {
            JLabel horseName = new JLabel(horses.get(i).getName());
            JLabel horseSymbol = new JLabel(String.valueOf(horses.get(i).getSymbol()));
            JLabel horseConfidence = new JLabel(String.valueOf(horses.get(i).getConfidence()));

            gbc.gridx = 0;
            gbc.gridy = i;
            raceTrackPanel.add(horseName, gbc);

            gbc.gridx = 1;
            gbc.gridy = i;
            raceTrackPanel.add(horseSymbol, gbc);

            gbc.gridx = 2;
            gbc.gridy = i;
            raceTrackPanel.add(horseConfidence, gbc);
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
        controlPanel.add(startButton);

        // Add panels to the frame
        add(welcomePanel, BorderLayout.NORTH);
        add(raceTrackPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

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
