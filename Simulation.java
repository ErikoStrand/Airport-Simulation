import java.util.ArrayList;
import java.util.Random;
import java.awt.geom.Point2D;


public class Simulation {
    public static ArrayList<Airport> airports = new ArrayList<>();
    public static ArrayList<Aeroplane> aeroplanes = new ArrayList<>();

    public static int noofAirports = 4;
    public static int noofAeroplanes = 10;

    static Point2D.Float generateLocation(ArrayList<Airport> airports, int width, int height) {
        int distanceBetween = 20;
        Random random = new Random();
        Point2D.Float location = new Point2D.Float(random.nextFloat() * width, random.nextFloat() * height);
        if (airports.size() <= 0) {
            return location;
        } else {
            Point2D.Float newLocation = new Point2D.Float(random.nextFloat() * width, random.nextFloat() * height);

            for (Airport airport : airports) {
                if (Point2D.distance(newLocation.x, newLocation.y, airport.location.x, airport.location.y) <= distanceBetween) {
                    return generateLocation(airports, width, height); // Recursive call
                }
            }

            return newLocation; // Return newLocation if it doesn't violate the condition
        }
    }
    
    public static void main(String[] args) {
        float time = 0;
        int width = 800;
        int height = 800;
        boolean running = true;
        double start = System.nanoTime() /100000000;


        //creates all airports and adds to array list
        for (int air = 0; air < noofAirports; air++) {
            airports.add(new Airport(4, 8, air, generateLocation(airports, width, height)));
        }
        //creates all aeroplanes and adds to array list
        for (int aer = 0; aer < noofAeroplanes; aer++) {
            aeroplanes.add(new Aeroplane(aer, 3, airports));
        }
        
        for (Airport airport : airports) {
            System.out.println(airport.location);
        }

        while (running) {
            //all code;
            time = ((float) (System.nanoTime() / 100000000) - (float) start) / 10;



            for (Airport airport : airports) {
            }
            for (Aeroplane aeroplane : aeroplanes) {
                if (aeroplane.id == 0) {
                }
            }
            System.out.println(time);
        }
    }
}