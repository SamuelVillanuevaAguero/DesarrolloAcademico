package vistas;

import com.aspose.cells.SaveFormat;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
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
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilerias.busqueda.filaDato;
import utilerias.general.ControladorGeneral;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.traversal.NodeFilter;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;

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
    private Button botonExportar;
    @FXML
    private Button botonActualizarDatos;

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

    // TABLA ------------------------------------------------------------------------
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
    private TableColumn<filaDato, String> columnaTipoCapacitacion;
    @FXML
    private TableColumn<filaDato, Integer> columnaNumeroCursos;

    @FXML
    private Label numeroTotalDocentes;
    @FXML
    private Label docentesTomandoCursos;
    @FXML
    private Label porcentajeDocentesCapacitados;
    @FXML
    private Label NumeroDocentesNivel;

    private ObservableList data;

    public void exportarExcelAPDF(String rutaArchivo, File fileToSave) {
        try {
            // Cargar el archivo de Excel
            com.aspose.cells.Workbook workbook = new com.aspose.cells.Workbook(rutaArchivo);

            // Guardar el archivo como PDF
            workbook.save(fileToSave.getAbsolutePath(), SaveFormat.PDF);
            System.out.println("Archivo exportado exitosamente como PDF en: " + fileToSave.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Cell obtenerCeldaDebajo(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath); Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            // Obtener la primera hoja del libro
            Sheet sheet = workbook.getSheetAt(0);

            // Buscar la celda con el texto "NOMBRE DEL DOCENTE"
            for (Row row : sheet) {
                for (Cell cell : row) {
                    // Usar CellType en lugar de constantes deprecated
                    if (cell.getCellType() == Cell.CELL_TYPE_STRING && "NOMBRE DEL DOCENTE".equals(cell.getStringCellValue())) {

                        // Obtener el índice de la fila justo debajo
                        int rowIndexDebajo = row.getRowNum() + 1;
                        Row rowDebajo = sheet.getRow(rowIndexDebajo);

                        // Crear la fila si no existe
                        if (rowDebajo == null) {
                            rowDebajo = sheet.createRow(rowIndexDebajo);
                        }

                        // Obtener la celda en la misma columna
                        int columnIndex = cell.getColumnIndex();
                        Cell cellDebajo = rowDebajo.getCell(columnIndex);

                        // Crear la celda si no existe
                        if (cellDebajo == null) {
                            cellDebajo = rowDebajo.createCell(columnIndex);
                        }

                        // Actualizar la celda debajo con el valor "Samito"
                        cellDebajo.setCellValue("Samito");
                        System.out.println("Escrito en la celda debajo de 'NOMBRE DEL DOCENTE'");

                        // Retornar la celda justo debajo
                        return cellDebajo;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; // Si no se encuentra la celda "NOMBRE DEL DOCENTE"
    }

    public void escribirExcel(String rutaArchivo, Stage stage) {
        try (FileInputStream fileInputStream = new FileInputStream(rutaArchivo); Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0); // Obtener la primera hoja

            // Estilo para datos normales  
            CellStyle estiloNormal = workbook.createCellStyle();
            estiloNormal.setAlignment(CellStyle.ALIGN_CENTER);
            estiloNormal.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            estiloNormal.setBorderTop(CellStyle.BORDER_THIN);
            estiloNormal.setBorderBottom(CellStyle.BORDER_THIN);
            estiloNormal.setBorderLeft(CellStyle.BORDER_THIN);
            estiloNormal.setBorderRight(CellStyle.BORDER_THIN);

            // Estilo para encabezados (en negrita)  
            CellStyle estiloEncabezado = workbook.createCellStyle();
            estiloEncabezado.cloneStyleFrom(estiloNormal);
            Font fuenteNegrita = workbook.createFont();

            fuenteNegrita.setBold(true);
            estiloEncabezado.setFont(fuenteNegrita);

            // Crear encabezados si no existen
            String[] encabezados = {"NO.", "NOMBRE DEL DOCENTE", "NO. CURSO", "F.DOCENTE", "A. DOCENTE"};
            if (sheet.getRow(9) == null) {
                Row headerRow = sheet.createRow(9);
                for (int i = 0; i < encabezados.length; i++) {
                    Cell cell = headerRow.createCell(i + 1);
                    cell.setCellValue(encabezados[i]);
                    cell.setCellStyle(estiloEncabezado);
                }
            }

            // Encontrar la siguiente fila vacía para agregar datos
            int rowNum = 10;
            while (sheet.getRow(rowNum) != null) {
                rowNum++;
            }

            // Llenar datos  
            Row row = sheet.createRow(rowNum);
            String[] datos = {"1", "Samuelin", "45", "Si", "No"};
            for (int i = 0; i < datos.length; i++) {
                Cell cell = row.createCell(i + 1);
                cell.setCellValue(datos[i]);
                cell.setCellStyle(estiloNormal);
            }

            // Ajustar ancho de columnas  
            for (int i = 0; i < encabezados.length; i++) {
                sheet.autoSizeColumn(i + 1);
            }

            // Usar FileChooser para seleccionar la ubicación de exportación
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar como");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
            fileChooser.setInitialFileName("copia_" + new File(rutaArchivo).getName()); // Nombre por defecto

            File fileToSave = fileChooser.showSaveDialog(stage);
            if (fileToSave != null) {
                try (FileOutputStream outputStream = new FileOutputStream(fileToSave)) {
                    workbook.write(outputStream);
                }
                System.out.println("Datos añadidos exitosamente al archivo Excel en: " + fileToSave.getAbsolutePath());
            } else {
                System.out.println("Exportación cancelada.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportarArchivo(String rutaArchivo, Stage stage, int formato) {
        try (FileInputStream fileInputStream = new FileInputStream(rutaArchivo); Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0); // Obtener la primera hoja

            // Usar FileChooser para seleccionar la ubicación de exportación
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Guardar como");

            // Definir el tipo de archivo según el parámetro formato
            if (formato == 1) {
                // Formato PDF
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
                fileChooser.setInitialFileName("copia_" + new File(rutaArchivo).getName().replace(".xlsx", ".pdf"));
            } else if (formato == 2) {
                // Formato Excel
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
                fileChooser.setInitialFileName("copia_" + new File(rutaArchivo).getName());
            }

            File fileToSave = fileChooser.showSaveDialog(stage);
            if (fileToSave != null) {
                if (formato == 1) {
                    // Guardar como PDF usando Aspose.Cells
                    exportarAPDF(rutaArchivo, fileToSave);
                    System.out.println("Archivo exportado exitosamente como PDF en: " + fileToSave.getAbsolutePath());
                } else if (formato == 2) {
                    // Guardar como Excel
                    try (FileOutputStream outputStream = new FileOutputStream(fileToSave)) {
                        workbook.write(outputStream);
                    }
                    System.out.println("Archivo exportado exitosamente como Excel en: " + fileToSave.getAbsolutePath());
                }
            } else {
                System.out.println("Exportación cancelada.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportarAPDF(String rutaArchivo, File fileToSave) {
        try {
            // Cargar el archivo Excel con Aspose.Cells
            com.aspose.cells.Workbook workbook = new com.aspose.cells.Workbook(rutaArchivo);

            // Guardar el archivo Excel como PDF
            workbook.save(fileToSave.getAbsolutePath(), SaveFormat.PDF);

            System.out.println("Archivo exportado exitosamente como PDF en: " + fileToSave.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int contarFilasUnicas(ObservableList<filaDato> data) {
        // Utilizamos un Set para almacenar combinaciones únicas
        Set<String> setUnico = new HashSet<>();

        // Recorremos los datos y agregamos la combinación al Set
        for (filaDato fila : data) {
            String key = fila.getNombre() + " " + fila.getApellidoPaterno() + " " + fila.getApellidoMaterno();
            setUnico.add(key);  // Set no permite duplicados
        }

        // El tamaño del Set será el número de filas únicas
        return setUnico.size();
    }

    private int obtenerTotalDocentes(String ruta) {
        try {
            FileInputStream file = new FileInputStream(ruta);
            Workbook libro = new XSSFWorkbook(file);

            Sheet hoja = libro.getSheetAt(0);

            return (int) Double.parseDouble(hoja.getRow(0).getCell(1).toString());

        } catch (FileNotFoundException ex) {
            Logger.getLogger(BusquedaEstadisticaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BusquedaEstadisticaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

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
                    String tipoCapacitacion = row.getCell(13).getStringCellValue();
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
                    data.add(new filaDato(año, periodo, nombre, apellidoPaterno, apellidoMaterno, departamento, posgrado, acreditado, tipoCapacitacion, noCursos));
                }
            }
        }
        return data;
    }

    private ObservableList<filaDato> readAllExcelFiles(Integer año, Integer periodo) {
        ObservableList<filaDato> data = FXCollections.observableArrayList();
        Map<String, filaDato> dataMap = new HashMap<>();

        // Directorio base  
        String baseDir = "C:\\Users\\Samue\\Desktop\\Gestion_de_Cursos\\Sistema\\condensados_vista_de_visualizacion_de_datos";
        Path searchPath;

        // Construir la ruta del directorio basado en año y periodo  
        if (año != 0) {
            if (periodo == 0) {
                searchPath = Paths.get(baseDir, año.toString());
            } else {
                searchPath = Paths.get(baseDir, año.toString(), periodo.toString());
            }
        } else {
            searchPath = Paths.get(baseDir);
        }

        try {
            Stream<Path> pathStream = Files.walk(searchPath)
                    .filter(path -> path.toString().endsWith("Acreditacion_Docente.xlsx"));

            pathStream.forEach(path -> {
                System.out.println("Hoja n");
                try (FileInputStream file = new FileInputStream(path.toFile()); Workbook workbook = WorkbookFactory.create(file)) {

                    Sheet sheet = workbook.getSheetAt(0);

                    // Recorrer las filas del archivo Excel
                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);

                        if (row == null) {
                            continue;
                        }

                        int currentAño = 0;
                        int currentPeriodo = 0;

                        // Extracción de periodo y año  
                        String periodoCell = row.getCell(6).getStringCellValue();
                        String[] dates = periodoCell.split("-");
                        if (dates[0].trim().split("/").length > 1) {
                            currentAño = Integer.parseInt(dates[0].trim().split("/")[2]);
                            currentPeriodo = Integer.parseInt(dates[0].trim().split("/")[1]) <= 7 ? 1 : 2;
                        }

                        boolean passAñoFilter = currentAño == año || año == 0;
                        boolean passPeriodoFilter = periodo == 0 || currentPeriodo == periodo;

                        if (passAñoFilter && passPeriodoFilter) {
                            String nombre = row.getCell(0).getStringCellValue();
                            String apellidoPaterno = row.getCell(1).getStringCellValue();
                            String apellidoMaterno = row.getCell(2).getStringCellValue();
                            String departamento = row.getCell(8).getStringCellValue();
                            String posgrado = row.getCell(9).getStringCellValue();
                            String acreditado = row.getCell(12).getStringCellValue();
                            String tipoCapacitacion = row.getCell(13).getStringCellValue();
                            String key = currentAño + " " + periodoCell + " " + nombre + " " + apellidoPaterno + " " + apellidoMaterno;

                            // Verificar si ya existe el registro en el Map
                            if (dataMap.containsKey(key)) {
                                // Incrementar el contador de cursos si ya existe
                                filaDato existingDato = dataMap.get(key);
                                existingDato.setNoCursos(existingDato.getNoCursos() + 1);
                            } else {
                                // Si no existe, crear un nuevo objeto filaDato con noCursos = 1
                                filaDato newDato = new filaDato(currentAño, periodoCell, nombre,
                                        apellidoPaterno, apellidoMaterno, departamento, posgrado,
                                        acreditado, tipoCapacitacion, 1);
                                dataMap.put(key, newDato);
                            }
                        }
                    }
                } catch (Exception e) {
                    Logger.getLogger(BusquedaEstadisticaController.class.getName())
                            .log(Level.SEVERE, "Error reading file: " + path, e);
                }
            });
        } catch (IOException e) {
            Logger.getLogger(BusquedaEstadisticaController.class.getName())
                    .log(Level.SEVERE, "Error walking directory", e);
        }

        // Convertir el Map a una lista observable
        data.addAll(dataMap.values());
        return data;
    }

    private ObservableList<filaDato> readAllExcelFiles(Integer año, Integer periodo, String tipoCapacitacion, String departamento, String acreditacion, String nivel) {
        ObservableList<filaDato> data = FXCollections.observableArrayList();
        Map<String, filaDato> dataMap = new HashMap<>();

        // Directorio base  
        String baseDir = "C:\\Users\\Samue\\Desktop\\Gestion_de_Cursos\\Sistema\\condensados_vista_de_visualizacion_de_datos";
        Path searchPath;

        // Construir la ruta del directorio basado en año y periodo  
        if (año != 0) {
            searchPath = periodo == 0 ? Paths.get(baseDir, año.toString()) : Paths.get(baseDir, año.toString(), periodo.toString());
        } else {
            searchPath = Paths.get(baseDir);
        }

        try {
            // Filtrar archivos Excel por nombre
            Stream<Path> pathStream = Files.walk(searchPath)
                    .filter(path -> path.toString().endsWith("Acreditacion_Docente.xlsx"));

            pathStream.forEach(path -> {
                try (FileInputStream file = new FileInputStream(path.toFile()); Workbook workbook = WorkbookFactory.create(file)) {

                    Sheet sheet = workbook.getSheetAt(0);

                    for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                        Row row = sheet.getRow(i);
                        if (row == null) {
                            continue;
                        }

                        int currentAño = 0;
                        int currentPeriodo = 0;

                        // Extracción de año y periodo
                        String periodoCell = row.getCell(6).getStringCellValue();
                        String[] dates = periodoCell.split("-");
                        if (dates[0].trim().split("/").length > 1) {
                            currentAño = Integer.parseInt(dates[0].trim().split("/")[2]);
                            currentPeriodo = Integer.parseInt(dates[0].trim().split("/")[1]) <= 7 ? 1 : 2;
                        }

                        boolean passAñoFilter = currentAño == año || año == 0;
                        boolean passPeriodoFilter = periodo == 0 || currentPeriodo == periodo;

                        // Aplicar filtros de ComboBox
                        String filaTipoCapacitacion = row.getCell(13).getStringCellValue();
                        String filaDepartamento = row.getCell(8).getStringCellValue();
                        String filaPosgrado = row.getCell(9).getStringCellValue();
                        String filaAcreditacion = row.getCell(12).getStringCellValue();

                        boolean passTipoCapacitacion = tipoCapacitacion == null || tipoCapacitacion.equals(filaTipoCapacitacion);
                        boolean passDepartamento = departamento == null || departamento.equals(filaDepartamento);
                        boolean passAcreditacion = acreditacion == null || acreditacion.equalsIgnoreCase(filaAcreditacion) || acreditacion.equals("Ambos");
                        boolean passNivel = nivel == null || (nivel.equals("Licenciatura") && filaPosgrado.equals("No")) || (nivel.equals("Posgrado") && filaPosgrado.equals("Sí"));

                        // Aplicar todos los filtros
                        if (passAñoFilter && passPeriodoFilter && passTipoCapacitacion && passDepartamento && passAcreditacion && passNivel) {
                            String nombre = row.getCell(0).getStringCellValue();
                            String apellidoPaterno = row.getCell(1).getStringCellValue();
                            String apellidoMaterno = row.getCell(2).getStringCellValue();

                            // Crear clave única para el Map
                            String key = currentAño + " " + periodoCell + " " + nombre + " " + apellidoPaterno + " " + apellidoMaterno + " " + filaDepartamento + " " + filaPosgrado + " " + filaAcreditacion + " " + filaTipoCapacitacion;

                            // Verificar si ya existe el registro en el Map
                            if (dataMap.containsKey(key)) {
                                filaDato existingDato = dataMap.get(key);
                                existingDato.setNoCursos(existingDato.getNoCursos() + 1);
                            } else {
                                filaDato newDato = new filaDato(currentAño, periodoCell, nombre,
                                        apellidoPaterno, apellidoMaterno, filaDepartamento, filaPosgrado,
                                        filaAcreditacion, filaTipoCapacitacion,1);
                                dataMap.put(key, newDato);
                            }
                        }
                    }
                } catch (Exception e) {
                    Logger.getLogger(BusquedaEstadisticaController.class.getName()).log(Level.SEVERE, "Error leyendo archivo: " + path, e);
                }
            });
        } catch (IOException e) {
            Logger.getLogger(BusquedaEstadisticaController.class.getName()).log(Level.SEVERE, "Error recorriendo directorio", e);
        }

        data.addAll(dataMap.values());
        return data;
    }

    public List<Integer> getAvailableYears(String baseDirectoryPath) {
        List<Integer> years = new ArrayList<>();
        Path basePath = Paths.get(baseDirectoryPath);

        try {
            // Listar solo las carpetas en el directorio base
            years = Files.list(basePath)
                    .filter(Files::isDirectory)
                    .map(path -> path.getFileName().toString()) // Obtener el nombre de la carpeta
                    .filter(name -> name.matches("\\d{4}")) // Filtrar solo los nombres que sean números de 4 dígitos (año)
                    .map(Integer::parseInt) // Convertir el nombre de la carpeta a entero
                    .sorted(Comparator.reverseOrder()) // Ordenar de forma descendente
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al listar los años en el directorio: " + baseDirectoryPath);
        }

        return years;
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
        
        columnaTipoCapacitacion = new TableColumn<>("Capacitación");
        columnaTipoCapacitacion.setCellValueFactory(new PropertyValueFactory<>("tipoCapacitacion"));
        columnaNumeroCursos = new TableColumn<>("No.Cursos");
        columnaNumeroCursos.setCellValueFactory(new PropertyValueFactory<>("noCursos"));

        tabla.getColumns().addAll(columnaAño, columnaPeriodo, columnaNombre,
                columnaApellidoPaterno, columnaApellidoMaterno, columnaDepartamentoLicenciatura,
                columnaDepartamentoPosgrado, columnaAcreditado, columnaTipoCapacitacion, columnaNumeroCursos);

        data = readAllExcelFiles(0, 0);

        tabla.setItems(data);
        int docentesCursos = contarFilasUnicas(data);
        int totalDocentes = obtenerTotalDocentes("C:\\Users\\Samue\\Desktop\\Gestion_de_Cursos\\Sistema\\informacion_modificable\\info.xlsx");
        double porcentajeCapacitados = (double) docentesCursos / totalDocentes * 100;

        docentesTomandoCursos.setText(String.valueOf(docentesCursos));
        numeroTotalDocentes.setText(String.valueOf(totalDocentes));

        porcentajeDocentesCapacitados.setText((int)porcentajeCapacitados + "%");
        // Inicializar opciones de ComboBoxes
        comboTipoCapacitacion.getItems().addAll("Actualización profesional", "Formación docente");
        comboDepartamento.getItems().addAll("Ciencias Básicas", "Ciencias Económico Administrativas", "Ingeniería Industrial", "Sistemas Computacionales");
        comboAcreditacion.getItems().addAll("Si", "No", "Ambos");
        comboNivel.getItems().addAll("Licenciatura", "Posgrado");

        int currentYear = Year.now().getValue();

        comboAño.getItems().addAll(getAvailableYears("C:\\Users\\Samue\\Desktop\\Gestion_de_Cursos\\Sistema\\condensados_vista_de_visualizacion_de_datos"));
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

            String tipoCapacitacion = comboTipoCapacitacion.getValue().equals("Formación docente") ? "FD" : "AP";
            
            String departamento = comboDepartamento.getValue();
            String acreditacion = comboAcreditacion.getValue();
            String nivel = comboNivel.getValue();

            int año = comboAño.getValue() != null ? comboAño.getValue() : 0;
            int periodo = comboPeriodo.getValue() != null
                    ? comboPeriodo.getValue().equals("Enero - Julio") ? 1 : 2 : 0;

            // Llamar al método readAllExcelFiles pasando todos los filtros
            data = readAllExcelFiles(año, periodo, tipoCapacitacion, departamento, acreditacion, nivel);
            tabla.setItems(data);
            docentesTomandoCursos.setText("" + contarFilasUnicas(data));

        });

        botonExportar.setOnMouseClicked(event -> {
            String rutaArchivo = "C:\\Users\\Samue\\Desktop\\Gestion_de_Cursos\\Sistema\\informacion_modificable\\reporte.xlsx";

            switch (comboFormato.getValue() == null ? "default" : comboFormato.getValue()) {
                case "PDF":
                    exportarArchivo(rutaArchivo, (Stage) botonExportar.getScene().getWindow(), 1);
                    break;
                case "EXCEL":
                    exportarArchivo(rutaArchivo, (Stage) botonExportar.getScene().getWindow(), 2);
                    break;
                default:
            }
        });
        
        botonActualizarDatos.setOnMouseClicked(event -> {
            /*try {
                ControladorGeneral.regresar(event, "ModificacionDatos", getClass());
            } catch (IOException ex) {
                Logger.getLogger(BusquedaEstadisticaController.class.getName()).log(Level.SEVERE, null, ex);
            }*/
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