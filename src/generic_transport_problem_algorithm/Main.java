import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String filename = "entrada2.txt";
        Data input = new Data(filename);
        input.printData();

        Modeling model = new Modeling(input);
        model.printVarsMatrix();
        model.defineConstraints();
        model.solveObjective("min");
    }
}
