package com.example.cutorderplanservice;

import java.util.ArrayList;
import java.util.Arrays;

public class GeneticAlgorithm {
    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    private int elitismCount;

    private int generation=1;
    public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
    }

    public Population initPopulation(int chromosomeLength) {
        Population population = new Population(this.populationSize, chromosomeLength);
        return population;
    }

    public double calcFitness(Individual individual) {

        int fitness = 0;
        int b[] = new int[individual.getChromsomeLength()];
        for (int geneIndex = 0; geneIndex < individual.getChromsomeLength(); geneIndex++) {
            if (individual.getGene(geneIndex) != 0)
                b[geneIndex] = (int) Main.cut_order_matrix[geneIndex] / individual.getGene(geneIndex);
            else b[geneIndex] = Integer.MAX_VALUE;
        }


        //individual.setB(Arrays.stream(b).min().getAsInt());

        if (Arrays.stream(b).min().getAsInt() <= Main.hmax&&Arrays.stream(b).min().getAsInt() >= Main.hmin)
            individual.setB(Arrays.stream(b).min().getAsInt());
        else if (Arrays.stream(b).min().getAsInt() <= Main.hmin)
            individual.setB(Main.hmin);
        else
            individual.setB(Main.hmax);

        for (int geneIndex = 0; geneIndex < individual.getChromsomeLength(); geneIndex++) {
            fitness += individual.getB() * individual.getGene(geneIndex);
        }

        individual.setFitness(fitness);
  //      System.out.println(individual.toString() + " " + bmin + " " + fitness);
        return fitness;
    }

    public void evalPopulation(Population population) {
        double populationFitness = 0;
        for (Individual individual : population.getIndividuals()) {
            populationFitness += calcFitness(individual);
        }

        population.setPopulationFitness(populationFitness);
    }

    public boolean isTerminationConditionMet(Population population) {
        System.out.println("This Generation "+this.generation);
        if(this.generation>100)
            return true;

        /*
        for (Individual individual : population.getIndividuals()) {
            if (individual.getFitness() == 400) {
                return true;
            }
        }*/

        return false;
    }

    //Roulette wheel
    public int selectParent(Population population) {
        //Get individuals
        Individual individuals[] = population.getIndividuals();
        double populationFitness = population.getPopulationFitness();

        double rouletteWheelPosition = Math.random() * populationFitness;
        double spinWheel = 0;
        for (int i=0;i<individuals.length;i++) {
            spinWheel += individuals[i].getFitness();//cumulative fitness
            if (spinWheel >= rouletteWheelPosition) {
                return i;
            }
        }
        return individuals.length-1;
    }

    public Individual[] uniformCrossover(Individual parent1,Individual parent2) {
        Individual offspring1 = new Individual(parent1.getChromsomeLength());
        Individual offspring2 = new Individual(parent2.getChromsomeLength());

        for (int geneIndex = 0; geneIndex < parent1.getChromsomeLength(); geneIndex++) {
            if (Math.random() < 0.5) {
                if (Arrays.stream(offspring2.getChromosome()).sum() - parent2.getGene(geneIndex) + parent1.getGene(geneIndex) <= Main.gmax) {
                    offspring2.setGene(geneIndex, parent1.getGene(geneIndex));
                }
            } else {
                if (Arrays.stream(offspring1.getChromosome()).sum() - parent1.getGene(geneIndex) + parent2.getGene(geneIndex) <= Main.gmax) {
                    offspring1.setGene(geneIndex, parent2.getGene(geneIndex));
                }
            }
        }

        return new Individual[]{offspring1, offspring2};
    }
    public Population crossoverPopulation(Population population) {
        //Population newPopulation = new Population(population.size());
        ArrayList<Integer> parentIndex = new ArrayList<>();
        for(int i =0;i<4;i++)
            if (this.crossoverRate > Math.random()){
            parentIndex.add(selectParent(population));

        }
        int size = parentIndex.size();
        Individual offsprings[] = new Individual[2];
     //   System.out.println(parentIndex.toString());
        if(size==2)
        {
            offsprings=uniformCrossover(population.getIndividuals()[parentIndex.get(0)],population.getIndividuals()[parentIndex.get(1)]);

            if(offsprings[0].getFitness()>population.getIndividuals()[parentIndex.get(0)].getFitness()){
                population.setIndividual(parentIndex.get(0),offsprings[0]);
            }
            if(offsprings[1].getFitness()>population.getIndividuals()[parentIndex.get(1)].getFitness()){
                population.setIndividual(parentIndex.get(1),offsprings[1]);
            }
        }
            else if (size==3)
        {
            offsprings=uniformCrossover(population.getIndividuals()[parentIndex.get(0)],population.getIndividuals()[parentIndex.get(1)]);
            if(offsprings[0].getFitness()>population.getIndividuals()[parentIndex.get(0)].getFitness()){
                population.setIndividual(parentIndex.get(0),offsprings[0]);
            }
            if(offsprings[1].getFitness()>population.getIndividuals()[parentIndex.get(1)].getFitness()){
                population.setIndividual(parentIndex.get(1),offsprings[1]);
            }
        }
            else  if (size==4){
            Individual offsprings2[] = new Individual[2];
            offsprings=uniformCrossover(population.getIndividuals()[parentIndex.get(0)],population.getIndividuals()[parentIndex.get(1)]);
            if(offsprings[0].getFitness()>population.getIndividuals()[parentIndex.get(0)].getFitness()){
                population.setIndividual(parentIndex.get(0),offsprings[0]);
            }
            if(offsprings[1].getFitness()>population.getIndividuals()[parentIndex.get(1)].getFitness()){
                population.setIndividual(parentIndex.get(1),offsprings[1]);
            }
            offsprings2=uniformCrossover(population.getIndividuals()[parentIndex.get(2)],population.getIndividuals()[parentIndex.get(3)]);
            if(offsprings2[0].getFitness()>population.getIndividuals()[parentIndex.get(2)].getFitness()){
                population.setIndividual(parentIndex.get(2),offsprings2[0]);
            }
            if(offsprings2[1].getFitness()>population.getIndividuals()[parentIndex.get(3)].getFitness()){
                population.setIndividual(parentIndex.get(3),offsprings2[1]);
            }
        }



return  population;
/*

        Population newPopulation = new Population(population.size());
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {

            Individual parent1 = population.getFittest(populationIndex);
            Individual offspring;
            if (this.crossoverRate > Math.random() && populationIndex > this.elitismCount) {
                //Individual offspring = new Individual(parent1.getChromsomeLength());
                //System.out.println("Offspring before crossover"+offspring);
                Individual parent2 = population.getIndividuals()[selectParent(population)];
                offspring = uniformCrossover(parent1,parent2);


                newPopulation.setIndividual(populationIndex, offspring);
             //   System.out.println("Offspring After crossover "+offspring);
            } else {
                newPopulation.setIndividual(populationIndex, parent1);
            }

        }
return newPopulation;

*/
    }

    public Population mutatePopulation(Population population) {
        int mutationPos = 0;
        int mutationChromsome=0;
        int chromsomeLength = population.getFittest(0).getChromsomeLength();
        int genes = chromsomeLength*this.populationSize;

        for(int i=0;i<genes;i++){
            if (this.mutationRate > Math.random()){
                mutationPos=genes%this.populationSize;
                mutationChromsome= (int)(genes/populationSize)+1;
                break;
            }
        }

        int gmax=Main.gmax;
        Individual  individual = population.getIndividuals()[mutationChromsome];
        int rand;
        for(int geneIndex=mutationPos;geneIndex<chromsomeLength;geneIndex++){
            if (Main.cut_order_matrix[geneIndex] == 0) individual.setGene(geneIndex,0);
            else {
                if (gmax>=Main.gmin){


                if (Main.minQuantity > gmax){
                    rand = (int) Math.round(Main.gmin + (gmax - Main.gmin) * Math.random());
                    if(Arrays.stream(individual.getChromosome()).sum()<=Main.gmax)
                    individual.setGene(geneIndex,rand);
                }

                else{
                    rand = (int) Math.round(Main.gmin + (Main.minQuantity - Main.gmin) * Math.random());
                    if(Arrays.stream(individual.getChromosome()).sum()<=Main.gmax)
                    individual.setGene(geneIndex,rand);
                }

            }

            }
            gmax = gmax - individual.getGene(geneIndex);


        }



        for(int geneIndex=0;geneIndex>mutationPos;geneIndex++){
            if (Main.cut_order_matrix[geneIndex] == 0) individual.setGene(geneIndex,0);
            else {
                if (gmax >= Main.gmin) {
                    if (Main.minQuantity > gmax){
                        rand = (int) Math.round(Main.gmin + (gmax - Main.gmin) * Math.random());
                        if(Arrays.stream(individual.getChromosome()).sum()<=Main.gmax)
                        individual.setGene(geneIndex,rand);
                    }

                    else{
                        rand = (int) Math.round(Main.gmin + (Main.minQuantity - Main.gmin) * Math.random());
                        if(Arrays.stream(individual.getChromosome()).sum()<=Main.gmax)
                            individual.setGene(geneIndex, rand);
                    }

                }
            }
            gmax = gmax - individual.getGene(geneIndex);

        }

        population.setIndividual(mutationChromsome,individual);
        return population;


/*
        for (int populationIndex = 0; populationIndex < population.size(); populationIndex++) {
            Individual individual = population.getFittest(populationIndex);
            int gmax = Main.gmax;

            for (int geneIndex = 0; geneIndex < individual.getChromsomeLength(); geneIndex++) {
                if (populationIndex >= this.elitismCount) {
                    if (this.mutationRate > Math.random())
                        if (Main.cut_order_matrix[geneIndex] == 0) individual.setGene(geneIndex, 0);
                        else {
                            if (Main.minQuantity > gmax)
                                individual.setGene(geneIndex,(int) Math.round(Main.gmin + (gmax - Main.gmin) * Math.random()));
                            else
                                individual.setGene(geneIndex,(int) Math.round(Main.gmin + (Main.minQuantity - Main.gmin) * Math.random()));
                        }
                }

                System.out.println(geneIndex +" gmaxBefore: "+gmax);
                gmax = gmax - individual.getGene(geneIndex);
                System.out.println(individual);
                System.out.println("gmaxAfter: "+gmax);
            }
            newPopulation.setIndividual(populationIndex, individual);
            System.out.println("\n\n");
        }

 */
    }


    public int getGeneration() {
        return this.generation;
    }

    public void generationIncrement() {
        this.generation = this.generation+1;
    }
}
