package co.edu.uptc.gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Clase que representa el menú principal de la aplicación.
 * Contiene los botones de navegación y los paneles asociados a cada sección.
 */
public class MenuPrincipal extends JPanel {

    /**
     * Etiqueta que muestra el título del menú.
     */
    private JLabel labelTituloMenu;

    /**
     * Etiqueta que muestra el nombre del usuario actualmente autenticado.
     */
    private JLabel labelNombreUsuario;

    /**
     * Botón para acceder al catálogo de libros.
     */
    private JButton botonCatalogo;

    /**
     * Botón para acceder al carrito de compras.
     */
    private JButton botonCarrito;

    /**
     * Botón para ver el historial de compras del usuario.
     */
    private JButton botonCompras;

    /**
     * Botón para acceder al perfil del usuario.
     */
    private JButton botonPerfil;

    /**
     * Botón para cerrar sesión.
     */
    private JButton botonCerrarSesion;

    /**
     * Botón para acceder a la gestión de libros (solo para administradores).
     */
    private JButton botonGestionarLibros;

    /**
     * Botón para registrar un nuevo usuario (solo para administradores).
     */
    private JButton botonRegistrarUsuario;

    /**
     * Boton para iniciar ir a la ventana Iniciar Sesión
     */
    private JButton botonIniciarSesion;

    /**
     * Panel que muestra el catálogo de libros.
     */
    private PanelCatalogo panelCatalogo;

    /**
     * Panel que muestra el contenido del carrito de compras.
     */
    private PanelCarrito panelCarrito;

    /**
     * Panel que muestra la información del perfil del usuario.
     */
    private PanelPerfil panelPerfil;

    private PanelRecibo panelRecibo;

    /**
     * Scroll que permite visualizar tablas con datos extensos.
     */
    private JScrollPane scrollTabla;

    /**
     * Administrador de diseño para cambiar entre diferentes paneles.
     */
    private CardLayout cardLayout;

    /**
     * Panel contenedor de los diferentes paneles de la aplicación.
     */
    private JPanel panelCL;

    /**
     * Panel que muestra el historial de compras del usuario.
     */
    private PanelCompras panelCompras;

    /**
     * Panel para la gestión de libros (solo para administradores).
     */
    private PanelGestionLibro panelGestionLibro;

    /**
     * Panel para registrar un nuevo libro en el sistema.
     */
    private PanelRegistrarLibro panelRegistrarLibro;

    /**
     * Panel para registrar un nuevo usuario (solo para administradores).
     */
    private PanelRegistrarUsuario panelRegistrarUsuario;

    /**
     * Panel para modificar la información de un libro existente.
     */
    private PanelModificarLibro panelModificarLibro;

    /**
     * Panel para modificar la información de un usuario existente.
     */
    private PanelModificarUsuario panelModificarUsuario;

    /**
     * Panel para eliminar un libro del sistema.
     */
    private PanelEliminarLibro panelEliminarLibro;

    /**
     * Restricciones de diseño para la disposición de los elementos en el GridBagLayout.
     */
    private GridBagConstraints gbc;

    /**
     * Panel ubicado a la izquierda que contiene el menú de navegación.
     */
    private JPanel panelIzquierda;

    /**
     * Evento para actualizar los datos del usuario.
     */
    private EventoLista eventoLista;

    private JPanel panelClPrincipal;

    private CardLayout clPrincipal;

    private PanelInicioSesion panelInicioSesion;

    /**
     * Panel para confirmar la compra
     */
    private PanelConfirmCompra panelConfirmCompra;

    /**
     * Obtiene el panel de compras del usuario.
     *
     * @return PanelCompras que muestra el historial de compras.
     */
    public PanelCompras getPanelCompras() {
        return panelCompras;
    }

    /**
     * Obtiene el panel de gestión de libros.
     *
     * @return PanelGestionLibro utilizado para administrar libros (solo administradores).
     */
    public PanelGestionLibro getPanelGestionLibro() {
        return panelGestionLibro;
    }

    /**
     * Obtiene el panel para registrar un nuevo libro en el sistema.
     *
     * @return PanelRegistrarLibro utilizado para registrar nuevos libros.
     */
    public PanelRegistrarLibro getPanelRegistrarLibro() {
        return panelRegistrarLibro;
    }

