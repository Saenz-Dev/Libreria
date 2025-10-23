package co.edu.uptc.gui;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;

/**
 * Clase que gestiona los eventos de edición de celdas en la tabla de compras.
 * Implementa CellEditorListener para detectar cuando se edita la celda de eliminar producto.
 */
public class EventoCelda implements CellEditorListener {

    /** Tabla de compras sobre la que se detectan los eventos. */
    private JTable tablaCompras;
    /** Referencia a la ventana principal para realizar acciones sobre la compra. */
    private VentanaPrincipal ventanaPrincipal;

    /**
     * Constructor de EventoCelda.
     * @param tablaCompras Tabla de compras a observar.
     * @param ventanaPrincipal Ventana principal de la aplicación.
     */
    public EventoCelda(JTable tablaCompras, VentanaPrincipal ventanaPrincipal) {
        this.tablaCompras = tablaCompras;
        this.ventanaPrincipal = ventanaPrincipal;
    }

    /**
     * Se ejecuta cuando se termina de editar una celda. Elimina el producto si la celda de eliminar está activada.
     * @param e Evento de cambio de celda.
     */
    @Override
    public void editingStopped(ChangeEvent e) {
        int fila = tablaCompras.getSelectedRow();
        String isbn = (String) tablaCompras.getValueAt(fila, 0);
        Boolean valor = (Boolean) tablaCompras.getValueAt(fila, 8);
        if (valor) {
            ventanaPrincipal.eliminarProductoTabla(isbn);
            ((DefaultTableModel) tablaCompras.getModel()).removeRow(fila);
        }
    }

    /**
     * Se ejecuta si se cancela la edición de la celda (no realiza ninguna acción).
     * @param e Evento de cambio de celda.
     */
    @Override
    public void editingCanceled(ChangeEvent e) {
    }

}
