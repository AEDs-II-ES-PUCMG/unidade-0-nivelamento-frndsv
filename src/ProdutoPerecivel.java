import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProdutoPerecivel extends Produto {
    
    protected static final double DESCONTO = 0.25;
    protected static final int PRAZO_DESCONTO = 7;
    private LocalDate dataDeValidade;

    public ProdutoPerecivel(String desc, double precoCusto, double margemLucro, LocalDate dataDeValidade) {
        super(desc, precoCusto, margemLucro);

        if (dataDeValidade.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de validade não pode ser anterior a hoje.");
        }
        this.dataDeValidade = dataDeValidade;
    }

    @Override
    public double valorDeVenda() {
        long prazoValidade = ChronoUnit.DAYS.between(LocalDate.now(), dataDeValidade);
        
        double vendaNormal = precoCusto * (1 + margemLucro);

        if(prazoValidade <= PRAZO_DESCONTO && prazoValidade > 0) {
            return vendaNormal * (1 - DESCONTO);
        } 
        
        return vendaNormal;
    }
}
