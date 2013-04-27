import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class NaiveBayesStrategy extends AbstractBagStrategy
{
    static class CategoryKnowledge extends AbstractBagStrategy.AbstractKnowledge
    {
        @Override
        double test(List<String> dl)
        {
            double probgetval = 1.0;
            for (Map.Entry<String, Double> e : probabilities.entrySet())
            {
                // a feature (word)
                String ft = e.getKey();
                // frequency of that feature, in (0, 1]
                Double val = e.getValue();
                if (dl.contains(ft))
                    probgetval *= val;
                else
                    probgetval *= (1 - val);
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
