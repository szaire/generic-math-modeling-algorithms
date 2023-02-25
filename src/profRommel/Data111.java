import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Data111 {

	int nOrigens;
	int nDestinos;
	int[] producao;
	int[] demanda;
	int[][] custo;

	public Data111(String filename) throws FileNotFoundException {
		
		File f = new File(filename);

		Scanner s = new Scanner(f);
		nOrigens = s.nextInt();
		nDestinos = s.nextInt();

		producao = new int[nOrigens];
		for (int i = 0; i < nOrigens; i++) {
			producao[i] = s.nextInt();
		}

		demanda = new int[nDestinos];
		for (int i = 0; i < nDestinos; i++) {
			demanda[i] = s.nextInt();
		}

		custo = new int[nOrigens][nDestinos];
		for (int i = 0; i < nOrigens; i++) {
			for (int j = 0; j < nDestinos; j++) {
				custo[i][j] = s.nextInt();
			}
		}
	}
	
	public void mostraDados() {
		System.out.println("N�mero de origens: " + nOrigens);
		System.out.println("N�mero de destinos: " + nDestinos);
		System.out.println("Produ��o:");
		for (int i = 0; i < nOrigens; i++) {
			System.out.print(producao[i] + " ");
		}
		System.out.println();
		System.out.println("Demanda:");
		for (int i = 0; i < nDestinos; i++) {
			System.out.print(demanda[i] + " ");
		}
		System.out.println();
		System.out.println("Matriz custo:");
		for (int i = 0; i < nOrigens; i++) {
			for (int j = 0; j < nDestinos; j++) {
				System.out.print(custo[i][j] + " ");
			}
			System.out.println();
		}
	}
}
