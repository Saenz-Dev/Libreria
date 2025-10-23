package co.edu.uptc.gui;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase que gestiona los eventos de edición de celdas en la tabla de comentarios de compras.
 * Permite detectar cuando el usuario activa la opción de ver detalles de una compra desde la tabla.
 */
public class EventoComentario implements CellEditorListener {

    /** Tabla de comentarios sobre la que se detectan los eventos. */
    private JTable tablaComentarios;
    /** Referencia a la ventana principal para mostrar los detalles de la compra. */
    private VentanaPrincipal ventanaPrincipal;

    /**
     * Constructor de EventoComentario.
     * @param tablaComentarios Tabla de comentarios a observar.
     * @param ventanaPrincipal Ventana principal de la aplicación.
     */
    public EventoComentario(JTable tablaComentarios, VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.tablaComentarios = tablaComentarios;
    }

    /**
     * Se ejecuta cuando se termina de editar una celda. Si la celda de ver compra está activada,
     * muestra los detalles de la compra correspondiente.
     * @param e Evento de cambio de celda.
     */
    @Override
    public void editingStopped(ChangeEvent e) {
        int row = tablaComentarios.getSelectedRow();
        String fecha = (String) tablaComentarios.getValueAt(row, 0);
        int numeroRecibo = (int) tablaComentarios.getValueAt(row, 1);
        Boolean valor = (Boolean) tablaComentarios.getValueAt(row, 2);
        if (valor) {
            ventanaPrincipal.activarPanelVerCompra(fecha, numeroRecibo);
        }
    }

    /**
     * Se ejecuta si se cancela la edición de la celda (no realiza ninguna acción).
     * @param e Evento de cambio de celda.
     */
    @Override
    public void editingCanceled(ChangeEvent e) {}
}
