import java.util.ArrayList;
import java.util.Set;

public class Simulator {


    public ArrayList<Particle> particles;
    public ArrayList<Float> densities;

    public Simulator(ArrayList<Particle> particles) {
        this.particles = particles;
        densities = new ArrayList<>();
    }

    public float getMaxDensity() {
        updateDensities(Settings.SMOOTHING_RADIUS);
        float maxDensity = -1;
        for (float val : densities) {
            if(val > maxDensity) maxDensity = val;
        }
        return maxDensity;
    }

    public void update(float smoothingRadius) {
        updateDensities(smoothingRadius);

        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            Vector2 particlePos = particle.position.cpy();
            Vector2 pressureForce = calculatePressureForce(particlePos, smoothingRadius);
            Vector2 pressureAcceleration = pressureForce.cpy().scl(densities.get(i));
            Vector2 vel = pressureAcceleration;

            particle.velocity.x = vel.x;
            particle.velocity.y = vel.y;
        }
    }

    static float smoothingKernel(float radius, float dst) {
        if(dst >= radius) return 0;
        float volume = (float) ((Math.PI * Math.pow(radius, 4)) / 6);
        return (radius - dst) * (radius - dst) / volume;
    }

    static float smoothingKernelDerivative(float dst, float radius) {
        if (dst >= radius) return 0;
        float scale = (float) (12f / (Math.pow(radius, 4) * Math.PI));
        return (dst - radius) * scale;
    }

    Vector2 calculatePressureForce(Vector2 samplePoint, float smoothingRadius) {
        Vector2 pressureForce = new Vector2();
        for (int i = 0; i < particles.size(); i++) {
            Particle particle = particles.get(i);
            Vector2 position = particle.position.cpy();
            float dst = position.dst(samplePoint);
            if (dst == 0) {
                continue;
            }
            Vector2 dir = samplePoint.cpy().sub(position).setUnit();
            float slope = smoothingKernelDerivative(dst, smoothingRadius);
            float density = densities.get(i);
            float dpMag = -convertDensityToPressure(density) * slope * Settings.MASS / density;
            Vector2 dp = dir.scl(dpMag);
            pressureForce.add(dp);
        }
        return pressureForce;
    }

    float calculateDensity(Vector2 samplePoint, float smoothingRadius) {
        float density = 0;

        for (Particle particle : particles) {
            Vector2 delta = samplePoint.cpy().sub(particle.position.cpy());
            float dst = delta.dst(new Vector2());
            float influence = smoothingKernel(smoothingRadius, dst);
            density += Settings.MASS * influence;
        }
        return density;
    }

    public void updateDensities(float smoothingRadius) {
        densities.clear();
        for (int i = 0; i < particles.size(); i++) {
            densities.add(calculateDensity(particles.get(i).position, smoothingRadius));
        }
    }

    public float convertDensityToPressure(float density) {
        float densityError = density - Settings.TARGET_DENSITY;
        float pressure = densityError * Settings.PRESSURE_MULTIPLIER;
        return pressure;
    }
}



