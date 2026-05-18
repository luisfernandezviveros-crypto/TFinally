package com.uniajc;

import javax.swing.SwingUtilities;
import com.uniajc.vista.VistaPrincipalSwing;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> { // Funcion flecha o Funcion lambda para ejecutar el código en el hilo de eventos de Swing
            new VistaPrincipalSwing().setVisible(true);
        });
    }
}
