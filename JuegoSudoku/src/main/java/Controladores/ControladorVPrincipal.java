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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
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
	 * M�todo initialize() que se ejecuta al ejecutarse la aplicaci�n. Crea las opcionesd el ComboBox de dificultad, pone por defecto la primera,
	 * situa la informaci�n contextual, crea la matriz con todos los TextFields para su posterior manipulaci�n y, haciendo uso de �sta, situa los
	 * KeyEvent y los addListener. Despu�s corre el m�todo leer para cargar la info de partidas anteriores y da formato a la tabla para que quede
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
	 * Accion acGenerar, que se produce al pulsar el Bot�n btGenerar, que crea un sudoku mediante la clase Sudoku, corre limpiar() 
	 * y corre el m�todo establecerDificultad con la dificultar seleccionada en el ComboBox correspondiente.
	 */
    @FXML
    void acGenerar(ActionEvent event) {
    	sudoku=Sudoku.crear();
    	limpiar();
    	establecerDificultad(cbDificultad.getSelectionModel().getSelectedItem());
    }
    
    /*
     * M�todo limpiar() que crea un nuevo set aciertos para ir guardando los aciertos dentro, y recorre matriz para limpiar los TextField,
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
     * M�todo establecerDificultad() que recoge un dif, que se le pasar� desde acGenerar extra�do del ComboBox, y asigna dicha dificultad al field dificultad.
     * Despu�s, seg�n la dificultad que haya recibido, da el formato al label correspondiente, lo asigna, activa el bot�n de Pista si fuera necesario y
     * pasa a rellenarCasilla el n�mero de casillas que se deben rellenar seg�n la dificultad seleccionada.
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
     * Recibe un n�mero de vueltas que tiene que dar para generar esa cantidad de n�meros en casillas aleatorias. Para ello, genera dos n�meros aleatorios y
     * aplica una f�rmula para generar el correspondiente �ndice de matriz. Si dicho �ndice es superior a 80 es err�neo (solo puede haber 81 elementos) as� que
     * sigue dentro del bucle, y si no se puede agregar dicho �ndice al set aciertos es que est� repetido, por lo que sigue en el bucle y genera otra casilla.
     * Una vez tenemos el �ndice de la casilla, vamos a la fila y casilla correspondientes de sudoku y asignamos su valor al �ndice de matriz, haciendo no-editable
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
     * Evento comprobarAcierto, que recibir� un evento cuando se suelte una tecla. Obtenemos la fuente de dicho evento y la comparamos con matriz, 
     * para obtener el �ndice en que se ha realizado dicho cambio. Despu�s aplicamos una f�rmula para obtener la posici�n de la fila y la casilla de sodoku
     * correspondiente a dicho �ndice. Si el texto introducido en dicho �ndice es igual al de sudoku, el n�mero introducido en el TextField es correcto. Si la 
     * dificultad no es dif�cil, la casilla se pondr� verde y se bloquear�. Si no coincide el n�mero introducido ser� err�neo, por lo que si la dificultad
     * no es dif�cil, la casilla se pondr� en rojo.
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
     * M�todo formatearTabla() 
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
    
    private void comprobarReglas(String oldValue, String newValue) {
	    if (! matriz[indiceEscuchado].getText().matches("\\d*")) {
	    	matriz[indiceEscuchado].setText(matriz[indiceEscuchado].getText().replaceAll("[^\\d]", ""));
	    }
	    if (! matriz[indiceEscuchado].getText().matches("\\d{1}")) {
	    	matriz[indiceEscuchado].setText(matriz[indiceEscuchado].getText().replaceFirst("[\\d]", ""));
	    }
    }
    
    private void comprobarGanador() {
    	if (aciertos.size()==81) {
    		Dialogos.mostrarDialogoInformacion("�Conseguido!", "Felicidades, has resuelto correctamente el sudoku.");
    		btPista.setDisable(true);
        	btResolver.setDisable(true);
    	}
    }
    
    private void infoContextual() {
    	Tooltip difFacil = new Tooltip ("- F�cil: Empieza con 35 casillas rellenas y 5 pistas disponibles. \nLos fallos se indican en rojo y los aciertos en verde. \n - Intermedio: Empieza con 30 casillas rellenas y 3 pistas disponibles. \nLos fallos se indican en rojo y los aciertos en verde. \n - Dif�cil: Empieza con 25 casillas rellenas y 0 pistas disponibles. \nNi los fallos ni los aciertos se indican.");
    	Tooltip tpBtPista = new Tooltip ("Rellena una casilla vac�a aleatoria");
    	Tooltip tpBtResolver = new Tooltip ("Resuelve el sudoku autom�ticamente");
    	
    	cbDificultad.setTooltip(difFacil);
    	btPista.setTooltip(tpBtPista);
    	btResolver.setTooltip(tpBtResolver);
    }
    
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
    	Dialogos.mostrarDialogoInformacion("�Conseguido?", "Felicidades, has resuelto correctamente el sudoku. \n(Con algo de ayuda...)");
    }
    
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
