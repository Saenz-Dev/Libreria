package co.edu.uptc.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Clase que representa el panel de Inicio de Sesión en la interfaz gráfica.
 */
public class PanelInicioSesion extends JPanel {

    /** Campo de texto para ingresar el correo del usuario. */
    private JTextField txtCorreo;

    /** Campo de texto para ingresar la contraseña del usuario. */
    private JTextField txtContrasena;//TODO En lugar de poner JTextFiel cambiarlo por un JPasswordField

    /** Etiqueta del título del panel. */
    private JLabel labelTitulo;

    /** Etiqueta para el mensaje de inicio de sesión. */
    private JLabel labelIniciarSesion;

    /** Etiqueta para mostrar un texto informativo. */
    private JLabel labelTexto;

    /** Etiqueta para indicar el campo de correo. */
    private JLabel labelCorreo;

    /** Etiqueta para indicar el campo de contraseña. */
    private JLabel labelContrasena;

    /** Botón para continuar con el proceso de inicio de sesión. */
    private JButton botonContinuar;

    /** Botón para cancelar la acción y regresar a la pantalla anterior. */
    private JButton botonCancelar;

    /** Botón para crear una nueva cuenta de usuario. */
    private JButton botonCrearCuenta;

    /**
     * Obtiene el campo de texto donde se ingresa la contraseña.
     * @return Campo de texto de la contraseña.
     */
    public JTextField getTxtContrasena() {
        return txtContrasena;
    }

    /**
     * Obtiene el campo de texto donde se ingresa el correo.
     * @return Campo de texto del correo.
     */
    public JTextField getTxtCorreo() {
        return txtCorreo;
    }

    /**
     * Establece el campo de texto del correo.
     * @param txtCorreo Nuevo campo de texto para el correo.
     */
    public void setTxtCorreo(JTextField txtCorreo) {
        this.txtCorreo = txtCorreo;
    }

    /**
     * Establece el campo de texto de la contraseña.
     * @param txtContrasena Nuevo campo de texto para la contraseña.
     */
    public void setTxtContrasena(JTextField txtContrasena) {
        this.txtContrasena = txtContrasena;
    }

    /**
     * Constructor del panel de inicio de sesión.
     * @param evento Manejador de eventos de la aplicación.
     */
    public PanelInicioSesion(Evento evento) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 1, 60, 1);

        initAtributos();
        personalizarPanel();
        asignarAccionBoton(evento);

        add(labelTitulo, gbc);
        gbc.gridy = 1;
        gbc.insets.bottom = 5;
        add(labelIniciarSesion, gbc);
        gbc.gridy = 2;
        add(labelCorreo, gbc);
        gbc.gridy = 3;
        add(txtCorreo, gbc);
        gbc.gridy = 4;
        add(labelContrasena, gbc);
        gbc.gridy = 5;
        add(txtContrasena, gbc);
        gbc.gridy = 6;
        add(botonContinuar, gbc);
        gbc.gridy = 7;
        add(botonCancelar, gbc);
        gbc.gridy = 8;
        gbc.fill = GridBagConstraints.NONE;
        add(labelTexto, gbc);
        gbc.gridy = 9;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(botonCrearCuenta, gbc);
    }

    private void asignarAccionBoton(Evento evento) {
        botonContinuar.addActionListener(evento);
        botonContinuar.setActionCommand(evento.CONTINUAR_INICIAR_SESION);
        botonCancelar.addActionListener(evento);
        botonCancelar.setActionCommand(evento.SALIR);
        botonCrearCuenta.addActionListener(evento);
        botonCrearCuenta.setActionCommand(evento.VENTANA_REGISTRAR_USUARIO);
    }

    private void personalizarPanel() {
        Dimension dimensiontxt = new Dimension(500, 25);
        Font letra = new Font("Arial", Font.BOLD, 20);
        txtCorreo.setPreferredSize(dimensiontxt);
        txtContrasena.setPreferredSize(dimensiontxt);
        txtContrasena.selectAll();
        txtCorreo.selectAll();
        labelTitulo.setFont(letra);
        labelIniciarSesion.setFont(new Font("Arial", Font.BOLD, 15));
    }

    private void initAtributos() {
        botonContinuar = new JButton("Continuar");
        botonCancelar  = new JButton("Salir");
        botonCrearCuenta = new JButton("Crear Cuenta");
        labelTitulo = new JLabel("Librería Virtual", SwingConstants.CENTER);
        labelIniciarSesion = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        labelTexto = new JLabel("o crea una cuenta");
        labelContrasena = new JLabel("Contraseña: ");
        labelCorreo = new JLabel("Correo: ");
        txtCorreo = new JTextField( 25);
        txtContrasena = new JTextField(25);
    }
}
