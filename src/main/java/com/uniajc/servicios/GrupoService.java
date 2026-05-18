package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;

import com.uniajc.dao.DocenteDao;
import com.uniajc.dao.GrupoDao;
import com.uniajc.dao.MateriaDao;
import com.uniajc.modelo.Grupo;

public class GrupoService {

    private final GrupoDao grupoDao;
    private final DocenteDao docenteDao;
    private final MateriaDao materiaDao;

    public GrupoService(GrupoDao grupoDao, DocenteDao docenteDao, MateriaDao materiaDao) {
        this.grupoDao = grupoDao;
        this.docenteDao = docenteDao;
        this.materiaDao = materiaDao;
    }

    public void registrarGrupo(Grupo grupo) {
        validar(grupo);
        grupoDao.guardar(grupo);
    }

    public List<Grupo> obtenerTodos() {
        return grupoDao.obtenerTodos();
    }

    public Optional<Grupo> buscarPorId(int id) {
        return grupoDao.buscarPorId(id);
    }

    public void actualizarGrupo(Grupo grupo) {
        if (grupo == null) throw new IllegalArgumentException("El grupo no puede ser nulo.");
        if (grupo.getId() <= 0) throw new IllegalArgumentException("ID de grupo inválido.");
        if (grupoDao.buscarPorId(grupo.getId()).isEmpty()) {
            throw new IllegalArgumentException("Grupo no encontrado con ID: " + grupo.getId());
        }
        validar(grupo);
        grupoDao.actualizar(grupo);
    }

    public void eliminarGrupo(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID de grupo inválido.");
        if (grupoDao.buscarPorId(id).isEmpty()) {
            throw new IllegalArgumentException("Grupo no encontrado con ID: " + id);
        }
        grupoDao.eliminar(id);
    }

    private void validar(Grupo g) {
        if (g.getNombre() == null || g.getNombre().isEmpty())
            throw new IllegalArgumentException("El nombre del grupo es obligatorio.");
        if (g.getDocenteId() <= 0)
            throw new IllegalArgumentException("Debe seleccionar un docente válido.");
        if (g.getMateriaId() <= 0)
            throw new IllegalArgumentException("Debe seleccionar una materia válida.");
        if (docenteDao.buscarPorId(g.getDocenteId()).isEmpty())
            throw new IllegalArgumentException("El docente seleccionado no existe.");
        if (materiaDao.buscarPorId(g.getMateriaId()).isEmpty())
            throw new IllegalArgumentException("La materia seleccionada no existe.");
    }
}
