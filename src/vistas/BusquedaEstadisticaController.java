package vistas;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Year;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilerias.busqueda.filaDato;
import utilerias.general.ControladorGeneral;

public class BusquedaEstadisticaController implements Initializable {

    @FXML
    private Button botonCerrar;
    @FXML
    private Button botonMinimizar;
    @FXML
    private Button botonRegresar;
    @FXML
    private Label botonActualizar;
    @FXML
    private Button botonLimpiar;
    @FXML
    private Button botonBuscar;

    @FXML
    private ComboBox<String> comboTipoCapacitacion;
    @FXML
    private ComboBox<String> comboDepartamento;
    @FXML
    private ComboBox<String> comboAcreditacion;
    @FXML
    private ComboBox<String> comboNivel;
    @FXML
    private ComboBox<Integer> comboAño;
    @FXML
    private ComboBox<String> comboPeriodo;
    @FXML
    private ComboBox<String> comboFormato;

    @FXML
    private TableView<filaDato> tabla;

    @FXML
    private TableColumn<filaDato, Integer> columnaAño;
    @FXML
    private TableColumn<filaDato, String> columnaPeriodo;
    @FXML
    private TableColumn<filaDato, String> columnaNombre;
    @FXML
    private TableColumn<filaDato, String> columnaApellidoPaterno;
    @FXML
    private TableColumn<filaDato, String> columnaApellidoMaterno;
    @FXML
    private TableColumn<filaDato, String> columnaDepartamentoLicenciatura;
    @FXML
    private TableColumn<filaDato, String> columnaDepartamentoPosgrado;
    @FXML
    private TableColumn<filaDato, String> columnaAcreditado;
    @FXML
    private TableColumn<filaDato, Integer> columnaNumeroCursos;

