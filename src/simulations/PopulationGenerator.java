package simulations;

import java.awt.Color;
import java.util.List;
import java.util.Random;


/**
 * 
 * Handles the simulator view class and returning its instance
 * and handles populating for the simulator class
 * 
 * @author jackrutherford
 * @date 1/28/22
 * @class CSCI 235
 */
public class PopulationGenerator {

	// Constants representing configuration information for the population generator.

	// The probability that a fox will be created in any given grid position.
	private static final double FOX_CREATION_PROBABILITY = 0.02;
	// The probability that a rabbit will be created in any given position.
	private static final double RABBIT_CREATION_PROBABILITY = 0.08;    

	// Field for th
	private SimulatorView view;

	public PopulationGenerator() {
	}

	/**
	 * Method for accessing the simulator view variable
	 * @return Simulator View
	 */
	public SimulatorView getSimulatorView() {
		return view;
	}

	/**
	 * Randomly populate the field with foxes and rabbits.
	 */
	public void populate(Field field, List<Animal> animals)
	{
		Random rand = Randomizer.getRandom();
		field.clear();
		for(int row = 0; row < field.getDepth(); row++) {
			for(int col = 0; col < field.getWidth(); col++) {
				if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
					Location location = new Location(row, col);
					Animal fox = new Fox(true, field, location);
					animals.add(fox);
				}
				else if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
					Location location = new Location(row, col);
					Animal rabbit = new Rabbit(true, field, location);
					animals.add(rabbit);
				}
				// else leave the location empty.
			}
		}
	}

	public void setColors(int depth, int width) {
		// Create a view of the state of each location in the field.
		view = new SimulatorView(depth, width);
		view.setColor(Rabbit.class, Color.ORANGE);
		view.setColor(Fox.class, Color.BLUE);
	}


}
