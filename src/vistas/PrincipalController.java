/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vistas;

import utilerias.principal.Necesidadcapacitacion;
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

    @FXML
    private ImageView notiAlert;

    @FXML
    private ImageView reloadNotis;

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
        String rutaArchivo = "C:\\Users\\sebas\\OneDrive\\Escritorio\\Arhicvos Prueba\\Gestion_de_curso\\Archivos_importados\\Año\\Periodo\\docentesRecomendable.xlsx";

        // Leer docentes que necesitan capacitación
        List<Docente> docentes = leerDocentesConNecesidadDeCapacitacion(rutaArchivo);

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

        reloadNotis.setOnMouseClicked(event -> {
            generarNotis();
            System.out.println("Generacion de archivo para notis");
        });
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    //NOTIFICACIONES//
    /////////////////////////////////////////////////////////////////////////////////////////////////
    // Clase interna para almacenar la información de cada docente
    public static class Docente {

        String nombre;
        boolean necesitaCapacitacionFD;
        boolean necesitaCapacitacionAP;

        public Docente(String nombre, boolean necesitaFP, boolean necesitaAD) {
            this.nombre = nombre;
            this.necesitaCapacitacionFD = necesitaFP;
            this.necesitaCapacitacionAP = necesitaAD;
        }

    }

    //
    // Leer listado_de_necesidad_de_acreditacion con las columnas A, B y C
    public static List<Docente> leerDocentesConNecesidadDeCapacitacion(String rutaArchivo) {
        List<Docente> docentes = new ArrayList<>();

        // Verificar si el archivo existe antes de intentar leerlo
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            System.out.println("El archivo especificado no existe: " + rutaArchivo); // Mensaje en consola
            // Alternativamente, puedes usar un sistema de notificaciones visual
            // o simplemente manejarlo con un log
            return docentes; // Retornar lista vacía si el archivo no existe
        }

        // Intentar leer el archivo si existe
        try (FileInputStream fis = new FileInputStream(archivo); Workbook workbook = new XSSFWorkbook(fis)) {
            Sheet sheet = workbook.getSheetAt(0); // Leer la primera hoja

            // Iterar desde la segunda fila (índice 1) para omitir los encabezados
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue; // Saltar filas vacías
                }
                // Leer el nombre del docente en la columna A (índice 0)
                Cell nombreCell = row.getCell(0); // Columna A
                Cell fpCell = row.getCell(1);     // Columna B (FP)
                Cell adCell = row.getCell(2);     // Columna C (AD)

                // Validar que la celda del nombre no sea nula
                if (nombreCell != null) {
                    String nombre = nombreCell.toString().trim(); // Obtener valor como String

                    // Validar FP y AD, asignando "Recomendable" solo si coincide
                    boolean necesitaFP = fpCell != null && "Recomendable".equalsIgnoreCase(fpCell.toString().trim());
                    boolean necesitaAD = adCell != null && "Recomendable".equalsIgnoreCase(adCell.toString().trim());

                    // Agregar solo si se tiene un nombre válido
                    docentes.add(new Docente(nombre, necesitaFP, necesitaAD));
                }
            }
        } catch (Exception e) {
            // Manejar cualquier otro error que pueda ocurrir durante la lectura
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        return docentes;
    }

    // Método para leer los datos de docentes y generar las notificaciones en VBox
    public void generarNotificacionesEnVBox() {
        String rutaArchivo = "C:\\Users\\sebas\\OneDrive\\Escritorio\\Arhicvos Prueba\\Gestion_de_curso\\Archivos_importados\\Año\\Periodo\\docentesRecomendable.xlsx";
        List<Docente> docentesN = leerDocentesConNecesidadDeCapacitacion(rutaArchivo);

        // Limpiar el VBox antes de agregar nuevas notificaciones (para evitar duplicados si se llama varias veces)
        notificacioneBox.getChildren().clear();

        // Crear una entrada de notificación para cada docente
        for (Docente docente : docentesN) {
            // **Condición para ignorar docentes sin "Recomendable" en FP o AD**
            if (!docente.necesitaCapacitacionFD && !docente.necesitaCapacitacionAP) {
                continue; // Saltar este docente
            }

            // Crear un VBox para el docente, donde se mostrarán el nombre y las necesidades de capacitación
            VBox docenteBox = new VBox();
            docenteBox.setSpacing(3); // Espacio entre elementos en el VBox
            docenteBox.setStyle("-fx-padding: 5; -fx-border-color: black; -fx-background-color: white; -fx-border-radius: 5; -fx-background-radius: 5;");

            // Crear y añadir un Label para el nombre del docente
            Label nombreLabel = new Label("Nombre: " + docente.nombre);
            nombreLabel.setStyle("-fx-font-weight: bold;"); // Darle énfasis al nombre

            // Crear y añadir un Label para las necesidades de capacitación
            String necesidades = "Necesita capacitación en: ";
            if (docente.necesitaCapacitacionFD && docente.necesitaCapacitacionAP) {
                necesidades += "FD y AP";
            } else if (docente.necesitaCapacitacionFD) {
                necesidades += "FD";
            } else if (docente.necesitaCapacitacionAP) {
                necesidades += "AP";
            }

            Label capacitacionLabel = new Label(necesidades);

            // Añadir los Labels al VBox del docente
            docenteBox.getChildren().addAll(nombreLabel, capacitacionLabel);

            // Añadir el VBox del docente al contenedor principal
            notificacioneBox.getChildren().add(docenteBox);
        }

        // Ajustar altura del contenedor según el número de nodos
        notificacioneBox.setPrefHeight(Control.USE_COMPUTED_SIZE);
        notificacioneBox.requestLayout();

        // Obtener el número total de nodos dentro del VBox `notificacioneBox`
        int totalNodos = notificacioneBox.getChildren().size();

        // Condición para mostrar el `imageView` (icono de notificación)
        if (totalNodos >= 1) {
            notiAlert.setVisible(true);
        } else {
            notiAlert.setVisible(false);
        }

        // Ajustes del ScrollPane para desplazarse verticalmente
        scrollBox.setContent(notificacioneBox); // Asegúrate de que `notificacioneBox` esté dentro del `ScrollPane`
        scrollBox.setFitToWidth(true); // Ajustar el ancho del contenido al `ScrollPane`
        scrollBox.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // No permitir desplazamiento horizontal
        scrollBox.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Mostrar barra de desplazamiento vertical según sea necesario
        scrollBox.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
    }

    public void generarNotis() {
        try {
            Necesidadcapacitacion necesidadCapacitacion = new Necesidadcapacitacion();
            necesidadCapacitacion.generarArchivo();
            System.out.println("Generación de archivo para notificaciones completada.");
            generarNotificacionesEnVBox();
        } catch (IOException e) {
            System.err.println("Error al generar el archivo: " + e.getMessage());
        }
    }

}// FIN PrincipalController

