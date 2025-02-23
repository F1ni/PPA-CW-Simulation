import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color; 

/**
 * A simple model of a rabbit.
 * Rabbits age, move, breed, and die.
 * 
 * @author David J. Barnes, Michael Kölling and Jeffery Raphael
 * @version 2025.02.10
 */

public class Beaver extends Prey {

    private static final int BREEDING_AGE = 10;
    private static final int MAX_AGE = 60;
    private static final double BREEDING_PROBABILITY = 0.15;
    private static final int MAX_LITTER_SIZE = 4;
    private static final int PLANT_FOOD_VALUE = 3;
    private static final Random rand = Randomizer.getRandom();
    
    private int age;

    /**
     * Create a new rabbit. A rabbit may be created with age
     * zero (a new born) or with a random age.
     * 
     * @param randomAge If true, the rabbit will have a random age.
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    public Beaver(boolean randomAge, Field field, Location location, Color col, boolean diseased) {
        super(field, location, col, randomAge, diseased);
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
        return new Beaver(false, field, loc, getColor(), false);
    }
    
    @Override
    protected int getPlantValue() {
        return PLANT_FOOD_VALUE;
    }
    
    public void act(List<Animal> newBeavers) {
        if (getDiseased()) {
            spread();
        }
        incrementAge();
        incrementHunger();
        if(isAlive()) {
            giveBirth(newBeavers);
            Location newLocation = findFood();
            if (newLocation == null) {
            // Try to move into a free location.
                newLocation = getField().getFreeAdjacentLocation(getLocation());
            }
            if(newLocation != null) {
                setLocation(newLocation);
            }
            else {
                // Overcrowding.
                setDead();
            }
        }
    }
}
