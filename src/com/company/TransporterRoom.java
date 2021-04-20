package com.company;

import java.util.Random;
import java.util.ArrayList;
/**
 * This is a different kind of room that will transport the player to a random location
 *  with any direction they choose.
 *
 * @author Kwinn Danforth
 * @version 1.0.01
 */
public class TransporterRoom extends Room
{
    private final Random RANDOM_NUMBER_GENERATOR;
    private final ArrayList<Room> ROOMS;

    public TransporterRoom(String description)
    {
        super(description, "no one", "no item");
        RANDOM_NUMBER_GENERATOR = new Random();
        ROOMS = new ArrayList<>();
    }

    /**
     * Return a random room, independent of the direction
     * parameter
     *
     * @param  direction, ignored
     * @return   a random room
     */
    public Room getExit(String direction)
    {
        return findRandomRoom();
    }

    /**
     * Return a random room, independent of the direction
     * parameter
     *
     * @return   a random room
     */
    public Room findRandomRoom()
    {
        int random = this.RANDOM_NUMBER_GENERATOR.nextInt(ROOMS.size() - 1);
        return ROOMS.get(random);
    }

    /**
     * Sets possible random rooms
     *
     * @param  room to add
     */
    public void setRooms(Room room)
    {
        ROOMS.add(room);
    }
}

