public class Gate {
  public int id;
  private boolean occupied = false;

  public Gate(int id) {
      this.id = id;
  }
  
  public boolean isOccupied() {
      return occupied;
  }

  public void setOccupied(boolean newOccupied) {
      this.occupied = newOccupied;
  }
}


