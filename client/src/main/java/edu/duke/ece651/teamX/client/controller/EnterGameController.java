package edu.duke.ece651.teamX.client.controller;

import java.io.IOException;

import edu.duke.ece651.teamX.client.*;
import edu.duke.ece651.teamX.client.view.GeneralScreen;
import edu.duke.ece651.teamX.shared.*;

import java.net.Socket;
import java.util.ArrayList;

import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Label;

public class EnterGameController extends CreateGameController {
    @FXML
    private GridPane gridPane;
    private ArrayList<RoomSender> roomList;

    public EnterGameController(Stage st, Socket cs, ArrayList<String> np) {
        super(st, cs, np);
        gridPane = new GridPane();

    }

    @FXML
    public void displayRooms() {
        gridPane.getChildren().clear();
        RowConstraints rowcons = new RowConstraints();
        rowcons.setPercentHeight(7);
        String titles[] = { "Options", "Total Players", "Joined Players", "Has Begun", "Has Ended" };
        for (int i = 0; i < 5; i++) {
            Label titleLabel = new Label(titles[i]);
            gridPane.add(titleLabel, i, 0);
        }
        for (int i = 0; i < roomList.size(); i++) {
            Button indButton = new Button(String.valueOf(i));

            indButton.setOnAction(event -> {
                try {
                    chooseRoom(event);
                } catch (Exception e) {
                }
            });

            Label tpLable = new Label(String.valueOf(roomList.get(i).getTotalNum()));
            Label jpLable = new Label(String.valueOf(roomList.get(i).getJointedNum()));
            Label hbLable = new Label(String.valueOf(roomList.get(i).getIsBegin()));
            Label beLable = new Label(String.valueOf(roomList.get(i).getIsEnd()));
            gridPane.getRowConstraints().add(rowcons);
            gridPane.add(indButton, 0, i + 1);
            gridPane.add(tpLable, 1, i + 1);
            gridPane.add(jpLable, 2, i + 1);
            gridPane.add(hbLable, 3, i + 1);
            gridPane.add(beLable, 4, i + 1);
        }
        gridPane.getRowConstraints().add(rowcons);
        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            try {
                back();
            } catch (Exception e) {
            }
        });
        gridPane.add(backButton, 4, roomList.size() + 1);

    }

    public ArrayList<RoomSender> receiveRoomList() {
        try {
            ArrayList<RoomSender> roomList = (ArrayList<RoomSender>) Communicate.receiveObject(
                    clientSocket);
            return roomList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Invalid server socket to receive message");
        }

    }

    @FXML
    public void chooseRoom(ActionEvent ae) throws IOException, ClassNotFoundException {
        int choice = getSendNumber(ae);
        String mes = (String) Communicate.receiveObject(clientSocket);
        if (mes.length() == 0) {
            // System.out.println("enter room");
            // WaitController wc = new WaitController(stage, clientSocket, namePassword);
            // GeneralScreen<WaitController> wcs = new GeneralScreen<>(wc);
            // Client client = new Client(clientSocket, stage, namePassword);
            RoomSender rs = roomList.get(choice);
            if (rs.getIsBegin()) {
                Communicate.receivePlayer(clientSocket);
                PlayTurnController playTurnController = new PlayTurnController(stage, clientSocket, namePassword);
                GeneralScreen<PlayTurnController> generalScreen = new GeneralScreen<>(playTurnController);
            } else {
                WaitController wc = new WaitController(stage, clientSocket, namePassword);
                GeneralScreen<WaitController> wcs = new GeneralScreen<>(wc);
            }

        } else {
            back();
        }
    }

    @Override
    public void setNewLayout() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/entergame.fxml"));
        loader.setController(this);
        roomList = receiveRoomList();
        Parent root = loader.load();
        displayRooms();
        // URL cssResource = getClass().getResource("/style/roomButton.css");
        Scene scene = new Scene(root, 640, 480);
        stage.setTitle("Enter a Game");
        stage.setScene(scene);
        stage.show();

    }
}
