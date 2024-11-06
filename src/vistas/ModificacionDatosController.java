

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
private static final String FILE_PATH = "C:\\Users\\TUF\\Documents\\NetBeansProjects\\DesarrolloAcademico\\DatosEjemplo.xlsx"; // Cambia esto por la ruta real de tu archivo Excel
   
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
        botonGuardar.setOnMouseClicked(event -> guardarDatos());

        // Configura el evento del botón "Cancelar" para que cierre la ventana
        botonCancelar.setOnMouseClicked(event ->cancelarVentana(event));
        cargarDatosDesdeExcel();
    }

    public void cargarDatosDesdeExcel() {
        
        try (FileInputStream fis = new FileInputStream(new File(FILE_PATH));
             Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);

            if (row != null) {
                directorField.setText(row.getCell(0).getStringCellValue());
                coordinadorField.setText(row.getCell(1).getStringCellValue());
                jefeDeptoField.setText(row.getCell(2).getStringCellValue());
                totalDocentesField.setText(String.valueOf((int) row.getCell(3).getNumericCellValue()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarDatosEnExcel() {
        try (FileInputStream fis = new FileInputStream(new File(FILE_PATH));
             Workbook workbook = new XSSFWorkbook(fis);
             FileOutputStream fos = new FileOutputStream(new File(FILE_PATH))) {
            Sheet sheet = workbook.getSheetAt(0);
            Row row = sheet.getRow(0);

            if (row == null) {
                row = sheet.createRow(0);
            }

            row.createCell(0).setCellValue(directorField.getText());
            row.createCell(1).setCellValue(coordinadorField.getText());
            row.createCell(2).setCellValue(jefeDeptoField.getText());
            row.createCell(3).setCellValue(Integer.parseInt(totalDocentesField.getText()));

            workbook.write(fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarDatos() {
        String director = directorField.getText();
        String coordinador = coordinadorField.getText();
        String jefeDepto = jefeDeptoField.getText();
        String totalDocentes = totalDocentesField.getText();

        guardarDatosEnExcel();

        // Aquí agrega la lógica para guardar los datos
        System.out.println("Datos guardados: ");
        System.out.println("Director: " + director);
        System.out.println("Coordinador: " + coordinador);
        System.out.println("Jefe de Departamento: " + jefeDepto);
        System.out.println("Total de Docentes: " + totalDocentes);
    }
}
