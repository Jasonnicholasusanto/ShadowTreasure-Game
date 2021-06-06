import bagel.Image;
import bagel.util.Point;

/**
 * The Sandwich class which is a child class to Entity class
 */
public class Sandwich extends Entity{

    private final Image sandwich;

    /**
     * Sandwich class constructor
     * @param coordinates: input to this constructor for the sandwich's coordinates
     * @param entityType: the type of the entity which in here is "Sandwich"
     */
    public Sandwich(Point coordinates, String entityType) {
        super(coordinates, entityType);
        this.sandwich = new Image("res/images/sandwich.png");
    }

    /**
     * Method to display the sandwich image onto the game screen
     */
    @Override
    public void printImage() {
        sandwich.draw(entityCoordinates.x, entityCoordinates.y);
    }
}
