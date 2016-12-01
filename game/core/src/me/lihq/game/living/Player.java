package me.lihq.game.living;

import com.badlogic.gdx.math.Interpolation;
import me.lihq.game.GameMain;
import me.lihq.game.Settings;
import me.lihq.game.models.Inventory;
import me.lihq.game.models.Room;

/**
 * Created by brookehatton on 18/11/2016.
 */
public class Player extends AbstractPerson
{

    //The personality will be a percent score (0-100) 50 being neutral etc etc
    private int personalityLevel = 50;

    private Inventory inventory = new Inventory();

    private int score = 0;

    public Boolean move = false;

    private String name;

    private Room currentRoom;

    public Player(String name, String imgSrc)
    {
        super(imgSrc);
        this.name = name;
    }

    /**
     * This method will change the players personality by the given amount.
     * It will cap the personality between 0 and 100.
     * <p>
     * If the change takes it out of these bounds, it will change it to the min or max.
     *
     * @param change - The amount to change by, can be positive or negative
     */
    public void addToPersonality(int change)
    {
        personalityLevel = personalityLevel + change;

        if (personalityLevel < 0) {
            personalityLevel = 0;
        } else if (personalityLevel > 100) {
            personalityLevel = 100;
        }
    }

    public void updateMotion(float delta)
    {

        if (this.state == ACTOR_STATE.WALKING) {
            this.animTimer += delta;
            this.setPosition(Interpolation.linear.apply(startPosition.x, destinationPosition.x, animTimer / ANIM_TIME), Interpolation.linear.apply(startPosition.y, destinationPosition.y, animTimer / ANIM_TIME));
            if (animTimer > ANIM_TIME) {
                this.finishMove();
                this.setTileCoordinates(destinationPosition.x/32, destinationPosition.y/32);
            }
        }


    }

    public void initialiseMove(DIRECTION dir) {
        this.startPosition.x = this.tileCoordinates.x * Settings.TILE_SIZE;
        this.startPosition.y = this.tileCoordinates.y * Settings.TILE_SIZE;
        this.destinationPosition.x = this.startPosition.x + dir.getDx() * Settings.TILE_SIZE;
        this.destinationPosition.y = this.startPosition.y + dir.getDy() * Settings.TILE_SIZE;
        this.animTimer = 0f;
        this.state = ACTOR_STATE.WALKING;

    }

    public void finishMove() {
        this.state = ACTOR_STATE.STANDING;
    }
    /**
     * This moves the player to a new tile
     * @param dx the amount of tiles to move in the x direction
     * @param dy the amout of tiles to move in the y direction
     */

    public void move(DIRECTION dir)
    {
        if (this.state != ACTOR_STATE.STANDING) {
            return;
        }

        if (!currentRoom.isWalkableTile(this.tileCoordinates.x + dir.getDx(),this.tileCoordinates.y + dir.getDy())) {
            return;
        }

        initialiseMove(dir);

    }

    public Inventory getInventory()
    {
        return this.inventory;
    }

    public String getPlayername()
    {
        return this.name;
    }

    public int getPersonality()
    {
        return this.personalityLevel;
    }

    public void changeRoom(int roomID, int newX, int newY)
    {
        changeRoom(GameMain.me.gameMap.getRoom(roomID), newX, newY);
    }

    public void changeRoom(Room newRoom, int newX, int newY)
    {
        currentRoom = newRoom;

        this.setTileCoordinates(newX, newY);
    }

    public void setRoom(Room room)
    {
        this.currentRoom = room;
    }

    public Room getRoom()
    {
        return this.currentRoom;
    }
}
