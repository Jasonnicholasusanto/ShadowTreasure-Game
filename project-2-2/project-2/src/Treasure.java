import bagel.Image;
import bagel.util.Point;

/**
 * The treasure class which instantiates the treasure object for the game
 */
public class Treasure extends Entity {

    private final Image treasure = new Image("res/images/treasure.png");

    /**
     * Treasure constructor
     * @param coordinates: Input to this method which is coordinates of type Point
     * @param entityType: Input to this method which is the entity type in this case: "Treasure"
     */
    public Treasure(Point coordinates, String entityType) {
        super(coordinates, entityType);
    }

    @Override
    public void printImage() {
        treasure.draw(entityCoordinates.x, entityCoordinates.y);
    }

}
