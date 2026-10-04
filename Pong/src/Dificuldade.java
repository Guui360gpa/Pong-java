public enum Dificuldade {

    FACIL("Facil", 2, 30),
    DIFICIL("Difícil", 4, 10);

    private final String rotulo;
    private final int velocidadeComputador;
    private final int tolerancia;

    Dificuldade(String rotulo, int velocidadeComputador, int tolerancia) {
        this.rotulo = rotulo;
        this.velocidadeComputador = velocidadeComputador;
        this.tolerancia = tolerancia;
    }

    public int getVelocidadeComputador(){
        return velocidadeComputador;
    }
    public int getTolerancia() {
        return tolerancia;
    }

    @Override
    public String toString() {
        return rotulo;
    }
}
