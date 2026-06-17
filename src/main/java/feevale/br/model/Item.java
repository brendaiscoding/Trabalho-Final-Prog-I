package feevale.br.model;

public class Item {
    private String nome;
    private double preco;
    private String tamanho;
    private Categoria categoria;
    private Alergenicos[] alergenicos;

    public Item(String nome, double preco, String tamanho, Categoria categoria, Alergenicos[] alergenicos) {
        this.nome = nome;
        this.preco = preco;
        this.tamanho = tamanho;
        this.categoria = categoria;
        this.alergenicos = alergenicos;
    }

    // Getters e Setters padrão
    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public String getTamanho() {
        return tamanho;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Alergenicos[] getAlergenicos() {
        return alergenicos;
    }
}
