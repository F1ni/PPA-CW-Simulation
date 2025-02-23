import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.LinkedList;
import javafx.scene.paint.Color; 

/**
 * A simple model of a fox.
 * Foxes age, move, eat rabbits, and die.
 * 
 * @author David J. Barnes and Michael Kölling
 * @version 2025.02.10
 */

public class Fox extends Animal {

    private static final int BREEDING_AGE = 5;
    private static final int MAX_AGE = 40;
    private static final double BREEDING_PROBABILITY = 0.08;
    private static final int MAX_LITTER_SIZE = 2;
    private static final int RABBIT_FOOD_VALUE = 9;
    private static final int RAT_FOOD_VALUE = 5;
    private static final Random rand = Randomizer.getRandom();
    
    /**
     * Create a fox. A fox can be created as a new born (age zero
     * and not hungry) or with a random age and food level.
     * 
     * @param randomAge If true, the fox will have random age and hunger level.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Fox(boolean randomAge, Field field, Location location, Color col, boolean diseased) {
        super(field, location, col, randomAge, diseased);
        if (randomAge) {
            age = rand.nextInt(MAX_AGE);
            foodLevel = rand.nextInt(RABBIT_FOOD_VALUE);
        }
        else {
            age = 0;
            foodLevel = RABBIT_FOOD_VALUE;
        }
    }
    
    @Override
    protected int getBreedingAge() { 
        return BREEDING_AGE; 
    }
    
    @Override
    protected int getMaxAge() { 
        return MAX_AGE; 
    }
    
    @Override
    protected double getBreedingProbability() { 
        return BREEDING_PROBABILITY; 
    }
    
    @Override
    protected int getMaxLitterSize() { 
        return MAX_LITTER_SIZE; 
    }
    
    @Override
    protected Animal createYoung(Field field, Location loc) {
        return new Fox(false, field, loc, getColor(), false);
    }
    
    /**
     * This is what the fox does most of the time: it hunts for
     * rabbits. In the process, it might breed, die of hunger,
     * or die of old age.
     * @param field The field currently occupied.
     * @param newFoxes A list to return newly born foxes.
     */
    public void act(List<Animal> newFoxes) {
        if (getDiseased()) {
            spread();
        }
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newFoxes);            
            // Move towards a source of food if found.
            Location newLocation = findFood();
            if(newLocation == null) { 
                // No food found - try to move to a free location.
                newLocation = getField().getFreeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
    
    /**
     * Look for rabbits adjacent to the current location.
     * Only the first live rabbit is eaten.
     * @return Where food was found, or null if it wasn't.
     */
    private Location findFood() {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal instanceof Rabbit) {
                Rabbit rabbit = (Rabbit) animal;
                if(rabbit.isAlive()) { 
                    rabbit.setDead();
                    foodLevel = RABBIT_FOOD_VALUE;
                    return where;
                }
            }
            else if (animal instanceof Rat) {
                Rat rat = (Rat) animal;
                if (rat.isAlive()) {
                    rat.setDead();
                    foodLevel = RAT_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
}