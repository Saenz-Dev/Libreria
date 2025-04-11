package co.edu.uptc.gui;

import co.edu.uptc.negocio.Usuario;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa el panel de modificar el usuario en la interfaz gráfica.
 * Permite visualizar los datos del usuario y gestionar su modificación.
 */
public class PanelModificarUsuario extends JDialog {

    /** Etiqueta para el nombre del usuario. */
    private JLabel labelNombre;

    /** Etiqueta para el correo electrónico del usuario. */
    private JLabel labelCorreo;

    /** Etiqueta para la contraseña del usuario. */
    private JLabel labelContrasena;

    /** Etiqueta para la dirección del usuario. */
    private JLabel labelDireccion;

    /** Etiqueta para el número de teléfono del usuario. */
    private JLabel labelTelefono;

    /** Etiqueta para el tipo de cliente (ej. Regular, VIP). */
    private JLabel labelTipoCliente;

    /** Campo de texto para ingresar el nombre del usuario. */
    private JTextField txtNombre;

    /** Campo de texto para ingresar el correo electrónico del usuario. */
    private JTextField txtCorreo;

    /** Campo de texto para ingresar la contraseña del usuario. */
    private JTextField txtContrasena;

    /** Campo de texto para ingresar la dirección del usuario. */
    private JTextField txtDireccion;

    /** Campo de texto para ingresar el número de teléfono del usuario. */
    private JTextField txtTelefono;

    /** ComboBox para seleccionar el tipo de cliente (ej. Regular, VIP). */
    private JComboBox cbTipoCliente;

    /** Etiqueta para el título de la sección de actualización de datos. */
    private JLabel labelTitulo;

    /** Botón para actualizar la información del usuario. */
    private JButton botonActualizar;

    /** Botón para cancelar la actualización y cerrar la ventana. */
    private JButton botonCancelar;

    /**
     * Obtiene el nombre ingresado por el usuario.
     * @return el nombre como una cadena de texto.
     */
    public String getTxtNombre() {
        return txtNombre.getText();
    }

    /**
     * Obtiene el correo electrónico ingresado por el usuario.
     * @return el correo como una cadena de texto.
     */
    public String getTxtCorreo() {
        return txtCorreo.getText();
    }

    /**
     * Obtiene la contraseña ingresada por el usuario.
     * @return la contraseña como una cadena de texto.
     */
    public String getTxtContrasena() {
        return txtContrasena.getText();
    }

    /**
     * Obtiene la dirección ingresada por el usuario.
     * @return la dirección como una cadena de texto.
     */
    public String getTxtDireccion() {
        return txtDireccion.getText();
    }

    /**
     * Obtiene el número de teléfono ingresado por el usuario.
     * @return el teléfono como una cadena de texto.
     */
    public String getTxtTelefono() {
        return txtTelefono.getText();
    }

    /**
     * Obtiene el tipo de cliente seleccionado en el JComboBox.
     * @return el tipo de cliente como una cadena de texto.
     */
    public String getCbTipoCliente() {
        return (String) cbTipoCliente.getSelectedItem();
    }

    /**
     * Establece un nuevo nombre en el campo de texto.
     * @param nombre el nuevo nombre a asignar.
     */
    public void setTxtNombre(String nombre) {
        txtNombre.setText(nombre);
    }

    /**
     * Establece un nuevo correo en el campo de texto.
     * @param correo el nuevo correo a asignar.
     */
    public void setTxtCorreo(String correo) {
        txtCorreo.setText(correo);
    }

    /**
     * Establece una nueva contraseña en el campo de texto.
     * @param contrasena la nueva contraseña a asignar.
     */
    public void setTxtContrasena(String contrasena) {
        txtContrasena.setText(contrasena);
    }

    /**
     * Establece una nueva dirección en el campo de texto.
     * @param direccion la nueva dirección a asignar.
     */
    public void setTxtDireccion(String direccion) {
        txtDireccion.setText(direccion);
    }

    /**
     * Establece un nuevo número de teléfono en el campo de texto.
     * @param telefono el nuevo teléfono a asignar.
     */
    public void setTxtTelefono(String telefono) {
        txtTelefono.setText(telefono);
    }

    /**
     * Establece un nuevo tipo de cliente en el JComboBox.
     * @param tipoCliente el nuevo tipo de cliente a asignar.
     */
    public void setCbTipoCliente(String tipoCliente) {
        cbTipoCliente.setSelectedItem(tipoCliente);
    }

