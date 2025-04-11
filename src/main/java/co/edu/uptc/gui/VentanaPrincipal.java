package co.edu.uptc.gui;

import co.edu.uptc.negocio.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class VentanaPrincipal extends JFrame {

    private final Evento evento;
    private final MenuPrincipal menuPrincipal;
    private final Tienda tienda;
    private final EventoCerrarFrame eventoCerrarFrame;

    public MenuPrincipal getMenuPrincipal() {
        return menuPrincipal;
    }

    public VentanaPrincipal() {
        super("Librería Virtual");
        setLayout(new BorderLayout());

        evento = new Evento(this);
        tienda = new Tienda();
        eventoCerrarFrame = new EventoCerrarFrame(this);

        menuPrincipal = new MenuPrincipal(evento, this);
        menuPrincipal.agregarVentanaCl(menuPrincipal);
        menuPrincipal.agregarInicioSesion();
        addWindowListener(eventoCerrarFrame);

        iniciaAplicacion();
        add(menuPrincipal.getPanelClPrincipal(), BorderLayout.CENTER);
        setResizable(false);
        setSize(800, 500);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public void iniciaAplicacion() {
        try {
            tienda.asignarUsuarioGenerico();
            menuPrincipal.usuarioNull();
            menuPrincipal.getPanelCatalogo().crearPanelesLibros(tienda.listarLibros());
            menuPrincipal.activarPanelCatalogo();
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
        menuPrincipal.vistaPanelVenta();
    }

    public void activarPanelVenta() {
        String correo = menuPrincipal.getPanelInicioSesion().getTxtCorreo().getText();
        String contrasena = menuPrincipal.getPanelInicioSesion().getTxtContrasena().getText();
        try {
            tienda.iniciarSesion(correo, contrasena);
            limpiarTxtLogin();
            menuPrincipal.getPanelCatalogo().crearPanelesLibros(tienda.listarLibros());
            menuPrincipal.vistaPanelVenta();
            menuPrincipal.activarPanelCatalogo();
            menuPrincipal.setLabelNombreUsuario(tienda.getUserLogin().getNombre());
            if (tienda.isAdminLogin()) {
                menuPrincipal.usuarioIniciaSesion();
                menuPrincipal.anadirFuncionesAdmin();
            } else {
                menuPrincipal.usuarioIniciaSesion();
                menuPrincipal.quitarFuncionesAdmin();
            }
        } catch (IllegalArgumentException | IOException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelClPrincipal(), e.getMessage(), "Informacion", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void activarCerrarSesion() {
        try {
            tienda.cerrarSesion();
            menuPrincipal.activarIniciarSesion();
            menuPrincipal.usuarioNull();
            menuPrincipal.activarPanelCatalogo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(menuPrincipal, e.getMessage(), "Cerrar Sesión", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarPanelPerfil() {
        menuPrincipal.getPanelPerfil().setLabelNombre("Nombre: " + tienda.getUserLogin().getNombre());
        menuPrincipal.getPanelPerfil().setLabelCorreo("Correo: " + tienda.getUserLogin().getCuenta().getCorreo());
        menuPrincipal.getPanelPerfil().setLabelDireccionEnvio("Dirección de envío: " + tienda.getUserLogin().getDireccionEnvio());
        menuPrincipal.getPanelPerfil().setLabelTelefono("Teléfono: " + tienda.getUserLogin().getTelefono());
        menuPrincipal.getPanelPerfil().setLabelTipoUsuario("Tipo de usuario: " + tienda.getUserLogin().getTipoCliente());
        menuPrincipal.setLabelNombreUsuario(tienda.getUserLogin().getNombre());
        menuPrincipal.activarPanelPerfil();
    }

    public void activarCarrito() {
        menuPrincipal.activarPanelCarrito();
        menuPrincipal.getPanelCarrito().anadirProductosPanel(tienda.getUserLogin().getCarrito().getLibros());
        ValorCompra valorCompra = tienda.resumenCompra();
        menuPrincipal.getPanelCarrito().modificarValores(valorCompra);
    }

    public void activarPanelCompras() {
        menuPrincipal.activarPanelCompras();
    }

    public void activarPanelRegistrarLibros() {
        menuPrincipal.activarPanelRegistrarLibros();
    }

    public void activarPanelRegistrarUsuario() {
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
        String[] titulosLibros = tienda.obtenerTitulosLibros();
        menuPrincipal.getPanelModificarLibro().listarLibros(titulosLibros);
        menuPrincipal.activarPanelModificarLibro();
    }

    public void llenarCamposModificarLibros(String tituloLibro) {
        Libro libro = tienda.buscarLibro(tituloLibro);
        menuPrincipal.getPanelModificarLibro().llenarCampos(libro);
    }

    public void activarCancelarModificacionLibro() {
        menuPrincipal.activarCancelarModificacionLibro();
    }


    //Estar pendiente, puede que me ocasione algun tipo de error
    public void activarModificarDatosUsuario() {
        Usuario usuario = tienda.getUserLogin();
        menuPrincipal.getPanelModificarUsuario().llenarCampos(usuario);
        menuPrincipal.activarActualizarDatosUsuario();
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
            tienda.registrarUsuario(usuario);
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarUsuario(), "Usuario Registrado Exitosamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            menuPrincipal.getPanelRegistrarUsuario().limpiarTxt();
            menuPrincipal.getPanelRegistrarUsuario().setVisible(false);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarUsuario(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarEliminarLibros() {
        try {
            menuPrincipal.activarEliminarLibros();
            menuPrincipal.getPanelEliminarLibro().crearPanelesLibros(tienda.listarLibros());
        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelEliminarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //TODO Verificar si el método funciona correctamente
    public void activarFuncionEliminarLibros() {
        try {
            ArrayList<String> isbnLibros = menuPrincipal.getPanelEliminarLibro().isbnLibros();
            tienda.eliminarLibro(isbnLibros);
            menuPrincipal.getPanelEliminarLibro().eliminarPanelesSeleccionados();
            menuPrincipal.getPanelEliminarLibro().repintarPanelLibros();
            menuPrincipal.getPanelEliminarLibro().crearPanelesLibros(tienda.listarLibros());
        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelEliminarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void activarAceptarModificarUsuario() {
        try {
            Usuario usuario = menuPrincipal.getPanelModificarUsuario().obtenerDatos();
            tienda.modificarUsuario(usuario);
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
            tienda.modificarLibro(libro);
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarLibro(), "Libro Modificado Exitosamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            String[] titulosLibros = tienda.obtenerTitulosLibros();
            menuPrincipal.getPanelModificarLibro().listarLibros(titulosLibros);
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarUsuario(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarFuncionRegistrarLibro() {
        try {
            Libro libro = menuPrincipal.getPanelRegistrarLibro().obtenerDatos();
            tienda.registrarLibro(libro);
            JOptionPane.showMessageDialog(this, "El libro ha sido registrado", "Libro Registrado", JOptionPane.INFORMATION_MESSAGE);
            menuPrincipal.getPanelRegistrarLibro().limpiarTxtFieldsLibro();
            menuPrincipal.getPanelRegistrarLibro().setVisible(false);
        } catch (RuntimeException | IOException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarPanelCatalogo() {
        try {
            menuPrincipal.getPanelCatalogo().crearPanelesLibros(tienda.listarLibros());
            menuPrincipal.activarPanelCatalogo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void anadirProductosCarrito(String isbnLibro, int cantidad, PanelLibro panelLibro) {
        try {
            tienda.anadirLibrosCarrito(isbnLibro, cantidad);
            panelLibro.habilitacionBoton(tienda.validarExistenciaLibro(isbnLibro));
        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sumarProductoCarrito(String isbnProducto, PanelProducto panelProducto) {
        try {
            ResumenProductoDTO resumenProductoDTO = tienda.sumarProductos(isbnProducto);
            panelProducto.actualizarPrecio(resumenProductoDTO.getSubtotal());
            panelProducto.actualizarCantidad(resumenProductoDTO.getCantidadReservada());
            ValorCompra valorCompra = tienda.resumenCompra();
            menuPrincipal.getPanelCarrito().repaintPanel(valorCompra);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void disminuirProductoCarrito(String isbnProducto, PanelProducto panelProducto) {
        try {
            ResumenProductoDTO resumenProductoDTO = tienda.disminuirProductoCarrito(isbnProducto);
            panelProducto.actualizarPrecio(resumenProductoDTO.getSubtotal());
            panelProducto.actualizarCantidad(resumenProductoDTO.getCantidadReservada());
            ValorCompra valorCompra = tienda.resumenCompra();
            menuPrincipal.getPanelCarrito().repaintPanel(valorCompra);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void eliminarProductoCarrito(String isbnProducto, PanelProducto panelProducto) {
        try {
            tienda.eliminarProductoCarrito(isbnProducto);
            menuPrincipal.getPanelCarrito().getListPanelesProductos().remove(panelProducto);
            menuPrincipal.getPanelCarrito().eliminarPanelProducto(panelProducto);
            ValorCompra valorCompra = tienda.resumenCompra();
            menuPrincipal.getPanelCarrito().repaintPanel(valorCompra);
        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarCancelarEliminarLibro() {
        menuPrincipal.activarPanelGestionLibro();
    }

    public void activarPanelConfirmCompra() {
        if (menuPrincipal.getPanelCarrito().getListPanelesProductos().isEmpty()) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), "No hay productos en el carrito para comprar \nSeleccionalos en la sección catálogo.", "Información", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (debeIniciarSesion()) return; // Salimos del método sin activar el panel si el usuario es genérico
        menuPrincipal.activarPanelConfirmCompra();
    }

    private boolean debeIniciarSesion() {
        if (tienda.isGenericoLogin() /* || tienda.getUserLogin().getCuenta().getCorreo().equals("user_default")*/) {
            int respuesta = JOptionPane.showConfirmDialog(this, "No ha iniciado sesión, ¿Desea Iniciar Sesión?", "Inicie Sesión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (respuesta == JOptionPane.YES_OPTION) {
                menuPrincipal.activarIniciarSesion();
            }
            return true;
        }
        return false;
    }

    public void aceptarConfirmarCompra() {
        if (menuPrincipal.getPanelConfirmCompra().seleccionEfectivo()) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelConfirmCompra(), "Se pagará con efectivo", "Información", JOptionPane.WARNING_MESSAGE);
        }
        if (menuPrincipal.getPanelConfirmCompra().seleccionTarjeta()) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelConfirmCompra(), "Se pagará con Tarjeta de Crédito", "Información", JOptionPane.WARNING_MESSAGE);
        }
        menuPrincipal.getPanelConfirmCompra().setVisible(false);
    }

    public void cancelarConfirmarCompra() {
        menuPrincipal.getPanelConfirmCompra().setVisible(false);
    }

    public void cerrarSesionUsuario() {
        try {
            if (tienda.isGenericoLogin()) {
                tienda.eliminarLibroUsuarioGenerico();
            }
            tienda.cerrarSesionUsuario();
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //TODO implementar que cuando inicie la app se muestre la interfaz de Catalogo con un usuario 'user_default' y que haya un boton arriba de cerrar sesión que sea el de iniciar sesión...:) y pues también la lógica obviamente
}