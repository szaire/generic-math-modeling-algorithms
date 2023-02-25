import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Data {
    private int nOrigens;
    private int nDestinos;
    private int[] producao;
    private int[] demanda;
    private int[][] custo;
    private int totalOffer;
    private int totalDemand;

    public Data(String filename) throws FileNotFoundException {
        // Encontro o arquivo de texto com o nome passado como parâmetro
        File fileData = new File(filename);
        // Declaro um Scanner que produz valores de acordo com a leitura de um arquivo específico
        Scanner in = new Scanner(fileData);

        this.nOrigens = in.nextInt();
        this.nDestinos = in.nextInt();

        this.producao = new int[nOrigens];
        this.demanda = new int[nDestinos];
        this.custo = new int[nOrigens][nDestinos];

        for (int i = 0; i < nOrigens; i++) {
            this.producao[i] = in.nextInt();
        }
        for (int i = 0; i < nDestinos; i++) {
            this.demanda[i] = in.nextInt();
        }
        for (int i = 0; i < nOrigens; i++) {
            for (int j = 0; j < nDestinos; j++) {
                this.custo[i][j] = in.nextInt();
            }
        }
    }

    public int modelBalance() {
        this.totalOffer = Arrays.stream(this.producao).reduce(0, Integer::sum);
        this.totalDemand = Arrays.stream(this.demanda).reduce(0, Integer::sum);
        int balanceType = totalOffer - totalDemand;

        if (balanceType == 0) {
            System.out.println("=================");
            System.out.println("Modelo Balanceado");
            System.out.println("=================");
            System.out.println();
            return 0;
        }
        else if (balanceType > 0) {
            System.out.println("===============================");
            System.out.println("Modelo Desbalanceado p/ Demanda");
            System.out.println("===============================");
            System.out.println();
            return 1;
        }
        else {
            System.out.println("==============================");
            System.out.println("Modelo Desbalanceado p/ Oferta");
            System.out.println("==============================");
            System.out.println();
            return -1;
        }
    }

    public void printData() {
        System.out.print("Vetor de Origens: \t[");
        for (int i = 0; i < this.producao.length; i++) {
            if (this.producao.length - i != 1) {
                System.out.print(this.producao[i] + "\t");
            }
            else {
                System.out.print(this.producao[i] + "]");
            }
        }
        System.out.println();

        System.out.print("Vetor de Destinos: \t[");
        for (int i = 0; i < this.demanda.length; i++) {
            if (this.demanda.length - i != 1) {
                System.out.print(this.demanda[i] + "\t");
            }
            else {
                System.out.print(this.demanda[i] + "]");
            }
        }
        System.out.println();

        System.out.println("Matriz de Custo:");
        for (int i = 0; i < nOrigens; i++) {
            System.out.print("[");
            for (int j = 0; j < nDestinos; j++) {
                if (this.custo[i].length - j != 1) {
                    System.out.print(this.custo[i][j] + "\t");
                }
                else {
                    System.out.print(this.custo[i][j] + "]");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public int get_nOrigens() {
        return nOrigens;
    }

    public int get_nDestinos() {
        return nDestinos;
    }

    public void setnOrigens(int nOrigens) {
        this.nOrigens = nOrigens;
    }

    public void setnDestinos(int nDestinos) {
        this.nDestinos = nDestinos;
    }

    public void setProducao(int[] producao) {
        this.producao = producao;
    }

    public void setDemanda(int[] demanda) {
        this.demanda = demanda;
    }

    public int[] getProducao() {
        return producao;
    }

    public int[] getDemanda() {
        return demanda;
    }

    public void setCusto(int[][] custo) {
        this.custo = custo;
    }

    public int[][] getCusto() {
        return custo;
    }

    public int getTotalOffer() {
        return totalOffer;
    }

    public int getTotalDemand() {
        return totalDemand;
    }
}
