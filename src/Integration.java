/**
 * 
 * @author 15rytlewskim
 * @details object for integration related methods and functions
 * 
 */

public class Integration extends Calculus
{
	//constructor
	public Integration()
	{

	}//end constructor

	//procedure to integrate a given basic equation
	public String integrate(String szUserInput , int iSolType)
	{
		//declare variables
		String szIntegrated = "" ;
		String szFunction = "" ;
		int iCoefficient = 0 ;
		int iPower = 0 ;
		float fIntegCoeff = 0 ;
		int iIntegCoeff = 0 ;
		int iIntegPower = 0 ;
		String szIntegCoeff = "" ;
		String szIntegPower = "" ;
		int iTerm = 0 ;

		//declare arrays
		String[] szTermArray ;
		String[] szCoefficientArray ;
		String[] szPowerArray ;
		String[] szOperatorsArray ;

		//call function to check for negative powers
		szFunction = checkNegative(szUserInput)  ;

		//call function to split the users input on + and - into separate terms
		szTermArray = splitInput(szFunction, "[+-]")  ;

		//call function to find the operators within the equation and input them
		//into an array one by one
		szOperatorsArray = findOperators (szFunction , szTermArray.length)  ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "\nAdd 1 to the power, then divide the coefficient by the new power to integrate each term.\n") ;

		//use a for loop to go through and integrate each term
		//within the entered equation separately
		for (iTerm = 0  ; iTerm < szTermArray.length  ; iTerm ++)
		{
			//call function to split array on * and ^
			szCoefficientArray = splitInput(szTermArray[iTerm] , "\\*") ;
			szPowerArray = splitInput(szCoefficientArray[1] , "\\^") ;

			//use an if statement to check if a negative power is being used by searching for an '_'
			if (szPowerArray[1].contains("_"))
			{
				//convert the power into an integer and multiply by -1 to get a negative
				iPower = Integer.parseInt(szPowerArray[1].substring(1, 2)) ;
				iPower = iPower / -1  ;
			}
			else
			{
				//convert the power into an integer
				iPower = Integer.parseInt(szPowerArray[1]) ;
			}

			//convert the coefficient of the term into an integer
			iCoefficient = Integer.parseInt(szCoefficientArray[0]) ;

			//calculate the new power and coefficient of the term
			iIntegPower = iPower + 1 ;
			fIntegCoeff = (float) iCoefficient / (float) iIntegPower ;

			//use an if statement to check if the new coefficient is a decimal or not
			if (fIntegCoeff % 1 != 0)
			{
				//convert the new coefficient into a string
				szIntegCoeff = Float.toString(fIntegCoeff) ;
			}
			else if (fIntegCoeff == 1)
			{
				//convert the new coefficient into a string
				szIntegCoeff = "" ;
			}
			else
			{
				//convert the coefficient into an int
				iIntegCoeff = (int) fIntegCoeff ;

				//convert the new coefficient into a string
				szIntegCoeff = Integer.toString(iIntegCoeff) ;
			}

			//convert the new power into a string and add both the new coefficient
			//and power to the string of the final integrated result
			szIntegPower = Integer.toString(iIntegPower) ;

			//use an if statement to decide if there is a power of 0 or not
			if (szIntegPower.equals("0"))
			{
				szIntegrated = szIntegrated + "" + szIntegCoeff + "" + szOperatorsArray[iTerm] + "" ;

			}
			else
			{
				szIntegrated = szIntegrated + "" + szIntegCoeff + "*"
						+ szPowerArray[0] + "^" + szIntegPower + "" + szOperatorsArray[iTerm] + "" ;
			}


		}//end for

		//replace any double operator signs with the correct singular sign
		szIntegrated = szIntegrated.replaceAll("\\-\\-" , "\\+").replaceAll("\\+\\-", "-") ;

