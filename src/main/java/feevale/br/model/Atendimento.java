package feevale.br.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Atendimento {
    private final String numeroAtendimento; // Número único
    private final StringProperty status = new SimpleStringProperty();

    public Atendimento(String numeroAtendimento, String statusInicial) {
        this.numeroAtendimento = numeroAtendimento;
        this.status.set(statusInicial);
    }

    // Método abstrato que cada tipo de atendimento calculará à sua maneira
    public abstract double calcularValorTotal();

    // Getters e Setters para JavaFX
    public String getNumeroAtendimento() { return numeroAtendimento; }
    public String getStatus() { return status.get(); }
    public void setStatus(String novoStatus) { this.status.set(novoStatus); }
    public StringProperty statusProperty() { return status; }
}