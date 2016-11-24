package Assignment_2;

public class Individuals{

	int gene[];
	int fitness = 0;
	
	public Individuals(int value){
		this.gene = new int[value];
	}
	
	public Individuals clone(){
		Individuals clone = new Individuals(gene.length);
		clone.gene = gene.clone();
		clone.fitness = 0;
		return clone;
	}
}
