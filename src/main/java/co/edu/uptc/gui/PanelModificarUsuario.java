package co.edu.uptc.gui;

import co.edu.uptc.modelo.TipoLibroEnum;
import co.edu.uptc.modelo.TipoUsuarioEnum;
import co.edu.uptc.modelo.Usuario;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Clase que representa el panel de modificar el usuario en la interfaz gráfica.
 * Permite visualizar los datos del usuario y gestionar su modificación.
 */
public class PanelModificarUsuario extends JDialog {

    /**
     * Etiqueta para el nombre del usuario.
     */
    private JLabel labelNombre;

    /**
     * Etiqueta para el correo electrónico del usuario.
     */
    private JLabel labelCorreo;

    /**
     * Etiqueta para la contraseña del usuario.
     */
    private JLabel labelContrasena;

    /**
     * Etiqueta para la dirección del usuario.
     */
    private JLabel labelDireccion;

    /**
     * Etiqueta para el número de teléfono del usuario.
     */
    private JLabel labelTelefono;

    /**
     * Etiqueta para el tipo de cliente (ej. Regular, VIP).
     */
    private JLabel labelTipoCliente;

    /**
     * Campo de texto para ingresar el nombre del usuario.
     */
    private JTextField txtNombre;

    /**
     * Campo de texto para ingresar el correo electrónico del usuario.
     */
    private JTextField txtCorreo;

    /**
     * Campo de texto para ingresar la contraseña del usuario.
     */
    private JPasswordField txtContrasena;

    /**
     * Campo de texto para ingresar la dirección del usuario.
     */
    private JTextField txtDireccion;

    /**
     * Campo de texto para ingresar el número de teléfono del usuario.
     */
    private JTextField txtTelefono;

    /**
     * ComboBox para seleccionar el tipo de cliente (ej. Regular, VIP).
     */
    private JComboBox cbTipoCliente;

    /**
     * Etiqueta para el título de la sección de actualización de datos.
     */
    private JLabel labelTitulo;

    /**
     * Botón para actualizar la información del usuario.
     */
    private JButton botonActualizar;

    /**
     * Botón para cancelar la actualización y cerrar la ventana.
     */
    private JButton botonCancelar;

    private JButton botonEliminar;

    private JComboBox cbUsuario;

    private JButton botonRegistrarUsuario;

    private JLabel labelUsuario;

    private Font fontBotones = new Font("Arial", Font.BOLD, 12);

    /**
     * Obtiene el nombre ingresado por el usuario.
     *
     * @return el nombre como una cadena de texto.
     */
    public String getTxtNombre() {
        return txtNombre.getText();
    }

    /**
     * Obtiene el correo electrónico ingresado por el usuario.
     *
     * @return el correo como una cadena de texto.
     */
    public String getTxtCorreo() {
        return txtCorreo.getText();
    }

    /**
     * Obtiene la contraseña ingresada por el usuario.
     *
     * @return la contraseña como una cadena de texto.
     */
    public String getTxtContrasena() {
        return txtContrasena.getText();
    }

    /**
     * Obtiene la dirección ingresada por el usuario.
     *
     * @return la dirección como una cadena de texto.
     */
    public String getTxtDireccion() {
        return txtDireccion.getText();
    }

    /**
     * Obtiene el número de teléfono ingresado por el usuario.
     *
     * @return el teléfono como una cadena de texto.
     */
    public String getTxtTelefono() {
        return txtTelefono.getText();
    }

    /**
     * Obtiene el tipo de cliente seleccionado en el JComboBox.
     *
     * @return el tipo de cliente como una cadena de texto.
     */
    public TipoUsuarioEnum getCbTipoCliente() {
        return TipoUsuarioEnum.valueOf(cbTipoCliente.getSelectedItem().toString());
    }

    /**
     * Establece un nuevo nombre en el campo de texto.
     *
     * @param nombre el nuevo nombre a asignar.
     */
    public void setTxtNombre(String nombre) {
        txtNombre.setText(nombre);
    }

    /**
     * Establece un nuevo correo en el campo de texto.
     *
     * @param correo el nuevo correo a asignar.
     */
    public void setTxtCorreo(String correo) {
        txtCorreo.setText(correo);
    }

    /**
     * Establece una nueva contraseña en el campo de texto.
     *
     * @param contrasena la nueva contraseña a asignar.
     */
    public void setTxtContrasena(String contrasena) {
        txtContrasena.setText(contrasena);
    }

