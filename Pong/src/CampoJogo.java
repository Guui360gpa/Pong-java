import javax.swing.*;
import java.awt.*;

public class CampoJogo extends JPanel {

    //Dimenção constantes da Janela
    public static final int LARGURA = 800;
    public static final int ALTURA = 600;

    //Dimenções constantes da Raquete
    private static final int LARGURA_RAQUETE = 15;
    private static final int ALTURA_RAQUETE = 100;
    private static final int MARGEM_RAQUETE = 30;
    private static final int VELOCIDADE_RAQUETE = 6;

    //Controla as raquetes
    private final Raquete raqueteJogador;
    private final Raquete raqueteComputador;

    public CampoJogo(){
        setPreferredSize(new Dimension(LARGURA,ALTURA)); //configura dimenção da janela
        setBackground(Color.BLACK); //muda a cor do background

        int yCentralizado = (ALTURA - ALTURA_RAQUETE) / 2;

        //Esquerda
        raqueteJogador = new Raquete(
                MARGEM_RAQUETE,yCentralizado,
                LARGURA_RAQUETE, ALTURA_RAQUETE,VELOCIDADE_RAQUETE
        );

        //Direita
        raqueteComputador = new Raquete(
                LARGURA - MARGEM_RAQUETE - LARGURA_RAQUETE, yCentralizado,
                LARGURA_RAQUETE,ALTURA_RAQUETE, VELOCIDADE_RAQUETE
        );
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

        raqueteJogador.desenhar(g);
        raqueteComputador.desenhar(g);
    }
}
