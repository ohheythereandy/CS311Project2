/**
 * Created by Andy on 2/18/18.
 */
import java.io.*;
import java.util.*;
public class DFSA {

    private static final String ALPHABET = "a b c d e f g h i j k l m n o p q r s t u v w x y z";


    private HashMap<Character, Integer> internalAlphabet;
    private boolean[] oldFinalState, finalState;
    private ArrayList<String> testers, transitionArray;
    private ArrayList<State> stateList = new ArrayList<>();
    int[][] fsa_machine;


    //List to keep track of

    public DFSA(Set<Integer>[][] nfa, HashMap<Character, Integer> internalAlphabet, boolean[] finalState, ArrayList<String> transitionArray, ArrayList<String> testers, int iteration) throws IOException{

        this.internalAlphabet = internalAlphabet;
        oldFinalState = finalState;
        this.transitionArray = transitionArray;
        this.testers = testers;

        subsetConstruction(nfa);
        getFinalStates();
        printToFile(iteration);
    }

    private void getFinalStates() {

        finalState = new boolean[stateList.size()];

        List<Integer> finalStateList = new ArrayList<>();

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

    private void printToFile(int iteration) throws IOException{
        
        try{
            FileWriter fw = new FileWriter("FSAOutputFile.txt", true);
            BufferedWriter writer = new BufferedWriter(fw);
            writer.write("-------------------------\n");
            writer.write("Finite State Automaton #" + iteration + "\n");
            writer.write("(1) Number of States: " + stateList.size() + "\n");
            writer.write("(2) Final States: " + printFinalStates() + "\n");
//            writer.write("(3) Transitions:\n");
//            printTransitions();
//
//            writer.write("(4) Strings: \n" );
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
            System.out.println("Could not write to file");
        }
        
    }

    private boolean testFSA(String s) {
        return false;
    }

    private void printTransitions() {

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
     * We then set
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

    private int findEquivalentSet(Set<Integer> workingSet) {

        int ret = 0;
        for(State current : stateList){

           if(current.getStates().equals(workingSet))
               ret = current.getID();
        }

        return ret;
    }

    //This method navigates through given set and returns a string of all the integers in the set
    private String returnSet(int firstIndex, int secondIndex, Set<Integer>[][] nfa){
        StringBuilder sb = new StringBuilder();
        Set<Integer> tempSet = nfa[firstIndex][secondIndex];

        for(Integer i : tempSet){
            sb.append(i + " ");
        }

        return sb.toString();
    }
}
