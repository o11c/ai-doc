import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.List;

// A combination of preprocessor and strategy
public class Driver
{
    PrintWriter out;
    Preprocessor p;
    Strategy s;

    public Driver(File out, Preprocessor p, Strategy s)
    {
        try
        {
            this.out = new PrintWriter(out);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        this.p = p;
        this.s = s;
    }

    public List<String> preprocess(File f)
    {
        Reader in;
        try
        {
            in = new FileReader(f);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return p.preprocess(in);
    }

    public void train(File f, String c)
    {
        List<String> body = preprocess(f);
        s.train(body, c);
    }

    public void test(File f)
    {
        List<String> body = preprocess(f);
        String cat = s.test(body);
        out.println(f.getName() + "," + cat);
        out.flush(); // Java does not flush streams at exit?
    }
}