    /**
     * Establece una nueva dirección en el campo de texto.
     *
     * @param direccion la nueva dirección a asignar.
     */
    public void setTxtDireccion(String direccion) {
        txtDireccion.setText(direccion);
    }

    /**
     * Establece un nuevo número de teléfono en el campo de texto.
     *
     * @param telefono el nuevo teléfono a asignar.
     */
    public void setTxtTelefono(String telefono) {
        txtTelefono.setText(telefono);
    }

    /**
     * Establece un nuevo tipo de cliente en el JComboBox.
     *
     * @param tipoCliente el nuevo tipo de cliente a asignar.
     */
    public void setCbTipoCliente(TipoUsuarioEnum tipoCliente) {
        cbTipoCliente.setSelectedItem(tipoCliente);
    }

    public JButton getBotonEliminar() {
        return botonEliminar;
    }

    public void setBotonEliminar(JButton botonEliminar) {
        this.botonEliminar = botonEliminar;
    }

    public JComboBox getCbUsuario() {
        return cbUsuario;
    }

    public void setCbUsuario(JComboBox cbUsuario) {
        this.cbUsuario = cbUsuario;
    }

    /**
     * Constructor del panel de actualización de datos del usuario.
     *
     * @param evento Manejador de eventos de la aplicación.
     */
    public PanelModificarUsuario(Evento evento, EventoGestionUsuario eventoGestionUsuario) {
        setTitle("Actualizar Datos del Usuario");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 10, 5, 10);
        inicializarAtributos();
        asignarAccionBoton(evento, eventoGestionUsuario);
        personalizarComponentes();

        // Limitar el ancho de los JTextField
        Dimension campoDimension = new Dimension(200, 25);
        txtNombre.setPreferredSize(campoDimension);
        txtDireccion.setPreferredSize(campoDimension);
        txtTelefono.setPreferredSize(campoDimension);
        txtCorreo.setPreferredSize(campoDimension);
        txtContrasena.setPreferredSize(campoDimension);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;
        add(labelTitulo, gbc);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1;
        gbc.weighty = 0;
        botonRegistrarUsuario.setToolTipText("Registrar nuevo usuario");
        add(botonRegistrarUsuario, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridy = 1;
        gbc.gridx = 0;
        add(labelUsuario, gbc);

        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 1;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(cbUsuario, gbc);

        // Campos y etiquetas
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.NONE;

        gbc.gridy = 2;
        gbc.gridx = 0;
        add(labelNombre, gbc);
        gbc.gridx = 1;
        add(txtNombre, gbc);

        gbc.gridy = 3;
        gbc.gridx = 0;
        add(labelDireccion, gbc);
        gbc.gridx = 1;
        add(txtDireccion, gbc);

        gbc.gridy = 4;
        gbc.gridx = 0;
        add(labelTelefono, gbc);
        gbc.gridx = 1;
        add(txtTelefono, gbc);

        gbc.gridy = 5;
        gbc.gridx = 0;
        add(labelTipoCliente, gbc);
        gbc.gridx = 1;
        add(cbTipoCliente, gbc);

        gbc.gridy = 6;
        gbc.gridx = 0;
        add(labelCorreo, gbc);
        gbc.gridx = 1;
        add(txtCorreo, gbc);

        gbc.gridy = 7;
        gbc.gridx = 0;
        add(labelContrasena, gbc);
        gbc.gridx = 1;
        add(txtContrasena, gbc);

        // Panel de botones
        JPanel panelBotones = ajustarPanelBotones();
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(panelBotones, gbc);

        setResizable(false);
        setModal(true);
        setSize(400, 450);
        setLocationRelativeTo(null);
    }

    public void llenarComboBoxUsuarios(ArrayList<Usuario> usuarios) {
        cbUsuario.removeAllItems();
        for (Usuario usuario : usuarios) {
            cbUsuario.addItem(usuario.getNombre());
        }
        cbUsuario.setSelectedItem(0);
    }

    private JPanel ajustarPanelBotones() {
        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(botonActualizar);
        panelBotones.add(botonEliminar);
        panelBotones.add(botonCancelar);
        return panelBotones;
    }

    private void asignarAccionBoton(Evento evento, EventoGestionUsuario eventoGestionUsuario) {
        botonActualizar.addActionListener(evento);
        botonActualizar.setActionCommand(Evento.ACEPTAR_ACTUALIZAR_USUARIO);
        botonCancelar.addActionListener(evento);
        botonCancelar.setActionCommand(Evento.CANCELAR_ACTUALIZAR_USUARIO);
        botonEliminar.addActionListener(evento);
        botonEliminar.setActionCommand(Evento.ELIMINAR_USUARIO);
        cbUsuario.addItemListener(eventoGestionUsuario);
        botonRegistrarUsuario.addActionListener(evento);
        botonRegistrarUsuario.setActionCommand(Evento.VENTANA_REGISTRAR_USUARIO);
    }

