using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConnectFour_FinalProject
{
    class CFPlayer : Player
    {
        private String color;

        public CFPlayer(String color)
        {
            //constructs the player
            this.color = color;

        }
        public String getColor
        {
            get { return this.color; }
        }
        public override void setPlayerOrder(CFRound connectfour, int playerOrder)
        {
            string order = playerOrder.ToString();
            connectfour.saveOrder(order);
        }
        public override int GetColumn(CFRound connectfour)
        {
            return 1;
        }
        public override Boolean taketurn(CFRound connectfour, int column, String color)
        {
            //Console.WriteLine(name + " pick a column from 1 to 7 to place your circle");
            //read the column picked as a string
            int columnPick = column;
            
            //drop the circle on the board
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
