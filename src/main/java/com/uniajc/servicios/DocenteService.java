package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import com.uniajc.dao.DocenteDao;
import com.uniajc.modelo.Docente;

public class DocenteService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private final DocenteDao docenteDao;

    public DocenteService(DocenteDao docenteDao) {
        this.docenteDao = docenteDao;
    }

    public void registrarDocente(Docente docente) {
        validar(docente);
        Optional<Docente> existente = docenteDao.buscarPorEmail(docente.getEmail());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un docente con el email: " + docente.getEmail());
        }
        docenteDao.guardar(docente);
    }

    public List<Docente> obtenerTodos() {
        return docenteDao.obtenerTodos();
    }

    public Optional<Docente> buscarPorId(int id) {
        return docenteDao.buscarPorId(id);
    }

    public void actualizarDocente(Docente docente) {
        if (docente == null) throw new IllegalArgumentException("El docente no puede ser nulo.");
        if (docente.getId() <= 0) throw new IllegalArgumentException("ID de docente inválido.");

        if (docenteDao.buscarPorId(docente.getId()).isEmpty()) {
            throw new IllegalArgumentException("Docente no encontrado con ID: " + docente.getId());
        }
        validar(docente);

        Optional<Docente> otro = docenteDao.buscarPorEmail(docente.getEmail());
        if (otro.isPresent() && otro.get().getId() != docente.getId()) {
            throw new IllegalArgumentException("Ya existe otro docente con el email: " + docente.getEmail());
        }
        docenteDao.actualizar(docente);
    }

    public void eliminarDocente(int id) {
        if (id <= 0) throw new IllegalArgumentException("ID de docente inválido.");
        if (docenteDao.buscarPorId(id).isEmpty()) {
            throw new IllegalArgumentException("Docente no encontrado con ID: " + id);
        }
        docenteDao.eliminar(id);
    }

    private void validar(Docente d) {
        if (d.getNombre() == null || d.getNombre().isEmpty())
            throw new IllegalArgumentException("El nombre es obligatorio.");
        if (d.getApellido() == null || d.getApellido().isEmpty())
            throw new IllegalArgumentException("El apellido es obligatorio.");
        if (d.getEmail() == null || d.getEmail().isEmpty())
            throw new IllegalArgumentException("El email es obligatorio.");
        if (!EMAIL_PATTERN.matcher(d.getEmail()).matches())
            throw new IllegalArgumentException("El formato del email es inválido.");
        if (d.getEspecialidad() == null || d.getEspecialidad().isEmpty())
            throw new IllegalArgumentException("La especialidad es obligatoria.");
    }
}
