import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.awt.Color;
import java.awt.geom.Point2D;

public class Aeroplane {
  Random rand = new Random();
  int id;
  int speed = rand.nextInt(3, 6);
  String state = "flying";
  float distance;
  float landingTime;
  float serviceTime;
  float takeOffTime;
  float flightTime;
  Gate gate;
  Runway runway;
  Color color;
  int size = 10;
  Airport currentAirport;


  Point2D.Float location = new Point2D.Float(rand.nextFloat(800), rand.nextFloat(800)); //creates random location.
  //linked list because micke said so, also should be able to check if i can land etc, 
  //call all the functuions from here: like route.get(0).isrunwayavailbiel.
  LinkedList<Airport> route = new LinkedList<>();
  public Aeroplane(int id, int routeLenght, ArrayList<Airport> airports, Color color) {
    this.id = id;
    this.color = color;
    generateRoute(routeLenght, airports);
    this.distance = distance(location, route.get(0).location);
  }
  
  //creates a random route for the airplane to follow, then it loops it.
  public String getTimeRemaining() {
    if (state == "waitingToLand" || state == "waitingToTakeOff") {
      String queue = String.format("Queue: %s/%s", route.get(0).planesWaitingRunway.indexOf(this) + 1, route.get(0).planesWaitingRunway.size());
      return queue;
    } else  if (state == "waitingToService") {
      String queue = String.format("Queue: %s/%s", route.get(0).planesWaitingGate.indexOf(this) + 1, route.get(0).planesWaitingGate.size());
      return queue;
    }
    String time = String.format("Time Left: %.1f", Math.max(Math.max(landingTime, flightTime), Math.max(serviceTime, takeOffTime))) + "s";
    return time;
  }
  public void setLandingTime() {
    landingTime = rand.nextFloat(15, 20);
  }
  public void setServiceTime() {
    serviceTime = rand.nextFloat(23, 35);
  }
  public void setTakeOffTime() {
    takeOffTime = rand.nextFloat(14, 23);
  }
  public float distance(Point2D.Float curLoc, Point2D.Float tarLoc) {
    return (float) Point2D.distance(curLoc.x, curLoc.y, tarLoc.x, tarLoc.y);
  }

  public void generateRoute(int routeLenght,  ArrayList<Airport> airports) {
    if (routeLenght > airports.size() || routeLenght <= 0) {
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
    currentAirport = route.get(0);
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
        flightTime = distance / speed;
        location.setLocation(newX, newY);

        if (distance <= 2) {
          flightTime = 0;
          distance = 0;
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
        landingTime -= dt;
        location = new Point2D.Float(currentAirport.runwayLocation.x - size/1.9f, currentAirport.runwayLocation.y - size - (size * runway.id));
        if (landingTime <= 0) {
          landingTime = 0;
          runway.setOccupied(false);
          route.get(0).moveRunwayQueue(runway);
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
        serviceTime -= dt;
        location = new Point2D.Float(currentAirport.gateLocation.x - size/1.9f, currentAirport.gateLocation.y + (size * gate.id));
        if (serviceTime <= 0) {
          serviceTime = 0;
          gate.setOccupied(false);
          route.get(0).moveGateQueue(gate);
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
      location = new Point2D.Float(currentAirport.runwayLocation.x - size/1.9f, currentAirport.runwayLocation.y - size - (size * runway.id));
      takeOffTime -= dt;
        
        if (takeOffTime <= 0) {
          takeOffTime = 0;
          runway.setOccupied(false);
          route.get(0).moveRunwayQueue(runway);
          getNextAirportInRoute();
          state = "flying";
        }
      break;

      case "waitingToLand":  //runway
        location = new Point2D.Float(currentAirport.runwayQueueLocation.x - size, currentAirport.runwayQueueLocation.y - size/2 + (size * currentAirport.planesWaitingRunway.indexOf(this)));

      break;

      case "waitingToTakeOff": //runway
        location = new Point2D.Float(currentAirport.runwayQueueLocation.x - size, currentAirport.runwayQueueLocation.y - size/2 + (size * currentAirport.planesWaitingRunway.indexOf(this)));


      break;

      case "waitingToService": //gate
        location = new Point2D.Float(currentAirport.gateQueueLocation.x, currentAirport.gateQueueLocation.y - size/2 - (size * currentAirport.planesWaitingGate.indexOf(this)));


      break;
    }

  }
}
