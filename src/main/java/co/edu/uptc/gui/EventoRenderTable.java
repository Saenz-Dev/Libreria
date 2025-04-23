package co.edu.uptc.gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class EventoRenderTable implements TableCellRenderer {

    JCheckBox check = new JCheckBox();
    JLabel label = new JLabel();

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String isbn = (String) table.getValueAt(row, 0);

        //if (isbn.equals("Subtotal") || isbn.equals("Impuestos") || isbn.equals("Des. Premium") || isbn.equals("Desc. Frecuencia") || isbn.equals("Total")) {
        if (!isbn.isEmpty() && isbn != null) {
            check.setSelected(Boolean.TRUE.equals(value));
            check.setHorizontalAlignment(JLabel.CENTER);
            return check;
        } else {
            label.setText("N/A");
            label.setHorizontalAlignment(JLabel.CENTER);
            return label;
        }
    }
}
