package edu.duke.ece651.teamX.server;

import edu.duke.ece651.teamX.shared.*;
import java.util.*;
import java.io.*;

public class ServerChecker {

  private HashMap<Integer, ArrayList<Territory>> world;
  private ActionSender action;

  public ServerChecker(HashMap<Integer, ArrayList<Territory>> myworld) {
    world = new HashMap<>();
    world = myworld;
    action = new ActionSender();
  }

  public boolean Check(ActionSender myaction) {
    action = myaction;
    boolean temp = checkTerritory() && checkNeighbor();
    if (temp == true) {
      System.out.println("[DEBUG] servercheck succeed");
    }
    return temp;
  }

  // check if action's srcTerrotory and dstTerritory are in terrirory Map
  private boolean checkTerritory() {
    String srcName = action.getSource().getName();
    String dstName = action.getDestination().getName();
    DoAction myDoAction = new DoAction(world);
    String findSrcName = myDoAction.findTerritory(world, srcName).getName();
    String findDstName = myDoAction.findTerritory(world, dstName).getName();
    return srcName.equals(findSrcName) && dstName.equals(findDstName);
  }


  // check if there is a valid path from srcTerritory to dstTerritory
  private boolean checkNeighbor() {
    System.out.println("[DEBUG] checkNum succeed");
    HashSet<Territory> visitedSet = new HashSet<>();
    DoAction myDoAction2 = new DoAction(world);
    Territory dstTerritory = myDoAction2.findTerritory(world, action.getDestination().getName());
    Territory srcTerritory = myDoAction2.findTerritory(world, action.getSource().getName());

    DoAction myDoAction = new DoAction(world);

    Stack<Territory> stack = new Stack<Territory>();
    stack.add(srcTerritory);
    visitedSet.add(srcTerritory);
    while (!stack.isEmpty()) {
      Territory curr = stack.pop();
      if (curr.getName().equals(dstTerritory.getName())) {
        return true;
      }

      Iterator<Territory> neighborIt = curr.getNeighbours();
      ArrayList<Territory> list = new ArrayList<>();
      neighborIt.forEachRemaining(list::add);
      for (int i = 0; i < list.size(); i++) {
        Territory tempTerr = list.get(i);
        String tempName = tempTerr.getName();
        Territory Neighbor = myDoAction.findTerritory(world, tempName);
        if (!visitedSet.contains(Neighbor)) {
          if (Neighbor.getName().equals(dstTerritory.getName())) {
            return true;
          }

          visitedSet.add(Neighbor);
        }
      }

    }

    return false;
  }

  private void printStack(Stack<Territory> stack) {
    for (Territory item : stack) {
      System.out.print(item.getName() + ",");
    }
    System.out.print("\n");
  }
}