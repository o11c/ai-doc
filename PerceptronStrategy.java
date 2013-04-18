import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PerceptronStrategy implements Strategy
{
    Map<String, Map<String, Integer>> knowledge = new HashMap<String, Map<String, Integer>>();

    public void train(String[] body, String cat)
    {
        Map<String, Integer> ck = knowledge.get(cat);
        if (ck == null)
        {
            ck = new HashMap<String, Integer>();
            knowledge.put(cat, ck);
        }

        for (String s : body)
        {
            Integer value = ck.get(s);
            if (value == null)
                value = 0;
            value++;
            ck.put(s, value);
        }
    }

    public String test(String[] body)
    {
        for (Map.Entry<String, Map<String, Integer>> oe : knowledge.entrySet())
        {
            String key = oe.getKey();
            ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(oe.getValue().entrySet());
            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
                    {
                        public int compare(Map.Entry<String, Integer> l, Map.Entry<String, Integer> r)
                        {
                            // reversed comparison
                            return l.getValue() - r.getValue();
                        }
                    });
            // stupid Java
            int i = 0;
            Map.Entry<String, Integer>[] best = new Map.Entry/*<String, Integer>*/[Math.min(20, list.size())];
            for (Map.Entry<String, Integer> ie : list)
                best[i++] = ie;
        }
        return null;
    }
}
