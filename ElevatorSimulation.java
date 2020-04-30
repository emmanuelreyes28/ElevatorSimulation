import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

//Emmanuel Reyes 58725927
//Thomas Tai Nguyen 61879630

public class ElevatorSimulation // The class that handles the simulation logic.
{
	private BuildingManager manager; // The manager for the floors. Shared across all Elevator threads.
	private int totalSimulationTime; // The total time that the simulation will run for.
	private int simulationSecondRate; // How many milliseconds each simulated second is.
	private ArrayList<ArrayList<PassengerArrival>> behavior; // The spawn behavior for each floor.
	private SimClock clock; // The SimClock initialized.
	
	private Elevator[] elevators; // An array that stores the elevator classes (with their IDs)
	private Thread[] threadpool; // The array that stores each elevator thread.
	
	//Constructor
	public ElevatorSimulation()
	{
		behavior = new ArrayList<ArrayList<PassengerArrival>>();
		elevators = new Elevator[5];
		threadpool = new Thread[5];
		clock = new SimClock();
		manager = new BuildingManager();
		
		for (int i = 0; i != 5; i++) // 5 floors
		{
			behavior.add(new ArrayList<PassengerArrival>());
			elevators[i] = new Elevator(i, manager);
		}
		
		readConfig(); // Read in the configuration first

	}
	
	public void start() throws InterruptedException // Starts the simulation.
	{
		for (int i = 0; i != 5; i++)
		{
			threadpool[i] = new Thread(elevators[i]);
			threadpool[i].start();
		}
		
		while (SimClock.getTime() != totalSimulationTime)
		{
			for (int floor = 0; floor != behavior.size(); floor++)
			{
				for (int arrival = 0; arrival != behavior.get(floor).size(); arrival++)
				{
					PassengerArrival current = behavior.get(floor).get(arrival);
					if (current.getExpectedTimeOfArrival() == SimClock.getTime()) // New passengers need to spawn.
					{
						manager.addRequests(floor, current.getDestinationFloor(), current.getNumPassengers());
						current.setExpectedTimeOfArrival(current.getExpectedTimeOfArrival() + current.getTimePeriod());
						IO.printEvent(SimClock.getTime(), "PassengerSpawnEvent", "(Floor " + floor + ") " + current.getNumPassengers() + " passengers have spawned at Floor " + floor + " and they are requesting to go to Floor " + current.getDestinationFloor());
					}
				}
			}
			Thread.sleep(simulationSecondRate);
			SimClock.tick();
		}
		
		for (int i = 0; i != 5; i++)
		{
			threadpool[i].interrupt();
		}
		
		printBuildingState();
	}
	
	public void printBuildingState() // Prints out the Simulation Statistics
	{
		// notes: double check numbers to ensure correctness
		// note: after double checking numbers, double check simulation to ensure correctness
		// then write report, turn in
		System.out.println("\n\n------------------------- Time " + SimClock.getTime() + " --------------------------");
		System.out.println("Building");
		for (int i = 0; i != 5; i++)
		{
			System.out.println("->    Floor " + i);
			System.out.println("        Total Passengers Requesting Elevator Access: " + manager.getFloors()[i].getTotalDestinationRequestsAsInteger());
			System.out.println("        Total Passengers That Have Arrived: " + manager.getFloors()[i].getArrivedPassengersAsInteger());
			System.out.println("        Current Passengers Waiting: " + manager.getFloors()[i].getPassengerRequestsAsInteger());
			System.out.println("        Current Elevator Approach: " + manager.getFloors()[i].getApproachingElevator());
			System.out.println("");
		}
		
		System.out.println("Elevator");
		for (int i = 0; i != 5; i++)
		{
			System.out.println("->    Elevator " + i);
			System.out.println("        Total Passengers Loaded: " + elevators[i].getTotalLoadedPassengers());
			System.out.println("        Total Passengers Unloaded: " + elevators[i].getTotalUnloadedPassengers());
			System.out.println("        Current Passengers Loaded: " + elevators[i].getNumPassengers());
			System.out.println("");
		}
	}
	
	public void readConfig() // Reads in ElevatorConfig.txt and stores the data into the correct variables.
	{
		String s;
		Scanner inFile = null;
		try
		{
			inFile = new Scanner(new File("ElevatorConfig.txt"));
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		
		totalSimulationTime = Integer.parseInt(inFile.nextLine());
		simulationSecondRate = Integer.parseInt(inFile.nextLine());
		
		int counter = 0;
		while (inFile.hasNextLine())
		{
			s = inFile.nextLine();
			String[] rules = s.split(";");
			for (int i = 0; i != rules.length; i++)
			{
				String[] currentRule = rules[i].split(" ");
				behavior.get(counter).add(new PassengerArrival(Integer.parseInt(currentRule[0]), 
					Integer.parseInt(currentRule[1]), 
					Integer.parseInt(currentRule[2])));
			}
			counter++;
		}
	}
}
