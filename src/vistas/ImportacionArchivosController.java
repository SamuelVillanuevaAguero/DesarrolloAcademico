package vistas;

import java.io.File;
import static java.io.File.separator;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.*;
import javafx.collections.FXCollections;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.stage.*;
import utilerias.general.ControladorGeneral;

/**
 * FXML Controller class
 *
 * @author Samue
 */
public class ImportacionArchivosController implements Initializable {

    private File programaCapacitacion;
    private File listado;
    private File formato;
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
    private Button botonPC;
    @FXML
    private Button botonListado;
    @FXML
    private Button botonFormato;
    @FXML
    private Button botonGuardar;
    @FXML
    private ComboBox<String> comboBoxListados;
    @FXML
    private ComboBox<String> comboBoxFormatos;
    @FXML
    private Label labelPC;
    @FXML
    private Label labelListado;
    @FXML
    private Label labelFormato;

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

    public void cargarProgramaCap(MouseEvent event) {
        FileChooser selectorArchivos = new FileChooser();
        selectorArchivos.setTitle("Seleccionar archivo");
        selectorArchivos.setInitialDirectory(new File(System.getProperty("user.home")));
        selectorArchivos.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Todos los archivos", "*.xlsx", "*.xls"),
                //new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Excel", "*.xlsx", "*.xls")
        //new FileChooser.ExtensionFilter("Word", "*.doc", "*.docx")
        );

