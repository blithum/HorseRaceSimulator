
/**
 * Write a description of class Horse here.
 * 
 * @author Lloyd Alexander Walker
 * @version 0.1 04/04/2024
 */
public class Horse
{
    //Fields of class Horse
    String name;
    char symbol;
    int distanceTravelled;
    boolean fallen;
    double confidence;
    
      
    //Constructor of class Horse
    /**
     * Constructor for objects of class Horse
     */

    public Horse(char horseSymbol, String horseName, double horseConfidence)
    {
        this.name = horseName;
        this.symbol = horseSymbol;
        this.confidence = horseConfidence;
    }
    
    
    
    //Other methods of class Horse
    public void fall()
    {
        fallen = true;
    }
    
    public double getConfidence()
    {
        return confidence;
    }
    
    public int getDistanceTravelled()
    {
        return distanceTravelled;
    }
    
    public String getName()
    {
        return name;
    }
    
    public char getSymbol()
    {
        return symbol;
    }
    
    public void goBackToStart()
    {
        distanceTravelled = 0;
    }
    
    public boolean hasFallen()
    {
        return fallen;
    }

    public void moveForward()
    {
        distanceTravelled++;
    }

    public void setConfidence(double newConfidence)
    {
        confidence = newConfidence;
    }
    
    public void setSymbol(char newSymbol)
    {
        symbol = newSymbol;
    }
    
}