		//return the integrated function
		return szIntegrated ;

	}//end function

	//function to calculate the area under a curve using definite integrals
	public float calcDefiniteArea (String szCurve , int iLowerBound , int iUpperBound , int iSolType)
	{
		//declare variables
		float fUpperArea = 0 ;
		float fLowerArea = 0 ;
		float fTotalArea = 0 ;
		String szIntegral = "" ;
		String szUpperSubstituted = "" ;
		String szLowerSubstituted = "" ;

		//integrate the entered function 
		szIntegral = integrate(szCurve , iSolType) ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "\nThe integrated function is: " + szIntegral.replaceAll("\\*", "").replaceAll("\\^1", "").replaceAll("x\\^0", "") + "\n" ) ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "In order to calculate the area under the curve, subtract the substituted lower limit from the substituted upper limit into the integrated function. " ) ;

		//substitute the x's in the calculated integral with the upper bound entered by the user
		szUpperSubstituted = substitute(szIntegral , iUpperBound) ;

		//substitute the x's in the calculated integral with the lower bound entered by the user
		szLowerSubstituted = substitute(szIntegral , iLowerBound) ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "Area = (" + szUpperSubstituted + ") - (" + szLowerSubstituted + ")") ;

		//call function to evaluate the integral for the upper bound
		fUpperArea = evaluateFunction(szIntegral , iUpperBound) ;

		//call function to evaluate the integral for the lower bound
		fLowerArea = evaluateFunction(szIntegral , iLowerBound) ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "     = (" + (float) Math.round(fUpperArea * 100) / 100 + ") - (" 
				+ (float) Math.round(fLowerArea * 100) / 100 + ")") ;

		//calculate the total area
		fTotalArea = fUpperArea - fLowerArea ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "     = " + (float) Math.round(fTotalArea * 100) / 100 + "\n" ) ;

		return fTotalArea;

	}//end function

	//function to calculate area using the trapezium rule
	public double calcAreaTrap (String szFunction , int iLowerBound , int iUpperBound , int iBarAmount , int iSolType)
	{
		//declare variables
		String szSubstitutedXValue = "" ;
		float[] fXValues = new float[iBarAmount + 1] ;
		float[] fYValues = new float[iBarAmount + 1] ;
		double dArea = 0 ;
		float fTrapRuleBracket = 0 ;
		float fBarWidth = 0 ;
		float fCurrentXValue = 0 ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "\nFirstly, we need to calculate the width of each bar, so that we can calculate all the required x-values.\n") ;

		//calculate the width of each bar
		fBarWidth = (float) (iUpperBound - iLowerBound) / iBarAmount ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "Bar Width = ( " + iUpperBound + " - " + iLowerBound + " ) / " + iBarAmount ) ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "          = " + (float) Math.round(fBarWidth * 100) / 100 + "\n" );

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "Now we can use the width of each bar to calculate all of the x-values.\n") ;

		//set the first index of the array of x values as the lower bound entered by the user
		fXValues[0] = iLowerBound ;

		//set the current X value as the lower bound
		fCurrentXValue = iLowerBound ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "X-Value 1 = " + iLowerBound) ;

		//use a for loop to calculate all of the x values
		for (int i = 1 ; i < fXValues.length ; i ++ )
		{
			//assign the next x value to the next index of the array of x values
			fXValues[i] = fCurrentXValue + fBarWidth ;

			//change the current x value by adding the width of one bar onto it
			fCurrentXValue = fCurrentXValue + fBarWidth ;

			//call procedure to decide solution type wanted by the user
			decideSolType(iSolType , "X-Value " + (i + 1) + " = " + (float) Math.round(fXValues[i] * 100) / 100) ;

		}//end if

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "\nNow we need to calculate all of the corresponding Y-values to the X-values that we just obtained.\n") ;

		//use a for loop to calculate the corresponding y-value to each of the x-values
		for (int i = 0 ; i < fXValues.length ; i ++)
		{
			//plug in the current x value into the original equation
			szSubstitutedXValue = substitute(szFunction , fXValues[i]) ;

			//call function to calculate the corresponding y-value to the current x-value
			fYValues[i] = evaluateFunction(szFunction , fXValues[i]) ;

			//call procedure to decide solution type wanted by the user
			decideSolType(iSolType , "Y-Value " + (i + 1) + " = " + szSubstitutedXValue + " = " + (float) Math.round(fYValues[i] * 100) / 100) ;

		}//end for

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "\nNow we can finally use the trapezium rule to calculate an estimate for the area.\n");

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "Area = 0.5h(y0 + yn + 2(y1 + y2 + ... + yn-1)");

		//use a for loop to calculate part of the formula for the calculating the area using the trapezium rule
		for (int i = 1 ; i < (fYValues.length - 1) ; i ++)
		{
			//calculate the part of the trapezium rule that is (y1 + y2 + ... + yn-1)
			fTrapRuleBracket = fTrapRuleBracket + fYValues[i] ;

		}

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "     = 0.5(" + fBarWidth + ")(" + fYValues[0] + " + " + (float) Math.round(fYValues[(fYValues.length - 1)] * 100) / 100 + " "
				+ "+ 2(" + (float) Math.round(fYValues[1] * 100) / 100 + " + " + (float) Math.round(fYValues[2] * 100) / 100 + " + ... + " 
				+ (float) Math.round(fYValues[fYValues.length - 2] * 100) / 100 + ")");

		//use the trapezium rule to calculate an estimate for the area under the curve entered by the user
		dArea = (0.5 * fBarWidth) * (fYValues[0] + fYValues[fYValues.length - 1] + 2 *(fTrapRuleBracket)) ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "     = " + (double) Math.round(dArea * 100) / 100  + "\n") ;

		return dArea ;

	}//end function

	//procedure to calculate the percentage error in estimating the area using trapezium rule
	public void calcPercentError (String szFunction , int iLowerBound , int iUpperBound , int iBarAmount , int iSolType)
	{
		//declare variables
		double dDefiniteArea = 0 ;
		double dEstimateArea = 0 ;
		double dPercentError = 0 ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "\nIn order to calculate the %% error in the estimate made by the trapezium rule, we firstly need to calculate\n"
				+ "the area using both definite integrals and using the trapezium rule.\n" ) ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "Firstly, lets calculate the area using definite integrals.\n" ) ; 

		//call function to calculate area using definite integrals
		dDefiniteArea = calcDefiniteArea(szFunction, iLowerBound, iUpperBound, iSolType) ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "Now, lets calculate the area using the trapezium rule.\n" ) ; 

		//call function to calculate area using the trapezium rule
		dEstimateArea = calcAreaTrap(szFunction, iLowerBound, iUpperBound, iBarAmount, iSolType) ;

		//calculate the percentage error
		dPercentError = ( (dEstimateArea - dDefiniteArea) / dDefiniteArea ) * 100 ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "Now that we have calculated both areas, we can calculate the percentage error in the estimate.\n") ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "%% Error = ((Estimate Area - Exact Area) / Exact Area) * 100") ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "        = ((" + (double) Math.round(dEstimateArea * 100) / 100 + " - " + (double) Math.round(dDefiniteArea * 100) / 100 + ") / " 
				+ (double) Math.round(dDefiniteArea * 100) / 100 +  ") * 100") ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "        = " + (double) Math.round(dPercentError * 100) / 100 + "%%" ) ;

		//use an if statement to check if the user would just like the answer output
		if (iSolType == 3)
		{
			//print out the answer for the user
			System.out.println("\n% Error in Trapezium Rule Estimate = " + (double) Math.round(dPercentError * 100) / 100 + "%" ) ;

		}//end if

	}//end procedure

	public static void main(String[] args) 
	{
		//declare integration object
		Integration IntegObject = new Integration() ;

		//call procedure to integrate
		IntegObject.integrate("5*x^4+7*x^-3" , 1) ;

		//call procedure to calculate area under curve
		IntegObject.calcDefiniteArea("1*x^2+5*x^1+4*x^0", 1, 3 , 2) ;

		//call procedure to calculate area under curve using trapezium rule
		IntegObject.calcAreaTrap("4*x^0-1*x^2", -2, 2, 10 , 2) ;

		//call procedure to calculate % error in using trapezium rule
		IntegObject.calcPercentError("7*x^3+5*x^1", 0 , 6 , 6 , 1) ;

	}//end main

}//end class