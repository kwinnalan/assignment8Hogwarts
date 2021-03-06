package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

/**
 *  This class is the Main class of the Hogwarts adventure application.
 *  This is a very simple, text based adventure game.  Users
 *  can walk around some scenery and pick up items.
 *
 *  This Main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 *
 * @author  Kwinn Danforth
 * @version 1.0.01
 */

public class Game {
    private final ArrayList<String> rooms;
    private final ArrayList<String> descriptions;
    private final ArrayList<String> occupancies;
    private final ArrayList<String> items;
    private final ArrayList<Integer> numbers;

    private final HashMap<String, String> roomToDesc;
    private final HashMap<String, String> roomToOccupancy;
    private final HashMap<String, String> roomToItem;
    private final HashMap<String, Integer> roomToNumber;

    private final ArrayList<String> PLAYERS_ITEMS;
    private final Parser PARSER;
    private Room currentRoom;

    /**
     * Create the game and initialise its internal map
     */
    public Game() {
        rooms = new ArrayList<>();
        descriptions = new ArrayList<>();
        occupancies = new ArrayList<>();
        items = new ArrayList<>();
        numbers = new ArrayList<>();

        roomToDesc = new HashMap<>();
        roomToOccupancy = new HashMap<>();
        roomToItem = new HashMap<>();
        roomToNumber = new HashMap<>();

        PARSER = new Parser();
        PLAYERS_ITEMS = new ArrayList<>();

        getRoomData();
        createRooms();
        play();
    }


