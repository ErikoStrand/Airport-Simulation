import java.util.ArrayList;
import java.awt.geom.Point2D;
import java.util.LinkedList;

public class Airport {
    Point2D.Float location = new Point2D.Float();
    Point2D.Float gateLocation = new Point2D.Float();
    Point2D.Float runwayLocation = new Point2D.Float();
    Point2D.Float gateQueueLocation = new Point2D.Float();
    Point2D.Float runwayQueueLocation = new Point2D.Float();
    Point2D.Float locationCenter = new Point2D.Float();


    int id;
    ArrayList<Runway> runways = new ArrayList<>();
    ArrayList<Gate> gates = new ArrayList<>();
    LinkedList<Aeroplane> planesWaitingRunway = new LinkedList<>();
    LinkedList<Aeroplane> planesWaitingGate = new LinkedList<>();
    int size = 20;



    public Airport(int noofRunways, int noofGates, int id, Point2D.Float location) {
      this.id = id;
      this.location = location;
      this.locationCenter = new Point2D.Float(location.x, location.y);
      this.runwayLocation = new Point2D.Float(location.x + size / 2, location.y);
      this.gateLocation = new Point2D.Float(location.x + size / 2, location.y + size);
      this.gateQueueLocation = new Point2D.Float(location.x + size, location.y + size / 2);
      this.runwayQueueLocation = new Point2D.Float(location.x, location.y + size/2);
      //creates runways
      for (int i = 0; i < noofRunways; i++) {
        runways.add(new Runway(i));
      }
      //creates gates
      for (int i = 0; i < noofGates; i++) {
        gates.add(new Gate(i));
      }
    }
    public int getNoofAeroplanes() {
      int amt = 0;
      amt += planesWaitingGate.size();
      amt += planesWaitingRunway.size();
      for (Runway runway : runways) {
        if (runway.isOccupied()) {amt++;}
      }
      for (Gate gate : gates) {
        if (gate.isOccupied()) {amt++;}
      }
      return amt;
    }


    public void moveRunwayQueue(Runway runway) {
      //checks so the list is not empty.
      if (planesWaitingRunway.size() > 0) {
        runway.setOccupied(true);
        //sätter runwayen på planet så den är samma som det planet som nyss stack. så det liksom blir att man byter runwayen mellan varandra.
        planesWaitingRunway.getFirst().runway = runway;
        if (planesWaitingRunway.getFirst().state == "waitingToLand") {planesWaitingRunway.getFirst().state = "landing"; planesWaitingRunway.getFirst().setLandingTime();}
        if (planesWaitingRunway.getFirst().state == "waitingToTakeOff") {planesWaitingRunway.getFirst().state = "takingOff"; planesWaitingRunway.getFirst().setTakeOffTime();}
        planesWaitingRunway.removeFirst();
      }
    }

    public void moveGateQueue(Gate gate) {
      //same thing as the runway one.
      if (planesWaitingGate.size() > 0) {
        gate.setOccupied(true);
        planesWaitingGate.getFirst().gate = gate;
        if (planesWaitingGate.getFirst().state == "waitingToService") {planesWaitingGate.getFirst().state = "service"; planesWaitingGate.getFirst().setServiceTime();}
        planesWaitingGate.removeFirst();
      }
    }
    public int indexOfTakeOff(Aeroplane plane) {
      int index = 0;
      for (Aeroplane p : planesWaitingRunway) {
          if (p.state == "waitingToTakeOff") {
            //found the index of the plane
              if (p.equals(plane)) {
                  return index;
              }
              //not correct plane so add ++ to index.
              index++;
          }
      }
      return -1; // Return -1 if the plane is not found or not waiting to take off
  }
    public Object[] isRunwayAvailable(Aeroplane aeroplane) {
      for (Runway runway : runways) {
        if (!runway.isOccupied()) {
            runway.setOccupied(true);
            return new Object[]{runway, true};
          }
        }
        //means all runways are taken, plane should wait.
        planesWaitingRunway.add(aeroplane);
        return new Object[]{null, false};
      } 
    
      public Object[] isGateAvailable(Aeroplane aeroplane) {
        for (Gate gate : gates) {
          if (!gate.isOccupied()) {
            gate.setOccupied(true);
            return new Object[]{gate, true};
            }
          }
          //means all runways are taken, plane should wait.
          planesWaitingGate.add(aeroplane);
          return new Object[]{null, false};
        } 
  }