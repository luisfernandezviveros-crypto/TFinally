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

public class VistaEstudianteSwing extends JPanel implements ActionListener, VistaEstudiante {

    private static final Color COLOR_PRIMARIO   = new Color(41, 128, 185);
    private static final Color COLOR_SECUNDARIO = new Color(236, 240, 241);
    private static final Color COLOR_ACENTO     = new Color(231, 76, 60);
    private static final Color COLOR_EXITO      = new Color(39, 174, 96);
    private static final String[] COLUMNAS = {"ID", "Nombre", "Apellido", "Email"};

    private final EstudianteService estudianteService;

    private JTable tablaEstudiantes;
    private DefaultTableModel modeloTabla;
    private JTextField textoBusqueda;
    private JButton botonBuscar, botonLimpiar, botonRegistrar, botonEditar, botonEliminar;

    private JDialog dialogoFormulario;
    private JTextField campoNombre, campoApellido, campoEmail;
    private JButton botonGuardar, botonCancelar;
    private boolean modoEdicion;
    private Estudiante estudianteSeleccionado;

    public VistaEstudianteSwing() {
        this.estudianteService = new EstudianteService(new EstudianteDao());
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
                if (e.getKeyCode() == KeyEvent.VK_ENTER) buscarEstudiantes();
            }
        });

        botonBuscar = crearBoton("Buscar", COLOR_PRIMARIO);
        botonBuscar.addActionListener(e -> buscarEstudiantes());

        botonLimpiar = crearBoton("Limpiar", new Color(149, 165, 166));
        botonLimpiar.addActionListener(e -> { textoBusqueda.setText(""); cargarEstudiantes(); });

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
            public boolean isCellEditable(int row, int column) { return false; }
        };

        tablaEstudiantes = new JTable(modeloTabla);
        tablaEstudiantes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaEstudiantes.setRowHeight(25);
        tablaEstudiantes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaEstudiantes.setGridColor(new Color(189, 195, 199));
        tablaEstudiantes.setBackground(Color.WHITE);
        tablaEstudiantes.setSelectionBackground(COLOR_PRIMARIO);
        tablaEstudiantes.setSelectionForeground(Color.WHITE);
        tablaEstudiantes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaEstudiantes.getTableHeader().setBackground(COLOR_PRIMARIO);
        tablaEstudiantes.getTableHeader().setForeground(Color.WHITE);
        tablaEstudiantes.getTableHeader().setReorderingAllowed(false);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return c;
            }
        };
        renderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < COLUMNAS.length; i++)
            tablaEstudiantes.getColumnModel().getColumn(i).setCellRenderer(renderer);

        tablaEstudiantes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaEstudiantes.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaEstudiantes.getColumnModel().getColumn(2).setPreferredWidth(150);
        tablaEstudiantes.getColumnModel().getColumn(3).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(tablaEstudiantes);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 2));
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
            if (tablaEstudiantes.getSelectedRow() >= 0) mostrarDialogoRegistro(true);
            else mostrarMensaje("Seleccione un estudiante de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        });

        botonEliminar = crearBoton("Eliminar", COLOR_ACENTO);
        botonEliminar.setPreferredSize(new Dimension(130, 40));
        botonEliminar.addActionListener(e -> eliminarEstudiante());

        panel.add(botonRegistrar);
        panel.add(botonEditar);
        panel.add(botonEliminar);
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
            for (Estudiante e : estudianteService.obtenerTodosLosEstudiantes())
                modeloTabla.addRow(new Object[]{e.getId(), e.getNombre(), e.getApellido(), e.getEmail()});
        } catch (Exception ex) {
            mostrarMensaje("Error al cargar estudiantes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarEstudiantes() {
        modeloTabla.setRowCount(0);
        String busqueda = textoBusqueda.getText().trim().toLowerCase();
        if (busqueda.isEmpty()) { cargarEstudiantes(); return; }
        try {
            for (Estudiante e : estudianteService.obtenerTodosLosEstudiantes()) {
                boolean coincide = String.valueOf(e.getId()).contains(busqueda)
                        || e.getNombre().toLowerCase().contains(busqueda)
                        || e.getApellido().toLowerCase().contains(busqueda)
                        || e.getEmail().toLowerCase().contains(busqueda);
                if (coincide)
                    modeloTabla.addRow(new Object[]{e.getId(), e.getNombre(), e.getApellido(), e.getEmail()});
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
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panelFormulario.add(labelTitulo, gbc);
        gbc.gridwidth = 1;

        String[] labels = {"Nombre:", "Apellido:", "Email:"};
        JTextField[] campos = new JTextField[3];
        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i + 1; gbc.gridx = 0;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lbl.setForeground(COLOR_PRIMARIO);
            panelFormulario.add(lbl, gbc);
            gbc.gridx = 1;
            campos[i] = new JTextField(20);
            campos[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            campos[i].setPreferredSize(new Dimension(250, 30));
            panelFormulario.add(campos[i], gbc);
        }
        campoNombre   = campos[0];
        campoApellido = campos[1];
        campoEmail    = campos[2];

        if (esEdicion && estudianteSeleccionado != null) {
            campoNombre.setText(estudianteSeleccionado.getNombre());
            campoApellido.setText(estudianteSeleccionado.getApellido());
            campoEmail.setText(estudianteSeleccionado.getEmail());
        }

        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(Color.WHITE);
        botonGuardar = crearBoton("Guardar", COLOR_EXITO);
        botonGuardar.setPreferredSize(new Dimension(110, 35));
        botonGuardar.addActionListener(e -> guardarEstudiante());
        botonCancelar = crearBoton("Cancelar", new Color(149, 165, 166));
        botonCancelar.setPreferredSize(new Dimension(110, 35));
        botonCancelar.addActionListener(e -> dialogoFormulario.dispose());
        panelBotones.add(botonGuardar);
        panelBotones.add(botonCancelar);
        panelFormulario.add(panelBotones, gbc);

        dialogoFormulario.add(panelFormulario);
        dialogoFormulario.setVisible(true);
    }

    private void guardarEstudiante() {
        try {
            estudianteSeleccionado.setNombre(campoNombre.getText().trim());
            estudianteSeleccionado.setApellido(campoApellido.getText().trim());
            estudianteSeleccionado.setEmail(campoEmail.getText().trim());

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
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar el estudiante seleccionado?", "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
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

    // ── Implementación de la interfaz VistaEstudiante ──────────────────────────
    @Override
    public Estudiante solicitarDatosEstudiante() {
        return estudianteSeleccionado;
    }

    @Override
    public void mostrarTodosLosEstudiantes(List<Estudiante> estudiantes) {
        modeloTabla.setRowCount(0);
        for (Estudiante e : estudiantes)
            modeloTabla.addRow(new Object[]{e.getId(), e.getNombre(), e.getApellido(), e.getEmail()});
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        mostrarMensaje(mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) { }
}
