package co.edu.uptc.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Clase que representa el panel de Inicio de Sesión en la interfaz gráfica.
 */
public class PanelInicioSesion extends JPanel {

    /**
     * Campo de texto para ingresar el correo del usuario.
     */
    private JTextField txtCorreo;

    /**
     * Campo de texto para ingresar la contraseña del usuario.
     */
    private JPasswordField txtContrasena;//TODO En lugar de poner JTextFiel cambiarlo por un JPasswordField

    /**
     * Etiqueta del título del panel.
     */
    private JLabel labelTitulo;

    /**
     * Etiqueta para el mensaje de inicio de sesión.
     */
    private JLabel labelIniciarSesion;

    /**
     * Etiqueta para mostrar un texto informativo.
     */
    private JLabel labelTexto;

    /**
     * Etiqueta para indicar el campo de correo.
     */
    private JLabel labelCorreo;

    /**
     * Etiqueta para indicar el campo de contraseña.
     */
    private JLabel labelContrasena;

    /**
     * Botón para continuar con el proceso de inicio de sesión.
     */
    private JButton botonContinuar;

    /**
     * Botón para cancelar la acción y regresar a la pantalla anterior.
     */
    private JButton botonCancelar;

    /**
     * Botón para crear una nueva cuenta de usuario.
     */
    private JButton botonCrearCuenta;

    /**
     * Imagen de fondo del panel de inicio de sesión.
     */
    private Image imagenFondo;

    /**
     * Obtiene el campo de texto donde se ingresa la contraseña.
     *
     * @return Campo de texto de la contraseña.
     */
    public JTextField getTxtContrasena() {
        return txtContrasena;
    }

    /**
     * Obtiene el campo de texto donde se ingresa el correo.
     *
     * @return Campo de texto del correo.
     */
    public JTextField getTxtCorreo() {
        return txtCorreo;
    }

    /**
     * Establece el campo de texto del correo.
     *
     * @param txtCorreo Nuevo campo de texto para el correo.
     */
    public void setTxtCorreo(String txtCorreo) {
        this.txtCorreo.setText(txtCorreo);
    }

    /**
     * Establece el campo de texto de la contraseña.
     *
     * @param txtContrasena Nuevo campo de texto para la contraseña.
     */
    public void setTxtContrasena(String txtContrasena) {
        this.txtContrasena.setText(txtContrasena);
    }


    /**
     * Constructor del panel de inicio de sesión.
     *
     * @param evento Manejador de eventos de la aplicación.
     */
    public PanelInicioSesion(Evento evento) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 1, 60, 1);
        setBackground(Color.GRAY);

        initAtributos();
        asignarAccionBoton(evento);
        personalizarBotones();

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
        mouseListenersBtn();
    }


    /**
     * Personaliza la apariencia de los botones y etiquetas del panel.
     */
    public void personalizarBotones() {

        labelTitulo.setFont(new Font("Montserrat", Font.BOLD, 28));
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        labelCorreo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelCorreo.setForeground(Color.WHITE);
        labelContrasena.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelContrasena.setForeground(Color.WHITE);

        labelIniciarSesion.setFont(new Font("Montserrat", Font.BOLD, 21));
        labelIniciarSesion.setForeground(new Color(255, 224, 130));
        labelIniciarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);

        txtCorreo.setMaximumSize(new Dimension(220, 30));
        txtCorreo.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        txtContrasena.setMaximumSize(new Dimension(220, 30));
        txtContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        botonContinuar.setBackground(new Color(33, 150, 243));
        botonContinuar.setForeground(Color.WHITE);
        botonContinuar.setFocusPainted(false);
        botonContinuar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        botonContinuar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonContinuar.setMaximumSize(new Dimension(220, 35));

        botonCancelar.setBackground(new Color(189, 189, 189));
        botonCancelar.setForeground(Color.BLACK);
        botonCancelar.setFocusPainted(false);
        botonCancelar.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        botonCancelar.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonCancelar.setMaximumSize(new Dimension(220, 35));

        labelTexto.setForeground(Color.WHITE);
        labelTexto.setAlignmentX(Component.CENTER_ALIGNMENT);

        botonCrearCuenta.setBackground(new Color(33, 150, 243));
        botonCrearCuenta.setForeground(Color.WHITE);
        botonCrearCuenta.setFocusPainted(false);
        botonCrearCuenta.setFont(new Font("Segoe UI", Font.BOLD, 15));
        botonCrearCuenta.setAlignmentX(Component.CENTER_ALIGNMENT);
        botonCrearCuenta.setMaximumSize(new Dimension(220, 35));

    }

    private void initAtributos() {
        botonContinuar = new JButton("Continuar");
        botonCancelar = new JButton("Salir");
        botonCrearCuenta = new JButton("Crear Cuenta");
        labelTitulo = new JLabel("Librería Virtual", SwingConstants.CENTER);
        labelIniciarSesion = new JLabel("Iniciar Sesión", SwingConstants.CENTER);
        labelTexto = new JLabel("o crea una cuenta");
        labelContrasena = new JLabel("Contraseña: ");
        labelCorreo = new JLabel("Correo: ");
        txtCorreo = new JTextField(25);
        txtCorreo.putClientProperty("JTextField.placeholderText", "Correo Electrónico");
        txtContrasena = new JPasswordField(25);
        txtContrasena.putClientProperty("JTextField.placeholderText", "Contraseña");
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
    }

    /**
     * Asigna los listeners de mouse a los botones del panel.
     */
    private void mouseListenersBtn() {
        botonContinuar.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent event) {
                botonContinuar.setBackground(new Color(25, 118, 210));
            }

            public void mouseExited(MouseEvent event) {
                botonContinuar.setBackground(new Color(33, 150, 243));
            }
        });

        botonCrearCuenta.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent event) {
                botonCrearCuenta.setBackground(new Color(25, 118, 210));
            }

            public void mouseExited(MouseEvent event) {
                botonCrearCuenta.setBackground(new Color(33, 150, 243));
            }
        });
    }
}
