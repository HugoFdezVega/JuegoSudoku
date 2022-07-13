package JuegoSudoku;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Sudoku {
	private static Set<Integer> sfila0 = new LinkedHashSet<>();
	private static Set<Integer> sfila1 = new LinkedHashSet<>();
	private static Set<Integer> sfila2 = new LinkedHashSet<>();
	private static Set<Integer> sfila3 = new LinkedHashSet<>();
	private static Set<Integer> sfila4 = new LinkedHashSet<>();
	private static Set<Integer> sfila5 = new LinkedHashSet<>();
	private static Set<Integer> sfila6 = new LinkedHashSet<>();
	private static Set<Integer> sfila7 = new LinkedHashSet<>();
	private static Set<Integer> sfila8 = new LinkedHashSet<>();

	private static Set<Integer> col0 = new LinkedHashSet<>();
	private static Set<Integer> col1 = new LinkedHashSet<>();
	private static Set<Integer> col2 = new LinkedHashSet<>();
	private static Set<Integer> col3 = new LinkedHashSet<>();
	private static Set<Integer> col4 = new LinkedHashSet<>();
	private static Set<Integer> col5 = new LinkedHashSet<>();
	private static Set<Integer> col6 = new LinkedHashSet<>();
	private static Set<Integer> col7 = new LinkedHashSet<>();
	private static Set<Integer> col8 = new LinkedHashSet<>();

	private static Set<Integer> cuadroP = new LinkedHashSet<>();
	private static Set<Integer> cuadroSg = new LinkedHashSet<>();
	private static Set<Integer> cuadroT = new LinkedHashSet<>();
	private static Set<Integer> cuadroC = new LinkedHashSet<>();
	private static Set<Integer> cuadroQ = new LinkedHashSet<>();
	private static Set<Integer> cuadroSx = new LinkedHashSet<>();
	private static Set<Integer> cuadroSp = new LinkedHashSet<>();
	private static Set<Integer> cuadroO = new LinkedHashSet<>();
	private static Set<Integer> cuadroN = new LinkedHashSet<>();
	
	private Sudoku() {
		
	};

	public static List[] crear() {
		do {
			limpiar();
			generarSudoku();
		} while (sfila0.size()<9||sfila1.size()<9||sfila2.size()<9||sfila3.size()<9||sfila4.size()<9||sfila5.size()<9||sfila6.size()<9||sfila7.size()<9||sfila8.size()<9);
		
		List<String>fila0= new ArrayList<>();
		for (Integer i: sfila0) {
			fila0.add(String.valueOf(i));
		}
		List<String>fila1= new ArrayList<>();
		for (Integer i: sfila1) {
			fila1.add(String.valueOf(i));
		}
		List<String>fila2= new ArrayList<>();
		for (Integer i: sfila2) {
			fila2.add(String.valueOf(i));
		}
		List<String>fila3= new ArrayList<>();
		for (Integer i: sfila3) {
			fila3.add(String.valueOf(i));
		}
		List<String>fila4= new ArrayList<>();
		for (Integer i: sfila4) {
			fila4.add(String.valueOf(i));
		}
		List<String>fila5= new ArrayList<>();
		for (Integer i: sfila5) {
			fila5.add(String.valueOf(i));
		}
		List<String>fila6= new ArrayList<>();
		for (Integer i: sfila6) {
			fila6.add(String.valueOf(i));
		}
		List<String>fila7= new ArrayList<>();
		for (Integer i: sfila7) {
			fila7.add(String.valueOf(i));
		}
		List<String>fila8= new ArrayList<>();
		for (Integer i: sfila8) {
			fila8.add(String.valueOf(i));
		}
		
		List[] sudoku=new List[] {fila0,fila1,fila2,fila3,fila4,fila5,fila6,fila7,fila8};
		
		String patata=fila1.get(5);
		
		return sudoku;
	}
	
	private static void generarCasilla(Set<Integer> fila, Set<Integer> col, Set<Integer> cuadro) {
		int numero = 0;
		boolean duplicado;
		long intentos=0;
		
		do {
			duplicado = false;
			numero = (int) Math.floor(Math.random() * (9 - 1 + 1) + 1);
			intentos++;
			if (fila.add(numero)) {
				if (cuadro.add(numero) == false) {
					fila.remove(numero);
					duplicado = true;
				} else {
					if(col.add(numero) == false) {
						fila.remove(numero);
						cuadro.remove(numero);
						duplicado = true;
					}
				}
			} else {
				duplicado = true;
			}
		} while (duplicado == true&&intentos<9000);
	}
	
	private static void generarSudoku() {
		int indice=1;
		do {
			switch(indice) {
				case 1: generarCasilla(sfila0,col0,cuadroP);
				break;
				case 2: generarCasilla(sfila0,col1,cuadroP);
				break;
				case 3: generarCasilla(sfila0,col2,cuadroP);
				break;
				case 4: generarCasilla(sfila0,col3,cuadroSg);
				break;
				case 5: generarCasilla(sfila0,col4,cuadroSg);
				break;
				case 6: generarCasilla(sfila0,col5,cuadroSg);
				break;
				case 7: generarCasilla(sfila0,col6,cuadroT);
				break;
				case 8: generarCasilla(sfila0,col7,cuadroT);
				break;
				case 9: generarCasilla(sfila0,col8,cuadroT);
				break;
				case 10: generarCasilla(sfila1,col0,cuadroP);
				break;
				case 11: generarCasilla(sfila1,col1,cuadroP);
				break;
				case 12: generarCasilla(sfila1,col2,cuadroP);
				break;
				case 13: generarCasilla(sfila1,col3,cuadroSg);
				break;
				case 14: generarCasilla(sfila1,col4,cuadroSg);
				break;
				case 15: generarCasilla(sfila1,col5,cuadroSg);
				break;
				case 16: generarCasilla(sfila1,col6,cuadroT);
				break;
				case 17: generarCasilla(sfila1,col7,cuadroT);
				break;
				case 18: generarCasilla(sfila1,col8,cuadroT);
				break;
				case 19: generarCasilla(sfila2,col0,cuadroP);
				break;
				case 20: generarCasilla(sfila2,col1,cuadroP);
				break;
				case 21: generarCasilla(sfila2,col2,cuadroP);
				break;
				case 22: generarCasilla(sfila2,col3,cuadroSg);
				break;
				case 23: generarCasilla(sfila2,col4,cuadroSg);
				break;
				case 24: generarCasilla(sfila2,col5,cuadroSg);
				break;
				case 25: generarCasilla(sfila2,col6,cuadroT);
				break;
				case 26: generarCasilla(sfila2,col7,cuadroT);
				break;
				case 27: generarCasilla(sfila2,col8,cuadroT);
				break;
				case 28: generarCasilla(sfila3,col0,cuadroC);
				break;
				case 29: generarCasilla(sfila3,col1,cuadroC);
				break;
				case 30: generarCasilla(sfila3,col2,cuadroC);
				break;
				case 31: generarCasilla(sfila3,col3,cuadroQ);
				break;
				case 32: generarCasilla(sfila3,col4,cuadroQ);
				break;
				case 33: generarCasilla(sfila3,col5,cuadroQ);
				break;
				case 34: generarCasilla(sfila3,col6,cuadroSx);
				break;
				case 35: generarCasilla(sfila3,col7,cuadroSx);
				break;
				case 36: generarCasilla(sfila3,col8,cuadroSx);
				break;
				case 37: generarCasilla(sfila4,col0,cuadroC);
				break;
				case 38: generarCasilla(sfila4,col1,cuadroC);
				break;
				case 39: generarCasilla(sfila4,col2,cuadroC);
				break;
				case 40: generarCasilla(sfila4,col3,cuadroQ);
				break;
				case 41: generarCasilla(sfila4,col4,cuadroQ);
				break;
				case 42: generarCasilla(sfila4,col5,cuadroQ);
				break;
				case 43: generarCasilla(sfila4,col6,cuadroSx);
				break;
				case 44: generarCasilla(sfila4,col7,cuadroSx);
				break;
				case 45: generarCasilla(sfila4,col8,cuadroSx);
				break;
				case 46: generarCasilla(sfila5,col0,cuadroC);
				break;
				case 47: generarCasilla(sfila5,col1,cuadroC);
				break;
				case 48: generarCasilla(sfila5,col2,cuadroC);
				break;
				case 49: generarCasilla(sfila5,col3,cuadroQ);
				break;
				case 50: generarCasilla(sfila5,col4,cuadroQ);
				break;
				case 51: generarCasilla(sfila5,col5,cuadroQ);
				break;
				case 52: generarCasilla(sfila5,col6,cuadroSx);
				break;
				case 53: generarCasilla(sfila5,col7,cuadroSx);
				break;
				case 54: generarCasilla(sfila5,col8,cuadroSx);
				break;
				case 55: generarCasilla(sfila6,col0,cuadroSp);
				break;
				case 56: generarCasilla(sfila6,col1,cuadroSp);
				break;
				case 57: generarCasilla(sfila6,col2,cuadroSp);
				break;
				case 58: generarCasilla(sfila6,col3,cuadroO);
				break;
				case 59: generarCasilla(sfila6,col4,cuadroO);
				break;
				case 60: generarCasilla(sfila6,col5,cuadroO);
				break;
				case 61: generarCasilla(sfila6,col6,cuadroN);
				break;
				case 62: generarCasilla(sfila6,col7,cuadroN);
				break;
				case 63: generarCasilla(sfila6,col8,cuadroN);
				break;
				case 64: generarCasilla(sfila7,col0,cuadroSp);
				break;
				case 65: generarCasilla(sfila7,col1,cuadroSp);
				break;
				case 66: generarCasilla(sfila7,col2,cuadroSp);
				break;
				case 67: generarCasilla(sfila7,col3,cuadroO);
				break;
				case 68: generarCasilla(sfila7,col4,cuadroO);
				break;
				case 69: generarCasilla(sfila7,col5,cuadroO);
				break;
				case 70: generarCasilla(sfila7,col6,cuadroN);
				break;
				case 71: generarCasilla(sfila7,col7,cuadroN);
				break;
				case 72: generarCasilla(sfila7,col8,cuadroN);
				break;
				case 73: generarCasilla(sfila8,col0,cuadroSp);
				break;
				case 74: generarCasilla(sfila8,col1,cuadroSp);
				break;
				case 75: generarCasilla(sfila8,col2,cuadroSp);
				break;
				case 76: generarCasilla(sfila8,col3,cuadroO);
				break;
				case 77: generarCasilla(sfila8,col4,cuadroO);
				break;
				case 78: generarCasilla(sfila8,col5,cuadroO);
				break;
				case 79: generarCasilla(sfila8,col6,cuadroN);
				break;
				case 80: generarCasilla(sfila8,col7,cuadroN);
				break;
				case 81: generarCasilla(sfila8,col8,cuadroN);
				break;
			}
			indice++;
		} while (indice<82);
	}
	
	private static void limpiar() {
		sfila0.clear();
		sfila1.clear();
		sfila2.clear();
		sfila3.clear();
		sfila4.clear();
		sfila5.clear();
		sfila6.clear();
		sfila7.clear();
		sfila8.clear();
		col0.clear();
		col1.clear();
		col2.clear();
		col3.clear();
		col4.clear();
		col5.clear();
		col6.clear();
		col7.clear();
		col8.clear();
		cuadroP.clear();
		cuadroSg.clear();
		cuadroT.clear();
		cuadroC.clear();
		cuadroQ.clear();
		cuadroSx.clear();
		cuadroSp.clear();
		cuadroO.clear();
		cuadroN.clear();
	}

	
}
