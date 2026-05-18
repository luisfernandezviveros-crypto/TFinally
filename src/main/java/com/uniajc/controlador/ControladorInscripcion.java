package com.uniajc.controlador;

import com.uniajc.modelo.Inscripcion;
import com.uniajc.servicios.InscripcionService;
import com.uniajc.vista.VistaInscripcion;

public class ControladorInscripcion {

    private VistaInscripcion vista;
    private InscripcionService servicio;

    public ControladorInscripcion(VistaInscripcion vista, InscripcionService servicio) {
        this.vista = vista;
        this.servicio = servicio;
    }

    public void registrarInscripcion() {

        try {
            Inscripcion inscripcion = vista.solicitarDatosInscripcion();

            if (inscripcion != null) {
                servicio.registrarInscripcion(inscripcion);
                vista.mostrarMensaje("Inscripción registrada exitosamente.");
            }

        } catch (Exception e) {
            vista.mostrarMensaje("Error al registrar la inscripción.");
        }
    }

    public void mostrarTodasLasInscripciones() {
        vista.mostrarTodasLasInscripciones(servicio.obtenerTodasLasInscripciones());
    }
}