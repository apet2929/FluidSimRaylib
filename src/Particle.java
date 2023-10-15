import com.raylib.java.Raylib;
import com.raylib.java.core.Color;

public class Particle {
    // position in range (0,1)
    public Vector2 position;
    public Vector2 velocity;
    public float radius;
    public float density;

    public Particle(Vector2 position) {
        this.position = position.scl(1f/Settings.WIDTH, 1f/Settings.HEIGHT);
        System.out.println("position = " + position);
        velocity = new Vector2();
        radius = Settings.PARTICLE_SIZE;
        density = 0.0f;
    }

    public void update(float deltaTime) {
        position.x += velocity.x * deltaTime;
        position.y += velocity.y * deltaTime;

        velocity.y += Settings.GRAVITY * deltaTime;
        resolveCollisions();
    }

    public void draw(Raylib r) {
        Vector2 pos = position.cpy().scl(Settings.WIDTH, Settings.HEIGHT);
        r.shapes.DrawCircle((int) pos.x, (int) pos.y, radius, Color.BLUE);
    }

    private void resolveCollisions() {

        float rx = radius / Settings.WIDTH;
        float ry = radius / Settings.HEIGHT;
        if(position.x - rx < Settings.MARGIN_X) {
            position.x = Settings.MARGIN_X + rx;
            velocity.x *= -1;
        } else if(position.x + rx > 1 - Settings.MARGIN_X) {
            position.x = 1 - Settings.MARGIN_X - rx;
            velocity.x *= -1;
        }

        if(position.y - ry < Settings.MARGIN_Y) {
            position.y = Settings.MARGIN_Y + ry;
            velocity.y *= -1;
        } else if(position.y + ry > 1 - Settings.MARGIN_Y) {
            position.y = 1 - Settings.MARGIN_Y - ry;
            velocity.y *= -1;
        }
    }
}
