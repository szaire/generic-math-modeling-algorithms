import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Data {
    private int nOrigens;
    private int nDestinos;
    private int[] consumo; // Referente às Origens
    private int[] lucro; // Referente às Origens
    private int[] capacidade; // Referente às Demandas

    public Data(String filename) throws FileNotFoundException {
        // Encontro o arquivo de texto com o nome passado como parâmetro
        File fileData = new File(filename);
        // Declaro um Scanner que produz valores de acordo com a leitura de um arquivo específico
        Scanner in = new Scanner(fileData);

        this.nOrigens = in.nextInt();
        this.nDestinos = in.nextInt();

        this.consumo = new int[nOrigens];
        this.lucro = new int[nOrigens];
        this.capacidade = new int[nDestinos];

        for (int i = 0; i < nOrigens; i++) {
            this.consumo[i] = in.nextInt();
        }
        for (int i = 0; i < nOrigens; i++) {
            this.lucro[i] = in.nextInt();
        }
        for (int i = 0; i < nDestinos; i++) {
            this.capacidade[i] = in.nextInt();
        }
    }

    public void printData() {
        System.out.print("Vetor de Origens - Consumo: \t[");
        for (int i = 0; i < this.consumo.length; i++) {
            if (this.consumo.length - i != 1) {
                System.out.print(this.consumo[i] + "\t");
            }
            else {
                System.out.print(this.consumo[i] + "]");
            }
        }
        System.out.println();

        System.out.print("Vetor de Origens - Lucro: \t[");
        for (int i = 0; i < this.lucro.length; i++) {
            if (this.lucro.length - i != 1) {
                System.out.print(this.lucro[i] + "\t");
            }
            else {
                System.out.print(this.lucro[i] + "]");
            }
        }
        System.out.println();

        System.out.print("Vetor de Destinos - Capacidade: \t[");
        for (int i = 0; i < this.capacidade.length; i++) {
            if (this.capacidade.length - i != 1) {
                System.out.print(this.capacidade[i] + "\t");
            }
            else {
                System.out.print(this.capacidade[i] + "]");
            }
        }
        System.out.println();
    }

    public int get_nOrigens() {
        return nOrigens;
    }

    public int get_nDestinos() {
        return nDestinos;
    }

    public void set_nOrigens(int nOrigens) {
        this.nOrigens = nOrigens;
    }

    public void set_nDestinos(int nDestinos) {
        this.nDestinos = nDestinos;
    }

    public void setConsumo(int[] consumo) {
        this.consumo = consumo;
    }

    public void setLucro(int[] lucro) {
        this.lucro = lucro;
    }

    public void setCapacidade(int[] capacidade) {
        this.capacidade = capacidade;
    }

    public int[] getConsumo() {
        return consumo;
    }

    public int[] getLucro() {
        return lucro;
    }

    public int[] getCapacidade() {
        return capacidade;
    }
}
