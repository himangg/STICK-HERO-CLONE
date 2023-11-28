module com.example.ap_project {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ap_project to javafx.fxml;
    exports com.example.ap_project;
}