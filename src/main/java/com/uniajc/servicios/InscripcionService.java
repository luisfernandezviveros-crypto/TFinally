package com.uniajc.servicios;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.uniajc.dao.EstudianteDao;
import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.InscripcionDao;
import com.uniajc.modelo.Inscripcion;

public class InscripcionService {

    private final InscripcionDao inscripcionDao;
    private final EstudianteDao estudianteDao;
    private final GrupoDao grupoDao;

    public InscripcionService(InscripcionDao inscripcionDao, EstudianteDao estudianteDao, GrupoDao grupoDao) {
        this.inscripcionDao = inscripcionDao;
        this.estudianteDao = estudianteDao;
        this.grupoDao = grupoDao;
    }

    public void registrarInscripcion(Inscripcion inscripcion) {
        validar(inscripcion);
        if (inscripcionDao.existeInscripcion(inscripcion.getEstudianteId(), inscripcion.getGrupoId())) {
            throw new IllegalArgumentException("El estudiante ya está inscrito en ese grupo.");
        }
        if (inscripcion.getFecha() == null || inscripcion.getFecha().isEmpty()) {
            inscripcion.setFecha(LocalDate.now().toString());
        }
        inscripcionDao.guardar(inscripcion);
    }

    public List<Inscripcion> obtenerTodasLasInscripciones() {
        return inscripcionDao.obtenerTodos();
    }

    public Optional<Inscripcion> buscarPorId(int id) {
        return inscripcionDao.buscarPorId(id);
    }

    public void eliminarInscripcion(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID de inscripción inválido.");
        if (inscripcionDao.buscarPorId(id).isEmpty()) {
            throw new IllegalArgumentException("Inscripción no encontrada con ID: " + id);
        }
        inscripcionDao.eliminar(id);
    }

    private void validar(Inscripcion i) {
        if (i.getEstudianteId() <= 0)
            throw new IllegalArgumentException("Debe seleccionar un estudiante válido.");
        if (i.getGrupoId() <= 0)
            throw new IllegalArgumentException("Debe seleccionar un grupo válido.");
        if (estudianteDao.buscarPorId(i.getEstudianteId()).isEmpty())
            throw new IllegalArgumentException("El estudiante seleccionado no existe.");
        if (grupoDao.buscarPorId(i.getGrupoId()).isEmpty())
            throw new IllegalArgumentException("El grupo seleccionado no existe.");
    }
}
