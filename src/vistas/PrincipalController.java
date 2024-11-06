/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vistas;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import utilerias.general.ControladorGeneral;

/**
 * FXML Controller class
 *
 * @author Samue
 */
public class PrincipalController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button botonCerrar;
    @FXML
    private Button botonMinimizar;
    
    //Botones que redireccionan
    @FXML
    private Pane botonImportacion;
    
    @FXML
    private Pane botonVisualizacion;
    
    @FXML
    private Pane botonBusqueda;
    
    @FXML
    private Pane botonModificacion;
    
    @FXML
    private Pane botonExportacion;
    
    @FXML
    private Pane botonRespaldo;
    
    
    //Métodos de los botones de la barra superior :)
    public void cerrarVentana(MouseEvent event) throws IOException{
        ControladorGeneral.cerrarVentana(event, "¿Quieres cerrar sesión?", getClass());
    }
    
    public void minimizarVentana(MouseEvent event){
        ControladorGeneral.minimizarVentana(event);
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
        
        // VISTA UNO ()
        botonImportacion.setOnMouseClicked(event -> {
            try {
                ControladorGeneral.regresar(event,"ImportacionArchivos", getClass());
            } catch (IOException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        // VISTA DOS
        botonVisualizacion.setOnMouseClicked(event -> {
            try {
                ControladorGeneral.regresar(event, "VizualizacionDatos", getClass());
            } catch (IOException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        //VISTA TRES
        botonBusqueda.setOnMouseClicked(event ->{
            try {
                ControladorGeneral.regresar(event, "BusquedaEstadistica", getClass());
            } catch (IOException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        //VISTA CUATRO
        botonModificacion.setOnMouseClicked(event ->{
            try {
                ControladorGeneral.regresar(event, "ModificacionDatos", getClass());
            } catch (IOException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //VISTA CINCO
        botonExportacion.setOnMouseClicked(event -> {
            try {
                ControladorGeneral.regresar(event, "ExportacionReconocimientos", getClass());
            } catch (IOException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
       
        //VISTA SEIS
        botonRespaldo.setOnMouseClicked(event ->{
            try {
                ControladorGeneral.regresar(event, "Respaldo", getClass());
            } catch (IOException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }   
    
}
