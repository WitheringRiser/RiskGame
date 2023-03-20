package edu.duke.ece651.teamX.client;

import edu.duke.ece651.teamX.shared.MoveSender;
import java.io.IOException;
import java.util.ArrayList;
import edu.duke.ece651.teamX.shared.*;
import java.net.Socket;
import java.io.PrintStream;
import java.util.Iterator;

public class ClientMove extends ClientTurnAction<MoveSender> {
    public ClientMove(Socket s, PrintStream o, UserInReader uir, TextPromot tp, Map m, Player ply) {
        super(s, o, uir, tp, m, ply);
    }

    public ArrayList<Territory> findDestTerrs(Territory source) {
        if (!map.getOwner(source).equals(this.player)) {
            throw new IllegalArgumentException("The territory does not belong to this player");
        }
        ArrayList<Territory> dests = new ArrayList<Territory>();
        ArrayList<Territory> visited = new ArrayList<Territory>();
        ArrayList<Territory> todo = new ArrayList<Territory>();
        todo.add(source);
        while (!todo.isEmpty()) {
            Territory curr = todo.get(0);
            todo.remove(0);
            if (!visited.contains(curr)) {
                Iterator<Territory> iter = curr.getNeighbours();
                while (iter.hasNext()) {
                    Territory t = iter.next();
                    if (map.getOwner(t).equals(this.player)) {
                        if (!dests.contains(t) && (!t.equals(source))) {
                            dests.add(t);
                        }
                        todo.add(t);
                    }
                }
                visited.add(curr);
            }
        }
        return dests;
    }

    public void perform()throws IOException  {
        ActionSender res = generateAction();
        if (res != null) {
            MoveSender mvs = new MoveSender(res.getSource(), res.getDestination(), res.getUnitsNum());
            this.actions.add(mvs);
            res.getSource().removeUnits(res.getUnitsNum());
            res.getDestination().addUnits(null, res.getUnitsNum());
        }
    }
}