        programaCapacitacion = selectorArchivos.showOpenDialog(botonPC.getScene().getWindow());
        if (programaCapacitacion != null) {
            labelPC.setText(programaCapacitacion.getName());
        }
    }

    public void cargarFormato(MouseEvent event) {
        if (comboBoxFormatos.getValue() == null) {
            mostrarError("Debe seleccionar primero una opción de formato.");
            return;
        }

        FileChooser selectorArchivos = new FileChooser();
        selectorArchivos.setTitle("Seleccionar archivo");
        selectorArchivos.setInitialDirectory(new File(System.getProperty("user.home")));

        String tipoFormato = comboBoxFormatos.getValue();
        if (tipoFormato.equals("Formato de hojas membretadas para reconocimientos")) {
            selectorArchivos.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf")
            );
        } else {
            selectorArchivos.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Archivos Excel", "*.xlsx", "*.xls")
            );
        }

        formato = selectorArchivos.showOpenDialog(botonPC.getScene().getWindow());
        if (formato != null) {
            labelFormato.setText(formato.getName());
        }
    }

    public void cargarListado(MouseEvent event) {
        if (comboBoxListados.getValue() == null) {
            mostrarError("Debe seleccionar primero una opción de listado.");
            return;
        }

        FileChooser selectorArchivos = new FileChooser();
        selectorArchivos.setTitle("Seleccionar archivo");
        selectorArchivos.setInitialDirectory(new File(System.getProperty("user.home")));
        selectorArchivos.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Archivos Excel", "*.xlsx", "*.xls")
        );

        listado = selectorArchivos.showOpenDialog(botonPC.getScene().getWindow());
        if (listado != null) {
            labelListado.setText(listado.getName());
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void inicializarEstructuraDirectorios() {
        String directorioUsuario = System.getProperty("user.home");
        String separador = File.separator;
        String directorioBase = directorioUsuario + separador + "Desktop" + separador + "Gestion_de_Cursos";

        // Obtener año actual
        Calendar calendario = Calendar.getInstance();
        int year = calendario.get(Calendar.YEAR);
        int mesActual = calendario.get(Calendar.MONTH) + 1;

        // Definir la estructura de directorios
        String carpetaPeriodo = (mesActual >= 1 && mesActual <= 7) ? "1-" + year : "2-" + year;
        String directorioImportados = directorioBase + separador + "Archivos_importados" + separador + year + separador + carpetaPeriodo;
        String directorioExportados = directorioBase + separador + "Archivos_exportados" + separador + year + separador + carpetaPeriodo;
        String directorioSistema = directorioBase + separador + "Sistema" + separador + year + separador + carpetaPeriodo;

        // Crear directorio base
        crearDirectorio(directorioBase);

        // Crear estructura de año y período
        //crearDirectorio(directorioBase + separador + year);
        crearDirectorio(directorioImportados);
        crearDirectorio(directorioExportados);
        crearDirectorio(directorioSistema);

        // Crear subdirectorios de archivos importados
        crearDirectorio(directorioImportados + separador + "formato_de_hojas_membretadas_para_reconocimientos");
        crearDirectorio(directorioImportados + separador + "formato_de_lista_de_asistencias");
        crearDirectorio(directorioImportados + separador + "formato_de_reporte_para_docentes_capacitados");
        crearDirectorio(directorioImportados + separador + "listado_de_pre_regitro_a_cursos_de_capacitacion");
        crearDirectorio(directorioImportados + separador + "listado_de_etiquetas_de_cursos");
        crearDirectorio(directorioImportados + separador + "listado_de_deteccion_de_necesidades");
        crearDirectorio(directorioImportados + separador + "programa_institucional");

        // Crear subdirectorios de archivos exportados
        crearDirectorio(directorioExportados + separador + "listas_asistencia");
        crearDirectorio(directorioExportados + separador + "reconocimientos");
        crearDirectorio(directorioExportados + separador + "reportes_estadisticos");

        // Crear estructura de "Sistema"
        crearDirectorio(directorioSistema + separador + "condensados_vista_de_visualizacion_de_datos");
        crearDirectorio(directorioSistema + separador + "informacion_modificable");
        crearDirectorio(directorioSistema + separador + "registros_contraseñas");
    }

    private void crearDirectorio(String ruta) {
        File directorio = new File(ruta);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
    }

    public void guardarArchivos(MouseEvent evento) {
        String directorioUsuario = System.getProperty("user.home");
        String separador = File.separator;
        List<String> archivosImportados = new ArrayList<>();
        boolean archivoImportadoExitoso = false;

        // Determinar el período actual
        Calendar calendario = Calendar.getInstance();
        int year = calendario.get(Calendar.YEAR);
        int mesActual = calendario.get(Calendar.MONTH) + 1;

        String carpetaPeriodo = (mesActual >= 1 && mesActual <= 7)
                ? "1-" + year
                : "2-" + year;

        // Ruta base para archivos importados
        String directorioBase = directorioUsuario + separador + "Desktop" + separador + "Gestion_de_Cursos";
        String directorioImportados = directorioBase + separador + "Archivos_importados"
                + separador + year + separador + carpetaPeriodo;

        // Manejar Programa de Capacitación
        if (programaCapacitacion != null) {
            String dirPC = directorioImportados + separator + "programa_institucional";
            File directorioPC = new File(dirPC);

            // Encontrar el siguiente número de semana disponible
            int numeroSemana = 1;
            String extension = getExtensionArchivo(programaCapacitacion);
            File archivoDestino;
            do {
                archivoDestino = new File(dirPC + separador + "programa_institucional_(Semana_"
                        + numeroSemana + ")" + extension);
                numeroSemana++;
            } while (archivoDestino.exists());

            try {
                java.nio.file.Files.copy(programaCapacitacion.toPath(), archivoDestino.toPath());
                archivosImportados.add("programa_institucional");
                archivoImportadoExitoso = true;
            } catch (IOException e) {
                mostrarError("Error al copiar el programa institucional: " + e.getMessage());
            }
        }

        // Manejar Listados según selección del ComboBox
        if (listado != null && comboBoxListados.getValue() != null) {
            String tipoListado = comboBoxListados.getValue();
            String nombreCarpeta = convertirNombreCarpeta(tipoListado);
            String dirListado = directorioImportados + separador + nombreCarpeta;

            int numeroSemana = 1;
            String extension = getExtensionArchivo(listado);
            File archivoDestino;
            do {
                archivoDestino = new File(dirListado + separador + "listado_(Semana_"
                        + numeroSemana + ")" + extension);
                numeroSemana++;
            } while (archivoDestino.exists());

            try {
                java.nio.file.Files.copy(listado.toPath(), archivoDestino.toPath());
                archivosImportados.add(nombreCarpeta);
                archivoImportadoExitoso = true;
            } catch (IOException e) {
                mostrarError("Error al copiar el listado: " + e.getMessage());
            }
        }

        // Manejar Formatos según selección del ComboBox
        if (formato != null && comboBoxFormatos.getValue() != null) {
            String tipoFormato = comboBoxFormatos.getValue();
            String nombreCarpeta = convertirNombreCarpeta(tipoFormato);
            String dirFormato = directorioImportados + separador + nombreCarpeta;

            int numeroSemana = 1;
            String extension = getExtensionArchivo(formato);
            File archivoDestino;
            do {
                archivoDestino = new File(dirFormato + separador + "formato_(Version_"
                        + numeroSemana + ")" + extension);
                numeroSemana++;
            } while (archivoDestino.exists());

            try {
                java.nio.file.Files.copy(formato.toPath(), archivoDestino.toPath());
                archivosImportados.add(nombreCarpeta);
                archivoImportadoExitoso = true;
            } catch (IOException e) {
                mostrarError("Error al copiar el formato: " + e.getMessage());
            }
        }

        if (archivoImportadoExitoso) {
            String mensaje = "Se importó correctamente el archivo en la(s) carpeta(s): "
                    + String.join(", ", archivosImportados)
                    + " del período " + carpetaPeriodo;
            mostrarMensajeExito(mensaje);
            limpiarCampos();
        } else {
            mostrarError("No se ha seleccionado ningún archivo para importar");
        }
    }

    private void limpiarCampos() {
        // Primero limpiamos los labels y archivos
        labelPC.setText("");
        labelListado.setText("");
        labelFormato.setText("");
        programaCapacitacion = null;
        listado = null;
        formato = null;

        comboBoxListados.setValue(null);
        comboBoxFormatos.setValue(null);
        comboBoxListados.setPromptText("Selecciona una opción");
        comboBoxFormatos.setPromptText("Selecciona una opción");
    }

// Método auxiliar para obtener la extensión del archivo
    private String getExtensionArchivo(File archivo) {
        String nombreArchivo = archivo.getName();
        int lastIndexOf = nombreArchivo.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return nombreArchivo.substring(lastIndexOf);
    }

// Método auxiliar para convertir nombres de ComboBox a nombres de carpeta
    private String convertirNombreCarpeta(String nombre) {
        return nombre.toLowerCase()
                .replace(" ", "_")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("ñ", "n");
    }


// Método para mostrar mensaje de éxito
    private void mostrarMensajeExito(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Éxito");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        inicializarEstructuraDirectorios();

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

        comboBoxListados.setValue(null);
        comboBoxFormatos.setValue(null);

        comboBoxListados.setItems(FXCollections.observableArrayList(
                "Listado de pre regitro a cursos de capacitación",
                "Listado de etiquetas de cursos",
                "Listado de detección de necesidades"));

        comboBoxFormatos.setItems(FXCollections.observableArrayList(
                "Formato de hojas membretadas para reconocimientos",
                "Formato de lista de asistencias",
                "Formato de reporte para docentes capacitados"));

        comboBoxListados.setPromptText("Selecciona una opción");
        comboBoxFormatos.setPromptText("Selecciona una opción");

        botonPC.setOnMouseClicked(event -> {
            cargarProgramaCap(event);
        });

        botonListado.setOnMouseClicked(event -> {
            cargarListado(event);
        });

        botonFormato.setOnMouseClicked(event -> {
            cargarFormato(event);
        });

        botonGuardar.setOnMouseClicked(event -> {
            guardarArchivos(event);
        });

    }

}
