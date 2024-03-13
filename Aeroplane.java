import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.awt.geom.Point2D;

public class Aeroplane {
  boolean watching = false;
  Random rand = new Random();
  int id;
  int speed = rand.nextInt(5, 8);
  String state = "flying";
  float distance;
  float landingTime;
  float serviceTime;
  float takeOffTime;
  Gate gate;
  Runway runway;

  Point2D.Float location = new Point2D.Float(rand.nextFloat(800), rand.nextFloat(800)); //creates random location.
  //linked list because micke said so, also should be able to check if i can land etc, 
  //call all the functuions from here: like route.get(0).isrunwayavailbiel.
  LinkedList<Airport> route = new LinkedList<>();
  public Aeroplane(int id, int routeLenght, ArrayList<Airport> airports) {
    this.id = id;
    generateRoute(routeLenght, airports);
    this.distance = distance(location, route.get(0).location);
  }
  
  //creates a random route for the airplane to follow, then it loops it.
  public void setLandingTime() {
    landingTime = rand.nextFloat(3, 7);
  }
  public void setServiceTime() {
    serviceTime = rand.nextFloat(5, 8);
  }
  public void setTakeOffTime() {
    takeOffTime = rand.nextFloat(6, 9);
  }
  public float distance(Point2D.Float curLoc, Point2D.Float tarLoc) {
    return (float) Point2D.distance(curLoc.x, curLoc.y, tarLoc.x, tarLoc.y);
  }

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
  private void getNextAirportInRoute() {
    location = route.get(0).location;
    route.addLast(route.removeFirst());
    distance = distance(location, route.get(0).location);

  }

  public void update(float dt) {
    switch(state) {
      case "flying":
        distance -= dt * speed * 20;
        if (watching) {
          System.out.printf("\nFlying - Towards airport nmr %s\nDistance: %skm, ID: %s DT: %s", route.get(0).id, (int) distance, id, dt);
        }
        if (distance < 10) {
          Object[] result = route.get(0).isRunwayAvailable(this);
          runway = (Runway) result[0];
          if((boolean) result[1]) {
            state = "landing";
            location = route.get(0).location;
            setLandingTime();

          } else {
            state = "waitingToLand";
          }
        }
        break;

      case "landing":
        if (watching) {
          System.out.printf("\nLanding\nTime left: %.1fs ID: %s", landingTime, id);

        }
        landingTime -= dt;
        if (landingTime <= 0) {
          runway.setOccupied(false);
          Object[] result = route.get(0).isGateAvailable(this);
          gate = (Gate) result[0];
          if ((boolean) result[1]) {
            state = "service";
            setServiceTime();
          } else {
            state = "waitingToService";
          }
        }
        break;

      case "service":
        if (watching) {
          System.out.printf("\nService\nTime left: %.1fs ID: %s", serviceTime, id);
        }
        serviceTime -= dt;
        if (serviceTime <= 0) {
          gate.setOccupied(false);
          Object[] result = route.get(0).isRunwayAvailable(this);
          runway = (Runway) result[0];
          if ((boolean) result[1]) {
            state = "takingOff";
            setTakeOffTime();
          } else {
            state = "waitingToTakeOff";
          }
        }
      break;

      case "takingOff":
      if (watching) {
        System.out.printf("\nTaking Off\nTime left: %.1fs ID: %s", takeOffTime, id);
      }
        takeOffTime -= dt;
        if (takeOffTime <= 0) {
          getNextAirportInRoute();
          state = "flying";
        }
      break;

      case "waitingToLand":  //runway
      if (watching) {
       System.out.printf("\nWaiting for landing slot\nPlace in Queue: %s ID: %s", route.get(0).planesWaitingRunway.indexOf(this), id);
      }
      

      break;

      case "waitingToTakeOff": //runway
      if (watching) {
        System.out.printf("\nWaiting for take of slot\nPlace in Queue: %s ID: %s", route.get(0).planesWaitingRunway.indexOf(this), id);
      }

      break;

      case "waitingToService": //gate
      if (watching) {
        System.out.printf("\nWaiting for service slot\nPlace in Queue: %s ID: %s", route.get(0).planesWaitingGate.indexOf(this), id);
      }

      break;
    }

  }
}
