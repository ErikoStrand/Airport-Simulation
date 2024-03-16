import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.*;
import java.awt.geom.Ellipse2D;

class SimulationPanel extends JPanel {


   private Aeroplane selectedAeroplane = null; // Track the selected aeroplane

    public SimulationPanel() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Determine if an aeroplane was clicked
                for (Aeroplane aeroplane : Simulation.aeroplanes) {
                    Ellipse2D.Float aeroplaneShape = new Ellipse2D.Float(
                        aeroplane.location.x, aeroplane.location.y, aeroplane.size * 2, aeroplane.size * 2);
                        if (aeroplaneShape.contains(e.getPoint())) {
                          selectedAeroplane = aeroplane;
                          return;
                      }
                }
                // If no aeroplane was clicked, clear the selection
                selectedAeroplane = null;
                Simulation.infoLabel.setText("Selected Plane: None (click one)"); // Clear the label
                repaint();
            }
        });
    }

  @Override
  protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      Graphics2D g2d = (Graphics2D) g;

      // Draw airports
      for (Airport airport : Simulation.airports) {
          drawAirport(g2d, airport);
      }

      // Draw aeroplanes
      for (Aeroplane aeroplane : Simulation.aeroplanes) {
          drawAeroplane(g2d, aeroplane);
      }

      // draw a highlight so you know which plane is selected.
      if (selectedAeroplane != null) {
        String infoText = "<html> Selected Plane <b>" + selectedAeroplane.id 
        + "<br>State: <b>" + selectedAeroplane.state 
        + "<br>" + selectedAeroplane.getTimeRemaining()
        + "<br>Distance Left: <b>" + (int) selectedAeroplane.distance + "km"
        + "</b></html>";
        Simulation.infoLabel.setText(infoText);
        repaint();
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(3.0f));
        g2d.draw(new Ellipse2D.Float(selectedAeroplane.location.x - 1.5f, selectedAeroplane.location.y - 1.5f, selectedAeroplane.size + 3, selectedAeroplane.size + 3));
      }
      }

  private void drawAirport(Graphics2D g2d, Airport airport) {
      // Draw a small circle for the airport
      g2d.setColor(new Color(10, 10, 10));
      g2d.fillRect((int) airport.location.x, (int) airport.location.y, airport.size, airport.size);
  }

  private void drawAeroplane(Graphics2D g2d, Aeroplane aeroplane) {
      // Draw a small circle for the aeroplane
      g2d.setColor(aeroplane.color);
      g2d.fill(new Ellipse2D.Float(aeroplane.location.x, aeroplane.location.y, aeroplane.size, aeroplane.size));

      //g2d.setColor(Color.BLACK);
      //g2d.drawString(String.valueOf(aeroplane.id), aeroplane.location.x + aeroplane.size, aeroplane.location.y + aeroplane.size);
  }
}