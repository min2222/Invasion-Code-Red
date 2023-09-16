package com.min01.invasioncodered.misc;

import java.util.concurrent.ThreadLocalRandom;

public class ICRUtil
{
    public static boolean percent(double percent) 
    {
        return percent > ThreadLocalRandom.current().nextDouble(0, 1);
    }
}
