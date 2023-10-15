import com.raylib.java.Raylib;
import com.raylib.java.core.Color;
import com.raylib.java.core.input.Keyboard;
import com.raylib.java.core.rCore;

import com.raylib.java.shapes.Rectangle;


import java.util.ArrayList;


public class Main {
    ArrayList<Particle> particles;
    ArrayList<UIButton> uiButtons;
    Simulator simulator;
    String uiInput;
    int selectedButton;


    public static void main(String[] args){
        Main main = new Main();
        main.run();
    }

    public void run() {
        Raylib rlj = new Raylib();
        rlj.core.InitWindow(Settings.WIDTH, Settings.HEIGHT, "Raylib-J Example");

        reset();

        while (!rlj.core.WindowShouldClose()){
            update(rlj);
            render(rlj);
        }
    }

    public void reset() {
        particles = new ArrayList<>();
        uiButtons = new ArrayList<>();
        uiInput = "";
        spawnParticles(Settings.WIDTH - Settings.MARGIN_X_PIXELS, Settings.HEIGHT - Settings.MARGIN_Y_PIXELS, Settings.NUM_PARTICLES);
        initUI();
        simulator = new Simulator(particles);
        System.out.println(simulator.getMaxDensity());
    }

    private void update(Raylib rlj) {
        float deltaTime = rCore.GetFrameTime();
        simulator.update(Settings.SMOOTHING_RADIUS);
        for(Particle particle: particles) {
            particle.update(deltaTime);
        }
        updateUI(rlj);
    }

    private void render(Raylib rlj) {
        rlj.core.BeginDrawing();
        rlj.core.ClearBackground(Color.WHITE);

        for(Particle particle : particles) {
            particle.draw(rlj);
        }
        rlj.shapes.DrawRectangleLines(Settings.MARGIN_X_PIXELS,Settings.MARGIN_Y_PIXELS,
                Settings.WIDTH - (Settings.MARGIN_X_PIXELS * 2), Settings.HEIGHT - (Settings.MARGIN_Y_PIXELS * 2),
                Color.DARKGRAY);

        renderUI(rlj);
        rlj.text.DrawFPS(0,0);
        rlj.text.DrawText("Click to select setting", 0, 20, Settings.FONT_SIZE, Color.DARKGRAY);
        rlj.text.DrawText("Type to edit setting", 0, 40, Settings.FONT_SIZE, Color.DARKGRAY);
        rlj.text.DrawText("ENTER to confirm", 0, 52, Settings.FONT_SIZE, Color.DARKGRAY);
        rlj.text.DrawText("C to copy current value", 0, 72, Settings.FONT_SIZE, Color.DARKGRAY);
        rlj.text.DrawText("Max Density: " + simulator.getMaxDensity(), 0, Settings.HEIGHT - 20,
                Settings.FONT_SIZE + 8, Color.BROWN);
        rlj.core.EndDrawing();
    }

    private void spawnParticles(int width, int height, int numParticles) {
        float particleSpacing = Settings.PARTICLE_SPACING;
        float particleSize = Settings.PARTICLE_SIZE;
        int particlesPerRow = (int) Math.sqrt(numParticles);
        int particlesPerCol = (numParticles - 1) / particlesPerRow + 1;
        float spacing = particleSize * 2 + particleSpacing;
        for (int i = 0; i < numParticles; i++) {
            float x = ((i % particlesPerRow) - (particlesPerRow / 2f) + 0.5f) * spacing;
            float y = ((i / particlesPerRow) - (particlesPerCol / 2f) + 0.5f) * spacing;
            x += width / 2f;
            y += height / 2f;
            particles.add(new Particle(new Vector2(x, y)));
        }
    }

