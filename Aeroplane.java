import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.awt.geom.Point2D;

public class Aeroplane {
  boolean watching = false;
  Random rand = new Random();
  int id;
  int speed = rand.nextInt(15, 25);
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
    landingTime = rand.nextFloat(2, 4);
  }
  public void setServiceTime() {
    serviceTime = rand.nextFloat(3, 4);
  }
  public void setTakeOffTime() {
    takeOffTime = rand.nextFloat(4, 6);
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
    route.addLast(route.removeFirst());
    distance = distance(location, route.get(0).location);

  }

  public void update(float dt) {
    if (watching) {
      System.out.printf("\nPlane number %s is: ", id);
    }
    switch(state) {
      case "flying":
        // Calculate the angle towards the next airport
        float angle = (float) Math.atan2(route.get(0).location.y - location.y, route.get(0).location.x - location.x);

        // Calculate the distance to move
        float distanceToMove = dt * speed;
        distanceToMove = Math.min(distanceToMove, distance);

        // Update the distance remaining
        distance -= distanceToMove;

        // Calculate the new position
        float newX = (float) (location.x + distanceToMove * Math.cos(angle));
        float newY = (float) (location.y + distanceToMove * Math.sin(angle));

        // Update the location
        location.setLocation(newX, newY);
        if (watching) {
          System.out.printf("\nFlying - Towards airport nmr %s\nDistance: %skm, ID: %s DT: %s\n", route.get(0).id, (int) distance, id, dt);
        }
        if (distance < 10) {
          Object[] result = route.get(0).isRunwayAvailable(this);
          runway = (Runway) result[0];
          if((boolean) result[1]) {
            state = "landing";
            setLandingTime();

          } else {
            state = "waitingToLand";
          }
        }
        break;

      case "landing":
        if (watching) {
          System.out.printf("\nLanding\nTime left: %.1fs", landingTime);

        }
        landingTime -= dt;
        if (landingTime <= 0) {
          runway.setOccupied(false);
          route.get(0).checkRunwayQueue();
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
          System.out.printf("\nService\nTime left: %.1fs", serviceTime);
        }
        serviceTime -= dt;
        if (serviceTime <= 0) {
          gate.setOccupied(false);
          route.get(0).checkGateQueue();
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
        System.out.printf("\nTaking Off\nTime left: %.1fs", takeOffTime);
      }
        takeOffTime -= dt;
        if (takeOffTime <= 0) {
          runway.setOccupied(false);
          getNextAirportInRoute();
          state = "flying";
        }
      break;

      case "waitingToLand":  //runway
      if (watching) {
       System.out.printf("\nWaiting for landing slot\nPlace in Queue: %s/%s", route.get(0).planesWaitingRunway.indexOf(this) + 1,route.get(0).planesWaitingRunway.size());
      }
      

      break;

      case "waitingToTakeOff": //runway
      if (watching) {
        System.out.printf("\nWaiting for take of slot\nPlace in Queue: %s/%s", route.get(0).planesWaitingRunway.indexOf(this) + 1,route.get(0).planesWaitingRunway.size());
      }

      break;

      case "waitingToService": //gate
      if (watching) {
        System.out.printf("\nWaiting for service slot\nPlace in Queue: %s/%s", route.get(0).planesWaitingGate.indexOf(this) + 1,route.get(0).planesWaitingGate.size());
      }

      break;
    }

  }
}
