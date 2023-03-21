/**
 * 
 * @author 15rytlewskim
 * @details object that deals with all user interaction and gives them access to various menus within the program
 * to allow them to choose between various functions of the calculator
 */

//import classes
import java.util.Scanner  ;

public class MainConsole
{
	//constructor
	public MainConsole()
	{

	}//end constructor

	//function to validate coordinate input
	public String inputCoord (String szCoordType)
	{
		//declare variables
		Scanner szKeyboard = new Scanner (System.in) ; 
		String szCoord;

		//prompt the user to enter the coordinate
		System.out.print("\nPlease enter the " + szCoordType + "-Coordinate: ");
		szCoord = szKeyboard.nextLine() ;

		//use an if statement to see if the input coord is an X or not
		if (!szCoord.equalsIgnoreCase("X"))
		{
			//check if the user has entered a valid coordinate
			//regex source: https://stackoverflow.com/questions/16119154/whats-a-good-regular-expression-for-real-numbers-in-java
			while (!szCoord.matches("((\\+|-)?([0-9]+)(\\.[0-9]+)?)|((\\+|-)?\\.?[0-9]+)\r\n"))
			{
				//print out the error message
				System.out.println("Sorry! That isn't a valid coordinate!") ;

				//prompt the user to enter the coordinate
				System.out.print("\nPlease enter the " + szCoordType + "-Coordinate: ") ;
				szCoord = szKeyboard.nextLine() ;

			}//end while

		}//end if

		return szCoord ;

	}

	//function to input which type of solution the user would like to see
	public int inputSolType ()
	{
		//declare Scanner object
		Scanner szKeyboard = new Scanner (System.in) ;

		//declare variables
		String szSolutionType;
		int iSolutionType;

		//ask the user if they would like to see a full solution, step-by-step solution or just the answer
		System.out.println("\nWould you like:\n1. A step-by-step Solution\n2. A full solution\n3. The Answer");
		szSolutionType = szKeyboard.nextLine() ;

		//use a while loop to check if the user has entered a valid option
		while (!szSolutionType.matches("[1-3]"))
		{
			//print out the error message
			System.out.println("That is not a valid option!");

			//ask the user if they would like to see a full solution, step-by-step solution or just the answer
			System.out.println("Would you like:\n1. A step-by-step Solution\n2. A full solution\n3. The Answer\n\n");
			szSolutionType = szKeyboard.nextLine() ;

		}//end while

		//parse the string into an int
		iSolutionType = Integer.parseInt(szSolutionType) ;

		return iSolutionType ;

	}

	//procedure to input all information needed for calculating the tangent or normal of a line
	public void inputCalcLine(String szTangOrNorm , int iSolutionType)
	{
		//declare Scanner
		Scanner szKeyboard = new Scanner (System.in) ;

		//declare objects
		Differentiation DiffObject = new Differentiation() ;
		Calculus CalcObject = new Calculus() ;

		//declare variables
		String szEquation;
		String szXCoord;
		String szYCoord;
		int iXCoord = 0 ;
		int iYCoord = 0 ;
		boolean bValidCoord = false ;
		boolean bExit = false ;

		//prompt the user to enter the equation to differentiate
		System.out.println("\nPlease enter the equation to which the " + szTangOrNorm + " is to be found in the following format\n"
				+ "a*x^n+b*x^n-1+...+y*x^1+z*x^0 (Integers Only)\n") ;
		szEquation = szKeyboard.nextLine() ;

		//use a do while loop to make the user keep inputting the coordinates until they are valid or until they want
		//to exit
		do
		{
			//call the function to prompt the user to enter the X and Y coordinates at which they would like to find the tangent at
			System.out.print("\nNow, please enter the coordinates of the point at which the " + szTangOrNorm + " is at!\n") ;
			szXCoord = inputCoord("X") ;
			szYCoord = inputCoord("Y") ;

			//use an if statement to check if the user has entered an X in either field and so if they want to exit
			if (szXCoord.equalsIgnoreCase("X") || szYCoord.equalsIgnoreCase("X"))
			{
				//set bExit to true
				bExit = true ;

			}
			else
			{
				//convert the users input into a string
				iXCoord = Integer.parseInt(szXCoord) ;
				iYCoord = Integer.parseInt(szYCoord) ;

				//call function to check if the entered coordinate is on the line or not
				bValidCoord = CalcObject.checkCoord(szEquation , iXCoord , iYCoord) ;

				//use an if statement to check if the coordinates are valid or not
				if (!bValidCoord)
				{
					//print out the error message to the user
					System.out.println("\nThis point does not exist on the function entered! Please enter a valid set of coordinates!\n"
							+ "Alternatively, enter 'X' in either or both fields to return to the Differentiation Menu.");

				}//end if 

			}//end if

		} while(!bValidCoord && !bExit) ;

		//use an if statement to check if the coordinates are valid or if the user wants to exit back
		//to the differentiation menu
		if (bValidCoord)
		{
			//leave a line between the previous and next output
			System.out.println();

			//calculate the tangent/normal
			DiffObject.calcTangOrNorm(szEquation, iXCoord, iYCoord , !szTangOrNorm.equals("tangent"), iSolutionType) ;

		}
		else if (bExit)
		{
			//call procedure to output the differentiation menu
			useDiffMenu() ;

		}//end if

	}//end procedure

