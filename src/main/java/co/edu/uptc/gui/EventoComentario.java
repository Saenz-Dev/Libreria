package co.edu.uptc.gui;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EventoComentario implements CellEditorListener {

    private JTable tablaComentarios;
    private VentanaPrincipal ventanaPrincipal;

    public EventoComentario(JTable tablaComentarios, VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.tablaComentarios = tablaComentarios;
    }

    @Override
    public void editingStopped(ChangeEvent e) {
        int row = tablaComentarios.getSelectedRow();
        String fecha = (String) tablaComentarios.getValueAt(row, 0);
        int numeroRecibo = (int) tablaComentarios.getValueAt(row, 1);
        /*String isbn = (String) tablaComentarios.getValueAt(row, 1);
        String nombreLibro = (String) tablaComentarios.getValueAt(row, 2);*/
        Boolean valor = (Boolean) tablaComentarios.getValueAt(row, 2);
        if (valor) {
            ventanaPrincipal.activarPanelVerCompra(fecha, numeroRecibo);
        }
    }

    @Override
    public void editingCanceled(ChangeEvent e) {}
}
