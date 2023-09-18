package simulations;

import java.util.List;
import java.util.Random;

/**
 * 
 * A Super class that includes general methods about animals
 * Includes how they move and breed, and handles age, location,
 * and other key information.
 * 
 * @author jackrutherford
 * @date 1/28/22
 * @class CSCI 235
 *
 */
public abstract class Animal {

	// Whether the animal is alive or not.
	private boolean alive;
	// The animal's position.
	private Location location;
	// The field occupied.
	private Field field;
	// The animal's age.
	private int age;

	public Animal(Location location, Field field, int age) {
		this.alive = true;
		this.location = location;
		this.field = field;
		this.age = age;
	}

	/**
	 * Lets the animal act, abstract so that each
	 * animal can act in a different way if it wants
	 */
	public abstract void act(List<Animal> newAnimals);

	//protected abstract void giveBirth(List<Animal> animal);

	/**
	 * Check whether the rabbit is alive or not.
	 * @return true if the rabbit is still alive.
	 */
	public boolean isAlive()
	{
		return alive;
	}

	/**
	 * Indicate that the rabbit is no longer alive.
	 * It is removed from the field.
	 */
	protected void setDead()
	{
		alive = false;
		if(location != null) {
			field.clear(location);
			location = null;
			field = null;
		}
	}

	/**
	 * Return the rabbit's location.
	 * @return The rabbit's location.
	 */
	public Location getLocation()
	{
		return location;
	}

	/**
	 * Place the rabbit at the new location in the given field.
	 * @param newLocation The rabbit's new location.
	 */
	protected void setLocation(Location newLocation)
	{
		if(location != null) {
			field.clear(location);
		}
		location = newLocation;
		field.place(this, newLocation);
	}

	public Field getField() {
		return field;
	}

	/**
	 * An animal can breed if it has reached the breeding age.
	 */
	protected boolean canBreed()
	{
		return age >= getBreedingAge();
	}

	abstract protected int getBreedingAge();

	abstract protected int getMaxBreedingAge();

	abstract protected int getMaxLitterSize();

	abstract protected double getBreedingProbability();

	abstract protected Animal createAnimal(boolean randomAge, Field field, Location loc);

	protected int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * Increase the age.
	 * This could result in the rabbit's death.
	 */
	protected void incrementAge()
	{
		age++;
		if(age > getMaxBreedingAge()) {
			setDead();
		}
	}

	/**
	 * Generate a number representing the number of births,
	 * if it can breed.
	 * @return The number of births (may be zero).
	 */
	protected int breed()
	{
		Random rand = Randomizer.getRandom();
		int births = 0;
		if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
			births = rand.nextInt(getMaxLitterSize()) + 1;
		}
		return births;
	}

	protected void giveBirth(List<Animal> newAnimals) {  
		List<Location> free = field.getFreeAdjacentLocations(getLocation()); 
		int births = breed(); 
		for (int b = 0; b < births && free.size() > 0; b++) { 
			Location loc = free.remove(0); 
			Animal young = createAnimal(false, field, loc); 
			newAnimals.add(young);
		}
	}

}