	//procedure to input all information needed for calculating area under graph using definite integrals
	//or the trapezium rule and calling the function that does so
	public double inputCalcAreaInfo (String szAreaType , int iSolType)
	{
		//declare Integration object
		Integration IntegObject = new Integration() ;

		//declare variables
		Scanner szKeyboard = new Scanner (System.in) ;
		String szEquation;
		String szLowerBound;
		String szUpperBound;
		String szBarAmount;
		int iBarAmount;
		int iLowerBound = 0 ;
		int iUpperBound = 0 ;
		boolean bValidLimits;
		double dAreaOrPercent = 0 ;

		//prompt the user to enter the equation to integrate
		System.out.println("\nPlease enter the equation to be integrated in the following format\n"
				+ "a*x^n+b*x^n-1+...+y*x^1+z*x^0 (Integers Only)\n") ;
		szEquation = szKeyboard.nextLine() ;

		System.out.println() ;

		//use a do while to make the user keep entering the limits until they are valid
		do
		{
			//prompt the user to enter the lower limit for the integral
			System.out.print("Please enter the lower limit for the integral: ");
			szLowerBound = szKeyboard.nextLine() ;

			//prompt the user to enter the upper limit for the integral
			System.out.print("Please enter the upper limit for the integral: ");
			szUpperBound = szKeyboard.nextLine() ;

			//use an if statement to check if the upper and lower limits are both integers
			if (!szLowerBound.matches("-?[0-9]+") || !szUpperBound.matches("-?[0-9]+") )
			{
				//print out the error message
				System.out.println("Those are not valid limits!\n");

				//set bValidLimits to false
				bValidLimits = false ;
			}
			else
			{
				//set bValidLimits to true
				bValidLimits = true ;

			}//end if

			//use an if statement to check if both limits entered are integers
			if (bValidLimits)
			{
				//convert the users input into integers
				iLowerBound = Integer.parseInt(szLowerBound) ;
				iUpperBound = Integer.parseInt(szUpperBound) ;

				//use an if statement to check if the upper limit is greater than the lower limit
				if (iLowerBound > iUpperBound)
				{
					//print out the error message
					System.out.println("The upper limit must be greater than the lower limit!");

					//set bValidLimits to false
					bValidLimits = false ;
				}

			}//end if 

		} while (!bValidLimits) ; //end while

		//use an if statement to check if a definite area, an estimated area or the % error is being calculated
		if (szAreaType.equals("trap"))
		{
			//prompt the user to enter how many bars they would like to split the area into
			System.out.print("\nPlease enter how many bars you would like to split the area into: ");
			szBarAmount = szKeyboard.nextLine() ; 

			//use a while loop to check if the user has entered a valid bar amount
			while (!szBarAmount.matches("[0-9]+"))
			{
				//print out the error message
				System.out.println("That is not a valid bar amount!\n");

				//prompt the user to enter how many bars they would like to split the area into
				System.out.print("Please enter how many bars you would like to split the area into: ");
				szBarAmount = szKeyboard.nextLine() ; 

			}//end while

			//convert the users input into an int
			iBarAmount = Integer.parseInt(szBarAmount) ;

			//call function to calculate the area using trapezium rule
			dAreaOrPercent = IntegObject.calcAreaTrap (szEquation , iLowerBound , iUpperBound , iBarAmount , iSolType) ;

		}
		else if (szAreaType.equals("percent"))
		{
			//prompt the user to enter how many bars they would like to split the area into
			System.out.print("\nPlease enter how many bars you would like to split the area into: ");
			szBarAmount = szKeyboard.nextLine() ; 

			//use a while loop to check if the user has entered a valid bar amount
			while (!szBarAmount.matches("[0-9]+"))
			{
				//print out the error message
				System.out.println("That is not a valid bar amount!\n");

				//prompt the user to enter how many bars they would like to split the area into
				System.out.print("Please enter how many bars you would like to split the area into: ");
				szBarAmount = szKeyboard.nextLine() ; 

			}//end while

			//convert the users input into an int
			iBarAmount = Integer.parseInt(szBarAmount) ;

			//call procedure to calculate the percentage error in the trapezium rule
			IntegObject.calcPercentError(szEquation , iLowerBound , iUpperBound , iBarAmount , iSolType) ;
		}
		else
		{
			//call function to calculate the area using definite integrals
			dAreaOrPercent = IntegObject.calcDefiniteArea (szEquation , iLowerBound , iUpperBound , iSolType) ;
		}

		return dAreaOrPercent ;

	}//end function


