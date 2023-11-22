
package tile;

import java.util.Random;

public class PerlinNoise {
	private static final int P = 512;
    private static final int[] permutation = new int[P * 2];

    static {
        Random rand = new Random();

        for (int i = 0; i < P; i++) {
            permutation[i] = permutation[i + P] = rand.nextInt(P);
        }
    }

    public static int[][] generateNoise(int width, int height, int octaveCount, double persistence) {
        int[][] noise = new int[width][height];

        double maxNoiseValue = 0;
        double amplitude = 1;
        double frequency = 1;

        for (int octave = 0; octave < octaveCount; octave++) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    double xCoord = x / (double) width * frequency;
                    double yCoord = y / (double) height * frequency;

                    double perlinValue = noise(xCoord, yCoord);

                    // Scale to [0, 9] and adjust for the game scenario
                    noise[x][y] += (int) (4 * (perlinValue + 1)); // Scale and shift

                    // Ensure the values are within [0, 9]
                    noise[x][y] = Math.min(Math.max(0, noise[x][y]), 9);

                    // Apply specific values for the game scenario
                    if (noise[x][y] == 5 || noise[x][y] == 6 || noise[x][y] == 7 || noise[x][y] == 2) {
                       
                    } else if (octave == octaveCount - 1 && (x < 5 || x > width - 6 || y < 5 || y > height - 6)) {
                        noise[x][y] = 1; // Walls around specific areas
                    }
                }
            }

            maxNoiseValue += amplitude;
            amplitude *= persistence;
            frequency *= 2;
        }

        return noise;
    }

    private static double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    private static double grad(int hash, double x) {
        int h = hash & 15;
        double grad = 1 + (h & 7);
        if ((h & 8) != 0) grad = -grad;
        return (grad * x);
    }

    private static double noise(double x, double y) {
        int X = (int) Math.floor(x) & 255;
        int Y = (int) Math.floor(y) & 255;

        x -= Math.floor(x);
        y -= Math.floor(y);

        double u = fade(x);
        double v = fade(y);

        int a = permutation[X] + Y;
        int b = permutation[X + 1] + Y;

        return lerp(v, lerp(u, grad(permutation[a], x), grad(permutation[b], x - 1)),
                        lerp(u, grad(permutation[a + 1], x - 1), grad(permutation[b + 1], x - 1)));
    }
}
