package edu.duke.ece651.teamX.shared;

import java.io.Serializable;

public abstract class Unit implements Serializable {
    private int num;
    public Unit(int num){
        this.num = num;
    }
}
