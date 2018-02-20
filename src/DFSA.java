/**
 * Created by Andy on 2/18/18.
 * This class represents the DFSA object
 * Information passed to it from the NFSA class where it is instantiated for every NFSA
 */
import java.io.*;
import java.util.*;
public class DFSA {


    private HashMap<Character, Integer> internalAlphabet;
    private HashMap<Integer, Character> reverseAlphabet;
    private boolean[] oldFinalState, finalState;
    private ArrayList<String> testers, transitionArray;
    private ArrayList<State> stateList = new ArrayList<>();
    int[][] fsa_machine;


    /**
     * Class constructor
     * @param nfa is the 2d array of sets representing the NFA
     * @param internalAlphabet is the mapping of characters to an integer
     * @param finalState is the array of final states for the nfa
     * @param testers is the set of test strings to run the machine against
     * @param iteration is the machine count
     * @throws IOException if file can't be written to for whatever reason
     */
    public DFSA(Set<Integer>[][] nfa, HashMap<Character, Integer> internalAlphabet, boolean[] finalState, ArrayList<String> testers, int iteration) throws IOException{

        this.internalAlphabet = internalAlphabet;
        getReverseAlphaMap();
        oldFinalState = finalState;
        this.transitionArray = new ArrayList<>();
        this.testers = testers;

        subsetConstruction(nfa);
        getFinalStates();
        printToFile(iteration);
    }

    /**
     * This method creates a map reversing the key value pairs in the initial internalAlphabet map
     */
    private void getReverseAlphaMap() {
        reverseAlphabet = new HashMap<>();

        for(char i : internalAlphabet.keySet()){
            reverseAlphabet.put(internalAlphabet.get(i), i);
        }

    }

    /**
     * This method parses through our list of State objects to identify final states from previous final state information
     */
    private void getFinalStates(){

        finalState = new boolean[stateList.size()];

        List<Integer> finalStateList = new ArrayList<>();

        //add final states to list
        for(int i = 0 ; i < oldFinalState.length; i++){

            if(oldFinalState[i]){
                finalStateList.add(i);
            }
        }

        //iterate through state list to see if they are final states
        for(State currentState: stateList){

            //run check against each final state
            for(int i: finalStateList){

                if(currentState.getStates().contains(i)){
                    currentState.setFinal();
                }
            }
        }

        for(State currentState: stateList){

            if(currentState.isFinal()){
                finalState[currentState.getID()] = true;
            }
        }


    }

    /**
     * This method is responsible for printing to the file
     * @param iteration is the FSA number being tested
     * @throws IOException whenever file can't be written to
     */
    private void printToFile(int iteration) throws IOException{
        
        try{
            FileWriter fw = new FileWriter("MachineOutput.txt", true);
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write("-------------------------\n");
            writer.write("Finite State Automaton #" + iteration + "\n");
            writer.write("(1) Number of States: " + stateList.size() + "\n");
            writer.write("(2) Final States: " + printFinalStates() + "\n");
            writer.write("(3) Transitions:\n");
            printTransitions(writer);

           writer.write("(4) Strings: \n" );

            for(String s: testers){

                if(testFSA(s))
                    writer.write("      " + s + "       " + "Accepted\n");
                else
                    writer.write("      " + s + "       " + "Rejected\n");
            }

            writer.close();
        }
        catch(IOException i){
            System.out.println("Could not write to file");
        }
        
    }

    /**
     * This is the implementation of the algorithm outlined in the Project Document
     * @param s is the current string being tested against the FSA
     * @return true if string is accepted by FSA, false otherwise
     */
    private boolean testFSA(String s) {

        //set state to initial state and boolean to false
        int state = 0;
        int testCount = 0;
        boolean exit = false;
        char[] ch = s.toCharArray();
        boolean ret = false;

        while(!exit){

            int symbol;
            if(testCount < s.length()){

                if(!(internalAlphabet.containsKey(ch[testCount]))){
                    return false;
                }

                symbol = internalAlphabet.get(ch[testCount]);

            }
            else{
                symbol = 100;
            }

            //check to see if character is in alphabet
            if(reverseAlphabet.containsKey(symbol)){
                state = fsa_machine[state][symbol];

                //check to see if trap state
                if(state == 0){
                    exit = true;
                    ret = false;
                }

            }
            else{
                exit = true;

                if(!(isEndMarker(s, testCount))){
                   ret = false;
                }
                else if(finalState[state]){
                    ret = true;
                }
                else{
                    ret = false;
                }
            }
            testCount++;
        }

        return ret;
    }

