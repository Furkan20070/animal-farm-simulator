import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Transform
{
    int[] position;
    Random rand;

    public Transform(Random rand)
    {
        this.rand = rand;
    }

    //Rastegele başlangıç pozisyonu ata.
    public void InitializePosition()
    {
        position = new int[]{rand.nextInt(501), rand.nextInt(501)};
    }

    //Hız ile orantılı olarak rastgele bir nokta seç ve pozisyonu oraya ata.
    public void move(int distance)
    {
        List<int[]> possiblePoints = getPointsWithChebyshevDistance(position, distance);
        position = possiblePoints.get(rand.nextInt(possiblePoints.size()));
    }

    //Mesafe hesabı yaptığımız fonksiyon.
    public int chebyshevDistance(int[] pos1, int[] pos2)
    {
        return Math.max(Math.abs(pos2[0] - pos1[0]), Math.abs(pos2[1] - pos1[1]));
    }

    //Hareket edilebilecel mümkün olan bütün noktaları döndüren fonksiyon.
    private List<int[]> getPointsWithChebyshevDistance(int[] position, int distance)
    {
        List<int[]> points = new ArrayList<>();

        for (int i = position[0] - distance; i <= position[0] + distance; i++) {
            for (int j = position[1] - distance; j <= position[1] + distance; j++) {
                int[] testPosition = new int[]{i, j};
                int dist = chebyshevDistance(position, testPosition);
                if (dist == distance && i >= 0 && i <= 500 && j >= 0 && j <= 500)
                {
                    points.add(new int[]{i, j});
                }
            }
        }
        return points;
    }

    public String GetPositionAsString()
    {
        return ("x: " + position[0] + " y: " + position[1]);
    }
}
