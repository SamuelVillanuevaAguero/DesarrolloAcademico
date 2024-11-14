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

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * FXML Controller class
 */
public class ExportacionReconocimientosController implements Initializable {

    // Definimos las constantes para los tipos de celda
    public static final int CELL_TYPE_STRING = 1;
    public static final int CELL_TYPE_NUMERIC = 0;
    public static final int CELL_TYPE_BOOLEAN = 4;
    public static final int CELL_TYPE_FORMULA = 2;

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
    private TextField txtcodigodelcurso;
    @FXML
    private TextField txtNombreInstructor;
    @FXML
    private TextField txtFechaCurso;
    @FXML
    private ComboBox<String> txtFormatos;
    @FXML
    private Button botonLimpiar;
    @FXML
    private ComboBox<String> txtHoras;
    @FXML
    private TextArea txtAreaCompetencias;
    @FXML
    private TextArea txtAreaNombreCurso;
    @FXML
    private Button buttonRedireccionar;

    // Métodos de los botones de la barra superior
    public void cerrarVentana(MouseEvent event) throws IOException {
        ControladorGeneral.cerrarVentana(event, "¿Quieres cerrar sesión?", getClass());
    }

    public void minimizarVentana(MouseEvent event) {
        ControladorGeneral.minimizarVentana(event);
    }

