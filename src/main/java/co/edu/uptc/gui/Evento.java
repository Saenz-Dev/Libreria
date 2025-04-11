package co.edu.uptc.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Clase que gestiona los eventos de la aplicación.
 * Implementa ActionListener para manejar las acciones del usuario en la interfaz gráfica.
 */
public class Evento implements ActionListener {

    public final static String ACTIVAR_INICIAR_SESION = "Activar Iniciar Sesion";

    /** Acción para gestionar libros. */
    public final static String GESTIONAR_LIBROS = "Gestionar Libros";

    /** Acción para regresar al menú anterior. */
    public final static String SALIR = "Regresar";

    /** Acción para abrir la ventana de registro de libros. */
    public final static String VENTANA_REGISTRAR_LIBRO = "Ventana Registrar Libros";

    /** Acción para registrar un libro. */
    public final static String REGISTRAR_LIBRO = "Registrar Libros";

    /** Acción para abrir la ventana de modificación de libros. */
    public final static String VENTANA_MODIFICAR_LIBRO = "Ventana Modificar Libros";

    /** Acción para abrir la ventana de registro de usuarios. */
    public final static String VENTANA_REGISTRAR_USUARIO = "Ventana Registrar Usuarios";

    /** Acción para registrar un usuario. */
    public final static String REGISTRAR_USUARIO = "Registrar Usuario";

    /** Acción para eliminar un libro. */
    public final static String ELIMINAR_LIBRO = "Eliminar Libros";

    /** Función para eliminar un libro. */
    public final static String FUNCION_ELIMINAR_LIBRO = "Funcion Eliminar Libro";

    /** Acción para cancelar el registro de un usuario. */
    public final static String CANCELAR_REGISTRO_USUARIO = "Cancelar Usuario";

    /** Acción para cancelar el registro de un libro. */
    public final static String CANCELAR_REGISTRO_LIBRO = "Cancelar Libro";

    /** Acción para cancelar la modificación de un libro. */
    public final static String CANCELAR_MODIFICACION_LIBRO = "Cancelar Modificacion Libro";

    /** Acción para cancelar la eliminación de un libro. */
    public final static String CANCELAR_ELIMINAR_LIBRO = "Cancelar Eliminar Libro";

    /** Acción para continuar con el inicio de sesión. */
    public final static String CONTINUAR_INICIAR_SESION = "Continuar";

    /** Acción para ver el catálogo de libros. */
    public final static String CATALOGO = "Catalogo";

    /** Acción para cerrar sesión. */
    public final static String CERRAR_SESION = "Cerrar Sesion";

    /** Acción para acceder al perfil del usuario. */
    public final static String PERFIL = "Perfil";

    /** Acción para acceder al carrito de compras. */
    public final static String CARRITO = "Carrito";

    /** Acción para ver las compras realizadas. */
    public final static String COMPRAS = "Compras";

    /** Acción para actualizar los datos del usuario. */
    public final static String ACTUALIZAR_DATOS_USUARIO = "Actualizar Datos Usuario";

    /** Acción para aceptar la actualización de datos del usuario. */
    public final static String ACEPTAR_ACTUALIZAR_USUARIO = "Aceptar Datos Usuario";

    /** Acción para cancelar la actualización de datos del usuario. */
    public final static String CANCELAR_ACTUALIZAR_USUARIO = "Cancelar Datos Usuario";

    /** Acción para modificar un libro. */
    public final static String MODIFICAR_LIBRO = "Modificar Libro";

    /** Acción para realizar una compra. */
    public final static String COMPRAR = "Comprar";

    /**Accion para acceder al panel de confirmar la compra */
    public final static String ACTIVAR_PANEL_CONFIRMAR = "Activar Panel Confirmar Compra";

    /**Accion para acceder al salir del panel de confirmar compra */
    public final static String CANCELAR_CONFIRMAR_COMPRA = "Cancelar Confirmar Compra";

    /**Accion para confirmar la compra */
    public final static String ACEPTAR_CONFIRMAR_COMPRA = "Aceptar Confirmar Compra";

    /** Referencia a la VentanaPrincipal. */
    private VentanaPrincipal ventana;

    /**
     * Constructor de la clase.
     * @param ventana Instancia de VentanaPrincipal.
     */
    public Evento(VentanaPrincipal ventana) {
        this.ventana = ventana;
    }

    /**
     * Maneja los eventos generados por la interfaz gráfica.
     * @param e el evento de acción que se ha disparado.
     */
    public void actionPerformed(ActionEvent e) {

        String eventoStr = e.getActionCommand();

        switch (eventoStr) {
            case ACTIVAR_INICIAR_SESION -> ventana.activarIniciarSesion();
            case GESTIONAR_LIBROS -> ventana.activarPanelGestionLibros();
            case SALIR -> ventana.activarFuncionRegresar();
            case VENTANA_REGISTRAR_LIBRO -> ventana.activarPanelRegistrarLibros();
            case VENTANA_MODIFICAR_LIBRO -> ventana.activarPanelModificarLibro();
            case VENTANA_REGISTRAR_USUARIO -> ventana.activarPanelRegistrarUsuario();
            case CANCELAR_REGISTRO_LIBRO -> ventana.activarCancelarRegistroLibro();
            case CANCELAR_REGISTRO_USUARIO -> ventana.activarCancelarRegistroUsuario();
            case CANCELAR_MODIFICACION_LIBRO -> ventana.activarCancelarModificacionLibro();
            case CONTINUAR_INICIAR_SESION -> ventana.activarPanelVenta();
            case CATALOGO -> ventana.activarPanelCatalogo();
            case CERRAR_SESION -> ventana.activarCerrarSesion();
            case PERFIL -> ventana.activarPanelPerfil();
            case CARRITO -> ventana.activarCarrito();
            case COMPRAS -> ventana.activarPanelCompras();
            case ELIMINAR_LIBRO -> ventana.activarEliminarLibros();
            case ACTUALIZAR_DATOS_USUARIO -> ventana.activarModificarDatosUsuario();
            case ACEPTAR_ACTUALIZAR_USUARIO -> ventana.activarAceptarModificarUsuario();
            case CANCELAR_ACTUALIZAR_USUARIO -> ventana.activarCancelarActualizarUser();
            case REGISTRAR_USUARIO -> ventana.activarFuncionRegistrarUsuario();
            case REGISTRAR_LIBRO -> ventana.activarFuncionRegistrarLibro();
            case MODIFICAR_LIBRO -> ventana.activarFuncionModificarLibro();
            case FUNCION_ELIMINAR_LIBRO -> ventana.activarFuncionEliminarLibros();
            case CANCELAR_ELIMINAR_LIBRO -> ventana.activarCancelarEliminarLibro();
            case ACTIVAR_PANEL_CONFIRMAR -> ventana.activarPanelConfirmCompra();
            case ACEPTAR_CONFIRMAR_COMPRA -> ventana.aceptarConfirmarCompra();
            case CANCELAR_CONFIRMAR_COMPRA -> ventana.cancelarConfirmarCompra();
            //case COMPRAR -> ventana.activarFuncionComprar();
        }
    }
}
