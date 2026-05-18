package com.uniajc.controlador;

import com.uniajc.modelo.Materia;
import com.uniajc.servicios.MateriaService;
import com.uniajc.vista.VistaMateria;

public class ControladorMateria {

    private VistaMateria vista;
    private MateriaService servicio;

    public ControladorMateria(VistaMateria vista, MateriaService servicio) {
        this.vista = vista;
        this.servicio = servicio;
    }

    public void registrarMateria() {

        try {
            Materia materia = vista.solicitarDatosMateria();

            if (materia != null) {
                servicio.registrarMateria(materia);
                vista.mostrarMensaje("Materia registrada exitosamente.");
            }

        } catch (Exception e) {
            vista.mostrarMensaje("Error al registrar la materia.");
        }
    }

    public void mostrarTodasLasMaterias() {
        vista.mostrarTodasLasMaterias(servicio.obtenerTodasLasMaterias());
    }
}