public abstract class Produto {

    private static final double MARGEM_PADRAO = 0.2;
    private String descricao;
    protected double precoCusto;
    protected double margemLucro;

    private void init(String desc, double precoCusto, double margemLucro){
        if (precoCusto < 0) {
            throw new IllegalArgumentException("Preço de custo não pode ser negativo");
        }
        if (margemLucro < 0) {
            throw new IllegalArgumentException("Margem de lucro não pode ser negativa");
        }
        this.descricao = desc;
        this.precoCusto = precoCusto;
        this.margemLucro = margemLucro;
    }

    protected Produto(String desc, double precoCusto, double margemLucro){
        init(desc, precoCusto, margemLucro);
    }

    protected Produto(String desc, double precoCusto){
        init(desc, precoCusto, MARGEM_PADRAO);
    }

    public double valorVenda() {
        return precoCusto * (1 + margemLucro);
    }

    @Override
    public String toString(){
        return String.format("Descrição: %s | Valor de Venda: R$ %.2f", descricao, valorVenda());
    }

    public String getDescricao(){
        return this.descricao;
    }

    public double getPrecoCusto(){
        return this.precoCusto;
    }

    public double getMargemLucro(){
        return this.margemLucro;
    }

    public void setDescricao(String descricao){
        this.descricao = descricao;
    }

    public void setPrecoCusto(double precoCusto){
        this.precoCusto = precoCusto;
    }

    public void setMargemLucro(double margemLucro){
        if(margemLucro > 0){
        this.margemLucro = margemLucro;
        } else {
            this.margemLucro = MARGEM_PADRAO;
        }
    }
}