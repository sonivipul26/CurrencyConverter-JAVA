import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class BackgroundPanel extends JPanel {
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        int width = getWidth();
        int height = getHeight();
        
        // Multi-color gradient background
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(238, 242, 255),
            width, height, new Color(224, 231, 255)
        );
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);
        
        // Add decorative circles
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.08f));
        
        // Large circle top right
        g2d.setColor(new Color(99, 102, 241));
        g2d.fillOval(width - 300, -100, 400, 400);
        
        // Medium circle bottom left
        g2d.setColor(new Color(16, 185, 129));
        g2d.fillOval(-100, height - 250, 350, 350);
        
        // Small circle middle
        g2d.setColor(new Color(245, 158, 11));
        g2d.fillOval(width - 150, height / 2, 200, 200);
        
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }
}