	//procedure to output the main menu
	public void useMainMenu()
	{
		//declare variables
		String szMenuChoice;
		Scanner szKeyboard = new Scanner (System.in) ;

		//output the option menu
		System.out.println("\n======= Differentiation And Integration Calculator =======\n"
				+ "1. Differentiation\n"
				+ "2. Integration\n"
				+ "3. Exit") ;

		//prompt the user to enter a menu choice
		System.out.print("\nPlease choose an option from the Menu: ") ;
		szMenuChoice = szKeyboard.nextLine() ;

		//use a while loop to check if the user entered a valid option
		while (!szMenuChoice.matches("[1-3]"))
		{
			//print out the error message
			System.out.println("Sorry! That isn't a valid input!") ;

			//prompt the user to enter a menu choice
			System.out.print("\nPlease choose an option from the Menu: ") ;
			szMenuChoice = szKeyboard.nextLine() ;

		}// end while

		//use a switch case to see which menu option the user chose
		switch (szMenuChoice) {
			case "1" ->
				//call procedure to output differentiation menu
					useDiffMenu();
			case "2" ->
				//call procedure to output integration menu
					useIntegMenu();
			case "3" ->

				//print out the end message
					System.out.println("\nThank you for using our calculator!\nGoodbye!");
		}//end switch case

	}//end procedure

