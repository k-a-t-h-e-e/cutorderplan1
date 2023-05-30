package com.example.cutorderplanservice;

public class Individual {
    private int[] chromosome;
    private int b;
    private double fitness = -1;

    public Individual(int[] chromosome) {
        this.chromosome = chromosome;
    }

    public Individual(int chromosomeLength) {
        this.chromosome = new int[chromosomeLength];
        int gmax = Main.gmax;
        for (int gene = 0; gene < chromosomeLength; gene++) {
            if (Main.cut_order_matrix[gene] == 0)
                this.setGene(gene, 0);
            else if (gmax >= Main.gmin) {
                if (Main.minQuantity > gmax)
                    this.setGene(gene, (int) Math.round(Main.gmin + (gmax - Main.gmin) * Math.random()));
                else
                    this.setGene(gene, (int) Math.round(Main.gmin + (Main.minQuantity - Main.gmin) * Math.random()));
            }
            gmax = gmax - getGene(gene);

        }

    }

    public int[] getChromosome() {
        return this.chromosome;
    }

    public int getChromsomeLength() {
        return this.chromosome.length;
    }

    public void setGene(int offset, int gene) {
        this.chromosome[offset] = gene;
    }

    public int getGene(int offset) {
        return this.chromosome[offset];
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public double getFitness() {
        return this.fitness;
    }

    public String toString() {
        String output = "";
        for (int gene = 0; gene < this.chromosome.length; gene++) {
            output += this.chromosome[gene];
            output += "\t";
        }
        output+= "plies\t"+b;
        return output;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }
}
