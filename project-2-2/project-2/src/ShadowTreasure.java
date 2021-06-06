import bagel.*;
import bagel.util.Point;

import java.io.*;
import java.util.ArrayList;


/**
 * An example Bagel game.
 */
public class ShadowTreasure extends AbstractGame {

    private final Image BACKGROUND = new Image("res/images/background.png");

    // Constant variables
    private static final int TYPE_INDEX = 0;
    private static final int POS_X_INDEX = 1;
    private static final int POS_Y_INDEX = 2;
    private static final int ENERGY_INDEX = 3;

    // An array list to store zombies and sandwiches (entities) for the game's environment
    private final ArrayList<Entity> entityList = new ArrayList<>();

    private boolean endOfGame;
    private boolean appendState;
    private int tickCounter=0;
    private int sandwichCounter=0;
    private int zombieCounter=0;

    private Player player;
    private Entity treasure;

    /**
     * ShadowTreasure class constructor
     */
    public ShadowTreasure() throws IOException {
        this.loadEnvironment("res/IO/environment.csv");
        this.endOfGame = false;
        this.appendState = false;
    }

    /**
     * A setter for the variable appendState
     * @param appendState: input to this method which is a boolean variable
     */
    public void setAppendState(boolean appendState){
        this.appendState = appendState;
    }

    /**
     * A getter for the variable appendState
     */
    public boolean getAppendState(){
        return appendState;
    }

    /**
     * A getter for the treasure object within the game's environment
     */
    public Entity getTreasure() {
        return treasure;
    }

    /**
     * This method is used to know if there is any zombies/sandwiches or not in the game
     * @param type: input to this method which is a String to denounce which type of entity
     */
    public boolean getCount(String type){
        if(type.equals("Zombie") && zombieCounter!=0){
            return true;
        } else return type.equals("Sandwich") && sandwichCounter != 0;
    }

    /**
     * Method to reduce the count of entity after being removed from the environment
     * @param type: input to this method which is a string to denounce which type of entity
     */
    public void setCount(String type){
        if(type.equals("Zombie")){
            this.zombieCounter -= 1;
        } else if (type.equals("Sandwich")){
            this.sandwichCounter -= 1;
        }
    }

    /**
     * Method to get a specific entity type's index from the game's environment (arraylist)
     * @param type: input to this method which is a string to denounce which type of entity
     */
    public int getEntityIndex(String type){
        int index=0;
        double closestDist = 0;

        for(int i=0; i<entityList.size(); i++) {
            Entity entity = entityList.get(i);
            if (entity.entityType.equals(type)) {
                double dist = entity.getEntityCoordinates().distanceTo(player.getPlayerCoordinates());
                if(dist<closestDist || closestDist==0){
                    closestDist = dist;
                    index = i;
                }
            }
        }
        return index;
    }

    /**
     * Method to retrieve a specific entity from the entity list
     * @param index: input to this method which is an index of an entity
     */
    public Entity getEntity(int index){
        return entityList.get(index);
    }

    /**
     * Method to remove entity from the environment
     * @param entity: input to this method which is an entity object
     */
    public void removeEntity(Entity entity){
        entityList.remove(entity);
    }

    /**
     * Method to check if player interacts with an entity in the game's environment on its way to another
     */
    public void checkEntityOverlap(){
        for(Entity entity:entityList){
            if(entity.getEntityType().equals("Sandwich")) {
                if (player.getPlayerCoordinates() == entity.getEntityCoordinates()) {
                    player.eatSandwich();
                    entityList.remove(entity);
                }
            }
        }
    }

    /**
     * Method loads from input file
     * referenced from Project1_sample_solution
     * @param filename: input to this method which is a name of an environment file
     */
    private void loadEnvironment(String filename){
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] info = line.split(",");
                String type = info[TYPE_INDEX];
                type = type.replaceAll("[^a-zA-Z0-9]", ""); // remove special characters
                Point coordinate = new
                        Point(Double.parseDouble(info[POS_X_INDEX]), Double.parseDouble(info[POS_Y_INDEX]));

                switch (type) {
                    case "Player" -> this.player = new Player(coordinate, Integer.parseInt(info[ENERGY_INDEX]));
                    case "Zombie" -> {
                        entityList.add(new Zombie(coordinate, type));
                        zombieCounter++;
                    }
                    case "Sandwich" -> {
                        entityList.add(new Sandwich(coordinate, type));
                        sandwichCounter++;
                    }
                    case "Treasure" -> this.treasure = new Treasure(coordinate, type);
                    default -> throw new BagelError("Unknown type: " + type);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /**
     * Method to display the entity images to the game
     * @param entities: input to this method which is an array list of entity objects
     */
    private void displayEntity(ArrayList<Entity> entities){
        for(Entity entity : entities)
            entity.printImage();
    }

    /**
     * Performs a state update.
     */
    @Override
    public void update(Input input) {

        tickCounter++;
        // This logic was taken from the sample solution
        if (this.endOfGame || input.wasPressed(Keys.ESCAPE)){
            Window.close();
        } else{
            BACKGROUND.drawFromTopLeft(0, 0);
            treasure.printImage();
            displayEntity(entityList);

            // Game proceeds as the speed of a tick
            if (tickCounter%10 == 0){
                player.printImage();

                // Player updates including the movement and setting her direction
                player.playerUpdate(this);

                // Bullet movement to a zombie
                if(player.getShooting()) {
                    player.getBullet().printImage();
                    player.getBullet().move();

                }


            } else {
                player.printImage();
                if(player.getShooting()){
                    player.getBullet().printImage();
                }
            }
        }
    }

    /**
     * Method setting the end of the game
     * @param state: input to this method which is a boolean
     */
    public void setEndOfGame(boolean state) {
        this.endOfGame = state;
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) throws IOException {
        ShadowTreasure game = new ShadowTreasure();
        game.run();
    }
}
