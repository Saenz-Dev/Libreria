package co.edu.uptc.gui;

import co.edu.uptc.negocio.Usuario;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa el panel de registro (formulario) de usuarios en la interfaz gráfica.
 * Permite ingresar los datos del usuario y gestionar su registro.
 */
public class PanelRegistrarUsuario extends JDialog {

    /**
     * Etiqueta para el nombre del cliente.
     */
    private JLabel labelNombre;

    /**
     * Etiqueta para el correo del cliente.
     */
    private JLabel labelCorreo;

    /**
     * Etiqueta para la contraseña del cliente.
     */
    private JLabel labelContrasena;

    /**
     * Etiqueta para la dirección del cliente.
     */
    private JLabel labelDireccion;

    /**
     * Etiqueta para el teléfono del cliente.
     */
    private JLabel labelTelefono;

    /**
     * Etiqueta para el tipo de cliente.
     */
    private JLabel labelTipoCliente;

    /**
     * Campo de texto para ingresar el nombre del cliente.
     */
    private JTextField txtNombre;

    /**
     * Campo de texto para ingresar el correo del cliente.
     */
    private JTextField txtCorreo;

    /**
     * Campo de texto para ingresar la contraseña del cliente.
     */
    private JTextField txtContrasena;

    /**
     * Campo de texto para ingresar la dirección del cliente.
     */
    private JTextField txtDireccion;

    /**
     * Campo de texto para ingresar el teléfono del cliente.
     */
    private JTextField txtTelefono;

    /**
     * ComboBox para seleccionar el tipo de cliente.
     */
    private JComboBox cbTipoCliente;

    /**
     * Etiqueta para el título del formulario.
     */
    private JLabel labelTitulo;

    /**
     * Botón para registrar un nuevo cliente.
     */
    private JButton botonRegistrar;

    /**
     * Botón para cancelar el registro.
     */
    private JButton botonCancelar;

    /**
     * Obtiene el nombre ingresado en el campo de texto.
     *
     * @return Nombre del cliente como una cadena de texto.
     */
    public String getTxtNombre() {
        return txtNombre.getText();
    }

    /**
     * Obtiene el correo ingresado en el campo de texto.
     *
     * @return Correo del cliente como una cadena de texto.
     */
    public String getTxtCorreo() {
        return txtCorreo.getText();
    }

    /**
     * Obtiene la contraseña ingresada en el campo de texto.
     *
     * @return Contraseña del cliente como una cadena de texto.
     */
    public String getTxtContrasena() {
        return txtContrasena.getText();
    }

    /**
     * Obtiene la dirección ingresada en el campo de texto.
     *
     * @return Dirección del cliente como una cadena de texto.
     */
    public String getTxtDireccion() {
        return txtDireccion.getText();
    }

    /**
     * Obtiene el teléfono ingresado en el campo de texto.
     *
     * @return Teléfono del cliente como una cadena de texto.
     */
    public String getTxtTelefono() {
        return txtTelefono.getText();
    }

    /**
     * Obtiene el tipo de cliente seleccionado en el JComboBox.
     *
     * @return Tipo de cliente como una cadena de texto.
     */
    public String getCbTipoCliente() {
        return (String) cbTipoCliente.getSelectedItem();
    }

    /**
     * Constructor del panel de registro de usuarios.
     *
     * @param evento Manejador de eventos de la aplicación.
     */
    public PanelRegistrarUsuario(Evento evento) {
        setTitle("Registrar Persona");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(5, 10, 5, 10);
        inicializarAtributos();
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 20));

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
        add(botonRegistrar, gbc);
        gbc.gridx = 1;
        add(botonCancelar, gbc);

        setResizable(false);
        setModal(true);
        setSize(400, 300);
        setLocationRelativeTo(null);
    }

    private void asignarAccionBoton(Evento evento) {
        botonRegistrar.addActionListener(evento);
        botonRegistrar.setActionCommand(evento.REGISTRAR_USUARIO);
        botonCancelar.addActionListener(evento);
        botonCancelar.setActionCommand(evento.CANCELAR_REGISTRO_USUARIO);
    }

    /**
     * Inicializa los atributos del panel de registro de usuarios.
     */
    public void inicializarAtributos() {
        labelNombre = new JLabel("Nombre*:");
        labelCorreo = new JLabel("Correo Electrónico*:");
        labelContrasena = new JLabel("Contraseña*: ");
        labelDireccion = new JLabel("Dirección*:");
        labelTelefono = new JLabel("Teléfono*:");
        labelTipoCliente = new JLabel("Tipo de Cliente*:");
        txtNombre = new JTextField(20);
        txtCorreo = new JTextField(20);
        txtContrasena = new JTextField(20);
        txtDireccion = new JTextField(20);
        txtTelefono = new JTextField(20);
        String[] tiposCliente = {"Regular", "Premium"};
        cbTipoCliente = new JComboBox<>(tiposCliente);
        labelTitulo = new JLabel("Registrar Usuario");
        botonRegistrar = new JButton("Registrar");
        botonCancelar = new JButton("Cancelar");
    }

    /**
     * Vacia la información de los JTextFields.
     */
    public void limpiarTxt() {
        txtNombre.setText("");
        txtContrasena.setText("");
        txtCorreo.setText("");
        txtTelefono.setText("");
        txtDireccion.setText("");
    }

    /**
     * Guarda los datos del usuario en un objeto usuario y lo retorna.
     *
     * @return Objeto Usuario con los datos contenidos en los JTextFields.
     */
    public Usuario obtenerDatos() throws RuntimeException {
        Usuario usuario = new Usuario();

        usuario.setNombre(getTxtNombre());
        usuario.setDireccionEnvio(getTxtDireccion());
        usuario.setTelefono(Long.parseLong(getTxtTelefono().matches("^[0-9]+$") ? getTxtTelefono() : "0"));
        usuario.setTipoCliente(getCbTipoCliente());
        usuario.getCuenta().setCorreo(getTxtCorreo());
        usuario.getCuenta().setContrasena(getTxtContrasena());
        return usuario;

    }
}
