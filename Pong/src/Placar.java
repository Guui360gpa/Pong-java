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

    //O jogador venceu se atingiu (ou passou) a meta de pontos.
    public boolean jogadorVenceu(int meta) {
        return pontosJogador >= meta;
    }

    //O computador venceu se atingiu (ou passou) a meta de pontos.
    public boolean computadorVenceu(int meta) {
        return pontosComputador >= meta;
    }

    public void zerar() {
        pontosComputador = 0;
        pontosJogador = 0;
    }
}
