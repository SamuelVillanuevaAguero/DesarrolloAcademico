/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vistas;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import utilerias.general.ControladorGeneral;

/**
 * FXML Controller class
 *
 * @author Samue
 */
public class VizualizacionDatosController implements Initializable {

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
    private Button botonExportar;
    @FXML
    private Button botonActualizar;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colApellidoPat;
    @FXML
    private TableColumn<?, ?> colApellidoMat;
    @FXML
    private TableColumn<?, ?> colRFC;
    @FXML
    private TableColumn<?, ?> colSexo;
    @FXML
    private TableColumn<?, ?> colNombreCurso;
    @FXML
    private TableColumn<?, ?> colFecha;
    @FXML
    private TableColumn<?, ?> colInicio;
    @FXML
    private TableColumn<?, ?> colFin;
    @FXML
    private TableColumn<?, ?> colDepartamento;
    @FXML
    private TableColumn<?, ?> colLicenciatura;
    @FXML
    private TableColumn<?, ?> colPosgrado;
    @FXML
    private TableColumn<?, ?> colPuesto;
    @FXML
    private TableColumn<?, ?> colInstructor;
    @FXML
    private TableColumn<?, ?> colCapacitacion;
    @FXML
    private TableColumn<?, ?> colActDocente;
    @FXML
    private TableColumn<?, ?> colFormDocente;
    @FXML
    private TableColumn<?, ?> colAcreditacion;
    @FXML
    private Button botonModificar;
    @FXML
    private Button botonGuardar;

    
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
          
    }  

    @FXML
    private void Exportar(ActionEvent event) {
        
    }

    @FXML
    private void actuzalizar(ActionEvent event) {
    }

    @FXML
    private void modificar(ActionEvent event) {
    }

    @FXML
    private void guardar(ActionEvent event) {
                // Lógica para guardar los datos
        boolean guardadoExitoso = guardarDatos();

        // Mostrar mensaje de confirmación si se guarda correctamente
        if (guardadoExitoso) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Confirmación de Guardado");
            alert.setHeaderText(null);
            alert.setContentText("Los datos han sido guardados correctamente.");
            alert.showAndWait();
        } else {
            // Mostrar mensaje de error en caso de falla
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error al Guardar");
            alert.setHeaderText(null);
            alert.setContentText("Hubo un error al guardar los datos.");
            alert.showAndWait();
        }
    }

    private boolean guardarDatos() {
        try {
            // Aquí agregas la lógica de guardado
            // Ejemplo de lógica de guardado en un archivo de texto
            // Puedes reemplazarlo con lógica de guardado en base de datos o Excel
            FileWriter fileWriter = new FileWriter("datos_guardados.txt", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            // Suponiendo que tienes datos que quieres guardar, escribe aquí el contenido
            printWriter.println("Ejemplo de dato guardado");

            printWriter.close(); // Cierra el archivo después de guardar

            return true; // Indica que se guardó correctamente

        } catch (IOException e) {
            e.printStackTrace(); // Imprime el error para depuración
            return false; // Indica que hubo un error al guardar
        }
    }
    
}
