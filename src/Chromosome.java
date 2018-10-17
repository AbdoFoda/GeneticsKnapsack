import java.util.ArrayList;

public class Chromosome {
    ArrayList<Boolean> genes  = new ArrayList<>();
    int fitness;

    public Chromosome(){

    }
    public Chromosome(ArrayList<Boolean> genes, int fitness) {
        this.genes = genes;
        this.fitness = fitness;
    }
}
