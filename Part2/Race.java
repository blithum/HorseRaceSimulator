import java.util.concurrent.TimeUnit;
import java.lang.Math;
import java.util.ArrayList;

/**
 * A three-horse race, each horse running in its own lane
 * for a given distance
 * 
 * @author McFarewell
 * @version 1.0
 */
public class Race
{
    private int raceLength;
    private ArrayList<Horse> horses;

    /**
     * Constructor for objects of class Race
     * Initially there are no horses in the lanes
     * 
     * @param distance the length of the racetrack (in metres/yards...)
     */
    public Race(int distance)
    {
        // initialise instance variables
        raceLength = distance;
        horses = new ArrayList<>();
    }
    
    /**
     * Adds a horse to the race in a given lane
     * 
     * @param theHorse the horse to be added to the race
     * @param laneNumber the lane that the horse will be added to
     */
    public void addHorse(Horse theHorse, int laneNumber)
    {
        horses.add(theHorse);
    }

    //Method to get the horses
    public ArrayList<Horse> getHorses() {
        return this.horses;
    }

    //change race length
    public void setRaceLength(int newLength)
    {
        if (newLength > 0) {
            raceLength = newLength;
        }
    }
    /**
     * Start the race
     * The horse are brought to the start and
     * then repeatedly moved forward until the 
     * race is finished
     */
    public void startRace()
    {
        //declare a local variable to tell us when the race is finished
        boolean finished = false;
        boolean allHorsesFallen = false;
        int winCount = 0;

        //reset all the lanes (all horses not fallen and back to 0). 
        for (Horse horse : horses) {
            horse.goBackToStart();
            horse.standUp();
        }

        while (!finished && !allHorsesFallen)
        {
            //move each horse
            for (Horse horse : horses) {
                moveHorse(horse);
            }
                        
            //print the race positions
            printRace();

            //check for a tie
            for (Horse horse : horses) {
                if (raceWonBy(horse)){
                    winCount++;
                }
            }
            if (winCount > 1)
            {
                finished = true;
                System.out.println("It's a tie!");
            }
            //if any of the three horses has won announce the winner
            else {
                for (Horse horse : horses) {
                    if (raceWonBy(horse)) {
                        finished = true;
                        if(!(horse.getConfidence() + 0.05 > 0.95)) {
                            horse.setConfidence(horse.getConfidence() + 0.05);
                        }
                        else{
                            horse.setConfidence(0.95);
                        }
                        System.out.println(horse.getName() + " wins!");
                    }

                }
            }

            //checks if all horses have fallen
            allHorsesFallen = true;
            for (Horse horse : horses) {
                if (!horse.hasFallen()) {
                    allHorsesFallen = false;
                }
            }
            if (allHorsesFallen){
                System.out.println("All horses have fallen!");
            }

            //wait for 100 milliseconds
            try{ 
                TimeUnit.MILLISECONDS.sleep(100);
            }catch(Exception e){}
        }
    }
    
    /**
     * Randomly make a horse move forward or fall depending
     * on its confidence rating
     * A fallen horse cannot move
     * 
     * @param theHorse the horse to be moved
     */
    private void moveHorse(Horse theHorse)
    {
        //if the horse has fallen it cannot move, 
        //so only run if it has not fallen
        
        if  (!theHorse.hasFallen())
        {
            //the probability that the horse will move forward depends on the confidence;
            if (Math.random() < theHorse.getConfidence())
            {
               theHorse.moveForward();
            }
            
            //the probability that the horse will fall is very small (max is 0.1)
            //but will also will depends exponentially on confidence 
            //so if you double the confidence, the probability that it will fall is *2
            if ((Math.random() < (0.1*theHorse.getConfidence()*theHorse.getConfidence())) && !raceWonBy(theHorse))
            {
                theHorse.fall();
                if(!(theHorse.getConfidence() - 0.05 < 0.05)) {
                    theHorse.setConfidence(theHorse.getConfidence() - 0.05);
                }
                else{
                    theHorse.setConfidence(0.05);
                }
            }
        }
    }
        
    /** 
     * Determines if a horse has won the race
     *
     * @param theHorse The horse we are testing
     * @return true if the horse has won, false otherwise.
     */
    private boolean raceWonBy(Horse theHorse)
    {
        if (theHorse.getDistanceTravelled() == raceLength)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /***
     * Print the race on the terminal
     */
    private void printRace()
    {
        System.out.print('\u000C');  //clear the terminal window
        
        multiplePrint('=',raceLength+3); //top edge of track
        System.out.println();

       //print a lane for each horse
        for (Horse horse : horses) {
            printLane(horse);
            System.out.println();
        }
        
        multiplePrint('=',raceLength+3); //bottom edge of track
        System.out.println();    
    }
    
    /**
     * print a horse's lane during the race
     * for example
     * |           X                      |
     * to show how far the horse has run
     */
    private void printLane(Horse theHorse)
    {
        //calculate how many spaces are needed before
        //and after the horse
        int spacesBefore = theHorse.getDistanceTravelled();
        int spacesAfter = raceLength - theHorse.getDistanceTravelled();

        //print a | for the beginning of the lane
        System.out.print('|');
        
        //print the spaces before the horse
        multiplePrint(' ',spacesBefore);
        
        //if the horse has fallen then print dead
        //else print the horse's symbol
        if(theHorse.hasFallen())
        {
            System.out.print('\u2716');
        }
        else
        {
            System.out.print(theHorse.getSymbol());
        }
        
        //print the spaces after the horse
        multiplePrint(' ',spacesAfter);
        
        //print the | for the end of the track
        System.out.print('|');
    }
        
    
    /***
     * print a character a given number of times.
     * e.g. printmany('x',5) will print: xxxxx
     * 
     * @param aChar the character to Print
     */
    private void multiplePrint(char aChar, int times)
    {
        int i = 0;
        while (i < times)
        {
            System.out.print(aChar);
            i = i + 1;
        }
    }
}
