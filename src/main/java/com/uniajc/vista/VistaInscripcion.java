package com.uniajc.vista;

import java.util.List;
import com.uniajc.modelo.Inscripcion;

public interface VistaInscripcion {
    Inscripcion solicitarDatosInscripcion();
    void mostrarTodasLasInscripciones(List<Inscripcion> inscripciones);
    void mostrarMensaje(String mensaje);
}