    /**
     * Obtiene el panel para registrar un nuevo usuario.
     *
     * @return PanelRegistrarUsuario utilizado para registrar nuevos usuarios (solo administradores).
     */
    public PanelRegistrarUsuario getPanelRegistrarUsuario() {
        return panelRegistrarUsuario;
    }

    /**
     * Obtiene el panel para modificar la información de un libro existente.
     *
     * @return PanelModificarLibro utilizado para editar libros.
     */
    public PanelModificarLibro getPanelModificarLibro() {
        return panelModificarLibro;
    }

    /**
     * Obtiene el panel para modificar la información de un usuario existente.
     *
     * @return PanelModificarUsuario utilizado para editar usuarios.
     */
    public PanelModificarUsuario getPanelModificarUsuario() {
        return panelModificarUsuario;
    }

    /**
     * Método duplicado de getPanelModificarUsuario. Se recomienda eliminar o corregir si tiene un propósito diferente.
     *
     * @return PanelModificarUsuario utilizado para actualizar usuarios.
     */
    public PanelModificarUsuario getPanelActualizarUsuario() {
        return panelModificarUsuario;
    }

    /**
     * Obtiene el panel de perfil del usuario.
     *
     * @return PanelPerfil que muestra la información del usuario.
     */
    public PanelPerfil getPanelPerfil() {
        return panelPerfil;
    }

    /**
     * Obtiene el panel de catálogo de libros.
     *
     * @return PanelCatalogo que muestra la lista de libros disponibles.
     */
    public PanelCatalogo getPanelCatalogo() {
        return panelCatalogo;
    }

    /**
     * Obtiene el panel para eliminar un libro del sistema.
     *
     * @return PanelEliminarLibro utilizado para eliminar libros.
     */
    public PanelEliminarLibro getPanelEliminarLibro() {
        return panelEliminarLibro;
    }

    /**
     * Establece el nombre del usuario en la etiqueta correspondiente.
     *
     * @param nombreUsuario Nombre del usuario a mostrar en la interfaz.
     */
    public void setLabelNombreUsuario(String nombreUsuario) {
        labelNombreUsuario.setText(nombreUsuario);
    }

    /**
     * Obtiene el panel del carrito de compras.
     *
     * @return PanelCarrito que muestra los libros agregados al carrito.
     */
    public PanelCarrito getPanelCarrito() {
        return panelCarrito;
    }

    public PanelRecibo getPanelRecibo() {
        return panelRecibo;
    }

    public PanelConfirmCompra getPanelConfirmCompra() {
        return panelConfirmCompra;
    }

    /**
     * Constructor del menú principal, inicializa los paneles y los agrega al cardLayout.
     *
     * @param evento Manejador de eventos principal de la aplicación.
     * @param eventoLista Manejador de eventos de la lista de libros a modificar en el comboBox.
     * @param ventanaPrincipal Referencia de ventanas de la aplicación.
     */
    public MenuPrincipal(Evento evento, VentanaPrincipal ventanaPrincipal) {
        setLayout(new BorderLayout());
        panelIzquierda = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();

        eventoLista = new EventoLista(ventanaPrincipal);
        panelCatalogo = new PanelCatalogo(ventanaPrincipal);
        panelPerfil = new PanelPerfil(evento);
        panelCarrito = new PanelCarrito(ventanaPrincipal, evento);
        panelCompras = new PanelCompras();
        scrollTabla = new JScrollPane(panelCompras);
        panelGestionLibro = new PanelGestionLibro(evento);
        panelRegistrarLibro = new PanelRegistrarLibro(evento);
        panelRegistrarUsuario = new PanelRegistrarUsuario(evento);
        panelModificarLibro = new PanelModificarLibro(evento, eventoLista);
        panelModificarUsuario = new PanelModificarUsuario(evento);
        panelEliminarLibro = new PanelEliminarLibro(ventanaPrincipal, evento);
        panelConfirmCompra = new PanelConfirmCompra(evento);
        panelGestionLibro = new PanelGestionLibro(evento);
        panelInicioSesion = new PanelInicioSesion(evento);
        panelRecibo = new PanelRecibo();
        clPrincipal = new CardLayout();
        panelClPrincipal = new JPanel(clPrincipal);

        cardLayout = new CardLayout();
        panelCL = new JPanel(cardLayout);
        panelCL.add(panelCatalogo, "Catalogo");
        panelCL.add(panelPerfil, "Perfil");
        panelCL.add(panelCarrito, "Carrito");
        panelCL.add(scrollTabla, "Compras");
        panelCL.add(panelGestionLibro, "Gestion Libros");
        panelCL.add(panelEliminarLibro, "Eliminar Libros");

        add(panelIzquierda(evento), BorderLayout.WEST);
        add(panelCL, BorderLayout.CENTER);
    }

