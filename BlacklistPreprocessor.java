import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/// returns only lowercase
public class BlacklistPreprocessor implements Preprocessor
{
    private final Set<String> blacklist = new HashSet<String>(Arrays.asList(new String[]
            {
                // one-letter words excluded below
                "the",
                "and",
                "in",
                "is",
                "this",
                "that",
                "as",
                "or",
                "on",
            }));

    @Override
    public List<String> preprocess(Reader in)
    {
        BufferedReader reader = new BufferedReader(in);

        List<String> words = new ArrayList<String>();

        while (true)
        {
            String line;
            try
            {
                line = reader.readLine();
                if (line == null)
                    break;
            }
            catch (IOException e)
            {
                break;
            }
            String[] lw = line.toLowerCase().split("[^a-z]+");
            for (String s : lw)
                if (s.length() >= 2 && !blacklist.contains(s))
                    words.add(s);
        }

        return words;
    }
}
