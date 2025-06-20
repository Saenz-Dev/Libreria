package co.edu.uptc.gui;

import java.awt.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import co.edu.uptc.excepcion.CategoriaException;
import co.edu.uptc.modelo.*;
import com.formdev.flatlaf.themes.FlatMacLightLaf;

import co.edu.uptc.negocio.GestionTienda;

/**
 * Clase principal de la aplicación de la Librería Virtual.
 * Gestiona la ventana principal, la inicialización de la aplicación y la navegación entre paneles.
 * @author Miguel Angel Saenz Tibambre
 *
 */
public class VentanaPrincipal extends JFrame {
    /**
     * Instancia de la clase Evento para manejar eventos generales de la aplicación.
     */
    private final Evento evento;
    /**
     * Instancia del menú principal de la aplicación.
     */
    private final MenuPrincipal menuPrincipal;
    /**
     * Instancia de la clase GestionTienda para la lógica de negocio principal.
     */
    private GestionTienda gestionTienda;
    /**
     * Evento para manejar el cierre de la ventana principal.
     */
    private final EventoCerrarFrame eventoCerrarFrame;

    /**
     * Constructor de la ventana principal. Inicializa la interfaz y los componentes principales.
     */
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

    /**
     * Inicializa la aplicación, asigna usuario genérico y carga el catálogo y filtros.
     */
    public void iniciaAplicacion() {
        try {
            gestionTienda.asignarUsuarioGenerico();
            menuPrincipal.usuarioNull();
            menuPrincipal.getPanelCatalogo().llenarFiltros(gestionTienda.listarCategorias());
            menuPrincipal.getPanelCatalogo().crearPanelesLibros(gestionTienda.listarLibros());
            menuPrincipal.activarPanelCatalogo();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelInicioSesion(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarUsuario(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Activa el panel de inicio de sesión en el menú principal.
     */
    public void activarIniciarSesion() {
        menuPrincipal.activarIniciarSesion();
    }

    /**
     * Activa el panel de gestión de libros en el menú principal.
     */
    public void activarPanelGestionLibros() {
        menuPrincipal.activarPanelGestionLibro();
    }

    /**
     * Activa la función de regresar en el panel de inicio de sesión.
     */
    public void activarFuncionRegresar() {
        menuPrincipal.getPanelInicioSesion().setTxtCorreo("");
        menuPrincipal.getPanelInicioSesion().setTxtContrasena("");
        menuPrincipal.vistaPanelVenta();
    }

    /**
     * Activa el panel de venta y carga la información del usuario si ha iniciado sesión.
     */
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
            menuPrincipal.setLabelNombreUsuario(gestionTienda.getUserLogin());
            JOptionPane.showMessageDialog(menuPrincipal.getPanelInicioSesion(), "Inicio de sesión exitoso. Bienvenido(a) al sistema.", "Inicio Sesión", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelInicioSesion(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelClPrincipal(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Activa la función de cerrar sesión, mostrando un mensaje de confirmación.
     */
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

    /**
     * Activa el panel de perfil y carga la información del usuario.
     */
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

    /**
     * Activa el panel del carrito de compras y muestra los productos añadidos.
     */
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

    /**
     * Activa el panel de compras y muestra el historial de compras del usuario.
     */
    public void activarPanelCompras() {
        try {
            menuPrincipal.activarPanelCompras();
            menuPrincipal.getPanelCompras().llenarTabla(gestionTienda.getComprasUserLogin());
        } catch (SQLException | IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal, e.getMessage(), "Cerrar Sesión", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Activa el panel de registro de libros y carga las categorías disponibles.
     */
    public void activarPanelRegistrarLibros() {
        try {
            menuPrincipal.getPanelRegistrarLibro().llenarComboBoxCategoria(gestionTienda.listarCategorias());
            menuPrincipal.activarPanelRegistrarLibros();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Activa el panel de registro de usuarios, mostrando u ocultando el tipo de usuario según el rol.
     */
    public void activarPanelRegistrarUsuario() {
        if (gestionTienda.isAdminLogin()) {
            menuPrincipal.getPanelRegistrarUsuario().setVisibleCbTipoUsuario(true);
        } else {
            menuPrincipal.getPanelRegistrarUsuario().setVisibleCbTipoUsuario(false);
        }
        menuPrincipal.activarPanelRegistrarUsuario();
    }

    /**
     * Cancela el registro de un libro y limpia los campos del formulario.
     */
    public void activarCancelarRegistroLibro() {
        menuPrincipal.getPanelRegistrarLibro().limpiarTxtFieldsLibro();
        menuPrincipal.activarCancelarRegistroLibro();
    }

    /**
     * Cancela el registro de un usuario y limpia los campos del formulario.
     */
    public void activarCancelarRegistroUsuario() {
        menuPrincipal.getPanelRegistrarUsuario().limpiarTxt();
        menuPrincipal.activarCancelarRegistroUsuario();
    }

    /**
     * Activa el panel de modificación de libros y carga los libros y categorías existentes.
     */
    public void activarPanelModificarLibro() {
        try {
            String[] titulosLibros = gestionTienda.obtenerTitulosLibros();
            menuPrincipal.getPanelModificarLibro().llenarCbCategoria(gestionTienda.listarCategorias());
            menuPrincipal.getPanelModificarLibro().listarLibros(titulosLibros);
            menuPrincipal.activarPanelModificarLibro();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarLibro(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Llena los campos del formulario de modificación de libros con los datos del libro seleccionado.
     *
     * @param tituloLibro Título del libro a buscar y editar.
     */
    public void llenarCamposModificarLibros(String tituloLibro) {
        try {
            Libro libro = gestionTienda.buscarLibro(tituloLibro);
            menuPrincipal.getPanelModificarLibro().llenarCampos(libro);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelEliminarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cancela la modificación de un libro y regresa al panel anterior.
     */
    public void activarCancelarModificacionLibro() {
        menuPrincipal.activarCancelarModificacionLibro();
    }

    /**
     * Activa el panel de modificación de datos de usuario, cargando la información según el rol.
     */
    public void activarModificarDatosUsuario() {
        try {
            if (!gestionTienda.isAdminLogin()) {
                Usuario usuario = gestionTienda.getUserLogin();
                menuPrincipal.getPanelModificarUsuario().llenarCampos(usuario, false);
                menuPrincipal.activarActualizarDatosUsuario();
                menuPrincipal.getPanelModificarUsuario().setLabelTitulo("Gestionar Usuarios");
                return;
            }
            menuPrincipal.getPanelModificarUsuario().llenarComboBoxUsuarios(gestionTienda.listarUsuarios());
            menuPrincipal.getPanelModificarUsuario().llenarCampos(gestionTienda.getTienda().getUsuarios().getFirst(), true);
            menuPrincipal.activarActualizarDatosUsuario();
            menuPrincipal.getPanelModificarUsuario().setLabelTitulo("Modificar Datos");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cancela la actualización de datos de usuario y regresa al panel anterior.
     */
    public void activarCancelarActualizarUser() {
        menuPrincipal.activarCancelarActualizarUser();
    }

    /**
     * Limpia los campos de texto del formulario de inicio de sesión.
     */
    public void limpiarTxtLogin() {
        menuPrincipal.getPanelInicioSesion().getTxtCorreo().setText("");
        menuPrincipal.getPanelInicioSesion().getTxtContrasena().setText("");
    }

    /**
     * Activa la función de registro de usuario, obteniendo los datos del formulario y llamando a la lógica de negocio.
     */
    public void activarFuncionRegistrarUsuario() {
        try {
            Usuario usuario = menuPrincipal.getPanelRegistrarUsuario().obtenerDatos();
            gestionTienda.registrarUsuario(usuario);
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarUsuario(), "Usuario Registrado Exitosamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            menuPrincipal.getPanelRegistrarUsuario().limpiarTxt();
            menuPrincipal.getPanelRegistrarUsuario().setVisible(false);
            menuPrincipal.getPanelModificarUsuario().llenarComboBoxUsuarios(gestionTienda.listarUsuarios());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarUsuario(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarUsuario(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Activa el panel de eliminación de libros.
     */
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

    /**
     * Confirma y ejecuta la eliminación de los libros seleccionados en el panel de eliminación de libros.
     */
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

    /**
     * Confirma y aplica la modificación de los datos de usuario.
     */
    public void activarAceptarModificarUsuario() {
        try {
            Usuario usuario = menuPrincipal.getPanelModificarUsuario().obtenerDatos();
            gestionTienda.modificarUsuario(usuario);
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarUsuario(), "Usuario Modificado Exitosamente", "Información", JOptionPane.INFORMATION_MESSAGE);
            if (gestionTienda.isAdminLogin()) {
                menuPrincipal.getPanelModificarUsuario().setLabelTitulo("Gestionar Usuarios");
                menuPrincipal.getPanelModificarUsuario().llenarComboBoxUsuarios(gestionTienda.listarUsuarios());
                menuPrincipal.getPanelModificarUsuario().llenarCampos(gestionTienda.getTienda().getUsuarios().getFirst(), true);
                menuPrincipal.getPanelModificarUsuario().setVisible(true);
            } else {
                menuPrincipal.getPanelModificarUsuario().setLabelTitulo("Modificar Datos");
                menuPrincipal.getPanelModificarUsuario().getCbUsuario().setVisible(false);
                menuPrincipal.getPanelModificarUsuario().llenarCampos(gestionTienda.getUserLogin(), false);
                activarPanelPerfil();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarUsuario(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Confirma y aplica la modificación de los datos de un libro.
     */
    public void activarFuncionModificarLibro() {
        try {
            Libro libro = menuPrincipal.getPanelModificarLibro().obtenerDatos();
            gestionTienda.modificarLibro(libro);
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarLibro(), "Libro Modificado Exitosamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            String[] titulosLibros = gestionTienda.obtenerTitulosLibros();
            menuPrincipal.getPanelModificarLibro().listarLibros(titulosLibros);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarLibro(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Registra un nuevo libro en el sistema con los datos proporcionados en el formulario.
     */
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

    /**
     * Activa el panel de catálogo y carga los libros y categorías según el rol del usuario.
     */
    public void activarPanelCatalogo() {
        try {
            if (gestionTienda.isAdminLogin()) {
                menuPrincipal.getPanelCatalogo().llenarFiltros(gestionTienda.listarCategorias());
                menuPrincipal.getPanelCatalogo().crearTablaLibros(gestionTienda.listarLibros());
            } else {
                menuPrincipal.getPanelCatalogo().llenarFiltros(gestionTienda.listarCategorias());
                menuPrincipal.getPanelCatalogo().crearPanelesLibros(gestionTienda.listarLibros());
            }
            menuPrincipal.activarPanelCatalogo();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRegistrarLibro(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Añade un producto al carrito de compras y actualiza la interfaz del panel del libro.
     *
     * @param isbnLibro  ISBN del libro a añadir al carrito.
     * @param panelLibro Panel del libro desde donde se añade el libro al carrito.
     */
    public void anadirProductosCarrito(String isbnLibro, PanelLibro panelLibro) {
        try {
            Libro libro = gestionTienda.anadirLibrosCarrito(isbnLibro);
            panelLibro.setLabelTipoLibroCantidad(libro);
            panelLibro.habilitacionBoton(gestionTienda.validarExistenciaLibro(isbnLibro));
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCatalogo(), "Libro añadido al carrito exitosamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCatalogo(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException | IllegalArgumentException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCatalogo(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Suma un producto al carrito, actualizando la cantidad y el precio en el panel correspondiente.
     *
     * @param isbnProducto  ISBN del producto a sumar.
     * @param panelProducto Panel del producto donde se actualizará la información.
     */
    public void sumarProductoCarrito(String isbnProducto, PanelProducto panelProducto) {
        try {
            LibroComprado libroComprado = gestionTienda.sumarProductos(isbnProducto);
            panelProducto.actualizarPrecio(libroComprado.getPrecioVenta());
            panelProducto.actualizarCantidad(libroComprado.getCantidadComprada());
            TotalesCompra totalesCompra = gestionTienda.resumenCompra();
            menuPrincipal.getPanelCarrito().repaintPanel(totalesCompra);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Disminuye la cantidad de un producto en el carrito, actualizando el precio y la cantidad en el panel correspondiente.
     *
     * @param isbnProducto  ISBN del producto a disminuir.
     * @param panelProducto Panel del producto donde se actualizará la información.
     */
    public void disminuirProductoCarrito(String isbnProducto, PanelProducto panelProducto) {
        try {
            LibroComprado libroComprado = gestionTienda.disminuirProductoCarrito(isbnProducto);
            panelProducto.actualizarPrecio(libroComprado.getPrecioVenta());
            panelProducto.actualizarCantidad(libroComprado.getCantidadComprada());
            TotalesCompra totalesCompra = gestionTienda.resumenCompra();
            menuPrincipal.getPanelCarrito().repaintPanel(totalesCompra);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCarrito(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Elimina un producto del carrito y actualiza el panel del carrito.
     *
     * @param isbnProducto  ISBN del producto a eliminar.
     * @param panelProducto Panel del producto que se eliminará del carrito.
     */
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

    /**
     * Elimina un producto de la tabla de confirmación de compra y actualiza la información del carrito.
     *
     * @param isbnProducto ISBN del producto a eliminar de la tabla.
     */
    public void eliminarProductoTabla(String isbnProducto) {
        try {
            gestionTienda.eliminarProductoCarrito(isbnProducto);
            menuPrincipal.getPanelConfirmCompra().llenarTabla(gestionTienda.valorCompra(), gestionTienda.listaCarrito());
        } catch (IOException | SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelConfirmCompra(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Cancela la eliminación de un libro y regresa al panel de gestión de libros.
     */
    public void activarCancelarEliminarLibro() {
        menuPrincipal.activarPanelGestionLibro();
    }

    /**
     * Activa el panel de confirmación de compra y muestra los detalles de la compra.
     */
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

    /**
     * Acepta y procesa la confirmación de compra, registrando la compra en el sistema.
     */
    public void aceptarConfirmarCompra() {
        try {
            ArrayList<String> listaIsbn = menuPrincipal.getPanelCarrito().isbnLibrosCarrito();
            if (menuPrincipal.getPanelConfirmCompra().seleccionEfectivo()) {
                gestionTienda.registrarCompra(TipoPagoEnum.EFECTIVO);
            }
            if (menuPrincipal.getPanelConfirmCompra().seleccionTarjeta()) {
                gestionTienda.registrarCompra(TipoPagoEnum.TARJETA);
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

    /**
     * Activa el panel de recibo y muestra los detalles de una compra específica.
     *
     * @param fecha        Fecha de la compra a mostrar en el recibo.
     * @param numeroRecibo Número de recibo de la compra a mostrar.
     */
    public void activarPanelVerCompra(String fecha, int numeroRecibo) {
        try {
            menuPrincipal.getPanelRecibo().modificarLabels(gestionTienda.comprasUsuarioLog(fecha, numeroRecibo), true);
            menuPrincipal.activarPanelRecibo();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRecibo(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelRecibo(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Cancela la confirmación de compra y regresa al panel del carrito.
     */
    public void cancelarConfirmarCompra() {
        menuPrincipal.getPanelConfirmCompra().setVisible(false);
        activarCarrito();
    }

    /**
     * Cierra la sesión del usuario actual, ya sea genérico o registrado.
     */
    public void cerrarSesionUsuario() {
        try {
            gestionTienda.cerrarSesion(true);
        } catch (IOException | RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelClPrincipal(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelClPrincipal(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Activa el panel para registrar un comentario sobre un libro.
     *
     * @param isbn        ISBN del libro al que se le quiere registrar un comentario.
     * @param nombreLibro Nombre del libro al que se le quiere registrar un comentario.
     */
    public void activarRegistrarComentario(String isbn, String nombreLibro) {
        menuPrincipal.getPanelCalificar().setLabelLibro(isbn, nombreLibro);
        menuPrincipal.activarPanelCalificar();
    }

    /**
     * Registra un comentario en el sistema, obteniendo los datos del panel de calificación.
     */
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

    /**
     * Activa el panel que muestra los comentarios registrados sobre un libro.
     *
     * @param isbn ISBN del libro cuyos comentarios se quieren mostrar.
     */
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

    /**
     * Activa la validación de un código premium ingresado por el usuario.
     */
    public void activarValidarPremium() {
        String codigo = menuPrincipal.getPanelPremium().obtenerCodigo();
        try {
            gestionTienda.usarCodigo(codigo);
            menuPrincipal.getPanelPremium().mostrarResultado("Ahora eres Premium", TipoResultadoEnum.EXITO);
            JOptionPane.showMessageDialog(menuPrincipal.getPanelPremium(), "¡¡¡Felicidades, ahora eres un usuario Premium!!!\nRecibiras descuentos especiales.", "Felicidades", JOptionPane.INFORMATION_MESSAGE);
            menuPrincipal.getPanelPremium().setVisible(false);
            menuPrincipal.getBotonPremium().setVisible(false);
            menuPrincipal.setLabelNombreUsuario(gestionTienda.getUserLogin());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelPremium(), e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            menuPrincipal.getPanelPremium().mostrarResultado(e.getMessage(), TipoResultadoEnum.ERROR);
        }
    }

    /**
     * Activa el panel de premium para mostrar información y opciones relacionadas.
     */
    public void activarPanelValidPremium() {
        menuPrincipal.activarPanelPremium();
    }

    /**
     * Guarda un código promocional en el sistema, registrado por el administrador.
     */
    public void guardarCodigo() {
        try {
            gestionTienda.registrarCodigo(menuPrincipal.getPanelAggCodigo().obtenerCodigo());
            menuPrincipal.getPanelAggCodigo().mostrarMensajeExito("Codigo registrado.");
            menuPrincipal.getPanelAggCodigo().construirTabla(gestionTienda.consultaCodigos());
            menuPrincipal.getPanelAggCodigo().limpiarCampoCodigo();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelAggCodigo(), e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            menuPrincipal.getPanelAggCodigo().mostrarMensaje(e.getMessage());
        }
    }

    /**
     * Activa el panel para guardar códigos promocionales, mostrando la lista de códigos existentes.
     */
    public void activarGuardarCodigo() {
        try {
            menuPrincipal.getPanelAggCodigo().construirTabla(gestionTienda.consultaCodigos());
            menuPrincipal.activarPanelGuardarCodigos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelAggCodigo(), e.getMessage(), "Mensaje", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelAggCodigo(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Cierra el panel de calificación y limpia el comentario ingresado.
     */
    public void cerrarCalificar() {
        menuPrincipal.desactivarPanelCalificar();
        menuPrincipal.getPanelCalificar().limpiarComentario();
    }

    /**
     * Filtra los libros en el catálogo según la categoría y formato seleccionados.
     */
    public void activarFiltrarLibros() {
        try {
            String categoria = menuPrincipal.getPanelCatalogo().getCategoriaSeleccionada();
            String formato = menuPrincipal.getPanelCatalogo().getFormatoSeleccionado();
            ArrayList<Libro> librosFiltrados = gestionTienda.filtrarLibros(categoria, formato);
            if (librosFiltrados.isEmpty()) {
                menuPrincipal.getPanelCatalogo().activarMensajes("No se encontraron libros con los criterios seleccionados.");
                JOptionPane.showMessageDialog(menuPrincipal.getPanelCatalogo(), "No se encontraron libros con los criterios seleccionados.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } else {
                if (gestionTienda.isAdminLogin()) {
                    menuPrincipal.getPanelCatalogo().crearTablaLibros(librosFiltrados);
                } else {
                    menuPrincipal.getPanelCatalogo().crearPanelesLibros(librosFiltrados);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCatalogo(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelCatalogo(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Elimina un usuario del sistema, dado su correo electrónico.
     */
    public void eliminarUsuario() {
        try {
            String correo = menuPrincipal.getPanelModificarUsuario().getTxtCorreo();
            int opcion = JOptionPane.showOptionDialog(menuPrincipal.getDialogAgregarCategoria(), "\n¿Estas seguro de eliminar este usuario?", "Advertencia", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sí", "No"}, JOptionPane.QUESTION_MESSAGE);
            if (opcion == 1) {
                return;
            }
            gestionTienda.eliminarUsuario(correo);
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarUsuario(), "Usuario eliminado exitosamente.", "Información", JOptionPane.INFORMATION_MESSAGE);
            activarModificarDatosUsuario();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarUsuario(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarUsuario(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Cambia la información mostrada en el panel de modificación de usuario, según el usuario seleccionado.
     */
    public void cambiarInfoUsuario() {
        try {
            String usuario = (String) menuPrincipal.getPanelModificarUsuario().getCbUsuario().getSelectedItem();
            menuPrincipal.getPanelModificarUsuario().llenarCampos(gestionTienda.buscarUsuario(usuario), true);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getPanelModificarUsuario(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Activa el diálogo para agregar una nueva categoría desde el panel de registro de libros.
     */
    public void activarAgregarCategoriaAg() {
        menuPrincipal.activarDialogoAgregarCategoria(menuPrincipal.getPanelRegistrarLibro());
    }

    /**
     * Activa el diálogo para agregar una nueva categoría desde el panel de modificación de libros.
     */
    public void activarAgregarCategoria() {
        menuPrincipal.activarDialogoAgregarCategoria(menuPrincipal.getPanelModificarLibro());
    }

    /**
     * Agrega una nueva categoría al sistema, evitando duplicados y categorías parecidas.
     */
    public void agregarCategoria() {
        String categoria = menuPrincipal.getDialogAgregarCategoria().getCampoCategoria();
        try {
            gestionTienda.agregarCategoria(categoria);
            JOptionPane.showMessageDialog(menuPrincipal.getDialogAgregarCategoria(), "Categoria registrada", "Exito", JOptionPane.INFORMATION_MESSAGE);
            menuPrincipal.getPanelModificarLibro().llenarCbCategoria(gestionTienda.listarCategorias());
            menuPrincipal.getPanelRegistrarLibro().llenarComboBoxCategoria(gestionTienda.listarCategorias());
        } catch (CategoriaException e) {
            confirmar(e, categoria);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getDialogAgregarCategoria(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getDialogAgregarCategoria(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void confirmar(CategoriaException e, String categoria) {
        switch (e.getTipoConflicto()) {
            case DUPLICADO:
                JOptionPane.showMessageDialog(menuPrincipal.getDialogAgregarCategoria(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
                break;
            case PARECIDA:
                int opcion = JOptionPane.showOptionDialog(menuPrincipal.getDialogAgregarCategoria(), e.getMessage() + "\n¿Deseas agregarla de todas formas?", "Advertencia", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Sí", "No"}, JOptionPane.QUESTION_MESSAGE);
                if (opcion == 1) {
                    return;
                }
                try {
                    gestionTienda.insertarCategoria(categoria);
                    menuPrincipal.getPanelModificarLibro().llenarCbCategoria(gestionTienda.listarCategorias());
                    menuPrincipal.getPanelRegistrarLibro().llenarComboBoxCategoria(gestionTienda.listarCategorias());
                    JOptionPane.showMessageDialog(menuPrincipal.getDialogAgregarCategoria(), "Categoria registrada", "Exito", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException | RuntimeException ex) {
                    JOptionPane.showMessageDialog(menuPrincipal.getDialogAgregarCategoria(), ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                break;
        }
    }

    /**
     * Vacía el carrito de compras, eliminando todos los productos añadidos.
     */
    public void activarVaciarCarrito() {
        try {
            gestionTienda.vaciarCarrito();
            menuPrincipal.getPanelCarrito().repaintPanel(new TotalesCompra(0, 0, 0, 0, 0));
            menuPrincipal.getPanelCarrito().anadirProductosPanel(gestionTienda.listaCarrito());
            JOptionPane.showMessageDialog(menuPrincipal.getDialogAgregarCategoria(), "Se vació el carrito correctamente.", "Vaciar Carrito", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getDialogAgregarCategoria(), e.getMessage(), "Advertencia", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(menuPrincipal.getDialogAgregarCategoria(), e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
