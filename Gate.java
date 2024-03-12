import java.util.Random;

public class Gate {
  public int id;
  private boolean occupied = false;
  private float serviceTime = 0;
  private float countdown;



  public Gate(int id) {
    this.serviceTime = new Random().nextInt(10, 30);
    this.id = id;

  }

  public void update(float dt) {
    
  }
}
