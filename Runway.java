public class Runway {
    public int id;
    private boolean occupied = false;

    public Runway(int id) {
        this.id = id;
    }
    
    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean newOccupied) {
        this.occupied = newOccupied;
    }
}


