package example;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 * @see http://codekhongkho.com
 * @author ngockhuong
 */

public final class MainPanel extends JPanel {

    private MainPanel() {
        super(new BorderLayout());
        String[] array = {
            "codekhongkho", "lâm ngọc khương", "thử nghiệm", "java swing",
            "custom jcombobox", "học lập trình", "cảm ơn đã ghé thăm", "codekhongkho.com"};
        JComboBox<String> combo = makeComboBox(array);
        combo.setEditable(true);
        combo.setSelectedIndex(-1);
        JTextField field = (JTextField) combo.getEditor().getEditorComponent();
        field.setText("");
        field.addKeyListener(new ComboKeyHandler(combo));

        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder("Auto-Completion ComboBox"));
        p.add(combo, BorderLayout.NORTH);

        Box box = Box.createVerticalBox();
        box.add(makeHelpPanel());
        box.add(Box.createVerticalStrut(5));
        box.add(p);
        add(box, BorderLayout.NORTH);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        setPreferredSize(new Dimension(320, 240));
    }

    private static JComboBox<String> makeComboBox(String... model) {
        return new JComboBox<String>(model);
    }

    private static JPanel makeHelpPanel() {
        JPanel lp = new JPanel(new GridLayout(2, 1, 2, 2));
        lp.add(new JLabel("Char: hiện Popup"));
        lp.add(new JLabel("ESC: ẩn Popup"));

        JPanel rp = new JPanel(new GridLayout(2, 1, 2, 2));
        rp.add(new JLabel("RIGHT: Tự hoàn thành"));
        rp.add(new JLabel("ENTER: Thêm/Lựa chọn"));

        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Help"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 5, 0, 5);
        c.fill = GridBagConstraints.BOTH;
        c.weighty = 1d;

        c.weightx = 1d;
        p.add(lp, c);

        c.weightx = 0d;
        p.add(new JSeparator(SwingConstants.VERTICAL), c);

        c.weightx = 1d;
        p.add(rp, c);

        return p;
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
        JFrame frame = new JFrame("ComboBoxSuggestion - Codekhongkho.Com");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(new MainPanel());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

class ComboKeyHandler extends KeyAdapter {

    private final JComboBox<String> comboBox;
    private final List<String> list = new ArrayList<>();
    private boolean shouldHide;

    protected ComboKeyHandler(JComboBox<String> combo) {
        super();
        this.comboBox = combo;
        for (int i = 0; i < comboBox.getModel().getSize(); i++) {
            list.add((String) comboBox.getItemAt(i));
        }
    }

    @Override
    public void keyTyped(final KeyEvent e) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                String text = ((JTextField) e.getComponent()).getText();
                ComboBoxModel<String> m;
                if (text.isEmpty()) {
                    String[] array = list.toArray(new String[list.size()]);
                    m = new DefaultComboBoxModel<String>(array);
                    setSuggestionModel(comboBox, m, "");
                    comboBox.hidePopup();
                } else {
                    m = getSuggestedModel(list, text);
                    if (m.getSize() == 0 || shouldHide) {
                        comboBox.hidePopup();
                    } else {
                        setSuggestionModel(comboBox, m, text);
                        comboBox.showPopup();
                    }
                }
            }
        });
    }

    @Override
    public void keyPressed(KeyEvent e) {
        JTextField textField = (JTextField) e.getComponent();
        String text = textField.getText();
        shouldHide = false;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_RIGHT:
                for (String s : list) {
                    if (s.startsWith(text)) {
                        textField.setText(s);
                        return;
                    }
                }
                break;
            case KeyEvent.VK_ENTER:
                if (!list.contains(text)) {
                    list.add(text);
                    Collections.sort(list);
                    setSuggestionModel(comboBox, getSuggestedModel(list, text), text);
                }
                shouldHide = true;
                break;
            case KeyEvent.VK_ESCAPE:
                shouldHide = true;
                break;
            default:
                break;
        }
    }

    private static void setSuggestionModel(JComboBox<String> comboBox, ComboBoxModel<String> mdl, String str) {
        comboBox.setModel(mdl);
        comboBox.setSelectedIndex(-1);
        ((JTextField) comboBox.getEditor().getEditorComponent()).setText(str);
    }

    private static ComboBoxModel<String> getSuggestedModel(List<String> list, String text) {
        DefaultComboBoxModel<String> m = new DefaultComboBoxModel<>();
        for (String s : list) {
            if (s.startsWith(text)) {
                m.addElement(s);
            }
        }
        return m;
    }
}
