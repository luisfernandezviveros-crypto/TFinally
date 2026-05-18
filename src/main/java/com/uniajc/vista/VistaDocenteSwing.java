package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VistaDocenteSwing extends JPanel {

    public VistaDocenteSwing() {
        initComponents();
    }

    public void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(236, 240, 241));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelTitulo.setBackground(new Color(236, 240, 241));
        
        JLabel titulo = new JLabel("Gestión de Docentes");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titulo.setForeground(new Color(41, 128, 185));
        panelTitulo.add(titulo);

        JPanel panelVacio = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelVacio.setBackground(new Color(236, 240, 241));
        
        JLabel mensaje = new JLabel("Módulo en construcción...");
        mensaje.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        mensaje.setForeground(new Color(149, 165, 166));
        panelVacio.add(mensaje);

        add(panelTitulo, BorderLayout.NORTH);
        add(panelVacio, BorderLayout.CENTER);
    }
}
