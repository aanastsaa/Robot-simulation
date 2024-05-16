import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vut.ija_project.DataLayer.Common.Position;
import org.vut.ija_project.DataLayer.Environment.Environment;
import org.vut.ija_project.DataLayer.Environment.Room;
import org.vut.ija_project.DataLayer.Robot.ControlledRobot;
import org.vut.ija_project.DataLayer.Robot.Robot;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest
{
    private Environment room;
    private Robot r1, r2;

    /**
     * Creates room, where all tests, are played.
     */
    @BeforeEach
    public void setUp() {
        Environment room = Room.create(5, 8);

        room.createObstacleAt(1, 2);
        room.createObstacleAt(1, 4);
        room.createObstacleAt(1, 5);
        room.createObstacleAt(2, 5);

        Position p1 = new Position(4,2);
        r1 = ControlledRobot.create(room, p1);
        Position p2 = new Position(4,4);
        r2 = ControlledRobot.create(room, p2);

        this.room = room;
    }

    @Test
    public void room_GetRobots() {
        List<Robot> robots = room.robots();
        Assertions.assertEquals(2, robots.size());
        robots.remove(0);
        Assertions.assertEquals(2, room.robots().size());

        Assertions.assertEquals(r1, room.robots().get(0));
        Assertions.assertEquals(r2, room.robots().get(1));

    }

    @Test
    public void room_AddRobot() {
        Position newPos = new Position(4, 5);
        Robot newRobot = ControlledRobot.create(this.room, newPos);

        List<Robot> robots = room.robots();
        Assertions.assertEquals(3, robots.size());
    }

    @Test
    public void room_AddObstacle() {
        assertFalse(room.obstacleAt(3, 3)); // There should be no obstacle at (3, 3)
        room.createObstacleAt(3, 3);
        assertTrue(room.obstacleAt(3, 3)); // Now there should be an obstacle at (3, 3)

        assertNull(room.createObstacleAt(1, 2)); // There is already an obstacle at (1, 2)
        assertNull(room.createObstacleAt(4, 2)); // There is a robot at (4, 2)
    }

    @Test
    public void room_RobotAt() {
        Position robot1Pos = new Position(4, 2);
        Position robot2Pos = new Position(4, 4);
        Position robot3Pos = new Position(1, 2);

        assertTrue(room.robotAt(robot1Pos)); // Robot 1 is at (4, 2)
        assertTrue(room.robotAt(robot2Pos)); // Robot 2 is at (4, 4)
        assertFalse(room.robotAt(robot3Pos)); // No robot should be at (1, 2)
    }

}
