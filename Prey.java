import java.util.List;
import java.util.Iterator;
import javafx.scene.paint.Color;

public abstract class Prey extends Animal {
    
    protected abstract int getPlantValue();
    
    public Prey(Field field, Location location, Color col, boolean randomAge) {
        super(field, location, col, randomAge);
        if (randomAge) {
            age = rand.nextInt(getMaxAge());
            foodLevel = rand.nextInt(getPlantValue());
        }
        else {
            age = 0;
            foodLevel = getPlantValue();
        }
    }
    public Location findFood() {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while(it.hasNext()) {
            Location where = it.next();
            Object livingThing = field.getObjectAt(where);
            if (livingThing instanceof Plant) {
                field.clear(where);
                foodLevel = getPlantValue();
                return where;
            }
        }
        return null;
    }
}
