package inf112.skeleton.app.core.lazer;

import inf112.skeleton.app.core.board.Board;
import inf112.skeleton.app.core.board.Position;
import inf112.skeleton.app.core.board.events.Event;
import inf112.skeleton.app.core.board.events.LaserEvent;
import inf112.skeleton.app.core.enums.Direction;
import inf112.skeleton.app.core.robot.Robot;
import inf112.skeleton.app.core.tiles.ITile;
import inf112.skeleton.app.core.tiles.Tile;

public class Lazer {

    public Event shootLazer(Robot robot, Board board) {
        Position checkPosition = new Position(0,0);
        Direction dir = robot.getDirection();
        Position startPosition = board.getRobotPosition(robot);
        int PosX = startPosition.getX();
        int PosY = startPosition.getY();
        ITile tile = board.getTile(checkPosition);

        // Go from position in direction of dir and look for Robot or wall
        switch (dir){
            // NORTH = y-- down to 0
            case NORTH:

                for (int i=PosY; i>=0; i-- ){
                    tile = board.getTile(checkPosition);
                    checkPosition = new Position(PosX, i);
                if (!(board.getRobot(checkPosition)==null)) {
                    // Robot has been found, damage and return position
                    board.getRobot(checkPosition).takeEnergy(1);
                    break;
                }
                else if (tile.hasWall(Direction.SOUTH)||tile.hasWall(Direction.NORTH)) {
                        break;
                }
            }
            // SOUTH = y++ up to 11
            case SOUTH:

                for (int i=PosY; i<=11; i++){
                    tile = board.getTile(checkPosition);
                    checkPosition = new Position(PosX, i);
                if (!(board.getRobot(checkPosition)==null)) {
                    // Robot has been found, damage and return position
                    board.getRobot(checkPosition).takeEnergy(1);
                    break;
                }
                else if (tile.hasWall(Direction.SOUTH)||tile.hasWall(Direction.NORTH)) {
                    break;
                }
            }
            // EAST = x++ up to11
            case EAST:
                for (int i=PosX; i<=11; i++) {
                    tile = board.getTile(checkPosition);
                    checkPosition = new Position(i, PosY);
                    if (!(board.getRobot(checkPosition)==null)) {
                        // Robot has been found, damage and return position
                        board.getRobot(checkPosition).takeEnergy(1);
                        break;
                    }
                    else if (tile.hasWall(Direction.EAST)||tile.hasWall(Direction.WEST)) {
                        break;
                    }
            }
            // WEST = x-- down to 0
            case WEST:
                for (int i=PosX; i>=0; i--) {
                    tile = board.getTile(checkPosition);
                    checkPosition = new Position(i, PosY);
                    if (!(board.getRobot(checkPosition)==null)) {
                        // Robot has been found, damage and return position
                        board.getRobot(checkPosition).takeEnergy(1);
                        break;
                    }
                    else if (tile.hasWall(Direction.EAST)||tile.hasWall(Direction.WEST)) {
                        break;
                    }
            }

        }

        // build Laserevent and return values
        LaserEvent returnEvent = new LaserEvent(startPosition,checkPosition);
        return returnEvent;
    }
}
