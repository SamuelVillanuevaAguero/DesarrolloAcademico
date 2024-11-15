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

    private String usuarioPredefinido = "Administrador";
    public String contraseñaPredefinida;

    private static final String FILE_PATH = "contraseña_historial.txt";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Carga la ultima contraseña para el incio de sesion.
        cargarUltimaContraseña();

        //A primera instancia no se ve el panel de RestablecerContraseña
        PaneRestablcer.setVisible(false);

        // Inicialmente, el label "Enviando..." está oculto
        Enviando.setVisible(false);

        //Botones de cerrar/minimizar ventana
        botonCerrar.setOnMouseClicked(event -> cerrarVentana());
        botonMinimizar.setOnMouseClicked(event -> minimizarVentana(event));

        //Metodo para ver la contraseña
        botonVerContraseña.setOnMousePressed(event -> {
            IngresaContraseña.setVisible(false);
            abajocontraseña.setText(IngresaContraseña.getText());
            abajocontraseña.setVisible(true);
        });
        botonVerContraseña.setOnMouseReleased(event -> {
            IngresaContraseña.setVisible(true);
            abajocontraseña.setVisible(false);
        });

        //Boton de iniciar sesion con mensajr de usuario/contraseña incorrectos.
        botonIniciarSesion.setOnMouseClicked(event -> {
            if (validarCredenciales()) {
                cargarVistaPrincipal();
            } else {
                mostrarAlertaError("Usuario o contraseña incorrectos.");
            }
        });

        //Label restablecer contraseña muestra el pane de restablecer contraseña
        RestablecerContraseña.setOnMouseClicked(event -> {
            Enviando.setVisible(false); // Aseguramos que Enviando esté oculto cuando se abre el panel
            PaneRestablcer.setVisible(true);
        });

        //Boton para cerrar el pane restablecer contraseña.
        CerrarRestablecer.setOnMouseClicked(event -> PaneRestablcer.setVisible(false));

        //Boton Enviar correo.
        EnviarCorreo.setOnMouseClicked(event -> restablecerContraseña());
    }

    //Metodo de cargaar la ultima contrseña.
    private void cargarUltimaContraseña() {
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linea, ultimaLinea = null;
            while ((linea = br.readLine()) != null) {
                ultimaLinea = linea; // Captura la última línea del archivo
            }

            if (ultimaLinea != null) {
                // Extrae la contraseña de la línea
                String[] partes = ultimaLinea.split(" \\| "); // Asume que el formato es "contraseña | fechaHora"
                contraseñaPredefinida = partes[0]; // La primera parte es la contraseña
            } else {
                contraseñaPredefinida = "123"; // Contraseña predeterminada si el archivo está vacío
            }
        } catch (IOException e) {
            contraseñaPredefinida = "123"; // Contraseña predeterminada en caso de error
        }
    }

    //Guarda la nueva contraseña generada en el archivo con fecha y hora.
    private void guardarContraseñaEnArchivo(String nuevaContraseña) {
        String fechaHora = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date());
        try (FileWriter fw = new FileWriter(FILE_PATH, true)) {
            fw.write(nuevaContraseña + " | " + fechaHora + "\n"); // Guardar en el formato "contraseña | fechaHora"
        } catch (IOException e) {
            mostrarAlertaError("No se pudo guardar la nueva contraseña.");
        }
    }

    //Genera la contraseña aleatoria.
    private String generarContraseñaAleatoria() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder nuevaContraseña = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            nuevaContraseña.append(caracteres.charAt(rnd.nextInt(caracteres.length())));
        }
        return nuevaContraseña.toString();
    }

    //Mensaje de exito
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

    //Mensaje de error.
    private void mostrarAlertaError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Error");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    //Metodo de cerrar ventana de inicio sesion.
    private void cerrarVentana() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Sesión");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Quieres salir del sistema?");
        if (alerta.showAndWait().get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    //Metodo para minimizar la ventana inicio de sesion.
    private void minimizarVentana(MouseEvent event) {
        ControladorGeneral.minimizarVentana(event);
    }

    //valida las credenciales.
    private boolean validarCredenciales() {
        return IngresaUsuario.getText().equals(usuarioPredefinido)
                && IngresaContraseña.getText().equals(contraseñaPredefinida);
    }

    //Redirecciona a la vista principal.
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

    //Dentro del panel restablcer contraseña enviar un correo a los destinos marcados.
    private void restablecerContraseña() {
        Enviando.setVisible(true); // Mostrar el mensaje "Enviando..."

        String[] destinatarios = {
            "L21091003@zacatepec.tecnm.mx",
            "xochitlmaritzafloressarabia@gmail.com" // Lista de destinatarios
        };

        String nuevaContraseña = generarContraseñaAleatoria();
        guardarContraseñaEnArchivo(nuevaContraseña); // Guardar nueva contraseña
        contraseñaPredefinida = nuevaContraseña; // Actualizar contraseña predefinida

        new Thread(() -> {
            boolean todosEnviadosConExito = true; // Declarar fuera del ciclo

            for (String destinatario : destinatarios) {
                if (!enviarCorreoNuevaContraseña(destinatario, nuevaContraseña)) {
                    todosEnviadosConExito = false; // Si falla al menos uno, marcar como error
                }
            }

            // Actualizar la interfaz en el hilo principal
            final boolean resultadoFinal = todosEnviadosConExito; // Capturar el resultado para Platform.runLater
            Platform.runLater(() -> {
                Enviando.setVisible(false); // Ocultar "Enviando..."

                if (resultadoFinal) {
                    mostrarAlertaExito("Correo enviado con nueva contraseña.");
                } else {
                    mostrarAlertaError("No se pudo enviar a uno o más destinatarios.");
                }
            });
        }).start();
    }

    //Metodo para enviar la nueva cocntraseña desde el correo de restablecer contraseña.
    private boolean enviarCorreoNuevaContraseña(String destinatario, String nuevaContraseña) {
        String remitente = "desarrolloacademico.pass@gmail.com";
        String pass = "owcvxxhxlyeydvqk";

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
