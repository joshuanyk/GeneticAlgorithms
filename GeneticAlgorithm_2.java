/* 
 * Author: Joshua Ng Yit Kit
 * Student ID: 14044641
 * 
 * This is for dataset2.txt
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class GeneticAlgorithm_2 {
		
		//Declaration of generation & mutation rate
		static int generation = 50;
		static int mutation = 10;
		
	public static void main(String [] args) throws IOException{
		
		//total population and length of gene
		int pop = 50, value = 448;
		int dataPop = 64, dataValue = 6;
		
		//creating population array
		Individuals population[] = new Individuals[pop];
		Individuals fittest = null;
		Data data[] = new Data[dataPop];
		
		//creating genes
		for(int i = 0; i < pop; i++){
			population[i] = new Individuals(value); 
		}
		//creating genes for DATA
		for(int i = 0; i< dataPop;i++){
			data[i] = new Data(dataValue);
		}
		//read from file
		readFile(data,dataPop,dataValue);	
		
		//creating Individuals
		for(int i = 0; i < pop; i++){
			for(int j =0; j<value; j++){
				population[i].gene[j] = (Math.random() < 0.5 ? 0:1); 
			}
			population[i].fitness = 0;
		}
		
		//createFitness on initial population
		createFitness(population,data,dataPop,dataValue);
		System.out.println("");
		System.out.println("Generation: 1");
		System.out.println("Best Fitness: " + bestFitness(population,pop));
		System.out.println("Mean Fitness: " + meanFitness(population,pop));
		
		//loop thru according to amount of generations
		for(int k = 1; k < generation; k++){
			
			//2nd generation to N amount of generation 
			System.out.println("_________________________________________________");
			System.out.println("Generation: " +(k+1));
			
			//creating new array of offspring 
			Individuals offspring[] = new Individuals[pop];
			
			//Selection from population
			int parent1, parent2;
			Random ran = new Random();			
			for(int i = 0; i < pop; i++){
				
				//selecting random parents
				parent1 = ran.nextInt(pop);
				parent2 = ran.nextInt(pop);
				
				//choose best out of two parents
				if(population[parent1].fitness >= population[parent2].fitness){
					offspring[i] = population[parent1].clone();
				}else{
					offspring[i] = population[parent2].clone();
				}
			}
			
			//Crossover between genes
			for(int i = 0; i <pop; i = i+2){
				int breakPoint = ran.nextInt(value);
				for(int j=breakPoint;j<value;j++){
					int temp = offspring[i].gene[j];
					offspring[i].gene[j] = offspring[i+1].gene[j];
					offspring[i+1].gene[j] = temp;
					
				}
			}
			//createFitness after selection and crossover
			createFitness(offspring,data,dataPop,dataValue);
			System.out.println("Best Fitness after Selection & Crossover: " + bestFitness(offspring,pop));
			System.out.println("Mean Fitness after Selection & Crossover: " + meanFitness(offspring,pop));
			
			//Mutation
			for(int i = 0; i <pop;i++){
				for(int j = 0;j <value; j++){
					//uses randoming function to flip the bit
					if(Randoming()){
						if(offspring[i].gene[j] == 1){
							offspring[i].gene[j] = 0;
						}else if(offspring[i].gene[j] == 2){
							if(Randoming()){
								offspring[i].gene[j] = 0;
							}else{
								offspring[i].gene[j] = 1;
							}
						}else{
							offspring[i].gene[j] = 1;
						}
					}
				}
			}
			//createFitness after mutation
			createFitness(offspring,data,dataPop,dataValue);
			
			System.out.println("Best Fitness after Mutation: " + bestFitness(offspring,pop));
			System.out.println("Mean Fitness after Mutation: " + meanFitness(offspring,pop));
						
			//Get the fittest Individual
			fittest = getFittestIndividual(population,pop);
						
			//replace worst individual in offspring with best in population
			replaceWorstWithFittest(offspring,pop,fittest);
			
			//replaces the whole population with offspring
			population = offspring;			
		}
				
	}
	
	//function to randomize and return true of false
	public static boolean Randoming()
	{
		Random ran = new Random();
		int chance = ran.nextInt(100);
		
		if (chance <= mutation)return true;
	  
		else return false;
	}
	
	//function to print current fitness of all population
	public static void printAllFitness(Individuals population[],int pop){
		for(int i = 0; i<pop; i++){
			System.out.print(population[i].fitness+" ");
		}
		System.out.println("");
	}
	
	//function to read from file
	public static void readFile(Data data[],int dataPop,int value) throws IOException{
		
		BufferedReader BR = new BufferedReader(new FileReader("data2.txt"));
		String temp[] = new String[dataPop];
				
		for(int i = 0; i<dataPop; i++){
			//Read from File
			String temp1 = BR.readLine();
			temp[i] = temp1;
			
			//splitting condition and action 
			String[] parts = temp[i].split(" ");
			String part1 = parts[0];
			String part2 = parts[1];
			
			//splitting condition into parts
			String[] conditionPart = part1.split("");
			for(int j =0; j<conditionPart.length; j++){
				data[i].condition[j] = Integer.parseInt(conditionPart[j]);
			}
		
			//Storing action into Data
			data[i].action = Integer.parseInt(part2);
			}
	}
	
	//print all bits in a population
	public static void PrintBits(Individuals population[],int pop,int value){
		System.out.println("_Bits_");
		for(int i = 0;i<pop;i++){
			for(int j=0;j<value;j++){
				System.out.print(population[i].gene[j]);
			}
			System.out.println("");
		}
		System.out.println("______");
	}

	//function to replace Individual offspring with the fittest Individual in population
	public static void replaceWorstWithFittest(Individuals offspring[],int pop, Individuals fittest){
		int worstFitness = getWorstFitness(offspring,pop);
		for(int i = 0; i<pop; i++){
			if(offspring[i].fitness == worstFitness){
				offspring[i] = fittest;
				break;
			}
		}
	}
	
	//find the individual with worst fitness
	public static int getWorstFitness(Individuals offspring[], int pop){
		int lowest = Integer.MAX_VALUE;
		for(int i=0;i<pop;i++){
			if(offspring[i].fitness < lowest){
				lowest = offspring[i].fitness;
			}
		}
		return lowest;
	}
	
	//creating a fitness for each individual
	public static void createFitness(Individuals population[],Data data[],int dataPop,int dataValue){
		
		int ruleLength = dataPop;

        for (Individuals individual : population) {
        	individual.fitness =0;
            Rule[] rules = new Rule[ruleLength];

            for (int i = 0; i < ruleLength; i++) {
            	int lower = i*(dataValue+1);
            	int upper = lower+(dataValue);            	
                rules[i] = new Rule(dataValue);
                rules[i].condition = Arrays.copyOfRange(individual.gene, lower, upper);
                rules[i].action = individual.gene[upper];
            }
            for (Data d : data) {
                for (Rule r : rules) {
                    if (Arrays.equals(d.condition, r.condition)){
                    	if(d.action == r.action){
                    	individual.fitness++;
                    	}else break;
                    }
                }
            }
        }
        }
    
	//getting the fittest individual
	public static Individuals getFittestIndividual(Individuals population[],int pop){
		Individuals fittest = null;
		int largest = Integer.MIN_VALUE;
		for(int i = 0; i<pop; i++){
			if(population[i].fitness > largest){
				largest = population[i].fitness;
				fittest = population[i];
			}
		}
		return fittest;
	}

	//returns the best fitness
	public static int bestFitness(Individuals population[],int pop){
		int biggest = Integer.MIN_VALUE;
		for(int i = 0; i<pop; i++){
			if(population[i].fitness >= biggest){
				biggest = population[i].fitness;
			}
		}
		return biggest;
	}
	
	//getting the average fitness in a population
	public static double meanFitness(Individuals population[],int pop){
		double total = totalFitness(population,pop);
		double meanFitness = total/pop;
		return meanFitness;
	}
	
	//getting total fitness in a population
	public static int totalFitness(Individuals population[], int pop){
		int total = 0;
		for(int i = 0; i<pop; i++){
			total = total + population[i].fitness; 
		}
		return total;
	}
	
}
