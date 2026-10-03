import java.awt.*;

public class Bola {

    //Posição da bola
    private int x;
    private int y;

    //Dimenção da bola
    private final int diametro;

    //Deslocamento por atualização
    private int velocidadeX;
    private int velocidadeY;

    public Bola(int x, int y, int diametro, int velocidadeX, int velocidadeY) {
        this.x = x;
        this.y = y;
        this.diametro = diametro;
        this.velocidadeX = velocidadeX;
        this.velocidadeY = velocidadeY;
    }

    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public int getDiametro() {
        return diametro;
    }
    public int getVelocidadeX() {
        return velocidadeX;
    }
    public int getVelocidadeY() {
        return velocidadeY;
    }

    //Avança a bola um passo (soma velocidade á posição)
    public void mover() {
        x += velocidadeX;
        y += velocidadeY;
    }

    //Desenha a bola
    public void desenhar(Graphics g){
        g.setColor(Color.WHITE);
        g.fillOval(x,y,diametro,diametro);
    }
}
