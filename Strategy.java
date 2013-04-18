public interface Strategy
{
    void train(String[] body, String cat);
    String test(String[] body);
}
