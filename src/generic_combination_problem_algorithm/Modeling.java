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
    int[][] solucao;

    // Inicializando Infinity
    {
        infinity = Double.POSITIVE_INFINITY;
    }

    public Modeling(Data input) {
        Loader.loadNativeLibraries();
        this.dataInput = input;

        // Para esse caso empre será um problema de PLI, pois o valor das variáveis
        // é apenas binário, ou seja, 0 ou 1
        this.solver = MPSolver.createSolver("SCIP");

        this.nOrigens = dataInput.get_nOrigens();
        this.nDestinos = dataInput.get_nDestinos();

        // Definindo todas as veriáveis da problemática
        this.vars = new MPVariable[nOrigens][nDestinos];
        for (int i = 0; i < nOrigens; i++) {
            for (int j = 0; j < nDestinos; j++) {
                this.vars[i][j] = solver.makeBoolVar("x[" + (i+1) + "][" + (j+1) + "]");
            }
        }
    }

    public void defineConstraints() {
        Loader.loadNativeLibraries();

        // Restrições referente aos destinos
        for (int i = 0; i < this.nDestinos; i++)
        {
            double capacidade = this.dataInput.getCapacidade()[i];
            MPConstraint destiny_con = solver.makeConstraint(0.0, capacidade);
            for (int j = 0; j < this.nOrigens; j++) {
                destiny_con.setCoefficient(this.vars[j][i], this.dataInput.getConsumo()[j]);
            }
        }

        // Restrições referente às origens
        for (int i = 0; i < this.nOrigens; i++)
        {
            MPConstraint origin_con = solver.makeConstraint(0.0, 1.0, "con " + (i+1));
            for (int j = 0; j < this.nDestinos; j++) {
                origin_con.setCoefficient(this.vars[i][j], 1);
            }
        }

        System.out.println("Número de restrições = " + solver.numConstraints());
    }
    /**
    * @param type Sets objective to maximization ("max") or minimization ("min")
    *
    * */
    public void solveObjective(String type) {
        Loader.loadNativeLibraries();

        MPObjective objective = solver.objective();

        for (int i = 0; i < nOrigens; i++) {
            for (int j = 0; j < nDestinos; j++) {
                objective.setCoefficient(this.vars[i][j], this.dataInput.getLucro()[i]);
            }
        }

        if (type.equals("max")) objective.setMaximization();
        else if (type.equals("min")) objective.setMinimization();
        else throw new IllegalArgumentException("Argumento informado inválido! Escolha entre 'max' ou 'min'.");

        MPSolver.ResultStatus resultStatus = solver.solve();

        if (resultStatus == MPSolver.ResultStatus.OPTIMAL) {
            System.out.println("Solução:");
            System.out.println("Custo da função objetivo = " + objective.value());

            // A matriz solucao desempenha o papel de guardar o valor substituído das soluções
            // (1 em vez de 0.999999...)
            this.solucao = new int[nOrigens][nDestinos]; // inicializando a matriz solução
            for (int i = 0; i < nOrigens; i++) {
                for (int j = 0; j < nDestinos; j++) {
                    if (this.vars[i][j].solutionValue() > 0.9) {
                        this.solucao[i][j] = 1;
                    }
                }
            }
            
            for (int i = 0; i < this.vars.length; i++) {
                for (int j = 0; j < this.vars[i].length; j++) {
                    System.out.println(this.vars[i][j].name() + " = " + this.solucao[i][j]);
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
