package vistas;

import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilerias.general.ControladorGeneral;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

public class VizualizacionDatosController implements Initializable {

    @FXML
    private Button botonCerrar, botonMinimizar, botonRegresar, botonExportar,
            botonActualizar, botonModificar, botonGuardar, botonBuscar;

    @FXML
    private TextField campoBusqueda;

    @FXML
    private TableView<Evento> tableView;

    private ObservableList<Evento> eventoList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        botonCerrar.setOnMouseClicked(event -> {
            try {
                cerrarVentana(event);
            } catch (IOException ex) {
                Logger.getLogger(VizualizacionDatosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        botonMinimizar.setOnMouseClicked(this::minimizarVentana);
        botonRegresar.setOnMouseClicked(event -> {
            try {
                regresarVentana(event);
            } catch (IOException ex) {
                Logger.getLogger(VizualizacionDatosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        configurarTabla();
        cargarDatos();
    }

    private void configurarTabla() {
        TableColumn<Evento, String> colApellidoPaterno = new TableColumn<>("Apellido Paterno");
        colApellidoPaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoPaterno"));

        TableColumn<Evento, String> colApellidoMaterno = new TableColumn<>("Apellido Materno");
        colApellidoMaterno.setCellValueFactory(new PropertyValueFactory<>("apellidoMaterno"));

        TableColumn<Evento, String> colNombres = new TableColumn<>("Nombres");
        colNombres.setCellValueFactory(new PropertyValueFactory<>("nombres"));

        TableColumn<Evento, String> colRFC = new TableColumn<>("RFC");
        colRFC.setCellValueFactory(new PropertyValueFactory<>("rfc"));

        TableColumn<Evento, String> colSexo = new TableColumn<>("Sexo");
        colSexo.setCellValueFactory(new PropertyValueFactory<>("sexo"));

        TableColumn<Evento, String> colDepartamento = new TableColumn<>("Departamento");
        colDepartamento.setCellValueFactory(new PropertyValueFactory<>("departamento"));

        TableColumn<Evento, String> colPuesto = new TableColumn<>("Puesto");
        colPuesto.setCellValueFactory(new PropertyValueFactory<>("puesto"));

        TableColumn<Evento, String> colNombreEvento = new TableColumn<>("Nombre del Evento");
        colNombreEvento.setCellValueFactory(new PropertyValueFactory<>("nombreEvento"));

        TableColumn<Evento, String> colCapacitacion = new TableColumn<>("Capacitación");
        colCapacitacion.setCellValueFactory(new PropertyValueFactory<>("capacitacion"));

        TableColumn<Evento, String> colNombreFacilitador = new TableColumn<>("Nombre del Facilitador");
        colNombreFacilitador.setCellValueFactory(new PropertyValueFactory<>("nombreFacilitador"));

        TableColumn<Evento, String> colPeriodo = new TableColumn<>("Periodo");
        colPeriodo.setCellValueFactory(new PropertyValueFactory<>("periodo"));

        // Columna de botones
        TableColumn<Evento, Void> colAcreditacion = new TableColumn<>("Acreditación");
        colAcreditacion.setCellFactory(getButtonCellFactory());

        tableView.getColumns().addAll(colApellidoPaterno, colApellidoMaterno, colNombres, colRFC, colSexo, colDepartamento,
                colPuesto, colNombreEvento, colCapacitacion, colNombreFacilitador, colPeriodo, colAcreditacion);
    }

    private Callback<TableColumn<Evento, Void>, TableCell<Evento, Void>> getButtonCellFactory() {
        return param -> new TableCell<>() {
            private final Button btnAcredita = new Button();
            private final Button btnNoAcredita = new Button();

            {
                // Cargar las imágenes desde los recursos
                Image imagePalomita = new Image(getClass().getResourceAsStream("/utilerias/visualizacionDatos/Palomita.png"));
                Image imageTacha = new Image(getClass().getResourceAsStream("/utilerias/visualizacionDatos/Tacha.png"));

                // Crear ImageView para redimensionar las imágenes
                ImageView iconPalomita = new ImageView(imagePalomita);
                iconPalomita.setFitWidth(16); // Ancho del ícono
                iconPalomita.setFitHeight(16); // Alto del ícono

                ImageView iconTacha = new ImageView(imageTacha);
                iconTacha.setFitWidth(16); // Ancho del ícono
                iconTacha.setFitHeight(16); // Alto del ícono

                // Asignar los íconos redimensionados a los botones
                btnAcredita.setGraphic(iconPalomita);
                btnNoAcredita.setGraphic(iconTacha);

                // Configurar acciones de los botones
                btnAcredita.setOnAction(event -> {
                    Evento evento = getTableView().getItems().get(getIndex());
                    evento.setAcreditado(true);
                    updateButtons(evento); // Actualiza la interfaz
                });

                btnNoAcredita.setOnAction(event -> {
                    Evento evento = getTableView().getItems().get(getIndex());
                    evento.setAcreditado(false);
                    updateButtons(evento); // Actualiza la interfaz
                });
            }

            private void updateButtons(Evento evento) {
                // Cambia los gráficos de los botones según el estado
                if (evento.isAcreditado()) {
                    ImageView iconPalomita = new ImageView(new Image(getClass().getResourceAsStream("/utilerias/visualizacionDatos/Palomita.png")));
                    iconPalomita.setFitWidth(16);
                    iconPalomita.setFitHeight(16);
                    btnAcredita.setGraphic(iconPalomita);
                    btnNoAcredita.setGraphic(null); // Ocultar el botón de tacha
                } else {
                    ImageView iconTacha = new ImageView(new Image(getClass().getResourceAsStream("/utilerias/visualizacionDatos/Tacha.png")));
                    iconTacha.setFitWidth(16);
                    iconTacha.setFitHeight(16);
                    btnNoAcredita.setGraphic(iconTacha);
                    btnAcredita.setGraphic(null); // Ocultar el botón de palomita
                }
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Evento evento = getTableView().getItems().get(getIndex());
                    updateButtons(evento);

                    // Organizar los botones en un VBox para alineación vertical
                    VBox vbox = new VBox(btnAcredita, btnNoAcredita);
                    vbox.setSpacing(5);
                    setGraphic(vbox);
                }
            }
        };
    }

    private void cargarDatos() {
        try {
            // Definir las rutas para ambos archivos Excel
            String rutaArchivoEventos = "C:/excel/PRE-REGISTRO_Cursos_de_Capacitacion_FORMULARIO.xlsx";
            String rutaArchivoClasificaciones = "C:/excel/PROG-INSTITUCIONAL-ENERO-2023.xlsx"; // Asegúrate de poner la ruta correcta de tu archivo de clasificaciones

            // Llamar al método para leer los eventos y las clasificaciones
            eventoList.addAll(leerEventosDesdeExcel(rutaArchivoEventos, rutaArchivoClasificaciones));

            // Establecer los datos en la tabla
            tableView.setItems(eventoList);

        } catch (IOException e) {
            // Mostrar un mensaje de error si no se pudieron cargar los datos
            mostrarAlerta("Error", "No se pudieron cargar los datos del archivo Excel.", AlertType.ERROR);
        }
    }

    @FXML
    private void Exportar(ActionEvent event) {
        // Crear un FileChooser para seleccionar dónde guardar el archivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos Excel", "*.xlsx"));

        // Abrir el diálogo de guardar
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                // Crear una hoja en el archivo Excel
                Sheet sheet = workbook.createSheet("Lista de Asistencia");

                // Crear estilo para los encabezados
                CellStyle headerStyle = workbook.createCellStyle();
                Font font = workbook.createFont();
                font.setBold(true);
                headerStyle.setFont(font);
                headerStyle.setAlignment(CellStyle.ALIGN_LEFT);

                // Crear estilo para bordes
                CellStyle borderStyle = workbook.createCellStyle();
                borderStyle.setBorderBottom(CellStyle.BORDER_THIN);
                borderStyle.setBorderTop(CellStyle.BORDER_THIN);
                borderStyle.setBorderLeft(CellStyle.BORDER_THIN);
                borderStyle.setBorderRight(CellStyle.BORDER_THIN);

                // Extraer los datos dinámicos de la tabla
                Evento ejemplo = tableView.getItems().get(0); // Obtener el primer elemento como referencia
                String curso = ejemplo.getNombreEvento(); // Nombre del curso
                String facilitador = ejemplo.getNombreFacilitador(); // Nombre del facilitador
                String periodo = ejemplo.getPeriodo(); // Período

                // Sección de encabezado (ajustando posiciones)
                Row headerRow1 = sheet.createRow(4);
                headerRow1.createCell(1).setCellValue("NOMBRE DEL EVENTO:");
                headerRow1.createCell(2).setCellValue(curso); // Movido a columna C
                headerRow1.createCell(6).setCellValue("DURACIÓN:");
                headerRow1.createCell(9).setCellValue("HORARIO:");

                Row headerRow2 = sheet.createRow(5);
                headerRow2.createCell(1).setCellValue("NOMBRE DEL FACILITADOR (A):");
                headerRow2.createCell(2).setCellValue(facilitador); // Movido a columna C
                headerRow2.createCell(6).setCellValue("TIPO:");

                Row headerRow3 = sheet.createRow(6);
                headerRow3.createCell(1).setCellValue("PERIODO:");
                headerRow3.createCell(2).setCellValue(periodo); // Movido a columna C
                headerRow3.createCell(6).setCellValue("SEDE:");

                // Crear encabezados de tabla
                Row tableHeaderRow = sheet.createRow(8);
                String[] headers = {"No.", "NOMBRE DEL PARTICIPANTE", "R.F.C", "PUESTO Y DEPARTAMENTO DE ADSCRIPCIÓN",
                    "H", "M", "PUESTO TIPO", "ASISTENCIA", "CALIFICACIÓN"};
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = tableHeaderRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(headerStyle);
                }

                // Obtener datos de la tabla y agregarlos al Excel
                List<Evento> participantes = tableView.getItems();
                int rowIndex = 9; // Comenzar en la fila 10
                for (int i = 0; i < participantes.size(); i++) {
                    Evento participante = participantes.get(i);
                    Row row = sheet.createRow(rowIndex++);

                    // Llenar datos
                    row.createCell(0).setCellValue(i + 1); // Número
                    row.createCell(1).setCellValue(participante.getNombres()); // Nombre del participante
                    row.createCell(2).setCellValue(participante.getRfc()); // RFC
                    row.createCell(3).setCellValue(participante.getDepartamento()); // Departamento

                    // Sexo (H/M)
                    row.createCell(4).setCellValue(participante.getSexo().equalsIgnoreCase("Hombre") ? "X" : "");
                    row.createCell(5).setCellValue(participante.getSexo().equalsIgnoreCase("Mujer") ? "X" : "");

                    // Opcional: Rellenar otras columnas si los datos están disponibles
                    if (participante.getPuesto() != null) {
                        row.createCell(6).setCellValue(participante.getPuesto()); // Puesto Tipo
                    }

                    // Aplicar estilo de borde a todas las celdas
                    for (int j = 0; j < headers.length; j++) {
                        if (row.getCell(j) != null) {
                            row.getCell(j).setCellStyle(borderStyle);
                        }
                    }
                }

                // Ajustar automáticamente el tamaño de las columnas
                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                // Agregar filas para las firmas
                int firmaRowIndex = rowIndex + 5; // Dejar un espacio debajo de la tabla
                Row firmaRow1 = sheet.createRow(firmaRowIndex);
                firmaRow1.createCell(1).setCellValue("NOMBRE Y FIRMA DEL FACILITADOR(A)");
                Row firmaRow2 = sheet.createRow(firmaRowIndex + 1); // Tres filas más abajo
                firmaRow2.createCell(5).setCellValue("NOMBRE Y FIRMA DEL COORDINADOR(A) DE ACTUALIZACIÓN DOCENTE");

                // Escribir el archivo Excel en disco
                try (FileOutputStream fileOut = new FileOutputStream(file)) {
                    workbook.write(fileOut);
                }

                System.out.println("Archivo Excel exportado con éxito");

            } catch (IOException e) {
                System.out.println("Error al exportar: " + e.getMessage());
            }
        }
    }

    @FXML
    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

   @FXML
private void buscarPorEnter(KeyEvent event) {
    // Verifica si la tecla presionada es "Enter"
    if (event.getCode() == KeyCode.ENTER) {
        System.out.println("Enter presionado");
        Buscar(); // Llama al método de búsqueda
    }
}

    @FXML
    private void Buscar() {
        String textoBusqueda = campoBusqueda.getText().trim().toLowerCase();

        // Si el campo de búsqueda está vacío, mostramos una advertencia.
        if (textoBusqueda.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Campo de Búsqueda Vacío");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, introduce un valor para buscar.");
            alert.showAndWait();
            return;
        }

        // Filtra los datos según el texto de búsqueda.
        FilteredList<Evento> datosFiltrados = new FilteredList<>(eventoList, p -> true);
        datosFiltrados.setPredicate(info -> {
            return info.getNombres().toLowerCase().contains(textoBusqueda)
                    || info.getApellidoPaterno().toLowerCase().contains(textoBusqueda)
                    || info.getApellidoMaterno().toLowerCase().contains(textoBusqueda)
                    || info.getRfc().toLowerCase().contains(textoBusqueda)
                    || info.getSexo().toLowerCase().contains(textoBusqueda)
                    || info.getNombreEvento().toLowerCase().contains(textoBusqueda)
                    || info.getDepartamento().toLowerCase().contains(textoBusqueda)
                    || info.getNombreFacilitador().toLowerCase().contains(textoBusqueda)
                    || info.getPeriodo().toLowerCase().contains(textoBusqueda)
                    || info.getPuesto().toLowerCase().contains(textoBusqueda);
        });

        // Si no se encuentra ningún resultado, mostramos un mensaje de información y restauramos la tabla completa.
        if (datosFiltrados.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Resultado de Búsqueda");
            alert.setHeaderText(null);
            alert.setContentText("No se encontró ningún resultado para \"" + textoBusqueda + "\".");
            alert.showAndWait();

            // Restauramos la tabla con todos los datos
            tableView.setItems(eventoList);
        } else {
            // Si hay resultados, mostramos solo los datos filtrados.
            tableView.setItems(datosFiltrados);
        }

        // Limpiar el campo de búsqueda después de realizar la búsqueda
        campoBusqueda.clear();
    }

    @FXML
    private void actuzalizar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ImportacionArchivos.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) botonActualizar.getScene().getWindow(); // Obtener la ventana actual
            stage.setScene(new Scene(root)); // Mostrar la nueva escena
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modificar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModificacionDatos.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) botonModificar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            // Crear un libro de Excel
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Datos");

