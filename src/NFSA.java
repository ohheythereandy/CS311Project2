/**
 * Created by Andy on 2/15/18.
 */
import java.util.*;
import java.io.*;

public class NFSA {


    private static final String ALPHABET = "a b c d e f g h i j k l m n o p q r s t u v w x y z";
    private HashMap<Character, Integer> internalAlphabet = new HashMap<>();
    private int totalStates;
    private boolean[] finalState;
    private ArrayList<String> testers, transitionArray;
    private ArrayList<Character> actualAlphabet;
    private String firstLine;
    private String[] initInput;
    Set<Integer>[][] nfa;


    public NFSA(String initialRead, ArrayList<String> testStrings, int iteration) throws IOException{

        firstLine = initialRead;
        testers = testStrings;
        transitionArray = new ArrayList<>();
        actualAlphabet = new ArrayList<>();
        totalStates = 1;
        //parse out total state number and alphabet
        loadStates();
        setFinalStates();
        loadMachine();
        printToFile(iteration);

        //Create new DFSA from NFSA info
        DFSA dfa = new DFSA(nfa, internalAlphabet,finalState, transitionArray, testers, iteration);


    }

    private void printToFile(int iteration) throws IOException{

        try {
            FileWriter fw = new FileWriter("FSAOutputFile.txt", true);
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write("-------------------------\n");
            writer.write("Non-Finite State Automaton #" + iteration + "\n");
            writer.write("(1) Number of States: " + totalStates + "\n");
            writer.write("(2) Final States: " + printFinalStates() + "\n");
            writer.write("(3) Alphabet: " + ALPHABET + "\n");
            writer.write("(4) Transitions:\n");
            printTransitions(writer);

//            writer.write("(5) Strings: \n" );
//
//            for(String s: testers){
//
//                if(testFSA(s))
//                    writer.write("      " + s + "       " + "Accepted\n");
//                else
//                    writer.write("      " + s + "       " + "Rejected\n");
//            }

            writer.close();

        }
        catch(IOException i){
            System.out.println("Unable to print.");
        }
    }


    /**
     * This method is responsible for printing the transitions loaded into the table by traversing through the array,
     * looking for non empty sets.
     * @param bw is used to write to the file
     * @throws IOException if file can't be written to
     */
    private void printTransitions(BufferedWriter bw) throws IOException {
        //traverse through 2D array of sets to print transitions
        for(int i = 0; i < nfa.length; i++){

            for(int j = 0; j < nfa[i].length; j++){

                if(!(nfa[i][j].isEmpty())){
                    //bw.write("        " + "i " + actualAlphabet.get(j).toString() +  "\n");
                    transitionArray.add("        " + i + " " + actualAlphabet.get(j).toString() + "  " + printSet(i,j) +  "\n");
                }
            }
        }

        //write all transitions
        for(String s : transitionArray){
            bw.write(s + "\n");
        }
    }

    //This method navigates through given set and returns a string of all the integers in the set
    private String printSet(int firstIndex, int secondIndex){
        StringBuilder sb = new StringBuilder();
        Set<Integer> tempSet = nfa[firstIndex][secondIndex];

        for(Integer i : tempSet){
            sb.append(i + " ");
        }

        return sb.toString();
    }

    /**
     * This method is responsible for iterating through the FinalState array and printing out the indices that are final states
     * @return String containing all final states represented as integers
     */
    private String printFinalStates() {
        int count = 0;
        StringBuilder sb = new StringBuilder();
        for(boolean b : finalState){
            if(b){
                sb.append(count + " ");
            }
            count++;
        }
        return sb.toString();
    }

    /**
     * This method is responsible for adding transitions to the table
     * Also keeps track of the alphabet and numbers them internally
     */
    private void loadMachine(){
        initializeSets();

        int currentState = 0;
        int lastPass = 0;
        ArrayList<Character> tempAlphabet = new ArrayList<>();
        //loop through characters in each string and add each one to the list of letters and the map to number it
        //Also start creating nfa by adding transitions.
        //currentState represents the count of state we are in
        //We use the map to get the internal number for the letter
        for(String input: initInput){
            char[] temp = input.toCharArray();
            for(char letter: temp){

                currentState++;

//                //if first letter in string
//                if(letter == input.indexOf(0)){
//                    //if the letter already is in list, go back to initial state
//                    if(!(tempAlphabet.contains(letter)))
//                    {
//                        nfa[(currentState - 1)][internalAlphabet.get(letter)].add((currentState));
//                        tempAlphabet.add(letter);
//                    }
//                    else{
//                        nfa[0][internalAlphabet.get(letter)].add((currentState));
//                    }
//                }

                //if the letter already is in list, go back to initial state
                if(letter == temp[0]){
                    nfa[0][internalAlphabet.get(letter)].add((currentState));
                }
                else if(!(tempAlphabet.contains(letter)))
                {
                    nfa[(currentState - 1)][internalAlphabet.get(letter)].add((currentState));
                    tempAlphabet.add(letter);
                }
                else{

                        nfa[currentState-1][internalAlphabet.get(letter)].add(currentState);


                }


            }
        }

    }

    /**
     * To intialize all sets within nfa in an effort to avoid NullPointerException
     */
    private void initializeSets() {
        nfa = new HashSet[totalStates][actualAlphabet.size()];

        for(int i = 0; i < totalStates; i++){
            for(int j = 0; j < actualAlphabet.size(); j++){
                nfa[i][j] = new HashSet<Integer>();
            }
        }
    }


    /**
     * This Method is responsible for setting the final states inside the boolean array
     */
    private void setFinalStates(){
        finalState = new boolean[totalStates];
        int currentState = 0;
        for(String input: initInput) {

            currentState += (input.length());
            finalState[currentState] = true;
        }
    }

    /**
     * This method first checks if line read is empty
     * If not empty, then it splits the input strings into an array of strings and keeps count of total
     * character count between all input strings
     *
     */
    private void loadStates(){
        //check to see if line is empty
        if(firstLine.isEmpty())
            System.out.println("Error!");
        else{

            initInput = firstLine.split(" ");
            int count = 0;
            for(String input: initInput){
                totalStates += input.length();
                char[] temp = input.toCharArray();
                //for each character, add unique ones to alphabet list
                //also map
                for(char state: temp){

                    if(!(actualAlphabet.contains(state)))
                        actualAlphabet.add(state);
                    if(!(internalAlphabet.containsKey(state))){
                        internalAlphabet.put(state, count);
                        count++;
                    }
                }
            }
        }
    }


}
