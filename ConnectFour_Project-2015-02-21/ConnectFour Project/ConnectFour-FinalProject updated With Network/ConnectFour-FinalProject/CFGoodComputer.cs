using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConnectFour_FinalProject
{
    class CFGoodComputer : Player
    {
        private String Color;
        private Boolean firstMove;
        public CFGoodComputer(string color)
        {
            this.Color = color;
            this.firstMove = true;
        }
        public String getColor
        {
            get { return this.Color; }
        }
        public override void setPlayerOrder(CFRound connectfour, int playerOrder)
        {
            string order = playerOrder.ToString();
            connectfour.saveOrder(order);
        }

        public override int GetColumn(CFRound connectfour)
        {
            Random ran = new Random();
            int compColumn = 4;
            String[][] board = connectfour.getBoard2;
            //if first move play column 4
            if (firstMove)
            {
                firstMove = false;
                return 4;
            }
            else
            {
                Boolean valid = false;
                while (!valid)
                {
                    //check if someone will win
                    //if (connectfour.winCheck() == true) 
                    //{
                    //    //select the column NEED TO MAKE NEW FUNCTION THAT CHECKS FOR AN "ALMOST" WINNER
                    //    break;
                    //}
                    //check own columns and play a column 
                    bool keeplooping = true;
                    for (int j = 0; j < board.Length; j++)
                    {
                        for (int i = 0; i < board[j].Length; i++)
                        {
                            if ((board[j][i] == "Red" || board[j][i] == "yellow" ||board[j][i] == "blue") && (keeplooping))
                            {
                                //
                                int range = i+1;
                                if (range == 1)
                                {
                                    compColumn = ran.Next(range, range + 2);
                                }
                                else if (range == 7)
                                {
                                    compColumn = ran.Next(range - 1, range + 1);
                                }
                                else
                                {
                                    compColumn = ran.Next(range - 1, range + 2);
                                    //check if the column is valid, if it is break
                                }

                                if (connectfour.validColumnCheck(compColumn))
                                {
                                    keeplooping = false;
                                    break;
                                }
                                else
                                {
                                    continue;
                                }
                            }
                        }
                    }
                    Boolean secondValidCheck = false;
                    //return 4;
                    while (!secondValidCheck)
                    {
                        //make a random decision
                        compColumn = ran.Next(1, 8);
                        //check if the choice is valid, if not then start over again
                        if (connectfour.validColumnCheck(compColumn))
                        {
                            secondValidCheck = true;
                        }
                    }
                    valid = connectfour.validColumnCheck(compColumn);
                }

                return compColumn;
            }
        }


        public override Boolean taketurn(CFRound connectfour, int column, String color)
        {

            int columnPick = column;
            
            Boolean validColumn = true;
            validColumn = connectfour.validColumnCheck(column);

            if (validColumn == false)
            {
                return false;
            }
            else
            {
                connectfour.dropCircle(column, color);
                return true;
            }
        }
    }
}
