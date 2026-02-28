public class ProdutoNaoPerecivel extends Produto {
    
    // Construtor com margem de lucro informada
    public ProdutoNaoPerecivel(String desc, double precoCusto, double margemLucro) {
        super(desc, precoCusto, margemLucro);
    }

    // Construtor utilizando margem padrão
    public ProdutoNaoPerecivel(String desc, double precoCusto) {
        super(desc, precoCusto, MARGEM_PADRAO);
    }

    @Override
    public double valorDeVenda() {
        return precoCusto * (1 + margemLucro);
    }
}
