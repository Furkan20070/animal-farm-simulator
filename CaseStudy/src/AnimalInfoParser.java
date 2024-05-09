import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AnimalInfoParser
{
    public ArrayList<AnimalInfo> parseConfigFile(String fileName)
    {
        AnimalInfo info;
        ArrayList<AnimalInfo> results = new ArrayList<AnimalInfo>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName)))
        {
            String line;
            while ((line = br.readLine()) != null)
            {
                if (line.equals("ANIMAL:"))
                {
                    info = new AnimalInfo();
                    String nameLine = br.readLine();
                    info.name = nameLine.substring(6);
                    String moveSpeedLine = br.readLine();
                    info.move_speed = Integer.parseInt(moveSpeedLine.substring(12));
                    String preyLine = br.readLine().substring(6);
                    String[] preysStringArray = preyLine.split(",");
                    info.prey = Arrays.asList(preysStringArray);
                    String rangeLine = br.readLine().substring(7);
                    info.range = Integer.parseInt(rangeLine);
                    String maleAmountLine = br.readLine().substring(13);
                    info.maleAmount = Integer.parseInt(maleAmountLine);
                    String femaleAmountLine = br.readLine().substring(15);
                    info.femaleAmount = Integer.parseInt(femaleAmountLine);
                    results.add(info);
                }
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return results;
    }
}
