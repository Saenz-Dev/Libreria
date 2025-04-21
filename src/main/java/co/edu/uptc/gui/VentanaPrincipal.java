package co.edu.uptc.gui;

import co.edu.uptc.modelo.Libro;
import co.edu.uptc.modelo.ResumenProductoDTO;
import co.edu.uptc.modelo.Usuario;
import co.edu.uptc.modelo.ValorCompra;
import co.edu.uptc.negocio.*;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class VentanaPrincipal extends JFrame {

    private final Evento evento;
    private final MenuPrincipal menuPrincipal;
    private final GestionTienda gestionTienda;
    private final EventoCerrarFrame eventoCerrarFrame;

    public MenuPrincipal getMenuPrincipal() {
        return menuPrincipal;
    }

    public VentanaPrincipal() {
        super("Librería Virtual");
        setLayout(new BorderLayout());

        evento = new Evento(this);
        gestionTienda = new GestionTienda();
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
            gestionTienda.asignarUsuarioGenerico();
            menuPrincipal.usuarioNull();
            menuPrincipal.getPanelCatalogo().crearPanelesLibros(gestionTienda.listarLibros());
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
            gestionTienda.iniciarSesion(correo, contrasena);
            limpiarTxtLogin();
            menuPrincipal.getPanelCatalogo().crearPanelesLibros(gestionTienda.listarLibros());
            menuPrincipal.vistaPanelVenta();
            menuPrincipal.activarPanelCatalogo();
            menuPrincipal.setLabelNombreUsuario(gestionTienda.getUserLogin().getNombre());
            if (gestionTienda.isAdminLogin()) {
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
            gestionTienda.cerrarSesion();
            menuPrincipal.activarIniciarSesion();
            menuPrincipal.usuarioNull();
            menuPrincipal.activarPanelCatalogo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(menuPrincipal, e.getMessage(), "Cerrar Sesión", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarPanelPerfil() {
        menuPrincipal.getPanelPerfil().setLabelNombre("Nombre: " + gestionTienda.getUserLogin().getNombre());
        menuPrincipal.getPanelPerfil().setLabelCorreo("Correo: " + gestionTienda.getUserLogin().getCuenta().getCorreo());
        menuPrincipal.getPanelPerfil().setLabelDireccionEnvio("Dirección de envío: " + gestionTienda.getUserLogin().getDireccionEnvio());
        menuPrincipal.getPanelPerfil().setLabelTelefono("Teléfono: " + gestionTienda.getUserLogin().getTelefono());
        menuPrincipal.getPanelPerfil().setLabelTipoUsuario("Tipo de usuario: " + gestionTienda.getUserLogin().getTipoCliente());
        menuPrincipal.setLabelNombreUsuario(gestionTienda.getUserLogin().getNombre());
        menuPrincipal.activarPanelPerfil();
    }

    public void activarCarrito() {
        menuPrincipal.activarPanelCarrito();
        menuPrincipal.getPanelCarrito().anadirProductosPanel(gestionTienda.getUserLogin().getCarrito().getLibros());
        ValorCompra valorCompra = gestionTienda.resumenCompra();
        menuPrincipal.getPanelCarrito().modificarValores(valorCompra);
    }

    public void activarPanelCompras() {
        try {
            menuPrincipal.activarPanelCompras();
            menuPrincipal.getPanelCompras().llenarTabla(gestionTienda.getComprasUserLogin());
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal, e.getMessage(), "Cerrar Sesión", JOptionPane.ERROR_MESSAGE);
        }
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
        String[] titulosLibros = gestionTienda.obtenerTitulosLibros();
        menuPrincipal.getPanelModificarLibro().listarLibros(titulosLibros);
        menuPrincipal.activarPanelModificarLibro();
    }

    public void llenarCamposModificarLibros(String tituloLibro) {
        Libro libro = gestionTienda.buscarLibro(tituloLibro);
        menuPrincipal.getPanelModificarLibro().llenarCampos(libro);
    }

    public void activarCancelarModificacionLibro() {
        menuPrincipal.activarCancelarModificacionLibro();
    }

    //Estar pendiente, puede que me ocasione algun tipo de error
    public void activarModificarDatosUsuario() {
        Usuario usuario = gestionTienda.getUserLogin();
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
            gestionTienda.registrarUsuario(usuario);
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
            menuPrincipal.getPanelEliminarLibro().crearPanelesLibros(gestionTienda.listarLibros());
        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelEliminarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarFuncionEliminarLibros() {
        try {
            ArrayList<String> isbnLibros = menuPrincipal.getPanelEliminarLibro().isbnLibros();
            gestionTienda.eliminarLibro(isbnLibros);
            menuPrincipal.getPanelEliminarLibro().eliminarPanelesSeleccionados();
            menuPrincipal.getPanelEliminarLibro().repintarPanelLibros();
            menuPrincipal.getPanelEliminarLibro().crearPanelesLibros(gestionTienda.listarLibros());
        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelEliminarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarUsuario(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarFuncionRegistrarLibro() {
        try {
            Libro libro = menuPrincipal.getPanelRegistrarLibro().obtenerDatos();
            gestionTienda.registrarLibro(libro);
            JOptionPane.showMessageDialog(this, "El libro ha sido registrado", "Libro Registrado", JOptionPane.INFORMATION_MESSAGE);
            menuPrincipal.getPanelRegistrarLibro().limpiarTxtFieldsLibro();
            menuPrincipal.getPanelRegistrarLibro().setVisible(false);
        } catch (RuntimeException | IOException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarPanelCatalogo() {
        try {
            menuPrincipal.getPanelCatalogo().crearPanelesLibros(gestionTienda.listarLibros());
            menuPrincipal.activarPanelCatalogo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void anadirProductosCarrito(String isbnLibro, int cantidad, PanelLibro panelLibro) {
        try {
            gestionTienda.anadirLibrosCarrito(isbnLibro, cantidad);
            panelLibro.habilitacionBoton(gestionTienda.validarExistenciaLibro(isbnLibro));
        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void sumarProductoCarrito(String isbnProducto, PanelProducto panelProducto) {
        try {
            ResumenProductoDTO resumenProductoDTO = gestionTienda.sumarProductos(isbnProducto);
            panelProducto.actualizarPrecio(resumenProductoDTO.getSubtotal());
            panelProducto.actualizarCantidad(resumenProductoDTO.getCantidadReservada());
            ValorCompra valorCompra = gestionTienda.resumenCompra();
            menuPrincipal.getPanelCarrito().repaintPanel(valorCompra);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void disminuirProductoCarrito(String isbnProducto, PanelProducto panelProducto) {
        try {
            ResumenProductoDTO resumenProductoDTO = gestionTienda.disminuirProductoCarrito(isbnProducto);
            panelProducto.actualizarPrecio(resumenProductoDTO.getSubtotal());
            panelProducto.actualizarCantidad(resumenProductoDTO.getCantidadReservada());
            ValorCompra valorCompra = gestionTienda.resumenCompra();
            menuPrincipal.getPanelCarrito().repaintPanel(valorCompra);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    public void eliminarProductoCarrito(String isbnProducto, PanelProducto panelProducto) {
        try {
            gestionTienda.eliminarProductoCarrito(isbnProducto);
            menuPrincipal.getPanelCarrito().getListPanelesProductos().remove(panelProducto);
            menuPrincipal.getPanelCarrito().eliminarPanelProducto(panelProducto);
            ValorCompra valorCompra = gestionTienda.resumenCompra();
            menuPrincipal.getPanelCarrito().repaintPanel(valorCompra);
        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void activarCancelarEliminarLibro() {
        menuPrincipal.activarPanelGestionLibro();
    }

    public void activarPanelConfirmCompra() {
        try {
            if (menuPrincipal.getPanelCarrito().getListPanelesProductos().isEmpty()) {
                JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), "No hay productos en el carrito para comprar \nSeleccionalos en la sección catálogo.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            if (debeIniciarSesion()) return;// Salimos del método sin activar el panel si el usuario es genérico
            menuPrincipal.getPanelConfirmCompra().llenarTabla(gestionTienda.valorCompra(), gestionTienda.listaCarrito());
            menuPrincipal.activarPanelConfirmCompra();
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean debeIniciarSesion() {
        if (gestionTienda.isGenericoLogin() /* || tienda.getUserLogin().getCuenta().getCorreo().equals("user_default")*/) {
            int respuesta = JOptionPane.showConfirmDialog(this, "No ha iniciado sesión, ¿Desea Iniciar Sesión?", "Inicie Sesión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (respuesta == JOptionPane.YES_OPTION) {
                menuPrincipal.activarIniciarSesion();
            }
            return true;
        }
        return false;
    }

    public void aceptarConfirmarCompra() {
        try {

            if (menuPrincipal.getPanelConfirmCompra().seleccionEfectivo()) {
                ArrayList<String> listaIsbn = menuPrincipal.getPanelCarrito().isbnLibrosCarrito();
                gestionTienda.registrarCompra(listaIsbn, TipoPago.EFECTIVO);
            }
            if (menuPrincipal.getPanelConfirmCompra().seleccionTarjeta()) {
                ArrayList<String> listaIsbn = menuPrincipal.getPanelCarrito().isbnLibrosCarrito();
                gestionTienda.registrarCompra(listaIsbn, TipoPago.TARJETA);
            }
            JOptionPane.showMessageDialog(menuPrincipal.getPanelConfirmCompra(), "Su compra ha sido exitosa.");
            menuPrincipal.getPanelConfirmCompra().setVisible(false);
            menuPrincipal.activarPanelRecibo();
            menuPrincipal.getPanelRecibo().modificarLabels(gestionTienda.valorCompra(), gestionTienda.getComprasUserLogin().getLast());
            menuPrincipal.getPanelCarrito().repaintPanel(new ValorCompra(0,0,0));
            menuPrincipal.getPanelCarrito().vaciarCarrito();
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void cancelarConfirmarCompra() {
        menuPrincipal.getPanelConfirmCompra().setVisible(false);
    }

    public void cerrarSesionUsuario() {
        try {
            if (gestionTienda.isGenericoLogin()) {
                gestionTienda.eliminarLibroUsuarioGenerico();
            }
            gestionTienda.cerrarSesionUsuario();
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    //TODO implementar que cuando inicie la app se muestre la interfaz de Catalogo con un usuario 'user_default' y que haya un boton arriba de cerrar sesión que sea el de iniciar sesión...:) y pues también la lógica obviamente
}