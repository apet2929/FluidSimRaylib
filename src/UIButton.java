import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import com.raylib.java.shapes.Rectangle;

public abstract class UIButton {
    public Rectangle rectangle;
    public Color color;
    String text;
    int id;

    public UIButton(Rectangle rectangle, String text) {
        this(rectangle, text, Color.DARKGRAY);
    }

    public UIButton(Rectangle rectangle, String text, Color color) {
        this.rectangle = rectangle;
        this.color = color;
        this.text = text;
        id = text.hashCode();
    }

    public void draw(Raylib rlj){
        rlj.shapes.DrawRectangleLinesEx(rectangle, 3, color);
        rlj.text.DrawText(text, (int) (rectangle.x + 5),
                (int) (rectangle.y + (rectangle.height / 2) - (Settings.FONT_SIZE/2)), Settings.FONT_SIZE, color);
    }

    public boolean intersect(Raylib rlj, Vector2 pos) {
        return rlj.shapes.CheckCollisionPointRec(pos, rectangle);
    }
    public boolean selected(int selectedId) {
        return selectedId == id;
    }

    public abstract void onClick(String input);
}
