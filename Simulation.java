import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import java.awt.geom.Point2D;
import javax.swing.*;

public class Simulation {
    public static ArrayList<Airport> airports = new ArrayList<>();
    public static ArrayList<Aeroplane> aeroplanes = new ArrayList<>();



    static Point2D.Float generateLocation(ArrayList<Airport> airports, int width, int height, int edgeOffset, int noofAirports) {
        int distanceBetween = width / noofAirports;
        Random random = new Random();
        Point2D.Float location = new Point2D.Float(random.nextFloat(edgeOffset / 2, width - edgeOffset), random.nextFloat(edgeOffset / 2, height - edgeOffset));
        if (airports.size() <= 0) {
            return location;
        } else {
            Point2D.Float newLocation = new Point2D.Float(random.nextFloat(edgeOffset / 2, width - edgeOffset), random.nextFloat(edgeOffset / 2, height - edgeOffset));

            for (Airport airport : airports) {
                if (Point2D.distance(newLocation.x, newLocation.y, airport.location.x, airport.location.y) <= distanceBetween) {
                    return generateLocation(airports, width, height, edgeOffset, noofAirports); // Recursive call
                }
            }

            return newLocation; // Return newLocation if it doesn't violate the condition
        }
    }

    static Color generateUniqueColor(int maxInt, int currentInt) {
        float hue = (float) currentInt / maxInt;
        return Color.getHSBColor(hue, 1f, 1f);
    }

    public static void main(String[] args) {
        float time = 0;
        float lastTime = 0;
        float dt = 0;
        int width = 800;
        int height = 800;
        int edgeOffset = 100;
        boolean running = true;
        double start = System.nanoTime() /100000000;
        int noofAirports = 5;
        int noofAeroplanes = 30;
        int idOfPlaneToWatch = 3;
        int frameCap = 10;

        // Create and set up the window
        JFrame frame = new JFrame("Airport Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        // Add the simulation panel
        SimulationPanel panel = new SimulationPanel();
        frame.add(panel);

        // Display the window
        frame.setVisible(true);

        //creates all airports and adds to array list
        for (int air = 0; air < noofAirports; air++) {
            airports.add(new Airport(3, 2, air, generateLocation(airports, width, height, edgeOffset, noofAirports)));
        }
        //creates all aeroplanes and adds to array list
        for (int aer = 0; aer < noofAeroplanes; aer++) {
            aeroplanes.add(new Aeroplane(aer, noofAirports - 2, airports, generateUniqueColor(noofAeroplanes, aer)));
        }
        //plane to watch.
        aeroplanes.get(idOfPlaneToWatch).watching = true;

        for (Airport airport : airports) {
            System.out.println(airport.location);
        }

        while (running) {
            //all code;

            time = ((float) (System.nanoTime() / 100000000) - (float) start) / frameCap; // / x is freamrate cap, so 10 is 10 ticks per seconds 100 is 100 ticks per second, increaseing it decresses speed of a airplanes movies tho.
            
            // only calls updates when its gone more then 0.1 seconds.
            //so i can do countdowns and stuff.
            if (time > lastTime) {
                dt = time - lastTime;
                for (Aeroplane aeroplane : aeroplanes) {
                    aeroplane.update(dt);
                }
            }
            //System.out.println(time);
            panel.repaint();
            lastTime = time;
        }
    }
}