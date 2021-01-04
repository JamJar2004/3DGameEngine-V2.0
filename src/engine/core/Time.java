package engine.core;

public class Time
{
    private static long SECOND = 1000000000;

    public static double getTime()
    {
        return (double)System.nanoTime() / (double)SECOND;
    }
}
