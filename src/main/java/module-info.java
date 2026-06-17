module feevale.br {
    requires javafx.controls;
    requires javafx.fxml;

    opens feevale.br to javafx.fxml;
    exports feevale.br;
}
