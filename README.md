# Horse Racing Simulator

## Overview

The Horse Racing Simulator is a Java application that simulates a horse race between multiple horses running in their own lanes. The simulator includes a graphical user interface (GUI) built using Swing, allowing users to interact with the race, view horse statistics, and place bets on the outcome of the race.

## Features

- **Race Simulation**: Simulate a horse race with multiple horses running in their own lanes.
- **Graphical User Interface**: Interactive GUI for controlling the race, viewing horse details, and placing bets.
- **Horse Information**: Display information about each horse, including its name, symbol, and confidence level.
- **Race Output Display**: View the progress of each horse during the race in real-time.
- **Race Control**: Start the race, view statistics, and place bets using intuitive buttons and menus.
- **Betting System**: Allow users to place bets on horses before starting the race, with odds calculated based on horse confidence levels.

## Usage

### Prerequisites

- Java Development Kit (JDK) installed on your system.
- Basic understanding of Java programming and Swing GUI framework.

# Directory Structure

- `Race.java`: Main class representing the horse race simulation.
- `Horse.java`: Class representing a horse participating in the race.
- `RaceGUI.java`: Class representing the GUI for the horse racing simulator.

## Public Methods

### Horse Class

- `Horse(char symbol, String name, double confidence)`: Constructor for initializing a horse with its symbol, name, and confidence level.
    - `symbol`: The symbol representing the horse.
    - `name`: The name of the horse.
    - `confidence`: The confidence level of the horse in winning the race.

- `char getSymbol()`: Returns the symbol representing the horse.

- `String getName()`: Returns the name of the horse.

- `double getConfidence()`: Returns the confidence level of the horse in winning the race.

- `void setSymbol(char symbol)`: Sets the symbol representing the horse.

- `void resetName(String newName)`: Resets the name of the horse.

- `void increaseTotalBets()`: Increases the total bets made on the horse.
### Race Class

- `Race(int distance)`: Constructor for initializing the race with a given distance.
    - `distance`: The length of the racetrack (in meters, yards, etc.).

- `void addHorse(Horse theHorse, int laneNumber)`: Adds a horse to the race in a given lane.
    - `theHorse`: The horse to be added to the race.
    - `laneNumber`: The lane that the horse will be added to.

- `void setRaceLength(int newLength)`: Changes the length of the race.
    - `newLength`: The new length of the race (in meters, yards, etc.).

- `ArrayList<Horse> getHorses()`: Returns the list of horses participating in the race.

- `ArrayList<String> getRaceResults()`: Returns the results of the race, including race number, time, and other details.

- `Horse getWinningHorse()`: Returns the winning horse of the race.

- `void startRace()`: Starts the race simulation.

### RaceGUI Class

- `RaceGUI(Race race)`: Constructor for creating the GUI interface for the horse racing simulator.
    - `race`: The instance of the Race class representing the race simulation.

- `private void updateHorseLabels()`: Updates the labels displaying information about each horse in the GUI.

- `private void rebuildHorsePanelAndRaceText()`: Rebuilds the horse panel and race text area in the GUI to reflect any changes.

- `public static void main(String[] args)`: Main method to launch the GUI for the horse racing simulator.


