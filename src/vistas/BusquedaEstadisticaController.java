/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vistas;

import java.io.IOException;
import java.net.URL;
import java.time.Year;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utilerias.general.ControladorGeneral;


/**
 * FXML Controller class
 *
 * @author Samue
 */
public class BusquedaEstadisticaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    //Botones (Button)
    @FXML
    private Button botonCerrar;
    @FXML
    private Button botonMinimizar;
    @FXML
    private Button botonRegresar;
    @FXML
    private Label botonActualizar;
    
    //Listas de opciones (ComboBox)
    @FXML
    private ComboBox comboTipoCapacitacion;
    @FXML
    private ComboBox comboDepartamento;
    @FXML
    private ComboBox comboAcreditacion;
    @FXML
    private ComboBox comboNivel;
    @FXML
    private ComboBox comboAño;
    @FXML
    private ComboBox comboPeriodo;
    @FXML
    private ComboBox comboFormato;
    
    //Métodos de los botones de la barra superior :)
    public void cerrarVentana(MouseEvent event) throws IOException{
        ControladorGeneral.cerrarVentana(event, "¿Quieres cerrar sesión?", getClass());
    }
    
    public void minimizarVentana(MouseEvent event){
        ControladorGeneral.minimizarVentana(event);
    }
    
    public void regresarVentana(MouseEvent event)throws IOException{
        ControladorGeneral.regresar(event, "Principal", getClass());
    }
    
    public void actualizarDocumentos(MouseEvent event) throws IOException{
        ControladorGeneral.regresar(event, "ImportacionArchivos", getClass());
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
        
        botonActualizar.setOnMouseClicked(event -> {
            try {
                actualizarDocumentos(event);
            } catch (IOException ex) {
                Logger.getLogger(BusquedaEstadisticaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        //Inicializar las opciones de los ComboBoxes
        comboTipoCapacitacion.getItems().addAll("Actualización profesional","Formación docente");
        comboDepartamento.getItems().addAll("Ciencias Básicas", "Ciencias Economico Administrativo",
                                            "Ciencias de la tierra", "Ingeniería Industrial", "Metal Mecánica", 
                                            "Química y Bioquímica", "Sistemas Computacionales");
        comboAcreditacion.getItems().addAll("Si", "No", "Ambos");
        comboNivel.getItems().addAll("Pendiente....");
        comboAño.getItems().addAll("2020");
        
        //Combo de años (los ultimos 10 años)
        int currentYear = Year.now().getValue();

        // Crear una lista con el año actual y los últimos 10 años
        List<Integer> years = IntStream.rangeClosed(currentYear - 10, currentYear)
                                       .boxed()
                                       .sorted((a, b) -> b - a) // Orden descendente
                                       .collect(Collectors.toList());
        comboPeriodo.getItems().addAll(years);
        comboFormato.getItems().addAll("PDF", "EXCEL");
    }

}
