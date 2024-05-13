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
    
    private static Scene scene;//Escena
    private static Model model;//Model de totes les pantalles
    //Controladors
    private static PrimaryController controlador1;
    private static SecondaryController controlador2;
    private static EdicioNotesController controlador3;
    private static CompartirController controlador4;
    //Es crea el model i els controladors, s'injecta la instància del model a tots els controladors
    @Override
    public void start(Stage stage) throws IOException {
        model = new Model();
        controlador1 = new PrimaryController();
        controlador2 = new SecondaryController();
        controlador3 = new EdicioNotesController();
        controlador4 = new CompartirController();
        controlador1.injecta(model);
        controlador2.injecta(model);
        controlador3.injecta(model);
        controlador4.injecta(model);
        controlador3.injectaStage(stage);
        scene = new Scene(loadFXML("primary"), 720, 540);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    //Càrrega dels FXML
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        if (fxml.equals("primary")) {
            fxmlLoader.setControllerFactory(param->controlador1);
        } else if(fxml.equals("secondary")){
            fxmlLoader.setControllerFactory(param->controlador2);
        }else if(fxml.equals("edicio_notes")){
           
            fxmlLoader.setControllerFactory(param->controlador3);
        }else{
            fxmlLoader.setControllerFactory(param->controlador4);
        }
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}