    /**
     * Constructor del panel de actualización de datos del usuario.
     * @param evento Manejador de eventos de la aplicación.
     */
    public PanelModificarUsuario(Evento evento) {
        setTitle("Actualizar Datos Usuario");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 10, 5, 10);
        inicializarAtributos();
        asignarAccionBoton(evento);

        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(labelTitulo, gbc);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        add(labelNombre, gbc);
        gbc.gridx = 1;
        add(txtNombre, gbc);
        gbc.gridy = 2;
        gbc.gridx = 0;
        add(labelDireccion, gbc);
        gbc.gridx = 1;
        add(txtDireccion, gbc);
        gbc.gridy = 3;
        gbc.gridx = 0;
        add(labelTelefono, gbc);
        gbc.gridx = 1;
        add(txtTelefono, gbc);
        gbc.gridy = 4;
        gbc.gridx = 0;
        add(labelTipoCliente, gbc);
        gbc.gridx = 1;
        add(cbTipoCliente, gbc);
        gbc.gridy = 5;
        gbc.gridx = 0;
        add(labelCorreo, gbc);
        gbc.gridx = 1;
        add(txtCorreo, gbc);
        gbc.gridy = 6;
        gbc.gridx = 0;
        add(labelContrasena, gbc);
        gbc.gridx = 1;
        add(txtContrasena, gbc);
        gbc.gridy = 7;
        gbc.gridx = 0;
        add(botonActualizar, gbc);
        gbc.gridx = 1;
        add(botonCancelar, gbc);

        setResizable(false);
        setModal(true);
        setSize(400, 350);
        setLocationRelativeTo(null);
    }

    private void asignarAccionBoton(Evento evento) {
        botonActualizar.addActionListener(evento);
        botonActualizar.setActionCommand(evento.ACEPTAR_ACTUALIZAR_USUARIO);
        botonCancelar.addActionListener(evento);
        botonCancelar.setActionCommand(evento.CANCELAR_ACTUALIZAR_USUARIO);
    }

    /**
     * Inicializa los atributos del panel de actualización de datos del usuario.
     */
    public void inicializarAtributos() {
        labelNombre = new JLabel("Nombre*:");
        labelCorreo = new JLabel("Correo Electrónico*:");
        labelContrasena = new JLabel("Contraseña*: ");
        labelDireccion = new JLabel("Dirección*:");
        labelTelefono = new JLabel("Teléfono*:");
        labelTipoCliente = new JLabel("Tipo de Cliente:");
        txtNombre = new JTextField(20);
        txtCorreo = new JTextField(20);
        txtContrasena = new JTextField(20);
        txtDireccion = new JTextField(20);
        txtTelefono = new JTextField(20);
        String[] tiposCliente = {"Regular", "Premium"};
        cbTipoCliente = new JComboBox<>(tiposCliente);
        labelTitulo = new JLabel("Actualizar Información Usuario");
        botonActualizar = new JButton("Actualizar");
        botonCancelar = new JButton("Cancelar");
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));
    }

    public Usuario obtenerDatos() {
        Usuario usuario = new Usuario();
        try {
            usuario.setNombre(getTxtNombre());
            usuario.setDireccionEnvio(getTxtDireccion());
            usuario.setTelefono(Long.parseLong(getTxtTelefono()));
            usuario.setTipoCliente(getCbTipoCliente());
            usuario.getCuenta().setCorreo(getTxtCorreo());
            usuario.getCuenta().setContrasena(getTxtContrasena());
            return usuario;
        } catch (RuntimeException e) {
            throw new RuntimeException("Verifica que el teléfono no contenga:\n-Caracteres especiales\n-Espacios\n-Signos de puntación");
        }
    }

    public void llenarCampos(Usuario usuario) {
        setTxtNombre(usuario.getNombre());
        setTxtCorreo(usuario.getCuenta().getCorreo());
        setTxtContrasena(usuario.getCuenta().getContrasena());
        setTxtDireccion(usuario.getDireccionEnvio());
        setTxtTelefono(String.valueOf(usuario.getTelefono()));
        setCbTipoCliente(usuario.getTipoCliente());
    }

    public void limpiarTxt() {
        setTxtNombre("");
        setTxtTelefono("");
        setTxtDireccion("");
        setCbTipoCliente("");
        setTxtCorreo("");
        setTxtContrasena("");
    }
}
