//import java.lang.Math.*;
import java.util.Arrays;
    
public class Board {
    private int N;
    private int[][] tiles;
    //need the solution to the board
    
    public Board(int[][] blocks)           
    {
        // construct a board from an N-by-N array of blocks
        //MaxPQ pq = new MaxPQ();
        tiles = new int[blocks.length][blocks.length];
        N = blocks.length;
        tiles = blocks;
        // (where blocks[i][j] = block in row i, column j)
    }
                                           
    public int dimension()                 
    {
        // board dimension N
        return this.N;
    }
    public int hamming()                   
    {
        // number of blocks out of place
        int outOfPlace = 0;
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[i].length; j++)
            {
                if (tiles[i][j] != (i*N)+(j+1) && tiles[i][j] != 0)
                {
                    outOfPlace = outOfPlace + 1;
                }
            }
        }
        outOfPlace = outOfPlace;
        return outOfPlace;
    }
    public int manhattan()                 
    {
        // sum of Manhattan distances between blocks and goal
        int manh=0;
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[i].length; j++)
            {
                if (tiles[i][j] != (i*N)+(j+1) && tiles[i][j] != 0)
                {
                    int row = (tiles[i][j]-1) / N;
                    int column = (tiles[i][j]-1) % N;
                    int currentTile = Math.abs(row-i) + Math.abs(column-j);
                    manh = manh + currentTile;
                }
            }
        }
        return manh;
        
    }
    public boolean isGoal()                
    {
        // is this board the goal board?
        return (hamming() == 0);//could also check if grid is equal to solved
    }
    public Board twin()                    
    {
        Board T;
        // a board that is obtained by exchanging two adjacent blocks in the same row
        int[][] tilesTwin = new int[N][N];
        boolean done = false;
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles.length; j++)
            {
                tilesTwin[i][j] = tiles[i][j];
            }
        }
        //exchange
        for (int k = 0; k < tilesTwin.length; k++)
        {
            if (!done){
                for (int l = 0; l < tilesTwin[k].length-1; l++){
                    if ((tilesTwin[k][l] != 0) && (tilesTwin[k][l+1] != 0) && !done){
                        int temp = tilesTwin[k][l+1];
                        tilesTwin[k][l+1] = tilesTwin[k][l];
                        tilesTwin[k][l] = temp;
                        done = true;
                    }
                }
            }
        }
        //create twin
        T = new Board(tilesTwin);
        return T;
        
    }
    public boolean equals(Object y)        
    {
        // does this board equal y?
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        
        Board that = (Board) y;
        if (this.dimension() != that.dimension())
        {
            return false;
        }
        boolean result = true;
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles.length; j++)
            {
                if (tiles[i][j] != that.tiles[i][j])
                {
                    result = false;
                }
            }
        }
        return result;
    }
    public Iterable<Board> neighbors()     
    {
        // all neighboring boards
        Stack<Board> stack = new Stack<Board>();
        //check for left right up and down
        
        //find 0 tile
        int row = 0;
        int column = 0;
        Board tilesCopy1 = new Board(copy());
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles.length; j++)
            {
                if (tiles[i][j] == 0)
                {
                    row = i;
                    column = j;
                }
            }
        }
        //compare to top bottom left and right
        //top
        if ( row > 0)
        {
            //exchange with top
            //StdOut.println("top");
            int temp = tilesCopy1.tiles[row-1][column]; //get the tile on top
            tilesCopy1.tiles[row-1][column] = tilesCopy1.tiles[row][column];
            tilesCopy1.tiles[row][column] = temp;
            stack.push(tilesCopy1);
            tilesCopy1 = new Board(copy());
            
        }
        //bottom
        if (row+1 < N){
            //StdOut.println("bot");
            int temp = tilesCopy1.tiles[row+1][column]; //get the tile on botom
            tilesCopy1.tiles[row+1][column] = tilesCopy1.tiles[row][column];
            tilesCopy1.tiles[row][column] = temp;
            stack.push(tilesCopy1);
            tilesCopy1 = new Board(copy());
        }
        //left
        if (column > 0)
        {
            //StdOut.println("left");
            int temp = tilesCopy1.tiles[row][column-1]; //get the tile on left
            tilesCopy1.tiles[row][column-1] = tilesCopy1.tiles[row][column];
            tilesCopy1.tiles[row][column] = temp;
            stack.push(tilesCopy1);
            tilesCopy1 = new Board(copy());
        }
        //right
        if (column +1 < N){
            //StdOut.println("right");
            int temp = tilesCopy1.tiles[row][column+1]; //get the tile on right
            tilesCopy1.tiles[row][column+1] = tilesCopy1.tiles[row][column];
            tilesCopy1.tiles[row][column] = temp;
            stack.push(tilesCopy1);
            tilesCopy1 = new Board(copy());
        }
            
        return stack;
    }
    private int[][] copy()
    {
        int[][] copyTiles = new int[N][N];
        for (int i = 0; i < copyTiles.length; i++)
        {
            for (int j = 0; j < copyTiles.length; j++)
            {
                int shit = tiles[i][j];
                copyTiles[i][j] = shit;
            }
        }
        return copyTiles;
    }
    public String toString()               
    {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
        // string representation of this board 
    }

    public static void main(String[] args) 
    {
        // unit tests 
        int[][] grid = new int[3][3];
        int[][] gridb = new int[4][4];
        int[] grid1 = {0, 1, 3};
        int[] grid2 = {4, 2, 5};
        int[] grid3 = {7, 8, 6};
        int[] grid4 = {0, 1, 3, 12};
        int[] grid5 = {4, 2, 5, 11};
        int[] grid6 = {7, 8, 62, 10};
        int[] grid7 = {17, 15, 14, 13};
        grid[0] = grid1;
        grid[1] = grid2;
        grid[2] = grid3;
        gridb[0] = grid4;
        gridb[1] = grid5;
        gridb[2] = grid6;
        gridb[3] = grid7;
//        int num = 1;
//        for (int i = 0; i < grid.length; i++)
//        {
//            for (int j = 0; j < grid.length; j++)
//            {
//                grid[i][j] = num;
//                num = num+1;
//                
//            }
//        }
        
        
        Board b = new Board(grid);
        Board e = new Board(gridb);
        StdOut.println(b);
        StdOut.println(b.hamming());
        StdOut.println(b.manhattan());
        StdOut.println(b.isGoal());
        for (Board c : b.neighbors())
        {
            StdOut.println(c);
        }
        StdOut.println(b);
        StdOut.println(b.equals(e));
        StdOut.println(e.equals(b));
    }
}