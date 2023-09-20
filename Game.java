import java.util.Stack;

/**
 *  This class is the main class of the "World of Zuul" application. 
 *  "World of Zuul" is a very simple, text based adventure game.  Users 
 *  can walk around some scenery. That's all. It should really be extended 
 *  to make it more interesting!
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kolling and David J. Barnes 
 * @version 2006.03.30
 * 
 * @author Lynn Marshall
 * @version A1 Solution
 * 
 * @author Ajen Srisivapalan 101248498
 * @version A2 Solution
 */

public class Game 
{
    private Parser parser;
    private Room currentRoom;
    private Room previousRoom;
    private Stack<Room> previousRoomStack;
    private Item heldItem;  
    private int hungerLevel;    
    private Room beamerRoom;
    /**
     * Create the game and initialise its internal map, as well
     * as the previous room (none) and previous room stack (empty) and initial
     * hunger level.    
     */
    public Game() 
    {
        createRooms();
        parser = new Parser();
        previousRoom = null;
        previousRoomStack = new Stack<Room>();
        hungerLevel = 0;
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        Room outside, theatre, pub, lab, office, transporterRoom;
        Item chair1, chair2, chair3, chair4, bar, computer1, computer2, computer3, tree1, tree2, cookie, beamer1, beamer2;
        
        // create some items
        chair1 = new Item("chair","a wooden chair",5);
        chair2 = new Item("chair","a wooden chair",5);
        chair3 = new Item("chair","a wooden chair",5);
        chair4 = new Item("chair","a wooden chair",5);
        bar = new Item("bar","a long bar with stools",95.67);
        computer1 = new Item("PC","a PC",10);
        computer2 = new Item("Mac","a Mac",5);
        computer3 = new Item("PC","a PC",10);
        tree1 = new Item("tree","a fir tree",500.5);
        tree2 = new Item("tree","a fir tree",500.5);
        cookie = new Item("cookie", "a cookie", 0.5);
        beamer1 = new Beamer("beamer", "a beamer", 3.0);
        beamer2 = new Beamer("beamer", "a beamer", 3.0);
       
        // create the rooms
        outside = new Room("outside the main entrance of the university");
        theatre = new Room("in a lecture theatre");
        pub = new Room("in the campus pub");
        lab = new Room("in a computing lab");
        office = new Room("in the computing admin office");
        transporterRoom = new TransporterRoom("in the wavy transporter room");
        
        // put items in the rooms
        outside.addItem(tree1);
        outside.addItem(tree2);
        outside.addItem(cookie);
        outside.addItem(beamer1);
        theatre.addItem(chair1);
        pub.addItem(bar);
        lab.addItem(chair2);
        lab.addItem(computer1);
        lab.addItem(chair3);
        lab.addItem(computer2);
        office.addItem(chair4);
        office.addItem(computer3);
        transporterRoom.addItem(beamer2);
        
        // initialise room exits
        outside.setExit("east", theatre); 
        outside.setExit("south", lab);
        outside.setExit("west", pub);
        outside.setExit("north",transporterRoom);

        theatre.setExit("west", outside);

        pub.setExit("east", outside);

        lab.setExit("north", outside);
        lab.setExit("east", office);

        office.setExit("west", lab);
        
        transporterRoom.setExit("anywhere", outside);

        currentRoom = outside;  // start game outside
    }

    /**
     *  Main play routine.  Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
                
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
        System.out.println(getHeldItemString());
    }
    
    /**
     * Return a string describing if the player is carrying something, if so, 
     * what item with decscription. 
     * "You are carrying: a fir tree that weighs 50.0kg".
     * 
     * @return Details of carried item
     */
    private String getHeldItemString()
    {
        if (heldItem == null) { 
            return "You are not carrying anything.";
        }
        else {
            return "You are carrying: " + "\n    " + heldItem.getDescription();
        }
    }

