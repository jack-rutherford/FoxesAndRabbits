package simulations;

import java.util.List;
import java.util.Random;

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2016.02.29
 */
public class Rabbit extends Animal
{
	// Characteristics shared by all rabbits (class variables).

	// The age at which a rabbit can start to breed.
	private static final int BREEDING_AGE = 5;
	// The age to which a rabbit can live.
	private static final int MAX_AGE = 40;
	// The likelihood of a rabbit breeding.
	private static final double BREEDING_PROBABILITY = 0.12;
	// The maximum number of births.
	private static final int MAX_LITTER_SIZE = 4;
	// A shared random number generator to control breeding.
	private static final Random rand = Randomizer.getRandom();

	// Individual characteristics (instance fields).


	/**
	 * Create a new rabbit. A rabbit may be created with age
	 * zero (a new born) or with a random age.
	 * 
	 * @param randomAge If true, the rabbit will have a random age.
	 * @param field The field currently occupied.
	 * @param location The location within the field.
	 */
	public Rabbit(boolean randomAge, Field field, Location location)
	{
		super(location, field, 0);
		setLocation(location);
		if(randomAge) {
			setAge(rand.nextInt(MAX_AGE));
		}
	}

	/**
	 * This is what the rabbit does most of the time - it runs 
	 * around. Sometimes it will breed or die of old age.
	 * @param newRabbits A list to return newly born rabbits.
	 */
	public void act(List<Animal> newRabbits)
	{
		incrementAge();
		if(isAlive()) {
			giveBirth(newRabbits);            
			// Try to move into a free location.
			Location newLocation = getField().freeAdjacentLocation(getLocation());
			if(newLocation != null) {
				setLocation(newLocation);
			}
			else {
				// Overcrowding.
				setDead();
			}
		}
	}

	@Override
	protected int getBreedingAge() {
		return BREEDING_AGE;
	}

	@Override
	protected int getMaxBreedingAge() {
		return MAX_AGE;
	}

	@Override
	protected int getMaxLitterSize() {
		return MAX_LITTER_SIZE;
	}

	@Override
	protected double getBreedingProbability() {
		return BREEDING_PROBABILITY;
	}

	/**
	 * @Override
	 * Create a new Animal of the Rabbit class
	 */
	protected Animal createAnimal(boolean randomAge, Field field, Location loc) {
		return new Rabbit(randomAge, field, loc);
	}

}
