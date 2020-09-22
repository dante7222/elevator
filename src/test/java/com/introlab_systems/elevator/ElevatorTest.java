package com.introlab_systems.elevator;

import com.introlab_systems.building.Building;
import com.introlab_systems.building.Floor;
import com.introlab_systems.passenger.Passenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NavigableSet;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;

class ElevatorTest {

    Building building;
    Elevator elevator;
    Floor firstFloor;
    Floor lastFloor;
    Queue<Passenger> firstFloorPassengers;
    Queue<Passenger> lastFloorPassengers;

    @BeforeEach
    void setUp() {
        building = new Building(5, 5);

        building.getFloors()
                .removeAll(building.getFloors().subList(4, building.getFloors().size() - 1));

        building.getFloors().forEach(floor -> floor.getPassengers()
                .forEach(passenger -> {
                    passenger.generateDestinationFloor();
                    passenger.setDirection();
                }));

        elevator = building.getElevator();
        firstFloor = building.getFloors().get(0);
        lastFloor = building.getFloors().get(4);

        firstFloorPassengers = firstFloor.getPassengers();
        lastFloorPassengers = lastFloor.getPassengers();

        firstFloorPassengers.clear();
        lastFloorPassengers.clear();

        for (int i = 0; i < 6; i++) {
            firstFloorPassengers.add(new Passenger(firstFloor));
        }

        for (int i = 0; i < 6; i++) {
            lastFloorPassengers.add(new Passenger(lastFloor));
        }

        firstFloorPassengers.forEach(passenger -> {
            passenger.generateDestinationFloor();
            passenger.setDirection();
        });

        lastFloorPassengers.forEach(passenger -> {
            passenger.generateDestinationFloor();
            passenger.setDirection();
        });
    }

    @Test
    void shouldMoveUpAndDown() {
        elevator.moveToFloor(5);

        assertThat(elevator.getCurrentFloor())
                .isEqualTo(building.getFloors().get(4));

        elevator.moveToFloor(1);
        System.out.println(elevator.getCurrentFloor().getFloorNumber());
        assertThat(elevator.getCurrentFloor())
                .isEqualTo(building.getFloors().get(0));
    }

    @Test
    void shouldMoveRelativelyToCurrentFloor() {
        assertThat(elevator.getCurrentFloor().getFloorNumber()).isEqualTo(firstFloor.getFloorNumber());

        elevator.moveRelativelyToCurrentFloor();

        assertThat(elevator.getCurrentFloor().getFloorNumber()).isEqualTo(2);

        elevator.moveToFloor(5);

        assertThat(elevator.getCurrentFloor().getFloorNumber()).isEqualTo(5);

        elevator.moveRelativelyToCurrentFloor();

        assertThat(elevator.getCurrentFloor().getFloorNumber()).isEqualTo(4);
    }

    @Test
    void shouldLoadPassengers() {
        assertThat(firstFloor.getPassengers().size()).isEqualTo(6);

        assertThat(elevator.getPassengersCabin().size()).isZero();


        elevator.loadPassengers();

        assertThat(elevator.getPassengersCabin().size()).isEqualTo(5);

        assertThat(firstFloor.getPassengers().size()).isEqualTo(1);
    }

    @Test
    void shouldLoadAndUnloadPassengers() {

        elevator.loadPassengers();

        assertThat(elevator.getPassengersCabin().size()).isEqualTo(5);

        elevator.calculateDestination();

        NavigableSet<Integer> passengersDestinations = elevator.getDestinations();

        elevator.moveToFloor(passengersDestinations.pollFirst());

        final int floorSizeBefore = elevator.getCurrentFloor().getPassengers().size();

        elevator.unloadPassengers();

        final int floorSizeAfter = elevator.getCurrentFloor().getPassengers().size();

        assertThat(elevator.getPassengersCabin().size()).isLessThan(5);

        assertThat(floorSizeAfter).isGreaterThan(floorSizeBefore);

    }

    @Test
    void testOneWorkToTop() {
        elevator.loadPassengers();

        elevator.calculateDestination();

        NavigableSet<Integer> passengersDestinations = elevator.getDestinations();

        while (!passengersDestinations.isEmpty()) {
            elevator.moveToFloor(passengersDestinations.pollFirst());
            elevator.unloadPassengers();
        }

        assertThat(elevator.getPassengersCabin().size()).isZero();
    }

    @Test
    void testWorkInBothDirections() {
        elevator.loadEmptyElevator();

        elevator.calculateDestination();

        NavigableSet<Integer> passengersDestinations = elevator.getDestinations();

        while (!passengersDestinations.isEmpty()) {
            elevator.moveToFloor(passengersDestinations.pollFirst());
            elevator.unloadPassengers();
        }
        if (elevator.getCurrentFloor().getFloorNumber() != 5) {
            elevator.moveToFloor(5);
        }

        assertThat(elevator.getPassengersCabin().size()).isZero();

        elevator.loadEmptyElevator();

        assertThat(elevator.getPassengersCabin().size()).isEqualTo(5);

        elevator.calculateDestination();

        int lowestFloor = 0;

        while (!passengersDestinations.isEmpty()) {
            lowestFloor = passengersDestinations.pollLast();
            elevator.moveToFloor(lowestFloor);
            elevator.unloadPassengers();
        }

        assertThat(elevator.getPassengersCabin().size()).isZero();
        assertThat(elevator.getCurrentFloor().getFloorNumber()).isEqualTo(lowestFloor);
    }

    @Test
    void shouldFindBiggestDirectionOnFloorAndReverse() {
        assertThat(elevator.getCurrentDirection()).isEqualTo("up");
        elevator.biggestDestinationOnFloor();
        assertThat(elevator.getCurrentDirection()).isEqualTo("up");

        elevator.moveToFloor(5);
        elevator.biggestDestinationOnFloor();
        assertThat(elevator.getCurrentDirection()).isEqualTo("down");
    }
}