    public void regresarVentana(MouseEvent event) throws IOException {
        ControladorGeneral.regresar(event, "Principal", getClass());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configuración inicial de eventos de los botones de la barra superior
        botonCerrar.setOnMouseClicked(event -> {
            try {
                cerrarVentana(event);
            } catch (IOException ex) {
                Logger.getLogger(ExportacionReconocimientosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        botonMinimizar.setOnMouseClicked(event -> minimizarVentana(event));

        botonRegresar.setOnMouseClicked(event -> {
            try {
                regresarVentana(event);
            } catch (IOException ex) {
                Logger.getLogger(ExportacionReconocimientosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Configurar opciones en ComboBox de horas y formatos
        for (int i = 20; i <= 50; i++) {
            txtHoras.getItems().add(String.valueOf(i));
        }
        txtFormatos.getItems().addAll("PDF", "Word", "Ambos");

        // Deshabilitar campos al inicio
        txtAreaNombreCurso.setDisable(true);
        txtFechaCurso.setDisable(true);
        txtHoras.setDisable(true);
        txtNombreInstructor.setDisable(true);
        txtAreaCompetencias.setDisable(true);
        txtFormatos.setDisable(true);
    }

    @FXML
    private void buscarCurso(ActionEvent event) {
        txtFormatos.setDisable(false); // Habilitar campo formatos
        txtcodigodelcurso.setDisable(true);
        String codigoCurso = txtcodigodelcurso.getText().trim();
        ExcelReader excelReader = new ExcelReader(); // Manejo del archivo Excel

        try {
            // Buscar el nombre del curso en el archivo de etiquetas
            String nombreCurso = excelReader.buscarCurso(codigoCurso);

            if (nombreCurso != null && !nombreCurso.isEmpty()) {
                // Si se encontró el nombre del curso, mostrarlo en el campo correspondiente
                txtAreaNombreCurso.setText(nombreCurso);

                // Buscar los detalles en el archivo de Prog-Institucional
                Map<String, String> datosCurso = excelReader.buscarDetallesCurso(nombreCurso);
                if (datosCurso != null) {
                    // Rellenar los campos con los datos encontrados
                    txtFechaCurso.setText(datosCurso.get("fechaCurso"));
                    txtHoras.setValue(datosCurso.get("horasCurso"));
                    txtNombreInstructor.setText(datosCurso.get("nombreInstructor"));
                    txtAreaCompetencias.setText(datosCurso.get("competencias"));
                } else {
                    // Si no se encuentran los detalles, mostrar un mensaje informativo
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "No se encontraron detalles adicionales para el curso.");
                    alert.showAndWait();
                }
            } else {
                // Si no se encuentra el nombre del curso, mostrar un mensaje informativo
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "No se encontró ningún curso con ese código.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            // Mostrar un mensaje de error en caso de problemas con el archivo Excel
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error al leer el archivo de Excel: " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    private void modificarDatos(ActionEvent event) {
        // Habilitar campos para edición
        txtFechaCurso.setDisable(false);
        txtNombreInstructor.setDisable(false);
        txtAreaCompetencias.setDisable(false);
        txtHoras.setDisable(false);

        // Mantener deshabilitados los campos no editables
        txtAreaNombreCurso.setDisable(true);
        txtcodigodelcurso.setDisable(true);
    }

    @FXML
    private void exportarReconocimientos(ActionEvent event) {
        System.out.println("Función exportarReconocimientos ejecutada.");
    }

    @FXML
    private void guardarDatos(ActionEvent event) {
        // Crear alerta de confirmación
        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación de guardado");
        confirmacion.setHeaderText("¿Está seguro de guardar los cambios?");

        // Mostrar la alerta y esperar respuesta del usuario
        confirmacion.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String nombreCurso = txtAreaNombreCurso.getText();
                Map<String, String> datosActualizados = new HashMap<>();
                datosActualizados.put("competencias", txtAreaCompetencias.getText());
                datosActualizados.put("fechaCurso", txtFechaCurso.getText());
                datosActualizados.put("horasCurso", txtHoras.getValue());
                datosActualizados.put("nombreInstructor", txtNombreInstructor.getText());

                ExcelReader excelReader = new ExcelReader();
                try {
                    if (excelReader.guardarDetallesCurso(nombreCurso, datosActualizados)) {
                        // Mostrar alerta de éxito
                        Alert exito = new Alert(AlertType.INFORMATION);
                        exito.setTitle("Éxito");
                        exito.setHeaderText(null);
                        exito.setContentText("Los cambios se guardaron con éxito.");
                        exito.showAndWait();

                        // Deshabilitar nuevamente los campos
                        txtFechaCurso.setDisable(true);
                        txtNombreInstructor.setDisable(true);
                        txtAreaCompetencias.setDisable(true);
                        txtHoras.setDisable(true);
                    } else {
                        Alert error = new Alert(AlertType.ERROR, "No se pudo encontrar el curso para actualizar.");
                        error.showAndWait();
                    }
                } catch (IOException e) {
                    Alert error = new Alert(AlertType.ERROR, "Error al guardar los datos: " + e.getMessage());
                    error.showAndWait();
                    e.printStackTrace();
                }
            } else {
                System.out.println("Guardado cancelado.");
            }
        });
    }

    @FXML
    private void limpiarCampos(ActionEvent event) {
        // Limpiar todos los TextFields
        txtcodigodelcurso.clear();
        txtAreaNombreCurso.clear();
        txtNombreInstructor.clear();
        txtAreaCompetencias.clear();
        txtFechaCurso.clear();

        // Restablecer ComboBoxes
        txtHoras.getSelectionModel().clearSelection();
        txtFormatos.getSelectionModel().clearSelection();

        // Deshabilitar campos al inicio
        txtAreaNombreCurso.setDisable(true);
        txtFechaCurso.setDisable(true);
        txtHoras.setDisable(true);
        txtFormatos.setDisable(true);
        txtNombreInstructor.setDisable(true);
        txtAreaCompetencias.setDisable(true);

        // habilitar codigo del curso
        txtcodigodelcurso.setDisable(false);
    }

    @FXML
    private void RedireccionarArchivos(ActionEvent event) {
    }

    class ExcelReader {

        private static final String ETIQUETAS_PATH = "C:/Users/ascen/OneDrive/Documentos/Documentos a ocupar/Etiquetas_Cursos_2024.xlsx";
        private static final String PROG_INSTITUCIONAL_PATH = "C:/Users/ascen/OneDrive/Documentos/Documentos a ocupar/PROG-INSTITUCIONAL-ENERO-2023.xlsx";

        // Método para buscar Nombre del curso en el archivo "Etiquetas"
        public String buscarCurso(String codigoCurso) throws IOException {

            try (FileInputStream fis = new FileInputStream(ETIQUETAS_PATH); Workbook workbook = new XSSFWorkbook(fis)) {
                Sheet sheet = workbook.getSheetAt(0); // Asume que está en la primera hoja

                for (Row row : sheet) {
                    org.apache.poi.ss.usermodel.Cell codigoCell = row.getCell(0); // Columna del código del curso
                    org.apache.poi.ss.usermodel.Cell NombreCursoCell = row.getCell(1); // Columna del Nombre

                    if (codigoCell != null && codigoCell.getStringCellValue().equals(codigoCurso)) {
                        return NombreCursoCell != null ? NombreCursoCell.getStringCellValue() : "";
                    }
                }
            }
            return null; // No se encontró el código
        }

        // Método para buscar detalles del curso en el archivo "Prog-Institucional"
        public Map<String, String> buscarDetallesCurso(String nombreCurso) throws IOException {
            Map<String, String> datosCurso = new HashMap<>();

            try (FileInputStream file = new FileInputStream("C:/Users/ascen/OneDrive/Documentos/Documentos a ocupar/PROG-INSTITUCIONAL-ENERO-2023.xlsx")) {
                Workbook workbook = new XSSFWorkbook(file);
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    // Omite las filas antes de la fila 9 (índice 8 en términos de programación)
                    if (row.getRowNum() < 8) {
                        continue;
                    }

                    org.apache.poi.ss.usermodel.Cell nombreCursoCell = row.getCell(1); // Columna "Nombre de los evento"
                    if (nombreCursoCell != null && nombreCursoCell.getCellType() == CELL_TYPE_STRING
                            && nombreCursoCell.getStringCellValue().equalsIgnoreCase(nombreCurso)) {

                        // Obtenemos cada celda relevante de la fila, usando la ruta completa de `Cell`
                        org.apache.poi.ss.usermodel.Cell competenciasCell = row.getCell(3); // Columna "Competencias a desarrollar"
                        org.apache.poi.ss.usermodel.Cell fechaCursoCell = row.getCell(4);    // Columna "Periodo de Realización"
                        org.apache.poi.ss.usermodel.Cell horasCursoCell = row.getCell(6);    // Columna "No. de horas x Curso"
                        org.apache.poi.ss.usermodel.Cell nombreInstructorCell = row.getCell(7); // Columna "Facilitador(a)"

                        // Procesamos cada celda según su tipo y añadimos validaciones adicionales
                        datosCurso.put("competencias", (competenciasCell != null && competenciasCell.getCellType() == CELL_TYPE_STRING)
                                ? competenciasCell.getStringCellValue() : "");

                        datosCurso.put("fechaCurso", (fechaCursoCell != null && fechaCursoCell.getCellType() == CELL_TYPE_STRING)
                                ? fechaCursoCell.getStringCellValue() : "");

                        datosCurso.put("horasCurso", (horasCursoCell != null && horasCursoCell.getCellType() == CELL_TYPE_NUMERIC)
                                ? String.valueOf((int) horasCursoCell.getNumericCellValue()) : "");

                        datosCurso.put("nombreInstructor", (nombreInstructorCell != null && nombreInstructorCell.getCellType() == CELL_TYPE_STRING)
                                ? nombreInstructorCell.getStringCellValue() : "");

                        break; // Detener el bucle después de encontrar el curso
                    }
                }
                workbook.close();
            }

            return datosCurso.isEmpty() ? null : datosCurso;
        }

        // Método auxiliar para obtener el valor de una celda como cadena de texto
        private String getCellStringValue(org.apache.poi.ss.usermodel.Cell cell) {
            if (cell == null) {
                return "";
            }
            switch (cell.getCellType()) { // Usa getCellType() en POI 3.12
                case CELL_TYPE_STRING:
                    return cell.getStringCellValue();
                case CELL_TYPE_NUMERIC:
                    return String.valueOf(cell.getNumericCellValue());
                case CELL_TYPE_BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                default:
                    return ""; // Para otros tipos de celdas o celdas de error
            }
        }

        public boolean guardarDetallesCurso(String nombreCurso, Map<String, String> datosActualizados) throws IOException {
            boolean actualizado = false;

            try (FileInputStream file = new FileInputStream(PROG_INSTITUCIONAL_PATH); Workbook workbook = new XSSFWorkbook(file)) {
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    // Omite las filas antes de la fila 9 (índice 8)
                    if (row.getRowNum() < 8) {
                        continue;
                    }

                    // Ubica la fila que corresponde al curso especificado
                    org.apache.poi.ss.usermodel.Cell nombreCursoCell = row.getCell(1);
                    if (nombreCursoCell != null && nombreCursoCell.getCellType() == CELL_TYPE_STRING
                            && nombreCursoCell.getStringCellValue().equalsIgnoreCase(nombreCurso)) {

                        // Actualizar los datos en las celdas correspondientes
                        if (datosActualizados.containsKey("competencias")) {
                            org.apache.poi.ss.usermodel.Cell competenciasCell = row.getCell(3);
                            if (competenciasCell == null) {
                                competenciasCell = row.createCell(3);
                            }
                            competenciasCell.setCellValue(datosActualizados.get("competencias"));
                        }

                        if (datosActualizados.containsKey("fechaCurso")) {
                            org.apache.poi.ss.usermodel.Cell fechaCursoCell = row.getCell(4);
                            if (fechaCursoCell == null) {
                                fechaCursoCell = row.createCell(4);
                            }
                            fechaCursoCell.setCellValue(datosActualizados.get("fechaCurso"));
                        }

                        if (datosActualizados.containsKey("horasCurso")) {
                            org.apache.poi.ss.usermodel.Cell horasCursoCell = row.getCell(6);
                            if (horasCursoCell == null) {
                                horasCursoCell = row.createCell(6);
                            }
                            horasCursoCell.setCellValue(Integer.parseInt(datosActualizados.get("horasCurso")));
                        }

                        if (datosActualizados.containsKey("nombreInstructor")) {
                            org.apache.poi.ss.usermodel.Cell nombreInstructorCell = row.getCell(7);
                            if (nombreInstructorCell == null) {
                                nombreInstructorCell = row.createCell(7);
                            }
                            nombreInstructorCell.setCellValue(datosActualizados.get("nombreInstructor"));
                        }

                        actualizado = true;
                        break;
                    }
                }

                if (actualizado) {
                    // Guardar los cambios en el archivo
                    try (FileOutputStream fos = new FileOutputStream(PROG_INSTITUCIONAL_PATH)) {
                        workbook.write(fos);
                    }
                }
            }

            return actualizado;
        }

    }
}
