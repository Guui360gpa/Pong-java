import java.awt.*;

public class Raquete {

    //Posição da raquete na janela
    private int x;
    private int y;

    //Dimenção da raquete
    private final int largura;
    private final int altura;

    private final int velocidade;

    //define o limite inferior de movimento
    private final int alturaCampo;

    public Raquete(int x, int y, int largura, int altura, int velocidade, int alturaCampo) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.velocidade = velocidade;
        this.alturaCampo = alturaCampo;
    }

    public void moverParaCima() {
        y = Math.max(0,y - velocidade);
    }

    public void moverParaBaixo() {
        y = Math.min(alturaCampo - altura, y + velocidade);
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getLargura() {
        return largura;
    }
    public int getAltura() {
        return altura;
    }
    public int getVelocidade() {
        return velocidade;
    }

    public void desenhar(Graphics g){
        g.setColor(Color.WHITE);
        g.fillRect(x,y,largura,altura);
    }
}
