import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimalClass
{
    private final Random rand;
    private int animalInfoIndex;
    public Gender gender;
    public Transform transform;
    public SimulationArea simulationArea;
    public boolean alreadyMated = false;
    public boolean hunted = false;

    public AnimalClass(int animalInfoIndex, Random rand, Gender gender, SimulationArea simulationArea)
    {
        this.animalInfoIndex = animalInfoIndex;
        this.rand = rand;
        this.gender = gender;
        transform = new Transform(this.rand);
        this.simulationArea = simulationArea;
        transform.InitializePosition();
    }

    public String getName()
    {
        return simulationArea.givenAnimals.get(animalInfoIndex).name;
    }

    public int getMoveSpeed()
    {
        return simulationArea.givenAnimals.get(animalInfoIndex).move_speed;
    }

    public List<String> getPreyList()
    {
        return simulationArea.givenAnimals.get(animalInfoIndex).prey;
    }

    public int getRange()
    {
        return simulationArea.givenAnimals.get(animalInfoIndex).range;
    }

    //Avlanma ve eş kontrollerini buradan hızlıca çağırabiliyoruz. Azvlanmış hayvanlar için fonksiyon herhangi birşey yapmıyor.
    public void Activity()
    {
        if (hunted)
        {
            return;
        }
        HuntCheck();
        PartnerCheck();
    }

    //Alanda rastgele hareket ettiren fonksiyon.
    public void Move()
    {
        simulationArea.RemoveFromMatrix(this);
        transform.move(getMoveSpeed());
        simulationArea.AddToMatrix(this);
    }

    //Yakındaki hayvanlar ile av kontrolü yaptığımız fonksiyon.
    private void HuntCheck()
    {
        ArrayList<ArrayList<AnimalClass>> animalsInVicinity = simulationArea.GetAnimalsInBroadPhase(this);
        for (ArrayList<AnimalClass> animals : animalsInVicinity)
        {
            for (AnimalClass animal : animals)
            {
                if (animal != this && (getPreyList().contains(animal.getName()) || getPreyList().get(0).equals("EVERYTHING")) && transform.chebyshevDistance(transform.position, animal.transform.position) <= getRange())
                {
                    simulationArea.AddAnimalsToHuntedList(animal);
                    animal.hunted = true;
                    System.out.println("Hunt occured! Name of hunter: " + getName() + " hunter position " + transform.GetPositionAsString() + ", Name of prey: " + animal.getName() + " position of prey: " + animal.transform.GetPositionAsString() + " ITERATION: " + simulationArea.iterationCounter);
                }
            }
        }
    }

    //Yakındaki hayvanlar ile eş kontrolü yaptığımız fonksiyon.
    private void PartnerCheck()
    {
        if (alreadyMated)
            return;

        ArrayList<ArrayList<AnimalClass>> animalsInVicinity = simulationArea.GetAnimalsInBroadPhase(this);
        for (ArrayList<AnimalClass> animals : animalsInVicinity)
        {
            for (AnimalClass animal : animals)
            {
                if (!animal.hunted && gender != animal.gender && transform.chebyshevDistance(transform.position, animal.transform.position) <= 3 && getName().equals(animal.getName()))
                {
                    AddNewMember();
                    animal.alreadyMated = true;
                    alreadyMated = true;
                    System.out.println("Added new " + getName() + " member! Position of parent 1: " + transform.GetPositionAsString() + ", position of parent 2: " + animal.transform.GetPositionAsString() + " ITERATION: " + simulationArea.iterationCounter);
                    return;
                }
            }
        }
    }

    private void AddNewMember()
    {
        AnimalClass toAdd;
        if (rand.nextFloat() <= 0.5f)
        {
            toAdd = new AnimalClass(animalInfoIndex, rand, Gender.FEMALE, simulationArea);
            simulationArea.AddAnimalToNewbornList(toAdd);
        }
        else
        {
            toAdd = new AnimalClass(animalInfoIndex, rand, Gender.MALE, simulationArea);
            simulationArea.AddAnimalToNewbornList(toAdd);
        }
    }
}
