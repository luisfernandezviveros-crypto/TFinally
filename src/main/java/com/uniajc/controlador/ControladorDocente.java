package com.uniajc.controlador;

import com.uniajc.modelo.Docente;
import com.uniajc.servicios.DocenteService;
import com.uniajc.vista.VistaDocente;

public class ControladorDocente {

    private VistaDocente vista;
    private DocenteService servicio;

    public ControladorDocente(VistaDocente vista, DocenteService servicio) {
        this.vista = vista;
        this.servicio = servicio;
    }

    public void registrarDocente() {

        try {
            Docente docente = vista.solicitarDatosDocente();

            if (docente != null) {
                servicio.registrarDocente(docente);
                vista.mostrarMensaje("Docente registrado exitosamente.");
            }

        } catch (Exception e) {
            vista.mostrarMensaje("Error al registrar el docente.");
        }
    }

    public void mostrarTodosLosDocentes() {
        vista.mostrarTodosLosDocentes(servicio.obtenerTodosLosDocentes());
    }
}