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
import java.util.logging.Level;
import java.util.logging.Logger;
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Todos los archivos", "*.pdf", "*.xlsx", "*.xls", "*.doc", "*.docx"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Excel", "*.xlsx", "*.xls"),
                new FileChooser.ExtensionFilter("Word", "*.doc", "*.docx")
        );

        programaCapacitacion = fileChooser.showOpenDialog(botonPC.getScene().getWindow());
        if (programaCapacitacion != null) {
            labelPC.setText(programaCapacitacion.getName());
            // Aquí puedes procesar el archivo seleccionado  
        }
    }

    public void cargarListado(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Todos los archivos", "*.pdf", "*.xlsx", "*.xls", "*.doc", "*.docx"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Excel", "*.xlsx", "*.xls"),
                new FileChooser.ExtensionFilter("Word", "*.doc", "*.docx")
        );

        listado = fileChooser.showOpenDialog(botonPC.getScene().getWindow());
        if (listado != null) {
            labelListado.setText(listado.getName());
            // Aquí puedes procesar el archivo seleccionado  
        }
    }

    public void cargarFormato(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Todos los archivos", "*.pdf", "*.xlsx", "*.xls", "*.doc", "*.docx"),
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Excel", "*.xlsx", "*.xls"),
                new FileChooser.ExtensionFilter("Word", "*.doc", "*.docx")
        );

        formato = fileChooser.showOpenDialog(botonPC.getScene().getWindow());
        if (formato != null) {
            labelFormato.setText(formato.getName());
            // Aquí puedes procesar el archivo seleccionado  
        }
    }

    public void guardarArchivos(MouseEvent event) {
        String userHome = System.getProperty("user.home");
        String separator = File.separator;
        List<String> archivosImportados = new ArrayList<>();
        boolean algunArchivoImportado = false;

        // Crear directorio principal en el escritorio
        String mainDir = userHome + separator + "Desktop" + separator + "Gestion_de_Cursos";
        File mainDirectory = new File(mainDir);
        if (!mainDirectory.exists()) {
            mainDirectory.mkdir();
        }

        // Crear directorio de archivos importados
        String importedDir = mainDir + separator + "Archivos_importados";
        File importedDirectory = new File(importedDir);
        if (!importedDirectory.exists()) {
            importedDirectory.mkdir();
        }

        // Manejar Programa de Capacitación
        if (programaCapacitacion != null) {
            String pcDir = importedDir + separator + "programas_de_capacitacion";
            File pcDirectory = new File(pcDir);
            if (!pcDirectory.exists()) {
                pcDirectory.mkdir();
            }

            // Encontrar el siguiente número de semana disponible
            int weekNumber = 1;
            String extension = getFileExtension(programaCapacitacion);
            File destFile;
            do {
                destFile = new File(pcDir + separator + "programa_de_capacitacion_(Semana_" + weekNumber + ")" + extension);
                weekNumber++;
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
            String listadoType = comboBoxListados.getValue();
            String folderName = convertirNombreCarpeta(listadoType);
            String listadoDir = importedDir + separator + folderName;
            File listadoDirectory = new File(listadoDir);
            if (!listadoDirectory.exists()) {
                listadoDirectory.mkdir();
            }

            // Encontrar el siguiente número de semana disponible
            int weekNumber = 1;
            String extension = getFileExtension(listado);
            File destFile;
            do {
                destFile = new File(listadoDir + separator + "listado_(Semana_" + weekNumber + ")" + extension);
                weekNumber++;
            } while (destFile.exists());

            try {
                java.nio.file.Files.copy(listado.toPath(), destFile.toPath());
                archivosImportados.add(folderName);
                algunArchivoImportado = true;
            } catch (IOException e) {
                mostrarError("Error al copiar el listado: " + e.getMessage());
            }
        }

        // Manejar Formatos según selección del ComboBox
        if (formato != null && comboBoxFormatos.getValue() != null) {
            String formatoType = comboBoxFormatos.getValue();
            String folderName = convertirNombreCarpeta(formatoType);
            String formatoDir = importedDir + separator + folderName;
            File formatoDirectory = new File(formatoDir);
            if (!formatoDirectory.exists()) {
                formatoDirectory.mkdir();
            }

            // Encontrar el siguiente número de semana disponible
            int weekNumber = 1;
            String extension = getFileExtension(formato);
            File destFile;
            do {
                destFile = new File(formatoDir + separator + "formato_(Semana_" + weekNumber + ")" + extension);
                weekNumber++;
            } while (destFile.exists());

            try {
                java.nio.file.Files.copy(formato.toPath(), destFile.toPath());
                archivosImportados.add(folderName);
                algunArchivoImportado = true;
            } catch (IOException e) {
                mostrarError("Error al copiar el formato: " + e.getMessage());
            }
        }

        // Verificar si se importó algún archivo
        if (algunArchivoImportado) {
            // Crear mensaje de éxito personalizado
            String mensaje = "Se importó correctamente el archivo en la(s) carpeta(s): "
                    + String.join(",", archivosImportados);
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
    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
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
