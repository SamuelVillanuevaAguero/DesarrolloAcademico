/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vistas;

import java.io.FileOutputStream;
import java.io.IOException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
import javafx.event.Event;

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
    private Spinner<Integer> totalDocentesField; 

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
        // Verificar si hay datos en los campos
        boolean hayDatos = !directorField.getText().isEmpty() || !coordinadorField.getText().isEmpty()
                || !jefeDeptoField.getText().isEmpty() || totalDocentesField.getValue() !=null;

        // Si hay datos, mostrar mensaje de confirmación para guardar
        if (hayDatos) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmación");
            confirmacion.setHeaderText("¿Deseas guardar los datos?");
            confirmacion.setContentText("Tienes datos ingresados en los campos. ¿Deseas guardarlos antes de regresar?");

            ButtonType botonGuardar = new ButtonType("Guardar");
            ButtonType botonNoGuardar = new ButtonType("No Guardar");
            ButtonType botonCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

            confirmacion.getButtonTypes().setAll(botonGuardar, botonNoGuardar, botonCancelar);
            Optional<ButtonType> resultado = confirmacion.showAndWait();

            if (resultado.isPresent()) {
                if (resultado.get() == botonGuardar) {
                    guardarDatosEnExcel(event); // Llamar a método de guardar
                    ControladorGeneral.regresar(event, "Principal", getClass()); // Regresar a la ventana principal
                } else if (resultado.get() == botonNoGuardar) {
                    ControladorGeneral.regresar(event, "Principal", getClass()); // Regresar sin guardar
                }
                // Si selecciona cancelar, no se realiza ninguna acción adicional
            }
        } else {
            // Si no hay datos, regresar sin mostrar mensaje
            ControladorGeneral.regresar(event, "Principal", getClass());
        }
    }

    

    public void limpiarCampos() {
        directorField.clear();
        coordinadorField.clear();
        jefeDeptoField.clear();
        totalDocentesField.getValueFactory().setValue(0); // Restablecer Spinner

    }

    // Método para guardar los datos en un archivo Excel
    public void guardarDatosEnExcel(MouseEvent event) {

        if (!validarCampos()) {
            return; // Detener si la validación falla
        }
        // Define la ruta donde se guardará el archivo (cámbiala según sea necesario)
        String rutaArchivo = "C:/Users/TUF/Documents/Admin/Excel/Informacion.xlsx";

        // Crear un nuevo libro de Excel
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Datos");

        // Crear filas y celdas, y asignarles valores desde los TextFields
        Row row1 = sheet.createRow(0);
        row1.createCell(0).setCellValue(directorField.getText());

        Row row2 = sheet.createRow(1);
        row2.createCell(0).setCellValue(coordinadorField.getText());

        Row row3 = sheet.createRow(2);
        row3.createCell(0).setCellValue(jefeDeptoField.getText());

        Row row4 = sheet.createRow(3);
        row4.createCell(0).setCellValue(totalDocentesField.getValue()); // Obtener valor del Spinner


        // Intentar guardar el archivo en la ruta especificada
        try (FileOutputStream fileOut = new FileOutputStream(rutaArchivo)) {
            workbook.write(fileOut);
            mostrarAlerta("Éxito", " Datos guardados exitosamente" + rutaArchivo, AlertType.INFORMATION);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo guardar el archivo: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Método para mostrar alertas en la aplicación
    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Método para validar los campos de entrada
    private boolean validarCampos() {
        // Validar que los campos de nombres solo contengan letras
        if (!directorField.getText().matches("[a-zA-Z\\s]+")) {
            mostrarAlerta("Validación", "El campo 'Director' solo debe contener letras.", AlertType.WARNING);
            return false;
        }
        if (!coordinadorField.getText().matches("[a-zA-Z\\s]+")) {
            mostrarAlerta("Validación", "El campo 'Coordinador' solo debe contener letras.", AlertType.WARNING);
            return false;
        }
        if (!jefeDeptoField.getText().matches("[a-zA-Z\\s]+")) {
            mostrarAlerta("Validación", "El campo 'Jefe de Departamento' solo debe contener letras.", AlertType.WARNING);
            return false;
        }

         // Validar que el campo de total de docentes solo contenga números
        if (totalDocentesField.getValue() == null || totalDocentesField.getValue() < 0) {
        mostrarAlerta("Validación", "El campo 'Total de Docentes' solo debe contener números positivos.", AlertType.WARNING);
        return false;
    }

        return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Otros botones
        
        // Configuración del Spinner para aceptar valores enteros
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(100,800); // Rango de 0 a 100, ajusta según necesites
        totalDocentesField.setValueFactory(valueFactory);
        // Configuración del botón Guardar para que llame al método guardarDatosEnExcel
        botonGuardar.setOnMouseClicked(event -> guardarDatosEnExcel(event));

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
        
        

        

    }
}
