/**
 * 
 * @author 15rytlewskim
 * @details object for differentiation related methods and functions
 * 
 */

//import classes
import com.groupdocs.conversion.*;
import com.groupdocs.conversion.filetypes.FileType;
import com.groupdocs.conversion.options.convert.ConvertOptions;

public class Differentiation extends Calculus
{
	//constructor
	public Differentiation()
	{

	}//end constructor

	//procedure to differentiate a given basic equation
	public String differentiate(String szUserInput , int iSolType)
	{

		//declare variables
		String szDifferentiated = "" ;
		String szFunction = "" ;
		int iCoefficient = 0 ;
		int iPower = 0 ;
		int iDiffCoeff = 0 ;
		int iDiffPower = 0 ;
		int iStartTerm = 0 ;
		String szDiffCoeff = "" ;
		String szDiffPower = "" ;
		int iTerm = 0 ;

		//declare arrays
		String[] szTermArray ;
		String[] szCoefficientArray ;
		String[] szPowerArray ;
		String[] szOperatorsArray ;	

		//call function to check for negative powers
		szFunction = checkNegative(szUserInput) ;

		//call function to split the users input on + and - into separate terms
		szTermArray = splitInput(szFunction, "[+-]") ;

		//call function to find the operators within the equation and input them
		//into an array one by one
		szOperatorsArray = findOperators (szFunction , szTermArray.length) ;

		//use an if statement to decide whether the entered function needs to be differentiated
		//using product rule, quotient rule or normally
		if (szFunction.contains("("))
		{

		}

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "\nMultiply the coefficient by the current power, then subtract 1 from the power to differentiate each term.") ;

		//use an if statement to decide if the first term is negative or not
		if (szFunction.charAt(0) == '-')
		{
			//set the first term to 1
			iStartTerm = 1 ;
		}
		else
		{
			//set the first term to 0
			iStartTerm = 0 ;

		}//end if

		//use a for loop to go through and differentiate each term
		//within the entered equation separately
		for (iTerm = iStartTerm ; iTerm < szTermArray.length ; iTerm ++)
		{
			//call functions to split the inputs on *'s and ^'s
			szCoefficientArray = splitInput(szTermArray[iTerm] , "\\*") ;
			szPowerArray = splitInput(szCoefficientArray[1] , "\\^") ;

			//use an if statement to check if a negative power is being used by searching for an '_'
			if (szPowerArray[1].contains("_"))
			{
				//convert the power into an integer and multiply by -1 to get a negative
				iPower = Integer.parseInt(szPowerArray[1].substring(1, 2)) ;
				iPower = iPower / -1 ;
			}
			else
			{
				//convert the power into an integer
				iPower = Integer.parseInt(szPowerArray[1]) ;
			}

			//convert the coefficient of the term into an integer
			iCoefficient = Integer.parseInt(szCoefficientArray[0]) ;

			//calculate the new coefficient and power of the term
			iDiffCoeff = iCoefficient * iPower ;
			iDiffPower = iPower - 1 ;

			//convert the new values into strings and add them to the string of
			//the final differentiated result
			szDiffCoeff = Integer.toString(iDiffCoeff) ;
			szDiffPower = Integer.toString(iDiffPower) ;

			//use an if statement to decide if the first term is negative or not and so if to
			//put a minus sign in front of it or not
			if (iStartTerm == 1 && iTerm == 1)
			{
				//construct the fully differentiated equation
				szDifferentiated = szDifferentiated + "-" + szDiffCoeff + "*"
						+ szPowerArray[0] + "^" + szDiffPower + "" + szOperatorsArray[iTerm] + "" ;
			}
			else
			{

				//construct the fully differentiated equation
				szDifferentiated = szDifferentiated + "" + szDiffCoeff + "*"
						+ szPowerArray[0] + "^" + szDiffPower + "" + szOperatorsArray[iTerm] + "" ;

			}//end if

		}//end for

		//get rid of any terms that are 0x^-1 as these were originally single terms with no x therefore,
		//when differentiating, would make 0 and so there is no reason to keep them within the equation
		szDifferentiated = szDifferentiated.replaceAll("0\\*x\\^-1", "") ; //if its the first term
		szDifferentiated = szDifferentiated.replaceAll("\\+0\\*x\\^-1", "") ; //if the term was positive
		szDifferentiated = szDifferentiated.replaceAll("-0\\*x\\^-1", "") ; //if the term was negative

		//get rid of any double - signs or +- signs with the one correct sign
		szDifferentiated = szDifferentiated.replaceAll("--", "+") ;
		szDifferentiated = szDifferentiated.replaceAll("\\+-", "-") ;

