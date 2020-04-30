//Emmanuel Reyes 58725927
//Thomas Tai Nguyen, 61879630

public class ElevatorEvent
{
	private int destination; // The destination floor for the event.
	private int expectedArrival; // The estimated time of arrival INCLUDING unloading/loading.
	
	// Constructor
	public ElevatorEvent(int destination, int expectedArrival)
	{
		this.destination = destination;
		this.expectedArrival = expectedArrival;
	}
	
	//Setters
	public void setDestination(int destination){
		this.destination = destination;
	}
	
	public void setExpectedArrival(int expectedArrival){
		this.expectedArrival = expectedArrival;
	}
	
	//Getters
	public int getDestination(){
		return destination;
	}
	
	public int getExpectedArrival(){
		return expectedArrival;
	}

}
