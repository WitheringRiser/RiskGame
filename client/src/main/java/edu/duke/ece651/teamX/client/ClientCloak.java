package edu.duke.ece651.teamX.client;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import edu.duke.ece651.teamX.shared.*;

public class ClientCloak {
    private Socket socket;
    private Communicate communicate;
    protected Map map;
    protected Player player;
    protected ArrayList<CloakSender> actions;

    public ClientCloak(Socket s, Map m, Player ply) {
        socket = s;
        map = m;
        player = ply;
        this.communicate = new Communicate();
        this.actions = new ArrayList<>();
    }

    public ArrayList<Territory> getSourcTerritories(){
        ArrayList<Territory> res=new ArrayList<>();
        for(Territory t: map.getTerritories(this.player)){
            if(!t.isCloaked()){
                res.add(t);
            }
        }
        return res;
    }

    public void perform(Territory source) {
        if (!player.getCanCloak()) {
            throw new IllegalArgumentException("This player cannot use cloak");
        }
        if (player.getTechResource() < CloakProcessor.getCloakCost()) {
            throw new IllegalArgumentException("No enough resource for cloak cost : " + CloakProcessor.getCloakCost());
        }
        source.addCloak();
        player.consumeTech(CloakProcessor.getCloakCost());
        this.actions.add(new CloakSender(source));
    }

    public void commit() throws IOException {
        communicate.sendObject(socket, this.actions);
        this.actions.clear();
    }
}
