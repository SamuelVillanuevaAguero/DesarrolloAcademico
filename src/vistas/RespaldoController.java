/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vistas;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;
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
import java.io.*;
import java.util.zip.*;
import javafx.stage.DirectoryChooser;


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
            select_formato_export.getItems().addAll("RAR", "ZIP");
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
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Seleccionar ubicación para exportar");
    File selectedDirectory = directoryChooser.showDialog(((Node) event.getSource()).getScene().getWindow());
    if (selectedDirectory != null) {
        txt_ruta_export.setText(selectedDirectory.getAbsolutePath());
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
            String formato = select_formato_export.getValue();
            File destinoDir = new File(txt_ruta_export.getText());
            File destino;

            if ("ZIP".equals(formato)) {
                destino = new File(destinoDir, "Gestion_De_Projectos.zip");
                comprimirZip(new File("Gestion_De_Projectos"), destino);
            } else if ("RAR".equals(formato)) {
                destino = new File(destinoDir, "Gestion_De_Projectos.rar");
                comprimirRar(new File("Gestion_De_Projectos"), destino);
            }

            System.out.println("Exportando respaldo...");
        } else {
                
            System.out.println("Exportación cancelada.");
        }
    } catch (IOException ex) {
        Logger.getLogger(RespaldoController.class.getName()).log(Level.SEVERE, null, ex);
    }
}

private void comprimirZip(File sourceDir, File zipFile) throws IOException {
    try (FileOutputStream fos = new FileOutputStream(zipFile);
         ZipOutputStream zos = new ZipOutputStream(fos)) {
        zipDirectory(sourceDir, sourceDir.getName(), zos);
    }
}

private void zipDirectory(File folder, String parentFolder, ZipOutputStream zos) throws IOException {
    File[] files = folder.listFiles();
    if (files == null) {
        return;
    }
    for (File file : files) {
        if (file.isDirectory()) {
            zipDirectory(file, parentFolder + "/" + file.getName(), zos);
            continue;
        }
        zos.putNextEntry(new ZipEntry(parentFolder + "/" + file.getName()));
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, length);
            }
        }
        zos.closeEntry();
    }
}

private void comprimirRar(File sourceDir, File rarFile) throws IOException {
    ProcessBuilder pb = new ProcessBuilder("rar", "a", rarFile.getAbsolutePath(), sourceDir.getAbsolutePath());
    pb.inheritIO();
    Process process = pb.start();
    try {
        process.waitFor();
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new IOException("La compresión RAR fue interrumpida", e);
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
    fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Archivos RAR", "*.rar"),
        new FileChooser.ExtensionFilter("Archivos ZIP", "*.zip")
    );
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
            File archivo = new File(txt_ruta_import.getText());
            File destino = new File("Gestion_De_Projectos");

            if (!destino.exists()) {
                destino.mkdirs();
            } else {
                
                for (File file : destino.listFiles()) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }

            if (archivo.getName().endsWith(".zip")) {
                descomprimirZip(archivo, destino);
            } else if (archivo.getName().endsWith(".rar")) {
                descomprimirRar(archivo, destino);
            }

            System.out.println("Importando respaldo...");
        } else {
                
            System.out.println("Importación cancelada.");
        }
    } catch (IOException | RarException ex) {
        Logger.getLogger(RespaldoController.class.getName()).log(Level.SEVERE, null, ex);
    }
}

private void deleteDirectory(File directory) {
    File[] files = directory.listFiles();
    if (files != null) {
        for (File file : files) {
            if (file.isDirectory()) {
                deleteDirectory(file);
            } else {
                file.delete();
            }
        }
    }
    directory.delete();
}

public void descomprimirZip(File archivoZip, File destino) throws IOException {
    byte[] buffer = new byte[1024];
    ZipInputStream zis = new ZipInputStream(new FileInputStream(archivoZip));
    ZipEntry zipEntry = zis.getNextEntry();
    while (zipEntry != null) {
        File nuevoArchivo = new File(destino, zipEntry.getName());
        if (zipEntry.isDirectory()) {
            nuevoArchivo.mkdirs();
        } else {
            new File(nuevoArchivo.getParent()).mkdirs();
            FileOutputStream fos = new FileOutputStream(nuevoArchivo);
            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
        }
        zipEntry = zis.getNextEntry();
    }
    zis.closeEntry();
    zis.close();
}

public void descomprimirRar(File archivoRar, File destino) throws IOException, RarException {
    Archive archive = new Archive(new FileInputStream(archivoRar));
    FileHeader fileHeader = archive.nextFileHeader();
    while (fileHeader != null) {
        File nuevoArchivo = new File(destino, fileHeader.getFileNameString().trim());
        if (fileHeader.isDirectory()) {
            nuevoArchivo.mkdirs();
        } else {
            new File(nuevoArchivo.getParent()).mkdirs();
            FileOutputStream fos = new FileOutputStream(nuevoArchivo);
            archive.extractFile(fileHeader, fos);
            fos.close();
        }
        fileHeader = archive.nextFileHeader();
    }
    archive.close();
}
}
