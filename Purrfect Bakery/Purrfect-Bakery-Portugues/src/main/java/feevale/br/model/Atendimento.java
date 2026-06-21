/*
 * Classe abstrata base para qualquer tipo de atendimento.
 *
 * Ela guarda dados comuns, como número e status. Por ser abstrata, obriga
 * as subclasses a implementarem o cálculo do valor total.
 */
package feevale.br.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public abstract class Atendimento {
    private final String numeroAtendimento;
    private final StringProperty status = new SimpleStringProperty();
    public Atendimento(String numeroAtendimento, String statusInicial) { this.numeroAtendimento = numeroAtendimento; this.status.set(statusInicial); }
    public abstract double calcularValorTotal();
    public String getNumeroAtendimento() { return numeroAtendimento; }
    public String getStatus() { return status.get(); }
    public void setStatus(String novoStatus) { this.status.set(novoStatus); }
    public StringProperty statusProperty() { return status; }
    @Override public String toString() { return numeroAtendimento + " - " + getStatus() + " - R$ " + String.format("%.2f", calcularValorTotal()); }
}
