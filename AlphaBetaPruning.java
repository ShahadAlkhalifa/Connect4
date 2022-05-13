import java.util.ArrayList;

public class AlphaBetaPruning  implements Cloneable{

    int depth;
    int v;

    public AlphaBetaPruning(int dep ){
        depth = dep;
        v = 0;
    }

    public int AlphaBetaSearch(State state) throws CloneNotSupportedException {
        ArrayList<Integer> validMoves = state.getLegalActions();
        double alpha = -1000000;
        double beta = 1000000;
        double v =  -1000000;
        double y;
        for (int i = 0; i < validMoves.size(); i++) {
             y = MinBeta(state.generateSuccessor('O', validMoves.get(i)), depth-1, alpha, beta);
             if (y > v){
                 v = y;
                 this.v =  validMoves.get(i);

             }
        }
        return this.v;
    }

    public double MinBeta(State state, int d,  double a, double b) throws CloneNotSupportedException {
        ArrayList<Integer> children = state.getLegalActions();
        if (d == 0 ||children.size() == 0)
            return state.evaluationFunction(); //If terminal nodes are reached then return the evaluation function
        else {
            double beta  = b;
            double value = 1000000;
            double z=0;
            for (int i = 0; i < children.size(); i++) {
                if (a < beta)
                    z = MaxAlpha(state.generateSuccessor('X', children.get(i)), d-1, a,b);
                if (z <= value){
                    value = z;
                    this.v = i;
                }
                if (value < beta)
                    beta = value;

                if (value <= a)
                    return value;
            }
            return beta;
        }
    }

    public double MaxAlpha(State state, int d, double a, double b) throws CloneNotSupportedException {
        ArrayList<Integer> children = state.getLegalActions();
        if (d == 0 ||children.size() == 0)
            return state.evaluationFunction(); //If terminal nodes are reached then return the evaluation function
        else {
            double alpha  = a;
            double value = -1000000;
            double z=0;
            for (int i = 0; i < children.size(); i++) {
                if (alpha < b)
                    z = MinBeta(state.generateSuccessor('0', children.get(i)), d-1, a,b);
                if (z>=value)
                    value = z;
                if (value > alpha)
                    alpha = value;
                if (value >= b)
                    return value;
            }
            return alpha;
        }


    }



}
