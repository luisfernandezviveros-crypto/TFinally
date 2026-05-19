package com.uniajc.vista;

import java.util.List;
import com.uniajc.modelo.Materia;

public interface VistaMateria {
    Materia solicitarDatosMateria();
    void mostrarTodasLasMaterias(List<Materia> materias);
    void mostrarMensaje(String mensaje);
}