    /**
     * Given a command, process (that is: execute) the command.
     * 
     * @param command The command to be processed
     * @return true If the command ends the game, false otherwise
     */
    private boolean processCommand(Command command) 
    {
        boolean wantToQuit = false;

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        else if (commandWord.equals("look")) {
            look(command);
        }
        else if (commandWord.equals("eat")) {
            eat(command);
        }
        else if (commandWord.equals("back")) {
            back(command);
        }
        else if (commandWord.equals("stackBack")) {
            stackBack(command);
        }
        else if (commandWord.equals("take")) {
            take(command);    
        }
        else if (commandWord.equals("drop")) {
            drop(command);
        }
        else if (commandWord.equals("charge")) {
            charge(command);
        }
        else if(commandWord.equals("fire")) {
            fire(command);
        }
        // else command not recognised.
        return wantToQuit;
    }

    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print a cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println(parser.getCommands());
    }

    /** 
     * Try to go to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     * If we go to a new room, update previous room and previous room stack.
     * 
     * @param command The command to be processed
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        Room nextRoom = currentRoom.getExit(direction);

        if (nextRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            previousRoom = currentRoom; // store the previous room
            previousRoomStack.push(currentRoom); // and add to previous room stack
            currentRoom = nextRoom;
            System.out.println(printRoomandCarry());
        }
    }

    /** 
     * Return String containing current room description and the item carried
     * by the player.
     * 
     * @return Current room description and item carry description
     */
    private String printRoomandCarry() {
        return currentRoom.getLongDescription() + "\n" + getHeldItemString();
    }
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     * @return true, if this command quits the game, false otherwise
     */
    private boolean quit(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
    
    /** 
     * "Look" was entered. Check the rest of the command to see
     * whether we really want to look.
     * 
     * @param command The command to be processed
     */
    private void look(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Look what?");
        }
        else {
            // output the long description of this room
            System.out.println(printRoomandCarry());
        }
    }
    
    /** 
     * "Eat" was entered. Check the rest of the command to see
     * whether we really want to eat.
     * 
     * @param command The command to be processed
     */
    private void eat(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Eat what?");
            return;
        }
        
        if(heldItem == null) {
            System.out.println("You have nothing to eat.");
            return;
        }
        
        if (heldItem.getName().equals("cookie")){
            hungerLevel = 5;
            System.out.println("You ate the cookie.");
            heldItem = null;
        }
        else {
            System.out.println("You can't eat this."); 
        }
    } 
    
    /** 
     * "Back" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * 
     * @param command The command to be processed
     */
    private void back(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("Back what?");
        }
        else {
            // go back to the previous room, if possible
            if (previousRoom==null) {
                System.out.println("No room to go back to.");
            } else {
                // go back and swap previous and current rooms,
                // and put current room on previous room stack
                Room temp = currentRoom;
                currentRoom = previousRoom;
                previousRoom = temp;
                previousRoomStack.push(temp);
                // and print description
                System.out.println(currentRoom.getLongDescription());
            }
        }
    }
    
    /** 
     * "StackBack" was entered. Check the rest of the command to see
     * whether we really want to stackBack.
     * 
     * @param command The command to be processed
     */
    private void stackBack(Command command) 
    {
        if(command.hasSecondWord()) {
            System.out.println("StackBack what?");
        }
        else {
            // step back one room in our stack of rooms history, if possible
            if (previousRoomStack.isEmpty()) {
                System.out.println("No room to go stack back to.");
            } else {
                // current room becomes previous room, and
                // current room is taken from the top of the stack
                previousRoom = currentRoom;
                currentRoom = previousRoomStack.pop();
                // and print description
                System.out.println(currentRoom.getLongDescription());
            }
        }
    }
    
    /** 
     * "Take" was entered. Check the rest of the command to see
     * whether we really take an item.
     * 
     * @param command The command to be processed
     */
    private void take(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to take...
            System.out.println("Take what?");
            return;
        }
        
        if (command.getSecondWord().equals("cookie")) {
            heldItem = currentRoom.pickupItem(command.getSecondWord());
            if (heldItem == null) {
                System.out.println("Item is not in room.");
            } else {
                System.out.println("You picked up cookie.");
            }
            return;
        }
        
        if (hungerLevel > 0) {
            if (heldItem == null) {
                heldItem = currentRoom.pickupItem(command.getSecondWord());
                hungerLevel -= 1;
                if (heldItem == null) {
                System.out.println("Item is not in room.");
                } else {
                System.out.println("Hunger level: " + hungerLevel + ", You picked up " + heldItem.getName());
                }
            } else {
                System.out.println("You're holding an item already.");
            }
        }
        else {
            System.out.println("You must take and eat a cookie first.");
        }
    }
    
    /** 
     * "Drop" was entered. Check the rest of the command to see
     * whether we really drop an item.
     * 
     * @param command The command to be processed
     */
    private void drop(Command command) 
    {
        if(command.hasSecondWord()) {
            // there should not be a second command
            System.out.println("Drop what?");
            return;
        }
        
        if (!(heldItem == null)) {
            currentRoom.addItem(heldItem);
            System.out.println("You have dropped " + heldItem.getName());
            heldItem = null;
        }
        else {
            System.out.println("You have nothing to drop.");
        }
    }
    
    /** 
     * "Charge" was entered. Check if the beamer is held. If so, charge the 
     * beamer and save currentRoom.  
     * 
     * @param command The command to be processed
     */
    private void charge(Command command) 
    {
        if(command.hasSecondWord()) {
            // there should not be a second command
            System.out.println("Charge what?");
            return;
        }
        
        if (heldItem != null) {
            if (heldItem.getName().equals("beamer")) {
                    Beamer beamer = (Beamer) heldItem;
                    if (!(beamer.isCharged())) {
                    beamer.charge();
                    beamerRoom = currentRoom;
                    System.out.println("The beamer was charged!");
                    }
                    else {
                        System.out.println("The beamer is already charged!");
                    }
                }
            }
        else {
            System.out.println("You have no beamer to charge.");
        }
        }
        
    /** 
     * "Fire" was entered. Check if the beamer is charged. If charged, fire the beamer and transport to beamerRoom. 
     * 
     * @param command The command to be processed
     */
    private void fire(Command command) 
    {
        if(command.hasSecondWord()) {
            // there should not be a second command
            System.out.println("Fire what?");
            return;
        }
        
        if (heldItem != null) {
            if (heldItem.getName().equals("beamer")) {
                Beamer beamer = (Beamer) heldItem;
                if (beamer.isCharged()) {
                    beamer.fire();
                    currentRoom = beamerRoom;
                    System.out.println("The beamer was fired!");
                    System.out.println(currentRoom.getLongDescription());
                    System.out.println(getHeldItemString());
                    }   
                else {
                    System.out.println("The beamer is not charged.");
                }
                }
            }
        else {
            System.out.println("You have no beamer to fire.");
        }
        }
    }   
