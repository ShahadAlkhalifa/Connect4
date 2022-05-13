import java.util.ArrayList;

public class MinMax implements Cloneable{

    int depth;
    int x = 0;

    public MinMax(int dep) {
        depth = dep;
    }

    public int MinMaxDecision(State state) throws CloneNotSupportedException {
        double val = MaxValue(state, depth);
        return x;
    }

    public double MaxValue(State state, int d) throws CloneNotSupportedException {
        ArrayList<Integer> children = new ArrayList<Integer>();
        if (d == 0)
            return state.evaluationFunction(); //If terminal nodes are reached then return the evaluation function
        else {
            children = state.getLegalActions();
            double v = -1000000;
            double y;
            for (int i = 0; i < children.size(); i++) {
                y = MinValue(state.generateSuccessor('O', children.get(i)), d);

                if (y >= v) {
                    v = y;
                    this.x = i;
                }
            }
            return v;
        }

    }

    public double MinValue(State state, int d) throws CloneNotSupportedException {
        ArrayList<Integer> children = new ArrayList<Integer>();
        if (d == 0)
            return state.evaluationFunction(); //If terminal nodes are reached then return the evaluation function
        else {
            children = state.getLegalActions();
            double v = 1000000;
            double z;
            for (int i = 0; i < children.size(); i++) {
                z = MaxValue(state.generateSuccessor('X', children.get(i)), d-1);

                if (z <= v)
                    v = z;
            }
            return v;
        }
    }

}
