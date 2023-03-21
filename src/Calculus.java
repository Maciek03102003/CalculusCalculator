/**
 * 
 * @author 15rytlewskim
 * @details object for generic calculus methods
 * 
 */

//import Scanner
import java.util.Scanner ;

public class Calculus 
{
	//constructor
	public Calculus()
	{
		
	}//end constructor
	
	//function to check for and replace any negative powers within the entered equation
	public String checkNegative(String szInput)
	{
		//declare variables
		char chCurrentCharacter = '\0' ;
		char chPreviousCharacter = '\0' ;
		String szCurrentCharacter = "" ;
		String szEquation = "" ;

		//use a for loop to go through all the characters within the equation to check if there
		//are any negative powers being used
		for (int i = 0  ; i < szInput.length()  ; i ++)
		{
			//use an if statement to decide if it is the first character or not
			if ( i != 0 )
			{
				//retrieve the current character 
				chCurrentCharacter = szInput.charAt(i) ;

				//retrieve the previous character
				chPreviousCharacter = szInput.charAt(i - 1) ;

				//use an if statement to check if the characters are a '^-'
				if (chPreviousCharacter == '^' && chCurrentCharacter == '-')
				{
					//change the current character to an _
					chCurrentCharacter = '_' ; //replies any minus that involves a negative power with a _ in order to differentiate
					//them from the operator - so that the program doesn't split the equation on it and it
					//can be easily identified later by knowing which character it was replaced by
				}

			}
			else
			{
				//retrieve the current character 
				chCurrentCharacter = szInput.charAt(i) ;
			}

			//change the current character to a string and rebuild the users input
			szCurrentCharacter = String.valueOf(chCurrentCharacter) ;
			szEquation = szEquation + szCurrentCharacter ;

		}

		return szEquation ;

	}//end function

	//function to split the the user's input on the passed character(s) into an array
	public String[] splitInput(String szInput , String szSplitter)
	{
		//declare array
		String[] szSplitInput ;

		//split the passed input on the character(s) passed to the function into an array
		szSplitInput = szInput.split(szSplitter) ;

		return szSplitInput ;

	}//end function

	//function to go through the users input and identify the operators used
	public String[] findOperators(String szInput , int iTermAmount)
	{
		//declare variables
		char chCurrentCharacter = '\0' ;
		String szCurrentCharacter = "" ;
		int iCharacterIndex = 0 ;
		String[] szOpArray ;
		int iOpAmount = 0 ;

		//instantiate the operator array based on the number of terms. Although there will be one less operators than
		//there are terms, still instantiate the array so that it has the same length as terms. This is so that we can assign
		//the last index a blank space so that at the end of the equation, there is no operator
		szOpArray = new String[iTermAmount] ;

		//use a do while loop to go through the users input and identify the operators used
		//between each term. The number of terms will decide how many operators there are as there
		//is always one less operator than there are terms
		do
		{
			//retrieve the current character of the users input
			chCurrentCharacter = szInput.charAt(iCharacterIndex) ;

			//convert the char into a String
			szCurrentCharacter = String.valueOf(chCurrentCharacter) ;

			//use an if statement to decide if the current character is a 
			//+, a - or a negative power
			if (szCurrentCharacter.equals("+"))
			{
				//assign a + to the current operator index
				szOpArray[iOpAmount] = "+" ;

				//increment the amount of operators found
				iOpAmount ++ ;
			}
			else if (szCurrentCharacter.equals("-"))
			{
				//assign a - to the current operator index
				szOpArray[iOpAmount] = "-" ;

				//increment the amount of operators found
				iOpAmount ++ ;
			}

			//increment the character index to move onto the next character in the string
			iCharacterIndex ++ ;

		} while(iOpAmount < (iTermAmount - 1)) ; //end do while

		//assign the final index of the operator array a blank space so that when the equation ends
		//no more operators follow at the end
		szOpArray[iOpAmount] = "" ;

		return szOpArray ;

	}//end function

