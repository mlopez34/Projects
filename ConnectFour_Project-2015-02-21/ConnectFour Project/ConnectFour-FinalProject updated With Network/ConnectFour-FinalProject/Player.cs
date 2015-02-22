using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConnectFour_FinalProject
{
    abstract class Player
    {
        public Player()
        {

        }
        public abstract int GetColumn(CFRound connectfour);
        public abstract void setPlayerOrder(CFRound connectfour, int playerOrder);
        public abstract Boolean taketurn(CFRound connectfour, int column, String color);
    }
}
