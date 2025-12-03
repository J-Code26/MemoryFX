module com.example.memoryfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.memoryfx to javafx.fxml;
    exports com.example.memoryfx;
}