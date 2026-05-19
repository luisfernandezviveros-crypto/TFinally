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
import javax.swing.DefaultComboBoxModel;
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

import com.uniajc.dao.DocenteDao;
import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.MateriaDao;
import com.uniajc.modelo.Docente;
import com.uniajc.modelo.Grupo;
import com.uniajc.modelo.Materia;
import com.uniajc.servicios.GrupoService;

public class VistaGrupoSwing extends JPanel implements ActionListener, VistaGrupo {

    private static final Color COLOR_PRIMARIO   = new Color(41, 128, 185);
    private static final Color COLOR_SECUNDARIO = new Color(236, 240, 241);
    private static final Color COLOR_ACENTO     = new Color(231, 76, 60);
    private static final Color COLOR_EXITO      = new Color(39, 174, 96);
    private static final String[] COLUMNAS = {"ID","Nombre","Docente","Materia"};

    private final GrupoService grupoService;
    private final DocenteDao docenteDao;
    private final MateriaDao materiaDao;

    private JTable tablaGrupo;
    private DefaultTableModel modeloTabla;
    private JTextField textoBusqueda;
    private JButton botonBuscar, botonLimpiar, botonRegistrar, botonEditar, botonEliminar;
    private JDialog dialogoFormulario;
    private JTextField campoNombre;
    private JComboBox<String> comboDocente, comboMateria;
    private List<Docente> listaDocentes;
    private List<Materia> listaMaterias;
    private JButton botonGuardar, botonCancelar;
    private boolean modoEdicion;
    private Grupo grupoSeleccionado;

