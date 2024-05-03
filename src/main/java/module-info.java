module com.maxmerino.appnotes_maxmerino {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires bcrypt;
    requires java.base;
    
    opens com.maxmerino.appnotes_maxmerino to javafx.fxml;
    exports com.maxmerino.appnotes_maxmerino;
}
