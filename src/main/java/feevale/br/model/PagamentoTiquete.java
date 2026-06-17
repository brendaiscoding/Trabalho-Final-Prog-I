package feevale.br.model;

public class PagamentoTiquete extends Atendimento {
    private double valorFixoTiquete;

    public PagamentoTiquete(String numeroAtendimento, double valorFixoTiquete) {
        super(numeroAtendimento, "Aguardando Pagamento");
        this.valorFixoTiquete = valorFixoTiquete;
    }

    @Override
    public double calcularValorTotal() {
        return this.valorFixoTiquete;
    }
}