package com.maxmerino.appnotes_maxmerino;

import com.maxmerino.appnotes_maxmerino.model.Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Model model;
    private static PrimaryController controlador1;
    private static SecondaryController controlador2;
    private static EdicioNotesController controlador3;
    @Override
    public void start(Stage stage) throws IOException {
        model = new Model();
        controlador1 = new PrimaryController();
        controlador2 = new SecondaryController();
        controlador3 = new EdicioNotesController();
        controlador1.injecta(model);
        controlador2.injecta(model);
        controlador3.injecta(model);
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        if (fxml.equals("primary")) {
            fxmlLoader.setControllerFactory(param->controlador1);
        } else if(fxml.equals("secondary")){
            fxmlLoader.setControllerFactory(param->controlador2);
        }else{
           
            fxmlLoader.setControllerFactory(param->controlador3);
        }
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}