            // Crear una fila de encabezados
            Row headerRow = sheet.createRow(0);
            String[] columnas = {
                "ApellidoPaterno", "ApellidoMaterno", "Nombres",
                "RFC", "Sexo", "Departamento", "Puesto", "NombreEvento", "Capacitacion",
                "NombreFacilitador", "Periodo", "Acreditación"
            };

            // Añadir encabezados a la fila
            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
            }

            // Añadir datos desde el TableView
            int rowIndex = 1; // Empezar en la segunda fila
            for (Evento evento : tableView.getItems()) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(2).setCellValue(evento.getApellidoPaterno());
                row.createCell(3).setCellValue(evento.getApellidoMaterno());
                row.createCell(4).setCellValue(evento.getNombres());
                row.createCell(5).setCellValue(evento.getRfc());
                row.createCell(6).setCellValue(evento.getSexo());
                row.createCell(7).setCellValue(evento.getDepartamento());
                row.createCell(8).setCellValue(evento.getPuesto());
                row.createCell(9).setCellValue(evento.getNombreEvento());
                row.createCell(10).setCellValue(evento.getCapacitacion());
                row.createCell(11).setCellValue(evento.getNombreFacilitador());
                row.createCell(12).setCellValue(evento.getPeriodo());
                row.createCell(13).setCellValue(evento.isAcreditado() ? "Sí acreditó" : "No acreditó");
            }

            // Autoajustar el ancho de las columnas
            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Guardar el archivo en C:\excel\datos_guardados.xlsx
            String filePath = "C:/excel/datos_guardados.xlsx";
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            workbook.close();
            return true; // Indica que se guardó correctamente

        } catch (IOException e) {
            e.printStackTrace(); // Imprime el error para depuración
            return false; // Indica que hubo un error al guardar
        }
    }

    //Métodos de los botones de la barra superior :)
    public void cerrarVentana(MouseEvent event) throws IOException {
        ControladorGeneral.cerrarVentana(event, "¿Quieres cerrar sesión?", getClass());
    }

    public void minimizarVentana(MouseEvent event) {
        ControladorGeneral.minimizarVentana(event);
    }

    public void regresarVentana(MouseEvent event) throws IOException {
        ControladorGeneral.regresar(event, "Principal", getClass());
    }

    public static class Evento {

        private String apellidoPaterno, apellidoMaterno, nombres, rfc, sexo, departamento, puesto, nombreEvento, capacitacion, nombreFacilitador, periodo;
        private BooleanProperty acreditacion;

        public Evento(String apellidoPaterno, String apellidoMaterno, String nombres,
                String rfc, String sexo, String departamento, String puesto, String nombreEvento, String capacitacion, String nombreFacilitador,
                String periodo, Boolean acreditacion) {
            this.apellidoPaterno = apellidoPaterno;
            this.apellidoMaterno = apellidoMaterno;
            this.nombres = nombres;
            this.rfc = rfc;
            this.sexo = sexo;
            this.departamento = departamento;
            this.puesto = puesto;
            this.nombreEvento = nombreEvento;
            this.capacitacion = capacitacion;
            this.nombreFacilitador = nombreFacilitador;
            this.periodo = periodo;
            this.acreditacion = new SimpleBooleanProperty(acreditacion);
        }

        public BooleanProperty acreditadoProperty() {
            return acreditacion;
        }

        public void setAcreditado(boolean acreditado) {
            this.acreditacion.set(acreditado);
        }

        public boolean isAcreditado() {
            return acreditacion.get();
        }

        public String getApellidoPaterno() {
            return apellidoPaterno;
        }

        public String getApellidoMaterno() {
            return apellidoMaterno;
        }

        public String getNombres() {
            return nombres;
        }

        public String getRfc() {
            return rfc;
        }

        public String getSexo() {
            return sexo;
        }

        public String getDepartamento() {
            return departamento;
        }

        public String getPuesto() {
            return puesto;
        }

        public String getNombreEvento() {
            return nombreEvento;
        }

        public String getCapacitacion() {
            return capacitacion;
        }

        public void setCapacitacion(String capacitacion) {
            this.capacitacion = capacitacion;
        }

        public String getNombreFacilitador() {
            return nombreFacilitador;
        }

        public String getPeriodo() {
            return periodo;
        }

        public BooleanProperty getAcreditacion() {
            return acreditacion;
        }

        public void setAcreditacion(Boolean acreditacion) {
            this.acreditacion = new SimpleBooleanProperty(acreditacion);
        }

        public Evento() {
            this.acreditacion = new SimpleBooleanProperty(false); // Inicialmente no marcado
        }

    }

    // Método principal para leer eventos desde el archivo Excel
    public static List<Evento> leerEventosDesdeExcel(String rutaArchivoEventos, String rutaArchivoClasificaciones) throws IOException {
        // Cargar clasificaciones desde el segundo archivo Excel
        HashMap<String, String> clasificaciones = cargarClasificacionesDesdeExcel(rutaArchivoClasificaciones);

        List<Evento> eventos = new ArrayList<>();

        try (FileInputStream archivo = new FileInputStream(rutaArchivoEventos); Workbook workbook = new XSSFWorkbook(archivo)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Saltar la fila de encabezados
                }

                String apellidoPaterno = getCellValue(row.getCell(4));
                String apellidoMaterno = getCellValue(row.getCell(5));
                String nombres = getCellValue(row.getCell(6));
                String rfc = getCellValue(row.getCell(7));
                String sexo = getCellValue(row.getCell(8));
                String departamento = getCellValue(row.getCell(9));
                String puesto = getCellValue(row.getCell(10));
                String nombreEvento = getCellValue(row.getCell(11));
                String nombreFacilitador = getCellValue(row.getCell(12));
                String periodo = getCellValue(row.getCell(13));
                Boolean acreditacion = "Sí".equalsIgnoreCase(getCellValue(row.getCell(14)));

                // Obtener la clasificación desde el mapa de clasificaciones
                String capacitacion = clasificaciones.getOrDefault(nombreEvento, "Sin clasificación");

                eventos.add(new Evento(apellidoPaterno, apellidoMaterno, nombres, rfc, sexo,
                        departamento, puesto, nombreEvento, capacitacion, nombreFacilitador, periodo, acreditacion));
            }
        }
        return eventos;
    }

    // Método para cargar las clasificaciones desde el archivo Excel
    private static HashMap<String, String> cargarClasificacionesDesdeExcel(String rutaArchivoClasificaciones) throws IOException {
        HashMap<String, String> clasificaciones = new HashMap<>();

        try (FileInputStream archivo = new FileInputStream(rutaArchivoClasificaciones); Workbook workbook = new XSSFWorkbook(archivo)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Saltar la fila de encabezados
                }

                String nombreEvento = getCellValue(row.getCell(1)); // Supongamos que columna 0 tiene el nombre del evento
                String tipoCapacitacion = getCellValue(row.getCell(2)); // Supongamos que columna 1 tiene el tipo

                if (!nombreEvento.isEmpty() && !tipoCapacitacion.isEmpty()) {
                    clasificaciones.put(nombreEvento, tipoCapacitacion);
                }
            }
        }
        return clasificaciones;
    }

    // Método para obtener el valor de una celda como String
    private static String getCellValue(Cell cell) {
        return cell != null ? cell.toString().trim() : "";
    }
}
