import javax.swing.*;

public class Pong {
    public static void main(String[] args) {
        System.out.println("Game Started");
        System.out.println("Would you like to enable random mode? Y/n");
        if (new java.util.Scanner(System.in).nextLine().equalsIgnoreCase("y")) { // create a random scanner cause ima never need it again
            PongPanel.randomAllowed = true;
        }
        
        // setup swing
        JFrame frame = new JFrame("Pong");
        PongPanel pongPanel = new PongPanel();
        frame.add(pongPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(PongPanel.WIDTH, PongPanel.HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);

        pongPanel.startGame();
    }
}
