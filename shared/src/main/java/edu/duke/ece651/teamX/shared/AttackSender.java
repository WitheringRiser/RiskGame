package edu.duke.ece651.teamX.shared;

import java.util.ArrayList;

public class AttackSender extends ActionSender {
  private ArrayList<Integer> indexList;

  public AttackSender(Territory _source, Territory _destination, int _unitsNum) {
    super(_source, _destination, _unitsNum);
    indexList = new ArrayList<>();
    for (int i = 0; i < _unitsNum; ++i) {
      indexList.add(i);
    }
  }

  public AttackSender(Territory _source, Territory _destination, ArrayList<Integer> _indexList) {
    super(_source, _destination, _indexList.size());
    indexList = _indexList;
  }

  public ArrayList<Integer> getIndexList() {
    return indexList;
  }

}
