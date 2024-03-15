import java.util.ArrayList;
import java.awt.geom.Point2D;
import java.util.LinkedList;

public class Airport {
    Point2D.Float location = new Point2D.Float();
    int id;
    ArrayList<Runway> runways = new ArrayList<>();
    ArrayList<Gate> gates = new ArrayList<>();
    LinkedList<Aeroplane> planesWaitingRunway = new LinkedList<>();
    LinkedList<Aeroplane> planesWaitingGate = new LinkedList<>();
    int size = 20;



    public Airport(int noofRunways, int noofGates, int id, Point2D.Float location) {
      this.id = id;
      this.location = location;

      //creates runways
      for (int i = 0; i < noofRunways; i++) {
        runways.add(new Runway(i));
      }
      //creates gates
      for (int i = 0; i < noofGates; i++) {
        gates.add(new Gate(i));
      }
    }

//    public void checkRunwayQueue() {
//      for (Runway runway : runways) {
//        if (!runway.isOccupied()) {
//          if (planesWaitingRunway.size() > 0) {
//            runway.setOccupied(true);
//            planesWaitingRunway.getFirst().runway = runway;
//            if (planesWaitingRunway.getFirst().state == "waitingToLand") {planesWaitingRunway.getFirst().state = "landing"; planesWaitingRunway.getFirst().setLandingTime();}
//            if (planesWaitingRunway.getFirst().state == "waitingToTakeOff") {planesWaitingRunway.getFirst().state = "takingOff"; planesWaitingRunway.getFirst().setTakeOffTime();}
//            planesWaitingRunway.removeFirst();
//          }
//        }
//      }
//    }

//    public void checkGateQueue() {
//      for (Gate gate : gates) {
//        if (!gate.isOccupied()) {
//          if (planesWaitingGate.size() > 0) {
//            gate.setOccupied(true);
//            planesWaitingGate.getFirst().gate = gate;
//            if (planesWaitingGate.getFirst().state == "waitingToService") {planesWaitingGate.getFirst().state = "service"; planesWaitingGate.getFirst().setServiceTime();}
//            planesWaitingGate.removeFirst();
//          }
//        }
//      }
//    }

    public void moveRunwayQueue(Runway runway) {
      if (planesWaitingRunway.size() > 0) {
        runway.setOccupied(true);
        planesWaitingRunway.getFirst().runway = runway;
        if (planesWaitingRunway.getFirst().state == "waitingToLand") {planesWaitingRunway.getFirst().state = "landing"; planesWaitingRunway.getFirst().setLandingTime();}
        if (planesWaitingRunway.getFirst().state == "waitingToTakeOff") {planesWaitingRunway.getFirst().state = "takingOff"; planesWaitingRunway.getFirst().setTakeOffTime();}
        planesWaitingRunway.removeFirst();
      }
    }

    public void moveGateQueue(Gate gate) {
      if (planesWaitingGate.size() > 0) {
        gate.setOccupied(true);
        planesWaitingGate.getFirst().gate = gate;
        if (planesWaitingGate.getFirst().state == "waitingToService") {planesWaitingGate.getFirst().state = "service"; planesWaitingGate.getFirst().setServiceTime();}
        planesWaitingGate.removeFirst();
      }
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