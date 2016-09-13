package example;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.util.*;
import java.util.List;
import javax.swing.*;
/**
 * @see http://codekhongkho.com
 * @author ngockhuong
 */
public final class MainPanel extends JPanel {

    private final JComboBox<? extends Enum> alignmentsChoices = new JComboBox<>(ButtonAlignments.values());
    private final List<? extends JButton> buttons;
    private final Box box = Box.createHorizontalBox();

    public MainPanel() {
        super(new BorderLayout());
        buttons = Arrays.asList(
                new RoundButton(new ImageIcon(getClass().getResource("005.png")), "005d.png", "005g.png"),
                new RoundButton(new ImageIcon(getClass().getResource("003.png")), "003d.png", "003g.png"),
                new RoundButton(new ImageIcon(getClass().getResource("001.png")), "001d.png", "001g.png"),
                new RoundButton(new ImageIcon(getClass().getResource("002.png")), "002d.png", "002g.png"),
                new RoundButton(new ImageIcon(getClass().getResource("004.png")), "004d.png", "004g.png"));

        box.setOpaque(true);
        box.setBackground(new Color(120, 120, 160));
        box.add(Box.createHorizontalGlue());
        box.setBorder(BorderFactory.createEmptyBorder(60, 10, 60, 10));
        for (JButton b : buttons) {
            box.add(b);
            box.add(Box.createHorizontalStrut(5));
        }
        box.add(Box.createHorizontalGlue());
        add(box, BorderLayout.NORTH);

        JPanel p = new JPanel();
        p.add(new JCheckBox(new AbstractAction("ButtonBorder Color") {
            @Override
            public void actionPerformed(ActionEvent e) {
                JCheckBox cb = (JCheckBox) e.getSource();
                Color bgc = cb.isSelected() ? Color.WHITE : Color.BLACK;
                for (JButton b : buttons) {
                    b.setBackground(bgc);
                }
                box.repaint();
            }
        }));
        alignmentsChoices.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                ButtonAlignments ba = (ButtonAlignments) alignmentsChoices.getSelectedItem();
                for (JButton b : buttons) {
                    b.setAlignmentY(ba.alingment);
                }
                box.revalidate();
            }
        });
        alignmentsChoices.setSelectedIndex(1);
        p.add(alignmentsChoices);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(p, BorderLayout.SOUTH);
        setPreferredSize(new Dimension(320, 240));
    }

    public static void main(String... args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        JFrame frame = new JFrame("RoundImageButton - Codekhongkho.Com");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class RoundButton extends JButton {

    protected Shape shape;
    protected Shape base;

    protected RoundButton(Icon icon, String i2, String i3) {
        super(icon);
        setPressedIcon(new ImageIcon(getClass().getResource(i2)));
        setRolloverIcon(new ImageIcon(getClass().getResource(i3)));
    }

    @Override
    public void updateUI() {
        super.updateUI();
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        setBackground(Color.BLACK);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setAlignmentY(Component.TOP_ALIGNMENT);
        initShape();
    }

    @Override
    public Dimension getPreferredSize() {
        Icon icon = getIcon();
        Insets i = getInsets();
        int iw = Math.max(icon.getIconWidth(), icon.getIconHeight());
        return new Dimension(iw + i.right + i.left, iw + i.top + i.bottom);
    }

    protected void initShape() {
        if (!getBounds().equals(base)) {
            Dimension s = getPreferredSize();
            base = getBounds();
            shape = new Ellipse2D.Double(0, 0, s.width - 1, s.height - 1);
        }
    }

    @Override
    protected void paintBorder(Graphics g) {
        initShape();
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setPaint(getBackground());
        g2.draw(shape);
        g2.dispose();
    }

    @Override
    public boolean contains(int x, int y) {
        initShape();
        return Objects.nonNull(shape) && shape.contains(x, y);
    }
}

enum ButtonAlignments {
    Top("Top Alignment", Component.TOP_ALIGNMENT),
    Center("Center Alignment", Component.CENTER_ALIGNMENT),
    Bottom("Bottom Alignment", Component.BOTTOM_ALIGNMENT);
    private final String description;
    public final float alingment;

    ButtonAlignments(String description, float alingment) {
        this.description = description;
        this.alingment = alingment;
    }

    @Override
    public String toString() {
        return description;
    }
}
