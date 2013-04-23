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
    static class CategoryKnowledge extends AbstractBagStrategy.AbstractKnowledge
    {
        double test(String[] body)
        {
            return 0.0;
        }
    }

    protected CategoryKnowledge new_CategoryKnowledge()
    {
        return new CategoryKnowledge();
    }
}
