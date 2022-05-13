import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Point;
import java.util.*;
import java.util.Random;

class State implements Cloneable
{

    int rows, cols;
    char[][] board;

    /* basic methods for constructing and proper hashing of State objects */
    public State(int n_rows, int n_cols){
        this.rows=n_rows;
        this.cols=n_cols;
        this.board=new char[n_rows][n_cols];

        //fill the board up with blanks
        for(int i=0; i<n_rows; i++)
            for(int j=0; j<n_cols; j++)
                this.board[i][j]='.';
    }

    public boolean equals(Object obj){

        State other=(State)obj;
        return Arrays.deepEquals(this.board, other.board);
    }

    public int hashCode(){
        String b="";
        for(int i=0; i<board.length; i++)
            b+=String.valueOf(board[0]);
        return b.hashCode();
    }

    public Object clone() throws CloneNotSupportedException {
        State new_state=new State(this.rows, this.cols);
        for (int i=0; i<this.rows; i++)
            new_state.board[i] = (char[]) this.board[i].clone();
        return new_state;
    }



    /* returns a list of actions that can be taken from the current state
    actions are integers representing the column where a coin can be dropped */
    public ArrayList<Integer> getLegalActions(){
        ArrayList<Integer> actions=new ArrayList<Integer>();
        for(int j=0; j<this.cols; j++)
            if(this.board[0][j]=='.')
                actions.add(j);
        return actions;
    }

    /* returns a State object that is obtained by the agent (parameter)
    performing an action (parameter) on the current state */
    public State generateSuccessor(char agent, int action) throws CloneNotSupportedException{

        int row;
        for(row=0; row<this.rows && this.board[row][action]!='X' && this.board[row][action]!='O'; row++);
        State new_state=(State)this.clone();
        new_state.board[row-1][action]=agent;

        return new_state;
    }