		//use an if statement to check if there is a + sign as the first character, if there is, remove it
		if (szDifferentiated.charAt(0) == '+')
		{
			//remove the + sign
			szDifferentiated = szDifferentiated.replaceFirst("\\+", "") ;

		}//end if

		return szDifferentiated ;

	}//end function

	//procedure to calculate the gradient at a given point on a curve
	public float calcGradient(String szFunction , float fXCoordinate , int iSolType)
	{
		//declare variables
		String szDifferential = "" ;
		String szSubstitutedCoord = "" ;
		float fGradient = 0 ;

		//differentiate the function that the user entered
		szDifferential = differentiate(szFunction , iSolType) ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "The differentiated function is: " + szDifferential.replaceAll("\\*", "").replaceAll("\\^1", "").replaceAll("x\\^0", "") + "\n" ) ;

		//plug in the x coordinate entered by the user into the differentiated function
		szSubstitutedCoord = substitute(szDifferential , fXCoordinate) ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "Now substitute the x values with the x-coordinate:\ndy/dx = " + szSubstitutedCoord.replaceAll("\\*\\(" + fXCoordinate + "\\)\\"
				+ "^0", "").replaceAll("\\^1", "").replaceAll("_", "-")) ; //replace *(X-Coordinate)^0 and ^1 with nothing for cleaner output, also replace all _ with - signs

		//use a for loop to go through and calculate the result of each term
		fGradient = evaluateFunction (szDifferential , fXCoordinate) ;

		return fGradient ;

	}//end function

	//procedure to calculate the equation of the tangent or normal to a curve at a given point
	public void calcTangOrNorm (String szFunction , int iXCoord , int iYCoord , boolean bNormal , int iSolType)
	{
		//declare variables
		float fOldGradient = 0 ;
		float fNewGradient = 0 ;
		float fRoundedGradient = 0 ;
		String szTangentOrNormal = "y-y1=m(x-x1)" ; //generic formula for linear equation
		float fFirstCValue = 0 ;
		float fFinalCValue = 0 ;
		float fRoundedFinalCValue = 0 ;

		//call function to calculate gradient of the the curve at the specified x-coordinate
		//and so the gradient of the tangent/normal at that point
		fOldGradient = calcGradient(szFunction , iXCoord , iSolType) ;

		//call procedure to decide solution type wanted by the user
		decideSolType (iSolType , "      = " + (float) (Math.round(fOldGradient * 100.0) / 100.0)) ;

		//use an if statement to decide if you need to calculate the tangent or normal
		if (bNormal == true)
		{
			//use an if statement to check if the original gradient is 0 or not, if its not, then calculate the new gradient
			if (fOldGradient != 0)
			{
				//calculate the negative reciprocal of the gradient
				fNewGradient = (float) -1 / fOldGradient ;
			}

			//call procedure to decide solution type wanted by the user
			decideSolType (iSolType , "\nCalculate the negative reciprocal of the gradient:\nm = -1 / " + fOldGradient + "\n  = "
					+ "" + (float) (Math.round(fNewGradient * 100.0) / 100.0));

		}
		else
		{
			//the gradient will not change
			fNewGradient = fOldGradient ; 
			
		}//end if

		//call procedure to decide solution type wanted by the user
		decideSolType (iSolType , "\nSubsitute y1 with the Y-Coordinate, x1 with the X-Coordinate and m with the calculated gradient:\n"
				+ szTangentOrNormal + "     (" + iXCoord + "," + iYCoord + ")") ;

		//use an if statement to decide if the x coordinate entered by the user is negative which would cause a sign change 
		if (iXCoord <= 0)
		{
			//replace the second - sign with a +
			szTangentOrNormal = szTangentOrNormal.replaceAll("-x1","+x1") ;

			//change the X value to positive
			iXCoord = iXCoord / -1 ;
		}
		else
		{
			//if the x coordinate is positive that means it will be taken away in the bracket on the right hand side
			//so remove the - sign and change the x value to be negative for future calculations and just insert it
			//into the string
			iXCoord = iXCoord / -1 ;
			szTangentOrNormal = szTangentOrNormal.replaceAll("-x1" , "x1") ;
		}

		//use an if statement to decide if the y coordinate entered by the user is negative which would cause a sign change
		if (iYCoord <= 0)
		{
			//replace the first - sign with a +
			szTangentOrNormal = szTangentOrNormal.replaceAll("-y1","+y1") ;

			//change the Y value to positive
			iYCoord = iYCoord / -1 ;

		}
		else
		{
			//if the y coordinate is positive that means it will be taken away on the left hand side
			//so remove the - sign and change the y value to be negative for future calculations and just insert it
			//into the string without any operation signs as the - in the negative will provide the - sign
			iYCoord = iYCoord / -1 ;
			szTangentOrNormal = szTangentOrNormal.replaceAll("-y1=" , "y1=") ;
		}

		//and replace y1 , m and x1 with the calculated gradient and the coordinates entered by the user
		szTangentOrNormal = szTangentOrNormal.replaceAll("y1", Integer.toString(iYCoord)) ;
		szTangentOrNormal = szTangentOrNormal.replaceAll("x1", Integer.toString(iXCoord)) ; 
		szTangentOrNormal = szTangentOrNormal.replaceAll("m", Float.toString(fNewGradient)) ; 

		//call procedure to decide solution type wanted by the user
		decideSolType (iSolType , szTangentOrNormal + "    <-- Now you need to expand the bracket on the RHS") ;

		//expand the bracket on the right hand side
		fFirstCValue = fNewGradient * iXCoord ;

		//round the gradient to 2dp
		fRoundedGradient = (float) (Math.round(fNewGradient * 100.0) / 100.0) ;

		//use an if statement to check if the first C Value is negative, 0 or not
		if (fFirstCValue <= 0)
		{
			//use an if statement to check if the gradient is negative or not
			if (fNewGradient <= 0)
			{
				//as the gradient is negative and the first c value is negative, that means that the right side had a + sign
				//so we need to search for a + sign instead of a -

				//don't add any operator sign on the right hand side was here side as that will be covered by the fact that the current
				//c value on the right hand side is negative, so that will provide the equation with the - sign
				szTangentOrNormal = szTangentOrNormal.replaceAll(fNewGradient + "\\(x\\+" + iXCoord + "\\)",  + fRoundedGradient + "x" + fFirstCValue) ;
			}
			else
			{
				//as the gradient is positive and the first c value is negative, that means that the right side had a - sign
				//so we need to search for a - sign, so we don't input any sign to search for as that - sign is included
				//as part of the x value

				//don't add any operator sign on the right hand side as that will be covered by the fact that the current
				//c value on the right hand side is negative, so that will provide the equation with the - sign
				szTangentOrNormal = szTangentOrNormal.replaceAll(fNewGradient + "\\(x" + iXCoord + "\\)",  + fRoundedGradient + "x" + fFirstCValue) ;

			}//end if

		}
		else
		{
			//use an if statement to check if the gradient is negative or not
			if (fNewGradient <= 0)
			{
				//as the gradient is negative but the first c value is positive, that means that the right side had a - sign
				//so we need to search for a - sign instead of a +, so we don't input any sign to search for as that - sign is included
				//as part of the x value

				//make sure that the sign on the right side is a + when building the string
				szTangentOrNormal = szTangentOrNormal.replaceAll(fNewGradient + "\\(x" + iXCoord + "\\)",  + fRoundedGradient + "x+" + fFirstCValue) ;
			}
			else
			{
				//as the gradient is positive and the first c value is positive, that means that the right side had a + sign
				//so we need to search for a + sign

				//make sure that the sign on the right side is a + when building the string
				szTangentOrNormal = szTangentOrNormal.replaceAll(fNewGradient + "\\(x\\+" + iXCoord + "\\)",  + fRoundedGradient + "x+" + fFirstCValue) ;

			}//end if

		}//end if

		//call procedure to decide solution type wanted by the user
		decideSolType (iSolType , szTangentOrNormal + "    <-- Now you need to subtract the final term from the LHS") ;

		//calculate the final c value
		fFinalCValue = fFirstCValue - iYCoord ; 

		//round the final C value to 2dp
		fRoundedFinalCValue = (float) (Math.round(fFinalCValue * 100.0) / 100.0) ;

		//remove everything on the left side of the equation, just leaving y=
		szTangentOrNormal = szTangentOrNormal.replaceAll("\\+" + iYCoord + "=", "=") ; //if the left hand side has y + y1
		szTangentOrNormal = szTangentOrNormal.replaceAll(iYCoord + "=", "=") ; //if the left hand side has y - y1 already

		//use an if statement to decide if the final value of c is negative as that will affect the sign
		//that needs to be used on the right side of the equation
		if (fFinalCValue < 0)
		{
			//don't add any operator sign on the right hand side as that will be filled by the fact that the current
			//c value on the right hand side is negative, so that will provide the - sign
			szTangentOrNormal = szTangentOrNormal.replaceAll("x\\+" + fFirstCValue, "x" + fRoundedFinalCValue) ; //if the right hand side has mx + c
			szTangentOrNormal = szTangentOrNormal.replaceAll("x" + fFirstCValue, "x" + fRoundedFinalCValue) ; //if the right hand side has mx - c already

		}
		else
		{
			//make sure that the sign on the right side is a + when building the string
			szTangentOrNormal = szTangentOrNormal.replaceAll("x\\+" + fFirstCValue, "x+" + fRoundedFinalCValue) ; //if the first c value was positive
			szTangentOrNormal = szTangentOrNormal.replaceAll("x" + fFirstCValue, "x+" + fRoundedFinalCValue) ; //if the first c value was negative

		}//end if

		//use an if statement to check if the equation contains a minus 0 or not
		if (fRoundedFinalCValue == 0 )
		{
			//get rid of the -0 as it is unnecessary
			szTangentOrNormal = szTangentOrNormal.substring(0 , szTangentOrNormal.length() - 4 ) ; // the last 4 characters will be -0.0 so eliminate them
		}

		//print out the final result, using an if statement to decide if its a tangent or normal
		if (bNormal == true)
		{

			System.out.println(szTangentOrNormal + "    <-- Equation of Normal");
		}
		else
		{
			System.out.println(szTangentOrNormal + "    <-- Equation of Tangent");
		}

	}//end procedure

	//function to calculate coordinates of any stationary points on the function entered by the user
	public void calcStatPoints(String szEquation , int iSolType)
	{
		//declare variables
		String szDifferential = "" ;
		String szCurrentTerm = "" ;
		String szSubstitutedValue = "" ;
		int iDiscriminant = 0 ;
		int iStartTerm = 0 ;
		String[] szTermArray ;
		int[] iQuadCoeffs = new int[3] ;
		float[] fXCoordinates ;
		float[] fYCoordinates ;
		int iTermAmount = 0 ;
		boolean bSquaredTerm = false ;
		boolean bXTerm = false ;
		boolean bConstant = false ;

		//call function to differentiate the entered equation once
		szDifferential = differentiate (szEquation , iSolType) ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "\nThe differentiated function is: " + szDifferential.replaceAll("\\*", "").replaceAll("\\^1", "").replaceAll("x\\^0", "") + "\n" ) ;

		//split the differentiated function into separate terms
		szTermArray = splitInput(szDifferential , "[+-]") ;

		//use an if statement to decide if the first term is negative or not
		if (szDifferential.charAt(0) == '-')
		{
			//set the first term to 1 as the 0th term will just be a - sign
			iStartTerm = 1 ;
		}
		else
		{
			//set the first term to 0
			iStartTerm = 0 ;

		}//end if

		//use an if statement to decide if the differentiated function is a quadratic
		//or a linear equation
		if (szTermArray[iStartTerm].contains("^2"))
		{
			//call procedure to decide solution type wanted by the user
			decideSolType(iSolType , "Since the differentiated function is a quadratic, we can use the quadratic formula to find the x-coordinates\n"
					+ "of the stationary points. However, first we must check if the quadratic has any real solutions using the discriminant (b^2-4ac):") ;

			//find out how many terms the quadratic has
			iTermAmount = szTermArray.length ;

			//use an if statement to check which terms the quadratic has and doesn't have
			if (iTermAmount < iStartTerm + 3)
			{
				//use a for loop go through each term and find out which terms are missing
				for (int i = iStartTerm ; i < iTermAmount ; i ++)
				{
					//assign the current term to a String variable
					szCurrentTerm = szTermArray[i] ;

					//use an if statement to identify what the current term is
					if (szCurrentTerm.contains("^2"))
					{
						//identify that there is an x squared term in the equation
						bSquaredTerm = true ;
					}
					else if (szCurrentTerm.contains("^1"))
					{
						//identify that there is an x term in the equation
						bXTerm = true ;
					}
					else if (szCurrentTerm.contains("^0"))
					{
						//identify that there is a constant term in the equation
						bConstant = true ;

					}//end if

				}//end for

			}
			else
			{
				//identify that all terms are present
				bSquaredTerm = true ;
				bXTerm = true ;
				bConstant = true ;

			}//end if

			//assign the correct value to a
			if (bSquaredTerm == true)
			{
				//fetch the coefficient of x^2 and parse it into an int
				iQuadCoeffs[0] = Integer.parseInt(String.valueOf(szTermArray[iStartTerm].substring(0, (szTermArray[iStartTerm].indexOf("*"))))) ;
				//the last part of the statement is so that it fetches all the digit prior to the * sign, just in case the coefficient is greater than one digit

				//use an if statement to check if the coefficient should be negative based on if there is a minus sign before it
				if (szDifferential.startsWith("-"))
				{
					//multiply the coefficient by -1
					iQuadCoeffs[0] = iQuadCoeffs[0] * (-1) ;

				}//end if

			}
			else
			{
				//set the x^2 coefficient of 0
				iQuadCoeffs[0] = 0 ;

			}//end if

			//assign the correct value to b
			if (bXTerm == true)
			{
				//fetch the coefficient of x and parse it into an int
				iQuadCoeffs[1] = Integer.parseInt(String.valueOf(szTermArray[iStartTerm + 1].substring(0, (szTermArray[iStartTerm + 1].indexOf("*"))))) ;
				//the last part of the statement is so that it fetches all the digit prior to the * sign, just in case the coefficient is greater than one digit

				//use an if statement to check if the coefficient should be negative based on if there is a minus sign before it
				if (szDifferential.contains("-" + szTermArray[1]))
				{
					//multiply the coefficient by -1
					iQuadCoeffs[1] = iQuadCoeffs[1] * (-1) ;

				}//end if
			}
			else
			{
				//set the x coefficient of 0
				iQuadCoeffs[1] = 0 ;

			}//end if

			//assign the correct value to c
			if (bConstant == true)
			{
				//fetch the constant and parse it into an int
				iQuadCoeffs[2] = Integer.parseInt(String.valueOf(szTermArray[iStartTerm + 2].substring(0, (szTermArray[iStartTerm + 2].indexOf("*"))))) ;
				//the last part of the statement is so that it fetches all the digit prior to the * sign, just in case the coefficient is greater than one digit

				//use an if statement to check if the coefficient should be negative based on if there is a minus sign before it
				if (szDifferential.contains("-" + szTermArray[2]))
				{
					//multiply the coefficient by -1
					iQuadCoeffs[2] = iQuadCoeffs[2] * (-1) ;

				}//end if
			}
			else
			{
				//set the constant to 0
				iQuadCoeffs[2] = 0 ;

			}//end if

			//call procedure to decide solution type wanted by the user
			decideSolType(iSolType , "\nb^2 - 4ac = (" + iQuadCoeffs[1] + ")^2 - 4(" + iQuadCoeffs[0] + ")(" + iQuadCoeffs[2] + ")");

			//calculate the discriminant
			iDiscriminant = ((int) Math.pow(iQuadCoeffs[1], 2)) - 4*(iQuadCoeffs[0])*(iQuadCoeffs[2]) ;

			//call procedure to decide solution type wanted by the user
			decideSolType(iSolType , "          = " + iDiscriminant);

			//use an if statement to decide if the discriminant is positive or negative
			if (iDiscriminant >= 0)
			{
				//use an if statement to decide if the discriminant is equal to or greater than 0
				//to decide if you need to work out 1 solution or two solutions
				if (iDiscriminant == 0)
				{
					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "\nSince the Discriminant is equal to 0, there is only one real solution to the quadratic,\n"
							+ "and therefore, the entered cubic has only one turning point.");

					//set the arrays to length 1
					fXCoordinates = new float[1] ;
					fYCoordinates = new float[1] ;

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "\nNow we can use the quadratic formula to solve for x:" ) ;

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "x = (-b +- root(b^2 - 4ac))/2a\n" ) ;

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "x = (-(" + iQuadCoeffs[1] + ") +- root(0)) / 2(" + iQuadCoeffs[0] + ")" ) ;

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "x = (" + (iQuadCoeffs[1] / -1) + ") / " + (iQuadCoeffs[0] * 2)) ;

					//use the quadratic formula to calculate the single x-value
					fXCoordinates[0] = (- (iQuadCoeffs[1])) / 2*iQuadCoeffs[0] ;

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "x = " + (float) Math.round(fXCoordinates[0] * 100) / 100 ) ;

					//call function to calculate y coordinate
					fYCoordinates[0] = evaluateFunction(szEquation , fXCoordinates[0]) ;

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "\n\nNow plug in the calculated x-value back into the original equation:" ) ;

					//plug in the x coordinate into the equation
					szSubstitutedValue = substitute(szEquation , fXCoordinates[0]) ; 

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "y = " + szSubstitutedValue ) ;

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "  = " + (float) Math.round(fYCoordinates[0] * 100) / 100 ) ;

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "\n\nTherefore, the coordinates of the turning point are: ( " + (float) Math.round(fXCoordinates[0] * 100) / 100 
							+ " , " + (float) Math.round(fYCoordinates[0] * 100) / 100 + " )" ) ;

				}
				else
				{
					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "\nSince the Discriminant is greater than 0, there are two real solutions to the quadratic,\n"
							+ "and therefore, the entered cubic has two turning points." ) ;

					//set the arrays to length 2
					fXCoordinates = new float[2] ;
					fYCoordinates = new float[2] ;

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "\nNow we can use the quadratic formula to solve for x:\n" ) ;

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "x = (-b +- root(b^2 - 4ac))/2a" ) ;

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "x = (-(" + iQuadCoeffs[1] + ") +- root(" + iDiscriminant + ")) / 2(" + iQuadCoeffs[0] + ")" ) ;

					//use the quadratic formula to calculate the two x-values
					fXCoordinates[0] =  (float) ((-1) * (iQuadCoeffs[1]) + Math.sqrt(iDiscriminant)) / (2*iQuadCoeffs[0]) ;
					fXCoordinates[1] =  (float) ((-1) * (iQuadCoeffs[1]) - Math.sqrt(iDiscriminant)) / (2*iQuadCoeffs[0]) ;

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "x = " + (float) Math.round(fXCoordinates[0] * 100) / 100 + " or x =" + (float) Math.round(fXCoordinates[1] * 100) / 100 ) ;

					//call function to calculate y coordinates
					fYCoordinates[0] = evaluateFunction(szEquation , fXCoordinates[0]) ;
					fYCoordinates[1] = evaluateFunction(szEquation , fXCoordinates[1]) ;

					//plug in the first x coordinate into the equation and output it as the next step
					szSubstitutedValue = substitute(szEquation , fXCoordinates[0]) ; 

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "\nNow plug in the calculated x-values back into the original equation:" ) ;

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "y = " + szSubstitutedValue ) ;

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "  = " + (float) Math.round(fYCoordinates[0]*100) / 100 ) ;

					//plug in the second x coordinate into the equation and output it as the next step
					szSubstitutedValue = substitute(szEquation , fXCoordinates[1]) ; 

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "\ny = " + szSubstitutedValue ) ;

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "  = " + (float) Math.round(fYCoordinates[1] * 100) / 100 ) ;

					//call procedure to decide solution type wanted by the user
					decideSolType(iSolType , "\nTherefore, the coordinates of the turning points are:\n"
							+ "(" + (float) Math.round(fXCoordinates[0]*100) / 100 + " , " + (float) Math.round(fYCoordinates[0]*100) / 100 + ")" + 
							"   " + "(" + (float) Math.round(fXCoordinates[1]*100) / 100 + " , " + (float) Math.round(fYCoordinates[1]*100) / 100 + ")" ) ; 

				}//end if

				//use an if statement to check if the user just wants the answer
				if (iSolType == 3)
				{
					//print out the answer
					System.out.println("\nCoordinates of Turning Points: " 
							+ "(" + (float) Math.round(fXCoordinates[0]*100) / 100 + " , " + (float) Math.round(fYCoordinates[0]*100) / 100 + ")" + 
							"   " + "(" + (float) Math.round(fXCoordinates[1]*100) / 100 + " , " + (float) Math.round(fYCoordinates[1]*100) / 100 + ")" ) ; 

				}//end if

			}
			else
			{
				//print out that there are not real solutions
				System.out.println("\nThere are no real solutions to the differential and so the entered equation has no real stationary points.\n");

			}//end if

		}
		else if (szTermArray[iStartTerm].contains("^1"))
		{
			//declare variables
			String szLinearEquation = "y=mx+c" ; 
			String[] szTermsArray ; 
			String[] szOperators = null ;
			int[] iCoefficients ;

			//instantiate the x and y coordinate arrays with length 1 as there will only be 1 solution
			fXCoordinates = new float[1] ;
			fYCoordinates = new float[1] ;

			//call procedure to decide solution type wanted by the user
			decideSolType(iSolType , "Since the differentiated function is a linear equation, we can just rearrange to solve for x." ) ;

			//split the differentiated function into separate terms
			szTermsArray = splitInput(szDifferential, "[+-]") ;

			//use an if statement to check if there are two terms in the equation
			if (szTermsArray.length == iStartTerm + 2)
			{
				//find the operator within the equation that separates the two terms
				szOperators = findOperators(szDifferential , szTermsArray.length) ;

			}//end if

			//instantiate coefficients array with same length as the terms array
			iCoefficients = new int [iStartTerm + 2] ;

			//use a for loop to go through each term in the terms array and put the coefficient of each term into a separate array
			for (int i = iStartTerm ; i < szTermsArray.length ; i ++)
			{	
				//for the current term find the coefficient by using .substring from the start until the index of the * sign 
				//and add it into the corresponding index of the coefficients array
				iCoefficients[i] = Integer.parseInt(szTermsArray[i].substring(0 , szTermsArray[i].indexOf("*") ) ) ;

			}//end for

			//use an if statement to decide if the linear equation has 1 or two terms
			if (szTermsArray.length == iStartTerm + 1)
			{
				//assign the second coefficient to be 0
				iCoefficients[iStartTerm + 1] = 0 ;

			}//end if

			//use an if statement to check if the equation has two terms or not
			if (szTermsArray.length == iStartTerm + 2)
			{
				//use an if statement to check if the first character of the differential is a - sign
				if (szDifferential.charAt(0) == '-')
				{
					//multiply the gradient term by -1
					iCoefficients[iStartTerm] = iCoefficients[iStartTerm] * -1 ;

					//check if the constant term needs to be negative or positive based on the operator found in front of it
					if (szOperators[1] == "-")
					{
						//multiply the constant term by -1
						iCoefficients[iStartTerm + 1] = iCoefficients[iStartTerm + 1] * -1 ;

						//replace the c within the linear equation with the constant term but also remove the + sign
						//as the constant term is negative
						szLinearEquation = szLinearEquation.replaceAll("\\+c", String.valueOf(iCoefficients[iStartTerm + 1]) ) ;

					}
					else
					{
						//constant term is positive so just replace c with the constant term
						szLinearEquation = szLinearEquation.replaceAll("c", String.valueOf(iCoefficients[iStartTerm + 1]) ) ;

					}//end if

				}
				else
				{
					//check if the constant term needs to be negative or positive based on the operator found in front of it
					if (szOperators[0] == "-")
					{
						//multiply the constant term by -1
						iCoefficients[iStartTerm + 1] = iCoefficients[iStartTerm + 1] * -1 ;

						//replace the c within the linear equation with the constant term but also remove the + sign
						//as the constant term is negative
						szLinearEquation = szLinearEquation.replaceAll("\\+c", String.valueOf(iCoefficients[iStartTerm + 1]) ) ;

					}
					else
					{
						//constant term is positive so just replace c with the constant term
						szLinearEquation = szLinearEquation.replaceAll("c", String.valueOf(iCoefficients[iStartTerm + 1]) ) ;

					}//end if

				}//end if

				//replace the m within the linear equation with the x coefficient
				szLinearEquation = szLinearEquation.replaceAll( "m", String.valueOf(iCoefficients[iStartTerm]) ) ;

				//call procedure to decide solution type wanted by the user
				decideSolType(iSolType , szLinearEquation )  ;

				//set the equation equal to 0
				szLinearEquation = szLinearEquation.replaceAll("y", "0" ) ;

				//call procedure to decide solution type wanted by the user
				decideSolType(iSolType , szLinearEquation )  ;

				//subtract the constant term from both sides and remove the constant term from the RHS
				szLinearEquation = szLinearEquation.replaceAll("0=", String.valueOf( 0 - iCoefficients[iStartTerm + 1]) + "=" ).replaceAll
						("\\+" + iCoefficients[iStartTerm + 1], "").replaceAll("x" + iCoefficients[iStartTerm + 1], "x") ;

				//call procedure to decide solution type wanted by the user
				decideSolType(iSolType , szLinearEquation )  ;

				//swap the LHS with the RHS so that the equation is in the form x = value
				szLinearEquation = szLinearEquation.replaceAll("", "") ;

				//calculate the value of x
				fXCoordinates[0] = (float) (0 - iCoefficients[iStartTerm + 1]) / iCoefficients[iStartTerm] ; 

				//call procedure to decide solution type wanted by the user
				decideSolType(iSolType , "x=" + (float) Math.round(fXCoordinates[0] * 100) / 100 )  ;

			}
			else
			{
				//remove the constant term from the linear equation as the differential has only on term
				szLinearEquation = szLinearEquation.replaceAll("\\+c", "") ;

				//call procedure to decide solution type wanted by the user
				decideSolType(iSolType , szLinearEquation )  ;

				//set the equation equal to 0
				szLinearEquation = szLinearEquation.replaceAll("y", "0" ) ;

				//call procedure to decide solution type wanted by the user
				decideSolType(iSolType , szLinearEquation )  ;

				//set the x coordinate to be 0
				fXCoordinates[0] = 0 ;

				//call procedure to decide solution type wanted by the user
				decideSolType(iSolType , "x=0" )  ;

			}//end if

			//call function to calculate y coordinates
			fYCoordinates[0] = evaluateFunction(szEquation , fXCoordinates[0]) ;

			//plug in the calculated x coordinate into the equation and output it as the next step
			szSubstitutedValue = substitute(szEquation , fXCoordinates[0]) ; 

			//call procedure to decide solution type wanted by the user
			decideSolType(iSolType , "\nNow plug in the calculated x-value back into the original equation:" ) ;

			//call procedure to decide solution type wanted by the user
			decideSolType(iSolType , "y = " + szSubstitutedValue ) ;

			//call procedure to decide solution type wanted by the user
			decideSolType(iSolType , "  = " + (float) Math.round(fYCoordinates[0]*100) / 100 ) ;

			//call procedure to decide solution type wanted by the user
			decideSolType(iSolType , "\nTherefore, the coordinates of the turning point are:\n"
					+ "(" + (float) Math.round(fXCoordinates[0]*100) / 100 + " , " + (float) Math.round(fYCoordinates[0]*100) / 100 + ")" ) ; 

			//use an if statement to check if the user just wants the answer
			if (iSolType == 3)
			{
				//print out the answer
				System.out.println("\nCoordinates of Turning Point: " + "(" + (float) Math.round(fXCoordinates[0]*100) / 100 + " , " 
						+ (float) Math.round(fYCoordinates[0]*100) / 100 + ")" ) ; 

			}//end if

		}//end if

	}//end function

	//procedure to distinguish the nature of a stationary point
	public void decideStatPointNature (String szEquation , float fXCoord , int iSolType)
	{
		//declare variables
		String szFirstDerivative = "" ;
		String szSecondDerivative = "" ;
		String szSubstitutedX = "" ;
		float fCalculatedSecond = 0 ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "\nIn order to distinguish the nature of the stationary point, we need to find the second derivative\n"
				+ "of the original equation, and so we need to differentiate it twice.") ;

		//call procedure to differentiate original equation
		szFirstDerivative = differentiate (szEquation , iSolType) ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "\nThe First Derivative is: " + szFirstDerivative.replaceAll("\\*", "").replaceAll("\\^1", "").replaceAll("x\\^0", "") + "\n" ) ;

		//call procedure to differentiate the first derivative
		szSecondDerivative = differentiate (szFirstDerivative , iSolType) ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "\nThe Second Derivative is: " + szSecondDerivative.replaceAll("\\*", "").replaceAll("\\^1", "").replaceAll("x\\^0", "") + "\n" ) ;

		//substitute the entered x value into the second derivative
		szSubstitutedX = substitute(szSecondDerivative , fXCoord) ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "d쾧/dx� = " + szSubstitutedX) ;

		//calculate the value of the second derivative at the entered x value
		fCalculatedSecond = evaluateFunction (szSecondDerivative , fXCoord) ;

		//call procedure to decide solution type wanted by the user
		decideSolType(iSolType , "        = " + (int) fCalculatedSecond) ;

		//use an if statement to decide if the calculated value is greater than, equal to, or less than 0
		if (fCalculatedSecond == 0)
		{
			//print out the nature of the stationary point entered by the user
			System.out.println("\nSince the value of d쾧/dx� is 0, the point entered is a point of inflection on the function." ) ;
		}
		else if (fCalculatedSecond > 0)
		{
			//print out the nature of the stationary point entered by the user
			System.out.println("\nSince the value of d쾧/dx� is greater than 0, the stationary point entered is a local minimum." ) ;
		}
		else
		{
			//print out the nature of the stationary point entered by the user
			System.out.println("\nSince the value of d쾧/dx� is less than 0, the stationary point entered is a local maximum." ) ;


		}//end if

	}//end procedure

	//procedure which decides which example of differentiation with first principles the user would like to see
	public void diffWithPrinciples(String szFileName)
	{
		// Load the source TEX file to be converted
		Converter converter = new Converter(szFileName);

		// Get the convert options ready for the target PDF format
		ConvertOptions convertOptions = new FileType().fromExtension("pdf").getConvertOptions();

		// Convert to PDF format
		converter.convert(szFileName.replaceAll("tex", "pdf"), convertOptions);

		//tell the user that you have created the pdf for them
		System.out.println("\nA PDF file with the requested example has been created!");

	}//end procedure

	public static void main(String[] args) 
	{
		//declare differentiation object
		Differentiation DiffObject = new Differentiation() ;
		String szResult = "" ;
		float fGradient = 0 ;

		//testing differentiation function
		szResult = DiffObject.differentiate("12*x^0-4*x^1+2*x^2" , 1) ;
		System.out.println(szResult) ;

		//testing function that calculates gradient of a line at a given point
		fGradient = DiffObject.calcGradient("-4*x^-2-2*x^6", 2 , 1) ;
		System.out.println("      = " + fGradient);

		//testing procedure that calculates the equation of a tangent at a given point to a curve
		DiffObject.calcTangOrNorm("12*x^0-4*x^1+2*x^2" , -1 , 18 , false , 1) ;

		//testing procedure that calculates the stationary points of an entered function (max cubic)
		DiffObject.calcStatPoints("-4*x^2-2*x^1", 3) ;

		//testing procedure that decides the nature of a stationary point
		DiffObject.decideStatPointNature("-5*x^2", 3 , 1) ;

	}//end main

}//end class