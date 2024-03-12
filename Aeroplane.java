import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.awt.geom.Point2D;

public class Aeroplane {
  Random rand = new Random();
  int id;
  int speed = rand.nextInt(5, 8);
  boolean isflying = true;
  public Point2D.Float location = new Point2D.Float(rand.nextFloat(800), rand.nextFloat(800)); //creates random location.
  //linked list because micke said so, also should be able to check if i can land etc, 
  //call all the functuions from here: like route.get(0).isrunwayavailbiel.
  LinkedList<Airport> route = new LinkedList<>();
  public Aeroplane(int id, int routeLenght, ArrayList<Airport> airports) {
    this.id = id;
    generateRoute(routeLenght, airports);
  }
  
  //creates a random route for the airplane to follow, then it loops it.
  public void generateRoute(int routeLenght,  ArrayList<Airport> airports) {
    if (routeLenght > airports.size()) {
      routeLenght = airports.size();
    }
    Collections.shuffle(airports);
    for (Airport airport : airports) {
      if (route.size() < routeLenght) {
        route.add(airport);
      }
    }
  }

  public void getNextAirportInRoute() {
    route.addLast(route.removeFirst());
  }
  public void update(double dt) {
  }
}
