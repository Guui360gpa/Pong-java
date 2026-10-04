public class ControleComputador {

    //Faixa, em pixels, em que a diferença entre os centros NÃO gera movimento
    private final int tolerancia;

    public ControleComputador(int tolerancia){
        this.tolerancia = tolerancia;
    }

    //Compara o centro da bola com o centro da raquete e move a raquete (no máximo uma vez, na velocidade e nos limites da própria raquete)
    public void atualizar(Raquete raquete, Bola bola) {
        double centroBola = bola.getY() + bola.getDiametro() / 2.0;
        double centroRaquete = raquete.getY() + raquete.getAltura() / 2.0;
        double diferenca = centroBola - centroRaquete;

        if (Math.abs(diferenca) <= tolerancia) {
            return; // Não se move
        }

        if (diferenca < 0) {
            raquete.moverParaCima(); //bola está acima do centro da raquete
        } else {
            raquete.moverParaBaixo(); // bola está abaixo do centro da raquete
        }
    }
}
