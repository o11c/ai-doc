import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PerceptronStrategy extends AbstractBagStrategy
{
    static class Memory
    {
        List<String> body;
        String cat;

        Memory(List<String> body, String cat)
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
            for (Memory m : mems)
            {
                double sum = test(m.body);
                boolean thinks_it_is = sum > 0.0;
                boolean really_is = m.cat.equals(identity);
                int err = 0;
                if (thinks_it_is && !really_is)
                    err = -1;
                if (!thinks_it_is && really_is)
                    err = 1;
                if (err == 0)
                    continue;

                for (Map.Entry<String, Double> e : weights.entrySet())
                {
                    double prob = 0.0;
                    for (String w : m.body)
                        if (e.getKey().equals(w))
                            prob++;
                    prob /= m.body.size();
                    e.setValue(e.getValue() + rate * err * prob);
                }
            }
        }

        @Override
        double test(List<String> body)
        {
            double sum = bias;
            for (Map.Entry<String, Double> e : weights.entrySet())
            {
                double prob = 0.0;
                for (String w : body)
                    if (e.getKey().equals(w))
                        prob++;
                prob /= body.size();
                sum += prob * e.getValue();
            }
            return sum;
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
    public void train(List<String> body, String cat)
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
        System.out.println("Resetting perceptron. This can take a LONG time.");
        System.out.print("P: " + rounds + " \r");
        System.out.flush();
        while (rounds-->0)
        {
            System.out.print("P: " + rounds + " \r");
            System.out.flush();
            tick(rate);
            rate *= 0.99;
        }
    }

    @Override
    protected AbstractKnowledge new_CategoryKnowledge()
    {
        return new CategoryKnowledge();
    }
}
