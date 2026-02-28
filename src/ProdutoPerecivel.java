import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @Override
    public String toString() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dados = super.toString();
        dados += "\nVálido até " + formato.format(dataDeValidade);
        return dados;
    }

    /**
    * Gera uma linha de texto a partir dos dados do produto. Preço e margem de lucro vão formatados com 2 casas
    decimais.
    * Data de validade vai no formato dd/mm/aaaa
    * @return Uma string no formato "2; descrição;preçoDeCusto;margemDeLucro;dataDeValidade"
    */
    @Override
    public String gerarDadosTexto() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String precoFormatado = String.format("%.2f", precoCusto).replace(",", ".");
        String margemFormatada = String.format("%.2f", margemLucro).replace(",", ".");
        String dataFormatada = formato.format(dataDeValidade);
        return String.format("2;%s,%f,%f,%s", descricao, precoFormatado, margemFormatada, dataFormatada);
    }
}
