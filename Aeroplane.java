import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class Aeroplane {
  int id;
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

  // gets the next airport after just landing etc.
  public void getNextAirportInRoute() {
    route.addLast(route.removeFirst());
  }
}
