import javax.swing.*;

public class JanelaJogo extends JFrame {

    public JanelaJogo(){
        super("Pong em Java"); //Título da janela
        setDefaultCloseOperation(EXIT_ON_CLOSE); //Fechar a janela encerra o processo
        setResizable(false); //tamanho fixo

        Dificuldade dificuldade = escolherDificuldade();
        add(new CampoJogo(dificuldade)); //adiciona o painel
        pack(); //ajusta a janela a o painel adicionado

        setLocationRelativeTo(null); //centraliza na tela
        setVisible(true); //exibe
    }

    //Mostra um diálogo com um botão por nível
    private Dificuldade escolherDificuldade() {
        Dificuldade[] opcoes = Dificuldade.values();
        int escolha = JOptionPane.showOptionDialog(
                null,
                "Escolha a dificuldade:",
                "Pong em Java",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoes,
                Dificuldade.FACIL);

        if (escolha < 0) {
            return Dificuldade.FACIL;
        }
        return opcoes[escolha];
    }
}
