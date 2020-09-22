package com.introlab_systems.building;

import com.introlab_systems.elevator.Elevator;
import com.introlab_systems.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public final class Building {
    private final static List<Floor> FLOORS = new ArrayList<>();
    private final Elevator elevator;

    public static int getFloorsAmount() {
        return FLOORS.size();
    }

    public List<Floor> getFloors() {
        return FLOORS;
    }

    public Building(int minFloor, int maxFloor) {
        generateFloors(minFloor, maxFloor);
        FLOORS.forEach(Floor::generatePassengersOnFloor);
        elevator = new Elevator(FLOORS);
    }

    private void generateFloors(int minFloor, int maxFloor) {
        int totalFloors = Utils.generateAmount(minFloor, maxFloor);

        for (int i = 1; i <= totalFloors; i++) {
            FLOORS.add(new Floor(i));
        }
    }

    public Elevator getElevator() {
        return elevator;
    }
}
