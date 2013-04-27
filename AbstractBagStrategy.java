import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class AbstractBagStrategy implements Strategy
{
    public static abstract class AbstractKnowledge
    {
        // Number of times each word appears
        private final Map<String, Integer> word_counts = new HashMap<String, Integer>();
        // Number of documents in which each word appears
        private final Map<String, Integer> document_bools = new HashMap<String, Integer>();
        // Number of words
        private int word_count = 0;
        // Number of documents
        protected int document_count = 0;
        // Cache of word frequencies
        protected Map<String, Double> probabilities = null;

        private void inc(Map<String, Integer> c, String key)
        {
            Integer value = c.get(key);
            if (value == null)
                value = 0;
            value++;
            c.put(key, value);
        }

        public void train(List<String> body)
        {
            Set<String> seen = new HashSet<String>();
            for (String key : body)
            {
                inc(word_counts, key);
                if (seen.contains(key))
                    continue;
                seen.add(key);
                inc(document_bools, key);
            }
            document_count++;
            word_count += body.size();
            probabilities = null;
        }

        public String[] most_common_words()
        {
            List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(word_counts.entrySet());
            Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
                    {
                        @Override
                        public int compare(Map.Entry<String, Integer> l, Map.Entry<String, Integer> r)
                        {
                            // really reversed comparison
                            // it's much easier to customize operators
                            return r.getValue() - l.getValue();
                        }
                    });
            if (list.size() > 20)
                list = list.subList(0, 20);
            String[] s = new String[list.size()];
            int i = 0;
            for (Map.Entry<String, Integer> e : list)
                s[i++] = e.getKey();
            return s;
        }

        private void recache(Map<String, Integer> stats, int count, Set<String> features)
        {
            probabilities = new HashMap<String, Double>();
            for (String s : features)
            {
                Integer docs = stats.get(s);
                if (docs == null)
                    docs = 1; // logically, 0
                Double f = (int) docs / (double) count;
                probabilities.put(s, f);
            }
        }

        public void recache_by_count(Set<String> features)
        {
            recache(word_counts, word_count, features);
        }
        public void recache_by_bool(Set<String> features)
        {
            recache(document_bools, document_count, features);
        }

        abstract double test(List<String> body);
    }

    protected Map<String, AbstractKnowledge> knowledge = new HashMap<String, AbstractKnowledge>();

    protected abstract AbstractKnowledge new_CategoryKnowledge();

    @Override
    public void train(List<String> body, String cat)
    {
        AbstractKnowledge ck = knowledge.get(cat);
        if (ck == null)
        {
            ck = new_CategoryKnowledge();
            knowledge.put(cat, ck);
        }

        ck.train(body);
    }

    protected Set<String> features = null;

    protected void update_cache()
    {
        if (features != null)
            return;
        features = new HashSet<String>();

        for (Map.Entry<String, AbstractKnowledge> e : knowledge.entrySet())
            features.addAll(Arrays.asList(e.getValue().most_common_words()));
        for (Map.Entry<String, AbstractKnowledge> e : knowledge.entrySet())
            e.getValue().recache_by_bool(features);
    }

    @Override
    public String test(List<String> body)
    {
        update_cache();

        String best_cat = null;
        double best_value = Double.NEGATIVE_INFINITY;
        for (Map.Entry<String, AbstractKnowledge> e : knowledge.entrySet())
        {
            double value = e.getValue().test(body);
            if (value > best_value)
            {
                best_cat = e.getKey();
                best_value = value;
            }
        }
        return best_cat;
    }
}
