//Emmnauel Reyes 58725927
//Thomas Tai Nguyen 61879630

public class Lab4 // The class containing main method. Makes new simulation and runs it.
{
	public static void main(String[] args)
	{
		try
		{
			ElevatorSimulation simulation = new ElevatorSimulation();
			simulation.start();			
		}
		catch (InterruptedException e)
		{
			System.out.println("The main thread was interrupted!");
		}
	}
}
