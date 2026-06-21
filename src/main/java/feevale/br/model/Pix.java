/*
 * Implementação de pagamento por PIX.
 *
 * Guarda uma chave/código PIX e deixa preparado o método processarPagamento
 * para uma futura integração real.
 */
package feevale.br.model;

public class Pix implements MetodoPagamento {
    private String codigo;

    public Pix(String codigo) {
        this.codigo = codigo;
    }

    public String getCodigo() {
        return codigo;
    }

    @Override
    public void processarPagamento(String dados) {
        // Lógica de pagamento via PIX
    }
}
