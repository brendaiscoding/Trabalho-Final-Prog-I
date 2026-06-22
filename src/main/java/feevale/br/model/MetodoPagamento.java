/*
 * Interface para padronizar métodos de pagamento.
 *
 * Qualquer forma de pagamento, como PIX ou cartão, precisa implementar
 * o método processarPagamento.
 */
package feevale.br.model;

public interface MetodoPagamento {
    void processarPagamento(String dados);
}