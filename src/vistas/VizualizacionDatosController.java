package vistas;

import java.io.FileInputStream;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
       
       // Configuración de la columna de acreditación
TableColumn<Evento, Boolean> colAcreditacion = new TableColumn<>("Acreditación");
colAcreditacion.setCellValueFactory(cellData -> cellData.getValue().acreditadoProperty());

// Configuración de CheckBoxTableCell correctamente enlazado a la propiedad Boolean
colAcreditacion.setCellFactory(CheckBoxTableCell.forTableColumn(colAcreditacion));

// Agregar todas las columnas, incluida la acreditación
tableView.getColumns().addAll(colHoraInicio, colHoraFinal, colApellidoPaterno,
        colApellidoMaterno, colNombres, colRFC, colSexo, colDepartamento,
        colPuesto, colNombreEvento, colNombreFacilitador, colPeriodo, colAcreditacion);

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
        mostrarAlerta("Exportar", "La función Exportar aún no está implementada.", AlertType.INFORMATION);
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

    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
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

        private String horaInicio;
        private String horaFinal;
        private String apellidoPaterno;
        private String apellidoMaterno;
        private String nombres;
        private String rfc;
        private String sexo;
        private String departamento;
        private String puesto;
        private String nombreEvento;
        private String nombreFacilitador;
        private String periodo;
        private BooleanProperty acreditacion;

        public Evento(String horaInicio, String horaFinal, String apellidoPaterno, String apellidoMaterno,
                String nombres, String rfc, String sexo, String departamento, String puesto,
                String nombreEvento, String nombreFacilitador, String periodo, Boolean acreditacion) {
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

    public boolean isAcreditado() {
        return acreditacion.get();
    }

    public void setAcreditado(boolean acreditado) {
        this.acreditacion.set(acreditado);
    }

    public BooleanProperty acreditadoProperty() {
        return acreditacion;
    }
        
        
        
    }

   public static class ExcelReader {

    public static List<Evento> leerEventosDesdeExcel(String rutaArchivo) throws IOException {
        List<Evento> eventos = new ArrayList<>();
        try (FileInputStream archivo = new FileInputStream(rutaArchivo); Workbook workbook = new XSSFWorkbook(archivo)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Saltar encabezado
                }

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
                
                // Convertir el valor de acreditación a Boolean
                String acreditacionTexto = getCellValue(row.getCell(14));
                Boolean acreditacion = acreditacionTexto.equalsIgnoreCase("Sí")|| acreditacionTexto.equals("true") || acreditacionTexto.equals("1");

                // Crear el objeto Evento con el valor Booleano de acreditación
                eventos.add(new Evento(horaInicio, horaFinal, apellidoPaterno, apellidoMaterno,
                        nombres, rfc, sexo, departamento, puesto, nombreEvento,
                        nombreFacilitador, periodo, acreditacion));
            }
        }
        return eventos;
    }

    private static String getCellValue(Cell cell) {
        return cell != null ? cell.toString() : "";
    }
}

}
