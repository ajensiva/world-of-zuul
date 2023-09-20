/**
 * Class Beamer - a beamer device in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "Beamer" represents a beamer device which may be charged and fired. It 
 * memorizes the current room, and when you fire the beamer it transports you 
 * to the room it was charged in. 
 * 
 * @author Ajen Srisivapalan 101248498
 * @version A2 Solution
 */
public class Beamer extends Item
{
    private boolean charged;
    /**
     * Constructor for objects of class Beamer.
     * 
     * @param name The name of the beamer
     * @param description The description of the beamer
     * @param weight The weight of the beamer  
     * 
     */
    public Beamer(String name, String description, double weight)
    {
        super(name, description, weight);
        charged = false;
    }

    /**
     * Charges the beamer, making charged field true.
     */
    public void charge(){
        charged = true;
    }
    
    /**
     * Returns if the beamer is charged or not.
     * @return True if the beamer is charged, false if not.
     */
    public boolean isCharged() {
        return charged;
    }
    
    /**
     * Fires the beamer, making the charged field false.
     */
    public void fire() {
        charged = false;
    }
}