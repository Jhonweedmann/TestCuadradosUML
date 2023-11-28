package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class PaintSimuladoConLimpiar extends JFrame {

    private ArrayList<Rectangulo> rectangulos;
    private Rectangulo rectanguloActual;

    public PaintSimuladoConLimpiar() {
        setTitle("Paint Simulado");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        rectangulos = cargarRectangulosDesdeArchivo(); // Cargar rectángulos desde el archivo

        MiPanel panel = new MiPanel();
        add(panel);

        JButton limpiarButton = new JButton("Limpiar");
        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rectangulos.clear();
                guardarRectangulosEnArchivo(); // Guardar rectángulos en el archivo
                panel.repaint();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(limpiarButton);
        add(buttonPanel, BorderLayout.SOUTH);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                rectanguloActual = new Rectangulo(e.getX(), e.getY(), 0, 0);
                rectangulos.add(rectanguloActual);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                rectanguloActual = null;
                guardarRectangulosEnArchivo(); // Guardar rectángulos en el archivo
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (rectanguloActual != null) {
                    int width = e.getX() - rectanguloActual.getX();
                    int height = e.getY() - rectanguloActual.getY();
                    rectanguloActual.setWidth(width);
                    rectanguloActual.setHeight(height);
                    repaint();
                }
            }
        });

        setVisible(true);
    }

    private class MiPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Rectangulo r : rectangulos) {
                r.dibujar(g);
            }
        }
    }

    private class Rectangulo implements Serializable {
        private int x, y, width, height;

        public Rectangulo(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public void dibujar(Graphics g) {
            g.drawRect(x, y, width, height);
        }
    }

    private void guardarRectangulosEnArchivo() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("rectangulos.ser"))) {
            out.writeObject(rectangulos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Rectangulo> cargarRectangulosDesdeArchivo() {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("rectangulos.ser"))) {
            return (ArrayList<Rectangulo>) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // Si no se puede cargar, retornar una nueva lista
            return new ArrayList<>();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PaintSimuladoConLimpiar());
    }
}