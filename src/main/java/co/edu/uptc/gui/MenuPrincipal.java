package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import co.edu.uptc.modelo.TipoUsuario;
import co.edu.uptc.modelo.Usuario;

/**
 * Clase que representa el menú principal de la aplicación. Contiene los botones
 * de navegación y los paneles asociados a cada sección.
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
    
    private JButton botonPremium;
    
    private JButton botonGuardarCodigos;

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
     * Restricciones de diseño para la disposición de los elementos en el
     * GridBagLayout.
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

    private PanelConfirmCompra panelConfirmCompra;

    private PanelComentario panelComentario;

    private PanelCalificar panelCalificar;
    
    private PanelAggCodigo panelAggCodigo;

    private VentanaPrincipal ventanaPrincipal;

    private final Color COLOR_ACTIVO = new Color(25, 118, 210);

    private final Color COLOR_INACTIVO = new Color(187, 222, 251);
    
    private final Color COLOR_CERRAR_SESION = new Color(229, 115, 115);
    
    private final Color COLOR_IZQUIERDA = new Color(227, 242, 253);
    
    private final Color COLOR_LETRA = new Color(64, 64, 64);
    
    private PanelPremium panelPremium;

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
     * @return PanelGestionLibro utilizado para administrar libros (solo
     *         administradores).
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
     * @return PanelRegistrarUsuario utilizado para registrar nuevos usuarios (solo
     *         administradores).
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
     * Metodo duplicado de getPanelModificarUsuario. Se recomienda eliminar o
     * corregir si tiene un propósito diferente.
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
    public void setLabelNombreUsuario(Usuario usuario) {
	labelNombreUsuario.setText("<html><div align: 'center'>" + usuario.getNombre() + " - " + usuario.getTipoCliente() + "</div></html>");
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

    public PanelComentario getPanelComentario() {
	return panelComentario;
    }

    public void setPanelComentario(PanelComentario panelComentario) {
	this.panelComentario = panelComentario;
    }

    public PanelCalificar getPanelCalificar() {
	return panelCalificar;
    }
    
    public PanelPremium getPanelPremium() {
	return panelPremium;
    }
    
    public PanelAggCodigo getPanelAggCodigo() {
	return panelAggCodigo;
    }
    
    public JButton getBotonPremium() {
	return botonPremium;
    }

    public void setPanelCalificar(PanelCalificar panelCalificar) {
	this.panelCalificar = panelCalificar;
    }

    /**
     * Constructor del menú principal, inicializa los paneles y los agrega al
     * cardLayout.
     *
     * @param evento           Manejador de eventos principal de la aplicación.
     * @param ventanaPrincipal Referencia de ventanas de la aplicación.
     */
    public MenuPrincipal(Evento evento, VentanaPrincipal ventanaPrincipal) {
	setLayout(new BorderLayout());
	panelIzquierda = new JPanel(new GridBagLayout());
	gbc = new GridBagConstraints();
	this.ventanaPrincipal = ventanaPrincipal;

	eventoLista = new EventoLista(ventanaPrincipal);
	panelCatalogo = new PanelCatalogo(ventanaPrincipal);
	panelPerfil = new PanelPerfil(evento);
	panelCarrito = new PanelCarrito(ventanaPrincipal, evento);
	panelCompras = new PanelCompras(evento, ventanaPrincipal);
	scrollTabla = new JScrollPane(panelCompras);
	panelGestionLibro = new PanelGestionLibro(evento);
	panelRegistrarLibro = new PanelRegistrarLibro(evento);
	panelRegistrarUsuario = new PanelRegistrarUsuario(evento);
	panelModificarLibro = new PanelModificarLibro(evento, eventoLista);
	panelModificarUsuario = new PanelModificarUsuario(evento);
	panelEliminarLibro = new PanelEliminarLibro(ventanaPrincipal, evento);
	panelConfirmCompra = new PanelConfirmCompra(ventanaPrincipal, evento);
	panelGestionLibro = new PanelGestionLibro(evento);
	panelInicioSesion = new PanelInicioSesion(evento);
	panelComentario = new PanelComentario();
	panelCalificar = new PanelCalificar(evento);
	panelRecibo = new PanelRecibo(ventanaPrincipal);
	panelPremium = new PanelPremium(evento);
	panelAggCodigo = new PanelAggCodigo(evento);
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
	panelCL.add(panelAggCodigo, "Agregar Codigos");

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
     * Crea el panel que contiene el menú, las opciones de navegación y los botones
     * de acción.
     * 
     * @param evento Manejador de eventos principal de la aplicación.
     * @return JPanel que contiene el menú principal.
     */
    public JPanel panelIzquierda(Evento evento) {
	panelIzquierda = new JPanel(new GridBagLayout());

	initAtributos();
	personalizarBotones();
	asignarAccionBotones(evento);
	labelNombreUsuario.setForeground(COLOR_LETRA);

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
	gbc.gridy = 5;
	panelIzquierda.add(botonPremium, gbc);
	gbc.weighty = 1.0;
	gbc.gridy = 8;
	panelIzquierda.add(new JLabel(), gbc);
	gbc.weighty = 0;
	gbc.gridy = 9;
	gbc.anchor = GridBagConstraints.SOUTH;
	panelIzquierda.add(labelNombreUsuario, gbc);
	gbc.gridy = 10;
	panelIzquierda.add(botonCerrarSesion, gbc);
	panelIzquierda.setBorder(new LineBorder(Color.DARK_GRAY, 2, true));
	panelIzquierda.setBackground(COLOR_IZQUIERDA);
	panelIzquierda.setPreferredSize(new Dimension(150, 500));
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
	botonPremium.addActionListener(evento);
	botonPremium.setActionCommand(evento.ACTIVAR_PANEL_PREMIUM);
	botonGuardarCodigos.addActionListener(evento);
	botonGuardarCodigos.setActionCommand(evento.ACTIVAR_GUARDAR_CODIGO);
    }

    private void initAtributos() {
	labelTituloMenu = new JLabel("Libreria Virtual", SwingUtilities.CENTER);
	labelNombreUsuario = new JLabel("", SwingUtilities.CENTER);
	labelNombreUsuario.setPreferredSize(new Dimension(20, 50));
	botonCatalogo = new JButton("Catalogo");
	botonCarrito = new JButton("Mi carrito");
	botonCompras = new JButton("Mis compras");
	botonPerfil = new JButton("Perfil");
	botonIniciarSesion = new JButton("Iniciar Sesión");
	botonCerrarSesion = new JButton("Cerrar Sesión");
	botonGestionarLibros = new JButton("Gestionar Libros");
	botonRegistrarUsuario = new JButton("Registrar Usuario");
	botonGuardarCodigos = new JButton("Guardar Codigos");
	botonPremium = new JButton("✨");
    }

    private void personalizarBotones() {
	botonCatalogo.setBackground(COLOR_INACTIVO);
	botonCatalogo.setForeground(COLOR_LETRA);
	botonCarrito.setBackground(COLOR_INACTIVO);
	botonCarrito.setForeground(COLOR_LETRA);
	botonCompras.setBackground(COLOR_INACTIVO);
	botonCompras.setForeground(COLOR_LETRA);
	botonPerfil.setBackground(COLOR_INACTIVO);
	botonPerfil.setForeground(COLOR_LETRA);
	botonIniciarSesion.setBackground(COLOR_INACTIVO);
	botonIniciarSesion.setForeground(COLOR_LETRA);
	botonCerrarSesion.setBackground(COLOR_CERRAR_SESION);
	botonCerrarSesion.setForeground(Color.WHITE);
	botonGestionarLibros.setBackground(COLOR_INACTIVO);
	botonGestionarLibros.setForeground(COLOR_LETRA);
	botonRegistrarUsuario.setBackground(COLOR_INACTIVO);
	botonRegistrarUsuario.setForeground(COLOR_LETRA);
	botonPremium.setBackground(COLOR_INACTIVO);
	botonPremium.setForeground(COLOR_LETRA);
	botonGuardarCodigos.setBackground(COLOR_INACTIVO);
	botonGuardarCodigos.setForeground(COLOR_LETRA);
    }

    public void usuarioNull() {
	botonCompras.setVisible(false);
	botonPerfil.setVisible(false);
	botonCerrarSesion.setVisible(false);
	labelNombreUsuario.setVisible(false);
	botonIniciarSesion.setVisible(true);
	botonGestionarLibros.setVisible(false);
	botonRegistrarUsuario.setVisible(false);
	botonCatalogo.setVisible(true);
	botonCarrito.setVisible(true);
	botonPremium.setVisible(false);
	botonGuardarCodigos.setVisible(false);
	gbc.anchor = GridBagConstraints.SOUTH;
	gbc.gridy = 9;
	panelIzquierda.add(botonIniciarSesion, gbc);
	panelIzquierda.revalidate();
	panelIzquierda.repaint();
    }

    public void usuarioIniciaSesion(Usuario usuario) {
	botonCompras.setVisible(true);
	botonPerfil.setVisible(true);
	botonCerrarSesion.setVisible(true);
	labelNombreUsuario.setVisible(true);
	botonIniciarSesion.setVisible(false);
	botonGuardarCodigos.setVisible(false);
	botonPremium.setVisible(usuario.getTipoCliente() == TipoUsuario.Regular);
	panelIzquierda.revalidate();
	panelIzquierda.repaint();
    }

    /**
     * Agrega botones de administrador si se encuentra logueado.
     */
    public void anadirFuncionesAdmin() {
	botonGestionarLibros.setVisible(true);
	botonRegistrarUsuario.setVisible(true);
	botonCompras.setVisible(false);
	botonCatalogo.setVisible(true);
	botonCarrito.setVisible(false);
	botonPerfil.setVisible(false);
	botonPremium.setVisible(false);
	botonGuardarCodigos.setVisible(true);
	
	gbc.weighty = 0;
	gbc.gridy = 5;
	panelIzquierda.add(botonGestionarLibros, gbc);
	gbc.gridy = 6;
	panelIzquierda.add(botonRegistrarUsuario, gbc);
	gbc.gridy = 7;
	panelIzquierda.add(botonGuardarCodigos, gbc);
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
	personalizarBotones();
	botonCatalogo.setBackground(COLOR_ACTIVO);
	cardLayout.show(panelCL, "Catalogo");
    }

    /**
     * Muestra el panel del perfil del usuario.
     */
    public void activarPanelPerfil() {
	personalizarBotones();
	botonPerfil.setBackground(COLOR_ACTIVO);
	cardLayout.show(panelCL, "Perfil");
    }

    /**
     * Muestra el panel del carrito de compras.
     */
    public void activarPanelCarrito() {
	personalizarBotones();
	botonCarrito.setBackground(COLOR_ACTIVO);
	cardLayout.show(panelCL, "Carrito");
    }

    /**
     * Muestra el panel del historial de compras.
     */
    public void activarPanelCompras() {
	personalizarBotones();
	botonCompras.setBackground(COLOR_ACTIVO);
	cardLayout.show(panelCL, "Compras");
    }

    /**
     * Muestra el panel de gestión de libros (para administradores).
     */
    public void activarPanelGestionLibro() {
	personalizarBotones();
	botonGestionarLibros.setBackground(COLOR_ACTIVO);
	cardLayout.show(panelCL, "Gestion Libros");
    }
    
    public void activarPanelGuardarCodigos() {
	personalizarBotones();
	botonGuardarCodigos.setBackground(COLOR_ACTIVO);
	cardLayout.show(panelCL, "Agregar Codigos");
    }

    /**
     * Activa el panel para registrar un nuevo libro.
     */
    public void activarPanelRegistrarLibros() {
	personalizarBotones();
	panelRegistrarLibro.setLocationRelativeTo(ventanaPrincipal);
	panelRegistrarLibro.setVisible(true);
    }

    /**
     * Activa el panel para registrar un nuevo usuario.
     */
    public void activarPanelRegistrarUsuario() {
	panelRegistrarUsuario.setLocationRelativeTo(ventanaPrincipal);
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
	panelModificarLibro.setLocationRelativeTo(ventanaPrincipal);
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
	panelModificarUsuario.setLocationRelativeTo(ventanaPrincipal);
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
	panelConfirmCompra.setLocationRelativeTo(ventanaPrincipal);
	panelConfirmCompra.setVisible(true);
    }

    public void activarPanelRecibo() {
	panelRecibo.setLocationRelativeTo(ventanaPrincipal);
	panelRecibo.setVisible(true);
    }

    public void activarPanelComentario() {
	panelComentario.setVisible(true);
    }

    public void desactivarPanelComentario() {
	panelComentario.setLocationRelativeTo(ventanaPrincipal);
	panelComentario.setVisible(false);
    }

    public void activarPanelCalificar() {
	panelCalificar.setLocationRelativeTo(ventanaPrincipal);
	panelCalificar.setVisible(true);
    }

    public void desactivarPanelCalificar() {
	panelCalificar.setVisible(false);
    }

    public void activarPanelPremium() {
	panelPremium.setLocationRelativeTo(ventanaPrincipal);
	panelPremium.setVisible(true);
    }
}
