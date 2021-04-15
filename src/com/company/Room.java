package com.company;

import java.util.Set;
import java.util.HashMap;


/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the Hogwarts application.
 *
 * A "Room" represents one location in the scenery of the game.  It is
 * connected to other rooms via exits.  For each existing exit, the room
 * stores a reference to the neighboring room. Some rooms have items in them that the player can collect.
 *
 * @author  Kwinn Danforth
 * @version 1.0.01
 */

public class Room
{
    private String description;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private String occupancy;



    private String item;

    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description, String occupancy, String item)
    {
        this.description = description;
        this.occupancy = occupancy;
        this.item = item;
        exits = new HashMap<>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor)
    {
        exits.put(direction, neighbor);
    }

    /**
     * Define an item for the room
     *
     * @param item The item for the room.
     */
    public void setItem(String item) {
        this.item = item;
    }

    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }



    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        String pickUpItemQuestion;
        if(!this.item.equals("no item")) {
            pickUpItemQuestion = "Would you like to pick up the item? (enter command yes or no)";
        }else{
            pickUpItemQuestion = "";
        }
        return "You are " + description + ".\n" + occupancy + " is here" + "\n"+"There is " + item +" in here!" + "\n"+ pickUpItemQuestion + "\n" + getExitString();

    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction)
    {
        return exits.get(direction);
    }
    public String getItem() {
        return item;
    }
}

