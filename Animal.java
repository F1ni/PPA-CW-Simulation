import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;
import java.util.Iterator;

/**
 * A class representing shared characteristics of animals.
 * 
 * @author David J. Barnes, Michael Kölling and Jeffery Raphael
 * @version 2025.02.10
 */

public abstract class Animal{
    
    private boolean alive;
    private Field field;
    private Location location;
    private Color color;
    protected int age;
    protected boolean diseased;
    protected int deadCounter;
    protected int foodLevel;
    protected Random rand = Randomizer.getRandom();
    
    
    protected abstract int getBreedingAge();
    protected abstract int getMaxAge();
    protected abstract double getBreedingProbability();
    protected abstract int getMaxLitterSize();
    protected abstract Animal createYoung(Field field, Location loc);
    
    /**
     * Create a new animal at location in field.
     * 
     * @param field The field currently occupied.
     * @param location The location within the field.
     */
    
    public Animal(Field field, Location location, Color col, boolean randomAge, boolean diseased) {
        alive = true;
        this.diseased = diseased;
        //Once sick, the animal should remain alive for a xed number of steps 
        if (diseased) {
            deadCounter = 3;
        }
        this.field = field;
        setLocation(location);
        setColor(col);
    }
    
    /**
     * Make this animal act - that is: make it do
     * whatever it wants/needs to do.
     * @param newAnimals A list to receive newly born animals.
     */
    abstract public void act(List<Animal> newAnimals);

    /**
     * Check whether the animal is alive or not.
     * @return true if the animal is still alive.
     */
    protected boolean isAlive() {
        return alive;
    }

    /**
     * Indicate that the animal is no longer alive.
     * It is removed from the field.
     */
    protected void setDead() {
        alive = false;
        if(location != null && field != null) {
            Object objectAtLocation = field.getObjectAt(location);
            if (objectAtLocation == this) {
                field.clear(location);
                // place a plant at the location where the animal died
                Plant plant = new Plant(field, location, Color.LIGHTGREEN);
            }
            location = null;
            field = null;
        }
    }

    /**
     * Return the animal's location.
     * @return The animal's location.
     */
    protected Location getLocation() {
        return location;
    }
    
    /**
     * Place the animal at the new location in the given field.
     * @param newLocation The animal's new location.
     */
    protected void setLocation(Location newLocation) {
        if(location != null && field != null) {
            Object objectAtLocation = field.getObjectAt(location);
            if (objectAtLocation == this) {
                field.clear(location);
            }
        }
        location = newLocation;
        if (field != null && newLocation != null) {
            field.place(this, newLocation);
        }
    }
    
    /**
     * Return the animal's field.
     * @return The animal's field.
     */
    protected Field getField() {
        return field;
    }
    
    /**
     * Changes the color of the animal
     */
    public void setColor(Color col) {
        color = col;
    }

    /**
     * Returns the animal's color
     */
    public Color getColor() {
        return color;
    } 
    
    public void setDiseased(boolean diseased) {
        this.diseased = diseased;
    }
    
    /**
     * Increase the age. This could result in the animal's death.
     */
    public void incrementAge() {
        age++;
        if(age > getMaxAge()) {
            setDead();
        }
    }
    
    /**
     * Make this animal more hungry. This could result in the animal's death.
     */
    public void incrementHunger() {
        foodLevel--;
        if(foodLevel <= 0) {
            setDead();
        }
        if (diseased) {
            deadCounter--;
        }
    }
    
    public int breed() {
        int births = 0;
        if(canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }
    
    public boolean canBreed() {
        return age >= getBreedingAge();
    }
    
    public void giveBirth(List<Animal> newAnimals) {
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(location);
        int births = breed();
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Animal young = createYoung(field, loc);
            newAnimals.add(young);
        }
    }
    
    public boolean getDiseased() {
        return diseased;
    }
    
    //Disease can spread to other animals of the same species
    //I dont think this works correctly
    public void spread() {
        Field field = getField();
        if (field == null) {
            return;
        }
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object animal = field.getObjectAt(where);
            if(animal != null && animal.getClass() == getClass()) {
                Animal sameAnimal = (Animal) animal;
                if (rand.nextDouble() < 0.50) {
                    sameAnimal.setDiseased(true);
                }
            }
        }
    }
}