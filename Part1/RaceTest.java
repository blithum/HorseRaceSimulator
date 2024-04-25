import java.util.Scanner;

public class RaceTest {
    public static void main(String[] param){
        Horse horse1 = new Horse('1', "Horse1", 0.5);
        Horse horse2 = new Horse('2', "Horse2", 0.5);
        Horse horse3 = new Horse('3', "Horse3", 0.5);
        Race race = new Race(1);

        race.addHorse(horse1, 1);
        race.addHorse(horse2, 2);
        race.addHorse(horse3, 3);

        String runAgain = "y";
        Scanner scanner = new Scanner(System.in);

        while(runAgain.equals("y")) {
            race.startRace();

            printHorseDetails(horse1);
            printHorseDetails(horse2);
            printHorseDetails(horse3);

            System.out.println("Run again? (y/n)");
            runAgain = scanner.nextLine();

        }
    }

    public static void printHorseDetails(Horse horse){
        System.out.println(horse.getName() + " details:\n");
        HorseTest.printHorseDetails(horse);
    }

    public static void testHorseConfidence(){
        Horse horse = new Horse('1', "TestHorse", 0.05);

        if(!(horse.getConfidence() - 0.05 < 0.05)) {
            horse.setConfidence(horse.getConfidence() - 0.05);
        }
        else{
            horse.setConfidence(0.05);
        }
        System.out.println(horse.getName() + " confidence: " + horse.getConfidence());
    }

    public static void testHorseFall(){
        Horse horse = new Horse('1', "TestHorse", 1);
        horse.moveForward();

        if ((Math.random() < (0.1*horse.getConfidence()*horse.getConfidence())) && horse.getDistanceTravelled() != 1)
        {
            horse.fall();
        }

        System.out.println(horse.getName() + " has fallen: " + horse.hasFallen());
    }
}
