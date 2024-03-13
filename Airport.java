import java.util.ArrayList;
import java.awt.geom.Point2D;
import java.util.LinkedList;

public class Airport {
    public Point2D.Float location = new Point2D.Float();
    public int id;
    public ArrayList<Runway> runways = new ArrayList<>();
    public ArrayList<Gate> gates = new ArrayList<>();
    public LinkedList<Aeroplane> planesWaitingRunway = new LinkedList<>();
    public LinkedList<Aeroplane> planesWaitingGate = new LinkedList<>();



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

    public void update(float dt) {
    }
  }