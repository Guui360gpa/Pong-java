import javax.swing.*;

public class JanelaJogo extends JFrame {

    public JanelaJogo(){
        super("Pong em Java"); //Título da janela
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Fechar a janela encerra o processo
        setResizable(false); //tamanho fixo
        add(new CampoJogo()); //adiciona o painel
        pack(); //ajusta a janela a o painel adicionado
        setLocationRelativeTo(null); //centraliza na tela
        setVisible(true); //exibe
    }
}
