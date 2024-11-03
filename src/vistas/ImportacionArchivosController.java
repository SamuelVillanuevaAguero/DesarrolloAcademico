/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vistas;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.ResourceBundle;
import java.util.logging.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
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

    public void guardarArchivos(MouseEvent event) {
        String rutaBase = System.getProperty("user.home");
        String separador = File.separator;
        List<String> archivosImportados = new ArrayList<>();
        boolean algunArchivoImportado = false;

        // Crear directorio principal en el escritorio
        String dirRaiz = rutaBase + separador + "Desktop" + separador + "Gestion_de_Cursos";
        File directorioRaiz = new File(dirRaiz);
        if (!directorioRaiz.exists()) {
            directorioRaiz.mkdir();
        }

        // Crear directorio de archivos importados
        String dirImport = dirRaiz + separador + "Archivos_importados";
        File directorioImportados = new File(dirImport);
        if (!directorioImportados.exists()) {
            directorioImportados.mkdir();
        }

        // Determinar el período actual y crear el directorio correspondiente
        Calendar calendario = Calendar.getInstance();
        int yearActual = calendario.get(Calendar.YEAR);
        int mesActual = calendario.get(Calendar.MONTH) + 1; // Calendar.MONTH va de 0-11

        String periodo;
        if (mesActual >= 1 && mesActual <= 7) {
            periodo = "1-" + yearActual;
        } else {
            periodo = "2-" + yearActual;
        }

        String dirPeriodo = dirImport + separador + periodo;
        File directorioPeriodo = new File(dirPeriodo);
        if (!directorioPeriodo.exists()) {
            directorioPeriodo.mkdir();
        }

        // Manejar Programa de Capacitación
        if (programaCapacitacion != null) {
            String pcDir = dirPeriodo + separador + "programas_de_capacitacion";
            File pcDirectorio = new File(pcDir);
            if (!pcDirectorio.exists()) {
                pcDirectorio.mkdir();
            }

            // Encontrar el siguiente número de semana disponible
            int numeroSemana = 1;
            String extension = getExtensionArchivo(programaCapacitacion);
            File destFile;
            do {
                destFile = new File(pcDir + separador + "programa_de_capacitacion_(Semana_" + numeroSemana + ")" + extension);
                numeroSemana++;
            } while (destFile.exists());

            try {
                java.nio.file.Files.copy(programaCapacitacion.toPath(), destFile.toPath());
                archivosImportados.add("programas_de_capacitacion");
                algunArchivoImportado = true;
            } catch (IOException e) {
                mostrarError("Error al copiar el programa de capacitación: " + e.getMessage());
            }
        }

        // Manejar Listados según selección del ComboBox
        if (listado != null && comboBoxListados.getValue() != null) {
            String tipoListado = comboBoxListados.getValue();
            String nombrecarpeta = convertirNombreCarpeta(tipoListado);
            String listadoDir = dirPeriodo + separador + nombrecarpeta;
            File listadoDirectorio = new File(listadoDir);
            if (!listadoDirectorio.exists()) {
                listadoDirectorio.mkdir();
            }

            // Encontrar el siguiente número de semana disponible
            int numeroSemana = 1;
            String extension = getExtensionArchivo(listado);
            File destFile;
            do {
                destFile = new File(listadoDir + separador + "listado_(Semana_" + numeroSemana + ")" + extension);
                numeroSemana++;
            } while (destFile.exists());

            try {
                java.nio.file.Files.copy(listado.toPath(), destFile.toPath());
                archivosImportados.add(nombrecarpeta);
                algunArchivoImportado = true;
            } catch (IOException e) {
                mostrarError("Error al copiar el listado: " + e.getMessage());
            }
        }

        // Manejar Formatos según selección del ComboBox
        if (formato != null && comboBoxFormatos.getValue() != null) {
            String tipoFormato = comboBoxFormatos.getValue();
            String nombreCarpeta = convertirNombreCarpeta(tipoFormato);
            String formatoDir = dirPeriodo + separador + nombreCarpeta;
            File formatoDirectorio = new File(formatoDir);
            if (!formatoDirectorio.exists()) {
                formatoDirectorio.mkdir();
            }

            // Encontrar el siguiente número de semana disponible
            int numeroSemana = 1;
            String extension = getExtensionArchivo(formato);
            File destFile;
            do {
                destFile = new File(formatoDir + separador + "formato_(Semana_" + numeroSemana + ")" + extension);
                numeroSemana++;
            } while (destFile.exists());

            try {
                java.nio.file.Files.copy(formato.toPath(), destFile.toPath());
                archivosImportados.add(nombreCarpeta);
                algunArchivoImportado = true;
            } catch (IOException e) {
                mostrarError("Error al copiar el formato: " + e.getMessage());
            }
        }

        // Verificar si se importó algún archivo
        if (algunArchivoImportado) {
            // Crear mensaje de éxito personalizado
            String mensaje = "Se importó correctamente el archivo en la(s) carpeta(s): "
                    + String.join("\n", archivosImportados)
                    + " del período " + periodo;
            mostrarMensajeExito(mensaje);

            // Limpiar los campos después de guardar
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
        // TODO

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
