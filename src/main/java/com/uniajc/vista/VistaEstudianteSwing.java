package com.uniajc.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import com.uniajc.dao.EstudianteDao;
import com.uniajc.modelo.Estudiante;
import com.uniajc.servicios.EstudianteService;

public class VistaEstudianteSwing extends JPanel implements ActionListener {

    // Definir colores y estilos para una apariencia más moderna
    private static final Color COLOR_PRIMARIO = new Color(41, 128, 185);
    private static final Color COLOR_SECUNDARIO = new Color(236, 240, 241);
    private static final Color COLOR_ACENTO = new Color(231, 76, 60);
    private static final Color COLOR_EXITO = new Color(39, 174, 96);

    // Definir las columnas de la tabla de estudiantes
    private static final String[] COLUMNAS = {"ID", "Nombre", "Apellido", "Email"};

    // Servicios y DAO para manejar la lógica de negocio y acceso a datos
    private final EstudianteService estudianteService;
    private final EstudianteDao estudianteDao;

    // Elementos o Componentes de la interfaz
    private JTable tablaEstudiantes;
    private DefaultTableModel modeloTabla; // Modelo para la tabla de estudiantes
    private JTextField textoBusqueda;
    private JButton botonBuscar;
    private JButton botonLimpiar;
    private JButton botonRegistrar;
    private JButton botonEditar;
    private JButton botonEliminar;
    // private JButton botonActualizar;

    // Elementos para el diálogo de registro/edición de estudiantes
    private JDialog dialogoFormulario;
    private JTextField campoNombre;
    private JTextField campoApellido;
    private JTextField campoEmail;
    private JButton botonGuardar;
    private JButton botonCancelar;
    private boolean modoEdicion;
    private Estudiante estudianteSeleccionado;

    public VistaEstudianteSwing() {
        this.estudianteDao = new EstudianteDao();
        this.estudianteService = new EstudianteService(estudianteDao);
        initComponents();
        cargarEstudiantes();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_SECUNDARIO);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        add(crearPanelBusqueda(), BorderLayout.NORTH);
        add(crearPanelTabla(), BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }

    private JPanel crearPanelBusqueda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(COLOR_SECUNDARIO);

        JLabel labelBusqueda = new JLabel("Buscar:");
        labelBusqueda.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelBusqueda.setForeground(COLOR_PRIMARIO);

