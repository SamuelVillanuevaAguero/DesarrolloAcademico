/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vistas;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import utilerias.general.ControladorGeneral;

/**
 * FXML Controller class
 *
 * @author Samue
 */
public class ImportacionArchivosController implements Initializable {
    
    
    private File programaCapacitacion;  
    private File listado;  
    private File formato;  
    /**
     * Initializes the controller class.
     */
    @FXML
    private Button botonCerrar;
    @FXML
    private Button botonMinimizar;
    @FXML
    private Button botonRegresar;
    @FXML
    private Button botonPC;
    @FXML
    private Button botonListado;
    @FXML
    private Button botonFormato;
    @FXML
    private ComboBox<String> comboBoxListados;
    @FXML
    private ComboBox<String> comboBoxFormatos;
    @FXML
    private Label labelPC;
    @FXML
    private Label labelListado;
    @FXML
    private Label labelFormato;

    //Métodos de los botones de la barra superior :)
    public void cerrarVentana(MouseEvent event) throws IOException {
        ControladorGeneral.cerrarVentana(event, "¿Quieres cerrar sesión?", getClass());
    }

    public void minimizarVentana(MouseEvent event) {
        ControladorGeneral.minimizarVentana(event);
    }

    public void regresarVentana(MouseEvent event) throws IOException {
        ControladorGeneral.regresar(event, "Principal", getClass());
    }

    public void cargarProgramaCap(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Excel", "*.xlsx", "*.xls")
        );

        programaCapacitacion = fileChooser.showOpenDialog(botonPC.getScene().getWindow());
        if (programaCapacitacion != null) {
            labelPC.setText(programaCapacitacion.getName());
            // Aquí puedes procesar el archivo seleccionado  
        }
    }
    
    public void cargarListado(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Excel", "*.xlsx", "*.xls")
        );

        listado = fileChooser.showOpenDialog(botonPC.getScene().getWindow());
        if (listado != null) {
            labelListado.setText(listado.getName());
            // Aquí puedes procesar el archivo seleccionado  
        }
    }
    
    public void cargarFormato(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Excel", "*.xlsx", "*.xls")
        );

        formato = fileChooser.showOpenDialog(botonPC.getScene().getWindow());
        if (formato != null) {
            labelFormato.setText(formato.getName());
            // Aquí puedes procesar el archivo seleccionado  
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        botonCerrar.setOnMouseClicked(event -> {
            try {
                cerrarVentana(event);
            } catch (IOException ex) {
                Logger.getLogger(BusquedaEstadisticaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        botonMinimizar.setOnMouseClicked(event -> {
            minimizarVentana(event);
        });

        botonRegresar.setOnMouseClicked(event -> {
            try {
                regresarVentana(event);
            } catch (IOException ex) {
                Logger.getLogger(BusquedaEstadisticaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        comboBoxListados.setItems(FXCollections.observableArrayList("Listado de cursos de capacitación a impartir con folio",
                "Listado de inscritos a cursos de capacitación", "Listado de necesidad de capacitación"));

        comboBoxFormatos.setItems(FXCollections.observableArrayList("Formato de hojas membretadas para reconocimientos",
                "Formato de hojas de reportes estadísticos docentes", "Formato de lista de asistencias",
                "Formato de reporte para docentes con necesidad de capacitación"));

        botonPC.setOnMouseClicked(event -> {
            cargarProgramaCap(event);
        });
        
        botonListado.setOnMouseClicked(event -> {
            cargarListado(event);
        });
        
        botonFormato.setOnMouseClicked(event -> {
            cargarFormato(event);
        });
    }

}
