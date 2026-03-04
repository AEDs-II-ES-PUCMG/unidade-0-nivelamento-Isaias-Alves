package com.example.model;

public class ProdutoNaoPerecivel extends Produto {

    public ProdutoNaoPerecivel(String desc, double precoCusto, double margemLucro) {
        super(desc, precoCusto, margemLucro);
    }

    public ProdutoNaoPerecivel(String desc, double precoCusto) {
        super(desc, precoCusto);
    }

    @Override
    public String gerarDadosTexto() {
        // Usamos Locale.US para garantir que o separador decimal seja ponto (.) e não
        // vírgula (,) no arquivo
        return String.format(java.util.Locale.US, "1;%s;%.2f;%.2f",
                getDescricao(), getPrecoCusto(), getMargemLucro());
    }

}