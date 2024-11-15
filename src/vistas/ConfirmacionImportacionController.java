package vistas;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class ConfirmacionImportacionController {

    private Stage dialogStage;
    private boolean confirmado = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    @FXML
    private void confirmarImportacion(ActionEvent event) {
        confirmado = true;
        dialogStage.close();
    }

    @FXML
    private void cancelarImportacion(ActionEvent event) {
        dialogStage.close();
    }
}