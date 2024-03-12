import java.util.ArrayList;
import java.awt.geom.Point2D;

public class Airport {
    public Point2D.Float location = new Point2D.Float();
    public int id;
    public ArrayList<Runway> runways = new ArrayList<>();
    public ArrayList<Gate> gates = new ArrayList<>();


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

    public boolean isRunwayAvailable() {
      boolean available = false;
      

      return available;
    }

    public void update(float dt) {
      for (Runway runway : runways) {
        runway.update(dt);
      }
      for (Gate gate : gates) {
        gate.update(dt);
      }
    }
  }