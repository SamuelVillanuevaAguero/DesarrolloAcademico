package vistas;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilerias.general.ControladorGeneral;

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
        TableColumn<Evento, String> colHoraInicio = new TableColumn<>("Fecha de Inicio");
        colHoraInicio.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));

        TableColumn<Evento, String> colHoraFinal = new TableColumn<>("Fecha de Finalización");
        colHoraFinal.setCellValueFactory(new PropertyValueFactory<>("horaFinal"));

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

        TableColumn<Evento, String> colNombreFacilitador = new TableColumn<>("Nombre del Facilitador");
        colNombreFacilitador.setCellValueFactory(new PropertyValueFactory<>("nombreFacilitador"));

        TableColumn<Evento, String> colPeriodo = new TableColumn<>("Periodo");
        colPeriodo.setCellValueFactory(new PropertyValueFactory<>("periodo"));

        // Columna de botones
        TableColumn<Evento, Void> colAcreditacion = new TableColumn<>("Acreditación");
        colAcreditacion.setCellFactory(getButtonCellFactory());

        tableView.getColumns().addAll(colHoraInicio, colHoraFinal, colApellidoPaterno,
                colApellidoMaterno, colNombres, colRFC, colSexo, colDepartamento,
                colPuesto, colNombreEvento, colNombreFacilitador, colPeriodo, colAcreditacion);
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
            eventoList.addAll(ExcelReader.leerEventosDesdeExcel("C:/excel/PRE-REGISTRO_Cursos_de_Capacitacion_FORMULARIO.xlsx"));
            tableView.setItems(eventoList);
        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudieron cargar los datos del archivo Excel.", AlertType.ERROR);
        }
    }

    @FXML
    private void Exportar(ActionEvent event) {
        // Crear un FileChooser para seleccionar dónde guardar el archivo
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV", "ListaAsistencia.csv"));

        // Abrir el diálogo de guardar
        var file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                // Escribir encabezados en el archivo CSV
                writer.write("Nombre del Participante, Curso\n");

                // Recorrer las filas de la tabla
                List<Evento> participantes = tableView.getItems();
                for (Evento participante : participantes) {
                    // Filtrar por curso y acreditación
                    if (participante.getNombreEvento().equals(campoBusqueda) && participante.isAcreditado()) {
                        writer.write(participante.getNombres() + ", " + participante.getNombreEvento() + "\n");
                    }
                }

                System.out.println("Archivo exportado con éxito");
            } catch (IOException e) {
                System.out.println("Error al exportar: " + e.getMessage());
            }
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    
    @FXML
    private void Buscar(ActionEvent event) {
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
                    || info.getHoraInicio().toLowerCase().contains(textoBusqueda)
                    || info.getHoraFinal().toLowerCase().contains(textoBusqueda)
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
            "HoraInicio", "HoraFinal", "ApellidoPaterno", "ApellidoMaterno", "Nombres",
            "RFC", "Sexo", "Departamento", "Puesto", "NombreEvento",
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
            row.createCell(0).setCellValue(evento.getHoraInicio());
            row.createCell(1).setCellValue(evento.getHoraFinal());
            row.createCell(2).setCellValue(evento.getApellidoPaterno());
            row.createCell(3).setCellValue(evento.getApellidoMaterno());
            row.createCell(4).setCellValue(evento.getNombres());
            row.createCell(5).setCellValue(evento.getRfc());
            row.createCell(6).setCellValue(evento.getSexo());
            row.createCell(7).setCellValue(evento.getDepartamento());
            row.createCell(8).setCellValue(evento.getPuesto());
            row.createCell(9).setCellValue(evento.getNombreEvento());
            row.createCell(10).setCellValue(evento.getNombreFacilitador());
            row.createCell(11).setCellValue(evento.getPeriodo());
            row.createCell(12).setCellValue(evento.isAcreditado() ? "Sí acreditó" : "No acreditó");
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
        private String horaInicio, horaFinal, apellidoPaterno, apellidoMaterno, nombres, rfc, sexo, departamento, puesto, nombreEvento, nombreFacilitador, periodo;
        private BooleanProperty acreditacion;

        public Evento(String horaInicio, String horaFinal, String apellidoPaterno, String apellidoMaterno, String nombres, 
                      String rfc, String sexo, String departamento, String puesto, String nombreEvento, String nombreFacilitador, 
                      String periodo, Boolean acreditacion) {
            this.horaInicio = horaInicio;
            this.horaFinal = horaFinal;
            this.apellidoPaterno = apellidoPaterno;
            this.apellidoMaterno = apellidoMaterno;
            this.nombres = nombres;
            this.rfc = rfc;
            this.sexo = sexo;
            this.departamento = departamento;
            this.puesto = puesto;
            this.nombreEvento = nombreEvento;
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
        public String getHoraInicio() {
            return horaInicio;
        }

        public String getHoraFinal() {
            return horaFinal;
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

    public static class ExcelReader {
        public static List<Evento> leerEventosDesdeExcel(String rutaArchivo) throws IOException {
            List<Evento> eventos = new ArrayList<>();
            try (FileInputStream archivo = new FileInputStream(rutaArchivo); Workbook workbook = new XSSFWorkbook(archivo)) {
                Sheet sheet = workbook.getSheetAt(0);
                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue;

                    String horaInicio = getCellValue(row.getCell(1));
                    String horaFinal = getCellValue(row.getCell(2));
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

                    eventos.add(new Evento(horaInicio, horaFinal, apellidoPaterno, apellidoMaterno, nombres, rfc, sexo, 
                                           departamento, puesto, nombreEvento, nombreFacilitador, periodo, acreditacion));
                }
            }
            return eventos;
        }

        private static String getCellValue(Cell cell) {
            return cell != null ? cell.toString() : "";
        }
    }

}
