package com.uniajc.vista;

import java.util.List;
import com.uniajc.modelo.Estudiante;

public interface VistaEstudiante {
    Estudiante solicitarDatosEstudiante();
    void mostrarTodosLosEstudiantes(List<Estudiante> estudiantes);
    void mostrarMensaje(String mensaje);
}
