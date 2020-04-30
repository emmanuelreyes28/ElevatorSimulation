import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

//Emmanuel Reyes 58725927
//Thomas Tai Nguyen 61879630

public class Elevator implements Runnable
{
	private int elevatorID; // The thread elevator ID
	private int currentFloor; // The floor its currently on
	private int numPassengers; // How many passengers are inside right now 
	private int totalLoadedPassengers; // How many total passengers the elevator has loaded in the simulation
	private int totalUnloadedPassengers; // How many total passengers the elevator has UNloaded in the simulation
	private ArrayList<ElevatorEvent> moveQueue; // The ArrayList of ElevatorEvents for the current elevator
 	private int[] passengerDestination; // An integer array representing how many passegners want to go to the ith floor
	private BuildingManager manager; // The building manager object that is shared across all Elevators
	
	// Constructor
	public Elevator(int elevatorID, BuildingManager manager)
	{
		this.elevatorID = elevatorID;
		this.manager = manager;
		this.currentFloor = 0;
		this.passengerDestination = new int[5];
		moveQueue = new ArrayList<ElevatorEvent>();
	}
	
	// Calculates the ETA of getting to a floor without considering other passenger's load/unload.
	private int calculateBaseETA(int floor)
	{
		return (Math.abs(this.currentFloor - floor) * 5) + 10 + SimClock.getTime();
	}
	
	// Calculates the ETA of getting to a floor considering other passenger's load/unload as well.
	private int calculateETA(int floor, boolean ascending)
	{
		int result = calculateBaseETA(floor);
		if (ascending)
		{
			for (int i = currentFloor+1; i != floor; i++) // floor 1, and floor 4
			{
				for (ElevatorEvent moveEvent : moveQueue)
				{
					if (moveEvent.getDestination() == i)
					{
						result += 10; // Other passengers need time to unload before you reach your destination!
					}
				}
			}
		}
		else
		{
			for (int i = currentFloor-1; i != floor; i--)
			{
				for (ElevatorEvent moveEvent : moveQueue)
				{
					if (moveEvent.getDestination() == i)
					{
						result += 10; // Other passengers need time to unload before you reach your destination!
					}
				}
			}
		}
		return result;
	}
	
	// Unload passengers and update the correct variables and statistics.
	private void unloadPassengers()
	{
		IO.printEvent(SimClock.getTime(), "ElevatorUnloadEvent", "(Elevator " + this.elevatorID + ") Reached Floor " + currentFloor + "; now unloading " + passengerDestination[currentFloor] + " passengers.");
		
		numPassengers -= passengerDestination[currentFloor]; // Update the number of passengers (subtract the ones unloading) 
		manager.getFloors()[currentFloor].getArrivedPassengers()[this.elevatorID] += passengerDestination[currentFloor]; // Updates Arrived Passenger Array for the floor.
		totalUnloadedPassengers += passengerDestination[currentFloor]; // Updates total unloaded passengers.
		passengerDestination[currentFloor] = 0; // After they unload, no one wants to go to this floor anymore, so set to 0.
	}
	
	// Load passengers.
	private void loadPassengers()
	{
		for (int aboveFloor = currentFloor+1; aboveFloor != 5; aboveFloor++) // see if anyone wants to go up
		{
			if (manager.getFloors()[currentFloor].getPassengerRequests()[aboveFloor] != 0)
			{
				loadAscendingPassengers(aboveFloor);
			}
		}
		if (numPassengers == 0) // we didnt load anyone to go up
		{
			for (int belowFloor = currentFloor-1; belowFloor >= 0; belowFloor--) // see if anyone wants to go up
			{
				if (manager.getFloors()[currentFloor].getPassengerRequests()[belowFloor] != 0)
				{
					loadDescendingPassengers(belowFloor);
				}
			}
		}
		manager.getFloors()[currentFloor].setApproachingElevator(-1);
	}
	
