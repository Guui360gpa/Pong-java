import javax.swing.*;
import java.awt.*;

public class CampoJogo extends JPanel {

    public static final int LARGURA = 800;
    public static final int ALTURA = 600;

    public CampoJogo(){
        setPreferredSize(new Dimension(LARGURA,ALTURA)); //configura dimenção da janela
        setBackground(Color.BLACK); //muda a cor do background
    }

    @Override
    protected void paintComponent(Graphics g) {
        //Desenha o tracejado branco do meio (LARGURA / 2)
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        int x = LARGURA / 2;

        for (int y = 0; y < ALTURA; y+=30) {
            g.drawLine(x,y,x,y+15);
        }
    }
}
