import bagel.Image;
import bagel.util.Point;

/**
 * The zombie class which is a child class to Entity class
 */
public class Zombie extends Entity{

    private static final double SHOT_DEAD = 25;
    private static final double SHOOTING_RANGE = 150;

    private final Image zombie;

    /**
     * Constructor for Zombie class
     * @param coordinates: input to the constructor which is the coordinates of the zombie object
     * @param entityType: the type of the entity which in here is "Zombie"
     */
    public Zombie(Point coordinates, String entityType) {
        super(coordinates, entityType);
        this.zombie = new Image("res/images/zombie.png");
    }

    /**
     * Method to print the zombie image to the game screen
     */
    @Override
    public void printImage() {
        zombie.draw(entityCoordinates.x, entityCoordinates.y);
    }

    /**
     * Method which states that a zombie has been shot by a bullet
     * referenced from Project1_sample_solution
     * @param bullet: input to this method which is the bullet object
     */
    public boolean shotDead(Bullet bullet){
        boolean hasKilled = false;

        double distanceToBullet = bullet.getBulletCoordinates().distanceTo(entityCoordinates);
        if (distanceToBullet < SHOT_DEAD) {
            hasKilled = true;
        }

        return hasKilled;
    }

    /**
     * Method to state when player has met a zombie at shooting range
     * referenced from Project1_sample_solution
     * @param player: input to this method which is the player object
     */
    @Override
    public boolean meets(Player player){
        boolean hasMet = false;

        double distanceToPlayer = player.getPlayerCoordinates().distanceTo(entityCoordinates);
        if (distanceToPlayer < SHOOTING_RANGE) {
            hasMet = true;
        }

        return hasMet;
    }
}