	// Loads passengers going UP only and updates the correct simulation state variables.
	private void loadAscendingPassengers(int aboveFloor)
	{
		IO.printEvent(SimClock.getTime(), "ElevatorLoadEvent", "(Elevator " + this.elevatorID + ") Reached Floor " + currentFloor + "; now loading " + manager.getFloors()[currentFloor].getPassengerRequests()[aboveFloor] + " passengers destined for Floor " + aboveFloor);
		
		numPassengers += manager.getFloors()[currentFloor].getPassengerRequests()[aboveFloor];
		totalLoadedPassengers += manager.getFloors()[currentFloor].getPassengerRequests()[aboveFloor];
		passengerDestination[aboveFloor] += passengerDestination[aboveFloor] + manager.getFloors()[currentFloor].getPassengerRequests()[aboveFloor];
		manager.getFloors()[currentFloor].setPassengerRequests(aboveFloor, 0);
		moveQueue.add(new ElevatorEvent(aboveFloor, calculateETA(aboveFloor, true)));
		
		IO.printEvent(SimClock.getTime(), "ElevatorMoveEvent", "(Elevator " + this.elevatorID + ") Will now head to Floor " + aboveFloor + " to drop off " + passengerDestination[aboveFloor] + " passengers.");
	}
	
	// Loads passengers going DOWN only and updates the correct simulation state variables.
	private void loadDescendingPassengers(int belowFloor)
	{
		IO.printEvent(SimClock.getTime(), "ElevatorLoadEvent", "(Elevator " + this.elevatorID + ") Reached Floor " + currentFloor + "; now loading " + manager.getFloors()[currentFloor].getPassengerRequests()[belowFloor] + " passengers destined for Floor " + belowFloor);
		
		numPassengers += manager.getFloors()[currentFloor].getPassengerRequests()[belowFloor];
		totalLoadedPassengers += manager.getFloors()[currentFloor].getPassengerRequests()[belowFloor];
		passengerDestination[belowFloor] += passengerDestination[belowFloor] + manager.getFloors()[currentFloor].getPassengerRequests()[belowFloor];
		manager.getFloors()[currentFloor].setPassengerRequests(belowFloor, 0);
		moveQueue.add(new ElevatorEvent(belowFloor, calculateETA(belowFloor, false)));
		
		IO.printEvent(SimClock.getTime(), "ElevatorMoveEvent", "(Elevator " + this.elevatorID + ") Will now head to Floor " + belowFloor + " to drop off " + passengerDestination[belowFloor] + " passengers.");
	}
	
	// Look for passengers to handle.
	private void lookForPassengers()
	{
		Lock lock = manager.getLock();
		lock.lock();
		
		BuildingFloor[] floors = manager.getFloors();
		Outer: for (int floor = 0; floor != floors.length; floor++)
		{
			int[] requests = floors[floor].getPassengerRequests();
			for (int desiredFloor = 0; desiredFloor != requests.length; desiredFloor++)
			{
				if (requests[desiredFloor] != 0)
				{
					if (manager.handleRequests(floor, desiredFloor, this.elevatorID))
					{
						moveQueue.add(new ElevatorEvent(floor, calculateBaseETA(floor)));
						IO.printEvent(SimClock.getTime(), "ElevatorMoveEvent", "(Elevator " + this.elevatorID + ") Now heading to floor " + floor + " to pick up " + requests[desiredFloor] + " passengers.");
						break Outer;
					}
				}
			}
		}
		lock.unlock();
	}
	
	// Runs the elevator logic. 
	public void run()
	{
		while(!Thread.interrupted())
		{
			if (numPassengers == 0 && moveQueue.isEmpty())
			{
				lookForPassengers();
			}
			else
			{
				ArrayList<ElevatorEvent> tempMoveQueue = new ArrayList<ElevatorEvent>(moveQueue);
				for (int i = 0; i != tempMoveQueue.size(); i++)
				{
					if (SimClock.getTime() == tempMoveQueue.get(i).getExpectedArrival())
					{
						Lock lock = manager.getLock();
						lock.lock();
						
						this.currentFloor = tempMoveQueue.get(i).getDestination();
						moveQueue.remove(tempMoveQueue.get(i));
						
						if (numPassengers != 0 && passengerDestination[currentFloor] != 0) // someone wants to unload here
						{
							unloadPassengers();
						}
						
						if (manager.getFloors()[currentFloor].getApproachingElevator() == this.elevatorID)
						{
							loadPassengers();
						}
						lock.unlock();
					}
				}
			}
		}		
	}
	
	// Getters
	
	public int getTotalLoadedPassengers()
	{
		return totalLoadedPassengers;
	}
	
	public int getTotalUnloadedPassengers()
	{
		return totalUnloadedPassengers;
	}
	
	public int getNumPassengers()
	{
		return numPassengers;
	}
}
