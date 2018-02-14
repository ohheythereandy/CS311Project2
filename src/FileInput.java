/**
 * Created by Andy on 2/13/18.
 */

import java.io.FileNotFoundException;
import java.util.*;
import java.io.*;

public class FileInput {

    //declare appropriate data types for loading information from file to be passed to FSA

    private String inputPath = "/MachineInput.txt";
    private String finalStates, alphabet, currentLine;
    private ArrayList<String> transitions = new ArrayList<>();
    private ArrayList<String> testStrings = new ArrayList<>();
    private int totalStates;

    FileReader file = new FileReader(inputPath);
    BufferedReader read = new BufferedReader(file);

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
        //iteration of machine
        int counter= 1;
        currentLine="***";
        boolean exit = false;
        try{

            //read current line
            currentLine = read.readLine();

            //while currentLine is not empty
            while(!exit  && !(currentLine.isEmpty())){

                //if '*' then new FSA machine
                if(("***").equals(currentLine)){


                    transitions.clear();

                    //Read appropriate information and parse when needed
                    totalStates = Integer.parseInt(read.readLine());
                    finalStates = read.readLine();
                    alphabet = read.readLine();

                    currentLine = read.readLine();

                    transitions = getTrans();
                    testStrings = getTesters();

                    //Pass on info to FSA
                    //FSA fsa = new FSA(totalStates, finalStates, alphabet, transitions, testStrings, counter);

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

    /*
        This method checks to see if line starts with a '('
        indicating a transition. Continues to loop over until not found
     */
    private ArrayList<String> getTrans() throws IOException {

        while(currentLine.contains("("))
        {
            transitions.add(currentLine);
            currentLine = read.readLine();
        }

        return transitions;
    }

    /*
        This method copies test strings into the ArrayList
        Loops until line is empty or it is a new FSA
     */
    private ArrayList<String> getTesters() throws IOException {

        boolean exit = false;
        while( !exit &&((!currentLine.contains("***")) && ((currentLine)!=null)) )
        {
            testStrings.add(currentLine);
            currentLine = read.readLine();
            //avoid NullPointerException
            if(currentLine==null)
                exit=true;
        }
        return testStrings;
    }
}
