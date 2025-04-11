package co.edu.uptc.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa el panel de perfil de un usuario en la interfaz gráfica.
 * Permite visualizar los datos del usuario y gestionar su actualización.
 */
public class PanelPerfil extends JPanel {

    /** Etiqueta para el título de la ventana o panel. */
    private JLabel labelTitulo;

    /** Etiqueta para mostrar el nombre del usuario. */
    private JLabel labelNombre;

    /** Etiqueta para mostrar el correo electrónico del usuario. */
    private JLabel labelCorreo;

    /** Etiqueta para mostrar el número de teléfono del usuario. */
    private JLabel labelTelefono;

    /** Etiqueta para mostrar la dirección de envío del usuario. */
    private JLabel labelDireccionEnvio;

    /** Etiqueta para mostrar el tipo de usuario. */
    private JLabel labelTipoUsuario;

    /** Botón para actualizar los datos del usuario. */
    private JButton botonActualizacionDatos;

    /**
     * Establece el nombre del usuario en la etiqueta correspondiente.
     * @param nombre Nombre del usuario.
     */
    public void setLabelNombre(String nombre) {
        labelNombre.setText(nombre);
    }

    /**
     * Establece el correo electrónico del usuario en la etiqueta correspondiente.
     * @param correo Correo electrónico del usuario.
     */
    public void setLabelCorreo(String correo) {
        labelCorreo.setText(correo);
    }

    /**
     * Establece el número de teléfono del usuario en la etiqueta correspondiente.
     * @param telefono Número de teléfono del usuario.
     */
    public void setLabelTelefono(String telefono) {
        labelTelefono.setText(telefono);
    }

    /**
     * Establece la dirección de envío del usuario en la etiqueta correspondiente.
     * @param direccionEnvio Dirección de envío del usuario.
     */
    public void setLabelDireccionEnvio(String direccionEnvio) {
        labelDireccionEnvio.setText(direccionEnvio);
    }

    /**
     * Establece el tipo de usuario en la etiqueta correspondiente.
     * @param tipoUsuario Tipo de usuario (ejemplo: Cliente, Administrador).
     */
    public void setLabelTipoUsuario(String tipoUsuario) {
        labelTipoUsuario.setText(tipoUsuario);
    }

    /**
     * Constructor del panel de perfil de un usuario.
     * @param evento Manejador de eventos de la aplicación.
     */
    public PanelPerfil(Evento evento) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        inicializarAtributos();
        asignarAccionBoton(evento);

        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 1.0;


        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(10, 30, 10, 10);

        cambiarFont();
        add(labelTitulo, gbc);
        gbc.gridy = 3;
        gbc.weighty = 0;
        add(labelNombre, gbc);
        gbc.gridy = 4;
        add(labelCorreo, gbc);
        gbc.gridy = 5;
        add(labelDireccionEnvio, gbc);
        gbc.gridy = 6;
        add(labelTelefono, gbc);
        gbc.gridy = 7;
        add(labelTipoUsuario, gbc);
        gbc.gridy = 8;
        gbc.weighty = 1.0;
        gbc.gridheight = 4;
        gbc.anchor = GridBagConstraints.SOUTHEAST;
        gbc.fill = GridBagConstraints.NONE;
        add(botonActualizacionDatos, gbc);
    }

    private void asignarAccionBoton(Evento evento) {
        botonActualizacionDatos.addActionListener(evento);
        botonActualizacionDatos.setActionCommand(evento.ACTUALIZAR_DATOS_USUARIO);
    }

    /**
     * Inicializa los atributos del panel de perfil de un usuario.
     */
    public void inicializarAtributos() {
        labelTitulo = new JLabel("Mi Perfil");
        labelNombre = new JLabel("Nombre:");
        labelCorreo = new JLabel("Correo:");
        labelTelefono = new JLabel("Teléfono: ");
        labelDireccionEnvio = new JLabel("Dirección de Envío:");
        labelTipoUsuario = new JLabel("Tipo de Usuario:");
        botonActualizacionDatos = new JButton("Actualizar Datos");
    }

    /**
     * Personaliza el formato de los textos del panel.
     */
    public void cambiarFont() {
        Font fontTitulo = new Font("Arial", Font.BOLD, 40);
        labelTitulo.setFont(fontTitulo);
        Font fontNombre = new Font("Arial", Font.BOLD, 20);
        labelNombre.setFont(fontNombre);
        Font fontAtributos = new Font("Arial", Font.PLAIN, 15);
        labelTelefono.setFont(fontAtributos);
        labelCorreo.setFont(fontAtributos);
        labelDireccionEnvio.setFont(fontAtributos);
        labelTipoUsuario.setFont(fontAtributos);
    }
}
