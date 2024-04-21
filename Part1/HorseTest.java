public class HorseTest {
    public static void main(String[] param){
        Horse horse1 = new Horse('1', "Horse1", 0.5);

        //prints horse details:
        System.out.println("horse1 details:\n");
        printHorseDetails(horse1);

        //Horse1 mutator methods
        System.out.println("\nupdate horse1 details:\n");
        horse1.fall();
        horse1.moveForward();
        horse1.setConfidence(0.8);
        horse1.setSymbol('Z');

        printHorseDetails(horse1);

        //reset to start of race
        horse1.goBackToStart();
        System.out.println("\nreset horse's distance to " + horse1.getDistanceTravelled());
    }
    public static void printHorseDetails(Horse horse1){
        //Horse1 accessor methods
        System.out.println("Name: " + horse1.getName());
        System.out.println("Symbol: " + horse1.getSymbol());
        System.out.println("Confidence: " + horse1.getConfidence());
        System.out.println("Distance Travelled: " + horse1.getDistanceTravelled());
        System.out.println("Horse has fallen: " + horse1.hasFallen());
    }
}
