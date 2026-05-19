package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;

import com.uniajc.dao.MateriaDao;
import com.uniajc.modelo.Materia;

public class MateriaService {

    private final MateriaDao materiaDao;

    public MateriaService(MateriaDao materiaDao) {
        this.materiaDao = materiaDao;
    }

    public void registrarMateria(Materia materia) {
        validar(materia);
        Optional<Materia> existente = materiaDao.buscarPorCodigo(materia.getCodigo());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe una materia con el código: " + materia.getCodigo());
        }
        materiaDao.guardar(materia);
    }

    public List<Materia> obtenerTodasLasMaterias() {
        return materiaDao.obtenerTodos();
    }

    public Optional<Materia> buscarPorId(int id) {
        return materiaDao.buscarPorId(id);
    }

    public void actualizarMateria(Materia materia) {
        if (materia == null) throw new IllegalArgumentException("La materia no puede ser nula.");
        if (materia.getId() <= 0) throw new IllegalArgumentException("ID de materia inválido.");
        if (materiaDao.buscarPorId(materia.getId()).isEmpty()) {
            throw new IllegalArgumentException("Materia no encontrada con ID: " + materia.getId());
        }
        validar(materia);
        Optional<Materia> otro = materiaDao.buscarPorCodigo(materia.getCodigo());
        if (otro.isPresent() && otro.get().getId() != materia.getId()) {
            throw new IllegalArgumentException("Ya existe otra materia con el código: " + materia.getCodigo());
        }
        materiaDao.actualizar(materia);
    }

    public void eliminarMateria(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID de materia inválido.");
        if (materiaDao.buscarPorId(id).isEmpty()) {
            throw new IllegalArgumentException("Materia no encontrada con ID: " + id);
        }
        materiaDao.eliminar(id);
    }

    private void validar(Materia m) {
        if (m.getNombre() == null || m.getNombre().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (m.getCodigo() == null || m.getCodigo().isEmpty())
            throw new IllegalArgumentException("El código es obligatorio.");
        if (m.getCreditos() <= 0)
            throw new IllegalArgumentException("Los créditos deben ser un número positivo.");
    }
}
