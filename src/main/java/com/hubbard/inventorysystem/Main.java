package com.hubbard.inventorysystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// ***Generated JavaDoc documentation is located in the "javadocs" folder***
// FUTURE_ENHANCEMENT is located above Main class in Main.java
// RUNTIME_ERROR is located is the addPart method in the Inventory class in Inventory.java
/**
 * <p>Main entry point for program</p>
 * <p>FUTURE_ENHANCEMENT The <code>initializePartList()</code> and <code>initializeProductList()</code>
 * methods can be extracted into another class in the future to load in data from an external spreadsheet or
 * database.</p>
 */

public class Main extends Application {

    /**
     * <p>Starts the GUI for the application</p>
     * @param stage The application's main stage/view
     * @throws IOException Signals that Input/Output exception has occurred
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
        stage.setResizable(false);

    }

    /**
     * <p>Launches the program</p>
     * @param args Contains supplied command line arguments
     */
    public static void main(String[] args) { launch(); }

}