package edu.duke.ece651.teamX.client;

import edu.duke.ece651.teamX.shared.*;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class GUI implements Displayable{
    @Override
    public void showMap(HashMap<Integer, ArrayList<Territory>> CurrentMap, Pair<Integer, String> playerInfo, HashMap<String, Button> ButtonMap) {
        //set all the button colors and shape in buttonMap
        ColorID PlayerColor = new ColorID();
        for (HashMap.Entry<Integer, ArrayList<Territory>> entry : CurrentMap.entrySet()){
            //iterate player colors and set the button territory to its color
            String color = PlayerColor.getPlayerColor(entry.getKey());
            ArrayList<Territory> TerrList = entry.getValue();
            String background = color.toLowerCase();
            
            for(int i = 0; i < TerrList.size(); i++){
                Territory OneTerr = TerrList.get(i);
                String TerrName = OneTerr.getName();
                String styleStr = getStyle(background, TerrName);
                Button Btn = ButtonMap.get(TerrName);
                Btn.setStyle(styleStr);
                Btn.setCursor(Cursor.HAND);
            }
        }
        System.out.println("Paint color already");
    }

    @Override
    public void showAction(HashMap<Integer, ArrayList<Action>> RecvAction, Pair<Integer, String> playerInfo, TreeView<String> tree) {
        
    }

    public String getStyle(String color, String territoryName) {
        //set button shapes and colors
        StringBuilder sb = new StringBuilder();
        //territory name map to shape and color
        if (color.equals(("green"))) { color = "darkolivegreen"; }
        else if (color.equals(("blue"))) { color = "cadetblue"; }
        else if (color.equals(("yellow"))) { color = "lemonchiffon"; }
        else if (color.equals(("red"))) { color = "lightcoral"; }
        else if (color.equals(("white"))) { color = "floralwhite"; }
        sb.append("-fx-background-color: " + color + ";");
        
        if (territoryName.equals("A")) { sb.append("-fx-shape: \"M 400 150 Q 450 400 350 400 Q 200 400 50 400 Q 50 200 250 200 Q 350 200 400 150\""); }
        else if (territoryName.equals("B")) { sb.append("-fx-shape: \"M 400 300 Q 450 300 600 300 L 600 100 L 400 100 Q 350 200 400 300\""); }
        else if (territoryName.equals("C")) { sb.append("-fx-shape: \"M 350 300 C 425 300 475 300 550 300 Q 650 200 600 50 L 350 50 Q 350 200 350 300\""); }
        else if (territoryName.equals("D")) { sb.append("-fx-shape: \"M 350 300 C 425 300 475 300 600 300 Q 650 200 600 100 Q 450 50 350 150 Q 350 200 350 300\""); }
        else if (territoryName.equals("E")) { sb.append("-fx-shape: \"M 400 200 Q 450 300 550 300 Q 650 300 750 250 Q 750 0 450 50 Q 450 100 400 200\""); }
        else if (territoryName.equals("F")) { sb.append("-fx-shape: \"M 350 350 C 450 350 450 350 550 350 Q 600 200 600 100 Q 450 0 350 100 Q 350 200 350 350\""); }
        else if (territoryName.equals("G")) { sb.append("-fx-shape: \"M 350 350 C 450 350 450 350 600 350 Q 650 250 600 150 Q 500 150 350 200 Q 350 200 350 350\""); }
        else if (territoryName.equals("H")) { sb.append("-fx-shape: \"M 550 350 Q 600 400 700 400 Q 800 250 750 100 L 550 100 Q 500 200 550 350\""); }
        else if (territoryName.equals("I")) { sb.append("-fx-shape: \"M 350 450 C 500 400 550 425 700 400 Q 650 250 700 100 L 350 100 Q 300 250 350 450\""); }
        else if (territoryName.equals("J")) { sb.append("-fx-shape: \"M 350 400 Q 500 450 700 400 Q 800 300 800 200 C 450 200 500 200 350 200 Q 300 250 350 400\""); }
        else if (territoryName.equals("K")) { sb.append("-fx-shape: \"M 350 350 C 500 350 550 450 700 400 Q 800 300 700 150 C 450 200 500 200 350 200 Q 300 250 350 350\""); }
        else if (territoryName.equals("L")) { sb.append("-fx-shape: \"M 350 450 C 450 500 450 450 550 450 Q 500 300 600 200 C 450 200 500 200 350 200 C 400 300 200 350 350 450\""); }
        
        sb.append(";");
        sb.append("-fx-border-color: black;");
        return sb.toString();
    }

}