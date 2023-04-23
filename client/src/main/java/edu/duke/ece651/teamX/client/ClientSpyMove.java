package edu.duke.ece651.teamX.client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import edu.duke.ece651.teamX.shared.*;

public class ClientSpyMove extends ClientTurnAction<SpyMoveSender> {

    public ClientSpyMove(Socket s, Map m, Player ply) {
        super(s, m, ply);
    }

    public ArrayList<Territory> findSourcTerritories(){
        ArrayList<Territory> res = new ArrayList<>();
        for(Territory t: map.getAllTerritories()){
            if(t.getSpyMoveIndsFromPlayer(player.getName()).size()>0){
                res.add(t);
            }
        }
        return res;
    }

    public ArrayList<Territory> findDestTerrs(Territory source) {
        ArrayList<Territory> res = new ArrayList<Territory>();
        Iterator<Territory> iter = source.getNeighbours();
        while (iter.hasNext()) {
            Territory curr = iter.next();
            res.add(curr);
        }
        return res;
    }

    public int calculateCost(Territory source, Territory dest, int num) {
        return num*(source.getTerritorySize()+dest.getTerritorySize());
    }

    public void perform_res(Territory source, Territory dest, String name, int num) {
        int cost = calculateCost(source,dest,num);
        if (cost > player.getFoodResource()) {
            throw new IllegalArgumentException("The food resource is not enough for moving spies cost " + cost);
        }
        if(source.getSpyMoveIndsFromPlayer(player.getName()).size()<num){
            throw new IllegalArgumentException("The spies is not enough to move");
        }
        dest.addSpies(source.removeSpies(player.getName(), num));  
        player.consumeFood(cost);  
        SpyMoveSender sms = new SpyMoveSender(source, dest, num, player.getName());    
        this.actions.add(sms);
    }

}
