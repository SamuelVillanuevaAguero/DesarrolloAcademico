/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vistas;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Modality;
import utilerias.general.ControladorGeneral;

/**
 * FXML Controller class
 *
 * @author Samue
 */
public class RespaldoController implements Initializable {

    /**
     * Initializes the controller class.
     */

    // elemntos de la vista de respaldo
    @FXML
    private Button botonCerrar;
    @FXML
    private Button botonMinimizar;
    @FXML
    private Button botonRegresar;
    // Elementos de la vista de importar respaldo
    @FXML
    private TextField txt_ruta_import;
    @FXML
    private Button btn_examinar_import;
    @FXML
    private Button btn_importar_importar;
     // Elementos de la vista de exportar respaldo
    @FXML
    private Button btn_examinar_export;
    @FXML
    private TextField txt_ruta_export;
    @FXML
    private ComboBox<String> select_formato_export;
    @FXML
    private Button btn_export;
    

    //Métodos de los botones de la barra superior 
    public void cerrarVentana(MouseEvent event) throws IOException{
        ControladorGeneral.cerrarVentana(event, "¿Quieres cerrar sesión?", getClass());
    }
    
    public void minimizarVentana(MouseEvent event){
        ControladorGeneral.minimizarVentana(event);
    }
    
    public void regresarVentana(MouseEvent event)throws IOException{
        ControladorGeneral.regresar(event, "Principal", getClass());
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

        // Verificar si el ComboBox select_formato_export no es null antes de inicializarlo
        if (select_formato_export != null) {
            select_formato_export.getItems().addAll("Formato 1", "Formato 2", "Formato 3");
        }
    }
    
    // Methodos de la vista de exportar respaldo
    @FXML
    private void ir_exportar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vistas/Respaldo_exportacion.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(RespaldoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     @FXML
    private void examinar_exportar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar ubicación para exportar");
        File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            txt_ruta_export.setText(file.getAbsolutePath());
        }
    }
   @FXML
    private void exportar_respaldo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/ConfirmacionExportacion.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Confirmación de Exportación");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            ConfirmacionExportacionController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isConfirmado()) {
                // Lógica para exportar el archivo seleccionado
                System.out.println("Exportando respaldo...");
            } else {
                // Lógica para cancelar la exportación
                System.out.println("Exportación cancelada.");
            }
        } catch (IOException ex) {
            Logger.getLogger(RespaldoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Methodos de la vista de importar respaldo
    @FXML
    private void ir_importart(ActionEvent event) {
           try {
        Parent root = FXMLLoader.load(getClass().getResource("/vistas/Respaldo_importacion.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (IOException ex) {
        Logger.getLogger(RespaldoController.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    @FXML
    private void examinar_importar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de respaldo");
        File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
        if (file != null) {
            txt_ruta_import.setText(file.getAbsolutePath());
        }
    }

    @FXML
    private void importacion_importar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/ConfirmacionImportacion.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Confirmación de Importación");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(((Node) event.getSource()).getScene().getWindow());
            Scene scene = new Scene(root);
            dialogStage.setScene(scene);

            ConfirmacionImportacionController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isConfirmado()) {
                // Lógica para importar el archivo seleccionado
                System.out.println("Importando respaldo...");
            } else {
                // Lógica para cancelar la importación
                System.out.println("Importación cancelada.");
            }
        } catch (IOException ex) {
            Logger.getLogger(RespaldoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
