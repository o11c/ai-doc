import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PerceptronStrategy extends AbstractBagStrategy
{
    static class Memory
    {
        String[] body;
        String cat;

        Memory(String[] body, String cat)
        {
            this.body = body;
            this.cat = cat;
        }
    }

    static class CategoryKnowledge extends AbstractBagStrategy.AbstractKnowledge
    {
        private double bias;
        private Map<String, Double> weights;

        void reset(Set<String> features)
        {
            weights = new HashMap<String, Double>();
            for (String s : features)
                weights.put(s, 1.0);
        }

        void tick(double rate, List<Memory> mems, String identity)
        {
            // int err = 0;
            for (Memory m : mems)
            {
                double sum = bias;
                for (Map.Entry<String, Double> e : weights.entrySet())
                {
                    double prob = 0.0;
                    for (String w : m.body)
                        if (e.getKey().equals(w))
                            prob++;
                    prob /= m.body.length;
                    sum += prob * e.getValue();
                }
                boolean thinks_it_is = sum > 0.0;
                boolean really_is = m.cat.equals(identity);
                int err = 0;
                if (thinks_it_is && !really_is)
                {
                    err--;
                }
                if (!thinks_it_is && really_is)
                {
                    err++;
                }
                if (err == 0)
                    continue;
            //}
            //for (Memory m : mems)
            //{
                for (Map.Entry<String, Double> e : weights.entrySet())
                {
                    double prob = 0.0;
                    for (String w : m.body)
                        if (e.getKey().equals(w))
                            prob++;
                    prob /= m.body.length;
                    e.setValue(e.getValue() + rate * err * prob);
                }
            }
        }

        double test(String[] body)
        {
            return 0.0;
        }
    }

    List<Memory> memories = new ArrayList<Memory>();

    private void tick(double rate)
    {
        for (Map.Entry<String, AbstractBagStrategy.AbstractKnowledge> e : knowledge.entrySet())
        {
            ((CategoryKnowledge)e.getValue()).tick(rate, memories, e.getKey());
        }
    }

    @Override
    public void train(String[] body, String cat)
    {
        super.train(body, cat);
        memories.add(new Memory(body, cat));
    }

    @Override
    protected void update_cache()
    {
        if (features != null)
            return;
        super.update_cache();

        for (AbstractBagStrategy.AbstractKnowledge ak : knowledge.values())
        {
            ((CategoryKnowledge)ak).reset(features);
        }

        int rounds = 100;
        double rate = 0.1;
        while (rounds-->0)
        {
            tick(rate);
            rate *= 0.99;
        }
    }

    protected CategoryKnowledge new_CategoryKnowledge()
    {
        return new CategoryKnowledge();
    }
}
