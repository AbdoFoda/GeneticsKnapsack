import javafx.util.Pair;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Main {
    static final int popSize = 2000;
    static final Double pc = 0.4;
    static final Double pm = 0.01;
    static final int numberOfGenerations = 5000;

    static ArrayList<Chromosome> population = new ArrayList<>();
    static Random R = new Random();

    public static void generatePopulation(int num_items) {
        population = new ArrayList<>();
        for(int i=0; i<popSize;i++)
        {
            Chromosome c = new Chromosome();
            for(int j=0; j<num_items;j++)
            {
                double D=R.nextDouble()*1;
                if(D<0.5)
                {
                    c.genes.add(true);
                }else{
                    c.genes.add(false);
                }
            }
            population.add(c);
        }
    }

    public static void evaluate(ArrayList<Item> items , int knapsackSize) {
        for(int i=0; i<popSize;i++)
        {
            int fitness = 0 , weight = 0;
            for(int j=0;j<items.size();++j){
                if(population.get(i).genes.get(j)) {
                    weight += items.get(j).weight;
                    fitness += items.get(j).benefit;
                }
            }
            if(weight > knapsackSize){
                fitness = -1;
            }
            population.get(i).fitness = fitness;
        }
    }
    public static Integer getChromosome(ArrayList<Integer> cum) {
        Integer idx = 0;
        int s = R.nextInt(cum.get(cum.size()-1));
        for(int i=0;i<cum.size();++i){
            if(cum.get(i) >= s){
                idx = i;
            }
        }
        return idx;
    }

    public static Pair<Integer,Integer> select(){
        ArrayList<Integer> cum = new ArrayList<>();
        for(int i=0;i<popSize;++i){
            if(population.get(i).fitness != -1){
                int prev = 0;
                if( cum.size() > 0){
                    prev = cum.get(cum.size()-1);
                }
                cum.add(population.get(i).fitness + prev);
            }
                //cum.add((i == 0 ? 0 : cum.get(cum.size()-1) ) + population.get(i).fitness);
        }
        return  new Pair(getChromosome(cum), getChromosome(cum));
    }

    public static void crossOver(Pair<Integer,Integer> selected,
                                                        Integer numOfGenes){
        Double r2 = R.nextDouble();
        if(r2 > pc){
            return;
        }
        Integer r1 = R.nextInt(numOfGenes-1) + 1;
        Chromosome c1 = population.get(selected.getKey()),
                   c2 = population.get(selected.getValue());
        for(int i=r1;i<numOfGenes;++i){
            Boolean tmp = c1.genes.get(i);
            c1.genes.set(i,c2.genes.get(i));
            c2.genes.set(i,tmp);
        }
    }
    public static void mutate(Chromosome c) {
        for(int i=0;i<c.genes.size();++i){
            Double d = R.nextDouble();
            if(d <= pm){
                c.genes.set(i,!c.genes.get(i));
            }
        }
    }
    public static void printPoplutation(Integer numberOfGenes){
        for(int j=0;j<popSize;++j)
        {
            for(int k=0;k<numberOfGenes;++k)
            {
                System.out.print(population.get(j).genes.get(k) ? 1 :0);
            }
            System.out.println(" , " + population.get(j).fitness);
        }
        System.out.println("***********");
    }
    public static Boolean isValidPopulation(){
        int cnt = 0;
        for(int i=0;i<popSize;++i){
            if(population.get(i).fitness != -1)
                cnt++;
        }
        return cnt >= 2;
    }
    public static void printSolution(int tc,ArrayList<Item> items){

        int mx = 0, idx = -1 , counter=0;
        for(int i=0;i<population.size();++i){
            if(population.get(i).fitness > mx){
                mx = population.get(i).fitness;
                idx = i;
            }
        }
        if (idx != -1){
            System.out.println("Case " + tc + ": "+mx);
            for(int j=0; j<items.size();j++)
            {
                if(population.get(idx).genes.get(j)){counter++;}
            }
            System.out.println(counter);
            for(int j=0; j<items.size();j++)
            {
                if(population.get(idx).genes.get(j)){
                    System.out.println(items.get(j).weight +" "+items.get(j).benefit);
                }
            }
        }
    }
    public static void main(String[] args) {
        int testCase,sizeOfKnapsack,numberOfGenes;
        File f = new File("/Users/apple/IdeaProjects/Genetics-1/src/in.txt");
        Scanner in;
        try {
            in = new Scanner(f);
            testCase= in.nextInt();
            for(int i=0;i<testCase;++i)
            {
                numberOfGenes = in.nextInt() ;
                sizeOfKnapsack = in.nextInt();
                //System.out.println("Here : " +numberOfGenes +" "+sizeOfKnapsack);
                ArrayList<Item> items = new ArrayList<>(numberOfGenes);
                for(int j=0;j<numberOfGenes;++j)
                {
                    items.add( new Item(in.nextInt() , in.nextInt()) );
                }
                generatePopulation(numberOfGenes);
                evaluate(items,sizeOfKnapsack);
                //printPoplutation(numberOfGenes);
                for(int j = 0; isValidPopulation() && j< numberOfGenerations; ++j ){
                    evaluate(items,sizeOfKnapsack);
                    Pair<Integer,Integer> selectedChromosomes = select();
                    crossOver(selectedChromosomes,numberOfGenes);
                    mutate(population.get(selectedChromosomes.getKey()));
                    mutate(population.get(selectedChromosomes.getValue()));
                }
                //printPoplutation(numberOfGenes);
                printSolution(i+1 ,items);
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }

    }
}
