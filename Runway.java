public class Runway {
    public int id;
    private boolean occupied = false;
    private float landTime = 10;
    private float countdown;

    public Runway(int id) {
        this.id = id;
    }
    
    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean newOccupied) {
        this.occupied = newOccupied;
        countdown = landTime;
    }

    public void update(float dt) {
        if (occupied) {
            countdown -= dt;
            if (countdown <= 0) {
                occupied = false;
            }
        }
    }

}


