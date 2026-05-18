package com.uniajc.vista;

import java.util.List;
import java.util.Scanner;

import com.uniajc.modelo.Estudiante;

public class VistaEstudiante {

    private Scanner scanner;

    public VistaEstudiante() {
        this.scanner = new Scanner(System.in);
    }

    public Estudiante solicitarDatosEstudiante() {
        // Aquí se podrían implementar métodos para solicitar al usuario que ingrese los datos del estudiante
        // Por ejemplo, utilizando Scanner para leer desde la consola o creando un formulario en una interfaz gráfica

        System.out.println("Ingrese el nombre del estudiante:");
        String nombre = scanner.nextLine();

        System.out.println("Ingrese el apellido del estudiante:");
        String apellido = scanner.nextLine();

        System.out.println("Ingrese la correo del estudiante:");
        String correo = scanner.nextLine();

        return new Estudiante(0, nombre, apellido, correo); // Retornar un objeto Estudiante con los datos ingresados por el usuario
    }

    // La manera sencilla de mostrar los detalles de un estudiante es a través de un método que reciba un objeto Estudiante y acceda a sus propiedades para mostrarlas. Aquí te dejo un ejemplo de cómo podrías implementar esto:
    public void mostrarDetallesEstudiante(Estudiante estudiante) {
        System.out.println("ID: " + estudiante.getId() + ", Nombre: " + estudiante.getNombre() + ", Apellido: " + estudiante.getApellido() + ", Correo: " + estudiante.getEmail());
    }

    public void mostrarTodosLosEstudiantes(List<Estudiante> estudiantes) {
        System.out.println("------");
        System.out.println("Lista de Estudiantes:");
        System.out.println("------");
        for (Estudiante estudiante : estudiantes) {
            mostrarDetallesEstudiante(estudiante);
        }
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

}
