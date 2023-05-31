import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Objects;

class MinesweeperView extends JFrame {
    MinesweeperModel model;
    MinesweeperController controller;
    JFrame frame;
    private JPanel panel;
    private final int IMAGE_HEIGHT = 50;
    private final int IMAGE_WIDTH = 43;
    private int size;

    MinesweeperView(MinesweeperModel model) {
        this.model = model;
    }

    void init(int side, int countBomb) {
        size = model.getSideCount();
        setTitle("MinesweeperHexagonal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        initPanel(size);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    void initPanel(int size) {
        JPanel panel= new JPanel(new GridLayout(size, size));
        panel.setBackground(Color.white);
        int xStart;
        for (int i = 0; i < size; i++) {
            int y = IMAGE_HEIGHT * i;
            if (i % 2 == 0)
                xStart = 0;
            else
                xStart = IMAGE_WIDTH / 2;
            for (int j = 0; j < size; j++) {
                int x = xStart + j * IMAGE_WIDTH;
                MinesweeperCell cell = model.getCell(i, j);
                if (cell == null) {
                    continue;
                }
                ImageIcon icon  = new ImageIcon("C:\\Users\\4842394\\Desktop\\img\\" + getState(i, j) + ".png");
                JButton cellButton = new JButton(icon);
                cellButton.setBounds(x, y, IMAGE_WIDTH, IMAGE_HEIGHT);
                cellButton.setOpaque(false);
                cellButton.setContentAreaFilled(false);
                cellButton.setBorderPainted(false);
                cellButton.setPreferredSize(new Dimension(IMAGE_WIDTH, IMAGE_HEIGHT));
                int finalI = i;
                int finalJ = j;
                cellButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (cell.notFlagged()) {
                            if (e.getButton() == MouseEvent.BUTTON1)
                                controller.onLeftClick(finalI, finalJ);
                            else if (e.getButton() == MouseEvent.BUTTON3)
                                controller.onRightClick(finalI, finalJ);
                        }
                    }
                });
                panel.add(cellButton);
            }
        }
        add(panel);
    }

    String getState(int strInd, int cellInd) {
        MinesweeperCell cell = model.getCell(strInd, cellInd);
        String name = "";
        if (cell != null) {
            if (model.isGameOver() && cell.mined())
                name = "bombed";
            if (cell.closed())
                name = "closed";
            if (cell.opened()) {
                name = "opened";
                panel.repaint();
                if (cell.empty())
                    name = "zero";
                else if (model.countMinesAround(strInd, cellInd) > 0)
                    name = String.valueOf(model.countMinesAround(strInd, cellInd));
                else if (cell.mined())
                    name = "bombed";
            } else if (cell.flagged())
                name = "flagged";
        }
        return name;
    }

    int[] getGameSettings() {
        JTextField cellsField = new JTextField();
        JTextField bombField = new JTextField();
        Object[] message = {"Side size (min 4, max 9):", cellsField, "Number of bomb (min 7):", bombField};
        int option = JOptionPane.showConfirmDialog(frame, message, "Game settings", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            int cells = Integer.parseInt(cellsField.getText());
            int bombs = Integer.parseInt(bombField.getText());
            return new int[]{cells, bombs};
        } else
            return null;
    }

    void showWinMessage() {
        JOptionPane.showMessageDialog(frame, "You won! :D");
    }

    void showGameOverMessage() {
        JOptionPane.showMessageDialog(frame, "You lose! :(");
    }

}
