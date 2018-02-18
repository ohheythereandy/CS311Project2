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
    private ArrayList<String> testers;
    private ArrayList<Character> actualAlphabet;
    private String firstLine;
    private String[] initInput;
    HashSet<Integer>[][] nfa;


    public NFSA(String initialRead, ArrayList<String> testStrings, int iteration) throws IOException{

        firstLine = initialRead;
        testers = testStrings;
        actualAlphabet = new ArrayList<>();
        totalStates = 1;
        //parse out total state number and alphabet
        loadStates();
        setFinalStates();
        loadMachine();
        printToFile(iteration);


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

            writer.write("(5) Strings: \n" );

            for(String s: testers){

                if(testFSA(s))
                    writer.write("      " + s + "       " + "Accepted\n");
                else
                    writer.write("      " + s + "       " + "Rejected\n");
            }

        }
        catch(IOException i){
            i.printStackTrace();
        }
    }

    private boolean testFSA(String s) {
        return false;
    }

    private void printTransitions(BufferedWriter bw) throws IOException {
        //traverse through 2D array of sets to print transitions
        for(int i = 0; i < nfa.length; i++){

            for(int j = 0; j < nfa[i].length; j++){

                if(!(nfa[i][j].isEmpty())){
                    //bw.write("        " + "i " + actualAlphabet.get(j).toString() +  "\n");
                }
            }
        }
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
        nfa = new HashSet[totalStates][actualAlphabet.size()];

        int currentState = 0;

        //loop through characters in each string and add each one to the list of letters and the map to number it
        //Also start creating nfa by adding transitions.
        //currentState represents the count of state we are in
        //We use the map to get the internal number for the letter
        for(String input: initInput){
            char[] temp = input.toCharArray();
            for(char state: temp){

                currentState++;
                //if we are the last letter in a string then set final state
                if(temp[temp.length-1] == state){
                    finalState[currentState] = true;
                }
                actualAlphabet.add(state);
                internalAlphabet.put(state, actualAlphabet.indexOf(state));
                //if the letter already is in list, go back to initial state
                if(actualAlphabet.contains(state))
                {
                    nfa[0][internalAlphabet.get(state)].add((currentState));
                }
                else{
                    nfa[currentState - 1][internalAlphabet.get(state)].add((currentState ));
                }
            }
        }

    }


    /**
     * This Method is responsible for setting the final states inside the boolean array
     */
    private void setFinalStates(){
        finalState = new boolean[totalStates];
    }

    /**
     * This method first checks if line read is empty
     * If not empty, then it splits the input strings into an array of strings and keeps count of total
     * character count between all input strings
     *
     */
    private void loadStates(){
        if(firstLine.isEmpty())
            System.out.println("Error!");
        else{
            initInput = firstLine.split(" ");
            for(String input: initInput){
                totalStates += input.length();
            }
        }
    }


}
