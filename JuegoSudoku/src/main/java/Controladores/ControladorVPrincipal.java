package Controladores;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import JuegoSudoku.Sudoku;
import Utilidades.Dialogos;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ControladorVPrincipal {
	List[] sudoku=new List[9];
	TextField[] matriz;
	String dificultad;
	int indiceEscuchado;
	Set<Integer> aciertos=new LinkedHashSet<>();
	int pistas;
	
	
	public void setEscenarioPrincipal (Stage escenarioPrincipal) {
		
	}
	
	/*
	 * Método initialize() que se ejecuta al ejecutarse la aplicación. Crea las opcionesd el ComboBox de dificultad, pone por defecto la primera,
	 * situa la información contextual, crea la matriz con todos los TextFields para su posterior manipulación y, haciendo uso de ésta, situa los
	 * KeyEvent y los addListener. Después corre el método leer para cargar la info de partidas anteriores y da formato a la tabla para que quede
	 * con los TextField del color adecuado, deshabilite los botones pertinentes, etc.
	 */
	@FXML private void initialize() {
		cbDificultad.setItems(FXCollections.observableArrayList("Facil","Intermedio","Dificil"));
		cbDificultad.getSelectionModel().select(0);
		infoContextual();
    	matriz=new TextField[] {tf00,tf01,tf02,tf03,tf04,tf05,tf06,tf07,tf08,tf10,tf11,tf12,tf13,tf14,tf15,tf16,tf17,tf18,tf20,tf21,tf22,tf23,tf24,tf25,tf26,tf27,tf28,tf30,tf31,tf32,tf33,tf34,tf35,tf36,tf37,tf38,tf40,tf41,tf42,tf43,tf44,tf45,tf46,tf47,tf48,tf50,tf51,tf52,tf53,tf54,tf55,tf56,tf57,tf58,tf60,tf61,tf62,tf63,tf64,tf65,tf66,tf67,tf68,tf70,tf71,tf72,tf73,tf74,tf75,tf76,tf77,tf78,tf80,tf81,tf82,tf83,tf84,tf85,tf86,tf87,tf88,};
    	for(TextField t : matriz) {
    		t.textProperty().addListener((ob, ov, nv) -> comprobarReglas(ov, nv));
    		t.setOnKeyReleased(e -> comprobarAcierto(e));
    	}
    	leer();
    	formatearTabla();
	}
	
	/*
	 * Accion acGenerar, que se produce al pulsar el Botón btGenerar, que crea un sudoku mediante la clase Sudoku, corre limpiar() 
	 * y corre el método establecerDificultad con la dificultar seleccionada en el ComboBox correspondiente.
	 */
    @FXML
    void acGenerar(ActionEvent event) {
    	sudoku=Sudoku.crear();
    	limpiar();
    	establecerDificultad(cbDificultad.getSelectionModel().getSelectedItem());
    }
    
    /*
     * Método limpiar() que crea un nuevo set aciertos para ir guardando los aciertos dentro, y recorre matriz para limpiar los TextField,
     * ponerlos todos en editable y ponerlos en negro
     */
    private void limpiar() {
    	aciertos=new LinkedHashSet<>();
    	for(TextField t : matriz) {
    		t.clear();
    		t.setEditable(true);
    		t.setStyle("-fx-text-fill: black");
    	}
    	btResolver.setDisable(false);
    }
	
    /*
     * Método establecerDificultad() que recoge un dif, que se le pasará desde acGenerar extraído del ComboBox, y asigna dicha dificultad al field dificultad.
     * Después, según la dificultad que haya recibido, da el formato al label correspondiente, lo asigna, activa el botón de Pista si fuera necesario y
     * pasa a rellenarCasilla el número de casillas que se deben rellenar según la dificultad seleccionada.
     */
    private void establecerDificultad(String dif) {
    	lbDificultad.setText(dif);
    	dificultad=dif;

    	if (dif=="Facil") {
    		lbDificultad.setTextFill(Color.GREEN);
    		pistas=5;
    		lbPistas.setText(String.valueOf(pistas));
    		btPista.setDisable(false);
    		rellenarCasilla(35);
    	}
    	else if(dif=="Intermedio") {
    		lbDificultad.setTextFill(Color.ORANGE);
    		pistas=3;
    		lbPistas.setText(String.valueOf(pistas));
    		btPista.setDisable(false);
    		rellenarCasilla(30);
    	}
    	else {
    		lbDificultad.setTextFill(Color.RED);
    		pistas=0;
    		lbPistas.setText(String.valueOf(pistas));
    		btPista.setDisable(true);
    		rellenarCasilla(25);
    	}
    }
    
    /*
     * Recibe un número de vueltas que tiene que dar para generar esa cantidad de números en casillas aleatorias. Para ello, genera dos números aleatorios y
     * aplica una fórmula para generar el correspondiente índice de matriz. Si dicho índice es superior a 80 es erróneo (solo puede haber 81 elementos) así que
     * sigue dentro del bucle, y si no se puede agregar dicho índice al set aciertos es que está repetido, por lo que sigue en el bucle y genera otra casilla.
     * Una vez tenemos el índice de la casilla, vamos a la fila y casilla correspondientes de sudoku y asignamos su valor al índice de matriz, haciendo no-editable
     * la casilla rellenada.
     */
    private void rellenarCasilla (int loops) {
    	int n1=0;
    	int n2=0;
    	int indiceMatriz=0;
    	for(int i=0;i<loops;i++) {
    		do {
        		do {
        			n1=(int) Math.floor(Math.random() * (8 - 0 + 1) + 0);
            		n2=(int) Math.floor(Math.random() * (8 - 0 + 1) + 0);
            		indiceMatriz=n1*8+n2+n1;
        		} while (indiceMatriz>80);
    		} while (aciertos.add(indiceMatriz)==false);
    		
    		List<String> fila=sudoku[n1];
    		String casilla=fila.get(n2);
    		matriz[indiceMatriz].setText(casilla);
    		matriz[indiceMatriz].setEditable(false);
    	}
    }
    
    /*
     * Evento comprobarAcierto, que recibirá un evento cuando se suelte una tecla. Obtenemos la fuente de dicho evento y la comparamos con matriz, 
     * para obtener el índice en que se ha realizado dicho cambio. Después aplicamos una fórmula para obtener la posición de la fila y la casilla de sodoku
     * correspondiente a dicho índice. Si el texto introducido en dicho índice es igual al de sudoku, el número introducido en el TextField es correcto,
     * por lo que añadimos dicho índice al set aciertos. Si la dificultad no es difícil, la casilla se pondrá verde y se bloqueará. Si no coincide 
     * el número introducido será erróneo, por lo que si la dificultad no es difícil, la casilla se pondrá en rojo.
     */
    private void comprobarAcierto(KeyEvent e) {
    	int indice=-1;
    	for(int i=0;i<matriz.length&&indice<0;i++) {
    		if(e.getSource().equals(matriz[i])) {
    			indice=i;
    			indiceEscuchado=i;
    		}
    	}
    	double decimales=(double) indice/9;
		int sinComas=(int) Math.floor(decimales*10);
		int n1=(int) Math.floor(sinComas/10);
		int n2=(int) Math.floor(sinComas%10);
		List<String> fila=sudoku[n1];
		String casilla=fila.get(n2);
		if (matriz[indice].getText().equals(casilla)) {
			aciertos.add(indice);
			comprobarGanador();
			if (dificultad!="Dificil") {
				matriz[indice].setStyle("-fx-text-fill: green");
				matriz[indice].setEditable(false);
			}
		} else {
			if (dificultad!="Dificil") {
				matriz[indice].setStyle("-fx-text-fill: red");
			}
		}
    }
    
    /*
     * Método formatearTabla(), que da el formato adecuado tras leer los datos, haciendo no-editables las casillas que estaban bien colocadas y poniendo en
     * rojo las que estaban mal colocadas, aunque no aquellas que están vacías.
     */
    public void formatearTabla() {
    	int n1=0;
    	int n2=0;
    	for(int i=0;i<matriz.length;i++) {
    		List<String> fila=sudoku[n1];
    		String casilla=fila.get(n2);
    		if (matriz[i].getText().equals(casilla)) {
    			if (dificultad!="Dificil") {
    				matriz[i].setEditable(false);
    			}
    		} else {
    			if (dificultad!="Dificil"&&matriz[i].getText().matches("[\\d]")) {
    				matriz[i].setStyle("-fx-text-fill: red");
    			}
    		}
    		n2++;
    		if (n2>8) {
    			n1++;
    			n2=0;
    		}
    		if(aciertos.size()==81) {
    	    	btPista.setDisable(true);
    	    	btResolver.setDisable(true);
    		}
    	}
    }
    
    /*
     * Método comprobarReglas(), que sustituirá cualquier valor introducido en los TextField que no sea un número. También sustituirá cualquier valor
     * anterior por el nuevo si éste sí es un número.
     */
    private void comprobarReglas(String oldValue, String newValue) {
	    if (! matriz[indiceEscuchado].getText().matches("\\d*")) {
	    	matriz[indiceEscuchado].setText(matriz[indiceEscuchado].getText().replaceAll("[^\\d]", ""));
	    }
	    if (! matriz[indiceEscuchado].getText().matches("\\d{1}")) {
	    	matriz[indiceEscuchado].setText(matriz[indiceEscuchado].getText().replaceFirst("[\\d]", ""));
	    }
    }
    
    /*
     * Método comprobarGanador(), que comprobará si el tamaño del set aciertos, donde hemos ido añadiendo todos los índices de matriz con números correctos,
     * es de 81. De ser así, popeamos un diálogo felicitando al jugador y para evitar problemas, deshabilitamos los botones de pista y de resolver.
     */
    private void comprobarGanador() {
    	if (aciertos.size()==81) {
    		Dialogos.mostrarDialogoInformacion("¡Conseguido!", "Felicidades, has resuelto correctamente el sudoku.");
    		btPista.setDisable(true);
        	btResolver.setDisable(true);
    	}
    }
    
    /*
     * Método infoContextual(), que crea y coloca la info contextual de los diversos elementos de la aplicación.
     */
    private void infoContextual() {
    	Tooltip difFacil = new Tooltip ("- Fácil: Empieza con 35 casillas rellenas y 5 pistas disponibles. \nLos fallos se indican en rojo y los aciertos en verde. \n - Intermedio: Empieza con 30 casillas rellenas y 3 pistas disponibles. \nLos fallos se indican en rojo y los aciertos en verde. \n - Difícil: Empieza con 25 casillas rellenas y 0 pistas disponibles. \nNi los fallos ni los aciertos se indican.");
    	Tooltip tpBtPista = new Tooltip ("Rellena una casilla vacía aleatoria");
    	Tooltip tpBtResolver = new Tooltip ("Resuelve el sudoku automáticamente");
    	
    	cbDificultad.setTooltip(difFacil);
    	btPista.setTooltip(tpBtPista);
    	btResolver.setTooltip(tpBtResolver);
    }
    
    /*
     * Acción acResolver, que entrará en un bucle con el tamaño de matriz e irá haciendo set en dicho índice al valor correspondiente de sudoku,
     * iterando con n2 e incrementando n1 cuando el primero llegue a 8, lo que significa que ha terminado dicha fila. Cada valor vuelve no-editable
     * la casilla y la pone en negro.
     */
    @FXML
    void acResolver(ActionEvent event) {
    	int n1=0;
    	int n2=0;
    	for(int i=0;i<matriz.length;i++) {
    		List<String> fila=sudoku[n1];
    		String casilla=fila.get(n2);
    		matriz[i].setText(casilla);
    		matriz[i].setEditable(false);
    		matriz[i].setStyle("-fx-text-fill: black");
    		n2++;
    		if (n2>8) {
    			n1++;
    			n2=0;
    		}
    	}
    	btPista.setDisable(true);
    	btResolver.setDisable(true);
    	Dialogos.mostrarDialogoInformacion("¿Conseguido?", "Felicidades, has resuelto correctamente el sudoku. \n(Con algo de ayuda...)");
    }
    
    /*
     * Acción acPista, que rellenará una casilla aleatoria con el método rellenarCasilla(), luego comprobará el ganador, decrementará el total de pistas
     * y cambiará el texto del correspondiente TextField. De llegar las pistas a 0, deshabilitará el botón asociado a la acción.
     */
    @FXML
    void acPista(ActionEvent event) {
    	rellenarCasilla(1);
    	comprobarGanador();
    	pistas--;
    	lbPistas.setText(String.valueOf(pistas));
    	if (pistas==0) {
    		btPista.setDisable(true);
    	}
    }
    
    /*
     * Método escribir(), que guardará toda la info relevante de la partida para su posterior recuperación. Ello incluye los valores de matriz, la plantilla
     * del sudoku, el set de aciertos, la dificultad y las pistas. 
     */
	public void escribir() {
		File archivoMatriz=new File("datos/matriz.dat");
		try {
			FileOutputStream fileOut=new FileOutputStream(archivoMatriz);
			ObjectOutputStream dataOS=new ObjectOutputStream(fileOut);
			for(TextField t : matriz) {
				dataOS.writeObject(t.getText());
			}
			dataOS.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No se pudo encontrar el fichero de matriz");
		} catch (IOException e) {
			System.out.println("ERROR inesperado de Entrada/Salida en matriz");
		}
		
		File archivoSudoku=new File("datos/sudoku.dat");
		try {
			FileOutputStream fileOut=new FileOutputStream(archivoSudoku);
			ObjectOutputStream dataOS=new ObjectOutputStream(fileOut);
			for(List l : sudoku) {
				dataOS.writeObject(l);
			}
			dataOS.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No se pudo encontrar el fichero de sudoku");
		} catch (IOException e) {
			System.out.println("ERROR inesperado de Entrada/Salida en sudoku");
		}
		
		File archivoAciertos=new File("datos/aciertos.dat");
		try {
			FileOutputStream fileOut=new FileOutputStream(archivoAciertos);
			ObjectOutputStream dataOS=new ObjectOutputStream(fileOut);
			for(Integer i : aciertos) {
				dataOS.writeObject(i);
			}
			dataOS.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No se pudo encontrar el fichero de aciertos");
		} catch (IOException e) {
			System.out.println("ERROR inesperado de Entrada/Salida en aciertos");
		}
		
		File archivoDificultad=new File("datos/dificultad.dat");
		try {
			FileOutputStream fileOut=new FileOutputStream(archivoDificultad);
			ObjectOutputStream dataOS=new ObjectOutputStream(fileOut);
				dataOS.writeObject(dificultad);
			dataOS.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No se pudo encontrar el fichero de aciertos");
		} catch (IOException e) {
			System.out.println("ERROR inesperado de Entrada/Salida en aciertos");
		}
		
		File archivoPistas=new File("datos/pistas.dat");
		try {
			FileOutputStream fileOut=new FileOutputStream(archivoPistas);
			ObjectOutputStream dataOS=new ObjectOutputStream(fileOut);
				dataOS.writeObject(pistas);
			dataOS.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No se pudo encontrar el fichero de aciertos");
		} catch (IOException e) {
			System.out.println("ERROR inesperado de Entrada/Salida en aciertos");
		}
	}
	
	/*
	 * Método leer(), que recuperará la información guardada en los documentos por el método escribir(). Asignará los datos de la matriz que fueron guardados,
	 * creará una nueva plantilla sudoku con las filas guardadas, asignará todos los índices acertados al set correspondiente y también recuperará y formateará
	 * la dificultad y las pistas restantes.
	 */
	public void leer() {
		File archivoMatriz=new File("datos/matriz.dat");
		try {
				FileInputStream fileIn = new FileInputStream(archivoMatriz);
				ObjectInputStream dataIS=new ObjectInputStream(fileIn);
				for (TextField t : matriz) {
					t.setText((String) dataIS.readObject());
				}
				dataIS.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No se pudo abrir el fichero de matriz");
		} catch (IOException e) {
			System.out.println("ERROR inesperado de Entrada/Salida en lectura de matriz");
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: No se pudo encontrar la clase a leer");
		}
		
		File archivoSudoku=new File("datos/sudoku.dat");
		try {
				FileInputStream fileIn = new FileInputStream(archivoSudoku);
				ObjectInputStream dataIS=new ObjectInputStream(fileIn);
				List<String> fila=null;
				int indice=0;
				do {
					fila=(List<String>) dataIS.readObject();
					if (fila!=null) {
						sudoku[indice]=(fila);
						indice++;
					}
				} while (fila!=null);
				dataIS.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No se pudo abrir el fichero de sudoku");
		} catch (IOException e) {
			// System.out.println("ERROR inesperado de Entrada/Salida en lectura de sudoku");
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: No se pudo encontrar la clase a leer");
		}
		
		File archivoAciertos=new File("datos/aciertos.dat");
		try {
				FileInputStream fileIn = new FileInputStream(archivoAciertos);
				ObjectInputStream dataIS=new ObjectInputStream(fileIn);
				Integer num;
				do {
					num=(int) dataIS.readObject();
					if (num!=null) {
						aciertos.add(num);
					}
				} while (num!=null);
				dataIS.close();
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No se pudo abrir el fichero de aciertos");
		} catch (IOException e) {
			// System.out.println("ERROR inesperado de Entrada/Salida en lectura de aciertos");
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: No se pudo encontrar la clase a leer");
		}
		
		File archivoDificultad=new File("datos/dificultad.dat");
		try {
				FileInputStream fileIn = new FileInputStream(archivoDificultad);
				ObjectInputStream dataIS=new ObjectInputStream(fileIn);
				String dif;
				dif=(String) dataIS.readObject();
				dataIS.close();
				dificultad=dif;
				lbDificultad.setText(dificultad);
				if (dificultad.equals("Facil")) {
		    		lbDificultad.setTextFill(Color.GREEN);
				}
				else if (dificultad.equals("Intermedio")) {
					lbDificultad.setTextFill(Color.ORANGE);
				}
				else {
					lbDificultad.setTextFill(Color.RED);
				}
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No se pudo abrir el fichero de dificultad");
		} catch (IOException e) {
			System.out.println("ERROR inesperado de Entrada/Salida en lectura de dificultad");
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: No se pudo encontrar la clase a leer");
		}
		
		File archivoPistas=new File("datos/pistas.dat");
		try {
				FileInputStream fileIn = new FileInputStream(archivoPistas);
				ObjectInputStream dataIS=new ObjectInputStream(fileIn);
				int num=0;
				num=(int) dataIS.readObject();
				dataIS.close();
				pistas=num;
				lbPistas.setText(String.valueOf(pistas));
				if (pistas==0) {
					btPista.setDisable(true);
				}
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: No se pudo abrir el fichero de pistas");
		} catch (IOException e) {
			System.out.println("ERROR inesperado de Entrada/Salida en lectura de pistas");
		} catch (ClassNotFoundException e) {
			System.out.println("ERROR: No se pudo encontrar la clase a leer");
		}
	}


    
	@FXML
    private Button btGenerar;
	
    @FXML
    private Button btPista;
    
    @FXML
    private Button btResolver;

    @FXML
    private ComboBox<String> cbDificultad;

    @FXML
    private Label lbDificultad;
    
    @FXML
    private Label lbPistas;
    
    @FXML
    private HBox hbCabecera;

    @FXML
    private TextField tf00;

    @FXML
    private TextField tf01;

    @FXML
    private TextField tf02;

    @FXML
    private TextField tf03;

    @FXML
    private TextField tf04;
    
    @FXML
    private TextField tf05;

    @FXML
    private TextField tf06;

    @FXML
    private TextField tf07;

    @FXML
    private TextField tf08;

    @FXML
    private TextField tf10;

    @FXML
    private TextField tf11;

    @FXML
    private TextField tf12;

    @FXML
    private TextField tf13;

    @FXML
    private TextField tf14;

    @FXML
    private TextField tf15;

    @FXML
    private TextField tf16;

    @FXML
    private TextField tf17;

    @FXML
    private TextField tf18;

    @FXML
    private TextField tf20;

    @FXML
    private TextField tf21;

    @FXML
    private TextField tf22;

    @FXML
    private TextField tf23;

    @FXML
    private TextField tf24;

    @FXML
    private TextField tf25;

    @FXML
    private TextField tf26;

    @FXML
    private TextField tf27;

    @FXML
    private TextField tf28;

    @FXML
    private TextField tf30;

    @FXML
    private TextField tf31;

    @FXML
    private TextField tf32;

    @FXML
    private TextField tf33;

    @FXML
    private TextField tf34;

    @FXML
    private TextField tf35;

    @FXML
    private TextField tf36;

    @FXML
    private TextField tf37;

    @FXML
    private TextField tf38;

    @FXML
    private TextField tf40;

    @FXML
    private TextField tf41;

    @FXML
    private TextField tf42;

    @FXML
    private TextField tf43;

    @FXML
    private TextField tf44;

    @FXML
    private TextField tf45;

    @FXML
    private TextField tf46;

    @FXML
    private TextField tf47;

    @FXML
    private TextField tf48;

    @FXML
    private TextField tf50;

    @FXML
    private TextField tf51;

    @FXML
    private TextField tf52;

    @FXML
    private TextField tf53;

    @FXML
    private TextField tf54;

    @FXML
    private TextField tf55;

    @FXML
    private TextField tf56;

    @FXML
    private TextField tf57;

    @FXML
    private TextField tf58;

    @FXML
    private TextField tf60;

    @FXML
    private TextField tf61;

    @FXML
    private TextField tf62;

    @FXML
    private TextField tf63;

    @FXML
    private TextField tf64;

    @FXML
    private TextField tf65;

    @FXML
    private TextField tf66;

    @FXML
    private TextField tf67;

    @FXML
    private TextField tf68;

    @FXML
    private TextField tf70;

    @FXML
    private TextField tf71;

    @FXML
    private TextField tf72;

    @FXML
    private TextField tf73;

    @FXML
    private TextField tf74;

    @FXML
    private TextField tf75;

    @FXML
    private TextField tf76;

    @FXML
    private TextField tf77;

    @FXML
    private TextField tf78;

    @FXML
    private TextField tf80;

    @FXML
    private TextField tf81;

    @FXML
    private TextField tf82;

    @FXML
    private TextField tf83;

    @FXML
    private TextField tf84;

    @FXML
    private TextField tf85;

    @FXML
    private TextField tf86;

    @FXML
    private TextField tf87;

    @FXML
    private TextField tf88;



	
	
	
}