    private ObservableList<filaDato> readExcelData(String filePath) throws IOException {
        ObservableList<filaDato> data = FXCollections.observableArrayList();
        try (FileInputStream file = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(file)) {
            Sheet sheet = workbook.getSheetAt(0);

            // Second pass: Create filaDato objects
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                System.out.println(sheet.getLastRowNum());
                Row row = sheet.getRow(i);
                if (row != null) {
                    System.out.println(row);
                    int año = 0;
                    String acreditado = "";
                    int noCursos = 0;

                    String nombre = row.getCell(0).getStringCellValue();
                    String apellidoPaterno = row.getCell(1).getStringCellValue();
                    String apellidoMaterno = row.getCell(2).getStringCellValue();
                    String periodo = row.getCell(6).getStringCellValue();
                    String departamento = row.getCell(8).getStringCellValue();
                    String posgrado = row.getCell(9).getStringCellValue();
                    // Assuming this cell has accreditation info
                    System.out.println(i);
                    // Extract year from the period
                    String[] dates = periodo.split("-");
                    if (dates[0].trim().split("/").length > 1) {
                        año = Integer.parseInt(dates[0].trim().split("/")[2]);
                        acreditado = row.getCell(12).getStringCellValue();
                    }

                    // Get number of courses for the instructor
                    // Create and add filaDato object
                    data.add(new filaDato(año, periodo, nombre, apellidoPaterno, apellidoMaterno, departamento, posgrado, acreditado, noCursos));
                }
            }
        }
        return data;
    }

    private ObservableList<filaDato> readAllExcelFiles() throws IOException {
        ObservableList<filaDato> data = FXCollections.observableArrayList();

        // Directorio base  
        String baseDir = "C:\\Users\\Samue\\Desktop\\Gestion_de_Cursos\\Sistema\\condensados_vista_de_visualizacion_de_datos";

        // Buscar y recorrer todos los archivos Excel  
        try {
            Files.walk(Paths.get(baseDir)).filter(path -> path.toString().endsWith("Acreditacion_Docente.xlsx")).forEach(path -> {
                try {
                    // Abrir el archivo Excel  
                    FileInputStream file = new FileInputStream(path.toFile());
                    Workbook workbook = WorkbookFactory.create(file);
                    Sheet sheet = workbook.getSheetAt(0);

                    // Recorrer las filas como en tu método original  
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);

                        if (row == null) {
                            continue;
                        }

                        // Mantener tu lógica original de extracción de datos  
                        System.out.println(row);
                    int año = 0;
                    String acreditado = "";
                    int noCursos = 0;

                    String nombre = row.getCell(0).getStringCellValue();
                    String apellidoPaterno = row.getCell(1).getStringCellValue();
                    String apellidoMaterno = row.getCell(2).getStringCellValue();
                    String periodo = row.getCell(6).getStringCellValue();
                    String departamento = row.getCell(8).getStringCellValue();
                    String posgrado = row.getCell(9).getStringCellValue();
                    // Assuming this cell has accreditation info
                    System.out.println(i);
                    // Extract year from the period
                    String[] dates = periodo.split("-");
                    if (dates[0].trim().split("/").length > 1) {
                        año = Integer.parseInt(dates[0].trim().split("/")[2]);
                        acreditado = row.getCell(12).getStringCellValue();
                    }

                    // Get number of courses for the instructor
                    // Create and add filaDato object
                    data.add(new filaDato(año, periodo, nombre, apellidoPaterno, apellidoMaterno, departamento, posgrado, acreditado, noCursos));
                    }

                    // Cerrar recursos  
                    file.close();
                    workbook.close();

                } catch (Exception e) {
                    Logger.getLogger(BusquedaEstadisticaController.class.getName())
                            .log(Level.SEVERE, "Error reading file: " + path, e);
                }
            });
        } catch (IOException e) {
            Logger.getLogger(BusquedaEstadisticaController.class.getName())
                    .log(Level.SEVERE, "Error walking directory", e);
        }

        return data;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnaAño = new TableColumn<>("Año");
        columnaAño.setCellValueFactory(new PropertyValueFactory<>("año"));
        columnaPeriodo = new TableColumn<>("Periodo");
        columnaPeriodo.setCellValueFactory(new PropertyValueFactory<>("periodo"));
        columnaNombre = new TableColumn<>("Nombre");
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaApellidoPaterno = new TableColumn<>("APP");
        columnaApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));
        columnaApellidoMaterno = new TableColumn<>("APM");
        columnaApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));
        columnaDepartamentoLicenciatura = new TableColumn<>("Licenciatura");
        columnaDepartamentoLicenciatura.setCellValueFactory(new PropertyValueFactory<>("departamentoLicenciatura"));
        columnaDepartamentoPosgrado = new TableColumn<>("Posgrado");
        columnaDepartamentoPosgrado.setCellValueFactory(new PropertyValueFactory<>("departamentoPosgrado"));
        columnaAcreditado = new TableColumn<>("Acreditado");
        columnaAcreditado.setCellValueFactory(new PropertyValueFactory<>("acreditado"));
        columnaNumeroCursos = new TableColumn<>("No.Cursos");
        columnaNumeroCursos.setCellValueFactory(new PropertyValueFactory<>("noCursos"));

        tabla.getColumns().addAll(columnaAño, columnaPeriodo, columnaNombre,
                columnaApellidoPaterno, columnaApellidoMaterno, columnaDepartamentoLicenciatura,
                columnaDepartamentoPosgrado, columnaAcreditado, columnaNumeroCursos);

        try {
            tabla.setItems(readAllExcelFiles());
        } catch (IOException ex) {
            Logger.getLogger(BusquedaEstadisticaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Inicializar opciones de ComboBoxes
        comboTipoCapacitacion.getItems().addAll("Actualización profesional", "Formación docente");
        comboDepartamento.getItems().addAll("Ciencias Básicas", "Ciencias Económico Administrativas", "Ingeniería Industrial", "Sistemas Computacionales");
        comboAcreditacion.getItems().addAll("Si", "No", "Ambos");
        comboNivel.getItems().addAll("Licenciatura", "Posgrado");

        int currentYear = Year.now().getValue();
        List<Integer> years = IntStream.rangeClosed(currentYear - 10, currentYear)
                .boxed()
                .sorted((a, b) -> b - a)
                .collect(Collectors.toList());
        comboAño.getItems().addAll(years);
        comboPeriodo.getItems().addAll("Enero - Julio", "Agosto - Diciembre");
        comboFormato.getItems().addAll("PDF", "EXCEL");

        botonCerrar.setOnMouseClicked(event -> {
            try {
                cerrarVentana(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        botonMinimizar.setOnMouseClicked(event -> minimizarVentana(event));
        botonRegresar.setOnMouseClicked(event -> {
            try {
                regresarVentana(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        botonActualizar.setOnMouseClicked(event -> {
            try {
                actualizarDocumentos(event);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        botonLimpiar.setOnMouseClicked(event -> {
            comboTipoCapacitacion.setValue(null);
            comboDepartamento.setValue(null);
            comboAcreditacion.setValue(null);
            comboNivel.setValue(null);
            comboAño.setValue(null);
            comboPeriodo.setValue(null);
            comboFormato.setValue(null);
            tabla.getItems().clear();
        });

        botonBuscar.setOnMouseClicked(event -> {
            System.out.println("Botón buscar...");

        });
    }

    public void cerrarVentana(MouseEvent event) throws IOException {
        ControladorGeneral.cerrarVentana(event, "¿Quieres cerrar sesión?", getClass());
    }

    public void minimizarVentana(MouseEvent event) {
        ControladorGeneral.minimizarVentana(event);
    }

    public void regresarVentana(MouseEvent event) throws IOException {
        ControladorGeneral.regresar(event, "Principal", getClass());
    }

    public void actualizarDocumentos(MouseEvent event) throws IOException {
        ControladorGeneral.regresar(event, "ImportacionArchivos", getClass());
    }
}
