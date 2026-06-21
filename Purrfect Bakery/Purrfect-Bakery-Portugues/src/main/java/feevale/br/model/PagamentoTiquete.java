/*
 * Tipo de atendimento com valor fixo.
 *
 * Parece ser uma classe complementar/alternativa, não muito usada no fluxo
 * principal atual do quiosque.
 */
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