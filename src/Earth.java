import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Earth {
    public static void main(String[] args) throws IOException {
        new MainWindow();
    }
}

class MainWindow extends JFrame {

    MainPanel panel;

    public MainWindow () throws IOException {
        super("earth");

        panel = new MainPanel();
        panel.addComponentListener(new MainPanelListener());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.setPreferredSize(new Dimension(800, 600));

        this.setLocation(100, 60);

        this.pack();

        this.setVisible(true);

        this.add(panel);

        Timer timer = new Timer(100, new PaintPanelListener(panel));
        timer.start();
    }
}

class MainPanel extends JPanel {
    BufferedImage earthImg, sunImg;
    boolean init;
    int x, y, xc, yc, dt, t, r;

    public MainPanel() {
        super();

        init = false;

        try {
            earthImg = ImageIO.read(new File("earth.png"));
            sunImg = ImageIO.read(new File("sun.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initDimensions() {
        t = 0;
        dt = 2;
        xc = getWidth() / 2;
        yc = getHeight() / 2;
        r = Math.min(xc, yc) / 2;
        init = true;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!init) {
            initDimensions();
        }

        t += dt;
        x = (int) (xc + r * Math.cos(t * Math.PI / 180));
        y = (int) (yc + r * Math.sin(t * Math.PI / 180));
        buildPicture(g, x, y);
    }

    public void repaintImage() {
        repaint();
    }

    protected void buildPicture(Graphics g, int x, int y) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.drawImage(sunImg, xc - sunImg.getWidth() / 2, yc - sunImg.getHeight() / 2, this);
        g.drawImage(earthImg, x - earthImg.getWidth() / 2, y - earthImg.getHeight() / 2, this);
    }
}

class PaintPanelListener implements ActionListener {
    JPanel panel;

    public PaintPanelListener(JPanel panel) {
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ((MainPanel) panel).repaintImage();
    }
}

class MainPanelListener extends ComponentAdapter {
    @Override
    public void componentResized(ComponentEvent e) {
        super.componentResized(e);
        ((MainPanel) e.getComponent()).init = false;
    }
}
