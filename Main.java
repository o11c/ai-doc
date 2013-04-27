import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    static class IO
    {
        BufferedReader in;
        PrintStream out;
        IO(InputStream in, PrintStream out)
        {
            this.in = new BufferedReader(new InputStreamReader(in));
            this.out = out;
        }

        String input(String p, String d)
        {
            out.println(p + "(default: " + d + ")");
            String o = null;
            try
            {
                o = in.readLine();
            }
            catch (IOException e)
            {
            }
            if (o == null || o.isEmpty())
                o = d;
            return o;
        }
    }

    static void do_train(List<Driver> drivers, File catdir, String catname)
    {
        // TODO: don't reopen the file for every driver
        for (File f : catdir.listFiles())
            if (f.isFile())
                for (Driver d : drivers)
                    d.train(f, catname);
    }

    static void do_test(List<Driver> drivers, File catdir)
    {
        for (File f : catdir.listFiles())
            if (f.isFile())
                for (Driver d : drivers)
                    d.test(f);
    }

    public static void main(String[] args)
    {
        File deedsoftrust, deedsofreconveyance, liens, testing, output;

        {
            IO io = new IO(System.in, System.out);

            deedsoftrust = new File(io.input("Path for the Deeds of Trust training data: ", "data/DT"));
            deedsofreconveyance = new File(io.input("Path for the Deeds of Reconveyance training data: ", "data/DR"));
            liens = new File(io.input("Path for the Liens training data: ", "data/L"));
            testing = new File(io.input("Path for the Testing data: ", "data/TEST"));
            output = new File(io.input("Directory for the Output files: ", "test-results"));
        }

        List<Driver> drivers = new ArrayList<Driver>();
        for (Preprocessor pp : new Preprocessor[]
                {
                    new LittlePreprocessor(),
                    new BlacklistPreprocessor(),
                })
            for (Strategy s : new Strategy[]
                    {
                        new IntelliGrepStrategy(),
                        new NaiveBayesStrategy(),
                        new PerceptronStrategy(),
                    })
            {
                String name = pp.getClass().getName() + "-" + s.getClass().getName() + ".txt";
                drivers.add(new Driver(new File(output, name), pp, s));
            }

        System.out.println("Training deeds of trust ...");
        do_train(drivers, deedsoftrust, "DT");
        System.out.println("Training deeds of reconveyance...");
        do_train(drivers, deedsofreconveyance, "DR");
        System.out.println("Training liens ...");
        do_train(drivers, liens, "L");

        System.out.println("Doing tests now!");
        do_test(drivers, testing);
        System.out.println("Everything is done now ...");
    }
}