    public VistaGrupoSwing() {
        this.docenteDao = new DocenteDao();
        this.materiaDao = new MateriaDao();
        this.grupoService = new GrupoService(new GrupoDao(), docenteDao, materiaDao);
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
        tablaGrupo = new JTable(modeloTabla);
        tablaGrupo.setFont(new Font("Segoe UI", Font.PLAIN, 12)); tablaGrupo.setRowHeight(25);
        tablaGrupo.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaGrupo.setGridColor(new Color(189, 195, 199)); tablaGrupo.setBackground(Color.WHITE);
        tablaGrupo.setSelectionBackground(COLOR_PRIMARIO); tablaGrupo.setSelectionForeground(Color.WHITE);
        tablaGrupo.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaGrupo.getTableHeader().setBackground(COLOR_PRIMARIO); tablaGrupo.getTableHeader().setForeground(Color.WHITE);
        tablaGrupo.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, s, f, row, col);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); return c;
            }
        };
        r.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < COLUMNAS.length; i++) tablaGrupo.getColumnModel().getColumn(i).setCellRenderer(r);
        tablaGrupo.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaGrupo.getColumnModel().getColumn(1).setPreferredWidth(150);
        tablaGrupo.getColumnModel().getColumn(2).setPreferredWidth(200);
        tablaGrupo.getColumnModel().getColumn(3).setPreferredWidth(200);
        JScrollPane scroll = new JScrollPane(tablaGrupo);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 2));
        panel.add(scroll, BorderLayout.CENTER); return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15)); panel.setBackground(COLOR_SECUNDARIO);
        botonRegistrar = crearBoton("Registrar", COLOR_EXITO); botonRegistrar.setPreferredSize(new Dimension(130, 40));
        botonRegistrar.addActionListener(e -> mostrarDialogo(false));
        botonEditar = crearBoton("Editar", COLOR_PRIMARIO); botonEditar.setPreferredSize(new Dimension(130, 40));
        botonEditar.addActionListener(e -> {
            if (tablaGrupo.getSelectedRow() >= 0) mostrarDialogo(true);
            else mostrarMsg("Seleccione un grupo.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        });
        botonEliminar = crearBoton("Eliminar", COLOR_ACENTO); botonEliminar.setPreferredSize(new Dimension(130, 40));
        botonEliminar.addActionListener(e -> eliminar());
        panel.add(botonRegistrar); panel.add(botonEditar); panel.add(botonEliminar); return panel;
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
            for (Grupo g : grupoService.obtenerTodosLosGrupos())
                modeloTabla.addRow(new Object[]{g.getId(), g.getNombre(), g.getDocenteNombre(), g.getMateriaNombre()});
        } catch (Exception ex) { mostrarMsg("Error al cargar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void buscar() {
        modeloTabla.setRowCount(0);
        String q = textoBusqueda.getText().trim().toLowerCase();
        if (q.isEmpty()) { cargar(); return; }
        try {
            for (Grupo g : grupoService.obtenerTodosLosGrupos()) {
                String dn = g.getDocenteNombre() != null ? g.getDocenteNombre().toLowerCase() : "";
                String mn = g.getMateriaNombre() != null ? g.getMateriaNombre().toLowerCase() : "";
                if (String.valueOf(g.getId()).contains(q) || g.getNombre().toLowerCase().contains(q) || dn.contains(q) || mn.contains(q))
                    modeloTabla.addRow(new Object[]{g.getId(), g.getNombre(), g.getDocenteNombre(), g.getMateriaNombre()});
            }
        } catch (Exception ex) { mostrarMsg("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void mostrarDialogo(boolean esEdicion) {
        this.modoEdicion = esEdicion;
        if (esEdicion && tablaGrupo.getSelectedRow() >= 0) {
            int id = (int) modeloTabla.getValueAt(tablaGrupo.getSelectedRow(), 0);
            try { grupoSeleccionado = grupoService.buscarPorId(id).orElse(null); }
            catch (Exception ex) { mostrarMsg("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); return; }
        } else { grupoSeleccionado = new Grupo(); }

        // Cargar docentes y materias para los combos
        try { listaDocentes = docenteDao.obtenerTodos(); }
        catch (Exception ex) { listaDocentes = new java.util.ArrayList<>(); }
        try { listaMaterias = materiaDao.obtenerTodos(); }
        catch (Exception ex) { listaMaterias = new java.util.ArrayList<>(); }

        dialogoFormulario = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
            esEdicion ? "Editar Grupo" : "Registrar Grupo", true);
        dialogoFormulario.setSize(480, 360); dialogoFormulario.setLocationRelativeTo(this); dialogoFormulario.setResizable(false);

        JPanel pnl = new JPanel(new GridBagLayout()); pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10); gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel tit = new JLabel(esEdicion ? "✏ Editar Grupo" : "➕ Registrar Grupo");
        tit.setFont(new Font("Segoe UI", Font.BOLD, 18)); tit.setForeground(COLOR_PRIMARIO);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; pnl.add(tit, gbc); gbc.gridwidth = 1;

        // Nombre
        gbc.gridy = 1; gbc.gridx = 0;
        JLabel lNombre = new JLabel("Nombre:"); lNombre.setFont(new Font("Segoe UI", Font.BOLD, 13)); lNombre.setForeground(COLOR_PRIMARIO);
        pnl.add(lNombre, gbc); gbc.gridx = 1;
        campoNombre = new JTextField(20); campoNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        campoNombre.setPreferredSize(new Dimension(250, 30)); pnl.add(campoNombre, gbc);

        // Combo Docente
        gbc.gridy = 2; gbc.gridx = 0;
        JLabel lDoc = new JLabel("Docente:"); lDoc.setFont(new Font("Segoe UI", Font.BOLD, 13)); lDoc.setForeground(COLOR_PRIMARIO);
        pnl.add(lDoc, gbc); gbc.gridx = 1;
        String[] nombresDocentes = listaDocentes.stream()
            .map(d -> d.getId() + " - " + d.getNombre() + " " + d.getApellido()).toArray(String[]::new);
        comboDocente = new JComboBox<>(nombresDocentes);
        comboDocente.setFont(new Font("Segoe UI", Font.PLAIN, 13)); comboDocente.setPreferredSize(new Dimension(250, 30));
        pnl.add(comboDocente, gbc);

        // Combo Materia
        gbc.gridy = 3; gbc.gridx = 0;
        JLabel lMat = new JLabel("Materia:"); lMat.setFont(new Font("Segoe UI", Font.BOLD, 13)); lMat.setForeground(COLOR_PRIMARIO);
        pnl.add(lMat, gbc); gbc.gridx = 1;
        String[] nombresMaterias = listaMaterias.stream()
            .map(m -> m.getId() + " - " + m.getNombre()).toArray(String[]::new);
        comboMateria = new JComboBox<>(nombresMaterias);
        comboMateria.setFont(new Font("Segoe UI", Font.PLAIN, 13)); comboMateria.setPreferredSize(new Dimension(250, 30));
        pnl.add(comboMateria, gbc);

        if (esEdicion && grupoSeleccionado != null) {
            campoNombre.setText(grupoSeleccionado.getNombre());
            // Seleccionar docente actual
            for (int i = 0; i < listaDocentes.size(); i++) {
                if (listaDocentes.get(i).getId() == grupoSeleccionado.getDocenteId()) { comboDocente.setSelectedIndex(i); break; }
            }
            // Seleccionar materia actual
            for (int i = 0; i < listaMaterias.size(); i++) {
                if (listaMaterias.get(i).getId() == grupoSeleccionado.getMateriaId()) { comboMateria.setSelectedIndex(i); break; }
            }
        }

        gbc.gridy = 4; gbc.gridx = 0; gbc.gridwidth = 2; gbc.insets = new Insets(18, 10, 10, 10);
        JPanel pb = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0)); pb.setBackground(Color.WHITE);
        botonGuardar = crearBoton("Guardar", COLOR_EXITO); botonGuardar.setPreferredSize(new Dimension(110, 35));
        botonGuardar.addActionListener(e -> guardar());
        botonCancelar = crearBoton("Cancelar", new Color(149, 165, 166)); botonCancelar.setPreferredSize(new Dimension(110, 35));
        botonCancelar.addActionListener(e -> dialogoFormulario.dispose());
        pb.add(botonGuardar); pb.add(botonCancelar); pnl.add(pb, gbc);
        dialogoFormulario.add(pnl); dialogoFormulario.setVisible(true);
    }

    private void guardar() {
        try {
            String nombre = campoNombre.getText().trim();
            if (nombre.isEmpty()) { mostrarMsg("El nombre es obligatorio.", "Validación", JOptionPane.WARNING_MESSAGE); return; }
            if (comboDocente.getSelectedIndex() < 0 || listaDocentes.isEmpty()) {
                mostrarMsg("Seleccione un docente.", "Validación", JOptionPane.WARNING_MESSAGE); return;
            }
            if (comboMateria.getSelectedIndex() < 0 || listaMaterias.isEmpty()) {
                mostrarMsg("Seleccione una materia.", "Validación", JOptionPane.WARNING_MESSAGE); return;
            }
            grupoSeleccionado.setNombre(nombre);
            grupoSeleccionado.setDocenteId(listaDocentes.get(comboDocente.getSelectedIndex()).getId());
            grupoSeleccionado.setMateriaId(listaMaterias.get(comboMateria.getSelectedIndex()).getId());
            if (modoEdicion) { grupoService.actualizarGrupo(grupoSeleccionado); mostrarMsg("Grupo actualizado.", "Éxito", JOptionPane.INFORMATION_MESSAGE); }
            else { grupoService.registrarGrupo(grupoSeleccionado); mostrarMsg("Grupo registrado.", "Éxito", JOptionPane.INFORMATION_MESSAGE); }
            dialogoFormulario.dispose(); cargar();
        } catch (IllegalArgumentException ex) { mostrarMsg(ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE); }
        catch (Exception ex) { mostrarMsg("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void eliminar() {
        if (tablaGrupo.getSelectedRow() < 0) { mostrarMsg("Seleccione un grupo.", "Advertencia", JOptionPane.WARNING_MESSAGE); return; }
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar el grupo seleccionado?", "Confirmar",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            try {
                int id = (int) modeloTabla.getValueAt(tablaGrupo.getSelectedRow(), 0);
                grupoService.eliminarGrupo(id); mostrarMsg("Grupo eliminado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargar();
            } catch (Exception ex) { mostrarMsg("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void mostrarMsg(String msg, String titulo, int tipo) { JOptionPane.showMessageDialog(this, msg, titulo, tipo); }

    // ── Implementación de la interfaz VistaGrupo ───────────────────────────────
    @Override
    public Grupo solicitarDatosGrupo() { return grupoSeleccionado; }

    @Override
    public void mostrarTodosLosGrupos(List<Grupo> grupos) {
        modeloTabla.setRowCount(0);
        for (Grupo g : grupos)
            modeloTabla.addRow(new Object[]{g.getId(), g.getNombre(), g.getDocenteNombre(), g.getMateriaNombre()});
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        mostrarMsg(mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override public void actionPerformed(ActionEvent e) { }
}
