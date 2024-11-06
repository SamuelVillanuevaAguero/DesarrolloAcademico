
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
                new FileChooser.ExtensionFilter("Todos los archivos", "*.pdf", "*.xlsx", "*.xls", "*.doc", "*.docx"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Excel", "*.xlsx", "*.xls"),
                new FileChooser.ExtensionFilter("Word", "*.doc", "*.docx")
        );

        programaCapacitacion = selectorArchivos.showOpenDialog(botonPC.getScene().getWindow());
        if (programaCapacitacion != null) {
            labelPC.setText(programaCapacitacion.getName());
        }
    }

    public void cargarListado(MouseEvent event) {
        FileChooser selectorArchivos = new FileChooser();
        selectorArchivos.setTitle("Seleccionar archivo");
        selectorArchivos.setInitialDirectory(new File(System.getProperty("user.home")));
        selectorArchivos.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Todos los archivos", "*.pdf", "*.xlsx", "*.xls", "*.doc", "*.docx"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Excel", "*.xlsx", "*.xls"),
                new FileChooser.ExtensionFilter("Word", "*.doc", "*.docx")
        );

        listado = selectorArchivos.showOpenDialog(botonPC.getScene().getWindow());
        if (listado != null) {
            labelListado.setText(listado.getName());
        }
    }

    public void cargarFormato(MouseEvent event) {
        FileChooser selectorArchivos = new FileChooser();
        selectorArchivos.setTitle("Seleccionar archivo");
        selectorArchivos.setInitialDirectory(new File(System.getProperty("user.home")));
        selectorArchivos.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Todos los archivos", "*.pdf", "*.xlsx", "*.xls", "*.doc", "*.docx"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Excel", "*.xlsx", "*.xls"),
                new FileChooser.ExtensionFilter("Word", "*.doc", "*.docx")
        );

        formato = selectorArchivos.showOpenDialog(botonPC.getScene().getWindow());
        if (formato != null) {
            labelFormato.setText(formato.getName());
        }
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
        Map<String, List<String>> estructura = new HashMap<>();
        
        // Estructura para Archivos_exportados
        List<String> subDirExportados = Arrays.asList(
            "listas_asistencia",
            "reconocimientos",
            "reportes_estadisticos"
        );
        
        // Estructura para Archivos_importados
        List<String> subDirImportados = Arrays.asList(
            "formato_de_hojas_de_reportes_estadisticos_docentes",
            "formato_de_hojas_membretadas_para_reconocimientos",
            "formato_de_lista_de_asistencias",
            "formato_de_reporte_para_docentes_con_necesidad_de_capacitacion",
            "listado_de_cursos_de_capacitacion_a_impartir_con_folio",
            "listado_de_inscritos_a_cursos_de_capacitacion",
            "listado_de_necesidad_de_capacitacion",
            "programas_de_capacitacion"
        );
        
        // Estructura para Otros
        List<String> subDirOtros = Arrays.asList(
            "condensados_vista_de_visualizacion_de_datos",
            "informacion_modificable",
            "registros_contraseñas"
        );

        // Crear directorio base
        crearDirectorio(directorioBase);

        // Crear estructura principal
        String[] directoriosPrincipales = {"Archivos_exportados", "Archivos_importados", "Otros"};
        for (String dirPrincipal : directoriosPrincipales) {
            String rutaPrincipal = directorioBase + separador + dirPrincipal;
            crearDirectorio(rutaPrincipal);

            if (dirPrincipal.equals("Otros")) {
                // Crear subdirectorios de Otros
                for (String subDir : subDirOtros) {
                    crearDirectorio(rutaPrincipal + separador + subDir);
                }
            } else {
                // Crear estructura de año actual
                String rutaYear = rutaPrincipal + separador + year;
                crearDirectorio(rutaYear);

                // Crear directorios de períodos
                String[] periodos = {"1-" + year, "2-" + year};
                for (String periodo : periodos) {
                    String rutaPeriodo = rutaYear + separador + periodo;
                    crearDirectorio(rutaPeriodo);

                    // Crear subdirectorios correspondientes
                    List<String> subDirs = dirPrincipal.equals("Archivos_exportados") ? 
                                         subDirExportados : subDirImportados;
                    for (String subDir : subDirs) {
                        crearDirectorio(rutaPeriodo + separador + subDir);
                    }
                }
            }
        }
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
        
        String carpetaPeriodo = (mesActual >= 1 && mesActual <= 7) ? 
                               "1-" + year : 
                               "2-" + year;

        // Ruta base para archivos importados
        String directorioBase = directorioUsuario + separador + "Desktop" + separador + "Gestion_de_Cursos";
        String directorioImportados = directorioBase + separador + "Archivos_importados" + 
                                    separador + year + separador + carpetaPeriodo;

        // Manejar Programa de Capacitación
        if (programaCapacitacion != null) {
            String dirPC = directorioImportados + separator + "programas_de_capacitacion";
            File directorioPC = new File(dirPC);
            
            // Encontrar el siguiente número de semana disponible
            int numeroSemana = 1;
            String extension = getExtensionArchivo(programaCapacitacion);
            File archivoDestino;
            do {
                archivoDestino = new File(dirPC + separador + "programa_de_capacitacion_(Semana_" + 
                                        numeroSemana + ")" + extension);
                numeroSemana++;
            } while (archivoDestino.exists());

            try {
                java.nio.file.Files.copy(programaCapacitacion.toPath(), archivoDestino.toPath());
                archivosImportados.add("programas_de_capacitacion");
                archivoImportadoExitoso = true;
            } catch (IOException e) {
                mostrarError("Error al copiar el programa de capacitación: " + e.getMessage());
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
                archivoDestino = new File(dirListado + separador + "listado_(Semana_" + 
                                        numeroSemana + ")" + extension);
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
                archivoDestino = new File(dirFormato + separador + "formato_(Version_" + 
                                        numeroSemana + ")" + extension);
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
            String mensaje = "Se importó correctamente el archivo en la(s) carpeta(s): " +
                           String.join(", ", archivosImportados) +
                           " del período " + carpetaPeriodo;
            mostrarMensajeExito(mensaje);
            limpiarCampos();
        } else {
            mostrarError("No se ha seleccionado ningún archivo para importar");
        }
    }

    private void limpiarCampos() {
        System.out.println("si entre ehhh ");
        // Primero limpiamos los labels y archivos
        labelPC.setText("");
        labelListado.setText("");
        labelFormato.setText("");
        programaCapacitacion = null;
        listado = null;
        formato = null;

        comboBoxListados.setValue(null);
        comboBoxFormatos.setValue(null);
        System.out.println("aqui toy ");
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

// Método para mostrar errores
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
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
                "Listado de cursos de capacitación a impartir con folio",
                "Listado de inscritos a cursos de capacitación",
                "Listado de necesidad de capacitación"));

        comboBoxFormatos.setItems(FXCollections.observableArrayList(
                "Formato de hojas membretadas para reconocimientos",
                "Formato de hojas de reportes estadísticos docentes",
                "Formato de lista de asistencias",
                "Formato de reporte para docentes con necesidad de capacitación"));

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
