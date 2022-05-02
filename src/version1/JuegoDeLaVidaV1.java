package version1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * PROGRAMA JUEGO DE LA VIDA
 * 
 * Tras varios cambios, he logrado poner el lector de archivos, pero tras tantos cambios no he sido capaz
 * de que funcione correctamente. No obstante, me he quedado sin tiempo, pero a lo largo de los días intentare
 * arreglarlo
 * Espero que en la segunda version este funcionando al 100% :)
 * 
 * @author Marta Muñoz San Román
 *
 */

public class JuegoDeLaVidaV1 {

	// ************ PROPIEDADES ***********************
	// en este caso, sera un bloque de 4 en una matriz de 10x10, que es igual a 40
	static final int SIZE = 10;

	// CELULAS: en la matriz, una muerta tiene 0 y una viva un 1
	static final char MUERTA = 0;
	static final char VIVA = 1;
	// CELULAS: imprime una muerta con espacio y una viva con +
		static final char PRINT_MUERTA = ' ';
		static final char PRINT_VIVA = '*';
	

	// 'genesis' es el estado incial de la matriz (gen 0), que ira cambiando con
	// cada nueva generacion
	static int[][] genesis = new int[SIZE][SIZE];
	

	/**
	 * METODO QUE LEE ARCHIVO CSV Y LO CARGA Esta informacion sera la que se cargara
	 * incialmente
	 * 
	 * @param ruta nombre y ruta completa del fichero al que escribimos
	 * @return array de 2D con los valores enteros leidos del fichero
	 * 
	 */

	static final String SEPARADOR = ",";

	public static void cargaCSV(String ruta) {
		// variables
		String linea;
		String[] fila;
		int numFila = 0;

		try (BufferedReader lector = new BufferedReader(new FileReader(ruta))) {
			

			while ((linea = lector.readLine()) != null) {

				fila = linea.split(",");
				for (int columnas = 0; columnas < SIZE; columnas++) {
					if (columnas < fila.length) {
						genesis[numFila][columnas] = Integer.parseInt(fila[columnas].trim());
					} else {
						//el resto de elementos se completan a cero
						genesis[numFila][columnas] = 0;
					}

				} 
				numFila++;

			} 
			// segun el ejercicio, una vez se recorran todas las filas el resto se rellenan con ceros
			if (numFila < SIZE) {
				for (int i = numFila; i < SIZE; i++) {
					for (int j = 0; j < SIZE; j++) {
						genesis[i][j] = 0;
					}

				}
			}
			
	} catch (IOException e) {
			System.out.println("Se produjo el siguiente ERROR al acceder al fichero: \n " + e.getMessage());
		} catch (NumberFormatException e) {
			System.out.println("Revise el fichero; hay VALORES que no pueden convertirse a ENTEROS. El ERROR es: \n "
					+ e.getMessage());
		}

	}//FIN metodo cargaCSV
	
	
	/**
	 * METODO que imprime una matriz inicial con caracteres
	 */
	public static void imprimirMatriz() {

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				//si la celula esta viva
				if (genesis[i][j] == VIVA) {
					System.out.println(PRINT_VIVA);
				} else if (genesis[i][j] == MUERTA) {
					System.out.println(PRINT_MUERTA);
				}
				//SALTO DE LINEA para que no se junten
				System.out.println();
			}
		}//fin for

	}//fin metodo
	
	
	
	/**
	 * METODO imprime vida
	 * Este metodo imprime la matriz por pantalla, con los numeros de celulas vivas que rodean
	 * la celda
	 * 
	 * @see celulasVivas
	 */
	static void imprimeVida() {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				System.out.format("%2d ", celulasVivas(i, j));
			}
			System.out.println();
		}

	}
	
	

	/**
	 * 
	 * METODO que determina el numero de celulas que se encuentran dentro del
	 * tablero. Devuelve el numero de celulas vivas que hay, una vez se han contado y
	 * recorrido
	 * 
	 * @param fila
	 * @param columna
	 * @return celViva
	 */
	public static int celulasVivas(int fila, int columna) {
		//propiedad para saber cuando hay una celula vecina viva
		int celViva = 0;
		//propiedades para usar durante el recorrido de la matriz
		int aFila;
		int zFila;
		int aCol;
		int zCol;

		//se recorre todo el tablero por "secciones"
		
		//primero las filas
		if (fila == 0) {
			aFila = 0;
			zFila = 1;
		} else if (fila == (SIZE -1)) {
			aFila = SIZE- 2;
			zFila = SIZE -1;
		} else {
			aFila = fila -1;
			zFila = fila +1;
		}
		
		//luego las columnas
		if (columna == 0) {
			aCol = 0;
			zCol = 1;
		} else if (columna == (SIZE -1)) {
			aCol = SIZE- 2;
			zCol = SIZE -1;
		} else {
			aCol = fila -1;
			zCol = fila +1;
		}
		
		//se recorre el tablero una vez delimitados los comienzos y finales de las filas y las columnas
		for (int i = aFila; i <= zFila; i++) {
			for (int j = aCol; j <= zCol; j++) {
				if ((i == fila && j == columna) && (genesis[i][j] == VIVA)) {
					celViva--;
				}
			}

		}
		
		return celViva;

	}
	
	


	/**
	 * METODO que decide si una celula vive, nace o muere,
	 * De acuerdo con las reglas del Juego de la Vida
	 * 
	 * @param args
	 */

	public static int[][] nextGen(int[][] generacion) {

		// TECLADO: Se pulsa 1 para seguir con la siguiente generacion; se pulsa 2 para salir
		Scanner teclado = new Scanner(System.in);
		int stop = 1;

		int[][] nextGen = new int[SIZE][SIZE];

		while (stop == 1) {
			// recorre las celulas
			for (int i = 0; i < SIZE -1; i++) {
				for (int j = 0; j < SIZE-1; j++) {
					celulasVivas(i, j);
					// si la celda esta viva...
					if (generacion[i][j] == VIVA) {
						// muere por soledad si tiene menos de 2 vecinos
						if (celulasVivas(i, j) < 2) {
							nextGen[i][j] = MUERTA;
							// si es tiene 2 o 3 vecinos sobrevive
						} else if (celulasVivas(i, j) < 4) {
							nextGen[i][j] = VIVA;
							// si tiene mas de 3 vecinos muere por sobrepoblacion
						} else {
							nextGen[i][j] = MUERTA;
						}
						// si la celda esta muerta, pero esta rodeada EXACTAMENTE por 3 celulas, nace
						// por generacion espontanea
					} else if (celulasVivas(i, j) == 3) {
							nextGen[i][j] = VIVA;
						}
					}
				}
			System.out.println("CREA una nueva generación con 1; SAL del programa con 2.");
			imprimeVida();
			stop = teclado.nextInt();
			}//fin while
		teclado.close();
		return nextGen;

	}// fin metodo


	public static void main(String[] args) {
		
		//ahora el archivo se carga desde una carpeta del proyecto
		final String RUTA_LECTURA = "CSVs/inicioVida.txt";

		cargaCSV(RUTA_LECTURA);

		imprimeVida();
		//imprime siguiente generacion y se guarda en la matriz inicial
		genesis= nextGen(genesis);
		

			

		}

}

