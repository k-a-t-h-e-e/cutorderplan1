package com.example.cutorderplanservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@RestController
public class Main {

    static int[] cut_order_matrix = {36,144,72,252,504,192};
    static int minQuantity = Arrays.stream(cut_order_matrix).min().getAsInt();
    static  int gmax = 16; // maximum number of garments allocated for a marker
    static final int gmin = 1; // minimum number of garments allocated for a marker

    static final int hmax = 300; // maximum number of plies
    static  int hmin = 10; //minimum number of plies

    private static List<List<Individual>> generateAlgorithm(RequestDTO requestDTO){
        if(requestDTO.getHmin()!=0)
        hmin = requestDTO.getHmin();
        if(requestDTO.getGmax()!=0)
            gmax = requestDTO.getGmax();
        List<List<Individual>> cut_order_plans = new ArrayList<List<Individual>>();

        int runningCount = 1;
        while (runningCount < requestDTO.getRunningCount()) {
            List<Individual> individuals = new ArrayList<Individual>();
            int markercount = 1;
            boolean error = false;
            cut_order_matrix = requestDTO.getCut_order_matrix().clone();
            GeneticAlgorithm ga = new GeneticAlgorithm(100, 0.02, 0.6, 3);
            Population population = ga.initPopulation(6);

            ga.evalPopulation(population);
            while (ga.isTerminationConditionMet(population) == false) {
                //System.out.println("Best solution: " + population.getFittest(0).toString());

                population = ga.crossoverPopulation(population);

                population = ga.mutatePopulation(population);

                ga.evalPopulation(population);
                ga.generationIncrement();
                //        System.out.println("GA COUNT"+ga.getGeneration());
            }

            //System.out.println("Found solution in " + ga.getGeneration() + " generations");
            //System.out.println();
            // System.out.println("Marker " + markercount+" :" + population.getFittest(0).toString());
            individuals.add(population.getFittest(0));

            if (Arrays.stream(population.getFittest(0).getChromosome()).sum() > Main.gmax) {
                //        System.out.println("ERROR " + Arrays.stream(population.getFittest(0).getChromosome()).sum());
                error = true;
            }

            int[] z = population.getFittest(0).getChromosome().clone();


            for (int i = 0; i < z.length; i++) {
                z[i] = z[i] * population.getFittest(0).getB();
                cut_order_matrix[i] = cut_order_matrix[i] - z[i];
            }
            //   System.out.print("New cut order matrix ");
            for (int i = 0; i < cut_order_matrix.length; i++) {
                //       System.out.print(cut_order_matrix[i]+" ");
            }
            //  System.out.println("\n");

            while (Arrays.stream(cut_order_matrix).sum() > 0) {
                ga = new GeneticAlgorithm(100, 0.02, 0.6, 0);
                population = ga.initPopulation(6);
                ga.evalPopulation(population);
                while (ga.isTerminationConditionMet(population) == false) {
                    //System.out.println("Best solution: " + population.getFittest(0).toString());

                    population = ga.crossoverPopulation(population);

                    population = ga.mutatePopulation(population);

                    ga.evalPopulation(population);
                    ga.generationIncrement();
                }

                //System.out.println("Found solution in " + ga.getGeneration() + " generations");
                markercount++;
                //    System.out.println("Marker "+markercount+" :" + population.getFittest(0).toString());
                individuals.add(population.getFittest(0));
                if (Arrays.stream(population.getFittest(0).getChromosome()).sum() > Main.gmax) {
                    //System.out.println("ERROR " + Arrays.stream(population.getFittest(0).getChromosome()).sum());
                    error = true;
                }

                z = population.getFittest(0).getChromosome().clone();


                for (int i = 0; i < z.length; i++) {
                    z[i] = z[i] * population.getFittest(0).getB();
                    cut_order_matrix[i] = cut_order_matrix[i] - z[i];
                }
                //     System.out.print("New cut order matrix ");
                for (int i = 0; i < cut_order_matrix.length; i++) {
                    //        System.out.print(cut_order_matrix[i]+" ");
                }


//                System.out.println("\n");
            }
            //  System.out.print("Wastage: ");
            for (int i = 0; i < cut_order_matrix.length; i++) {
              //      System.out.print(cut_order_matrix[i]+" ");
            }
            if (!error) {
                cut_order_plans.add(individuals);
            }

            runningCount++;
        }
        int k = 1;

        for (List<Individual> iList : cut_order_plans) {

            System.out.println("Solution " + k++);
            for (Individual i : iList) {
                System.out.println(i.toString());
            }
            System.out.println("");
        }
        return cut_order_plans;
    }
    @PostMapping("/generatecutorderplan/allsolutions")
    public static String generateCutOrderPlanAllSolutions(@RequestBody RequestDTO requestDTO) {
        List<List<Individual>> cut_order_plans = generateAlgorithm(requestDTO);
        String output="";
        int k =1;
        for (List<Individual> iList : cut_order_plans) {

            output+=("Solution " + k++ +"\n");
            for (Individual i : iList) {
                output+=(i.toString() +"\n");
            }
            output+=("\n");
        }
        return output;
    }
    @PostMapping("/generatecutorderplan/bestsolutions")
    public static String generateCutOrderPlanBestSolutions(@RequestBody RequestDTO requestDTO) {
        List<List<Individual>> cut_order_plans = generateAlgorithm(requestDTO);
        int minIndividuals = Integer.MAX_VALUE;
        for (List<Individual> individualList : cut_order_plans) {
            int numIndividuals = individualList.size();
            if (numIndividuals < minIndividuals) {
                minIndividuals = numIndividuals;
            }
        }

// Collect all lists with the minimum number of individuals
        List<List<Individual>> leastIndividualsLists = new ArrayList<>();
        for (List<Individual> individualList : cut_order_plans) {
            if (individualList.size() == minIndividuals) {
                leastIndividualsLists.add(individualList);
            }
        }
        String output="";
        int k =1;
        for (List<Individual> iList : leastIndividualsLists) {

            output+=("Solution " + k++ +"\n");
            for (Individual i : iList) {
                output+=(i.toString() +"\n");
            }
            output+=("\n");
        }
        return output;
    }


    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}
