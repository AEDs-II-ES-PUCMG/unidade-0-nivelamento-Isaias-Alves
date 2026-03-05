package com.example.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;

public class ProdutoPerecivel extends Produto {

    private LocalDate dataValidade;
    private static final double DESCONTO = 0.25;
    private static final int PRAZO_DESCONTO = 7;

    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate validade, int quantidadeEmEstoque) {
        super(desc, precoCusto, margemLucro, quantidadeEmEstoque);
        setDataValidade(validade);
        if (dataValidade.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de validade não pode ser mais antiga que a data atual");
        }
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    @Override
    public double valorVenda() {
        if (LocalDate.now().isAfter(dataValidade)) {
            return 0.0;
        }

        double precoNormal = super.valorVenda();
        long diasParaVencer = ChronoUnit.DAYS.between(LocalDate.now(), dataValidade);
        if (diasParaVencer <= PRAZO_DESCONTO) {
            return precoNormal * (1 - DESCONTO);
        }
        return precoNormal;
    }

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataFormatada = formato.format(dataValidade);
        return super.toString() + " | Validade: " + dataFormatada;
    }

    /** 
     * Gera uma linha de texto a partir dos dados do produto. Preço e margem de
     * lucro vão formatados com 2 casas
     * decimais.
     * Data de validade vai no formato dd/mm/aaaa
     * 
     * @return Uma string no formato "2;
     *         descrição;preçoDeCusto;margemDeLucro;dataDeValidade"
     */
    @Override
    public String gerarDadosTexto() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String precoFormatado = String.format("%.2f", getPrecoCusto()).replace(',', '.');
        String margemFormatada = String.format("%.2f", getMargemLucro()).replace(',', '.');
        String quantidadeFormatada = String.format("%d", getQuantidadeEmEstoque()).replace(',', '.');
        String dataFormatada = formato.format(dataValidade);
        return String.format(java.util.Locale.US, "2;%s;%s;%s;%s;%s", 
                         getDescricao(), precoFormatado, margemFormatada, dataFormatada, quantidadeFormatada);
    }

}