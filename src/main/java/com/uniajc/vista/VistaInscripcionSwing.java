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
import java.time.LocalDate;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.InscripcionDao;
import com.uniajc.modelo.Estudiante;
import com.uniajc.modelo.Grupo;
import com.uniajc.modelo.Inscripcion;
import com.uniajc.servicios.InscripcionService;

public class VistaInscripcionSwing extends JPanel implements ActionListener, VistaInscripcion {

    private static final Color COLOR_PRIMARIO   = new Color(41, 128, 185);
    private static final Color COLOR_SECUNDARIO = new Color(236, 240, 241);
    private static final Color COLOR_ACENTO     = new Color(231, 76, 60);
    private static final Color COLOR_EXITO      = new Color(39, 174, 96);
    private static final String[] COLUMNAS = {"ID","Estudiante","Grupo","Fecha"};

    private final InscripcionService inscripcionService;
    private final EstudianteDao estudianteDao;
    private final GrupoDao grupoDao;

    private JTable tablaInscripcion;
    private DefaultTableModel modeloTabla;
    private JTextField textoBusqueda;
    private JButton botonBuscar, botonLimpiar, botonRegistrar, botonEliminar;
    private JDialog dialogoFormulario;
    private JComboBox<String> comboEstudiante, comboGrupo;
    private JTextField campoFecha;
    private List<Estudiante> listaEstudiantes;
    private List<Grupo> listaGrupos;
    private JButton botonGuardar, botonCancelar;

