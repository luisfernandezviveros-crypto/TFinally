package com.uniajc.controlador;

import com.uniajc.modelo.Grupo;
import com.uniajc.servicios.GrupoService;
import com.uniajc.vista.VistaGrupo;

public class ControladorGrupo {

    private VistaGrupo vista;
    private GrupoService servicio;

    public ControladorGrupo(VistaGrupo vista, GrupoService servicio) {
        this.vista = vista;
        this.servicio = servicio;
    }

    public void registrarGrupo() {

        try {
            Grupo grupo = vista.solicitarDatosGrupo();

            if (grupo != null) {
                servicio.registrarGrupo(grupo);
                vista.mostrarMensaje("Grupo registrado exitosamente.");
            }

        } catch (Exception e) {
            vista.mostrarMensaje("Error al registrar el grupo.");
        }
    }

    public void mostrarTodosLosGrupos() {
        vista.mostrarTodosLosGrupos(servicio.obtenerTodosLosGrupos());
    }
