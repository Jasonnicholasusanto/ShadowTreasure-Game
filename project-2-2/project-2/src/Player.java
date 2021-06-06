import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Colour;
import bagel.util.Point;
import java.text.DecimalFormat;

/**
 * The Player class which instantiates the player object for the game and has the main components
 * for the gameplay logic
 */
public class Player implements Displayable, Movable {

    // Constant variables
    private static final double STEP_SIZE = 10;
    private static final int ENERGY_THRESHOLD = 3;
    private static final int ZOMBIE_ENERGY = 3;
    private static final int SANDWICH_ENERGY = 5;
    private static final int ENERGY_DISPLAY_X = 20;
    private static final int ENERGY_DISPLAY_Y = 760;

    // Attributes that will be displayed onto the game screen
    private final Image player;
    private final Font FONT = new Font("res/font/DejaVuSans-Bold.ttf", 20);
    private final DrawOptions OPT = new DrawOptions();

    private Point playerCoordinates;
    private double playerDirectionX, playerDirectionY;
    private int energy;
    private Bullet bullet;
    private boolean shooting = false;
    private Zombie currentZombieTargeted;

    // for rounding double number; use this to print the location of the player
    private static DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Player constructor
     */
    public Player(Point coordinates, int energy){
        this.player = new Image("res/images/player.png");
        this.playerCoordinates = coordinates;
        this.energy = energy;
    }

    /**
     * Getter for player coordinates
     */
    public Point getPlayerCoordinates(){
        return playerCoordinates;
    }

    /**
     * Method to display the player's image and her energy level to the game screen
     */
    @Override
    public void printImage() {
        player.draw(playerCoordinates.x, playerCoordinates.y);
        FONT.drawString("energy: "+ energy,ENERGY_DISPLAY_X,ENERGY_DISPLAY_Y, OPT.setBlendColour(Colour.BLACK));
    }

    /**
     * Method to set the player's course and print player's energy level to console
     * referenced from Project1_sample_solution
     * @param dest: Input to this method which is player's destination to entity
     */
    @Override
    public void setDirectionTo(Point dest) {
        // Setting the direction of the player
        this.playerDirectionX = dest.x-this.playerCoordinates.x;
        this.playerDirectionY = dest.y-this.playerCoordinates.y;
        movementDirection();
    }

    /**
     * Method to determine the player's movement direction
     * referenced from Project1_sample_solution
     */
    @Override
    public void movementDirection() {
        double len = Math.sqrt(Math.pow(this.playerDirectionX,2)+Math.pow(this.playerDirectionY,2));
        this.playerDirectionX /= len;
        this.playerDirectionY /= len;
    }

    /**
     * Method for the player's movement in game
     * referenced from Project1_sample_solution
     */
    @Override
    public void move() {
        this.playerCoordinates = new Point(this.playerCoordinates.x+STEP_SIZE*this.playerDirectionX,
                this.playerCoordinates.y+STEP_SIZE*this.playerDirectionY);
    }

    /**
     * Method which displays the player's main logic
     * @param game: input to this method which is the ShadowTreasure Game
     */
    public void playerUpdate(ShadowTreasure game){

        // Checks if player encounters an entity while approaching another
        game.checkEntityOverlap();
        // Player directs itself to zombie and checks if it has met a zombie
        // Checks if there is any zombie in the arraylist to avoid error: index out of bounds
        if (game.getCount("Zombie")) {
            this.meetZombie(game);
        }
        // Player directs itself to sandwich and checks if it has met a sandwich object
        // Checks if there is any sandwich in the arraylist to avoid error: index out of bounds
        if (game.getCount("Sandwich")) {
            this.meetSandwich(game);
        }
        // Checks if player has met treasure and there are no zombies left in the game
        if (!game.getCount("Zombie")) {
            this.meetTreasure(game);
        }
        // Player moves one step
        this.move();

    }

    /**
     * Method which checks if the player encountered a zombie
     * @param game: input to this method which is the game itself
     */
    public void meetZombie(ShadowTreasure game){

        // Get the nearest zombie from the environment
        Entity zombie = game.getEntity(game.getEntityIndex("Zombie"));

        // Player meets the zombie in a shooting range
        if (zombie.meets(this)) {

            // Player releases the bullet
            if(!shooting) {
                this.shooting = true;
                this.currentZombieTargeted = (Zombie) zombie;
                this.shootZombie(currentZombieTargeted);
            }

        }

        // Player directs to nearest zombie
        if (this.energy >= ENERGY_THRESHOLD){
            // direction to zombie
            this.setDirectionTo(zombie.getEntityCoordinates());
        }

        // Shooting the zombie process
        if(shooting){
            // Writing the bullet's coordinates to the output file
            bullet.writeToFile(game);
            // When the bullet reaches the zombie
            if(currentZombieTargeted.shotDead(bullet)){
                game.removeEntity(zombie);
                shooting=false;
                game.setCount("Zombie");

                // Logic for when player has no more energy to kill existing zombies
                if(this.energy<ENERGY_THRESHOLD && !game.getCount("Sandwich")){
                    // More zombies but no more energy to shoot
                    if(game.getCount("Zombie")){
                        game.setEndOfGame(true);
                        System.out.println(this.energy);
                    }
                }
            }
        }


    }

    /**
     * Method to check if player has reached a sandwich object
     * @param game: input to this method which is the game itself
     */
    public void meetSandwich(ShadowTreasure game){

        // Get the nearest sandwich from the environment
        Entity sandwich = game.getEntity(game.getEntityIndex("Sandwich"));
        if (sandwich.meets(this)) {
            game.removeEntity(sandwich);
            game.setCount("Sandwich");
            eatSandwich();
        }

        // Set player's course to a sandwich
        if(this.energy < ENERGY_THRESHOLD){
            setDirectionTo(sandwich.getEntityCoordinates());
        }

    }

    /**
     * Method to check if the player has met the treasure
     * @param game: input to this method which is the game itself
     */
    public void meetTreasure(ShadowTreasure game){
        // Check to see if the player has met the treasure
        if (game.getTreasure().meets(this)) {
            System.out.println(this.energy + ",success!");
            game.setEndOfGame(true);
        } else {
            setDirectionTo(game.getTreasure().getEntityCoordinates());
        }
    }

    /**
     * Method which adds player's energy when she eats a sandwich
     */
    public void eatSandwich(){
        this.energy += SANDWICH_ENERGY;
    }

    /**
     * Method for player's initializing bullet to shoot a zombie (action)
     * @param zombie: input to this method which is a zombie entity in the game
     */
    public void shootZombie(Entity zombie){
        this.bullet = new Bullet(this.getPlayerCoordinates());
        bullet.setDirectionTo(zombie.getEntityCoordinates());
        this.energy-=ZOMBIE_ENERGY;
    }

    /**
     * Getter for shooting action state
     */
    public Boolean getShooting() {
        return shooting;
    }

    /**
     * Getter for bullet object
     */
    public Bullet getBullet(){
        return this.bullet;
    }
}
