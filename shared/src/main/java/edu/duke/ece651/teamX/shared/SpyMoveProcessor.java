package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class SpyMoveProcessor {
    private Map map;
    private ArrayList<SpyMoveSender> allSpyMove;

    public SpyMoveProcessor(ArrayList<SpyMoveSender> _allSpyMove, Map m){
        map=m;
        allSpyMove=_allSpyMove;
    }
    
    public void resolveAllSpyMove(){
        for(SpyMoveSender sms : allSpyMove){
            
            Territory source = map.getTerritoryByName(sms.getSource().getName());
            Territory dest = map.getTerritoryByName(sms.getDestination().getName());
            Player owner = map.getPlayerByName(sms.getName());
            int num = sms.getUnitsNum();
            ArrayList<Integer> indexList = source.getSpyMoveIndsFromPlayer(sms.getName());
            if(num>0 && indexList.size()<num){
                throw new IllegalArgumentException("Do not have enough Spy to move");
            }
            if(!source.hasNeighbor(dest)){
                throw new IllegalArgumentException("These two territories are not neighbors");
            }
            int cost = num*(source.getTerritorySize()+dest.getTerritorySize());
            if(cost>owner.getFoodResource()){
                throw new IllegalArgumentException("Not enough food resource for moving spy");
            }
            dest.addSpies(source.removeSpies(sms.getName(), num));
            owner.consumeFood(cost);
        }
    }


}