    public PanelInicioSesion getPanelInicioSesion() {
        return panelInicioSesion;
    }

    public JPanel getPanelClPrincipal() {
        return panelClPrincipal;
    }

    public void activarIniciarSesion() {
        clPrincipal.show(panelClPrincipal, "Iniciar Sesion");
    }

    public void vistaPanelVenta() {
        clPrincipal.show(panelClPrincipal, "Panel Venta");
    }

    public void agregarVentanaCl(MenuPrincipal menuPrincipal) {
        panelClPrincipal.add(menuPrincipal, "Panel Venta");
    }

    public void agregarInicioSesion() {
        panelClPrincipal.add(panelInicioSesion, "Iniciar Sesion");
    }

    /**
     * Crea el panel que contiene el menú, las opciones de navegación y los botones de acción.
     * @param evento Manejador de eventos principal de la aplicación.
     * @return JPanel que contiene el menú principal.
     */
    public JPanel panelIzquierda(Evento evento) {
        panelIzquierda = new JPanel(new GridBagLayout());

        initAtributos();
        asignarAccionBotones(evento);
        labelNombreUsuario.setForeground(new Color(255, 0, 0));

        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.insets = new Insets(5, 5, 5, 5);
        panelIzquierda.add(labelTituloMenu, gbc);
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.gridy = 1;
        panelIzquierda.add(botonCatalogo, gbc);
        gbc.gridy = 2;
        panelIzquierda.add(botonCarrito, gbc);
        gbc.gridy = 3;
        panelIzquierda.add(botonCompras, gbc);
        gbc.gridy = 4;
        panelIzquierda.add(botonPerfil, gbc);
        gbc.weighty = 1.0;
        gbc.gridy = 7;
        panelIzquierda.add(new JLabel(), gbc);
        gbc.weighty = 0;
        gbc.gridy = 8;
        gbc.anchor = GridBagConstraints.SOUTH;
        panelIzquierda.add(labelNombreUsuario, gbc);
        gbc.gridy = 9;
        panelIzquierda.add(botonCerrarSesion, gbc);
        panelIzquierda.setBorder(new LineBorder(Color.DARK_GRAY, 2, true));
        return panelIzquierda;
    }

    private void asignarAccionBotones(Evento evento) {
        botonCatalogo.addActionListener(evento);
        botonCatalogo.setActionCommand(evento.CATALOGO);
        botonPerfil.addActionListener(evento);
        botonPerfil.setActionCommand(evento.PERFIL);
        botonCarrito.addActionListener(evento);
        botonCarrito.setActionCommand(evento.CARRITO);
        botonCerrarSesion.addActionListener(evento);
        botonCerrarSesion.setActionCommand(evento.CERRAR_SESION);
        botonCompras.addActionListener(evento);
        botonCompras.setActionCommand(evento.COMPRAS);
        botonGestionarLibros.addActionListener(evento);
        botonGestionarLibros.setActionCommand(evento.GESTIONAR_LIBROS);
        botonRegistrarUsuario.addActionListener(evento);
        botonRegistrarUsuario.setActionCommand(evento.VENTANA_REGISTRAR_USUARIO);
        botonIniciarSesion.addActionListener(evento);
        botonIniciarSesion.setActionCommand(evento.ACTIVAR_INICIAR_SESION);
    }

    private void initAtributos() {
        labelTituloMenu = new JLabel("Librería Virtual", SwingUtilities.CENTER);
        labelNombreUsuario = new JLabel("", SwingUtilities.CENTER);
        botonCatalogo = new JButton("Catalogo");
        botonCarrito = new JButton("Mi carrito");
        botonCompras = new JButton("Mis compras");
        botonPerfil = new JButton("Perfil");
        botonIniciarSesion = new JButton("Iniciar Sesión");
        botonCerrarSesion = new JButton("Cerrar Sesión");
        botonGestionarLibros = new JButton("Gestionar Libros");
        botonRegistrarUsuario = new JButton("Registrar Usuario");
    }

