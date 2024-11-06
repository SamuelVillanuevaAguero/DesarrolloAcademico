

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vistas;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utilerias.general.ControladorGeneral;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import java.util.Optional;

/**
 * FXML Controller class
 *
 * @author Samue
 */
public class ModificacionDatosController implements Initializable {

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
    private Button botonCancelar;
    @FXML
    private Button botonGuardar;
    @FXML
    private Button botonLimpiar;
    @FXML
    private TextField directorField;
    @FXML
    private TextField coordinadorField;
    @FXML
    private TextField jefeDeptoField;
    @FXML
    private TextField totalDocentesField;

    //Métodos de los botones de la barra superior :)
    

public void cerrarVentana(MouseEvent event) {
    Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
    confirmacion.setTitle("Confirmación");
    confirmacion.setHeaderText(null);
    confirmacion.setContentText("¿Quieres cerrar sesión?");

    Optional<ButtonType> resultado = confirmacion.showAndWait();
    if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}



    public void minimizarVentana(MouseEvent event) {
        ControladorGeneral.minimizarVentana(event);
    }

    public void regresarVentana(MouseEvent event) throws IOException {
        ControladorGeneral.regresar(event, "Principal", getClass());
    }

    public void cancelarVentana(MouseEvent event) {
        System.out.println("Botón Cancelar presionado"); 
        ((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
    }

    public void limpiarCampos() {
        directorField.clear();
        coordinadorField.clear();
        jefeDeptoField.clear();
        totalDocentesField.clear();
        
        
    }
    
    // private static final String FILE_PATH = "C:\\Users\\TUF\\Documents\\NetBeansProjects\\DesarrolloAcademico\\DatosEjemplo.xlsx"; // Cambia esto por la ruta real de tu archivo Excel
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Otros botones
        botonCerrar.setOnMouseClicked(event -> {
            cerrarVentana(event);
        });

        botonMinimizar.setOnMouseClicked(this::minimizarVentana);
        botonRegresar.setOnMouseClicked(event -> {
            try {
                regresarVentana(event);
            } catch (IOException ex) {
                Logger.getLogger(BusquedaEstadisticaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        botonLimpiar.setOnMouseClicked(event -> limpiarCampos());
        // botonGuardar.setOnMouseClicked(event -> guardarDatos());

        // Configura el evento del botón "Cancelar" para que cierre la ventana
        botonCancelar.setOnMouseClicked(event ->cancelarVentana(event));
       // cargarDatosDesdeExcel();
    }

}

