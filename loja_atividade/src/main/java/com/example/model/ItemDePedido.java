package com.example.model;

public class ItemDePedido {

    // Atributos encapsulados
    private Produto produto;
    private int quantidade;
    private double precoVenda;

    /**
     * Construtor da classe ItemDePedido.
     * O precoVenda deve ser capturado do produto no momento da criação do item,
     * garantindo que alterações futuras no preço do produto não afetem este pedido.
     */
    public ItemDePedido(Produto produto, int quantidade, double precoVenda) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoVenda = produto.valorVenda();
    }

    public void setQuantidade(int quantidade){
        this.quantidade += quantidade;
    }

    public double calcularSubtotal() {
        Double subtotal = precoVenda * quantidade;
        return subtotal;
    }

    public Produto getProduto(){
        return produto;
    }

    public int getQuantidade(){
        return quantidade;
    }

    public Double getPrecoVenda(){
        return precoVenda;
    }
    // --- Sobrescrita do método equals ---

    /**
     * Compara a igualdade entre dois itens de pedido.
     * A regra de negócio define que dois itens são iguais se possuírem o mesmo Produto.
     */
    @Override
    public boolean equals(Object obj) {
        ItemDePedido outro = (ItemDePedido) obj;
        return this.produto.equals(outro.produto);
    }
}
