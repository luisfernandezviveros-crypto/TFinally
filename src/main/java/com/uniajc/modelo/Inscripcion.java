package com.uniajc.modelo;

public class Inscripcion {

    private int id;
    private int estudianteId;
    private int grupoId;
    private String fecha;
    // Campos de apoyo para mostrar en la vista
    private String estudianteNombre;
    private String grupoNombre;

    public Inscripcion() { }

    public Inscripcion(int id, int estudianteId, int grupoId, String fecha) {
        this.id = id;
        this.estudianteId = estudianteId;
        this.grupoId = grupoId;
        this.fecha = fecha;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEstudianteId() { return estudianteId; }
    public void setEstudianteId(int estudianteId) { this.estudianteId = estudianteId; }

    public int getGrupoId() { return grupoId; }
    public void setGrupoId(int grupoId) { this.grupoId = grupoId; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstudianteNombre() { return estudianteNombre; }
    public void setEstudianteNombre(String estudianteNombre) { this.estudianteNombre = estudianteNombre; }

    public String getGrupoNombre() { return grupoNombre; }
    public void setGrupoNombre(String grupoNombre) { this.grupoNombre = grupoNombre; }
}
