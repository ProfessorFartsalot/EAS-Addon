package com.firehostredux.easaddon.mrbt0907util;

import java.util.Random;

/**A simple math library that can be used to simplify math related calculations*/
public class Maths
{
	private static Random random = new Random();
	
	/**Returns the integer if between min and max; otherwise will return the min or max*/
	public static int clamp(int input, int min, int max)
	{
		return min > max ? min : input > min ? input < max ? input : max : min;
	}
	
	/**Returns the float if between min and max; otherwise will return the min or max*/
	public static float clamp(float input, float min, float max)
	{
		return min > max ? min : input > min ? input < max ? input : max : min;
	}
	
	/**Returns the double if between min and max; otherwise will return the min or max*/
	public static double clamp(double input, double min, double max)
	{
		return min > max ? min : input > min ? input < max ? input : max : min;
	}
	
	/**Returns true 50% of the time this is ran.*/
	public static boolean chance()
	{
		return (random(1) == 0) ? true : false;
	}
	
	/**Returns true if a 0 through 100 random value is equal or greater than the given chance.
	1 = 1% Chance, 25 = 25% Chance, 100 = 100% Chance*/
	public static boolean chance(int chance)
	{
		return (random(0, 100) <= chance) ? true : false;
	}
	
	/**Returns true if a 0.0F through 1.0F random value is equal or greater than the given chance.
	0.01F = 1% Chance, 0.25F = 25% Chance, 1.0F = 100% Chance*/
	public static boolean chance(float chance)
	{
		return (random(0.0F, 1.0F) <= chance) ? true : false;
	}
	
	/**Returns true if a 0.0D through 1.0D random value is equal or greater than the given chance.
	0.01D = 1% Chance, 0.25D = 25% Chance, 1.0D = 100% Chance*/
	public static boolean chance(double chance)
	{
		return (random(0.0D, 1.0D) <= chance) ? true : false;
	}
	
	/**Returns a random integer value from 0 through integerA*/
	public static int random(int integerA)
	{
		return random(0, integerA);
	}
	
	/**Returns a random integer value from integerA through integerB*/
	public static int random(int integerA, int integerB)
	{	
		return integerA >= integerB ? integerA : random.nextInt(integerB + 1 - integerA) + integerA;
	}
	
	/**Returns a random float value from 0.0F through floatA*/
	public static float random(float floatA)
	{
		return random(0.0F, floatA);
	}
	
	/**Returns a random float value from floatA through floatB*/
	public static float random(float floatA, float floatB)
	{
		return floatA >= floatB ? floatA : (random.nextFloat() * (floatB - floatA)) + floatA;
	}
	
	/**Returns a random double value from 0.0D through doubleA*/
	public static double random(double doubleA)
	{
		return random(0, doubleA);
	}
	
	/**Returns a random double value from doubleA through doubleB*/
	public static double random(double doubleA, double doubleB)
	{
		return doubleA >= doubleB ? doubleA : (random.nextDouble() * (doubleB - doubleA)) + doubleA;
	}
}