    /**
     * This method compares the string length to the index counter to see if we are at the end of the string
     * @param s is the current test string
     * @param testCount is the current index within the test string that we are currently at
     * @return true if end of string, false otherwise
     */
    private boolean isEndMarker(String s, int testCount) {

        return s.length() == (testCount);
    }

    /**
     * This method prints transitions by traversing the List of State objects and seeing if their corresponding
     * transitions are empty
     * @param writer is the bufferedwriter used to write to the file
     * @throws IOException if the file can't be written to
     */
    private void printTransitions(BufferedWriter writer) throws IOException {

        for(State currentState: stateList){

            StringBuilder sb = new StringBuilder();
            sb.append("state " + currentState.getID() + ": " );
            boolean existTransition = false;

            HashMap<Integer, Integer> temp = new HashMap<>();
            for(int j = 0; j < fsa_machine[currentState.getID()].length; j++){

                if(fsa_machine[currentState.getID()][j] != 0){
                    temp.put(j, fsa_machine[currentState.getID()][j]);
                    existTransition = true;
                }
            }

            if(existTransition){

                for(int i : temp.keySet()){

                    if(temp.size() == 1){
                        sb.append(reverseAlphabet.get(i) + " "  + temp.get(i));
                    }
                    else{
                        sb.append( reverseAlphabet.get(i) + " "  + temp.get(i) + ", ");
                    }
                }


            }
            else{
                sb.append("none");
            }

            writer.write(sb.toString() + "\n");
        }

    }

    private String printFinalStates() {

        int count = 0;
        StringBuilder sb = new StringBuilder();
        for(boolean b: finalState)
        {

            if(b)
                sb.append(count + " ");
            count++;
        }
        return sb.toString();

    }

    /**
     * Implementation of subset construction algorithm covered in lecture
     * Use Queue to keep track of which state sets we need to create states for
     * Keep track of what state sets are already accounted for using list
     * @param nfa is the nfa 2D array of sets we use to build our DFA from
     */
    private void subsetConstruction(Set<Integer>[][] nfa) {

        //set initial state to 0
        int stateCounter = 0;
        Set<Integer> initSet = new HashSet<>();
        initSet.add(0);

        Queue<Set<Integer>> processStates = new LinkedList<>();
        processStates.add(initSet);

        //While there are still states to process
        while(!(processStates.isEmpty())){
            Set<Integer> newStateSet = processStates.remove();
            State currentState = new State( stateCounter , newStateSet , internalAlphabet.size(), nfa);
            stateCounter++;


            List<Set<Integer>> temp = currentState.getTransitions();

            for(Set<Integer> s : temp){

                if(!(s.isEmpty())){
                    if(!stateList.contains(s)){
                        processStates.add(s);
                    }
                }
            }

            stateList.add(currentState);

        }

        buildTransitionTable();
    }

    /**
     * In this method we build the transition table for the dfa
     * We go through each state, looking at its transition list and matching it to an existing set in our list
     * We then set the identifier of the matching state set to the integer in the 2d array
     */
    private void buildTransitionTable() {

        //new table size will be length of alphabet by length of states
        fsa_machine = new int[stateList.size()][internalAlphabet.size()];


        for(int i = 0; i < stateList.size(); i++){

            List<Set<Integer>> temp = stateList.get(i).getTransitions();

            for(int j = 0; j < temp.size(); j++){
                Set<Integer> workingSet = temp.get(j);

                if(!(workingSet.isEmpty())){
                    int index = findEquivalentSet(workingSet);
                    fsa_machine[i][j] = index;
                }
            }
        }
    }

    /**
     * Helper function for buildTransitionTable()
     * Looks through existing state list for a matching set of current states. Get the identifier of the matching set
     * @param workingSet is the set we are looking for a match for
     * @return identifier of matching state set
     */
    private int findEquivalentSet(Set<Integer> workingSet) {

        int ret = 0;
        for(State current : stateList){

           if(current.getStates().equals(workingSet))
               ret = current.getID();
        }

        return ret;
    }

}
