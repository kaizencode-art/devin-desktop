package dev.kaizensphere.devin.desktop;

import atlantafx.base.theme.PrimerDark;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class DevinGUI extends Application {

    static {
        AppContext.run();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(DevinGUI.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        scene.getStylesheets().add(new PrimerDark().getUserAgentStylesheet());

        stage.setMinWidth(800);
        stage.setMinHeight(600);

        stage.setTitle("DEVIN!");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        AppContext.repository.close();
    }

    public static void main(String[] args) {
        launch();
    }
}