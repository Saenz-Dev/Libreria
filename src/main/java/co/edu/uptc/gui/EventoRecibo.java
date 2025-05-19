package co.edu.uptc.gui;

import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

public class EventoRecibo implements CellEditorListener{

    private JTable tablaComentarios;
    private VentanaPrincipal ventanaPrincipal;
    
    public EventoRecibo(JTable tablaComentarios, VentanaPrincipal ventanaPrincipal) {
	 this.ventanaPrincipal = ventanaPrincipal;
	 this.tablaComentarios = tablaComentarios;
    }
    
    @Override
    public void editingStopped(ChangeEvent e) {
	int row = tablaComentarios.getSelectedColumn();
	String isbn = (String) tablaComentarios.getValueAt(row, 1);
        String nombreLibro = (String) tablaComentarios.getValueAt(row, 2);
        Boolean valor = (Boolean) tablaComentarios.getValueAt(row, 2);
        if (valor) {
            ventanaPrincipal.activarRegistrarComentario(isbn, nombreLibro);
        }
    }

    @Override
    public void editingCanceled(ChangeEvent e) {
	// TODO Auto-generated method stub
	
    }

}
