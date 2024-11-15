/* Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package vistas;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.ImageView;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import javafx.scene.layout.Pane;
import utilerias.general.ControladorGeneral;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

public class InicioSesionController implements Initializable {

    @FXML
    private Button botonCerrar;
    @FXML
    private Button botonMinimizar;
    @FXML
    private Button botonIniciarSesion;
    @FXML
    private TextField IngresaUsuario;
    @FXML
    private PasswordField IngresaContraseña;
    @FXML
    private TextField abajocontraseña;
    @FXML
    private ImageView botonVerContraseña;
    @FXML
    private Label RestablecerContraseña;
    @FXML
    private Pane PaneRestablcer;
    @FXML
    private Label CerrarRestablecer;
    @FXML
    private Button EnviarCorreo;
    @FXML
    private Label Enviando; // Label para mostrar "Enviando..."

    private String usuarioPredefinido = "admin";
    public String contraseñaPredefinida;

    private static final String FILE_PATH = "contraseña_historial.txt";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarUltimaContraseña();
        PaneRestablcer.setVisible(false);

        // Inicialmente, el label "Enviando..." está oculto
        Enviando.setVisible(false);

        botonCerrar.setOnMouseClicked(event -> cerrarVentana());
        botonMinimizar.setOnMouseClicked(event -> minimizarVentana(event));

        botonVerContraseña.setOnMousePressed(event -> {
            IngresaContraseña.setVisible(false);
            abajocontraseña.setText(IngresaContraseña.getText());
            abajocontraseña.setVisible(true);
        });

        botonVerContraseña.setOnMouseReleased(event -> {
            IngresaContraseña.setVisible(true);
            abajocontraseña.setVisible(false);
        });

        botonIniciarSesion.setOnMouseClicked(event -> {
            if (validarCredenciales()) {
                cargarVistaPrincipal();
            } else {
                mostrarAlertaError("Usuario o contraseña incorrectos.");
            }
        });

        RestablecerContraseña.setOnMouseClicked(event -> {
            Enviando.setVisible(false); // Aseguramos que Enviando esté oculto cuando se abre el panel
            PaneRestablcer.setVisible(true);
        });

        CerrarRestablecer.setOnMouseClicked(event -> PaneRestablcer.setVisible(false));

        EnviarCorreo.setOnMouseClicked(event -> restablecerContraseña());
    }

    private void cargarUltimaContraseña() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linea, ultimaLinea = null;
            while ((linea = br.readLine()) != null) {
                ultimaLinea = linea;
            }
            contraseñaPredefinida = (ultimaLinea != null) ? ultimaLinea : "123";
        } catch (IOException e) {
            contraseñaPredefinida = "123";
        }
    }

    private void guardarContraseñaEnArchivo(String nuevaContraseña) {
        String fechaHora = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
        try (FileWriter fw = new FileWriter(FILE_PATH, true)) {
            fw.write(nuevaContraseña + " | " + fechaHora + "\n");
        } catch (IOException e) {
            mostrarAlertaError("No se pudo guardar la nueva contraseña.");
        }
    }

    private String generarContraseñaAleatoria() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder nuevaContraseña = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            nuevaContraseña.append(caracteres.charAt(rnd.nextInt(caracteres.length())));
        }
        return nuevaContraseña.toString();
    }

    private void mostrarAlertaExito(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Éxito");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);

        // Agregar un manejador para el botón de "Aceptar"
        alerta.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Cuando el usuario presiona "Aceptar", cerrar el panel de restablecer contraseña
                PaneRestablcer.setVisible(false);
            }
        });
    }

    private void mostrarAlertaError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void cerrarVentana() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Sesión");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Quieres salir del sistema?");
        if (alerta.showAndWait().get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    private void minimizarVentana(MouseEvent event) {
        ControladorGeneral.minimizarVentana(event);
    }

    private boolean validarCredenciales() {
        return IngresaUsuario.getText().equals(usuarioPredefinido)
                && IngresaContraseña.getText().equals(contraseñaPredefinida);
    }

    private void cargarVistaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistas/principal.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) botonIniciarSesion.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            mostrarAlertaError("Algo anda mal: " + e.getMessage());
        }
    }

    private void restablecerContraseña() {
        // Mostrar el mensaje "Enviando..." cuando se hace clic en el botón
        Enviando.setVisible(true);

        String destinatario = "L21091003@zacatepec.tecnm.mx";
        String nuevaContraseña = generarContraseñaAleatoria();
        guardarContraseñaEnArchivo(nuevaContraseña);
        contraseñaPredefinida = nuevaContraseña;

        // Hacer el envío del correo en un hilo separado para evitar bloquear la interfaz
        new Thread(() -> {
            boolean correoEnviado = enviarCorreoNuevaContraseña(destinatario, nuevaContraseña);

            // Actualizar la interfaz de usuario desde el hilo principal
            Platform.runLater(() -> {
                // Ocultar el mensaje "Enviando..."
                Enviando.setVisible(false);

                // Mostrar la alerta de éxito o error dependiendo del resultado
                if (correoEnviado) {
                    mostrarAlertaExito("Correo enviado con nueva contraseña.");
                } else {
                    mostrarAlertaError("No se pudo enviar el correo.");
                }
            });
        }).start();
    }

    private boolean enviarCorreoNuevaContraseña(String destinatario, String nuevaContraseña) {
        String remitente = "xochitlmaritzafloressarabia@gmail.com";
        String pass = "cjqhvctyrvqsnevb";

        Properties propiedades = new Properties();
        propiedades.put("mail.smtp.host", "smtp.gmail.com");
        propiedades.put("mail.smtp.port", "587");
        propiedades.put("mail.smtp.auth", "true");
        propiedades.put("mail.smtp.starttls.enable", "true");

        Session sesion = Session.getInstance(propiedades, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, pass);
            }
        });

        try {
            Message mensajeCorreo = new MimeMessage(sesion);
            mensajeCorreo.setFrom(new InternetAddress(remitente));
            mensajeCorreo.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensajeCorreo.setSubject("Nueva contraseña generada");
            mensajeCorreo.setText("Hola,\n\nTu nueva contraseña es: " + nuevaContraseña + "\n\nSaludos.");

            Transport.send(mensajeCorreo);
            return true;
        } catch (MessagingException e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            return false;
        }
    }
}
