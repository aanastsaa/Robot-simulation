import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.vut.ija_project.DataLayer.Environment.Environment;
import org.vut.ija_project.DataLayer.Environment.Room;
import org.vut.ija_project.DataLayer.Robot.AutonomousRobot;
import org.vut.ija_project.DataLayer.Robot.Robot;
import org.vut.ija_project.DataLayer.Common.Position;

public class AutonomousRobotTest
{
    private Environment room;
    private Robot r1, r2;

    @BeforeEach
    public void setUp() {
        Environment room = Room.create(5, 8);

        room.createObstacleAt(1, 2);
        room.createObstacleAt(1, 4);
        room.createObstacleAt(2, 2);

        Position p1 = new Position(4,2);
        r1 = AutonomousRobot.create(room, p1);
        Position p2 = new Position(4,4);
        r2 = AutonomousRobot.create(room, p2);

        this.room = room;
    }

    @Test
    public void AutonomousRobot_TurnsTest() {

        String strTurn1 = "CLOCKWISE";
        String strTurn2 = "COUNTERCLOCKWISE";

        // Test for turning robot in the environment without obstacles
        Assertions.assertTrue(r1.canMove());
        Assertions.assertTrue(r2.canMove());

        Position p3 = new Position(2,4);
        ((AutonomousRobot) r2).setTurningDirection(AutonomousRobot.Direction.CLOCKWISE);
        r2.updatePosition();
        r2.updatePosition();
        Assertions.assertEquals(p3, r2.getPosition());

        Position p4 = new Position(2,2);
        ((AutonomousRobot) r1).setTurningDirection(AutonomousRobot.Direction.CLOCKWISE);
        r1.updatePosition();
        r1.updatePosition();
        r1.updatePosition();
        // we have obstacle at (2,2) so robot should not move, we will have position (2,3)
        Assertions.assertNotEquals(p4, r1.getPosition());
        Assertions.assertEquals(new Position(2,3), r1.getPosition());

        ((AutonomousRobot) r1).setTurningDirection(AutonomousRobot.Direction.COUNTERCLOCKWISE);
        r1.updatePosition();
        r1.updatePosition();
        Position p5 = new Position(1,3);
        Assertions.assertEquals(p5, r1.getPosition());

    }

    @Test
    public void AutonomousRobot_CheckObstacles() {

        // Test for turning robot in the environment with obstacles
        Position p3 = new Position(1,4);
        Robot r3 = AutonomousRobot.create(room, p3);
        Assertions.assertNull(r3);

        Position p4 = new Position(3,3);
        Robot r4 = AutonomousRobot.create(room, p4);
        Assertions.assertNotNull(r4);
        room.createObstacleAt(2, 3);
        room.createObstacleAt(3, 3);
        Assertions.assertFalse(r4.canMove());

        Position p5 = new Position(3,4);
        Robot r5 = AutonomousRobot.create(room, p5);
        Robot r6 = AutonomousRobot.create(room, p5);
        Assertions.assertNotEquals(null, r5);
        Assertions.assertNull(r6);

        room.createObstacleAt(4, 4);
        room.createObstacleAt(3, 5);
        room.createObstacleAt(2, 4);
        room.createObstacleAt(3, 3);
        Assertions.assertFalse(r5.canMove());
    }
}