	//procedure to output differentiation menu
	public void useDiffMenu()
	{
		//declare objects
		Differentiation DiffObject = new Differentiation() ;
		Calculus CalcObject = new Calculus() ;

		//declare variables
		String szDiffChoice;
		Scanner szKeyboard = new Scanner (System.in) ;
		String szEquation;
		String szDifferential;
		String szPrinciplesChoice;
		String szFileName = "" ;
		float fXCoord;
		String szXCoord;
		int iSolType;
		boolean bValidCoord;

		//output the option menu
		System.out.println("\n======= Differentiation =======\n"
				+ "1. Basic Differentiation\n"
				+ "2. Gradient at a Given Point\n"
				+ "3. Tangent at a Given Point\n"
				+ "4. Normal at a Given Point\n"
				+ "5. Stationary Points of a Curve\n"
				+ "6. Local Maxima or Minima\n"
				+ "7. First Principles\n"
				+ "8. Back to Main Menu") ;

		//prompt the user to enter a menu choice
		System.out.print("\nPlease choose an option from the Menu: ") ;
		szDiffChoice = szKeyboard.nextLine() ;

		//use a while loop to check if the user entered a valid option
		while (!szDiffChoice.matches("[1-8]}"))
		{
			//print out the error message
			System.out.println("Sorry! That isn't a valid input!") ;

			//prompt the user to enter a menu choice
			System.out.print("\nPlease choose an option from the Menu: ") ;
			szDiffChoice = szKeyboard.nextLine() ;

		}// end while

		//use a switch case to check what the user input
		switch (szDiffChoice) {
			case "1" -> {
				//call procedure to input desired solution type
				iSolType = inputSolType();

				//prompt the user to enter the equation to differentiate
				System.out.println("\nPlease enter the equation to be differentiated in the following format\n"
						+ "a*x^n+b*x^n-1+...+y*x^1+z*x^0 (Integers Only)\n");
				szEquation = szKeyboard.nextLine();

				//call the function to differentiate the equation
				szEquation = DiffObject.differentiate(szEquation, iSolType);

				//output the differentiated equation but replace all *'s, ^1's and x^0's with nothing to create a cleaner output
				System.out.println("The differential is: " + szEquation.replaceAll("\\*", "").replaceAll("\\^1", "").replaceAll("x\\^0", ""));
			}
			case "2" -> {
				//declare variables
				float fGradient;

				//call procedure to input desired solution type
				iSolType = inputSolType();

				//prompt the user to enter the equation to differentiate
				System.out.println("\nPlease enter the equation to be differentiated in the following format\n"
						+ "a*x^n+b*x^n-1+...+y*x^1+z*x^0 (Integers Only)\n");
				szEquation = szKeyboard.nextLine();

				//call function to enter and validate coordinate and convert what it returns to a float
				szXCoord = inputCoord("X");
				fXCoord = Float.parseFloat(szXCoord);

				//leave a line between the previous and next output
				System.out.println();

				//call procedure to calculate the gradient
				fGradient = DiffObject.calcGradient(szEquation, fXCoord, iSolType);

				//output the gradient to the user
				System.out.println("Gradient = " + (float) (Math.round(fGradient * 100.0) / 100.0)); //round gradient to 2dp
			}
			case "3" -> {
				//call procedure to input desired solution type
				iSolType = inputSolType();

				//call procedure to input all the information to calculate the tangent and calculate it
				inputCalcLine("tangent", iSolType);
			}
			case "4" -> {
				//call procedure to input desired solution type
				iSolType = inputSolType();

				//call procedure to input all the information to calculate the normal and calculate it
				inputCalcLine("normal", iSolType);
			}
			case "5" -> {
				//call procedure to input desired solution type
				iSolType = inputSolType();

				//prompt the user to enter the equation to differentiate
				System.out.println("\nPlease enter the equation to be differentiated in the following format\n"
						+ "a*x^n+b*x^n-1+...+y*x^1+z*x^0 (Integers Only , Max Cubic, Highest Power First)\n");
				szEquation = szKeyboard.nextLine();

				//call procedure to calculate stationary points
				DiffObject.calcStatPoints(szEquation, iSolType);
			}
			case "6" -> {
				//call procedure to input desired solution type
				iSolType = inputSolType();

				//prompt the user to enter the equation to differentiate
				System.out.println("\nPlease enter the equation to be differentiated in the following format\n"
						+ "a*x^n+b*x^n-1+...+y*x^1+z*x^0 (Integers Only)\n");
				szEquation = szKeyboard.nextLine();

				//use a do while loop to make the user keep inputting the coordinates until they have input a stationary point on the line
				//to exit
				do {
					//call function to enter and validate coordinate and convert what it returns to a float
					szXCoord = inputCoord("X");
					fXCoord = Float.parseFloat(szXCoord);

					//call function to differentiate equation entered by user and call function to check if the coordinate
					//entered by the user is actually a stationary point on the entered function
					szDifferential = DiffObject.differentiate(szEquation, 3);
					bValidCoord = CalcObject.checkCoord(szDifferential, fXCoord, 0);

					//use an if statement to check if the coordinates are valid or not
					if (!bValidCoord) {
						//print out the error message to the user
						System.out.println("\nThis point is not a stationary point on the entered function! Please enter a"
								+ " stationary point on the function!");

					}//end if

				} while (!bValidCoord);

				//call procedure to decide the nature of the stationary point at the entered x coordinate
				DiffObject.decideStatPointNature(szEquation, fXCoord, iSolType);
			}
			case "7" -> {
				//output the option menu
				System.out.println("\n======= First Principles =======\n"
						+ "1. x²\n"
						+ "2. 6x²\n"
						+ "3. x³\n"
						+ "4. Sinx\n"
						+ "5. Cosx");

				//prompt the user to enter a menu choice
				System.out.print("\nPlease choose an example to see from the Menu: ");
				szPrinciplesChoice = szKeyboard.nextLine();

				//use a while loop to check if the user entered a valid option
				while (!szPrinciplesChoice.matches("[1-5]")) {
					//print out the error message
					System.out.println("Sorry! That isn't a valid input!");

					//prompt the user to enter a menu choice
					System.out.print("\nPlease choose an example to see from the Menu: ");
					szPrinciplesChoice = szKeyboard.nextLine();

				}// end while


				//use an if statement to decide which example the user chose and so which file
				//to use
				switch (szPrinciplesChoice) {
					case "1" ->
						//assign the correct file name
							szFileName = "x^2.tex";
					case "2" ->
						//assign the correct file name
							szFileName = "6x^2.tex";
					case "3" ->
						//assign the correct file name
							szFileName = "x^3.tex";
					case "4" ->
						//assign the correct file name
							szFileName = "Sinx.tex";
					case "5" ->
						//assign the correct file name
							szFileName = "Cosx.tex";
				}


				//call procedure to make the pdf file with the example for the user
				DiffObject.diffWithPrinciples(szFileName);
			}
			case "8" ->

				//call procedure to output main menu
					useMainMenu();
		}//end switch case

		//call procedure to output main menu
		useMainMenu() ;

	}//end procedure

