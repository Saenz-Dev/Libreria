package co.edu.uptc.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import co.edu.uptc.modelo.TipoResultadoEnum;

public class PanelPremium extends JDialog {

    private JLabel labelTitulo;
    private JLabel labelSubtitulo;
    private JTextField txtFieldCodigo;
    private JButton botonValidar;
    private JLabel labelProgreso;

    public String obtenerCodigo() {
	return txtFieldCodigo.getText();
    }

    private final Color BACKGROUND_COLOR = new Color(102, 126, 234);
    private final Color GOLD_COLOR = new Color(255, 215, 0);
    private final Color SUCCESS_COLOR = new Color(76, 175, 80);
    private final Color ERROR_COLOR = new Color(244, 67, 54);
    private final Color TEXT_WHITE = new Color(255, 255, 255);
    private final Color TEXT_DARK = new Color(51, 51, 51);

    public PanelPremium(Evento evento) {
	initJDialog();
	crearComponentes();
	confiPanel();
	funcionBoton(evento);
	/*
	 * confiLayout(); confiListeners();
	 */

    }

    private void initJDialog() {
	setTitle("Validador de Código Premium");
	setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	setSize(500, 550);
	setLocationRelativeTo(null);
	setResizable(false);
	setModal(true);
	setVisible(false);
    }

    private void crearComponentes() {
	labelTitulo = new JLabel("ACCESO PREMIUM", SwingConstants.CENTER);
	labelTitulo.setFont(new Font("Arial", Font.BOLD, 28));
	labelTitulo.setForeground(TEXT_WHITE);

	labelSubtitulo = new JLabel(
		"<html><center>Ingresa tu código para desbloquear<br>funciones exclusivas</center></html>",
		SwingConstants.CENTER);
	labelSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
	labelSubtitulo.setForeground(new Color(224, 224, 224));

	txtFieldCodigo = new JTextField("Ingresa tu código premium");
	txtFieldCodigo.setFont(new Font("Arial", Font.BOLD, 16));
	txtFieldCodigo.setPreferredSize(new Dimension(300, 50));
	txtFieldCodigo.setHorizontalAlignment(SwingConstants.CENTER);

	botonValidar = new JButton("VALIDAR CÓDIGO");
	botonValidar.setFont(new Font("Arial", Font.BOLD, 16));
	botonValidar.setPreferredSize(new Dimension(300, 50));

	labelProgreso = new JLabel("", SwingConstants.CENTER);
	labelProgreso.setFont(new Font("Arial", Font.BOLD, 16));
	labelProgreso.setPreferredSize(new Dimension(350, 80));
	labelProgreso.setVisible(false);
    }

    private void confiPanel() {
	getContentPane().setLayout(new BorderLayout());
	getContentPane().setBackground(BACKGROUND_COLOR);

	JPanel mainPanel = new JPanel();
	mainPanel.add(Box.createVerticalStrut(40));
	mainPanel.setOpaque(false);
	mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
	

	labelTitulo.setForeground(Color.WHITE);
	labelSubtitulo.setForeground(Color.WHITE);
	labelProgreso.setForeground(Color.WHITE);

	txtFieldCodigo.setForeground(Color.WHITE);
	txtFieldCodigo.setBackground(BACKGROUND_COLOR);

	botonValidar.setForeground(Color.WHITE);
	botonValidar.setBackground(GOLD_COLOR);

	JPanel textPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	textPanel.setOpaque(false);
	textPanel.add(labelTitulo);
	textPanel.add(labelSubtitulo);
	mainPanel.add(textPanel);

	JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	inputPanel.setOpaque(false);
	inputPanel.add(txtFieldCodigo);
	mainPanel.add(inputPanel);

	JPanel botonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	botonPanel.setOpaque(false);
	botonPanel.add(botonValidar);
	mainPanel.add(botonPanel);

	mainPanel.add(Box.createVerticalStrut(10));

	JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	statusPanel.setOpaque(false);
	statusPanel.add(labelProgreso);
	mainPanel.add(statusPanel);

	getContentPane().add(mainPanel, BorderLayout.CENTER);
    }

    private void funcionBoton(Evento evento) {
	botonValidar.addActionListener(evento);
	botonValidar.setActionCommand(Evento.VALIDAR_PREMIUM);
    }

    public void mostrarResultado(String mensaje, TipoResultadoEnum esPremium) {
	labelProgreso.setText(mensaje);
	labelProgreso.setVisible(true);

	switch (esPremium) {
	case EXITO:
	    labelProgreso.setOpaque(true);
	    labelProgreso.setBackground(SUCCESS_COLOR);
	    labelProgreso.setForeground(TEXT_WHITE);
	    break;
	case ERROR:
	    labelProgreso.setOpaque(true);
	    labelProgreso.setBackground(ERROR_COLOR);
	    labelProgreso.setForeground(TEXT_WHITE);
	    break;
	}
	revalidate();
	repaint();
    }

    public void mostrarEstadoPremium() {
	labelProgreso.setText("✨ USUARIO PREMIUM ACTIVADO ✨");
	labelProgreso.setOpaque(true);
	labelProgreso.setBackground(GOLD_COLOR);
	labelProgreso.setForeground(TEXT_DARK);
	labelProgreso.setVisible(true);

	revalidate();
	repaint();
    }

    private void ocultarResultado() {
	labelProgreso.setVisible(false);
	revalidate();
	repaint();
    }

}
