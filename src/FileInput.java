/**
 * Created by Andy on 2/13/18.
 */

import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;

public class FileInput {

    //declare appropriate data types for loading information from file to be passed to FSA

    private static final String ALPHABET = "a b c d e f g h i j k l m n o p q r s t u v w x y z";

    private String inputPath = "MachineInput.txt";
    private String currentLine, initialRead, testLine;
    private ArrayList<String> testStrings = new ArrayList<>();


    /**
     * Class constructor calls {@link #readFile() readFile} to begin file reading
     */
    public FileInput() throws IOException{
        readFile();
    }

    /**
     * This method is responsible for reading from the file
     * @throws IOException if file can't be read from
     */
    private void readFile() throws IOException{

        //iteration count of machine
        int counter= 1;
        currentLine=" ";
        boolean exit = false;
        initialRead = "";
        try{

            FileReader file = new FileReader(inputPath);
            BufferedReader read = new BufferedReader(file);


            //while currentLine is not empty
            while(!exit  && !(currentLine.isEmpty())){

                //read current line
                currentLine = read.readLine();

                //if '*' then new FSA machine
                if(("***").equals(currentLine)){

                    //Read appropriate information and parse when needed
                    initialRead = read.readLine();
                    testLine = read.readLine();

                    testStrings = getTesters();
                    //Pass on info to FSA
                    NFSA nfa = new NFSA(initialRead, testStrings, counter);

                    testStrings.clear();

                    counter++;
                }
                if(currentLine==null)
                    exit = true;

            }

            System.out.println("Done!");
            read.close();
        }
        catch (FileNotFoundException e){
            System.out.println("Can't open file.");
        }
        catch (IOException i){
            System.out.println("Can't read file.");
        }
    }

    /**
     * This method parses the line with the test strings from the input file into an array list
     * @return ArrayList populated with individual test strings
     */
    private ArrayList<String> getTesters(){

        String[] temp = testLine.split(" ");
        for(String var : temp){
            testStrings.add(var);
        }
        return testStrings;
    }


}
