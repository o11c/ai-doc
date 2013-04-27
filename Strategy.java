import java.util.List;

public interface Strategy
{
    void train(List<String> body, String cat);
    String test(List<String> body);
}
