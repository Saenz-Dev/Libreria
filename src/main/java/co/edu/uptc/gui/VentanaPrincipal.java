package co.edu.uptc.gui;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.themes.FlatMacLightLaf;

import co.edu.uptc.modelo.Comentario;
import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.ResumenProductoDTO;
import co.edu.uptc.modelo.TipoPagoEnum;
import co.edu.uptc.modelo.TipoResultadoEnum;
import co.edu.uptc.modelo.Usuario;
import co.edu.uptc.modelo.TotalesCompra;
import co.edu.uptc.negocio.GestionTienda;

public class VentanaPrincipal extends JFrame {

    private final Evento evento;
    private final MenuPrincipal menuPrincipal;
    private GestionTienda gestionTienda;
    private final EventoCerrarFrame eventoCerrarFrame;

    public VentanaPrincipal() {

        super("Librería Virtual");
        setLayout(new BorderLayout());

        evento = new Evento(this);
        try {
            gestionTienda = new GestionTienda();
            UIManager.setLookAndFeel(new FlatMacLightLaf());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        eventoCerrarFrame = new EventoCerrarFrame(this);
        menuPrincipal = new MenuPrincipal(evento, this);
        menuPrincipal.agregarVentanaCl(menuPrincipal);
        menuPrincipal.agregarInicioSesion();
        addWindowListener(eventoCerrarFrame);

        iniciaAplicacion();
        add(menuPrincipal.getPanelClPrincipal(), BorderLayout.CENTER);
        setResizable(false);
        setSize(1500, 800);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void iniciaAplicacion() {
        try {
            gestionTienda.asignarUsuarioGenerico();
            menuPrincipal.usuarioNull();
            menuPrincipal.getPanelCatalogo().crearPanelesLibros(gestionTienda.listarLibros());
            menuPrincipal.activarPanelCatalogo();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelInicioSesion(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarUsuario(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarIniciarSesion() {
        menuPrincipal.activarIniciarSesion();
    }

    public static void main(String[] args) {
        VentanaPrincipal main = new VentanaPrincipal();
    }

    public void activarPanelGestionLibros() {
        menuPrincipal.activarPanelGestionLibro();
    }

    public void activarFuncionRegresar() {
        menuPrincipal.getPanelInicioSesion().setTxtCorreo("");
        menuPrincipal.getPanelInicioSesion().setTxtContrasena("");
        menuPrincipal.vistaPanelVenta();
    }

    public void activarPanelVenta() {
        String correo = menuPrincipal.getPanelInicioSesion().getTxtCorreo().getText();
        String contrasena = menuPrincipal.getPanelInicioSesion().getTxtContrasena().getText();
        try {
            gestionTienda.iniciarSesion(correo, contrasena);
            limpiarTxtLogin();

            menuPrincipal.vistaPanelVenta();
            menuPrincipal.activarPanelCatalogo();
            if (gestionTienda.isAdminLogin()) {
                menuPrincipal.usuarioIniciaSesion(gestionTienda.getUserLogin());
                menuPrincipal.anadirFuncionesAdmin();
                menuPrincipal.getPanelCatalogo().crearTablaLibros(gestionTienda.listarLibros());
            } else {
                menuPrincipal.usuarioIniciaSesion(gestionTienda.getUserLogin());
                menuPrincipal.quitarFuncionesAdmin();
                menuPrincipal.getPanelCatalogo().crearPanelesLibros(gestionTienda.listarLibros());
            }
            JOptionPane.showMessageDialog(menuPrincipal.getPanelInicioSesion(), "Inicio de sesión exitoso. Bienvenido(a) al sistema.", "Inicio Sesión", JOptionPane.INFORMATION_MESSAGE);
            menuPrincipal.setLabelNombreUsuario(gestionTienda.getUserLogin());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelInicioSesion(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelClPrincipal(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void activarCerrarSesion() {
        try {

            int respuesta = JOptionPane.showOptionDialog(menuPrincipal, "¿Estás seguro de que deseas cerrar sesión?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sí", "No"}, JOptionPane.QUESTION_MESSAGE);
            if (respuesta != JOptionPane.YES_OPTION) {
                return; // Si el usuario selecciona "No", no se cierra la sesión
            }
            gestionTienda.cerrarSesion(false);
            menuPrincipal.activarIniciarSesion();
            menuPrincipal.usuarioNull();
            menuPrincipal.activarPanelCatalogo();
            activarPanelCatalogo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(menuPrincipal, e.getMessage(), "Cerrar Sesión", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarPanelPerfil() {
        try {
            menuPrincipal.getPanelPerfil().setLabelNombre("Nombre: " + gestionTienda.getUserLogin().getNombre());
            menuPrincipal.getPanelPerfil().setLabelCorreo("Correo: " + gestionTienda.getUserLogin().getCuenta().getCorreo());
            menuPrincipal.getPanelPerfil().setLabelDireccionEnvio("Dirección de envío: " + gestionTienda.getUserLogin().getDireccionEnvio());
            menuPrincipal.getPanelPerfil().setLabelTelefono("Teléfono: " + gestionTienda.getUserLogin().getTelefono());
            menuPrincipal.getPanelPerfil().setLabelTipoUsuario("Tipo de usuario: " + gestionTienda.getUserLogin().getTipoCliente());
            menuPrincipal.setLabelNombreUsuario(gestionTienda.getUserLogin());
            menuPrincipal.activarPanelPerfil();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelPerfil(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarCarrito() {
        try {
            menuPrincipal.activarPanelCarrito();
            menuPrincipal.getPanelCarrito().anadirProductosPanel(gestionTienda.listaCarrito());
            TotalesCompra totalesCompra = gestionTienda.resumenCompra();
            menuPrincipal.getPanelCarrito().modificarValores(totalesCompra);
        } catch (SQLException | IOException e) {
            JOptionPane.showMessageDialog(menuPrincipal, e.getMessage(), "Cerrar Sesión", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarPanelCompras() {
        try {
            menuPrincipal.activarPanelCompras();
            menuPrincipal.getPanelCompras().llenarTabla(gestionTienda.getComprasUserLogin());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal, e.getMessage(), "Cerrar Sesión", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal, e.getMessage(), "Cerrar Sesión", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarPanelRegistrarLibros() {
        menuPrincipal.activarPanelRegistrarLibros();
    }

    public void activarPanelRegistrarUsuario() {
        if (gestionTienda.isAdminLogin()) {
            menuPrincipal.getPanelRegistrarUsuario().setVisibleCbTipoUsuario(true);
        } else {
            menuPrincipal.getPanelRegistrarUsuario().setVisibleCbTipoUsuario(false);
        }
        menuPrincipal.activarPanelRegistrarUsuario();
    }

    public void activarCancelarRegistroLibro() {
        menuPrincipal.getPanelRegistrarLibro().limpiarTxtFieldsLibro();
        menuPrincipal.activarCancelarRegistroLibro();
    }

    public void activarCancelarRegistroUsuario() {
        menuPrincipal.getPanelRegistrarUsuario().limpiarTxt();
        menuPrincipal.activarCancelarRegistroUsuario();
    }

    public void activarPanelModificarLibro() {
        try {
            String[] titulosLibros = gestionTienda.obtenerTitulosLibros();
            menuPrincipal.getPanelModificarLibro().listarLibros(titulosLibros);
            menuPrincipal.activarPanelModificarLibro();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void llenarCamposModificarLibros(String tituloLibro) {
        try {
            Libro libro = gestionTienda.buscarLibro(tituloLibro);
            menuPrincipal.getPanelModificarLibro().llenarCampos(libro);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelEliminarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarCancelarModificacionLibro() {
        menuPrincipal.activarCancelarModificacionLibro();
    }

    public void activarModificarDatosUsuario() {
        try {
            Usuario usuario = gestionTienda.getUserLogin();
            menuPrincipal.getPanelModificarUsuario().llenarCampos(usuario);
            menuPrincipal.activarActualizarDatosUsuario();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarCancelarActualizarUser() {
        menuPrincipal.activarCancelarActualizarUser();
    }

    public void limpiarTxtLogin() {
        menuPrincipal.getPanelInicioSesion().getTxtCorreo().setText("");
        menuPrincipal.getPanelInicioSesion().getTxtContrasena().setText("");
    }

    public void activarFuncionRegistrarUsuario() {
        try {
            Usuario usuario = menuPrincipal.getPanelRegistrarUsuario().obtenerDatos();
            gestionTienda.registrarUsuario(usuario);
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarUsuario(), "Usuario Registrado Exitosamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            menuPrincipal.getPanelRegistrarUsuario().limpiarTxt();
            menuPrincipal.getPanelRegistrarUsuario().setVisible(false);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarUsuario(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarUsuario(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void activarEliminarLibros() {
        try {
            menuPrincipal.activarEliminarLibros();
            menuPrincipal.getPanelEliminarLibro().crearPanelesLibros(gestionTienda.listarLibros());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelEliminarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelEliminarLibro(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void activarFuncionEliminarLibros() {
        try {
            ArrayList<String> isbnLibros = menuPrincipal.getPanelEliminarLibro().isbnLibros();
            int option = JOptionPane.showOptionDialog(menuPrincipal.getPanelEliminarLibro(), "¿Estás seguro de que deseas eliminar este libro?\nEsta acción no se puede deshacer.", "Eliminar Libros", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Eliminar", "Cancelar"}, JOptionPane.QUESTION_MESSAGE);
            if (option == 1) {
                return;
            }
            gestionTienda.eliminarLibro(isbnLibros);
            menuPrincipal.getPanelEliminarLibro().eliminarPanelesSeleccionados();
            JOptionPane.showMessageDialog(menuPrincipal.getPanelEliminarLibro(), "Libro(s) Eliminado(s)");
            menuPrincipal.getPanelEliminarLibro().repintarPanelLibros();
            menuPrincipal.getPanelEliminarLibro().crearPanelesLibros(gestionTienda.listarLibros());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelEliminarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelEliminarLibro(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void activarAceptarModificarUsuario() {
        try {
            Usuario usuario = menuPrincipal.getPanelModificarUsuario().obtenerDatos();
            gestionTienda.modificarUsuario(usuario);
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarUsuario(), "Usuario Modificado Exitosamente", "Información", JOptionPane.INFORMATION_MESSAGE);
            menuPrincipal.getPanelModificarUsuario().limpiarTxt();
            activarPanelPerfil();
            menuPrincipal.getPanelModificarUsuario().setVisible(false);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarUsuario(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarFuncionModificarLibro() {
        try {
            Libro libro = menuPrincipal.getPanelModificarLibro().obtenerDatos();
            gestionTienda.modificarLibro(libro);
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarLibro(), "Libro Modificado Exitosamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            String[] titulosLibros = gestionTienda.obtenerTitulosLibros();
            menuPrincipal.getPanelModificarLibro().listarLibros(titulosLibros);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarUsuario(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void activarFuncionRegistrarLibro() {
        try {
            Libro libro = menuPrincipal.getPanelRegistrarLibro().obtenerDatos();
            gestionTienda.registrarLibro(libro);
            JOptionPane.showMessageDialog(this, "El libro ha sido registrado", "Libro Registrado", JOptionPane.INFORMATION_MESSAGE);
            menuPrincipal.getPanelRegistrarLibro().limpiarTxtFieldsLibro();
            menuPrincipal.getPanelRegistrarLibro().setVisible(false);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException | IOException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void activarPanelCatalogo() {
        try {
            if (gestionTienda.isAdminLogin()) {
                menuPrincipal.getPanelCatalogo().crearTablaLibros(gestionTienda.listarLibros());
            } else {
                menuPrincipal.getPanelCatalogo().crearPanelesLibros(gestionTienda.listarLibros());
            }
            menuPrincipal.activarPanelCatalogo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void anadirProductosCarrito(String isbnLibro, int cantidad, PanelLibro panelLibro) {
        try {
            gestionTienda.anadirLibrosCarrito(isbnLibro, cantidad);
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCatalogo(), "Libro añadido al carrito exitosamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            panelLibro.habilitacionBoton(gestionTienda.validarExistenciaLibro(isbnLibro));
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void sumarProductoCarrito(String isbnProducto, PanelProducto panelProducto) {
        try {
            ResumenProductoDTO resumenProductoDTO = gestionTienda.sumarProductos(isbnProducto);
            panelProducto.actualizarPrecio(resumenProductoDTO.getSubtotal());
            panelProducto.actualizarCantidad(resumenProductoDTO.getCantidadReservada());
            TotalesCompra totalesCompra = gestionTienda.resumenCompra();
            menuPrincipal.getPanelCarrito().repaintPanel(totalesCompra);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void disminuirProductoCarrito(String isbnProducto, PanelProducto panelProducto) {
        try {
            ResumenProductoDTO resumenProductoDTO = gestionTienda.disminuirProductoCarrito(isbnProducto);
            panelProducto.actualizarPrecio(resumenProductoDTO.getSubtotal());
            panelProducto.actualizarCantidad(resumenProductoDTO.getCantidadReservada());
            TotalesCompra totalesCompra = gestionTienda.resumenCompra();
            menuPrincipal.getPanelCarrito().repaintPanel(totalesCompra);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void eliminarProductoCarrito(String isbnProducto, PanelProducto panelProducto) {
        try {
            gestionTienda.eliminarProductoCarrito(isbnProducto);
            menuPrincipal.getPanelCarrito().getListPanelesProductos().remove(panelProducto);
            menuPrincipal.getPanelCarrito().eliminarPanelProducto(panelProducto);
            TotalesCompra totalesCompra = gestionTienda.resumenCompra();
            menuPrincipal.getPanelCarrito().repaintPanel(totalesCompra);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void eliminarProductoTabla(String isbnProducto) {
        try {
            gestionTienda.eliminarProductoCarrito(isbnProducto);
            menuPrincipal.getPanelConfirmCompra().llenarTabla(gestionTienda.valorCompra(), gestionTienda.listaCarrito());
        } catch (IOException | SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarCancelarEliminarLibro() {
        menuPrincipal.activarPanelGestionLibro();
        // activarCarrito();
    }

    public void activarPanelConfirmCompra() {
        try {
            if (menuPrincipal.getPanelCarrito().getListPanelesProductos().isEmpty()) {
                JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), "No hay productos en el carrito para comprar \nSeleccionalos en la sección catálogo.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (debeIniciarSesion()) return;// Salimos del metodo sin activar el panel si el usuario es genérico
            menuPrincipal.getPanelConfirmCompra().llenarTabla(gestionTienda.valorCompra(), gestionTienda.listaCarrito());
            menuPrincipal.activarPanelConfirmCompra();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException | IOException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private boolean debeIniciarSesion() {
        if (gestionTienda.isGenericoLogin()) {
            JOptionPane.showMessageDialog(this, "Inicie sesión para poder realizar la compra.", "Inicie Sesión", JOptionPane.WARNING_MESSAGE);
            menuPrincipal.activarIniciarSesion();
            return true;
        }
        return false;
    }

    public void aceptarConfirmarCompra() {
        try {
            ArrayList<String> listaIsbn = menuPrincipal.getPanelCarrito().isbnLibrosCarrito();
            if (menuPrincipal.getPanelConfirmCompra().seleccionEfectivo()) {
                gestionTienda.registrarCompra(listaIsbn, TipoPagoEnum.EFECTIVO);
            }
            if (menuPrincipal.getPanelConfirmCompra().seleccionTarjeta()) {
                gestionTienda.registrarCompra(listaIsbn, TipoPagoEnum.TARJETA);
            }
            JOptionPane.showMessageDialog(menuPrincipal.getPanelConfirmCompra(), "Su compra ha sido exitosa.");
            menuPrincipal.getPanelConfirmCompra().setVisible(false);
            menuPrincipal.getPanelRecibo().modificarLabels(gestionTienda.reciboUsuario(), false);
            menuPrincipal.activarPanelRecibo();

            menuPrincipal.getPanelCarrito().repaintPanel(new TotalesCompra(0, 0, 0, 0, 0));
            menuPrincipal.getPanelCarrito().vaciarCarrito();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void activarPanelVerCompra(String fecha, int numeroRecibo) {
        try {
            menuPrincipal.getPanelRecibo().modificarLabels(gestionTienda.comprasUsuarioLog(fecha, numeroRecibo), true);
            menuPrincipal.activarPanelRecibo();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void cancelarConfirmarCompra() {
        menuPrincipal.getPanelConfirmCompra().setVisible(false);
        activarCarrito();
    }

    public void cerrarSesionUsuario() {
        try {
            gestionTienda.cerrarSesion(true);
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarRegistrarComentario(String isbn, String nombreLibro) {
        menuPrincipal.getPanelCalificar().setLabelLibro(isbn, nombreLibro);
        menuPrincipal.activarPanelCalificar();
    }

    public void registrarComentario() {
        try {
            Comentario comentario = menuPrincipal.getPanelCalificar().getComentario();
            gestionTienda.guardarComentario(comentario);
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCalificar(), "Comentario registrado exitosamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            menuPrincipal.desactivarPanelCalificar();
            menuPrincipal.getPanelCalificar().limpiarComentario();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCalificar(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCalificar(), e.getMessage(), "Mensaje", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCalificar(), e.getMessage(), "Mensaje", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void activarMostrarComentario(String isbn) {
        try {

            menuPrincipal.getPanelComentario().agregarComentarios(gestionTienda.listarComentarios(isbn));
            menuPrincipal.activarPanelComentario();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelComentario(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelComentario(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelComentario(), e.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void activarValidarPremium() {
        String codigo = menuPrincipal.getPanelPremium().obtenerCodigo();
        try {
            gestionTienda.usarCodigo(codigo);
            menuPrincipal.getPanelPremium().mostrarResultado("Ahora eres Premium", TipoResultadoEnum.EXITO);
            JOptionPane.showMessageDialog(menuPrincipal.getPanelPremium(), "¡¡¡Felicidades, ahora eres un usuario Premium!!!\nRecibiras descuentos especiales.", "Felicidades", JOptionPane.INFORMATION_MESSAGE);
            menuPrincipal.getPanelPremium().setVisible(false);
            menuPrincipal.getBotonPremium().setVisible(false);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            menuPrincipal.getPanelPremium().mostrarResultado(e.getMessage(), TipoResultadoEnum.ERROR);
	    /*JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Mensaje",
		    JOptionPane.WARNING_MESSAGE);*/
        }
    }

    public void activarPanelValidPremium() {
        menuPrincipal.activarPanelPremium();
    }

    public void GuardarCodigo() {
        try {
            gestionTienda.registrarCodigo(menuPrincipal.getPanelAggCodigo().obtenerCodigo());
            menuPrincipal.getPanelAggCodigo().mostrarMensajeExito("Codigo registrado.");
            menuPrincipal.getPanelAggCodigo().construirTabla(gestionTienda.consultaCodigos());

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            menuPrincipal.getPanelAggCodigo().mostrarMensaje(e.getMessage());
        }
    }

    public void activarGuardarCodigo() {
        try {
            menuPrincipal.getPanelAggCodigo().construirTabla(gestionTienda.consultaCodigos());
            menuPrincipal.activarPanelGuardarCodigos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void cerrarCalificar() {
        menuPrincipal.desactivarPanelCalificar();
        menuPrincipal.getPanelCalificar().limpiarComentario();
    }
}