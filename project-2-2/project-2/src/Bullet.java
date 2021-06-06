import bagel.Image;
import bagel.util.Point;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

/**
 * The Bullet class which instantiates the bullet object to the game
 */
public class Bullet implements Movable, Displayable{

    private static final String OUTPUT_FILENAME = "res/IO/output.csv";
    private static final double STEP_SIZE = 25;

    private Point bulletCoordinates;
    private double bulletDirectionX, bulletDirectionY;
    private final Image bullet;

    private static DecimalFormat df = new DecimalFormat("0.00");

    /**
     * Bullet Constructor
     * @param pos: input to this method which is the initial position of the bullet
     */
    public Bullet(Point pos){
        this.bullet = new Image("res/images/shot.png");
        this.bulletCoordinates = pos;
    }

    /**
     * Getter for bullet coordinates
     */
    public Point getBulletCoordinates() {
        return bulletCoordinates;
    }

    /**
     * This method sets the direction of the bullet to a zombie
     * referenced from Project1_sample_solution
     * @param dest: input to this method which is the zombie's coordinates
     */
    @Override
    public void setDirectionTo(Point dest) {
        this.bulletDirectionX = dest.x-this.bulletCoordinates.x;
        this.bulletDirectionY = dest.y-this.bulletCoordinates.y;
        movementDirection();
    }

    /**
     * Method which determines the direction of the bullet
     * referenced from Project1_sample_solution
     */
    @Override
    public void movementDirection() {
        double len = Math.sqrt(Math.pow(this.bulletDirectionX,2)+Math.pow(this.bulletDirectionY,2));
        this.bulletDirectionX /= len;
        this.bulletDirectionY /= len;
    }

    /**
     * Method which enables the bullet's movement in the game
     * referenced from Project1_sample_solution
     */
    @Override
    public void move() {
        this.bulletCoordinates = new Point(this.bulletCoordinates.x+STEP_SIZE*this.bulletDirectionX,
                this.bulletCoordinates.y+STEP_SIZE*this.bulletDirectionY);
    }

    /**
     * Method which displays the bullet image onto the game screen
     */
    public void printImage(){
        bullet.draw(bulletCoordinates.x, bulletCoordinates.y);
    }

    /**
     * Method to write the bullet's coordinates to a CSV file
     */
    public void writeToFile(ShadowTreasure game){
        try(PrintWriter pw = new PrintWriter((new FileWriter(OUTPUT_FILENAME, game.getAppendState())))){
            pw.println(df.format(bulletCoordinates.x) + "," + df.format(bulletCoordinates.y));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Change the append state so consecutive write to files will not overwrite the previous information
        if(!game.getAppendState()){
            game.setAppendState(true);
        }
    }

}
