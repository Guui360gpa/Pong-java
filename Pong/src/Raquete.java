import java.awt.*;

public class Raquete {

    //Posição da raquete na janela
    private int x;
    private int y;

    //Dimenção da raquete
    private final int largura;
    private final int altura;

    private final int velocidade;

    public Raquete(int x, int y, int largura, int altura, int velocidade) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.velocidade = velocidade;
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