    private void initUI(){
        Rectangle marginXRect = new Rectangle(Settings.WIDTH - 140, 100, Settings.UI_BUTTON_WIDTH, 24);
        UIButton marginXButton = new UIButton(marginXRect, "Margin X: " + Settings.MARGIN_X_PIXELS, Color.BEIGE) {
            @Override
            public void onClick(String text) {
                int newVal = Integer.parseInt(text);
                Settings.MARGIN_X_PIXELS = newVal;
                Settings.MARGIN_X = (float) newVal / Settings.WIDTH;
                this.text = "Margin X: " + newVal;
            }
        };
        uiButtons.add(marginXButton);

        Rectangle marginYRect = new Rectangle(marginXRect.x, marginXRect.y + 50, Settings.UI_BUTTON_WIDTH, 24);
        UIButton marginYButton = new UIButton(marginYRect, "Margin Y: " + Settings.MARGIN_Y_PIXELS, Color.BEIGE) {
            @Override
            public void onClick(String text) {
                int newVal = Integer.parseInt(text);
                Settings.MARGIN_Y_PIXELS = newVal;
                Settings.MARGIN_Y = (float) newVal / Settings.HEIGHT;
                this.text = "Margin Y: " + newVal;
            }
        };
        uiButtons.add(marginYButton);

        Rectangle numParticlesRect = new Rectangle(marginYRect.x, marginYRect.y + 50, Settings.UI_BUTTON_WIDTH, 24);
        UIButton numParticlesButton = new UIButton(numParticlesRect, " # Particles: " + Settings.NUM_PARTICLES, Color.BLUE) {
            @Override
            public void onClick(String text) {
                int newVal = Integer.parseInt(text);
                Settings.NUM_PARTICLES = newVal;
                this.text = "# Particles: " + newVal;
                reset();
            }
        };
        uiButtons.add(numParticlesButton);

        Rectangle particleSpacingRect = new Rectangle(numParticlesRect.x, numParticlesRect.y + 50, Settings.UI_BUTTON_WIDTH, 24);
        UIButton particleSpacingButton = new UIButton(particleSpacingRect, "Spacing: " + Settings.PARTICLE_SPACING, Color.BLUE) {
            @Override
            public void onClick(String text) {
                float newVal = Float.parseFloat(text);
                Settings.PARTICLE_SPACING = newVal;
                this.text = "Spacing: " + newVal;
                reset();
            }
        };
        uiButtons.add(particleSpacingButton);

        String pressureTitle = "Pressure Mult: ";

        Rectangle pressureRect = new Rectangle(particleSpacingRect.x, particleSpacingRect.y + 50, Settings.UI_BUTTON_WIDTH + 30, 24);
        UIButton pressureButton = new UIButton(pressureRect, pressureTitle + Settings.PRESSURE_MULTIPLIER, Color.PURPLE) {
            @Override
            public void onClick(String text) {
                float newVal = Float.parseFloat(text);
                Settings.PRESSURE_MULTIPLIER = newVal;
                this.text = pressureTitle + newVal;
                reset();
            }
        };
        uiButtons.add(pressureButton);

        String densityTitle = "Target Density: ";

        Rectangle densityRect = new Rectangle(pressureRect.x, pressureRect.y + 50, Settings.UI_BUTTON_WIDTH + 30, 24);
        UIButton densityButton = new UIButton(densityRect, densityTitle + Settings.TARGET_DENSITY, Color.PURPLE) {
            @Override
            public void onClick(String text) {
                float newVal = Float.parseFloat(text);
                Settings.TARGET_DENSITY = newVal;
                this.text = densityTitle + newVal;
                reset();
            }
        };
        uiButtons.add(densityButton);


        Rectangle resetRect = new Rectangle(marginYRect.x, Settings.HEIGHT - 80, Settings.UI_BUTTON_WIDTH, 24);
        UIButton resetButton = new UIButton(resetRect, "Reset", Color.RED) {
            @Override
            public void onClick(String text) {
                reset();
            }
        };
        uiButtons.add(resetButton);
    }

    private void updateUI(Raylib rlj) {
        if(rlj.core.IsMouseButtonPressed(0)) {
            // if is within bounds
            Vector2 mousePos = new Vector2(rlj.core.GetMouseX(), rlj.core.GetMouseY());
            for(UIButton uiButton : uiButtons) {
                if(uiButton.intersect(rlj, mousePos)) {
                    selectedButton = uiButton.id;
                }
            }
        }

        char keyPressed = (char) rlj.core.GetCharPressed();
        while (keyPressed != 0) {
            uiInput += keyPressed;
            keyPressed = (char) rlj.core.GetCharPressed();
        }

        if(rlj.core.IsKeyPressed(Keyboard.KEY_BACKSPACE)) {
            uiInput = uiInput.substring(0,uiInput.length()-1);
        }

        if(rlj.core.IsKeyPressed(Keyboard.KEY_ENTER)) {
            for(UIButton uiButton : uiButtons) {
                if(uiButton.selected(selectedButton)) {
                    uiButton.onClick(uiInput);
                    uiInput = "";
                }
            }
        }

        if(rlj.core.IsKeyPressed(Keyboard.KEY_RIGHT_SHIFT)) {
            System.out.println("MaxDensity() = " + simulator.getMaxDensity());
        }

        // copy
        if(rlj.core.IsKeyPressed(Keyboard.KEY_C)) {
            UIButton sb = uiButtons.get(0);
            for(UIButton uiButton : uiButtons) {
                if(uiButton.selected(selectedButton)) {
                    sb = uiButton;
                }
            }
            int startIndex = sb.text.indexOf(':') + 1;
            System.out.println("sb.text = " + sb.text);
            System.out.println("startIndex = " + startIndex);
            if(startIndex == -1) return;
            uiInput = sb.text.substring(startIndex).strip();
        }
    }

    private void renderUI(Raylib rlj) {
        for(UIButton uiButton : uiButtons) {
            uiButton.draw(rlj);
        }
        rlj.text.DrawText(uiInput, Settings.WIDTH - Settings.UI_MARGIN_X, 20,
                Settings.FONT_SIZE, Color.DARKGRAY);
    }
}