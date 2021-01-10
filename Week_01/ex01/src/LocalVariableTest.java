public class LocalVariableTest {
    public static void main(String[] args) {
        MovingAverage movingAverage = new MovingAverage();
        int first = 1;
        int second = 2;
        movingAverage.submit(first);
        movingAverage.submit(second);
        double average = movingAverage.getAverage();
    }
}