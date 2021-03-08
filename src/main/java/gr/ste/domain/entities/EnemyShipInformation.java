package gr.ste.domain.entities;

import javafx.geometry.Orientation;

import java.util.*;

public class EnemyShipInformation {
    private final List<Position> shipLocations;

    public EnemyShipInformation(Position shipLocation) {
        this.shipLocations = new ArrayList<>();
        shipLocations.add(shipLocation);
    }

    public EnemyShipInformation(List<Position> shipLocations) {
        this.shipLocations = shipLocations;
    }

    public void addShipLocation(Position shipPosition) {
        shipLocations.add(shipPosition);
    }

    public List<Position> getShipLocations() {
        return shipLocations;
    }

    public List<Position> getExtremeLocations() {
        List<Position> extremePositions = new ArrayList<>();
        Position minPosition = shipLocations.get(0);
        Position maxPosition = shipLocations.get(0);
        for(Position knownLocation : shipLocations) {
            if(isOrientationDiscovered().isPresent()) {
                Orientation orientation = isOrientationDiscovered().get();
                if(orientation == Orientation.HORIZONTAL) {
                    if(knownLocation.getX() < minPosition.getX()) {
                        minPosition = knownLocation;
                    }
                    if(knownLocation.getX() > maxPosition.getX()) {
                        maxPosition = knownLocation;
                    }
                } else {
                    if(knownLocation.getY() < minPosition.getY()) {
                        minPosition = knownLocation;
                    }
                    if(knownLocation.getY() > minPosition.getY()) {
                        maxPosition = knownLocation;
                    }
                }
            } else {
                break;
            }
        }
        extremePositions.add(minPosition);
        extremePositions.add(maxPosition);
        return extremePositions;
    }

    public Optional<Orientation> isOrientationDiscovered() {
        if(shipLocations.size() > 1) {
            if(shipLocations.get(0).getX() == shipLocations.get(shipLocations.size() - 1).getX()) {
                return Optional.of(Orientation.VERTICAL);
            } else {
                return Optional.of(Orientation.HORIZONTAL);
            }
        } else {
            return Optional.empty();
        }
    }
}
