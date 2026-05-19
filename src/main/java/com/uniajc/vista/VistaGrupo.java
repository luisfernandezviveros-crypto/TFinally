package com.uniajc.vista;

import java.util.List;
import com.uniajc.modelo.Grupo;

public interface VistaGrupo {
    Grupo solicitarDatosGrupo();
    void mostrarTodosLosGrupos(List<Grupo> grupos);
    void mostrarMensaje(String mensaje);
}
