import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//Emmanuel Reyes 58725927
//Thomas Tai Nguyen, 61879630

public class BuildingManager
{
	private BuildingFloor[] floors; //Represents the state of all floors in the building 
	private Lock lock; //A lock for the BuildingManager object. An elevator needs this before it can do anything.
	
	public Lock getLock() // Gets the lock. 
	{
		return lock;
	}
	
	// Constructor
	public BuildingManager()
	{
		lock = new ReentrantLock();
		floors = new BuildingFloor[5];
		for (int i = 0; i != 5; i++)
		{
			floors[i] = new BuildingFloor();
		}
	}
	
	// Adds new spawned passengers to the requests for the floor.
	public void addRequests(int currentFloor, int destinationFloor, int passengers)
	{
		int[] currentRequests = floors[currentFloor].getPassengerRequests();
		currentRequests[destinationFloor] = currentRequests[destinationFloor] + passengers;
		
		int[] currentTotalRequests = floors[currentFloor].getTotalDestinationRequests();
		currentTotalRequests[destinationFloor] = currentTotalRequests[destinationFloor] + passengers;
	}
	
	// Set the approaching elevator for a floor to the elevatorID.
	public synchronized boolean handleRequests(int currentFloor, int destFloor, int elevatorID)
	{
		if (floors[currentFloor].getApproachingElevator() == -1) // Failsafe
		{ 
			// send this elevator and set approachingelevator to this one
			floors[currentFloor].setApproachingElevator(elevatorID);
			return true;
		}
		else return false;
	}

	
	// Getters
	public BuildingFloor[] getFloors()
	{
		return floors;
	} 
}
