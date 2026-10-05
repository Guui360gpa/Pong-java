public enum Dificuldade {

    FACIL("Facil",4,4, 3, 10),
    DIFICIL("Difícil",10,6, 5, 10);

    private final String rotulo;
    private final int velocidadeBolaX;
    private final int velocidadeBolaY;
    private final int velocidadeComputador;
    private final int tolerancia;

    Dificuldade(String rotulo,int velocidadeBolaX,int velocidadeBolaY, int velocidadeComputador, int tolerancia) {
        this.rotulo = rotulo;
        this.velocidadeBolaX = velocidadeBolaX;
        this.velocidadeBolaY = velocidadeBolaY;
        this.velocidadeComputador = velocidadeComputador;
        this.tolerancia = tolerancia;
    }

    public int getVelocidadeBolaX() {
        return velocidadeBolaX;
    }
    public int getVelocidadeBolaY() {
        return velocidadeBolaY;
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
