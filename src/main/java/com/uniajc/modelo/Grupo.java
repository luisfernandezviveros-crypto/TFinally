package com.uniajc.modelo;

public class Grupo {

    private int id;
    private String nombre;
    private int docenteId;
    private int materiaId;
    // Campos de apoyo para mostrar en la vista
    private String docenteNombre;
    private String materiaNombre;

    public Grupo() { }

    public Grupo(int id, String nombre, int docenteId, int materiaId) {
        this.id = id;
        this.nombre = nombre;
        this.docenteId = docenteId;
        this.materiaId = materiaId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getDocenteId() { return docenteId; }
    public void setDocenteId(int docenteId) { this.docenteId = docenteId; }

    public int getMateriaId() { return materiaId; }
    public void setMateriaId(int materiaId) { this.materiaId = materiaId; }

    public String getDocenteNombre() { return docenteNombre; }
    public void setDocenteNombre(String docenteNombre) { this.docenteNombre = docenteNombre; }

    public String getMateriaNombre() { return materiaNombre; }
    public void setMateriaNombre(String materiaNombre) { this.materiaNombre = materiaNombre; }
}
