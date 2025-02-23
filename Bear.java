import java.util.List;
import java.util.Iterator;
import java.util.Random;
import java.util.LinkedList;
import javafx.scene.paint.Color; 

public class Bear extends Animal {
    
    private static final int BREEDING_AGE = 20;
    private static final int MAX_AGE = 80;
    private static final double BREEDING_PROBABILITY = 0.16;
    private static final int MAX_LITTER_SIZE = 2;
    private static final int RABBIT_FOOD_VALUE = 9;
    private static final int FOX_FOOD_VALUE = 12;
    private static final Random rand = Randomizer.getRandom();
    
    public Bear(boolean randomAge, Field field, Location location, Color col, boolean diseased) {
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
        return new Bear(false, field, loc, getColor(), false);
    }
    
    public void act(List<Animal> newBears) {
        if (getDiseased()) {
            spread();
        }
        incrementAge();
        incrementHunger();
        if (isAlive()) {
            if (deadCounter != 0) {
                giveBirth(newBears);
                Location newLocation = findFood();
                if (newLocation == null) {
                    newLocation = getField().getFreeAdjacentLocation(getLocation());
                }
                
                if (newLocation != null) {
                    setLocation(newLocation);
                }
                else {
                    setDead();
                }
            }
            else {
                setDead();
            }
        }
    }
    
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
            else if (animal instanceof Beaver) {
                Beaver beaver = (Beaver) animal;
                if (beaver.isAlive()) {
                    beaver.setDead();
                    foodLevel = FOX_FOOD_VALUE;
                    return where;
                }
            }
        }
        return null;
    }
}