    /* Print's the current state's board in a nice pretty way */
    public void printBoard(){
        System.out.println(new String(new char[this.cols*2]).replace('\0', '-'));
        for(int i=0; i<this.rows; i++){
            for(int j=0; j<this.cols; j++){
                System.out.print(this.board[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println(new String(new char[this.cols*2]).replace('\0', '-'));
    }

    /* returns True/False if the agent(parameter) has won the game
    by checking all rows/columns/diagonals for a sequence of >=4 */
    public boolean isGoal(char agent){

        String find=""+agent+""+agent+""+agent+""+agent;

        //check rows
        for(int i=0; i<this.rows; i++)
            if(String.valueOf(this.board[i]).contains(find))
                return true;

        //check cols
        for(int j=0; j<this.cols; j++){
            String col="";
            for(int i=0; i<this.rows; i++)
                col+=this.board[i][j];

            if(col.contains(find))
                return true;
        }

        //check diags
        ArrayList<Point> pos_right=new ArrayList<Point>();
        ArrayList<Point> pos_left=new ArrayList<Point>();

        for(int j=0; j<this.cols-4+1; j++)
            pos_right.add(new Point(0,j));
        for(int j=4-1; j<this.cols; j++)
            pos_left.add(new Point(0,j));
        for(int i=1; i<this.rows-4+1; i++){
            pos_right.add(new Point(i,0));
            pos_left.add(new Point(i,this.cols-1));
        }

        //check right diags
        for (Point p : pos_right) {
            String d="";
            int x=p.x, y=p.y;
            while(true){
                if (x>=this.rows||y>=this.cols)
                    break;
                d+=this.board[x][y];
                x+=1; y+=1;
            }
            if(d.contains(find))
                return true;
        }

        //check left diags
        for (Point p : pos_left) {
            String d="";
            int x=p.x, y=p.y;
            while(true){
                if(y<0||x>=this.rows||y>=this.cols)
                    break;
                d+=this.board[x][y];
                x+=1; y-=1;
            }
            if(d.contains(find))
                return true;
        }

        return false;

    }



    /* returns the value of each state for minimax to min/max over at
    zero depth.  */
    public double evaluationFunction(){

        if (this.isGoal('O'))
            return 1000.0;
        if (this.isGoal('X'))
            return -1000.0;

        return 0.0;
    }









}


public class connect4AI{

    public static void main(String[] args) throws CloneNotSupportedException{


        Scanner in = new Scanner(System.in);

        State s=new State(6,7);
        Instant start, end;
        Duration UserVSRandom, UserVSMinMax, UserVSAlphaBeta, MinMaxVSRandom, MinMaxVSAlphaBeta, AlphaBetaVSRandom;

        System.out.println("Enter the algorithm:\n(1) Random Agent\n(2) Min/Max Agent\n(3) Alpha-Beta Pruning Agent\n(4) Min/Max VS Random Agent\n"
                + "(5) Alpha-Beta Pruning Agent VS Random Agent");
        int algorithm = in.nextInt();

        switch (algorithm) {

            case 1:
                while(true){
                    start = Instant.now();
                    Random ran = new Random();
                    int random_move = ran.nextInt(7);
                    s = s.generateSuccessor('O', random_move);
                    s.printBoard();
                    //check if O won?
                    if(s.isGoal('O')) {
                        end = Instant.now();
                        break;
                    }
                    System.out.println("Choose your move");
                    int enemy_move = in.nextInt();
                    s = s.generateSuccessor('X', enemy_move);
                    s.printBoard();
                    //check if X won? break
                    if(s.isGoal('X')) {
                        end = Instant.now();
                        break;
                    }
                }
                UserVSRandom = Duration.between(start, end);
                System.out.println("User VS Random duration is: " + UserVSRandom);
                break;
            case 2:
                start = Instant.now();
                System.out.println("Enter the depth");
                int depth = in.nextInt();
                MinMax agent = new MinMax(depth);
                while (true) {
                    int action = agent.MinMaxDecision(s);
                    s = s.generateSuccessor('O', action);
                    s.printBoard();
                    //check if O won?
                    if(s.isGoal('O')) {
                        end = Instant.now();
                        break;
                    }
                    System.out.println("Choose your move");
                    int enemy_move = in.nextInt();
                    s = s.generateSuccessor('X', enemy_move);
                    s.printBoard();
                    //check if X won? break
                    if(s.isGoal('X')) {
                        end = Instant.now();
                        break;
                    }
                }
                UserVSMinMax = Duration.between(start, end);
                System.out.println("User VS MinMax duration is: " + UserVSMinMax);
                break;

            case 3:
                start = Instant.now();
                System.out.println("Enter the depth");
                int depth2 = in.nextInt();
                AlphaBetaPruning agent2 = new AlphaBetaPruning(depth2);
                while (true) {
                    int action = agent2.AlphaBetaSearch(s);
                    s = s.generateSuccessor('O', action);
                    s.printBoard();
                    //check if O won?
                    if(s.isGoal('O')) {
                        end = Instant.now();
                        System.out.println("Winner: Alpha-Beta Agent");
                        break;
                    }
                    System.out.println("Choose your move");
                    int enemy_move = in.nextInt();
                    s = s.generateSuccessor('X', enemy_move);
                    s.printBoard();
                    //check if X won? break
                    if(s.isGoal('X')) {
                        end = Instant.now();
                        System.out.println("Winner: User");
                        break;
                    }
                }
                UserVSAlphaBeta = Duration.between(start, end);
                System.out.println("User VS AlphaBeta duration is: " + UserVSAlphaBeta);
                break;

            case 4:
                start = Instant.now();
                System.out.println("Enter the depth");
                int depth1 = in.nextInt();
                MinMax agent1 = new MinMax(depth1);
                System.out.println("Random agent is O and MinMax agent is X");
                while (true) {
                    Random ran = new Random();
                    int random_move = ran.nextInt(7);
                    s = s.generateSuccessor('O', random_move);
                    s.printBoard();
                    //check if O won?
                    if(s.isGoal('O')) {
                        end = Instant.now();
                        break;
                    }
                    int action = agent1.MinMaxDecision(s);
                    s = s.generateSuccessor('X', action);
                    s.printBoard();
                    //check if X won? break
                    if(s.isGoal('X')) {
                        end = Instant.now();
                        break;
                    }
                }
                MinMaxVSRandom = Duration.between(start, end);
                System.out.println("MinMax VS Random agent duration is: " + MinMaxVSRandom);
                break;

            case 5:
                start = Instant.now();
                System.out.println("Enter the depth");
                int depth4 = in.nextInt();
                AlphaBetaPruning agent5 = new AlphaBetaPruning(depth4);
                System.out.println("Random agent is O and AlphaBetaPruning agent is X");
                while (true) {
                    Random ran = new Random();
                    int random_move = ran.nextInt(7);
                    s = s.generateSuccessor('O', random_move);
                    s.printBoard();
                    //check if O won?
                    if(s.isGoal('O')) {
                        end = Instant.now();
                        System.out.println("Winner: Random agent");
                        break;
                    }
                    int action = agent5.AlphaBetaSearch(s);
                    s = s.generateSuccessor('X', action);
                    s.printBoard();
                    //check if X won? break
                    if(s.isGoal('X')) {
                        end = Instant.now();
                        System.out.println("Winner: Alpha-Beta Agent");
                        break;
                    }
                }
                AlphaBetaVSRandom = Duration.between(start, end);
                System.out.println("AlphaBeta VS Random agent duration is: " + AlphaBetaVSRandom);
                break;
        }




    }
}