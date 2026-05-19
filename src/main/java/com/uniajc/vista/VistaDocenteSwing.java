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

import com.uniajc.dao.DocenteDao;
import com.uniajc.modelo.Docente;
import com.uniajc.servicios.DocenteService;

public class VistaDocenteSwing extends JPanel implements ActionListener, VistaDocente {

    private static final Color COLOR_PRIMARIO   = new Color(41, 128, 185);
    private static final Color COLOR_SECUNDARIO = new Color(236, 240, 241);
    private static final Color COLOR_ACENTO     = new Color(231, 76, 60);
    private static final Color COLOR_EXITO      = new Color(39, 174, 96);
    private static final String[] COLUMNAS = {"ID", "Nombre", "Apellido", "Email", "Especialidad"};

    private final DocenteService docenteService;

    private JTable tablaDocentes;
    private DefaultTableModel modeloTabla;
    private JTextField textoBusqueda;
    private JButton botonBuscar, botonLimpiar, botonRegistrar, botonEditar, botonEliminar;

    private JDialog dialogoFormulario;
    private JTextField campoNombre, campoApellido, campoEmail, campoEspecialidad;
    private JButton botonGuardar, botonCancelar;
    private boolean modoEdicion;
    private Docente docenteSeleccionado;

    public VistaDocenteSwing() {
        this.docenteService = new DocenteService(new DocenteDao());
        initComponents();
        cargar();
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

        JLabel lbl = new JLabel("Buscar:");
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(COLOR_PRIMARIO);

        textoBusqueda = new JTextField(25);
        textoBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textoBusqueda.setPreferredSize(new Dimension(200, 30));
        textoBusqueda.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) buscar();
            }
        });

        botonBuscar = crearBoton("Buscar", COLOR_PRIMARIO);
        botonBuscar.addActionListener(e -> buscar());

        botonLimpiar = crearBoton("Limpiar", new Color(149, 165, 166));
        botonLimpiar.addActionListener(e -> { textoBusqueda.setText(""); cargar(); });

        panel.add(lbl);
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

        tablaDocentes = new JTable(modeloTabla);
        tablaDocentes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tablaDocentes.setRowHeight(25);
        tablaDocentes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaDocentes.setGridColor(new Color(189, 195, 199));
        tablaDocentes.setBackground(Color.WHITE);
        tablaDocentes.setSelectionBackground(COLOR_PRIMARIO);
        tablaDocentes.setSelectionForeground(Color.WHITE);
        tablaDocentes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaDocentes.getTableHeader().setBackground(COLOR_PRIMARIO);
        tablaDocentes.getTableHeader().setForeground(Color.WHITE);
        tablaDocentes.getTableHeader().setReorderingAllowed(false);

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
            tablaDocentes.getColumnModel().getColumn(i).setCellRenderer(renderer);

        tablaDocentes.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaDocentes.getColumnModel().getColumn(1).setPreferredWidth(120);
        tablaDocentes.getColumnModel().getColumn(2).setPreferredWidth(120);
        tablaDocentes.getColumnModel().getColumn(3).setPreferredWidth(200);
        tablaDocentes.getColumnModel().getColumn(4).setPreferredWidth(150);

        JScrollPane scroll = new JScrollPane(tablaDocentes);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 2));
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.setBackground(COLOR_SECUNDARIO);

        botonRegistrar = crearBoton("Registrar", COLOR_EXITO);
        botonRegistrar.setPreferredSize(new Dimension(130, 40));
        botonRegistrar.addActionListener(e -> mostrarDialogo(false));

        botonEditar = crearBoton("Editar", COLOR_PRIMARIO);
        botonEditar.setPreferredSize(new Dimension(130, 40));
        botonEditar.addActionListener(e -> {
            if (tablaDocentes.getSelectedRow() >= 0) mostrarDialogo(true);
            else mostrarMsg("Seleccione un docente de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        });

        botonEliminar = crearBoton("Eliminar", COLOR_ACENTO);
        botonEliminar.setPreferredSize(new Dimension(130, 40));
        botonEliminar.addActionListener(e -> eliminar());

        panel.add(botonRegistrar);
        panel.add(botonEditar);
        panel.add(botonEliminar);
        return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(color);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setPreferredSize(new Dimension(110, 35));
        b.addActionListener(this);
        return b;
    }

    private void cargar() {
        modeloTabla.setRowCount(0);
        try {
            for (Docente d : docenteService.obtenerTodosLosDocentes())
                modeloTabla.addRow(new Object[]{d.getId(), d.getNombre(), d.getApellido(), d.getEmail(), d.getEspecialidad()});
        } catch (Exception ex) {
            mostrarMsg("Error al cargar docentes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscar() {
        modeloTabla.setRowCount(0);
        String q = textoBusqueda.getText().trim().toLowerCase();
        if (q.isEmpty()) { cargar(); return; }
        try {
            for (Docente d : docenteService.obtenerTodosLosDocentes()) {
                boolean coincide = String.valueOf(d.getId()).contains(q)
                        || d.getNombre().toLowerCase().contains(q)
                        || d.getApellido().toLowerCase().contains(q)
                        || d.getEmail().toLowerCase().contains(q)
                        || (d.getEspecialidad() != null && d.getEspecialidad().toLowerCase().contains(q));
                if (coincide)
                    modeloTabla.addRow(new Object[]{d.getId(), d.getNombre(), d.getApellido(), d.getEmail(), d.getEspecialidad()});
            }
        } catch (Exception ex) {
            mostrarMsg("Error al buscar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarDialogo(boolean esEdicion) {
        this.modoEdicion = esEdicion;
        if (esEdicion && tablaDocentes.getSelectedRow() >= 0) {
            int id = (int) modeloTabla.getValueAt(tablaDocentes.getSelectedRow(), 0);
            try {
                docenteSeleccionado = docenteService.buscarPorId(id).orElse(null);
            } catch (Exception ex) {
                mostrarMsg("Error al cargar docente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } else {
            docenteSeleccionado = new Docente();
        }

        dialogoFormulario = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
                esEdicion ? "Editar Docente" : "Registrar Docente", true);
        dialogoFormulario.setSize(480, 400);
        dialogoFormulario.setLocationRelativeTo(this);
        dialogoFormulario.setResizable(false);

        JPanel pnl = new JPanel(new GridBagLayout());
        pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(9, 10, 9, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel tit = new JLabel(esEdicion ? "✏ Editar Docente" : "➕ Registrar Docente");
        tit.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tit.setForeground(COLOR_PRIMARIO);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        pnl.add(tit, gbc);
        gbc.gridwidth = 1;

        String[] labels = {"Nombre:", "Apellido:", "Email:", "Especialidad:"};
        JTextField[] campos = new JTextField[4];
        for (int i = 0; i < labels.length; i++) {
            gbc.gridy = i + 1; gbc.gridx = 0;
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
            lbl.setForeground(COLOR_PRIMARIO);
            pnl.add(lbl, gbc);
            gbc.gridx = 1;
            campos[i] = new JTextField(20);
            campos[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            campos[i].setPreferredSize(new Dimension(250, 30));
            pnl.add(campos[i], gbc);
        }
        campoNombre       = campos[0];
        campoApellido     = campos[1];
        campoEmail        = campos[2];
        campoEspecialidad = campos[3];

        if (esEdicion && docenteSeleccionado != null) {
            campoNombre.setText(docenteSeleccionado.getNombre());
            campoApellido.setText(docenteSeleccionado.getApellido());
            campoEmail.setText(docenteSeleccionado.getEmail());
            campoEspecialidad.setText(docenteSeleccionado.getEspecialidad());
        }

        gbc.gridy = labels.length + 1; gbc.gridx = 0; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        JPanel pb = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        pb.setBackground(Color.WHITE);
        botonGuardar = crearBoton("Guardar", COLOR_EXITO);
        botonGuardar.setPreferredSize(new Dimension(110, 35));
        botonGuardar.addActionListener(e -> guardar());
        botonCancelar = crearBoton("Cancelar", new Color(149, 165, 166));
        botonCancelar.setPreferredSize(new Dimension(110, 35));
        botonCancelar.addActionListener(e -> dialogoFormulario.dispose());
        pb.add(botonGuardar); pb.add(botonCancelar);
        pnl.add(pb, gbc);

        dialogoFormulario.add(pnl);
        dialogoFormulario.setVisible(true);
    }

    private void guardar() {
        try {
            String nombre       = campoNombre.getText().trim();
            String apellido     = campoApellido.getText().trim();
            String email        = campoEmail.getText().trim();
            String especialidad = campoEspecialidad.getText().trim();

            docenteSeleccionado.setNombre(nombre);
            docenteSeleccionado.setApellido(apellido);
            docenteSeleccionado.setEmail(email);
            docenteSeleccionado.setEspecialidad(especialidad);

            if (modoEdicion) {
                docenteService.actualizarDocente(docenteSeleccionado);
                mostrarMsg("Docente actualizado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                docenteService.registrarDocente(docenteSeleccionado);
                mostrarMsg("Docente registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
            dialogoFormulario.dispose();
            cargar();
        } catch (IllegalArgumentException ex) {
            mostrarMsg(ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE);
        } catch (Exception ex) {
            mostrarMsg("Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (tablaDocentes.getSelectedRow() < 0) {
            mostrarMsg("Seleccione un docente de la tabla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar el docente seleccionado?", "Confirmar",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            try {
                int id = (int) modeloTabla.getValueAt(tablaDocentes.getSelectedRow(), 0);
                docenteService.eliminarDocente(id);
                mostrarMsg("Docente eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargar();
            } catch (Exception ex) {
                mostrarMsg("Error al eliminar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarMsg(String msg, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, msg, titulo, tipo);
    }

    // ── Implementación de la interfaz VistaDocente ─────────────────────────────
    @Override
    public Docente solicitarDatosDocente() {
        // En la UI Swing los datos se capturan directamente en el diálogo
        return docenteSeleccionado;
    }

    @Override
    public void mostrarTodosLosDocentes(List<Docente> docentes) {
        modeloTabla.setRowCount(0);
        for (Docente d : docentes)
            modeloTabla.addRow(new Object[]{d.getId(), d.getNombre(), d.getApellido(), d.getEmail(), d.getEspecialidad()});
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        mostrarMsg(mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void actionPerformed(ActionEvent e) { }
}
