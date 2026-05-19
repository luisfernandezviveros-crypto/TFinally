package com.uniajc.vista;

import java.util.List;
import com.uniajc.modelo.Docente;

public interface VistaDocente {
    Docente solicitarDatosDocente();
    void mostrarTodosLosDocentes(List<Docente> docentes);
    void mostrarMensaje(String mensaje);
}
