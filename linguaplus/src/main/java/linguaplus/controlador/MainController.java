package linguaplus.controlador;

import javafx.fxml.FXML;

/**
 * Controlador de la ventana principal. Cada pestaña se define en su propio
 * FXML con su propio controlador independiente (todos comparten el estado
 * de la aplicacion a traves de {@link linguaplus.app.AppContext}), asi que
 * este controlador no necesita orquestar logica entre pestañas por ahora.
 */
public class MainController {

    @FXML
    public void initialize() {
        // Punto de extension: aqui podria centralizarse logica que dependa
        // de mas de una pestaña (por ejemplo, refrescar totales globales).
    }
}
