public class RaceTest {
    public static void main(String[] param){
        Horse horse1 = new Horse('1', "Horse1", 0.5);
        Horse horse2 = new Horse('2', "Horse2", 0.5);
        Horse horse3 = new Horse('3', "Horse3", 0.5);
        Race race = new Race(20);

        race.addHorse(horse1, 1);
        race.addHorse(horse2, 2);
        race.addHorse(horse3, 3);

        race.startRace();
    }
}