    public VistaInscripcionSwing() {
        this.estudianteDao = new EstudianteDao();
        this.grupoDao = new GrupoDao();
        this.inscripcionService = new InscripcionService(new InscripcionDao(), estudianteDao, grupoDao);
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
        JLabel lbl = new JLabel("Buscar:"); lbl.setFont(new Font("Segoe UI", Font.BOLD, 13)); lbl.setForeground(COLOR_PRIMARIO);
        textoBusqueda = new JTextField(25); textoBusqueda.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        textoBusqueda.setPreferredSize(new Dimension(200, 30));
        textoBusqueda.addKeyListener(new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e) { if (e.getKeyCode() == KeyEvent.VK_ENTER) buscar(); }
        });
        botonBuscar = crearBoton("Buscar", COLOR_PRIMARIO); botonBuscar.addActionListener(e -> buscar());
        botonLimpiar = crearBoton("Limpiar", new Color(149, 165, 166)); botonLimpiar.addActionListener(e -> { textoBusqueda.setText(""); cargar(); });
        panel.add(lbl); panel.add(textoBusqueda); panel.add(botonBuscar); panel.add(botonLimpiar);
        return panel;
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout()); panel.setBackground(COLOR_SECUNDARIO);
        modeloTabla = new DefaultTableModel(COLUMNAS, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        tablaInscripcion = new JTable(modeloTabla);
        tablaInscripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12)); tablaInscripcion.setRowHeight(25);
        tablaInscripcion.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaInscripcion.setGridColor(new Color(189, 195, 199)); tablaInscripcion.setBackground(Color.WHITE);
        tablaInscripcion.setSelectionBackground(COLOR_PRIMARIO); tablaInscripcion.setSelectionForeground(Color.WHITE);
        tablaInscripcion.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaInscripcion.getTableHeader().setBackground(COLOR_PRIMARIO); tablaInscripcion.getTableHeader().setForeground(Color.WHITE);
        tablaInscripcion.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, s, f, row, col);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); return c;
            }
        };
        r.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < COLUMNAS.length; i++) tablaInscripcion.getColumnModel().getColumn(i).setCellRenderer(r);
        tablaInscripcion.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaInscripcion.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaInscripcion.getColumnModel().getColumn(2).setPreferredWidth(180);
        tablaInscripcion.getColumnModel().getColumn(3).setPreferredWidth(100);
        JScrollPane scroll = new JScrollPane(tablaInscripcion);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 2));
        panel.add(scroll, BorderLayout.CENTER); return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15)); panel.setBackground(COLOR_SECUNDARIO);
        botonRegistrar = crearBoton("Inscribir", COLOR_EXITO); botonRegistrar.setPreferredSize(new Dimension(130, 40));
        botonRegistrar.addActionListener(e -> mostrarDialogo());
        botonEliminar = crearBoton("Cancelar Inscripción", COLOR_ACENTO); botonEliminar.setPreferredSize(new Dimension(180, 40));
        botonEliminar.addActionListener(e -> eliminar());
        panel.add(botonRegistrar); panel.add(botonEliminar); return panel;
    }

    private JButton crearBoton(String texto, Color color) {
        JButton b = new JButton(texto); b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(color); b.setForeground(Color.WHITE); b.setFocusPainted(false);
        b.setBorderPainted(false); b.setOpaque(true); b.setPreferredSize(new Dimension(110, 35));
        b.addActionListener(this); return b;
    }

    private void cargar() {
        modeloTabla.setRowCount(0);
        try {
            for (Inscripcion i : inscripcionService.obtenerTodasLasInscripciones())
                modeloTabla.addRow(new Object[]{i.getId(), i.getEstudianteNombre(), i.getGrupoNombre(), i.getFecha()});
        } catch (Exception ex) { mostrarMsg("Error al cargar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void buscar() {
        modeloTabla.setRowCount(0);
        String q = textoBusqueda.getText().trim().toLowerCase();
        if (q.isEmpty()) { cargar(); return; }
        try {
            for (Inscripcion i : inscripcionService.obtenerTodasLasInscripciones()) {
                String en = i.getEstudianteNombre() != null ? i.getEstudianteNombre().toLowerCase() : "";
                String gn = i.getGrupoNombre() != null ? i.getGrupoNombre().toLowerCase() : "";
                String f = i.getFecha() != null ? i.getFecha().toLowerCase() : "";
                if (String.valueOf(i.getId()).contains(q) || en.contains(q) || gn.contains(q) || f.contains(q))
                    modeloTabla.addRow(new Object[]{i.getId(), i.getEstudianteNombre(), i.getGrupoNombre(), i.getFecha()});
            }
        } catch (Exception ex) { mostrarMsg("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void mostrarDialogo() {
        try { listaEstudiantes = estudianteDao.obtenerTodos(); }
        catch (Exception ex) { listaEstudiantes = new java.util.ArrayList<>(); }
        try { listaGrupos = grupoDao.obtenerTodos(); }
        catch (Exception ex) { listaGrupos = new java.util.ArrayList<>(); }

        if (listaEstudiantes.isEmpty() || listaGrupos.isEmpty()) {
            mostrarMsg("Debe existir al menos un estudiante y un grupo registrado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        dialogoFormulario = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Nueva Inscripción", true);
        dialogoFormulario.setSize(480, 340); dialogoFormulario.setLocationRelativeTo(this); dialogoFormulario.setResizable(false);

        JPanel pnl = new JPanel(new GridBagLayout()); pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10); gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel tit = new JLabel("➕ Nueva Inscripción");
        tit.setFont(new Font("Segoe UI", Font.BOLD, 18)); tit.setForeground(COLOR_PRIMARIO);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; pnl.add(tit, gbc); gbc.gridwidth = 1;

        // Estudiante
        gbc.gridy = 1; gbc.gridx = 0;
        JLabel lE = new JLabel("Estudiante:"); lE.setFont(new Font("Segoe UI", Font.BOLD, 13)); lE.setForeground(COLOR_PRIMARIO);
        pnl.add(lE, gbc); gbc.gridx = 1;
        String[] nombresEst = listaEstudiantes.stream()
            .map(e -> e.getId() + " - " + e.getNombre() + " " + e.getApellido()).toArray(String[]::new);
        comboEstudiante = new JComboBox<>(nombresEst);
        comboEstudiante.setFont(new Font("Segoe UI", Font.PLAIN, 13)); comboEstudiante.setPreferredSize(new Dimension(270, 30));
        pnl.add(comboEstudiante, gbc);

        // Grupo
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lG = new JLabel("Grupo:"); lG.setFont(new Font("Segoe UI", Font.BOLD, 13)); lG.setForeground(COLOR_PRIMARIO);
        pnl.add(lG, gbc); gbc.gridx = 1;
        String[] nombresGrupo = listaGrupos.stream()
            .map(g -> g.getId() + " - " + g.getNombre()).toArray(String[]::new);
        comboGrupo = new JComboBox<>(nombresGrupo);
        comboGrupo.setFont(new Font("Segoe UI", Font.PLAIN, 13)); comboGrupo.setPreferredSize(new Dimension(270, 30));
        pnl.add(comboGrupo, gbc);

        // Fecha
        gbc.gridy = 3; gbc.gridx = 0;
        JLabel lF = new JLabel("Fecha (YYYY-MM-DD):"); lF.setFont(new Font("Segoe UI", Font.BOLD, 13)); lF.setForeground(COLOR_PRIMARIO);
        pnl.add(lF, gbc); gbc.gridx = 1;
        campoFecha = new JTextField(LocalDate.now().toString());
        campoFecha.setFont(new Font("Segoe UI", Font.PLAIN, 13)); campoFecha.setPreferredSize(new Dimension(270, 30));
        pnl.add(campoFecha, gbc);

        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2; gbc.insets = new Insets(18, 10, 10, 10);
        JPanel pb = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0)); pb.setBackground(Color.WHITE);
        botonGuardar = crearBoton("Inscribir", COLOR_EXITO); botonGuardar.setPreferredSize(new Dimension(110, 35));
        botonGuardar.addActionListener(e -> guardar());
        botonCancelar = crearBoton("Cancelar", new Color(149, 165, 166)); botonCancelar.setPreferredSize(new Dimension(110, 35));
        botonCancelar.addActionListener(e -> dialogoFormulario.dispose());
        pb.add(botonGuardar); pb.add(botonCancelar); pnl.add(pb, gbc);
        dialogoFormulario.add(pnl); dialogoFormulario.setVisible(true);
    }

    private void guardar() {
        try {
            if (comboEstudiante.getSelectedIndex() < 0) { mostrarMsg("Seleccione un estudiante.", "Validación", JOptionPane.WARNING_MESSAGE); return; }
            if (comboGrupo.getSelectedIndex() < 0) { mostrarMsg("Seleccione un grupo.", "Validación", JOptionPane.WARNING_MESSAGE); return; }
            Inscripcion ins = new Inscripcion();
            ins.setEstudianteId(listaEstudiantes.get(comboEstudiante.getSelectedIndex()).getId());
            ins.setGrupoId(listaGrupos.get(comboGrupo.getSelectedIndex()).getId());
            ins.setFecha(campoFecha.getText().trim());
            inscripcionService.registrarInscripcion(ins);
            mostrarMsg("Inscripción registrada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            dialogoFormulario.dispose(); cargar();
        } catch (IllegalArgumentException ex) { mostrarMsg(ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE); }
        catch (Exception ex) { mostrarMsg("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void eliminar() {
        if (tablaInscripcion.getSelectedRow() < 0) { mostrarMsg("Seleccione una inscripción.", "Advertencia", JOptionPane.WARNING_MESSAGE); return; }
        if (JOptionPane.showConfirmDialog(this, "¿Cancelar la inscripción seleccionada?", "Confirmar",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            try {
                int id = (int) modeloTabla.getValueAt(tablaInscripcion.getSelectedRow(), 0);
                inscripcionService.eliminarInscripcion(id); mostrarMsg("Inscripción cancelada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargar();
            } catch (Exception ex) { mostrarMsg("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void mostrarMsg(String msg, String titulo, int tipo) { JOptionPane.showMessageDialog(this, msg, titulo, tipo); }

    // ── Implementación de la interfaz VistaInscripcion ─────────────────────────
    @Override
    public Inscripcion solicitarDatosInscripcion() { return null; }

    @Override
    public void mostrarTodasLasInscripciones(List<Inscripcion> inscripciones) {
        modeloTabla.setRowCount(0);
        for (Inscripcion i : inscripciones)
            modeloTabla.addRow(new Object[]{i.getId(), i.getEstudianteNombre(), i.getGrupoNombre(), i.getFecha()});
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        mostrarMsg(mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override public void actionPerformed(ActionEvent e) { }
}
