package co.edu.uptc.gui;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;

public class EventoCelda implements CellEditorListener {

    private JTable tablaCompras;
    private VentanaPrincipal ventanaPrincipal;

    public EventoCelda(JTable tablaCompras, VentanaPrincipal ventanaPrincipal) {
        this.tablaCompras = tablaCompras;
        this.ventanaPrincipal = ventanaPrincipal;
    }

    @Override
    public void editingStopped(ChangeEvent e) {
        int fila = tablaCompras.getSelectedRow();
        String isbn = (String) tablaCompras.getValueAt(fila, 0);
        Boolean valor = (Boolean) tablaCompras.getValueAt(fila, 4);
        if (valor) {
            ventanaPrincipal.eliminarProductoTabla(isbn);
            ((DefaultTableModel) tablaCompras.getModel()).removeRow(fila);
        }
    }

    @Override
    public void editingCanceled(ChangeEvent e) {
    }

}
