import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/// returns only lowercase
public class LittlePreprocessor implements Preprocessor
{
    @Override
    public String[] preprocess(Reader in)
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
            words.addAll(Arrays.asList(lw));
        }

        String[] out = new String[words.size()];
        words.toArray(out);
        return out;
    }
}
