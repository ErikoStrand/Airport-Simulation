import java.util.ArrayList;
import java.util.Random;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.geom.Point2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Simulation {
    public static ArrayList<Airport> airports = new ArrayList<>();
    public static ArrayList<Aeroplane> aeroplanes = new ArrayList<>();
    public static JLabel infoLabelAeroplane = new JLabel("Selected Plane: None (click one)");
    public static JLabel infoLabelAirport = new JLabel();

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
        float sat = (float) currentInt / maxInt;

        return Color.getHSBColor(1f, sat, 0.8f);
    }

    public static void main(String[] args) {
        float time = 0;
        float lastTime = 0;
        float dt = 0;
        int width = 800;
        int height = 800;
        int edgeOffset = 50;
        boolean running = true;
        double start = System.nanoTime() /10000000;
        int noofAirports = 10;
        int noofAeroplanes = 100;

        // Create and set up the window
        JFrame frame = new JFrame("Airport Simulation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height + 50);

        //setup info label
        infoLabelAeroplane.setBorder(new EmptyBorder(10, 10, 10, 10));
        infoLabelAirport.setBorder(new EmptyBorder(10, 10, 10, 10));


        // Add the simulation panel
        SimulationPanel panel = new SimulationPanel();
        panel.setBackground(new Color(40, 40, 40));
        frame.add(panel, BorderLayout.CENTER);

        JPanel menu = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        menu.add(infoLabelAeroplane, BorderLayout.SOUTH);
        menu.add(infoLabelAirport, BorderLayout.SOUTH);   
        menu.setBackground(new Color(50, 50, 50)); 
        menu.setOpaque(true);
        infoLabelAeroplane.setForeground(new Color(200, 200, 200));
        infoLabelAirport.setForeground(new Color(200, 200, 200));

        frame.add(menu, BorderLayout.SOUTH);
        // Display the window
        frame.setVisible(true);

        //creates all airports and adds to array list
        for (int air = 0; air < noofAirports; air++) {
            airports.add(new Airport(4, 2, air, generateLocation(airports, width, height, edgeOffset, noofAirports)));
        }
        //creates all aeroplanes and adds to array list
        for (int aer = 0; aer < noofAeroplanes; aer++) {
            aeroplanes.add(new Aeroplane(aer, noofAirports - 2, airports, generateUniqueColor(noofAeroplanes, aer)));
        }

        while (running) {
            //all code;

            time = ((float) (System.nanoTime() / 10000000) - (float) start) / 10;
            
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