    /**
     * This method will retrieve the room data from a csv file
     */
    public void getRoomData() {
        try (Scanner scanner = new Scanner(Paths.get("src/com/company/RoomData.csv").toFile())) {
            while (scanner.hasNextLine()) {
                getRecordFromLine(scanner.nextLine());
            }
            for (int i = 0; i < (descriptions.size()); i++) {
                roomToDesc.put(rooms.get(i), descriptions.get(i));
                roomToOccupancy.put(rooms.get(i), occupancies.get(i));
                roomToItem.put(rooms.get(i), items.get(i));
                roomToNumber.put(rooms.get(i), numbers.get(i));
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Sorry there seems to be a problem retrieving the game data..");
            ex.printStackTrace();
        }
    }

    /**
     * This method will take out each record from the line and add each record to it's corresponding list
     *
     * @param line, the String value of the line
     */
        private void getRecordFromLine (String line){
            List<String> values = new ArrayList<>();
            try (Scanner rowScanner = new Scanner(line)) {
                rowScanner.useDelimiter(",");
                while (rowScanner.hasNext()) {
                    values.add(rowScanner.next());
                }
                rooms.add(values.get(0));
                descriptions.add(values.get(1));
                occupancies.add(values.get(2));
                items.add(values.get(3));
                numbers.add(Integer.valueOf(values.get(4)));
            }
        }


        /**
         * Create all the rooms and link their exits together.
         */
        private void createRooms ()
        {
            TransporterRoom transporterRoom;
            Room outside, mainHall, commonRoom, potionsLab, broomCloset, boardingRoom, hagridsHut;
            // create the rooms
            outside = new Room(roomToDesc.get("Outside"), roomToOccupancy.get("Outside"), roomToItem.get("Outside"));
            mainHall = new Room(roomToDesc.get("Main Hall"), roomToOccupancy.get("Main Hall"), roomToItem.get("Main Hall"));
            commonRoom = new Room(roomToDesc.get("Common Room"), roomToOccupancy.get("Common Room"), roomToItem.get("Common Room"));
            potionsLab = new Room(roomToDesc.get("Potions Lab"), roomToOccupancy.get("Potions Lab"), roomToItem.get("Potions Lab"));
            broomCloset = new Room(roomToDesc.get("Broom Closet"), roomToOccupancy.get("Broom Closet"), roomToItem.get("Broom Closet"));
            boardingRoom = new Room(roomToDesc.get("Boarding Room"), roomToOccupancy.get("Boarding Room"), roomToItem.get("Boarding Room"));
            hagridsHut = new Room(roomToDesc.get("Hagrid's Hut"), roomToOccupancy.get("Hagrid's Hut"), roomToItem.get("Hagrid's Hut"));

            transporterRoom = new TransporterRoom("in the transporter room, this room will transport you to a random room!");

            // initialise room exits
            outside.setExit("east", mainHall);
            outside.setExit("west", hagridsHut);

            mainHall.setExit("west", outside);
            mainHall.setExit("east", commonRoom);
            mainHall.setExit("north", boardingRoom);
            mainHall.setExit("south", potionsLab);

            commonRoom.setExit("north", broomCloset);
            commonRoom.setExit("west", mainHall);
            commonRoom.setExit("south", transporterRoom);

            potionsLab.setExit("north", mainHall);
            potionsLab.setExit("east", transporterRoom);
            potionsLab.setExit("west", outside);

            boardingRoom.setExit("east", broomCloset);
            boardingRoom.setExit("south", mainHall);

            broomCloset.setExit("west", boardingRoom);
            broomCloset.setExit("south", commonRoom);

            hagridsHut.setExit("east", outside);

            transporterRoom.setRooms(mainHall);
            transporterRoom.setRooms(boardingRoom);
            transporterRoom.setRooms(commonRoom);
            transporterRoom.setRooms(outside);
            transporterRoom.setRooms(hagridsHut);
            transporterRoom.setRooms(potionsLab);
            transporterRoom.setRooms(broomCloset);

            currentRoom = mainHall;  // start game in main hall
        }


        /**
         *  Main play routine.  Loops until end of play.
         */
        public void play ()
        {
            printWelcome();

            // Enter the Main command loop.  Here we repeatedly read commands and
            // execute them until the game is over.

            boolean finished = false;
            while (!finished) {
                Command command = PARSER.getCommand();
                finished = processCommand(command);
            }
            System.out.println("Thank you for playing.  Good bye.");
        }

        /**
         * Print out the opening message for the player.
         */
        private void printWelcome ()
        {
            System.out.println();
            System.out.println("Welcome to Hogwarts!");
            System.out.println("The magical place of witches and warlocks! Explore the grounds by typing commands and try to collect items. ('go <direction>' format)");
            System.out.println("Type '" + CommandWord.HELP + "' if you need help.");
            System.out.println();
            System.out.println(currentRoom.getLongDescription());
        }

        /**
         * Given a command, process (that is: execute) the command.
         * @param command The command to be processed.
         * @return true If the command ends the game, false otherwise.
         */
        private boolean processCommand (Command command)
        {
            boolean wantToQuit = false;

            CommandWord commandWord = command.getCommandWord();

            switch (commandWord) {
                case UNKNOWN:
                    System.out.println("I don't know what you mean...");
                    break;

                case HELP:
                    printHelp();
                    break;

                case GO:
                    goRoom(command);
                    break;

                case PICKUP:
                    addItem(currentRoom.getItem());
                    currentRoom.setItem("no item");
                    break;

                case QUIT:
                    wantToQuit = quit(command);
                    break;
            }
            return wantToQuit;
        }

        // implementations of user commands:

        /**
         * Print out some help information.
         * Here we print some stupid, cryptic message and a list of the
         * command words.
         */
        private void printHelp ()
        {
            System.out.println("You are lost. You have no items. You wander");
            System.out.println("around at Hogwarts and find Items.(there are four total!)");
            System.out.println();
            System.out.println("Your command words are:");
            PARSER.showCommands();
        }

    /**
     * Print out the players Items
     * Here we print out a list of the players items that he/she has collected
     */
    private void printItems()
    {
        for(String item : PLAYERS_ITEMS){
            if(!(item.equals("nothing"))) {
                System.out.println(item);
            }
        }
    }
        /**
         * Try to go in one direction. If there is an exit, enter the new
         * room, otherwise print an error message.
         */
        private void goRoom (Command command)
        {
            if (!command.hasSecondWord()) {
                // if there is no second word, we don't know where to go...
                System.out.println("Go where?");
                return;
            }

            String direction = command.getSecondWord();

            // Try to leave current room.
            Room nextRoom = currentRoom.getExit(direction);

            if (nextRoom == null) {
                System.out.println("There is no door!");
            } else {
                currentRoom = nextRoom;
                for(int i = 0; i < 25; i++) {
                    System.out.println("\n");
                }
                System.out.println(currentRoom.getLongDescription());
                System.out.println("\n" + "Collected Items: ");
                printItems();
            }
        }

        /**
         * "Quit" was entered. Check the rest of the command to see
         * whether we really quit the game.
         * @return true, if this command quits the game, false otherwise.
         */
        private boolean quit (Command command)
        {
            if (command.hasSecondWord()) {
                System.out.println("Quit what?");
                return false;
            } else {
                return true;  // signal that we want to quit
            }
        }

     /**
      * "Yes" was entered. So, we add the item to the list of items
      *
      * @param item, the item to add
     */
        private void addItem (String item)
        {
            PLAYERS_ITEMS.add(item);
        }
}

