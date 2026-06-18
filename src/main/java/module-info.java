module feevale.br {
    requires javafx.controls;
    requires javafx.fxml;

    opens feevale.br to javafx.fxml;
    exports feevale.br;

    exports feevale.br.controller;
    opens feevale.br.controller to javafx.fxml;
    
    exports feevale.br.model;
    opens feevale.br.model to javafx.fxml;
}
