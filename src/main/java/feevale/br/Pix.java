package feevale.br;

public class Pix implements MetodoPagamento {
    private String codigo;

    public Pix(String codigo) { this.codigo = codigo; }
    public String getCodigo() { return codigo; }

    @Override
    public void processarPagamento(String dados) {
        // Lógica de pagamento via PIX
    }
}