	//function to go through an equation and substitute all x values with the x coordinate
	public String substitute(String szEquation , float fXCoord)
	{
		//declare variables
		char chCurrentCharacter = '\0' ;
		String szCurrentCharacter = "" ;
		String szSubstitutedFunc = "" ;

		//use a for loop to go through the equation and locate all the x's and place brackets
		//around them
		for (int i = 0 ; i < szEquation.length() ; i ++) 
		{
			//retrieve the current character and convert it into a string
			chCurrentCharacter = szEquation.charAt(i) ;
			szCurrentCharacter = String.valueOf(chCurrentCharacter) ;

			//use an if statement to check if the current character is an x
			if (szCurrentCharacter.equals("x"))
			{
				//use an if statement to check if the entered x value is a decimal or not
				if (fXCoord % 1 == 0)
				{
					//add a bracket, then the x, then another bracket to the string
					szSubstitutedFunc = szSubstitutedFunc + "(" + fXCoord + ")" ;
				}
				else
				{
					//add a bracket, then the x, rounded to 2 dp, then another bracket to the string
					szSubstitutedFunc = szSubstitutedFunc + "(" + (float) (Math.round(fXCoord * 100)) / 100 + ")" ;
				}

			}
			else
			{
				//add the current character back into the string
				szSubstitutedFunc = szSubstitutedFunc + szCurrentCharacter ; 
			}


		}//end for

		return szSubstitutedFunc ;
	}

	//function to check if the coordinates entered by the user are valid and actually exist on the line
	public boolean checkCoord (String szFunction , float fX , float fY)
	{
		//declare variables
		boolean bValid = false ;
		int iTerm = 0 ;
		int iPower = 0 ;
		int iCoefficient = 0 ;
		float fTotalResult = 0 ; 
		float fCurrentTermResult = 0 ;
		String[] szTermsArray ;
		String[] szOperatorsArray ;
		String[] szCoefficientsArray ;
		String[] szPowersArray ;

		//call function to check for negative powers
		szFunction = checkNegative(szFunction) ;

		//call function to split the users input on + and - into separate terms
		szTermsArray = splitInput(szFunction, "[+-]") ;

		//call function to find the operators within the equation and input them
		//into an array one by one
		szOperatorsArray = findOperators (szFunction , szTermsArray.length) ;

		//use a for loop to go through and calculate the result of each term
		//within the entered equation separately
		for (iTerm = 0 ; iTerm < szTermsArray.length ; iTerm ++)
		{
			//call function to split array on * and ^
			szCoefficientsArray = splitInput(szTermsArray[iTerm] , "\\*") ;
			szPowersArray = splitInput(szCoefficientsArray[1] , "\\^") ;

			//use an if statement to check if a negative power is being used by searching for an '_'
			if (szPowersArray[1].contains("_"))
			{
				//convert the power into an integer and multiply by -1 to get a negative
				iPower = Integer.parseInt(szPowersArray[1].substring(1, 2)) ;
				iPower = iPower / -1 ;
			}
			else
			{
				//convert the power into an integer
				iPower = Integer.parseInt(szPowersArray[1]) ;
			}

			//convert the coefficient of the term into an integer
			iCoefficient = Integer.parseInt(szCoefficientsArray[0]) ;

			//calculate the result of the current term by exponentiating the x coordinate to the current power
			//and then multiplying by the current coefficient
			fCurrentTermResult = (float) ((Math.pow(fX, iPower)) * iCoefficient) ;

			//use an if statement to decide if its the first term or not. If it is the first term you will add it to the gradient
			//not matter what, if not you need to check which operator to use
			if (iTerm != 0)
			{
				//use an if statement to decide which operator to use a + or a - between the terms
				if (szOperatorsArray[iTerm - 1].equals("+"))
				{
					//add the value of the current term to the total value of the gradient
					fTotalResult = fTotalResult + fCurrentTermResult ;

				}
				else
				{
					//subtract the value of the current term from the total value of the gradient
					fTotalResult = fTotalResult - fCurrentTermResult ;

				}//end if

			}
			else
			{
				//add the value of the current term to the total value of the gradient
				fTotalResult = fTotalResult + fCurrentTermResult ;

			}//end if
		
		}//end for
		
		//use an if statement to decide if the coordinate exists on the line
		if (fTotalResult == (float) fY)
		{
			//set bValid to true
			bValid = true ;
		}
		else
		{
			//set bValid to false
			bValid = false ;

		}//end if

		return bValid ;

	}

