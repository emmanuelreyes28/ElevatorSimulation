//Emmanuel Reyes 58725927
//Thomas Tai Nguyen 61879630

public class PassengerArrival // A class representing the spawn information for new passengers.
{
	private int numPassengers; // The number of passengers that spawn.
	private int destinationFloor; // The floor these passengers want to go to.
	private int timePeriod; // How often they spawn.
	private int expectedTimeOfArrival; // The next time they will spawn.
	
	// Constructor
	public PassengerArrival(int numPassengers, int destinationFloor, int timePeriod)
	{
		this.numPassengers = numPassengers;
		this.destinationFloor = destinationFloor;
		this.timePeriod = timePeriod;
		this.expectedTimeOfArrival = timePeriod;
	}
	
	//Setters
	public void setNumPassengers(int numPassengers){
		this.numPassengers = numPassengers;
	}
	
	public void setDestinationFloor(int destinationFloor){
		this.destinationFloor = destinationFloor;
	}
	
	public void setTimePeriod(int timePeriod){
		this.timePeriod = timePeriod;
	}
	
	public void setExpectedTimeOfArrival(int expectedTimeOfArrival){
		this.expectedTimeOfArrival = expectedTimeOfArrival;
	}
	
	//Getters
	public int getNumPassengers(){
		return numPassengers;
	}
	
	public int getDestinationFloor(){
		return destinationFloor;
	}
	
	public int getTimePeriod(){
		return timePeriod;
	}
	
	public int getExpectedTimeOfArrival(){
		return expectedTimeOfArrival;
	}
}
