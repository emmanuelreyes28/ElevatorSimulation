//Emmanuel Reyes 58725927
//Thomas Tai Nguyen 61879630

public class BuildingFloor //This class represents the state of a specific floor in the building
{  
	
	private int[] totalDestinationRequests; // The total number of passengers that wanted to go to each floor (each floor = ith index)
	private int[] arrivedPassengers; // The total number of passengers who have arrived on this floor from the ith elevator.
	private int[] passengerRequests; // An array that represents how many passengers currently want to go to the ith floor.
	private int approachingElevator; // The elevator that is currently on its way to handle the passengers.
	
	// Constructor
	public BuildingFloor()
	{
		this.totalDestinationRequests = new int[5];
		this.arrivedPassengers = new int[5];
		this.passengerRequests = new int[5];
		this.approachingElevator = -1;
	}
	
	// Setters
	public void setTotalDestinationRequests(int[] totalDestinationRequests)
	{
		this.totalDestinationRequests = totalDestinationRequests.clone();
	}
	
	public void setArrivedPassengers(int[] arrivedPassengers)
	{
		this.arrivedPassengers = arrivedPassengers.clone();
	}
	
	public void setPassengerRequests(int destFloor, int updatedRequests)
	{
		this.passengerRequests[destFloor] = updatedRequests;
	}
	public void setApproachingElevator(int approachingElevator)
	{
		this.approachingElevator = approachingElevator;
	}
	
	// Getters
	public int[] getTotalDestinationRequests()
	{
		return this.totalDestinationRequests;
	}
	
	public int getTotalDestinationRequestsAsInteger()
	{
		int result = 0;
		for (int i = 0; i != 5; i++)
		{
			result += totalDestinationRequests[i];
		}
		return result;
	}
	
	public int getArrivedPassengersAsInteger()
	{
		int result = 0;
		for (int i = 0; i != 5; i++)
		{
			result += arrivedPassengers[i];
		}
		return result;
	}
	
	public int getPassengerRequestsAsInteger()
	{
		int result = 0;
		for (int i = 0; i != 5; i++)
		{
			result += passengerRequests[i];
		}
		return result;
	}
	
	
	public int[] getArrivedPassengers()
	{
		return this.arrivedPassengers;
	}
	
	public int[] getPassengerRequests()
	{
		return this.passengerRequests;
	}
	
	public int getApproachingElevator()
	{
		return approachingElevator;
	}
}
