package edu.duke.ece651.teamX.shared;
import java.io.Serializable;
public class Spy implements Serializable{
    private String owner;
    private boolean hasMoved;

    public Spy(String playerName){
        this.owner=playerName;
        hasMoved=false;
    }
    public void recordMove(){
        if(hasMoved){
            throw new IllegalArgumentException("This Spy is already moved in this Turn");
        }
        hasMoved=true;
    }
    public boolean checkMove(){
        return hasMoved;
    }

    /**
     * Need to be called each turn
     */
    public void turnReset(){
        hasMoved=false;
    }

    /**
     * Get owner player to display the info for the player
     * @return
     */
    public String getOwner(){
        return owner;
    }

    public static int getSpyCost(){
        return 10;
    }
}

