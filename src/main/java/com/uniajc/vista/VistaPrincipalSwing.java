package com.uniajc.vista;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

// Primer paso: Crear la clase VistaPrincipalSwing que extienda JFrame e implemente ActionListener
public class VistaPrincipalSwing extends JFrame implements ActionListener {

    // 2. Definir constantes para los nombres de los paneles/ventanas
    private static final String PANEL_ESTUDIANTE = "PANEL_ESTUDIANTE";
    private static final String PANEL_DOCENTE = "PANEL_DOCENTE";
    private static final String PANEL_MATERIA = "PANEL_MATERIA";
    private static final String PANEL_GRUPO = "PANEL_GRUPO";
    private static final String PANEL_INSCRIPCION = "PANEL_INSCRIPCION";
    private static final String PANEL_SETTINGS = "PANEL_SETTINGS";

    // 3. Definir variables para el CardLayout, el panel contenedor y los elementos del menú
    private CardLayout cardLayout;
    private JPanel panelContenedor;

    // Elementos del menú
    private JMenuItem menuItemEstudiante;
    private JMenuItem menuItemDocente;
    private JMenuItem menuItemMateria;
    private JMenuItem menuItemGrupo;
    private JMenuItem menuItemInscripcion;
    private JMenuItem menuItemSalir;
    private JMenuItem menuItemSettings;

    private VistaEstudianteSwing vistaEstudianteSwing;
    private VistaDocenteSwing vistaDocenteSwing;

    public VistaPrincipalSwing() {
        configurarLookAndFeel();
        initComponents();
    }

