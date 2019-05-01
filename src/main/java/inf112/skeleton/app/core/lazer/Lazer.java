package inf112.skeleton.app.core.lazer;

import inf112.skeleton.app.core.board.Board;
import inf112.skeleton.app.core.board.Position;
import inf112.skeleton.app.core.board.events.Event;
import inf112.skeleton.app.core.board.events.LaserEvent;
import inf112.skeleton.app.core.enums.Direction;
import inf112.skeleton.app.core.robot.Robot;
import inf112.skeleton.app.core.sound.Sound;
import inf112.skeleton.app.core.tiles.ITile;

public class Lazer {

    public static Event shootLazer(Robot robot, Board board) throws Exception {
        Sound effect = new Sound();

        Position checkPosition = new Position(0,0);
        Direction dir = robot.getDirection();
        Position startPosition = board.getRobotPosition(robot);
        int PosX = startPosition.getX();
        int PosY = startPosition.getY();
        ITile tile = board.getTile(checkPosition);


        effect.shootLaser(); // consider placement of function for best syncronization with graphics

        // Go from position in direction of dir and look for Robot or wall
        switch (dir){
            // NORTH = y-- down to 0
            case NORTH:

                for (int i=PosY; i>=0; i-- ){
                    checkPosition = new Position(PosX, i);
                    tile = board.getTile(checkPosition);

                    if (tile.hasRobot()) {
                    // Robot has been found, damage and return position
                        board.getRobot(checkPosition).takeEnergy(1);
                        effect.laserHit(); // consider placement of function for best syncronization with graphics
                        break;
                    }
                    else if (tile.hasWall(Direction.SOUTH)) {
                        checkPosition = new Position(PosX, i-1);
                        break;
                    }
                    else if (tile.hasWall(Direction.NORTH)) {
                        break;
                    }
                }
            // SOUTH = y++ up to 11
            case SOUTH:

                for (int i=PosY; i<=11; i++){
                    tile = board.getTile(checkPosition);
                    checkPosition = new Position(PosX, i);

                    if (tile.hasRobot()) {
                    // Robot has been found, damage and return position
                        board.getRobot(checkPosition).takeEnergy(1);
                        effect.laserHit(); // consider placement of function for best syncronization with graphics
                        break;
                    }
                    else if (tile.hasWall(Direction.SOUTH)) {
                        break;
                    }
                    else if (tile.hasWall(Direction.NORTH)) {
                        checkPosition = new Position(PosX, i-1);
                        break;
                    }
                }
            // EAST = x++ up to11
            case EAST:
                for (int i=PosX; i<=11; i++) {
                    tile = board.getTile(checkPosition);
                    checkPosition = new Position(i, PosY);
                    if (tile.hasRobot()) {
                        // Robot has been found, damage and return position
                        board.getRobot(checkPosition).takeEnergy(1);
                        effect.laserHit(); // consider placement of function for best syncronization with graphics
                        break;
                    }
                    else if (tile.hasWall(Direction.EAST)) {
                        break;
                    }
                    else if (tile.hasWall(Direction.WEST)) {
                        checkPosition = new Position (i-1, PosY);
                        break;
                    }
            }
            // WEST = x-- down to 0
            case WEST:
                for (int i=PosX; i>=0; i--) {
                    tile = board.getTile(checkPosition);
                    checkPosition = new Position(i, PosY);
                        if (tile.hasRobot()) {
                        // Robot has been found, damage and return position
                        board.getRobot(checkPosition).takeEnergy(1);
                        effect.laserHit(); // consider placement of function for best syncronization with graphics
                        break;
                    }
                    else if (tile.hasWall(Direction.EAST)) {
                        checkPosition = new Position(i-1, PosY);
                        break;
                    }
                    else if (tile.hasWall(Direction.WEST)) {
                        break;
                    }
            }

        }

        // build LaserEvent and return values
        LaserEvent returnEvent = new LaserEvent(startPosition,checkPosition);
        return returnEvent;
    }
}
