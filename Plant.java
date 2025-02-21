import javafx.scene.paint.Color;
/**
 * This is a plant
 */
public class Plant {
    private boolean alive;
    private Field field;
    private Color color;
    
    public Plant (boolean randomAge, Field field) {
        alive = true;
        this.field = field;
        this.color = Color.GREEN;
    }
    
    
    public Color getColour() {
        return color;
    }
}
