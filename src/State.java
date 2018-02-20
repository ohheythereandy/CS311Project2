import java.util.*;

/**
 * Created by Andy on 2/18/18.
 */
public class State {

    private Set<Integer> stateSet;
    private int id;
    private List<Set<Integer>> transition;
    private boolean isFinal;

    public State(int id, Set<Integer> stateSet , int transitionSize, Set<Integer>[][] nfa){
        this.id = id;
        this.stateSet = stateSet;
        isFinal = false;
        transition = new ArrayList<>();
        instantiateInitialSets(transitionSize);
        loadTransitions(nfa);

    }


    private void instantiateInitialSets(int transitionSize) {

        for(int i =0; i< transitionSize; i++){
            transition.add(new HashSet<Integer>());
        }
    }

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

    public int getID(){
        return id;
    }

    public Set<Integer> getStates() {
        return stateSet;
    }

    public List<Set<Integer>> getTransitions(){
        return transition;
    }

    public void setFinal() {
        isFinal = true;
    }

    public boolean isFinal(){
        return isFinal;
    }
}
