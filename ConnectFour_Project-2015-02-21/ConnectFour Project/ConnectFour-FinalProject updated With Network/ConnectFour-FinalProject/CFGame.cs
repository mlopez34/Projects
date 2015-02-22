using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;

namespace ConnectFour_FinalProject
{
    class CFGame
    {
        //public CFGame()
        //{
        //    //construct 2 players, red and blue
        //    redplayer = new CFPlayer("red player", "R");
        //    blueplayer = new CFPlayer("blue player", "B");
        //    //construct the board
        //    //connectfour = new CFRound();

        //}
        //private CFPlayer redplayer;
        //private CFPlayer blueplayer;
        //private int turn;
        //public int getTurn()
        //{
        //    return turn;
        //}

        //public void play()
        //{
        //    String fn = @"C:\Users\aLVaReZ\Desktop\gamelog.txt";
        //    FileStream fs = new FileStream(fn, FileMode.OpenOrCreate);
        //    fs.Close();
        //    //play the game
        //    Boolean winner = false;
        //    int currentplayer = 0;
        //    turn = 1;
        //    while (winner == false)
        //    {
        //        if (currentplayer % 2 == 0)
        //        {
        //            //connectfour.getBoard();
        //            redplayer.taketurn(connectfour);
        //        }
        //        else
        //        {
        //            //connectfour.getBoard();
        //            blueplayer.taketurn(connectfour);
        //        }
                //winner = connectfour.winCheck();
                //if (winner == true)
                //{
                //    //connectfour.getBoard();
                //    break;
                //}
                //currentplayer = currentplayer + 1;
                //turn = turn + 1;

        //    }
        //    if (currentplayer % 2 == 0)
        //    {
        //        Console.WriteLine("the winner is red player");
        //        Console.WriteLine("the winner is red player");
        //        Console.WriteLine("the winner is red player");
        //    }
        //    else 
        //    {
        //        Console.WriteLine("the winner is blue player");
        //        Console.WriteLine("the winner is blue player");
        //        Console.WriteLine("the winner is blue player");
        //    }
            
        //    Console.ReadLine();

        //}

    }
}
