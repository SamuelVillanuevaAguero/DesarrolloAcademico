package vistas;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class ConfirmacionExportacionController {

    private Stage dialogStage;
    private boolean confirmado = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    @FXML
    private void confirmarExportacion(ActionEvent event) {
        confirmado = true;
        dialogStage.close();
    }

    @FXML
    private void cancelarExportacion(ActionEvent event) {
        dialogStage.close();
    }
}