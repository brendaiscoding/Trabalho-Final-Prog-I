package feevale.br.model;

public class Cupom {
    private String codigo;
    private double percentual;

    public Cupom(String codigo, double percentual) {
        this.codigo = codigo;
        this.percentual = percentual;
    }
    public String getCodigo() { return codigo; }
    public double getPercentual() { return percentual; }
}
