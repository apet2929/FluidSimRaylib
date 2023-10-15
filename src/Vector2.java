public class Vector2 extends com.raylib.java.raymath.Vector2 {
    public Vector2(){
        super(0,0);
    }
    public Vector2(float x, float y) {
        super(x,y);
    }

    public Vector2 cpy() {
        return new Vector2(this.x, this.y);
    }

    public Vector2 scl(float scalar) {
        this.x *= scalar;
        this.y *= scalar;
        return this;
    }
    public Vector2 scl(float scalarX, float scalarY) {
        this.x *= scalarX;
        this.y *= scalarY;
        return this;
    }
    public Vector2 scl(Vector2 scalar) {
        this.x *= scalar.x;
        this.y *= scalar.y;
        return this;
    }

    public Vector2 sub(Vector2 other) {
        this.x -= other.x;
        this.y -= other.y;
        return this;
    }

    public Vector2 add(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
        return this;
    }

    public Vector2 setLength(float length) {
        setUnit();
        return scl(length);
    }

    public Vector2 setUnit() {
        float mag = dst(new Vector2());
        this.x /= mag;
        this.y /= mag;
        return this;
    }

    public float dst(Vector2 other) {
        return (float) Math.sqrt((Math.pow(this.x - other.x,2)) + (Math.pow(this.y - other.y, 2)));
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ')';

    }
}
