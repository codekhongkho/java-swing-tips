package example;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
/**
 * @see http://codekhongkho.com
 * @author ngockhuong
 */
public final class MainPanel extends JPanel {

    private MainPanel() {
        super(new BorderLayout());
        JComboBox<LRItem> combo = new JComboBox<LRItem>(makeModel()) {
            @Override
            public void updateUI() {
                super.updateUI();
                setRenderer(new MultiColumnCellRenderer<LRItem>());
            }
        };
        add(makeTitledBox("MultiColumnComboBox", combo), BorderLayout.NORTH);
        add(makeTitledBox("DefaultComboBox", new JComboBox<LRItem>(makeModel())), BorderLayout.SOUTH);
        setPreferredSize(new Dimension(320, 240));
    }

    private static Box makeTitledBox(String title, JComboBox combo) {
        final JTextField leftTextField = new JTextField();
        final JTextField rightTextField = new JTextField();
        leftTextField.setEditable(false);
        rightTextField.setEditable(false);
        Box box = Box.createVerticalBox();
        box.setBorder(BorderFactory.createTitledBorder(title));
        box.add(Box.createVerticalStrut(2));
        box.add(combo);
        box.add(Box.createVerticalStrut(2));
        box.add(leftTextField);
        box.add(Box.createVerticalStrut(2));
        box.add(rightTextField);
        combo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                LRItem item = (LRItem) e.getItem();
                leftTextField.setText("Tỉnh/T.P: " + item.getLeftText());
                rightTextField.setText("Mã vùng ĐT: " + item.getRightText());
            }
        });
        return box;
    }

    private static ComboBoxModel<LRItem> makeModel() {
        DefaultComboBoxModel<LRItem> model = new DefaultComboBoxModel<>();
        model.addElement(new LRItem("Quảng Nam", "0510"));
        model.addElement(new LRItem("Đà Nẵng", "0511"));
        model.addElement(new LRItem("Thừa Thiên Huế", "054"));
        model.addElement(new LRItem("Quảng Ngãi", "055"));
        model.addElement(new LRItem("Quảng Trị", "053"));
        model.addElement(new LRItem("Nghệ An", "038"));
        model.addElement(new LRItem("Thanh Hóa", "037"));
        model.addElement(new LRItem("Hà Tĩnh", "039"));

        return model;
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
        JFrame frame = new JFrame("ClippedLRComboBox - Codekhongkho.Com");
        frame.setMinimumSize(new Dimension(500, 100));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class MultiColumnCellRenderer<E extends LRItem> implements ListCellRenderer<E> {

    private final JLabel leftLabel = new JLabel() {
        @Override
        public void updateUI() {
            super.updateUI();
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        }
    };
    private final JLabel rightLabel = new JLabel() {
        @Override
        public void updateUI() {
            super.updateUI();
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
            setForeground(Color.GRAY);
            setHorizontalAlignment(SwingConstants.RIGHT);
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(80, 0);
        }
    };
    private final JPanel renderer = new JPanel(new BorderLayout()) {
        @Override
        public Dimension getPreferredSize() {
            Dimension d = super.getPreferredSize();
            return new Dimension(0, d.height);
        }

        @Override
        public void updateUI() {
            super.updateUI();
            setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
        }
    };

    @Override
    public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
        leftLabel.setText(value.getLeftText());
        rightLabel.setText(value.getRightText());

        leftLabel.setFont(list.getFont());
        rightLabel.setFont(list.getFont());

        renderer.add(leftLabel);
        renderer.add(rightLabel, BorderLayout.EAST);

        if (index < 0) {
            leftLabel.setForeground(list.getForeground());
            renderer.setOpaque(false);
        } else {
            leftLabel.setForeground(isSelected ? list.getSelectionForeground() : list.getForeground());
            renderer.setBackground(isSelected ? list.getSelectionBackground() : list.getBackground());
            renderer.setOpaque(true);
        }
        return renderer;
    }
}

class LRItem {

    private final String leftText;
    private final String rightText;

    protected LRItem(String strLeft, String strRight) {
        this.leftText = strLeft;
        this.rightText = strRight;
    }

    public String getLeftText() {
        return leftText;
    }

    public String getRightText() {
        return rightText;
    }

    @Override
    public String toString() {
        return leftText + " / " + rightText;
    }
}
