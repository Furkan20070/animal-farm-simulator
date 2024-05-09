import java.util.*;

public class SimulationArea
{
    int dividerMatrixSize;
    private ArrayList<ArrayList<ArrayList<AnimalClass>>> broadPhaseMatrix = new ArrayList<ArrayList<ArrayList<AnimalClass>>>();
    private ArrayList<AnimalClass> animals = new ArrayList<AnimalClass>();
    private ArrayList<AnimalClass> newBornAnimals = new ArrayList<AnimalClass>();
    private ArrayList<AnimalClass> huntedAnimals = new ArrayList<AnimalClass>();
    public int iterationCounter = 0;
    public ArrayList<AnimalInfo> givenAnimals = new ArrayList<AnimalInfo>();
    public SimulationArea(int divider)
    {
        dividerMatrixSize = divider;

        //Broad phase matrisini oluştur.
        CreateMatrix();

        //Config dosyasını oku.
        AnimalInfoParser parse = new AnimalInfoParser();
        givenAnimals = parse.parseConfigFile(System.getProperty("user.dir") + "\\src\\Config");
        Random randomEngine = new Random();

        //Config dosyasındaki verilere göre AnimalClass objelerini oluştur.
        for (int j = 0; j < givenAnimals.size(); j++)
        {
            for (int i = 0; i < givenAnimals.get(j).maleAmount; i++)
            {
                animals.add(new AnimalClass(j, randomEngine, Gender.MALE, this));
            }

            for (int i = 0; i < givenAnimals.get(j).femaleAmount; i++)
            {
                animals.add(new AnimalClass(j, randomEngine, Gender.FEMALE, this));
            }
        }

        Collections.shuffle(animals);

        //Oluşturulan hayvanları broad phase matrisine ekle.
        for (AnimalClass animal : animals)
        {
            AddToMatrix(animal);
        }
    }

    //Simülasyonu 1 adım çalıştırmak için kullanıyoruz.
    public void Simulate()
    {
        for (AnimalClass animal : animals)
        {
            animal.Move();
        }
        for (AnimalClass animal : animals)
        {
            animal.Activity();
        }
        AddAnimalsToGeneralList();
        RemoveHuntedAnimalsFromGeneralList();
        iterationCounter++;
    }

    //Simülasyon sonuçlarını yazdırmak için yardımcı fonksiyon.
    public void GetAmount()
    {
        ArrayList<String> animalTypes = new ArrayList<String>();
        ArrayList<Integer> animalAmount = new ArrayList<Integer>();
        for (int i = 0; i < animals.size(); i++)
        {
            if (animalTypes.contains(animals.get(i).getName()))
            {
                int index = animalTypes.indexOf(animals.get(i).getName());
                {
                    int newValue = animalAmount.get(index) + 1;
                    animalAmount.set(index, newValue);
                }
            }
            else
            {
                animalTypes.add(animals.get(i).getName());
                animalAmount.add(1);
            }
        }

        int sum = 0;
        for (int i = 0; i < animalTypes.size(); i++)
        {
            System.out.println("Animal type: " + animalTypes.get(i) + " Amount: " + animalAmount.get(i));
            sum += animalAmount.get(i);
        }
        System.out.println("Total animal count: " + sum);
    }

    public void AddAnimalToNewbornList(AnimalClass a)
    {
        newBornAnimals.add(a);
    }

    private void AddAnimalsToGeneralList()
    {
        for (AnimalClass a : newBornAnimals)
        {
            animals.add(a);
            AddToMatrix(a);
        }
        newBornAnimals.clear();
    }

    public void AddAnimalsToHuntedList(AnimalClass a)
    {
        huntedAnimals.add(a);
    }

    private void RemoveHuntedAnimalsFromGeneralList()
    {
        for (AnimalClass a : huntedAnimals)
        {
            RemoveFromMatrix(a);
            animals.remove(a);
        }
        huntedAnimals.clear();
    }

    void CreateMatrix()
    {
        for (int i = 0; i < (500 / dividerMatrixSize) + 1; i++)
        {
            broadPhaseMatrix.add(new ArrayList<ArrayList<AnimalClass>>());
            for (int j = 0; j < (500 / dividerMatrixSize) + 1; j++)
            {
                broadPhaseMatrix.get(i).add(new ArrayList<AnimalClass>());
            }
        }
    }

    void RemoveFromMatrix(AnimalClass caller)
    {
        int i = caller.transform.position[0] / dividerMatrixSize;
        int j = caller.transform.position[1] / dividerMatrixSize;
        broadPhaseMatrix.get(i).get(j).remove(caller);
    }

    void AddToMatrix(AnimalClass caller)
    {
        int i = caller.transform.position[0] / dividerMatrixSize;
        int j = caller.transform.position[1] / dividerMatrixSize;
        broadPhaseMatrix.get(i).get(j).add(caller);
    }

    //Çağıran hayvan objesinin çevresindeki hayvanları çıktı olarak veren foknsiyon.
    ArrayList<ArrayList<AnimalClass>> GetAnimalsInBroadPhase(AnimalClass caller)
    {
        ArrayList<ArrayList<AnimalClass>> result = new ArrayList<ArrayList<AnimalClass>>();
        int i = caller.transform.position[0] / dividerMatrixSize;
        int j = caller.transform.position[1] / dividerMatrixSize;
        result.add(broadPhaseMatrix.get(i).get(j));
        List<int[]> adjacentMatrices = getPointsWithChebyshevDistance(new int[]{i, j},1);
        for (int[] matrix : adjacentMatrices)
        {
            result.add(broadPhaseMatrix.get(matrix[0]).get(matrix[1]));
        }
        return result;
    }

    public int chebyshevDistance(int[] pos1, int[] pos2)
    {
        return Math.max(Math.abs(pos2[0] - pos1[0]), Math.abs(pos2[1] - pos1[1]));
    }

    private List<int[]> getPointsWithChebyshevDistance(int[] position, int distance)
    {
        List<int[]> points = new ArrayList<>();

        for (int i = position[0] - distance; i <= position[0] + distance; i++) {
            for (int j = position[1] - distance; j <= position[1] + distance; j++) {
                int[] testPosition = new int[]{i, j};
                int dist = chebyshevDistance(position, testPosition);
                if (dist == distance && i >= 0 && i <= 25 && j >= 0 && j <= 25)
                {
                    points.add(new int[]{i, j});
                }
            }
        }
        return points;
    }
}
