module me.jehn.javafxcourses {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens me.jehn.javafxcourses to javafx.fxml;
    exports me.jehn.javafxcourses;
}