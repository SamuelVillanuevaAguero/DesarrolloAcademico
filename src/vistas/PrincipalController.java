/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vistas;

import javafx.scene.layout.VBox;
import org.apache.poi.ss.usermodel.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
//import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utilerias.general.ControladorGeneral;

/**
 * FXML Controller class
 *
 * @author Samue
 */
public class PrincipalController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Button botonCerrar;
    @FXML
    private Button botonMinimizar;

    //Botones que redireccionan
    @FXML
    private Pane botonImportacion;

    @FXML
    private Pane botonVisualizacion;

    @FXML
    private Pane botonBusqueda;

    @FXML
    private Pane botonModificacion;

    @FXML
    private Pane botonExportacion;

    @FXML
    private Pane botonRespaldo;

    @FXML
    private ImageView notification;

    @FXML
    private Pane notificationPane;

    @FXML
    private Label cerrarNotificacion;

    //NUEVAS NOTIFICACIONES
    @FXML
    private VBox notificacioneBox;
    @FXML
    private ScrollPane scrollBox;

   

    //Métodos de los botones de la barra superior :)
    public void cerrarVentana(MouseEvent event) throws IOException {
        ControladorGeneral.cerrarVentana(event, "¿Quieres cerrar sesión?", getClass());
    }

    public void minimizarVentana(MouseEvent event) {
        ControladorGeneral.minimizarVentana(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        generarNotificacionesEnVBox();
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

        // VISTA UNO ()
        botonImportacion.setOnMouseClicked(event -> {
            try {
                ControladorGeneral.regresar(event, "ImportacionArchivos", getClass());
            } catch (IOException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        // VISTA DOS
        botonVisualizacion.setOnMouseClicked(event -> {
            try {
                ControladorGeneral.regresar(event, "VizualizacionDatos", getClass());
            } catch (IOException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //VISTA TRES
        botonBusqueda.setOnMouseClicked(event -> {
            try {
                ControladorGeneral.regresar(event, "BusquedaEstadistica", getClass());
            } catch (IOException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //VISTA CUATRO
        botonModificacion.setOnMouseClicked(event -> {
            try {
                ControladorGeneral.regresar(event, "ModificacionDatos", getClass());
            } catch (IOException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        //VISTA CINCO
        botonExportacion.setOnMouseClicked(event -> {
            try {
                ControladorGeneral.regresar(event, "ExportacionReconocimientos", getClass());
            } catch (IOException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        //VISTA SEIS
        botonRespaldo.setOnMouseClicked(event -> {
            try {
                ControladorGeneral.regresar(event, "Respaldo", getClass());
            } catch (IOException ex) {
                Logger.getLogger(PrincipalController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        // Ruta del archivo Excel
        String rutaArchivo = "C:\\Users\\sebas\\OneDrive\\Escritorio\\Prueba.xlsx";

        // Leer docentes que necesitan capacitación
        List<Docente> docentes = leerDocentesConNecesidadDeCapacitacion(rutaArchivo);

        // VBox para contener las notificaciones
        /*
              VBox notificationBox = new VBox(15);  // Espacio de 5px entre notificaciones
        notificationPane.getChildren().add(notificationBox);

        // Crear etiquetas de notificación para cada docente
        for (Docente docente : docentes) {
            String mensaje = docente.nombre + "\nnecesita capacitación en ";

            if (docente.necesitaCapacitacionFP && docente.necesitaCapacitacionAD) {
                mensaje += "FP y AD";
            } else if (docente.necesitaCapacitacionFP) {
                mensaje += "FP";
            } else if (docente.necesitaCapacitacionAD) {
                mensaje += "AD";
            }

            // Crear y añadir la etiqueta de notificación
            Label notificationLabel = new Label(mensaje);
            notificationBox.getChildren().add(notificationLabel);
        } */
        // Configurar el botón de notificación para mostrar el `notificationPane`
        notification.setOnMouseClicked(event -> {
            notificationPane.setVisible(true);
        });

        // Configurar el botón para cerrar el `notificationPane`
        cerrarNotificacion.setOnMouseClicked(event -> {
            notificationPane.setVisible(false);
        });

        // Ocultar el panel inicialmente
        notificationPane.setVisible(false);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    //NOTIFICACIONES//
    /////////////////////////////////////////////////////////////////////////////////////////////////
    // Clase interna para almacenar la información de cada docente
    public static class Docente {

        String nombre;
        boolean necesitaCapacitacionFP;
        boolean necesitaCapacitacionAD;

        public Docente(String nombre, boolean necesitaFP, boolean necesitaAD) {
            this.nombre = nombre;
            this.necesitaCapacitacionFP = necesitaFP;
            this.necesitaCapacitacionAD = necesitaAD;
        }
    }

    //
    public static List<Docente> leerDocentesConNecesidadDeCapacitacion(String rutaArchivo) {
        List<Docente> docentes = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(new File(rutaArchivo)); Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // Leer la primera hoja
            for (Row row : sheet) {
                // Leer el nombre del docente en la columna A
                Cell nombreCell = row.getCell(0); // Columna A
                Cell fpCell = row.getCell(2);     // Columna C (FP)
                Cell adCell = row.getCell(3);     // Columna D (AD)

                // Validar que las celdas no sean nulas antes de acceder a sus valores
                if (nombreCell != null && fpCell != null && adCell != null) {
                    String nombre = nombreCell.toString(); // Obtener valor como String
                    boolean necesitaFP = "Recomendable".equalsIgnoreCase(fpCell.toString().trim());
                    boolean necesitaAD = "Recomendable".equalsIgnoreCase(adCell.toString().trim());

                    // Agregar solo si se necesita alguna capacitación
                    if (necesitaFP || necesitaAD) {
                        docentes.add(new Docente(nombre, necesitaFP, necesitaAD));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return docentes;
    }
    // Método para leer los datos de docentes y generar las notificaciones en HBox

    public void generarNotificacionesEnVBox() {
        String rutaArchivo = "C:\\Users\\sebas\\OneDrive\\Escritorio\\Prueba.xlsx";
        List<Docente> docentesN = leerDocentesConNecesidadDeCapacitacion(rutaArchivo);

        // Limpiar el VBox antes de agregar nuevas notificaciones (para evitar duplicados si se llama varias veces)
        notificacioneBox.getChildren().clear();

        // Crear una entrada de notificación para cada docente
        for (Docente docente : docentesN) {
            // Crear un VBox para el docente, donde se mostrarán el nombre y las necesidades de capacitación
            VBox docenteBox = new VBox();
            docenteBox.setSpacing(3); // Espacio entre elementos en el VBox
            docenteBox.setStyle("-fx-padding: 5; -fx-border-color: black; -fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");

            // Crear y añadir un Label para el nombre del docente
            Label nombreLabel = new Label("Nombre: " + docente.nombre);
            nombreLabel.setStyle("-fx-font-weight: bold;"); // Darle énfasis al nombre

            // Crear y añadir un Label para las necesidades de capacitación
            String necesidades = "Necesita capacitación en: ";
            if (docente.necesitaCapacitacionFP && docente.necesitaCapacitacionAD) {
                necesidades += "FP y AD";
            } else if (docente.necesitaCapacitacionFP) {
                necesidades += "FP";
            } else if (docente.necesitaCapacitacionAD) {
                necesidades += "AD";
            }

            Label capacitacionLabel = new Label(necesidades);

            // Añadir los Labels al VBox del docente
            docenteBox.getChildren().addAll(nombreLabel, capacitacionLabel);

            // Añadir el VBox del docente al contenedor principal
            notificacioneBox.getChildren().add(docenteBox);
        }
        notificacioneBox.setPrefHeight(Control.USE_COMPUTED_SIZE);
        notificacioneBox.requestLayout();

        // Ajustes del ScrollPane para desplazarse verticalmente
        //scrollBox.setContent(notificacioneBox); // Asegúrate de que `notificacionesBox` esté dentro del `ScrollPane`
        scrollBox.setFitToWidth(true); // Ajustar el ancho del contenido al `ScrollPane`
        scrollBox.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // No permitir desplazamiento horizontal
        scrollBox.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Mostrar barra de desplazamiento vertical según sea necesario
        scrollBox.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

    }

    


}// FIN PrincipalController
