public class Solver {
    
    private MinPQ<BoardNode> mpqNode = new MinPQ<BoardNode>();
    //private MinPQ<BoardNode> mpqTwinNode = new MinPQ<BoardNode>();
    private boolean solvable = true;
    private int insertCount = 0;
    public Solver(Board initial)
    {
        //Board twinBoard = initial.twin();
        BoardNode start = new BoardNode(initial, null, 0);
        //BoardNode startTwin = new BoardNode(twinBoard, null, 0);
        mpqNode.insert(start);
        //mpqTwinNode.insert(startTwin);
        BoardNode solution = null;
        //BoardNode twinsolution = null;
       
        //until we reach our goal
        while (!isSolved()){
            solution = mpqNode.delMin();
            //StdOut.println("mhp" + solution.manhattanP + "moves" + solution.numMoves);
            //twinsolution = mpqTwinNode.delMin();
            //StdOut.println("popped " + solution.current);
            for (Board b : solution.current.neighbors())
            {
                //StdOut.println(b);
                if ( solution.previous == null || !b.equals(solution.previous.current))
                {
                    mpqNode.insert(new BoardNode(b,solution,solution.numMoves+1));
                    insertCount = insertCount+1;
                }
                
            }

//            for (Board b : twinsolution.current.neighbors())
//            {
//                if ( twinsolution.previous == null || !b.equals(twinsolution.previous.current))
//                {
//                    mpqTwinNode.insert(new BoardNode(b,twinsolution,twinsolution.numMoves+1));
//                }
//            }
            //StdOut.println(mpqNode);
            if (solution.current.hamming() ==1 && solution.current.manhattan() ==2)
            {
                solvable = false;
            }
        }
        StdOut.println(insertCount + " IC");
        
//        if (mpqTwinNode.min().current.isGoal()) 
//            {
//                solvable = false;
//            }
        
    }
    private boolean isSolved()
    {
        if (mpqNode.min().current.isGoal())
        {
            return true;
        }
        else if (solvable == false)
        {
            return true;
        }
        
//        if (mpqTwinNode.min().current.isGoal())
//        {
//            return true;
//        }
        return false;
    }
    //boardnode class to use in minPQ
    private class BoardNode implements Comparable<BoardNode>
    {
        private Board current;
        private int manhattanP;
        private int numMoves;
        private BoardNode previous;
        
        BoardNode(Board board, BoardNode p, int moves)
        {
            current = board;
            previous = p;
            numMoves = moves;
            manhattanP = current.manhattan();
        }
        public int compareTo(BoardNode that)
        {
            return manhattanP + numMoves - (that.manhattanP + that.numMoves);
        }
    }
    
    public boolean isSolvable()            // is the initial board solvable?
    {
        return solvable;
    }
    public int moves()
    {
        // min number of moves to solve initial board; -1 if unsolvable
        if (!solvable)
        {
            return -1;
        }
        else
        {
            return mpqNode.min().numMoves;
        }
    }
    public Iterable<Board> solution()
    {
        // sequence of boards in a shortest solution; null if unsolvable
        if (!solvable)
        {
            return null;
        }
        Stack<Board> stack = new Stack<Board>();
        for (BoardNode b = mpqNode.min(); b !=null; b = b.previous)
        {
            stack.push(b.current);
        }
        return stack;
        
    }
    public static void main(String[] args)
    {
         // solve a slider puzzle (given below)
        In in = new In(args[0]);
        int N =  in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++){
            for (int j = 0; j < N; j++){
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);
        
        //solve the puzzle
        Solver solver = new Solver(initial);
        
        //print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else{
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
            
        }
        
            
    }
}