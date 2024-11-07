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
public class ExportacionReconocimientosController implements Initializable {

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
    private Button botonBuscar;
    @FXML
    private Button botonModificar;
    @FXML
    private Button botonGuardar;
    @FXML
    private RadioButton radiobutonsi;
    @FXML
    private RadioButton radiobutonno;
    @FXML
    private TextField txtfolio;
    @FXML
    private TextField txtcodigodelcurso;
    @FXML
    private TextField txtNombreCurso;
    @FXML
    private TextField txtNombreInstructor;
    @FXML
    private TextField txtCompetencias;
    @FXML
    private TextField txtFechaCurso;
    @FXML
    private ComboBox<String> txtSemanas;
    @FXML
    private ComboBox<String> txtFormatos;
    @FXML
    private Button botonLimpiar;

    
    
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
        
        // para mostrar las semanas
        for (int i = 1; i <= 20; i++) {
        txtSemanas.getItems().add(String.valueOf(i));
        }
        // para mostrar los formatos
        txtFormatos.getItems().addAll("PDF", "Word", "Ambos");
        
        
        // Inicializar el campo txtfolio como deshabilitado y oculto
        txtfolio.setVisible(false);
        txtfolio.setDisable(true);
        
        // Configurar eventos para los RadioButtons
        radiobutonsi.setOnAction(this::clicksi);
        radiobutonno.setOnAction(this::clicno);
        
        // desabilitar txfields para el inicio
        txtcodigodelcurso.setDisable(true);
        txtNombreCurso.setDisable(true);
        txtFechaCurso.setDisable(true);
        txtSemanas.setDisable(true);
        txtNombreInstructor.setDisable(true);
        txtCompetencias.setDisable(true);
        txtFormatos.setDisable(true);

    }

    @FXML
    private void buscarCurso(ActionEvent event) {
        txtFormatos.setDisable(false); // Habilitar campo formatos
        String folio = txtfolio.getText().trim();
        String codigoCurso = txtcodigodelcurso.getText().trim();

    // Verifica si se ha ingresado un folio o un código del curso
    if (radiobutonsi.isSelected() && !folio.isEmpty()) {
        // Lógica de búsqueda basada en el folio
        txtcodigodelcurso.setText("JAVA2024");  
        txtNombreCurso.setText("Curso de Java Avanzado"); // Mostrar el nombre del curso
        txtFechaCurso.setText("01/01/2024");
        txtNombreInstructor.setText("Juan Pérez");
        txtCompetencias.setText("Programación avanzada en Java");
        System.out.println("Datos encontrados y mostrados en la interfaz usando el folio.");
    } else if (radiobutonno.isSelected() && !codigoCurso.isEmpty()) {
        // Lógica de búsqueda basada en el código del curso
        txtNombreCurso.setText("Curso de Java Avanzado"); // Mostrar el nombre del curso
        txtFechaCurso.setText("01/01/2024");
        txtNombreInstructor.setText("Juan Pérez");
        txtCompetencias.setText("Programación avanzada en Java");
        System.out.println("Datos encontrados y mostrados en la interfaz usando el código del curso.");
    } else {
        // Mensaje si no se ingresa un valor
        System.out.println("Ingrese un folio o código del curso para buscar.");
    }
    }

    @FXML
    private void modificarDatos(ActionEvent event) {
        txtFechaCurso.setDisable(false);     // Habilitar campo para edición    
        txtNombreInstructor.setDisable(false); // Habilitar campo para edición
        txtCompetencias.setDisable(false);   // Habilitar campo para edición
        txtSemanas.setDisable(false); // Habilitar campo semanas
        
         // Mantener deshabilitados los campos no editables
    txtNombreCurso.setDisable(true);
    txtcodigodelcurso.setDisable(true);
    }

    @FXML
    private void exportarReconocimientos(ActionEvent event) {
    }

    @FXML
    private void guardarDatos(ActionEvent event) {
         // Crear una alerta de confirmación
    Alert confirmacion = new Alert(AlertType.CONFIRMATION);
    confirmacion.setTitle("Confirmación de guardado");
    confirmacion.setHeaderText("¿Está seguro de guardar los cambios?");

    // Mostrar la alerta y esperar a que el usuario responda
    confirmacion.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
            // Guardar cambios (simulación)
            System.out.println("Cambios guardados exitosamente.");
            
            // Crear alerta de éxito
            Alert exito = new Alert(AlertType.INFORMATION);
            exito.setTitle("Éxito");
            exito.setHeaderText(null);
            exito.setContentText("Los cambios se guardaron con éxito.");
            exito.showAndWait();

            // Deshabilitar nuevamente los campos
            txtFechaCurso.setDisable(true);
            txtNombreInstructor.setDisable(true);
            txtCompetencias.setDisable(true);
        } else {
            System.out.println("Guardado cancelado.");
        }
    });
    }

    @FXML
    private void clicksi(ActionEvent event) {
        if (radiobutonsi.isSelected()) {
            txtfolio.setVisible(true); //Mostrar campo
            txtfolio.setDisable(false);   // Habilitar el campo txtfolio para escritura
            txtcodigodelcurso.setDisable(true); // Deshabilitar el campo txtcodigodelcurso
            txtcodigodelcurso.clear();          // Limpiar el campo txtcodigodelcurso
        }
    }

    @FXML
    private void clicno(ActionEvent event) {
        if (radiobutonno.isSelected()) {
            txtfolio.setDisable(true);          // Deshabilitar el campo txtfolio
            txtfolio.setVisible(false); //Deshabilitar campo
            txtfolio.clear();                   // Limpiar el campo txtfolio
            txtcodigodelcurso.setDisable(false); // Habilitar el campo txtcodigodelcurso para escritura
        }
    }

    @FXML
    private void escribirfolio(ActionEvent event) {
    }

    @FXML
    private void limpiarCampos(ActionEvent event) {
        // Limpiar todos los TextFields
    txtfolio.clear();
    txtcodigodelcurso.clear();
    txtNombreCurso.clear();
    txtNombreInstructor.clear();
    txtCompetencias.clear();
    txtFechaCurso.clear();

    // Restablecer ComboBoxes
    txtSemanas.getSelectionModel().clearSelection();
    txtFormatos.getSelectionModel().clearSelection();

    // Restablecer RadioButtons
    radiobutonsi.setSelected(false);
    radiobutonno.setSelected(false);

    // Deshabilitar campos que deben estar desactivados al inicio
    txtfolio.setVisible(false);
    txtfolio.setDisable(true);
    txtcodigodelcurso.setDisable(true);
    txtNombreCurso.setDisable(true);
    txtFechaCurso.setDisable(true);
    txtSemanas.setDisable(true);
    txtFormatos.setDisable(true);
    txtNombreInstructor.setDisable(true);
    txtCompetencias.setDisable(true);
    }
}
