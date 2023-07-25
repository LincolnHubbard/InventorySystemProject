module com.example.inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;



    opens com.hubbard.inventorysystem to javafx.fxml;
    exports com.hubbard.inventorysystem;
    exports com.hubbard.inventorysystem.controller;
    opens com.hubbard.inventorysystem.controller to javafx.fxml;
    opens com.hubbard.inventorysystem.model to javafx.base;
    exports com.hubbard.inventorysystem.model;
}