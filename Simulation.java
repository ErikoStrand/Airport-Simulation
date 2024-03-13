import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.Point2D;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

class SimulationPanel extends JPanel {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw airports
        for (Airport airport : Simulation.airports) {
            drawAirport(g2d, airport);
        }

        // Draw aeroplanes
        for (Aeroplane aeroplane : Simulation.aeroplanes) {
            drawAeroplane(g2d, aeroplane);
        }
    }

    private void drawAirport(Graphics2D g2d, Airport airport) {
        // Draw a small circle for the airport
        g2d.setColor(Color.BLACK);
        g2d.fillRect((int) airport.location.x, (int) airport.location.y, 30, 30);
    }

    private void drawAeroplane(Graphics2D g2d, Aeroplane aeroplane) {
        // Draw a small circle for the aeroplane
        g2d.setColor(Color.GRAY);
        g2d.fill(new Ellipse2D.Float(aeroplane.location.x, aeroplane.location.y, 10, 10));
    }
}

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
    
    public static void main(String[] args) {
        float time = 0;
        float lastTime = 0;
        float dt = 0;
        int width = 800;
        int height = 800;
        int edgeOffset = 100;
        boolean running = true;
        double start = System.nanoTime() /100000000;
        int noofAirports = 4;
        int noofAeroplanes = 20;
        int idOfPlaneToWatch = 3;
        int frameCap = 10;

        // Create and set up the window
        JFrame frame = new JFrame("Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        // Add the simulation panel
        SimulationPanel panel = new SimulationPanel();
        frame.add(panel);

        // Display the window
        frame.setVisible(true);


        //creates all airports and adds to array list
        for (int air = 0; air < noofAirports; air++) {
            airports.add(new Airport(3, 6, air, generateLocation(airports, width, height, edgeOffset, noofAirports)));
        }
        //creates all aeroplanes and adds to array list
        for (int aer = 0; aer < noofAeroplanes; aer++) {
            aeroplanes.add(new Aeroplane(aer, 15, airports));
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
                for (Airport airport : airports) {
                    airport.update();

                }

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