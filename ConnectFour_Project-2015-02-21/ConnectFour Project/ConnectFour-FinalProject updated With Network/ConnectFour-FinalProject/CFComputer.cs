using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Threading;

namespace ConnectFour_FinalProject
{
    class CFComputer: Player
    {
        private String Color;

        public CFComputer(string color)
        {
            this.Color = color;
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
            return 1;
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