	//function to evaluate a function with a given X value
	public float evaluateFunction (String szFunction , float fXValue)
	{
		//declare variables
		String[] szTermsArray ;
		String[] szOperatorsArray ;
		String[] szCoefficientsArray ;
		String[] szPowersArray ;
		int iTerm = 0 ;
		int iPower = 0 ;
		int iStartTerm = 0 ;
		float fCoefficient = 0 ;
		float fCurrentTermResult = 0 ;
		float fTotalResult = 0 ;

		//call function to check for negative powers
		szFunction = checkNegative(szFunction) ;

		//call function to split the users input on + and - into separate terms
		szTermsArray = splitInput(szFunction, "[+-]") ;

		//call function to find the operators within the equation and input them
		//into an array one by one
		szOperatorsArray = findOperators (szFunction , szTermsArray.length) ;

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

		//use a for loop to go through and calculate the result of each term
		//within the entered equation separately
		for (iTerm = iStartTerm ; iTerm < szTermsArray.length ; iTerm ++)
		{
			//call function to split array on * and ^
			szCoefficientsArray = splitInput(szTermsArray[iTerm] , "\\*") ;
			szPowersArray = splitInput(szCoefficientsArray[1] , "\\^") ;

			//use an if statement to check if a negative power is being used by searching for an '_'
			if (szPowersArray[1].contains("_"))
			{
				//convert the power into an integer and multiply by -1 to get a negative
				iPower = Integer.parseInt(szPowersArray[1].substring(1, 2)) ;
				iPower = iPower / -1 ;
			}
			else
			{
				//convert the power into an integer
				iPower = Integer.parseInt(szPowersArray[1]) ;
			}

			//convert the coefficient of the term into a number
			fCoefficient = Float.parseFloat(szCoefficientsArray[0]) ;

			//use an if statement to check if the current term has an x and if the x-coordinate
			//entered is 0. If so, the current term result will be 0
			if (szTermsArray[iTerm].contains("x") && fXValue == 0 && !szTermsArray[iTerm].contains("x^0")) //the last part is so that
				//it doesn't enter the if if the term has x^0
				//as that means it has no x component
			{
				//set the current term result to 0
				fCurrentTermResult = 0 ;
			}
			else
			{
				//calculate the result of the current term by exponentiating the x coordinate to the current power
				//and then multiplying by the current coefficient
				fCurrentTermResult = (float) ((Math.pow(fXValue, iPower)) * fCoefficient) ;

			}//end if

			//use an if statement to decide if its the first term or not. If it is the first term you will add it to the gradient
			//not matter what, if not you need to check which operator to use
			if (iTerm != 0)
			{
				//use an if statement to decide which operator to use a + or a - between the terms
				if (szOperatorsArray[iTerm - 1].equals("+"))
				{
					//add the value of the current term to the total value of the gradient
					fTotalResult = fTotalResult + fCurrentTermResult ;

				}
				else
				{
					//subtract the value of the current term from the total value of the gradient
					fTotalResult = fTotalResult - fCurrentTermResult ;

				}//end if

			}
			else
			{
				//add the value of the current term to the total value of the gradient
				fTotalResult = fTotalResult + fCurrentTermResult ;

			}//end if

		}//end for

		return fTotalResult;
	}

	//procedure to decide what kind of solution the user wants
	public void decideSolType (int iSolType , String szMessage)
	{
		//declare Scanner
		Scanner szKeyboard = new Scanner (System.in) ;

		//use an if statement to check if the user wants any solution at all or just the answer
		if (iSolType != 3)
		{
			//show the user the next step
			System.out.printf(szMessage);

		}//end if

		//use an if statement to decide if the user wants a step by step solution
		if (iSolType == 1)
		{
			//get the user to hit enter before seeing the next step
			System.out.print("");
			szKeyboard.nextLine() ;
		}
		else if (iSolType == 2)
		{
			//make a gap between this output and the next
			System.out.println("");

		}//end if

	}//end procedure

}//end class