    public void usuarioNull() {
        botonCompras.setVisible(false);
        botonPerfil.setVisible(false);
        botonCerrarSesion.setVisible(false);
        labelNombreUsuario.setVisible(false);
        botonIniciarSesion.setVisible(true);
        botonGestionarLibros.setVisible(false);
        botonRegistrarUsuario.setVisible(false);
        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.gridy = 9;
        panelIzquierda.add(botonIniciarSesion, gbc);
        panelIzquierda.revalidate();
        panelIzquierda.repaint();
    }

    public void usuarioIniciaSesion() {

        botonCompras.setVisible(true);
        botonPerfil.setVisible(true);
        botonCerrarSesion.setVisible(true);
        labelNombreUsuario.setVisible(true);
        botonIniciarSesion.setVisible(false);
        panelIzquierda.revalidate();
        panelIzquierda.repaint();
    }

    /**
     * Agrega botones de administrador si se encuentra logueado.
     */
    public void anadirFuncionesAdmin() {
        botonGestionarLibros.setVisible(true);
        botonRegistrarUsuario.setVisible(true);
        gbc.weighty = 0;
        gbc.gridy = 5;
        panelIzquierda.add(botonGestionarLibros, gbc);
        gbc.gridy = 6;
        panelIzquierda.add(botonRegistrarUsuario, gbc);
        panelIzquierda.revalidate();
        panelIzquierda.repaint();
    }

    /**
     * Elimina botones de administrador si no se encuentra logueado.
     */
    public void quitarFuncionesAdmin() {
        panelIzquierda.remove(botonGestionarLibros);
        panelIzquierda.remove(botonRegistrarUsuario);
        panelIzquierda.revalidate();
        panelIzquierda.repaint();
    }

    /**
     * Muestra el panel del catálogo de libros.
     */
    public void activarPanelCatalogo() {
        cardLayout.show(panelCL, "Catalogo");
    }

    /**
     * Muestra el panel del perfil del usuario.
     */
    public void activarPanelPerfil() {
        cardLayout.show(panelCL, "Perfil");
    }

    /**
     * Muestra el panel del carrito de compras.
     */
    public void activarPanelCarrito() {
        cardLayout.show(panelCL, "Carrito");
    }

    /**
     * Muestra el panel del historial de compras.
     */
    public void activarPanelCompras() {
        cardLayout.show(panelCL, "Compras");
    }

    /**
     * Muestra el panel de gestión de libros (para administradores).
     */
    public void activarPanelGestionLibro() {
        cardLayout.show(panelCL, "Gestion Libros");
    }

    /**
     * Activa el panel para registrar un nuevo libro.
     */
    public void activarPanelRegistrarLibros() {
        panelRegistrarLibro.setVisible(true);
    }

    /**
     * Activa el panel para registrar un nuevo usuario.
     */
    public void activarPanelRegistrarUsuario() {
        panelRegistrarUsuario.setVisible(true);
    }

    /**
     * Oculta el panel de registro de libros.
     */
    public void activarCancelarRegistroLibro() {
        panelRegistrarLibro.setVisible(false);
    }

    /**
     * Oculta el panel de registro de usuario.
     */
    public void activarCancelarRegistroUsuario() {
        panelRegistrarUsuario.setVisible(false);
    }

    /**
     * Activa el panel para modificar un libro.
     */
    public void activarPanelModificarLibro() {
        panelModificarLibro.setVisible(true);
    }

    /**
     * Oculta el panel de modificación de libros.
     */
    public void activarCancelarModificacionLibro() {
        panelModificarLibro.setVisible(false);
    }

    /**
     * Activa el panel para actualizar los datos de un usuario.
     */
    public void activarActualizarDatosUsuario() {
        panelModificarUsuario.setVisible(true);
    }

    /**
     * Oculta el panel de actualización de usuario.
     */
    public void activarCancelarActualizarUser() {
        panelModificarUsuario.setVisible(false);
    }

    /**
     * Muestra el panel de eliminación de libros.
     */
    public void activarEliminarLibros() {
        cardLayout.show(panelCL, "Eliminar Libros");
    }

    public void activarPanelConfirmCompra() {
        panelConfirmCompra.visibilizar();
    }

    public void activarPanelRecibo() {
        panelRecibo.setVisible(true);
    }
}
