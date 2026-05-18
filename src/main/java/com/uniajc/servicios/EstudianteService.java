package com.uniajc.servicios;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import com.uniajc.dao.EstudianteDao;
import com.uniajc.modelo.Estudiante;

public class EstudianteService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    private final EstudianteDao estudianteDao;

    public EstudianteService(EstudianteDao estudianteDao) {
        this.estudianteDao = estudianteDao;
    }

    public void registrarEstudiante(Estudiante estudiante) {
        validarEstudiante(estudiante);
        
        Optional<Estudiante> existente = estudianteDao.buscarPorEmail(estudiante.getEmail());
        if (existente.isPresent()) {
            throw new IllegalArgumentException("Ya existe un estudiante con el email: " + estudiante.getEmail());
        }

        estudianteDao.guardar(estudiante);
    }

    public List<Estudiante> obtenerTodosLosEstudiantes() {
        return estudianteDao.obtenerTodos();
    }

    public Optional<Estudiante> buscarEstudiantePorId(int id) {
        return estudianteDao.buscarPorId(id);
    }

    public void actualizarEstudiante(Estudiante estudiante) {
        if (estudiante == null) {
            throw new IllegalArgumentException("El estudiante no puede ser nulo.");
        }
        
        if (estudiante.getId() <= 0) {
            throw new IllegalArgumentException("ID de estudiante inválido.");
        }

        Optional<Estudiante> existente = estudianteDao.buscarPorId(estudiante.getId());
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Estudiante no encontrado con ID: " + estudiante.getId());
        }

        validarEstudiante(estudiante);

        Optional<Estudiante> otroConEmail = estudianteDao.buscarPorEmail(estudiante.getEmail());
        if (otroConEmail.isPresent() && otroConEmail.get().getId() != estudiante.getId()) {
            throw new IllegalArgumentException("Ya existe otro estudiante con el email: " + estudiante.getEmail());
        }

        estudianteDao.actualizar(estudiante);
    }

    public void eliminarEstudiante(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID de estudiante inválido.");
        }

        Optional<Estudiante> existente = estudianteDao.buscarPorId(id);
        if (existente.isEmpty()) {
            throw new IllegalArgumentException("Estudiante no encontrado con ID: " + id);
        }

        estudianteDao.eliminar(id);
    }

    private void validarEstudiante(Estudiante estudiante) {
        if (estudiante.getNombre() == null || estudiante.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }

        if (estudiante.getApellido() == null || estudiante.getApellido().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }

        if (estudiante.getEmail() == null || estudiante.getEmail().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio.");
        }

        if (!EMAIL_PATTERN.matcher(estudiante.getEmail()).matches()) {
            throw new IllegalArgumentException("El formato del email es inválido.");
        }
    }
}