        textoBusqueda = new JTextField(25);
        textoBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textoBusqueda.setPreferredSize(new Dimension(200, 30));
        textoBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    buscarEstudiantes();
                }
            }
        });

        botonBuscar = crearBoton("Buscar", COLOR_PRIMARIO);
        botonBuscar.addActionListener(e -> buscarEstudiantes());

        botonLimpiar = crearBoton("Limpiar", new Color(149, 165, 166));
        botonLimpiar.addActionListener(e -> {
            textoBusqueda.setText("");
            cargarEstudiantes();
        });

        panel.add(labelBusqueda);
        panel.add(textoBusqueda);
        panel.add(botonBuscar);
        panel.add(botonLimpiar);

        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_SECUNDARIO);

        modeloTabla = new DefaultTableModel(COLUMNAS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Configurar la tabla de estudiantes con un diseño más moderno
        tablaEstudiantes = new JTable(modeloTabla);
        tablaEstudiantes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaEstudiantes.setRowHeight(25);
        tablaEstudiantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEstudiantes.setGridColor(new Color(189, 195, 199));
        tablaEstudiantes.setBackground(Color.WHITE);
        tablaEstudiantes.setSelectionBackground(COLOR_PRIMARIO);
        tablaEstudiantes.setSelectionForeground(Color.WHITE);

        // Configurar el encabezado de la tabla para que tenga un estilo más atractivo
        tablaEstudiantes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaEstudiantes.getTableHeader().setBackground(COLOR_PRIMARIO);
        tablaEstudiantes.getTableHeader().setForeground(Color.WHITE);
        tablaEstudiantes.getTableHeader().setReorderingAllowed(false);

        // Configurar el renderizado de las celdas para mejorar la apariencia y legibilidad
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return c;
            }
        };

        // Centrar el texto en las celdas de la tabla
        renderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Aplicar el renderizador a todas las columnas de la tabla
        for (int i = 0; i < COLUMNAS.length; i++) {
            tablaEstudiantes.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        // Configurar el ancho de las columnas para una mejor distribución del espacio
        tablaEstudiantes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaEstudiantes.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaEstudiantes.getColumnModel().getColumn(2).setPreferredWidth(150);
        tablaEstudiantes.getColumnModel().getColumn(3).setPreferredWidth(200);

        // Configurar el panel de desplazamiento para la tabla con un borde personalizado
        JScrollPane scrollPane = new JScrollPane(tablaEstudiantes);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 2));
        scrollPane.setPreferredSize(new Dimension(600, 400));

        // Agregar el panel de desplazamiento al panel principal de la tabla
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(COLOR_SECUNDARIO);

        botonRegistrar = crearBoton("Registrar", COLOR_EXITO);
        botonRegistrar.setPreferredSize(new Dimension(130, 40));
        botonRegistrar.addActionListener(e -> mostrarDialogoRegistro(false));

        botonEditar = crearBoton("Editar", COLOR_PRIMARIO);
        botonEditar.setPreferredSize(new Dimension(130, 40));
        botonEditar.addActionListener(e -> {
            if (tablaEstudiantes.getSelectedRow() >= 0) {
                mostrarDialogoRegistro(true);
            } else {
                mostrarMensaje("Seleccione un estudiante de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        botonEliminar = crearBoton("Eliminar", COLOR_ACENTO);
        botonEliminar.setPreferredSize(new Dimension(130, 40));
        botonEliminar.addActionListener(e -> eliminarEstudiante());

        // botonActualizar = crearBoton("Actualizar", new Color(149, 165, 166));
        // botonActualizar.setPreferredSize(new Dimension(130, 40));
        // botonActualizar.addActionListener(e -> cargarEstudiantes());

        panel.add(botonRegistrar);
        panel.add(botonEditar);
        panel.add(botonEliminar);
        //panel.add(botonActualizar);

        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setOpaque(true);
        boton.setPreferredSize(new Dimension(110, 35));
        boton.addActionListener(this);
        return boton;
    }

    private void cargarEstudiantes() {
        modeloTabla.setRowCount(0);
        try {
            List<Estudiante> estudiantes = estudianteService.obtenerTodosLosEstudiantes();
            for (Estudiante e : estudiantes) {
                modeloTabla.addRow(new Object[]{e.getId(), e.getNombre(), e.getApellido(), e.getEmail()});
            }
        } catch (Exception ex) {
            mostrarMensaje("Error al cargar estudiantes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarEstudiantes() {
        modeloTabla.setRowCount(0);
        String busqueda = textoBusqueda.getText().trim().toLowerCase();
        
        if (busqueda.isEmpty()) {
            cargarEstudiantes();
            return;
        }
        
        try {
            List<Estudiante> estudiantes = estudianteService.obtenerTodosLosEstudiantes();
            for (Estudiante e : estudiantes) {
                boolean coincide = String.valueOf(e.getId()).contains(busqueda) ||
                                e.getNombre().toLowerCase().contains(busqueda) ||
                                e.getApellido().toLowerCase().contains(busqueda) ||
                                e.getEmail().toLowerCase().contains(busqueda);
                if (coincide) {
                    modeloTabla.addRow(new Object[]{e.getId(), e.getNombre(), e.getApellido(), e.getEmail()});
                }
            }
        } catch (Exception ex) {
            mostrarMensaje("Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogoRegistro(boolean esEdicion) {
        this.modoEdicion = esEdicion;
        
        if (esEdicion && tablaEstudiantes.getSelectedRow() >= 0) {
            int id = (int) modeloTabla.getValueAt(tablaEstudiantes.getSelectedRow(), 0);
            try {
                estudianteSeleccionado = estudianteService.buscarEstudiantePorId(id).orElse(null);
            } catch (Exception ex) {
                mostrarMensaje("Error al cargar estudiante: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            estudianteSeleccionado = new Estudiante();
        }

        dialogoFormulario = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
            esEdicion ? "Editar Estudiante" : "Registrar Estudiante", true);
        dialogoFormulario.setSize(450, 350);
        dialogoFormulario.setLocationRelativeTo(this);
        dialogoFormulario.setResizable(false);

        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel labelTitulo = new JLabel(esEdicion ? "✏ Editar Estudiante" : "➕ Registrar Estudiante");
        labelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        labelTitulo.setForeground(COLOR_PRIMARIO);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelFormulario.add(labelTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        JLabel labelNombre = new JLabel("Nombre:");
        labelNombre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelNombre.setForeground(COLOR_PRIMARIO);
        gbc.gridx = 0;
        panelFormulario.add(labelNombre, gbc);

        campoNombre = new JTextField(20);
        campoNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campoNombre.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        panelFormulario.add(campoNombre, gbc);

        gbc.gridy++;

        JLabel labelApellido = new JLabel("Apellido:");
        labelApellido.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelApellido.setForeground(COLOR_PRIMARIO);
        gbc.gridx = 0;
        panelFormulario.add(labelApellido, gbc);

        campoApellido = new JTextField(20);
        campoApellido.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campoApellido.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        panelFormulario.add(campoApellido, gbc);

        gbc.gridy++;

        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelEmail.setForeground(COLOR_PRIMARIO);
        gbc.gridx = 0;
        panelFormulario.add(labelEmail, gbc);

        campoEmail = new JTextField(20);
        campoEmail.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campoEmail.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        panelFormulario.add(campoEmail, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(Color.WHITE);

        botonGuardar = crearBoton("Guardar", COLOR_EXITO);
        botonGuardar.setPreferredSize(new Dimension(120, 38));
        botonGuardar.addActionListener(e -> guardarEstudiante());
        botonGuardar.setPreferredSize(new Dimension(110, 35));

        botonCancelar = crearBoton("Cancelar", new Color(149, 165, 166));
        botonCancelar.addActionListener(e -> dialogoFormulario.dispose());
        botonCancelar.setPreferredSize(new Dimension(110, 35));

        panelBotones.add(botonGuardar);
        panelBotones.add(botonCancelar);
        panelFormulario.add(panelBotones, gbc);

        if (esEdicion && estudianteSeleccionado != null) {
            campoNombre.setText(estudianteSeleccionado.getNombre());
            campoApellido.setText(estudianteSeleccionado.getApellido());
            campoEmail.setText(estudianteSeleccionado.getEmail());
        }

        dialogoFormulario.add(panelFormulario);
        dialogoFormulario.setVisible(true);
    }

    private void guardarEstudiante() {
        try {
            String nombre = campoNombre.getText().trim();
            String apellido = campoApellido.getText().trim();
            String email = campoEmail.getText().trim();

            if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty()) {
                mostrarMensaje("Todos los campos son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            estudianteSeleccionado.setNombre(nombre);
            estudianteSeleccionado.setApellido(apellido);
            estudianteSeleccionado.setEmail(email);

            if (modoEdicion) {
                estudianteService.actualizarEstudiante(estudianteSeleccionado);
                mostrarMensaje("Estudiante actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                estudianteService.registrarEstudiante(estudianteSeleccionado);
                mostrarMensaje("Estudiante registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }

            dialogoFormulario.dispose();
            cargarEstudiantes();
        } catch (IllegalArgumentException ex) {
            mostrarMensaje(ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            mostrarMensaje("Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarEstudiante() {
        if (tablaEstudiantes.getSelectedRow() < 0) {
            mostrarMensaje("Seleccione un estudiante de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(this,
            "¿Está seguro de eliminar el estudiante seleccionado?",
            "Confirmar Eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            try {
                int id = (int) modeloTabla.getValueAt(tablaEstudiantes.getSelectedRow(), 0);
                estudianteService.eliminarEstudiante(id);
                mostrarMensaje("Estudiante eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarEstudiantes();
            } catch (Exception ex) {
                mostrarMensaje("Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
    }
}