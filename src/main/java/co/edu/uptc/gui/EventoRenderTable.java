package co.edu.uptc.gui;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Renderizador personalizado para celdas de tabla que muestra un JCheckBox o un JLabel según el contenido.
 * Se utiliza para mostrar la opción de eliminar productos en tablas de compras o recibos.
 */
public class EventoRenderTable implements TableCellRenderer {

    /** Casilla de verificación utilizada para mostrar la opción de eliminar. */
    JCheckBox check = new JCheckBox();
    /** Etiqueta utilizada para mostrar texto alternativo cuando no hay ISBN. */
    JLabel label = new JLabel();

    /**
     * Devuelve el componente que se usará para renderizar la celda de la tabla.
     * Si el ISBN no está vacío, muestra un JCheckBox; de lo contrario, muestra un JLabel con "N/A".
     *
     * @param table      La tabla que se está renderizando.
     * @param value      El valor de la celda a renderizar.
     * @param isSelected Si la celda está seleccionada.
     * @param hasFocus   Si la celda tiene el foco.
     * @param row        Fila de la celda.
     * @param column     Columna de la celda.
     * @return Componente para renderizar la celda.
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String isbn = (String) table.getValueAt(row, 0);
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