import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class TransporterRoom - a transporter room in an adventure game.
 *
 * This class is part of the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * A "TransporterRoom" represents one location in the scenery of the game. Whenever the player leaves this room by typing the "go" command, he
 * or she is randomly transported into one of the other rooms in the game.
 * 
 * @author  Ajen Srisivapalan 101248498 
 * @version A2 Solution
 */

public class TransporterRoom extends Room
{
    private Random random = new Random();
    /**
     * Create a Transporter Room described "description".
     * 
     * @param description The Transporter room's description.
     */
    public TransporterRoom(String description)
    {
        super(description);
    } 
    
    /**
    * Returns a random room, independent of the direction parameter.
    *
    * @param direction Ignored.
    * @return A randomly selected room.
    */
    public Room getExit(String direction)
    {
        return findRandomRoom();
    }
    
    /**
    * Choose a random room.
    *
    * @return The room we end up in upon leaving this one.
    */
    private Room findRandomRoom()
    {
        int x = random.nextInt(rooms.size());
        return rooms.get(x);
    }
}