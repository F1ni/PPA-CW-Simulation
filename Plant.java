import javafx.scene.paint.Color;
/**
 * Write a description of class Plant here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Plant {
    
    private Field field;
    private Location location;
    private Color color;
    private boolean alive;
    
    public Plant(Field field, Location location, Color col) {
        alive = true;
        this.field = field;
        setLocation(location);
        setColour(col);
    }
    
    public void setLocation(Location newLocation) {
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
    
    public boolean isAlive() {
        return alive;
    }
    
    protected void setDead() {
        alive = false;
        if (location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }
    
    public Color getColour() {
        return color;
    }
    
    public void setColour(Color col) {
        color = col;
    }
}
