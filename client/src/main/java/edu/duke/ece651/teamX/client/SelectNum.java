package edu.duke.ece651.teamX.client;

import edu.duke.ece651.teamX.shared.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import java.io.IOException;
import java.util.HashMap;

public class SelectNum {

    @FXML private ImageView player0;
    @FXML private ImageView player1;
    @FXML private ImageView player2;
    @FXML private ImageView player3;
    @FXML private ImageView player4;

    private HashMap<Integer, ImageView> ImageViewMap;
    @FXML private ComboBox<Integer> PlayerNum;
    @FXML private Button OKBtn;
    private Player CurrPlayer;
    private Stage Window;


    public SelectNum(Player CurrPlayer, Stage Window){
        this.CurrPlayer = CurrPlayer;
        this.Window = Window;
    }
    @FXML
    public void initialize(){
        setUpImageView();
        PlayerNum.setValue(2);
        ObservableList<Integer> ChoiceNum = FXCollections.observableArrayList(2,3,4,5);
        PlayerNum.setItems(ChoiceNum);
    }

    @FXML
    public void DisplayPhoto(){
        int number = this.PlayerNum.getValue();
        for(int i = 0; i < 5; i++){
            if(i < number){
                Image Photo = new Image(getClass().getResourceAsStream("/PlayerPic/player" + i + ".png"));
                ImageViewMap.get(i).setImage(Photo);
            }
            else{
                Image Locked = new Image(getClass().getResourceAsStream("/PlayerPic/player" + i + ".png"));
                ImageViewMap.get(i).setImage(Locked);
            }
        }
    }
    private void setUpImageView(){
        ImageViewMap = new HashMap<>();
        ImageViewMap.put(0,player0);
        ImageViewMap.put(1,player1);
        ImageViewMap.put(2,player2);
        ImageViewMap.put(3,player3);
        ImageViewMap.put(4,player4);
    }
    @FXML
    public void OKNumber() throws IOException {
        //if the first player already choose the value, they will click on the OK button
        int totalNum = PlayerNum.getValue();
        CurrPlayer.numPlayers = totalNum;
        System.out.println("OK the number of player is " + totalNum);
        Player temp = new Player("p1", 3);

        //**TO DO**: send the total number of players chosen by the first to the server
        

        //ShowView.MainPageView(this.CurrPlayer, this.Window, true);
    }



}