package JuegoSudoku;

import Controladores.ControladorVPrincipal;
import Recursos.LocalizadorRecursos;
import Utilidades.Dialogos;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
	private static ControladorVPrincipal cVentanaPrincipal;
	
	@Override
	public void start(Stage escenarioPrincipal) {
		try {
			FXMLLoader cargadorVentanaPrincipal=new FXMLLoader(LocalizadorRecursos.class.getResource("/vistas/VPrincipal.fxml"));
			VBox raiz = cargadorVentanaPrincipal.load();
			ControladorVPrincipal cVentanaPrincipal=cargadorVentanaPrincipal.getController();
			cVentanaPrincipal.setEscenarioPrincipal(escenarioPrincipal);
			
			Scene escena = new Scene(raiz);
			escenarioPrincipal.setTitle("Juego Sudoku");
			escenarioPrincipal.setScene(escena);
			escenarioPrincipal.setOnCloseRequest(e -> confirmarSalida(escenarioPrincipal, e));
			escenarioPrincipal.show();
			Main.cVentanaPrincipal=cVentanaPrincipal;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void confirmarSalida(Stage escenarioPrincipal, WindowEvent e) {
    	if(Dialogos.mostrarDialogoConfirmacion("Salir", "¿Seguro que desea cerrar la aplicación?", escenarioPrincipal)) {
    		cVentanaPrincipal.escribir();
    		escenarioPrincipal.close();
    	}
    	else {
    		e.consume();
    	}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
