package edu.duke.ece651.teamX.client;

import java.io.IOException;
import java.util.ArrayList;
import edu.duke.ece651.teamX.shared.*;
import java.net.Socket;
import java.io.PrintStream;
import java.util.Iterator;

public class ClientAttack extends ClientTurnAction<AttackSender> {
    public ClientAttack(Socket s, PrintStream o, UserInReader uir, TextPromot tp, Map m, Player ply) {
        super(s, o, uir, tp, m, ply);
    }

    /**
     * Find all neighbors of a territory that are not belongs to this
     * player
     * 
     * @param source is the source territory to start attack
     * @return is the list of direct neighbor territories of other players
     */
    public ArrayList<Territory> findDestTerrs(Territory source) {
        if (!map.getOwner(source).equals(this.player)) {
            throw new IllegalArgumentException("The territory does not belong to this player");
        }
        Iterator<Territory> iter = source.getNeighbours();
        ArrayList<Territory> dests = new ArrayList<Territory>();
        while (iter.hasNext()) {
            Territory curr = iter.next();
            if (!map.getOwner(curr).equals(this.player)) {
                dests.add(curr);
            }
        }
        return dests;
    }

    public void perform() throws IOException {
        ActionSender res = generateAction();
        if (res != null) {
            AttackSender atts = new AttackSender(res.getSource(), res.getDestination(), res.getUnitsNum());
            this.actions.add(atts);
            res.getSource().removeUnits(res.getUnitsNum());
        }
    }
}
