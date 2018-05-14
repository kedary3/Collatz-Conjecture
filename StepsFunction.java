	import java.math.*;
	import java.io.File;
	import java.io.FileNotFoundException;
	import java.io.PrintWriter;
	import java.io.Writer;
	import java.lang.reflect.Array;
	import java.util.*;
	/*
	 * 
	 * 3,1
	 * 4,4
	 * 2,2
	 * -1,1
	 * any power of 2 in both a and b
	 * any power of 2 where a in neg and b is pos
	 * any power of 2 any combo of pos neg except -2^n -2^n
	 * 
	 * 4,8
	 * 6,2
	 * m*(n,3*n)=cyclical for all n
	 * -5,1
	 * -12,4
	 * (3,1) *2^n
	 * (1,2n) = grow to infinity
	 * (1,2n+1) = cyclical for all n>=1
	 * (2,2n-1)= infinity for all n>0
	 * 2n(20,-22) = creates 
	 * 1
		38
		19
		358
		179
		3558
		1779
		35558
		17779
	 * (10,-11) = creates
	 *  1
		19
		179
		1779
		17779
		177779
		1777779
		17777779
	 * (40,-44)n = creates
	  	1
	76
	38
	19
	716
	358
	179
	7116
	3558
	1779
	71116
	35558
	17779
	711116
	355558
	177779
	7111116
	3555558
	1777779
	71111116
	35555558
	17777779
	711111116
	355555558
	177777779
	7111111116
	3555555558
	1777777779
	71111111116
	35555555558
	17777777779
	711111111116
	355555555558
	177777777779
	7111111111116
	3555555555558
	1777777777779
	71111111111116
	35555555555558
	17777777777779
	 * (80,-88) = creates
	  	152
	76
	38
	19
	1432
	716
	358
	179
	14232
	7116
	3558
	1779
	142232
	71116
	35558
	17779
	1422232
	711116
	355558
	177779
	14222232
	7111116
	3555558
	1777779
	142222232
	71111116
	35555558
	17777779
	1422222232
	711111116
	355555558
	177777779
	14222222232
	7111111116
	3555555558
	1777777779
	142222222232
	71111111116
	35555555558
	17777777779
	1422222222232
	711111111116
	355555555558
	177777777779
	14222222222232
	7111111111116
	3555555555558
	 * infinite a==0, b==anything power of 2
	 */