	//procedure to use the integration menu
	public void useIntegMenu()
	{
		//declare Integration object
		Integration IntegObject = new Integration() ;

		//declare variables
		Scanner szKeyboard = new Scanner (System.in) ;
		String szIntegChoice;
		String szEquation;
		String szIntegral;
		int iSolType;
		double dArea;

		//output the option menu
		System.out.println("\n======= Integration =======\n"
				+ "1. Basic Integration\n"
				+ "2. Area under a Curve\n"
				+ "3. Trapezium Rule\n"
				+ "4. % Error in Trapezium Rule\n"
				+ "5. Back to Main Menu") ;

		//prompt the user to enter a menu choice
		System.out.print("\nPlease choose an option from the Menu: ") ;
		szIntegChoice = szKeyboard.nextLine() ;

		//use a while loop to check if the user entered a valid option
		while (!szIntegChoice.matches("[1-5]"))
		{
			//print out the error message
			System.out.println("Sorry! That isn't a valid input!") ;

			//prompt the user to enter a menu choice
			System.out.print("\nPlease choose an option from the Menu: ") ;
			szIntegChoice = szKeyboard.nextLine() ;

		}// end while

		//use a switch case to check what the user has input
		switch (szIntegChoice) {
			case "1" -> {
				//call procedure to input desired solution type
				iSolType = inputSolType();

				//prompt the user to enter the equation to integrate
				System.out.println("\nPlease enter the equation to be integrated in the following format\n"
						+ "a*x^n+b*x^n-1+...+y*x^1+z*x^0 (Integers Only)\n");
				szEquation = szKeyboard.nextLine();

				//call the function to integrate the equation
				szIntegral = IntegObject.integrate(szEquation, iSolType);

				//output the integrated equation but replace all *'s, ^1's and x^0's with nothing to create a cleaner output
				System.out.println("The integral is: " + szIntegral.replaceAll("\\*", "").replaceAll("\\^1", "").replaceAll("x\\^0", "") + "+c   < -- Add constant of integration");
			}
			case "2" -> {
				//call procedure to input desired solution type
				iSolType = inputSolType();

				//call procedure to input all info to calculate the definite area
				dArea = inputCalcAreaInfo("definite", iSolType);

				//use an if statement to check if the user just wants the answer
				if (iSolType == 3) {
					//print out the answer for the user
					System.out.println("\nArea = " + (double) Math.round(dArea * 100) / 100);

				}//end if
			}
			case "3" -> {
				//call procedure to input desired solution type
				iSolType = inputSolType();

				//call procedure to input all info to calculate the estimated area
				dArea = inputCalcAreaInfo("trap", iSolType);

				//use an if statement to check if the user just wants the answer
				if (iSolType == 3) {
					//print out the answer for the user
					System.out.println("\nArea = " + (double) Math.round(dArea * 100) / 100);

				}//end if
			}
			case "4" -> {
				//call procedure to input desired solution type
				iSolType = inputSolType();

				//call procedure to input all info to calculate both areas and then calculate the percent error
				//in the estimate
				inputCalcAreaInfo("percent", iSolType);
			}
			case "5" ->

				//call procedure to output main menu
					useMainMenu();
		}//end switch case

		//call procedure to output main menu
		useMainMenu() ;

	}//end procedure

	public static void main(String[] args) 
	{
		//create MainConsole object
		MainConsole Console = new MainConsole() ;

		//call procedure to use main menu
		Console.useMainMenu() ;

	}//end main

}//end class