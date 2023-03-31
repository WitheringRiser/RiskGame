package edu.duke.ece651.teamX.client;

import edu.duke.ece651.teamX.shared.*;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;

public class ShowView {

    public static void MainPageView(Player player, Stage window, Boolean firstTime) throws IOException {
        int tell;
        FXMLLoader loaderStart = new FXMLLoader(ShowView.class.getResource("/Views/Map.fxml"));
        loaderStart.setControllerFactory(c->{
            return new Map(player, window, firstTime);
        });
        Scene scene = new Scene(loaderStart.load());
        tell = 1;
        window.setScene(scene);
        window.show();
    }

}