public class StepsFunction //this class is used to generate data on the number of 
						   //steps to the value one or a cyclic behavior under an 
						   //iterative function mod 2 for all integers z.
{


		public static void main(String[] args)
		{
			BigInteger Multiplicative = new BigInteger("3"); // these two numbers will make up your iterative function mod 2
			BigInteger Additive = 		new BigInteger("1"); // for example: (5,1) will be 5x+1, (3,-3) will be 3x-3
			BigInteger IntegerRange = 	new BigInteger("-10000"); // this value represents the range of integer inputs for your specific iterative function from -n to pos n
			boolean cycleStyle = true; // use this boolean to set the prefered cycle data type. true will return cycle length as data point. 
									   // false will return lowest cycle value as cycle data
			int ModuloClass = 2; //Determines Which Modular Class your trajectories will under go. The original is 3x+1 mod 2
			/*for(int i=-113; i<=113; i++) 
			{
				if(i==1) {i++;}
				for(int f=-113; f<=113; f++) 
				{
					if(i%2==0 && f%2==0) {f++;}
					createStepsData(BigInteger.valueOf(i),BigInteger.valueOf(f),IntegerRange,cycleStyle,ModuloClass);
				}
			}*/
			//the above nested for loop allows one to cycle through all the combinations of multiplicative and additive values for bulk data collection under one modular class
			createStepsData(Multiplicative,Additive,IntegerRange,cycleStyle,ModuloClass); // if you don't want to use a for loop, here is a single call of our method			
		}
		public static int createStepsData(BigInteger a,BigInteger b,BigInteger upBound, boolean cycleStyle, int ModuloClass) 
		{
			int index = upBound.abs().intValue();
			BigInteger n = upBound;
			BigInteger one = new BigInteger("1");
			
			BigInteger[] list = new BigInteger[index+upBound.abs().intValue()+1];
			while(n.compareTo(BigInteger.valueOf(index))<=0) //while we have not gone through each integer in our range keep switching to the next integer input
			{
				BigInteger steps = getSteps(n,a,b,cycleStyle,ModuloClass); //gets the number of steps
				if(steps==null)		
				{
					return -1;
				}
				list[((int)n.longValue())+index]=steps; //records the step value
				n=n.add(one); //iterates to next integer
			}
			if(cyclestyle)
			{
			File GraphData = new File("StepsGraph" + "(" + a + "," + b + ")" + "cyclelength" + "mod" +ModuloClass + ".txt");
			}	// this will generate a txt file in your java src file.
			if(!cyclestyle)
			{
				File GraphData = new File("StepsGraph" + "(" + a + "," + b + ")" + "LowestCycleValue" + "mod" +ModuloClass + ".txt");
			}
			try {
				PrintWriter f =new PrintWriter(GraphData);
				for (int i = 0; i < list.length; i++)
				{
					f.println(list[i]);
				}
				f.close();
			} catch (FileNotFoundException e) 
			{
				e.printStackTrace();
			}
			return 0;
			
		}
		public static BigInteger getSteps(BigInteger num,BigInteger a, BigInteger b,boolean cycleStyle,int ModuloClass) //this method returns the number of steps
																				     //to one or the length of the cycle for a given input n
		{ 
			BigInteger steps = BigInteger.ZERO; //start with zero steps
			BigInteger MAX = new BigInteger("10000000000000000000000000"); //defines a max value for the algorithm. 
																		   //if we have our number iterate above this value,
																		   //then we know that we should stop it because it will reach infinity
			BigInteger one = new BigInteger("1");
			BigInteger two = new BigInteger("2");
		    ArrayList<BigInteger> cycleList = new ArrayList<BigInteger>(); //as this algorithm iterates, we need to check against previous
		    															   //numbers in the trajectory if we have been there before.
		    															   //if we have then we can say that this input is cyclic under the iterative map
			while(!BigInteger.ONE.equals(num)) //while this number has not converged to one, keep going.
			{
				if (num.abs().compareTo(MAX)==1) 
				{
					return null; // if we have grown too large, stop. it is easy to show that if one odd number grows without bound then all odd numbers will.
								 // 
				}
			    
			    cycleList.add(num); // maintain a data structure that holds the value of where this number has been under the map so we can determine if it is cyclical.

				if(num.mod(BigInteger.valueOf(ModuloClass))==BigInteger.ZERO) //if our number is even divide it by two
				{
					num =ifEven(num,ModuloClass);
					System.out.println(num);
					//out.print(num);
					
				}
				else if (!BigInteger.ZERO.equals(num)) //if odd, multiply by three and add two
				{
					num =ifOdd(num,a,b);
					System.out.println(num); //these println statements are here to see where the trajectory is going. 
										     //some iterative functions take very long to cycle to iterate to one.  
											 //for example, 8x-16 mod 2 took 24 hours of computation to complete the numbers 1-1000.

					
				}
				if(isCycle(cycleList)) //if we have a cycle, exit the loop and record either the cycle length or the smallest value in the cycle
				{
					if(!cycleStyle) 
					{
						return minimumCycleElement(cycleList).multiply(BigInteger.valueOf(-1));
					}																			//these -1 sings on our cycle data 
																								//are there to distinguish these
																								//data points from those that converge to one
					return BigInteger.valueOf(cycleList.size()*-1);
				}
				
				



					

				
				steps=steps.add(one); // After one iteration, increase the number of steps
			}
			return steps;
			
		}
		public static boolean isCycle(ArrayList<BigInteger> a) //returns a boolean for whether or not the current trajectory is a cycle. 
		{
			if(a.size()==1) 
			{
				return false;
			}
			for(int i=0; i<a.size(); i++) 
			{
				BigInteger temp = a.get(i);
				a.remove(i);
				if(a.contains(temp)) 
				{
					return true;
				}
				a.add(temp);
			}
			return false;
		}
		public static BigInteger minimumCycleElement(ArrayList<BigInteger> a) //returns the smallest element in a cycle. 
																			  //This number defines the cycle as "the n cycle" and separate from other cycles
		{
			for (int i = 0; i < a.size(); i++) 
			{
				if(a.get(i).compareTo(BigInteger.ZERO)==-1) 
				{
					a.set(i, a.get(i).multiply(BigInteger.valueOf(-1)));
				}
			}
			Collections.sort(a);
			return a.get(0);
		}
		public static BigInteger ifEven(BigInteger num,int ModuloClass) //if our number is even divide by two 
		{	
			return num.divide(BigInteger.valueOf(ModuloClass));
		}
		public static BigInteger ifOdd(BigInteger num, BigInteger a , BigInteger b) //if our number is odd multiply by the chosen multiplicative and add the chosen additive
		{
			num=num.multiply(a);
			num=num.add(b);
			return (num);
		}
	}


