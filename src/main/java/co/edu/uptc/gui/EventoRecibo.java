package co.edu.uptc.gui;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

/**
 * Clase que gestiona los eventos de edición de celdas en la tabla de recibos.
 * Permite detectar cuando el usuario activa la opción de registrar un comentario sobre un libro comprado.
 */
public class EventoRecibo implements CellEditorListener {

    /** Tabla de comentarios sobre la que se detectan los eventos. */
    private JTable tablaComentarios;
    /** Referencia a la ventana principal para registrar el comentario. */
    private VentanaPrincipal ventanaPrincipal;

    /**
     * Constructor de EventoRecibo.
     * @param tablaComentarios Tabla de comentarios a observar.
     * @param ventanaPrincipal Ventana principal de la aplicación.
     */
    public EventoRecibo(JTable tablaComentarios, VentanaPrincipal ventanaPrincipal) {
        this.ventanaPrincipal = ventanaPrincipal;
        this.tablaComentarios = tablaComentarios;
    }

    /**
     * Se ejecuta cuando se termina de editar una celda. Si la celda de registrar comentario está activada,
     * muestra el panel para registrar el comentario correspondiente.
     * @param e Evento de cambio de celda.
     */
    @Override
    public void editingStopped(ChangeEvent e) {
        int row = tablaComentarios.getSelectedRow();
        String isbn = (String) tablaComentarios.getValueAt(row, 0);
        String nombreLibro = (String) tablaComentarios.getValueAt(row, 1);
        Boolean valor = (Boolean) tablaComentarios.getValueAt(row, 8);
        if (valor) {
            ventanaPrincipal.activarRegistrarComentario(isbn, nombreLibro);
        }
    }

    /**
     * Se ejecuta si se cancela la edición de la celda (no realiza ninguna acción).
     * @param e Evento de cambio de celda.
     */
    @Override
    public void editingCanceled(ChangeEvent e) {}

}
