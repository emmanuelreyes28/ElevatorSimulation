//Emmanuel Reyes 58725927
//Thomas Tai Nguyen 61879630

public class IO // Helper class that prints out a formatted string for better readability.
{
	public static void printEvent(int time, String event, String description) // Prints out events in a nice manner.
	{
		System.out.format("%-10s: %-30s | " + description + "\n", "[Time " + time + "]", "(" + event + ")");
	}
}
