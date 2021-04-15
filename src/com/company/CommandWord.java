package com.company;

/**
 * Representations for all the valid command words for the game
 * along with a string in a particular language.
 *
 * @author  Kwinn Danforth
 * @version 1.0.01
 */
public enum CommandWord
{
    // A value for each command word along with its
    // corresponding user interface string.
    GO("go"), QUIT("quit"), HELP("help"), YES("yes"), NO("no"), UNKNOWN("?");

    // The command string.
    private String commandString;

    /**
     * Initialise with the corresponding command string.
     * @param commandString The command string.
     */
    CommandWord(String commandString)
    {
        this.commandString = commandString;
    }

    /**
     * @return The command word as a string.
     */
    public String toString()
    {
        return commandString;
    }
}
