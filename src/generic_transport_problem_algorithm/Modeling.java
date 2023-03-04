import com.google.ortools.Loader;
import com.google.ortools.linearsolver.MPConstraint;
import com.google.ortools.linearsolver.MPObjective;
import com.google.ortools.linearsolver.MPSolver;
import com.google.ortools.linearsolver.MPVariable;

public class Modeling
{
    Data dataInput;

    MPSolver solver;
    private double infinity;

    int nOrigens;
    int nDestinos;
    MPVariable[][] vars;
    MPConstraint[] cons;

    // Inicializando Infinity
    {
        infinity = Double.POSITIVE_INFINITY;
    }

    public Modeling(Data input) {
        Loader.loadNativeLibraries();
        this.dataInput = input;

        this.solver = MPSolver.createSolver("SCIP");

        int balanceType = this.dataInput.modelBalance();
        if (balanceType == 0) {
            balancedModeling();
        }
        else {
            int expectedBalance = this.dataInput.getTotalOffer() - this.dataInput.getTotalDemand();

            if (expectedBalance > 0) {
                unbalancedModeling_Demand(expectedBalance);
            }
            else if (expectedBalance < 0) {
                unbalancedModeling_Offer(expectedBalance * -1);
            }

            System.out.println("~x~ Nova configuração do modelo (agora balanceado): ~x~");
            this.dataInput.printData();
        }

        // Definindo todas as veriáveis da problemática
        this.vars = new MPVariable[nOrigens][nDestinos];
        for (int i = 0; i < nOrigens; i++) {
            for (int j = 0; j < nDestinos; j++) {
                this.vars[i][j] = solver.makeIntVar(0.0, infinity, "x_" + (i+1) + "_" + (j+1));
            }
        }
        // Definindo vetor de restrições
        this.cons = new MPConstraint[nOrigens + nDestinos];
    }

    private void balancedModeling() {
        this.nOrigens = dataInput.get_nOrigens();
        this.nDestinos = dataInput.get_nDestinos();
    }

    private void unbalancedModeling_Demand(int expectedBalance) {
        this.nOrigens = dataInput.get_nOrigens();
        this.nDestinos = dataInput.get_nDestinos() + 1;
        this.dataInput.setnDestinos(this.nDestinos);

        // Atualizando o vetor de Demanda para o caso desbalanceado
        int[] newDemanda = new int[this.nDestinos];
        for (int i = 0; i < this.nDestinos-1; i++) {
            newDemanda[i] = this.dataInput.getDemanda()[i];
        }
        newDemanda[this.nDestinos-1] = expectedBalance; // Demanda fictícia para balancear o modelo
        this.dataInput.setDemanda(newDemanda);

        // Atualizando a matriz custo para o caso desbalanceado
        int[][] newCusto = new int[this.nOrigens][this.nDestinos];
        for (int i = 0; i < this.nOrigens; i++) {
            for (int j = 0; j < this.nDestinos; j++) {
                if (j == this.nDestinos - 1) {
                    break; // Adicionando custo 0 à nova demanda fictícia
                }
                newCusto[i][j] = this.dataInput.getCusto()[i][j];
            }
        }
        this.dataInput.setCusto(newCusto);
    }

    private void unbalancedModeling_Offer(int expectedBalance) {
        this.nOrigens = dataInput.get_nOrigens() + 1;
        this.nDestinos = dataInput.get_nDestinos();
        this.dataInput.setnOrigens(this.nOrigens);

        // Atualizando o vetor de Demanda para o caso desbalanceado
        int[] newProducao = new int[this.nOrigens];
        for (int i = 0; i < this.nOrigens-1; i++) {
            newProducao[i] = this.dataInput.getProducao()[i];
        }
        newProducao[this.nOrigens-1] = expectedBalance; // Oferta fictícia para balancear o modelo
        this.dataInput.setProducao(newProducao);

        // Atualizando a matriz custo para o caso desbalanceado
        int[][] newCusto = new int[this.nOrigens][this.nDestinos];
        for (int i = 0; i < this.nOrigens; i++) {
            for (int j = 0; j < this.nDestinos; j++) {
                if (i == this.nOrigens - 1) {
                    break; // Adicionando custo 0 à nova produção fictícia
                }
                newCusto[i][j] = this.dataInput.getCusto()[i][j];
            }
        }
        this.dataInput.setCusto(newCusto);
    }

    public void defineConstraints() {
        Loader.loadNativeLibraries();

        // Definindo as restrições da problemática
        int conIndexRef = 0;

        // Restrições referente às origens
        for (int i = 0; i < this.nOrigens; i++)
        {
            double producao = this.dataInput.getProducao()[i];
            this.cons[conIndexRef] = solver.makeConstraint(producao, producao);

            for (int j = 0; j < this.nDestinos; j++) {
                this.cons[conIndexRef].setCoefficient(this.vars[i][j], 1);
            }

            conIndexRef++;
        }
        
        // Restrições referente aos destinos
        for (int i = 0; i < this.nDestinos; i++)
        {
            double demanda = this.dataInput.getDemanda()[i];
            this.cons[conIndexRef] = solver.makeConstraint(demanda, demanda);

            for (int j = 0; j < this.nOrigens; j++) {
                this.cons[conIndexRef].setCoefficient(this.vars[j][i], 1);
            }

            conIndexRef++;
        }
        System.out.println("Número de restrições = " + solver.numConstraints());
    }

    public void solveObjective(String type) {
        Loader.loadNativeLibraries();

        MPObjective objective = solver.objective();

        for (int i = 0; i < nOrigens; i++) {
            for (int j = 0; j < nDestinos; j++) {
//                System.out.println("variavel: " + this.vars[i][j]);
//                System.out.println("coeficiente: " + this.dataInput.getCusto()[i][j]);
                objective.setCoefficient(this.vars[i][j], this.dataInput.getCusto()[i][j]);
            }
        }

        if (type.equals("max")) objective.setMaximization();
        else if (type.equals("min")) objective.setMinimization();
        else throw new IllegalArgumentException("Argumento informado não é válido! Escolha entre 'max' ou 'min'.");

        MPSolver.ResultStatus resultStatus = solver.solve();

        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("Solução:");
            System.out.println("Custo da função objetivo = " + objective.value());

            for (int i = 0; i < this.vars.length; i++) {
                for (int j = 0; j < this.vars[i].length; j++) {
                    System.out.println(this.vars[i][j].name() + " = " + this.vars[i][j].solutionValue());
                }
            }

            System.out.println("Tempo de resolução = " + solver.wallTime() + " milissegundos");
            System.out.println(solver.exportModelAsLpFormat());
        } else {
            System.out.println("Solução ótima não encontrada!");
        }
    }

    public void printVarsMatrix() {
        System.out.println("Número de variáveis = " + solver.numVariables());
        System.out.println("Matriz de Variáveis:");
        for (MPVariable[] var : this.vars) {
            System.out.print("[");
            for (int j = 0; j < var.length; j++) {
                if (var.length - j != 1) {
                    System.out.print(var[j].name() + "\t");
                } else {
                    System.out.print(var[j].name() + "]");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
