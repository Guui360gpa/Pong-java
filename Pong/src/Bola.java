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

    //Região retangular da bola
    public Rectangle getRetangulo() {
        return new Rectangle(x,y,diametro,diametro);
    }

    //Avança a bola um passo (soma velocidade á posição)
    public void mover() {
        x += velocidadeX;
        y += velocidadeY;
    }

    //Corrige a posição e passa a descer
    public void rebaterNoTeto() {
        y = 0;
        velocidadeY = Math.abs(velocidadeY);
    }

    //Corrige aposição e passa a subir
    public void rebaterNoChao(int alturaCampo) {
        y = alturaCampo -diametro;
        velocidadeY = -Math.abs(velocidadeY);
    }

    //Rebote em uma raquete á Esquerda
    public void rebaterParaDireita(int novoX) {
        x = novoX;
        velocidadeX = Math.abs(velocidadeX);
    }

    public void rebaterParaEsquerda(int novoX) {
        x = novoX;
        velocidadeX = -Math.abs(velocidadeX);
    }

    //Desenha a bola
    public void desenhar(Graphics g){
        g.setColor(Color.WHITE);
        g.fillOval(x,y,diametro,diametro);
    }
}
