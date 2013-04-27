import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NaiveBayesStrategy extends AbstractBagStrategy
{
    static class CategoryKnowledge extends AbstractBagStrategy.AbstractKnowledge
    {
        @Override
        double test(String[] d)
        {
            List<String> dl = Arrays.asList(d);
            double probgetval = 1.0;
            for (Map.Entry<String, Double> e : probabilities.entrySet())
            {
                // a feature (word)
                String ft = e.getKey();
                // frequency of that feature
                Double val = e.getValue();
                if (dl.contains(ft))
                    probgetval *= probabilities.get(ft);
            }
            return probgetval / document_count;
        }
    }

    @Override
    protected AbstractKnowledge new_CategoryKnowledge()
    {
        return new CategoryKnowledge();
    }
}
