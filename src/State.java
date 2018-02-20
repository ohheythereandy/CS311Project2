import java.util.*;

/**
 * Created by Andy on 2/18/18.
 * This class represents the states created from converting an NFSA to a DFSA
 */
public class State {


    private Set<Integer> stateSet;
    private int id;
    private List<Set<Integer>> transition;
    private boolean isFinal;

    /**
     * Constructor for State Object. Calls appropriate methods to load transitions into List
     * @param id is the identifier for the state
     * @param stateSet is the possible set of states this State can take on
     * @param transitionSize is the number of transition sets needed to fit
     * @param nfa is the 2D array of sets that represent the nsfa
     */
    public State(int id, Set<Integer> stateSet , int transitionSize, Set<Integer>[][] nfa){
        this.id = id;
        this.stateSet = stateSet;
        isFinal = false;
        transition = new ArrayList<>();
        instantiateInitialSets(transitionSize);
        loadTransitions(nfa);

    }

    /**
     * This method instantiates the required sets into the list
     * @param transitionSize is the number of transition sets required to fit into needed format
     */
    private void instantiateInitialSets(int transitionSize) {

        for(int i =0; i< transitionSize; i++){
            transition.add(new HashSet<Integer>());
        }
    }

    /**
     * This set will load appropriate integers into corresponding transition sets by scanning the set of possible states that
     * this state an take on, and adding their transitons appropriately
     * @param nfa
     */
    private void loadTransitions(Set<Integer>[][] nfa) {

        //for each state, check to see if
        for(int state: stateSet){

            for(int j = 0; j < nfa[state].length; j++){

                if(!(nfa[state][j].isEmpty())){

                    //for each integer in the set, add it to our corresponding transition set
                    for(int trans: nfa[state][j]){
                        transition.get(j).add(trans);
                    }
                }
            }

        }

    }

    /**
     * Getter method for internal id
     * @returns state identifier
     */
    public int getID(){
        return id;
    }

    /**
     * Getter method for the set that represents possible current states
     * @return
     */
    public Set<Integer> getStates() {
        return stateSet;
    }

    /**
     * Getter method for the list of transitions for the current state
     * @return List of Sets that represent the transitions
     */
    public List<Set<Integer>> getTransitions(){
        return transition;
    }

    /**
     * Method to set a state as a final state
     */
    public void setFinal() {
        isFinal = true;
    }

    /**
     * Check to see if state is a final state
     * @return
     */
    public boolean isFinal(){
        return isFinal;
    }
}
