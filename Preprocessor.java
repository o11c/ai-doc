import java.io.Reader;
import java.util.List;

public interface Preprocessor
{
    List<String> preprocess(Reader in);
}
