package example;

import org.dyn4j.dynamics.Body;
import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.MassType;
import org.dyn4j.geometry.Rectangle;
import org.dyn4j.geometry.Vector2;

public class Test {
	static public void main(String[] args) {
		World world = new World();
		Body floor = new Body();
		floor.addFixture(new BodyFixture(new Rectangle(15.0, 1.0)));
		floor.setMass(MassType.INFINITE);
		System.out.println(floor.getWorldVector(new Vector2()));
		world.addBody(floor);
		world.step(1, 0.012);
		System.out.println(floor.getWorldVector(new Vector2()));
	}
}