    private void configurarLookAndFeel() {
        // Configurar un tema moderno y colores personalizados
        Color colorPrimario = new Color(41, 128, 185);
        Color colorSecundario = new Color(236, 240, 241);
        Color colorAcento = new Color(231, 76, 60);

        // Configurar la fuente global para toda la aplicación
        UIManager.put("Label.font", new FontUIResource("Segoe UI", Font.PLAIN, 12));
        UIManager.put("Button.font", new FontUIResource("Segoe UI", Font.BOLD, 12));
        UIManager.put("TextField.font", new FontUIResource("Segoe UI", Font.PLAIN, 12));
        UIManager.put("TextArea.font", new FontUIResource("Segoe UI", Font.PLAIN, 12));
        UIManager.put("Table.font", new FontUIResource("Segoe UI", Font.PLAIN, 11));
        UIManager.put("Table.headingFont", new FontUIResource("Segoe UI", Font.BOLD, 12));
        UIManager.put("Menu.font", new FontUIResource("Segoe UI", Font.PLAIN, 12));
        UIManager.put("MenuItem.font", new FontUIResource("Segoe UI", Font.PLAIN, 12));

        // Configurar colores para los componentes
        UIManager.put("Button.background", colorPrimario);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.select", colorAcento);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", Color.BLACK);
    }

    private void initComponents() {
        // Configurar la ventana principal
        setTitle("Sistema de Gestión Académica - UNIAJC");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Configurar el CardLayout y el panel contenedor
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        panelContenedor.setBackground(new Color(236, 240, 241));

        // Crear instancias de los paneles/ventanas
        vistaEstudianteSwing = new VistaEstudianteSwing();
        vistaDocenteSwing = new VistaDocenteSwing();

        JPanel panelBienvenida = crearPanelBienvenida();
        JPanel panelVacio = crearPanelVacio("Seleccione una opción del menú");

        panelContenedor.add(panelBienvenida, "BIENVENIDA");
        panelContenedor.add(vistaEstudianteSwing, PANEL_ESTUDIANTE);
        panelContenedor.add(vistaDocenteSwing, PANEL_DOCENTE);
        panelContenedor.add(panelVacio, PANEL_MATERIA);
        panelContenedor.add(panelVacio, PANEL_GRUPO);
        panelContenedor.add(panelVacio, PANEL_INSCRIPCION);
        panelContenedor.add(panelVacio, PANEL_SETTINGS);
        configurarMenu();

        add(panelContenedor);
        cardLayout.show(panelContenedor, "BIENVENIDA");
    }

    private JPanel crearPanelBienvenida() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(236, 240, 241));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel titulo = new JLabel("Sistema de Gestión Académica");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titulo.setForeground(new Color(41, 128, 185));
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titulo, gbc);

        gbc.gridy++;
        panel.add(new JLabel(" "), gbc);

        gbc.gridy++;
        JLabel subtitulo = new JLabel("UNIAJC - Universidad Antonio José Camacho");
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitulo.setForeground(new Color(127, 140, 141));
        subtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(subtitulo, gbc);

        gbc.gridy++;
        panel.add(new JLabel(" "), gbc);

        gbc.gridy++;
        JLabel version = new JLabel("Versión 1.0 - Práctica MVC");
        version.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        version.setForeground(new Color(149, 165, 166));
        version.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(version, gbc);

        return panel;
    }

    private JPanel crearPanelVacio(String mensaje) {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(236, 240, 241));

        JLabel label = new JLabel(mensaje);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        label.setForeground(new Color(149, 165, 166));
        panel.add(label);

        return panel;
    }

    private void configurarMenu() {
        // Configurar el menú con colores personalizados y fuentes modernas
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(41, 128, 185));

        Color colorMenu = new Color(44, 62, 80);
        Color colorMenuItem = new Color(52, 73, 94);

        // Configurar cada menú y sus elementos con estilos personalizados
        JMenu menuArchivo = new JMenu("Archivo");
        menuArchivo.setForeground(Color.WHITE);
        menuArchivo.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Configurar elementos del menú Archivo
        menuItemSalir = new JMenuItem("Salir");
        menuItemSalir.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        menuItemSalir.setBackground(colorMenuItem);
        menuItemSalir.setForeground(Color.WHITE);
        menuItemSalir.addActionListener(this);
        menuArchivo.add(menuItemSalir);

        // Agregar un nuevo elemento de menú para Settings
        menuItemSettings = new JMenuItem("Settings");
        menuItemSettings.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        menuItemSettings.setBackground(colorMenuItem);
        menuItemSettings.setForeground(Color.WHITE);
        menuItemSettings.addActionListener(this);
        menuArchivo.add(menuItemSettings);

        // Configurar los demás menús y sus elementos de manera similar
        JMenu menuEstudiante = new JMenu("Estudiante");
        menuEstudiante.setForeground(Color.WHITE);
        menuEstudiante.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Configurar elementos del menú Estudiante
        menuItemEstudiante = new JMenuItem("Gestión de Estudiantes");
        menuItemEstudiante.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        menuItemEstudiante.setBackground(colorMenuItem);
        menuItemEstudiante.setForeground(Color.WHITE);
        menuItemEstudiante.addActionListener(this);
        menuEstudiante.add(menuItemEstudiante);

        // Configurar el menú Docente y sus elementos
        JMenu menuDocente = new JMenu("Docente");
        menuDocente.setForeground(Color.WHITE);
        menuDocente.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Configurar elementos del menú Docente
        menuItemDocente = new JMenuItem("Gestión de Docentes");
        menuItemDocente.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        menuItemDocente.setBackground(colorMenuItem);
        menuItemDocente.setForeground(Color.WHITE);
        menuItemDocente.addActionListener(this);
        menuDocente.add(menuItemDocente);

        // Configurar el menú Materia y sus elementos
        JMenu menuMateria = new JMenu("Materia");
        menuMateria.setForeground(Color.WHITE);
        menuMateria.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Configurar elementos del menú Materia
        menuItemMateria = new JMenuItem("Gestión de Materias");
        menuItemMateria.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        menuItemMateria.setBackground(colorMenuItem);
        menuItemMateria.setForeground(Color.WHITE);
        menuItemMateria.addActionListener(this);
        menuMateria.add(menuItemMateria);

        // Configurar el menú Grupo y sus elementos
        JMenu menuGrupo = new JMenu("Grupo");
        menuGrupo.setForeground(Color.WHITE);
        menuGrupo.setFont(new Font("Segoe UI", Font.BOLD, 12));

        // Configurar elementos del menú Grupo
        menuItemGrupo = new JMenuItem("Gestión de Grupos");
        menuItemGrupo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        menuItemGrupo.setBackground(colorMenuItem);
        menuItemGrupo.setForeground(Color.WHITE);
        menuItemGrupo.addActionListener(this);
        menuGrupo.add(menuItemGrupo);

        JMenu menuInscripcion = new JMenu("Inscripción");
        menuInscripcion.setForeground(Color.WHITE);
        menuInscripcion.setFont(new Font("Segoe UI", Font.BOLD, 12));

        menuItemInscripcion = new JMenuItem("Gestión de Inscripciones");
        menuItemInscripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        menuItemInscripcion.setBackground(colorMenuItem);
        menuItemInscripcion.setForeground(Color.WHITE);
        menuItemInscripcion.addActionListener(this);
        menuInscripcion.add(menuItemInscripcion);

        // Agregar los menús a la barra de menú
        menuBar.add(menuArchivo);
        menuBar.add(menuEstudiante);
        menuBar.add(menuDocente);
        menuBar.add(menuMateria);
        menuBar.add(menuGrupo);
        menuBar.add(menuInscripcion);

        // Configurar la barra de menú en la ventana principal
        setJMenuBar(menuBar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object fuente = e.getSource();

        System.out.println("Acción realizada: " + ((JMenuItem) fuente).getText());

        // Manejar las acciones de los elementos del menú para mostrar el panel correspondiente
        if (fuente == menuItemSalir) {
            System.exit(0);
        } else if (fuente == menuItemSettings) {
            cardLayout.show(panelContenedor, PANEL_SETTINGS);
        } else if (fuente == menuItemEstudiante) {
            cardLayout.show(panelContenedor, PANEL_ESTUDIANTE);
        } else if (fuente == menuItemDocente) {
            cardLayout.show(panelContenedor, PANEL_DOCENTE);
        } else if (fuente == menuItemMateria) {
            cardLayout.show(panelContenedor, PANEL_MATERIA);
        } else if (fuente == menuItemGrupo) {
            cardLayout.show(panelContenedor, PANEL_GRUPO);
        } else if (fuente == menuItemInscripcion) {
            cardLayout.show(panelContenedor, PANEL_INSCRIPCION);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new VistaPrincipalSwing().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}