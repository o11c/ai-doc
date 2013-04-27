import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IntelliGrepStrategy implements Strategy
{
    Random rng = new Random();

    public void train(List<String> body, String cat)
    {
        // IntelliGrep requires no training
    }

    public String test(List<String> body)
    {
        int lcount = 0;
        int dtcount = 0;
        int drcount = 0;
        // TODO use Collections.indexOfSubList to genericize this
        String pp = null;
        String p = null;
        for (String w : body)
        {
            if ("lien".equals("w"))
                lcount++;
            if ("deed".equals(pp) && "of".equals(p))
            {
                if ("trust".equals(w))
                    dtcount++;
                if ("reconveyance".equals(w))
                    drcount++;
            }
            pp = p;
            p = w;
        }

        List<String> maxkeys = new ArrayList<String>();
        int max = 0;
        max = lcount;
        maxkeys.add("L");

        if (dtcount > max)
        {
            max = dtcount;
            maxkeys.clear();
            maxkeys.add("DT");
        }
        else if (dtcount == max)
        {
            maxkeys.add("DT");
        }

        if (drcount > max)
        {
            max = drcount;
            maxkeys.clear();
            maxkeys.add("DR");
        }
        else if (drcount == max)
        {
            maxkeys.add("DR");
        }

        int index = rng.nextInt(maxkeys.size());
        return maxkeys.get(index);
    }
}
