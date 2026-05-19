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
import javax.swing.BorderFactory;
import java.util.List;
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

import com.uniajc.dao.MateriaDao;
import com.uniajc.modelo.Materia;
import com.uniajc.servicios.MateriaService;

public class VistaMateriaSwing extends JPanel implements ActionListener, VistaMateria {

    private static final Color COLOR_PRIMARIO   = new Color(41, 128, 185);
    private static final Color COLOR_SECUNDARIO = new Color(236, 240, 241);
    private static final Color COLOR_ACENTO     = new Color(231, 76, 60);
    private static final Color COLOR_EXITO      = new Color(39, 174, 96);
    private static final String[] COLUMNAS = {"ID","Nombre","Código","Créditos"};

    private final MateriaService materiaService;
    private JTable tablaMateria;
    private DefaultTableModel modeloTabla;
    private JTextField textoBusqueda;
    private JButton botonBuscar, botonLimpiar, botonRegistrar, botonEditar, botonEliminar;
    private JDialog dialogoFormulario;
    private JTextField campoNombre, campoCodigo, campoCreditos;
    private JButton botonGuardar, botonCancelar;
    private boolean modoEdicion;
    private Materia materiaSeleccionada;

    public VistaMateriaSwing() {
        this.materiaService = new MateriaService(new MateriaDao());
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
        tablaMateria = new JTable(modeloTabla);
        tablaMateria.setFont(new Font("Segoe UI", Font.PLAIN, 12)); tablaMateria.setRowHeight(25);
        tablaMateria.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaMateria.setGridColor(new Color(189, 195, 199)); tablaMateria.setBackground(Color.WHITE);
        tablaMateria.setSelectionBackground(COLOR_PRIMARIO); tablaMateria.setSelectionForeground(Color.WHITE);
        tablaMateria.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaMateria.getTableHeader().setBackground(COLOR_PRIMARIO); tablaMateria.getTableHeader().setForeground(Color.WHITE);
        tablaMateria.getTableHeader().setReorderingAllowed(false);
        DefaultTableCellRenderer r = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, v, s, f, row, col);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); return c;
            }
        };
        r.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < COLUMNAS.length; i++) tablaMateria.getColumnModel().getColumn(i).setCellRenderer(r);
        tablaMateria.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaMateria.getColumnModel().getColumn(1).setPreferredWidth(250);
        tablaMateria.getColumnModel().getColumn(2).setPreferredWidth(120);
        tablaMateria.getColumnModel().getColumn(3).setPreferredWidth(80);
        JScrollPane scroll = new JScrollPane(tablaMateria);
        scroll.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 2));
        panel.add(scroll, BorderLayout.CENTER); return panel;
    }

    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15)); panel.setBackground(COLOR_SECUNDARIO);
        botonRegistrar = crearBoton("Registrar", COLOR_EXITO); botonRegistrar.setPreferredSize(new Dimension(130, 40));
        botonRegistrar.addActionListener(e -> mostrarDialogo(false));
        botonEditar = crearBoton("Editar", COLOR_PRIMARIO); botonEditar.setPreferredSize(new Dimension(130, 40));
        botonEditar.addActionListener(e -> {
            if (tablaMateria.getSelectedRow() >= 0) mostrarDialogo(true);
            else mostrarMsg("Seleccione una materia.", "Advertencia", JOptionPane.WARNING_MESSAGE);
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
            for (Materia m : materiaService.obtenerTodasLasMaterias())
                modeloTabla.addRow(new Object[]{m.getId(), m.getNombre(), m.getCodigo(), m.getCreditos()});
        } catch (Exception ex) { mostrarMsg("Error al cargar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void buscar() {
        modeloTabla.setRowCount(0);
        String q = textoBusqueda.getText().trim().toLowerCase();
        if (q.isEmpty()) { cargar(); return; }
        try {
            for (Materia m : materiaService.obtenerTodasLasMaterias()) {
                if (String.valueOf(m.getId()).contains(q) || m.getNombre().toLowerCase().contains(q) ||
                    m.getCodigo().toLowerCase().contains(q) || String.valueOf(m.getCreditos()).contains(q))
                    modeloTabla.addRow(new Object[]{m.getId(), m.getNombre(), m.getCodigo(), m.getCreditos()});
            }
        } catch (Exception ex) { mostrarMsg("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void mostrarDialogo(boolean esEdicion) {
        this.modoEdicion = esEdicion;
        if (esEdicion && tablaMateria.getSelectedRow() >= 0) {
            int id = (int) modeloTabla.getValueAt(tablaMateria.getSelectedRow(), 0);
            try { materiaSeleccionada = materiaService.buscarPorId(id).orElse(null); }
            catch (Exception ex) { mostrarMsg("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); return; }
        } else { materiaSeleccionada = new Materia(); }

        dialogoFormulario = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this),
            esEdicion ? "Editar Materia" : "Registrar Materia", true);
        dialogoFormulario.setSize(450, 340); dialogoFormulario.setLocationRelativeTo(this); dialogoFormulario.setResizable(false);

        JPanel pnl = new JPanel(new GridBagLayout()); pnl.setBackground(Color.WHITE);
        pnl.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10); gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel tit = new JLabel(esEdicion ? "✏ Editar Materia" : "➕ Registrar Materia");
        tit.setFont(new Font("Segoe UI", Font.BOLD, 18)); tit.setForeground(COLOR_PRIMARIO);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; pnl.add(tit, gbc); gbc.gridwidth = 1;

        String[] labs = {"Nombre:", "Código:", "Créditos:"};
        JTextField[] fs = new JTextField[3];
        for (int i = 0; i < labs.length; i++) {
            gbc.gridy = i + 1; gbc.gridx = 0;
            JLabel l = new JLabel(labs[i]); l.setFont(new Font("Segoe UI", Font.BOLD, 13)); l.setForeground(COLOR_PRIMARIO);
            pnl.add(l, gbc); gbc.gridx = 1;
            fs[i] = new JTextField(20); fs[i].setFont(new Font("Segoe UI", Font.PLAIN, 13));
            fs[i].setPreferredSize(new Dimension(250, 30)); pnl.add(fs[i], gbc);
        }
        campoNombre = fs[0]; campoCodigo = fs[1]; campoCreditos = fs[2];

        if (esEdicion && materiaSeleccionada != null) {
            campoNombre.setText(materiaSeleccionada.getNombre());
            campoCodigo.setText(materiaSeleccionada.getCodigo());
            campoCreditos.setText(String.valueOf(materiaSeleccionada.getCreditos()));
        }

        gbc.gridy = labs.length + 1; gbc.gridx = 0; gbc.gridwidth = 2; gbc.insets = new Insets(18, 10, 10, 10);
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
            String codigo = campoCodigo.getText().trim();
            String creditosStr = campoCreditos.getText().trim();
            if (nombre.isEmpty() || codigo.isEmpty() || creditosStr.isEmpty()) {
                mostrarMsg("Todos los campos son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE); return;
            }
            int creditos;
            try { creditos = Integer.parseInt(creditosStr); } catch (NumberFormatException ex) {
                mostrarMsg("Los créditos deben ser un número entero.", "Validación", JOptionPane.WARNING_MESSAGE); return;
            }
            materiaSeleccionada.setNombre(nombre); materiaSeleccionada.setCodigo(codigo); materiaSeleccionada.setCreditos(creditos);
            if (modoEdicion) { materiaService.actualizarMateria(materiaSeleccionada); mostrarMsg("Materia actualizada.", "Éxito", JOptionPane.INFORMATION_MESSAGE); }
            else { materiaService.registrarMateria(materiaSeleccionada); mostrarMsg("Materia registrada.", "Éxito", JOptionPane.INFORMATION_MESSAGE); }
            dialogoFormulario.dispose(); cargar();
        } catch (IllegalArgumentException ex) { mostrarMsg(ex.getMessage(), "Validación", JOptionPane.WARNING_MESSAGE); }
        catch (Exception ex) { mostrarMsg("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    }

    private void eliminar() {
        if (tablaMateria.getSelectedRow() < 0) { mostrarMsg("Seleccione una materia.", "Advertencia", JOptionPane.WARNING_MESSAGE); return; }
        if (JOptionPane.showConfirmDialog(this, "¿Eliminar la materia seleccionada?", "Confirmar",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            try {
                int id = (int) modeloTabla.getValueAt(tablaMateria.getSelectedRow(), 0);
                materiaService.eliminarMateria(id); mostrarMsg("Materia eliminada.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargar();
            } catch (Exception ex) { mostrarMsg("Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
        }
    }

    private void mostrarMsg(String msg, String titulo, int tipo) { JOptionPane.showMessageDialog(this, msg, titulo, tipo); }

    // ── Implementación de la interfaz VistaMateria ─────────────────────────────
    @Override
    public Materia solicitarDatosMateria() { return materiaSeleccionada; }

    @Override
    public void mostrarTodasLasMaterias(List<Materia> materias) {
        modeloTabla.setRowCount(0);
        for (Materia m : materias)
            modeloTabla.addRow(new Object[]{m.getId(), m.getNombre(), m.getCodigo(), m.getCreditos()});
    }

    @Override
    public void mostrarMensaje(String mensaje) {
        mostrarMsg(mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override public void actionPerformed(ActionEvent e) { }
}
