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

public class Rat extends Animal {

    private static final int BREEDING_AGE = 2;
    private static final int MAX_AGE = 20;
    private static final double BREEDING_PROBABILITY = 0.08;
    private static final int MAX_LITTER_SIZE = 4;
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
    public Rat(boolean randomAge, Field field, Location location, Color col) {
        super(field, location, col, randomAge);
        age = 0;
        
        if (randomAge) {
            age = rand.nextInt(MAX_AGE);
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
        return new Rat(false, field, loc, getColor());
    }
    
    /**
     * This is what the rabbit does most of the time - it runs 
     * around. Sometimes it will breed or die of old age.
     * @param newRabbits A list to return newly born rabbits.
     */
    public void act(List<Animal> newRats) {
        incrementAge();
        if(isAlive()) {
            giveBirth(newRats);            
            // Try to move into a free location.
            Location newLocation = getField().getFreeAdjacentLocation(getLocation());
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
