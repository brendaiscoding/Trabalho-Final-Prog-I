/*
 * Modelo que representa um item/produto do cardápio.
 *
 * Guarda nome, preço, tamanho, categoria, alergênicos, imagem e descrição.
 * Também define equals/hashCode usando o nome, o que permite usar Item como chave no carrinho.
 */
package feevale.br.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class Item {
    private String nome;
    private double preco;
    private String tamanho;
    private Categoria categoria;
    private Alergenicos[] alergenicos;
    private String icone;
    private String descricao;

    public Item(String nome, double preco, String tamanho, Categoria categoria, Alergenicos[] alergenicos) {
        this(nome, preco, tamanho, categoria, alergenicos, "🍞", "Produto da padaria");
    }
    public Item(String nome, double preco, String tamanho, Categoria categoria, Alergenicos[] alergenicos, String icone, String descricao) {
        this.nome = nome; this.preco = preco; this.tamanho = tamanho; this.categoria = categoria; this.alergenicos = alergenicos; this.icone = icone; this.descricao = descricao;
    }
    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public String getTamanho() { return tamanho; }
    public Categoria getCategoria() { return categoria; }
    public Alergenicos[] getAlergenicos() { return alergenicos; }
    public String getIcone() { return icone; }
    public String getDescricao() { return descricao; }
    public String getEspecificacoes() {
        String alerg = "sem alérgenos cadastrados";
        if (alergenicos != null && alergenicos.length > 0) alerg = Arrays.stream(alergenicos).map(Alergenicos::getNome).collect(Collectors.joining(", "));
        return tamanho + " • " + descricao + " • " + alerg;
    }
    @Override public String toString() { return nome + " - R$ " + String.format("%.2f", preco); }
    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof Item item)) return false; return Objects.equals(nome, item.nome); }
    @Override public int hashCode() { return Objects.hash(nome); }
}