    /**
     * Inicializa los atributos del panel de actualización de datos del usuario.
     */
    public void inicializarAtributos() {
        labelUsuario = new JLabel("Usuario:");
        labelNombre = new JLabel("Nombre*:");
        labelCorreo = new JLabel("Correo Electrónico*:");
        labelContrasena = new JLabel("Contraseña*: ");
        labelDireccion = new JLabel("Dirección*:");
        labelTelefono = new JLabel("Teléfono*:");
        labelTipoCliente = new JLabel("Tipo de Cliente:");
        txtNombre = new JTextField(20);
        txtCorreo = new JTextField(20);
        txtCorreo.setEditable(false);
        txtContrasena = new JPasswordField(20);
        txtDireccion = new JTextField(20);
        txtTelefono = new JTextField(20);
        cbTipoCliente = new JComboBox<>(TipoUsuarioEnum.values());
        labelTitulo = new JLabel("Gestionar Usuario");
        botonActualizar = new JButton("Actualizar Usuario");
        botonCancelar = new JButton("Salir");
        botonEliminar = new JButton("Eliminar Usuario");
        botonRegistrarUsuario = new JButton();
        ImageIcon iconoBoton = new ImageIcon("src/main/resources/registro.png");
        Image imagenEscalada = iconoBoton.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        botonRegistrarUsuario.setIcon(new ImageIcon(imagenEscalada));
        botonRegistrarUsuario.setPreferredSize(new Dimension(30, 30));
        cbUsuario = new JComboBox();
    }

    public void setLabelTitulo (String titulo) {
        labelTitulo.setText(titulo);
        revalidate();
        repaint();
    }

    private void personalizarComponentes() {
        botonActualizar.setBackground(Color.GREEN);
        botonActualizar.setForeground(Color.WHITE);
        botonActualizar.setFont(fontBotones);
        botonCancelar.setBackground(Color.GRAY);
        botonCancelar.setForeground(Color.WHITE);
        botonCancelar.setFont(fontBotones);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        botonEliminar.setBackground(Color.RED);
        botonEliminar.setForeground(Color.WHITE);
        botonEliminar.setFont(fontBotones);
    }

    public Usuario obtenerDatos() {
        Usuario usuario = new Usuario();
        usuario.setNombre(getTxtNombre());
        usuario.setDireccionEnvio(getTxtDireccion());
        usuario.setTipoCliente(getCbTipoCliente());
        usuario.getCuenta().setCorreo(getTxtCorreo());
        usuario.getCuenta().setContrasena(getTxtContrasena());
        if (getTxtTelefono() == null || getTxtTelefono().isBlank() || getTxtTelefono().isEmpty()) {
            usuario.setTelefono(0);
        } else if (!getTxtTelefono().matches("^[0-9]+$")) {
            usuario.setTelefono(-1);
        } else {
            try {
                usuario.setTelefono(Long.parseLong(getTxtTelefono()));
            } catch (NumberFormatException e) {
                throw new RuntimeException("El teléfono debe ser un número válido.");
            }
        }
        return usuario;
    }

    public void llenarCampos(Usuario usuario, boolean esAdmin) {
        setTxtNombre(usuario.getNombre());
        setTxtCorreo(usuario.getCuenta().getCorreo());
        setTxtContrasena(usuario.getCuenta().getContrasena());
        setTxtDireccion(usuario.getDireccionEnvio());
        setTxtTelefono(String.valueOf(usuario.getTelefono()));
        labelUsuario.setVisible(esAdmin);
        botonRegistrarUsuario.setVisible(esAdmin);
        cbUsuario.setVisible(esAdmin);
        cbTipoCliente.setVisible(esAdmin);
        labelTipoCliente.setVisible(esAdmin);
        botonEliminar.setVisible(esAdmin);
        setCbTipoCliente(usuario.getTipoCliente());
        revalidate();
        repaint();
    }

    public void limpiarTxt() {
        setTxtNombre("");
        setTxtTelefono("");
        setTxtDireccion("");
        cbTipoCliente.setSelectedIndex(0);
        setTxtCorreo("");
        setTxtContrasena("");
    }
}
