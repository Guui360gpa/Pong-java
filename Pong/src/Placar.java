public class Placar {

    private int pontosJogador;
    private int pontosComputador;

    public Placar() {
        zerar();
    }

    public int getPontosJogador() {
        return pontosJogador;
    }

    public int getPontosComputador() {
        return pontosComputador;
    }

    public void marcarPontoJogador() {
        pontosJogador++;
    }

    public void marcarPontoComputador() {
        pontosComputador++;
    }

    public void zerar() {
        pontosComputador = 0;
        pontosJogador = 0;
    }
}
