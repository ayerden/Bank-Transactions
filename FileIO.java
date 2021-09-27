
/**********************************************************
 * Program Name   :  FileIO
 * Author         :  Adam Yerden
 * Date           :	 March 7, 2019
 * Course/Section :  CSC-264-501
 * Program Description: This program will keep track of the
 * 		bank transactions using multiple files
 *
 * Methods:
 * --------------------------------------------------------
 * main - keeps track of bank transactions via files
 **********************************************************/

import java.text.*;
import java.io.*;

public class FileIO {

	/**********************************************************
	 * Method Name:    main
	 * Author:          Adam Yerden
	 * Date:            March 7, 2019
	 * Course/Section:  CSC - 264 - 501
	 * Program Description: This program will keep track of the balance and
	 *    transaction history for a simple savings account.  The program will
	 *    first input an initial balance.  It will then allow the user to input
	 *    up to 20 transactions.  Each transaction will be either a deposit
	 *    or a withdrawal.  The balance will be updated accordingly with each
	 *    transaction being stored in an array of transactions.  The forat of
	 *    each transaction will be a string that starts with either "w" or "d"
	 *    followed by the amount of the transaction.  Each transaction amount
	 *    must include both dollars and cents. If an invalid transaction is
	 *    entered return error message.  Use a sentinel value to stop entering
	 *    in transactions. When the user enteres the sentinel value the program
	 *    will print transactions history to file.
	 *
	 * BEGIN main
	 *    TRY (catch all i/o exceptions)
	 *    Ask user what file they would like to open
	 *	  open users transaction file
	 *	  open transaction summary file
	 *	  WHILE(user does not want to quit)
	 *	    Get Initial Balance from File
	 *      Write title blocks for coloms
	 *	    write initial balance to transaction file
	 *	    get first transaction from file
	 *    	WHLE you have not reached the end of the file
	 *	    	Change amount and balance string a to a float
	 *			Calculate balance
	 *          Get transaction type from the transaction that was entered
	 *    		IF (transaction type is invalid)
	 *				write error message to tranaction file
	 *    		ELSE
	 *    	    	IF (user tries to withdraw too much money)
	 *					write transaction to summary file
	 *                  suptract 20 from balance
	 *					write $20 withdrawl to the summary file
	 *    	    	ELSE
	 *			  		Write transaction to the summary file
	 *          	END IF
	 *    	  	END IF
	 *	    	close transaction file
	 *       END WHILE
	 *	 END WHILE
	 *   flush the transaction review file
	 *   close transaction review file
	 *   END TRY
	 *   CATCH (io excpetions)
	 *  	display exception message
	 *      display print stack trace
	 *   END CATCH
	 * END main
	 **********************************************************/
    public static void main(String [] args) {

		//local constants
		final String QUIT     = "q";	//constant for when user wants to quit
		final String WITHDRAW = "w";	//constant for when user wants to withdraw
		final String DEPOSITE = "d";	//constant for when user wants to deposite
		final int FEE         = 20;		//constant for overcharge fee

		//local variables
		Double dbamt;			//transaction amount as a double
		Double dbbal;			//bank account balance as a double
		String stamt = "";		//transaction amaount as a string
		String stbal = "";		//string balance
		String moneyBalance;	//bank account balance as a string
		String action = "";		//transaction from file
		String choice = "";		//users menu choice
		String act;				//transaction type
		boolean print = true;	//used to determine if transaction needed to be printed
		boolean over = false;	//used to determine if an overcharge need to be printed
		PrintWriter pw;
		BufferedReader in;
		DecimalFormat mon     = new DecimalFormat( "0.00" );
		String sourceFileName;

        // The name of the file to open.
        String fileName = "transaction_summary.txt";

        //Prints off menu
		System.out.println(Util.setLeft(58,"MENU"));
		System.out.println(Util.setLeft(50,"--------------------"));
		System.out.println(Util.setLeft(54,"f = input file"));
		System.out.println(Util.setLeft(54,"q = quit\n"));
		System.out.print(Util.setLeft(54,"Choice  :  "));
		choice = Keyboard.readString();
		System.out.println();

		while(!("q".equals(choice)))
		{

			try {
					//asks user for file name
					System.out.print(Util.setLeft(50,"File Name  :  "));
					sourceFileName = Keyboard.readString();
					System.out.println();

					// Assume default encoding.
					FileWriter fileWriter = new FileWriter(fileName);

					// Always wrap FileWriter in BufferedWriter.
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

					// print out title
					bufferedWriter.write("TYPE" + Util.setRight(19,"AMOUNT") + Util.setRight(19,"BALANCE"));
					bufferedWriter.newLine();
					bufferedWriter.write("--------------------------------------------");
					bufferedWriter.newLine();

					in = new BufferedReader(new FileReader(sourceFileName));

					//get initial balance
					stbal = in.readLine();

					//format balance
					dbbal = Double.parseDouble(stbal);
					stbal = mon.format(dbbal) + "";



					//chanes to a double
					dbbal = Double.parseDouble(stbal);

					//prints balance to file
					bufferedWriter.write("BALANCE" + Util.setRight(29 ,"$") + stbal);
					bufferedWriter.newLine();

					//wile the input file is not epty get the next line
					while((action = in.readLine()) != null)
					{
						//Get transaction type from the transaction that was entered
						act = action.substring(0,1);
						stamt = action.substring(1);
						act = act + "";

						//Convert transaction amount to double
						dbamt = Double.parseDouble(stamt);
						dbbal = Double.parseDouble(stbal);


						//if the transaction is a withdrawl
						if((WITHDRAW.equals(act)))
						{
							//the user tries to withdraw too much money display error message
							if(dbamt > dbbal)
							{
								//set over to true
								over = true;

								//set print to true
								print = true;
							}

							//subtracts amount from balance
							dbbal = dbbal - dbamt;

							//print out transaction to file
						}
						//if the transaction type is Deposit add the transaction amount to the balance
						else if(DEPOSITE.equals(act))
						{
							//set over to false
							over = false;

							//add amount to balance
							dbbal = dbbal + dbamt;

						}

						//if transaction type is invalid display error message
						else
						{
							stamt = mon.format(dbamt) + "";
							stamt = "$" + stamt;

							//display transaction error
							bufferedWriter.write(act + Util.setRight(20,stamt) + Util.setRight(30,"Invalid transaction type"));
							bufferedWriter.newLine();


							//set print to false
							print = false;
						}//end if

						//change to strings to print and add a "$"
						stbal = mon.format(dbbal) + "";
						stamt = mon.format(dbamt) + "";
						moneyBalance = "$" + stbal;
						stamt = "$" + stamt;

						//if print is true
						if(print == true)
						{
							//print transaction to the file
							bufferedWriter.write(act + Util.setRight(20,stamt) + Util.setRight(20,moneyBalance));
							bufferedWriter.newLine();

							//if over is true
							if(over == true)
							{
								//subtract $20 fee from balance
								dbbal = dbbal - FEE;
								stbal = mon.format(dbbal) + "";
								moneyBalance = "$" + stbal;

								//print out $20 overchage fee to file
								bufferedWriter.write("OVERCHARGE" + Util.setRight(13,"$20 FEE") + Util.setRight(18,moneyBalance));
								bufferedWriter.newLine();
							}
						}
					}//END WHILE

					//close file
					in.close();

					//close file
					bufferedWriter.close();
			}
			//catch IO exceptions
			catch(IOException ex)
			{
				//prints out IO error message
				System.out.println("Error writing to file" + fileName + "'");
			}
			//prints off menu to ask user for next action
			System.out.println(Util.setLeft(58,"MENU"));
			System.out.println(Util.setLeft(50,"--------------------"));
			System.out.println(Util.setLeft(54,"f = input file"));
			System.out.println(Util.setLeft(54,"q = quit\n"));
			System.out.print(Util.setLeft(54,"Choice  :  "));
			choice = Keyboard.readString();
			System.out.println();
		}//END WHILE
    }
}
