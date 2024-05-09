public class Main
{
    public static void main(String[] args)
    {

        System.out.println();

        SimulationArea simulationArea = new SimulationArea(20);

        for (int i = 0; i < 1000; i++)
        {
            simulationArea.Simulate();
        }
        simulationArea.GetAmount();
    }
}