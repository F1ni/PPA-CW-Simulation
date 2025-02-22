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
    
    public Plant(Field field, Location location, Color col) {
        this.field = field;
        setLocation(location);
        setColour(col);
    }
    
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
    
    public Color getColour() {
        return color;
    }
    
    public void setColour(Color col) {
        color = col;
    }
}
