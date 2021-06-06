import bagel.util.Point;

/**
 * An abstract parent class Entity, instantiates all the entities for the game's environment
 */
public abstract class Entity implements Displayable{
    private static final int ENTITY_DIST = 50;

    protected Point entityCoordinates;
    protected String entityType;

    /**
     * Entity constructor
     * @param coordinates: the input to this constructor which is the entity's coordinates
     * @param entityType: the input to this constructor which states the entity's type
     */
    public Entity(Point coordinates, String entityType){
        this.entityCoordinates = coordinates;
        this.entityType = entityType;
    }

    /**
     * A getter for en entity coordinates
     */
    public Point getEntityCoordinates() {
        return entityCoordinates;
    }

    /**
     * A getter for an entity type
     */
    public String getEntityType(){ return entityType; }

    /**
     * Method which states the player meeting an entity
     * referenced from Project1_sample_solution
     * @param player: input to this method which is the player object
     */
    public boolean meets(Player player) {
        boolean hasMet = false;

        double distanceToPlayer = player.getPlayerCoordinates().distanceTo(entityCoordinates);
        if (distanceToPlayer < ENTITY_DIST) {
                hasMet = true;
        }

        return hasMet;
    }
}
