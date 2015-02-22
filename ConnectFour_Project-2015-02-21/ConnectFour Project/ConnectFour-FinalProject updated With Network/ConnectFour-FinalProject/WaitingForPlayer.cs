using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace ConnectFour_FinalProject
{
    public partial class WaitingForPlayer : Form
    {
        private CFBoard GameBoard;
        public WaitingForPlayer(CFBoard board)
        {
            InitializeComponent();
            this.GameBoard = board;


        }

        private void button1_Click(object sender, EventArgs e)
        {


            this.Invoke((MethodInvoker)delegate
            {
                GameBoard.GameClose(this);
            });

        }

        private void WaitingForPlayer_Load(object sender, EventArgs e)
        {

        }

        public void label1_Click(object sender, EventArgs e)
        {

        }


        private void label2_Click(object sender, EventArgs e)
        {

        }
    }
}
