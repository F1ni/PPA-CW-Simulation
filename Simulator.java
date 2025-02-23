import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import javafx.scene.paint.Color; 

/**
 * A simple predator-prey simulator, based on a rectangular field
 * containing rabbits and foxes.
 * 
 * @author David J. Barnes, Michael Kölling and Jeffery Raphael
 * @version 2025.02.10
 */

public class Simulator {

    private static final double FOX_CREATION_PROBABILITY = 0.02;
    private static final double BEAR_CREATION_PROBABILITY = 0.01;
    private static final double RABBIT_CREATION_PROBABILITY = 0.08;
    private static final double BEAVER_CREATION_PROBABILITY = 0.05;
    private static final double RAT_CREATION_PROBABILITY = 0.06;
    private static final double DISEASE_PROBABILITY = 0.50;
   

    private List<Animal> animals;
    private Field field;
    private int step;
    
    /**
     * Create a simulation field with the given size.
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        
        animals = new ArrayList<>();
        field = new Field(depth, width);

        reset();
    }
    
    /**
     * Run the simulation from its current state for a single step.
     * Iterate over the whole field updating the state of each
     * fox and rabbit.
     */
    public void simulateOneStep() {
        step++;
        List<Animal> newAnimals = new ArrayList<>();        

        for(Iterator<Animal> it = animals.iterator(); it.hasNext(); ) {
            Animal animal = it.next();
            animal.act(newAnimals);
            if(!animal.isAlive()) {
                it.remove();
            }
        }
               
        animals.addAll(newAnimals);
    }
        
    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        animals.clear();
        populate();
    }
    
    /**
     * Randomly populate the field with foxes and rabbits.
     */
    private void populate() {
        Random rand = Randomizer.getRandom();
        field.clear();
        for(int row = 0; row < field.getDepth(); row++) {
            for(int col = 0; col < field.getWidth(); col++) {
                if(rand.nextDouble() <= FOX_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Color color = Color.RED;
                    if (isDiseased()) {
                        color = color.darker();
                    }
                    Fox fox = new Fox(true, field, location, color, isDiseased());
                    animals.add(fox);
                }
                
                else if (rand.nextDouble() <= BEAR_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Color color = Color.BROWN;
                    if (isDiseased()) {
                        color = color.darker();
                    }
                    Bear bear = new Bear(true, field, location, color, isDiseased());
                    animals.add(bear);
                }
                if(rand.nextDouble() <= RABBIT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Color color = Color.YELLOW;
                    if (isDiseased()) {
                        color = color.darker();
                    }
                    Rabbit rabbit = new Rabbit(true, field, location, color, isDiseased());
                    animals.add(rabbit);
                }
                else if (rand.nextDouble() <= BEAVER_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Color color = Color.ORANGE;
                    if (isDiseased()) {
                        color = color.darker();
                    }
                    Beaver beaver = new Beaver(true, field, location, color, isDiseased());
                    animals.add(beaver);
                }
                else if (rand.nextDouble() <= RAT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Color color = Color.LIGHTBLUE;
                    if (isDiseased()) {
                        color = color.darker();
                    }
                    Rat rat = new Rat(true, field, location, color, isDiseased());
                    animals.add(rat);
                }
                // else leave the location empty.
                else {
                    Location location = new Location(row, col);
                    Plant plant = new Plant(field, location, Color.LIGHTGREEN);
                }
            }
        }
    }
    
    /**
     * Pause for a given time.
     * @param millisec  The time to pause for, in milliseconds
     */
    public void delay(int millisec) {
        try {
            Thread.sleep(millisec);
        }
        catch (InterruptedException ie) {
            // wake up
        }
    }
    

    public Field getField() {
        return field;
    }

    public int getStep() {
        return step;
    }
    
    public boolean isDiseased() {
        boolean diseased = false;
        Random rand = Randomizer.getRandom();
        if (rand.nextDouble() <= DISEASE_PROBABILITY) {
            diseased = true;
        }
        return diseased;
    }
}