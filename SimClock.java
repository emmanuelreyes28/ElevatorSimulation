//Emmanuel Reyes 58725927
//Thomas Tai Nguyen 61879630

public class SimClock // Class that handles the time and its incrementation.
{
	private static int time; // Stores the current simulation time.
	
	public SimClock() // Initializes time, as per lab writeup
	{
		time = 0;
	}
	
	public static void tick(){ //Increments the simulated time by 1
		time++;
	}
	
	public static int getTime(){ //returns the value of the simulated time 
		return time;
	}

}
