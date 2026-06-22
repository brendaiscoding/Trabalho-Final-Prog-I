/*
 * Implementação de pagamento por cartão.
 *
 * No projeto atual, a classe guarda o tipo do cartão e deixa preparado
 * o método processarPagamento para uma futura integração real.
 */
package feevale.br.model;

public class Cartao implements MetodoPagamento {
    private String tipo; // "Crédito" ou "Débito"

    public Cartao(String tipo) {
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public void processarPagamento(String dados) {
        // Lógica de pagamento via Cartão
    }
}
