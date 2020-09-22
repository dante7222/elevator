package com.introlab_systems.passenger;

import com.introlab_systems.building.Building;
import com.introlab_systems.building.Floor;
import com.introlab_systems.elevator.Elevator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PassengerTest {

    Building building;
    List<Floor> floors;
    Elevator elevator;
    Floor firstFloor;

    @BeforeEach
    void setUp() {
        building = new Building(5, 5);
        building.getFloors()
                .removeAll(building.getFloors().subList(4, building.getFloors().size() - 1));
        floors = building.getFloors();
        firstFloor = floors.get(0);
        firstFloor.setElevator(building.getElevator());
        elevator = building.getElevator();
        firstFloor.getPassengers().add(new Passenger(firstFloor));
        firstFloor.getPassengers().forEach(passenger -> {
            passenger.generateDestinationFloor();
            passenger.setDirection();
        });
    }

    @Test
    void testSetDirection() {
        assertThat(firstFloor.getPassengers().peek().getDirection()).isEqualTo("up");

        assertThat(firstFloor.getPassengers().peek().getDestinationFloorNumber()).isGreaterThan(1);
    }

    @Test
    void shouldLoadToElevator() {

        int size = firstFloor.getPassengers().size();
        firstFloor.getPassengers().peek().loadToElevator();

        assertThat(firstFloor.getPassengers().size()).isLessThan(size);
        assertThat(elevator.getPassengersCabin().size()).isGreaterThan(0);
    }
}
