using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO;
using ConnectFour_FinalProject.Properties;
using System.Media;
using System.Resources;
using System.Threading;
using System.Xml;
using System.Xml.Linq;

namespace ConnectFour_FinalProject
{
    public partial class CFBoard : Form
    {
        //used for changing the button picture
        private Button[][] listofButtons = new Button[6][];
        //player 1
        private Player firstplayer;
        //player 2
        private Player secondplayer;
        //current turn
        private Player[] players;
        private int turn = 1;
        //new game
        private CFRound connectfour;
        //list of colors, will change to the colors inside xml file chosen by config
        private String[] Currentcolor = {"placeholder1","placeholder2"};
        //used to check whose turn it is
        private int nextColor = 0;
        //used only if it is a replay
        private Boolean replay;
        //game state, if true then board won't change
        private Boolean gameOver = false;
        //used to check what move the replay is at
        private int replayIndex = 0;
        //array for storing turn moves for replay
        private int[] repturns;
        Random ran = new Random();
        private Boolean multiplayer = false;
        Boolean ResumeComplete = false;
        //for single player game
        Boolean MyTurn = false;
        Network network;
        Boolean waitingAlreadyClosed = false;
        string opponentIP;

        public CFBoard(Boolean rep)
        {
            InitializeComponent();
            loadsettings();
            //load  XML file options and change colors according to the options, change board size (all the buttons) according to the size
            //read xml for colors, Currentcolor[0] will be player 1, .....player 2
            //read for board size, if set to large resize to larger
            connectfour = new CFRound();
            firstplayer = new CFPlayer(Currentcolor[0]);
            secondplayer = new CFGoodComputer(Currentcolor[1]);
            players = new Player[2];
            connectfour.setplayer1 = Currentcolor[0];
            connectfour.setplayer2 = Currentcolor[1];
            replay = rep;
            textBox2.Text = "It is "+Currentcolor[nextColor%2] + "'s turn";
            ResumeComplete = true;
            //constructs the list of buttons
            createButtons();
            //order of who takes first turn and who takes second
            int order = ran.Next(0, 2);
            if (order == 1)
            {
                SwitchColors();
                players[0] = secondplayer;
                players[1] = firstplayer;
                players[0].setPlayerOrder(connectfour, 2);
                compfirstMove();
                //write to file first line is 2 which means computer goes first
            }
            else
            {
                players[0] = firstplayer;
                players[1] = secondplayer;
                players[0].setPlayerOrder(connectfour, 1);
                //write to file first line is 1 which means player goes first
            }

        }
        //for networked game
        public CFBoard(string mult, string IP)
        {
            InitializeComponent();
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.CFBoard_FormClosing);
            loadsettings();
            opponentIP = IP;
            gameOver = true;
            
            //load  XML file options and change colors according to the options, change board size (all the buttons) according to the size
            //read xml for colors, Currentcolor[0] will be player 1, .....player 2
            //read for board size, if set to large resize to larger
            connectfour = new CFRound();
            firstplayer = new CFPlayer(Currentcolor[0]);
            secondplayer = new CFPlayer(Currentcolor[1]);
            players = new Player[2];
            players[0] = firstplayer;
            players[1] = secondplayer;
            connectfour.setplayer1 = Currentcolor[0];
            connectfour.setplayer2 = Currentcolor[1];
            replay = false;
            textBox2.Text = "It is " + Currentcolor[nextColor % 2] + "'s turn";
            ResumeComplete = true;
            multiplayer = true;
            textBox1.Visible = true;
            //constructs the list of buttons
            createButtons();
            //if connection stops, whoever didnt disconect wins
            //make computer player better
        }
        //for resume or replay
        public CFBoard(int[] turns, Boolean rep, int playerTurnStart)
        {
            //used for resume game if rep = false, used for replay if rep = true
            InitializeComponent();
            loadsettings();
            //load  XML file options and change colors according to the options, change board size (all the buttons) according to the size
            //read xml for colors, Currentcolor[0] will be player 1, .....player 2
            //read for board size, if set to large resize to larger
            connectfour = new CFRound();
            firstplayer = new CFPlayer(Currentcolor[0]);
            secondplayer = new CFComputer(Currentcolor[1]);
            players = new Player[2];
            connectfour.setplayer1 = Currentcolor[0];
            connectfour.setplayer2 = Currentcolor[1];
            this.replay = rep;
            textBox2.Text = "It is " + Currentcolor[nextColor % 2] + "'s turn";
            //constructs the list of buttons
            createButtons();
            this.repturns = turns;
            //load turns inside turns array, if it is resuming game
            if (replay == false)
            {
                //first check which player was going first and set them up inside players array
                if (playerTurnStart == 1)
                {
                    //player went first
                    players[0] = firstplayer;
                    players[1] = secondplayer;
                    players[0].setPlayerOrder(connectfour, 1);
                }
                else
                {
                    //player went second
                    SwitchColors();
                    players[0] = secondplayer;
                    players[1] = firstplayer;
                    players[0].setPlayerOrder(connectfour, 2);
                }
                for (int i = 0; i < turns.Length; i++)
                {
                    Push(this.repturns[i]);
                }
                ResumeComplete = true;
            }
                //this means it is a replay
            else
            {

                button8.Visible = true;
                button9.Visible = true;
                gameOver = true;
                if (playerTurnStart == 1)
                {
                    //player went first
                    players[0] = firstplayer;
                    players[1] = secondplayer;
                }
                else
                {
                    //player went second
                    SwitchColors();
                    players[0] = secondplayer;
                    players[1] = firstplayer;
                }

            }
        }


        //for resume game, CFBoard will take a file (gamelog) as a parameter, it will create the game like usual but then read the file and put each turn
        //into a list of integers.it will then run the push function on this list until the list is empty which will fill up the game board

        //for replay, CFBOARD will take a file (replay) as a parameter, it will create the game like usual, read the file and put each turn
        //into a list of integers. 
        public void CreateNetwork()
        {
            network = new Network(this, opponentIP);
            network.SendReady();
        }
        
        public void createButtons()
        {
            this.listofButtons[0] = new[] { button00, button01, button02, button03, button04, button05, button06 };
            this.listofButtons[1] = new[] { button10, button11, button12, button13, button14, button15, button16 };
            this.listofButtons[2] = new[] { button20, button21, button22, button23, button24, button25, button26 };
            this.listofButtons[3] = new[] { button30, button31, button32, button33, button34, button35, button36 };
            this.listofButtons[4] = new[] { button40, button41, button42, button43, button44, button45, button46 };
            this.listofButtons[5] = new[] { button50, button51, button52, button53, button54, button55, button56 };
        }
        public int getTurn()
        {
            return turn;
        }
        private void playSimpleSound()
        {
            //Stream str = Properties.Resources.Clocktick;
            SoundPlayer simpleSound = new SoundPlayer(Properties.Resources.Clocktick);
            simpleSound.Play();
        }
        private void changeColor(int column)
        {
            //change first player
            if (nextColor % 2 == 0)
            {
                if (Currentcolor[0] == "Red")
                {
                    listofButtons[connectfour.getglobalRow][column - 1].BackgroundImage = Resources.red1;
                }
                else if (Currentcolor[0] == "Yellow")
                {
                    listofButtons[connectfour.getglobalRow][column - 1].BackgroundImage = Resources.yellow1;
                }
                else
                {
                    listofButtons[connectfour.getglobalRow][column - 1].BackgroundImage = Resources.Blue;
                }
            }
                //change second player
            else
            {
                if (Currentcolor[1] == "Red")
                {
                    listofButtons[connectfour.getglobalRow][column - 1].BackgroundImage = Resources.red1;
                }
                else if (Currentcolor[1] == "Yellow")
                {
                    listofButtons[connectfour.getglobalRow][column - 1].BackgroundImage = Resources.yellow1;
                }
                else
                {
                    listofButtons[connectfour.getglobalRow][column - 1].BackgroundImage = Resources.Blue;
                }

            }
        }
        public void SwitchColors()
        {
            connectfour.setplayer1 = Currentcolor[1];
            connectfour.setplayer2 = Currentcolor[0];
            string temp = Currentcolor[0];
            Currentcolor[0] = Currentcolor[1];
            Currentcolor[1] = temp;
            textBox2.Text = "It is " + Currentcolor[nextColor % 2] + "'s turn";
        }
        public Boolean GameOver
        {

            get { return this.gameOver; }
            set { this.gameOver = value; }
        }
        public Boolean myTurn
        {
            get { return this.MyTurn; }
            set { this.MyTurn = value; }
        }
        public async void compfirstMove()
        {
            if (players[0] is CFComputer)
            {
                int compFirstTurn = ran.Next(1, 8);
                gameOver = true;
                await Task.Delay(1000);
                gameOver = false;
                Push(compFirstTurn);
            }
            else if (players[0] is CFGoodComputer)
            {
                int compFirstTurn = players[0].GetColumn(connectfour);
                gameOver = true;
                await Task.Delay(1000);
                gameOver = false;
                Push(compFirstTurn);
            }
        }
        public void OpponentMove(int Move, bool legal)
        {
            try
            {
                this.Invoke((MethodInvoker)delegate
                {
                    if (legal)
                    {
                        textBox1.Text = "Your turn!";
                        gameOver = false;
                        Push(Move);
                        MyTurn = true;
                    }
                    else
                    {
                        textBox1.Text = "Found an opponent!";
                    }
                });
            }
            catch
            {
            }
            
        }
        public void foundPlayer(int localOrder, int ReceivedMove)
        {
            try
            {
                this.Invoke((MethodInvoker)delegate
                {
                    textBox1.Text = "Found an opponent!";
                    playSimpleSound();
                    if (localOrder < ReceivedMove)
                    {
                        SwitchColors();
                        
                    }
                });
                //await Task.Delay(3500);
                this.Invoke((MethodInvoker)delegate
                {

                    playSimpleSound();
                    if (localOrder > ReceivedMove)
                    {
                        connectionEst = true;
                        GameOver = false;
                        myTurn = true;
                        textBox1.Text = "Your Move First!";
                    }
                    else
                    {
                        connectionEst = true;
                        GameOver = true;
                        myTurn = false;
                        textBox1.Text = "Your Opponent Moves First!";
                    }

                });
            }
            catch
            {
            }
            
        }
        public void WinnerButtonsChange()
        {
            int[][] Bss = connectfour.buttonsOfInterest;
            //change the background of Bss[x][x] to whatever color is playing right now
            if (nextColor % 2 == 0)
            {
                if (Currentcolor[0] == "Red")
                {
                    listofButtons[Bss[0][0]][Bss[0][1]].BackgroundImage = Resources.red1Win;
                    listofButtons[Bss[1][0]][Bss[1][1]].BackgroundImage = Resources.red1Win;
                    listofButtons[Bss[2][0]][Bss[2][1]].BackgroundImage = Resources.red1Win;
                    listofButtons[Bss[3][0]][Bss[3][1]].BackgroundImage = Resources.red1Win;
                }
                else if (Currentcolor[0] == "Yellow")
                {
                    listofButtons[Bss[0][0]][Bss[0][1]].BackgroundImage = Resources.yellow1Win;
                    listofButtons[Bss[1][0]][Bss[1][1]].BackgroundImage = Resources.yellow1Win;
                    listofButtons[Bss[2][0]][Bss[2][1]].BackgroundImage = Resources.yellow1Win;
                    listofButtons[Bss[3][0]][Bss[3][1]].BackgroundImage = Resources.yellow1Win;
                }
                else
                {
                    listofButtons[Bss[0][0]][Bss[0][1]].BackgroundImage = Resources.BlueWin;
                    listofButtons[Bss[1][0]][Bss[1][1]].BackgroundImage = Resources.BlueWin;
                    listofButtons[Bss[2][0]][Bss[2][1]].BackgroundImage = Resources.BlueWin;
                    listofButtons[Bss[3][0]][Bss[3][1]].BackgroundImage = Resources.BlueWin;
                }
            }
            //change second player
            else
            {
                if (Currentcolor[1] == "Red")
                {
                    listofButtons[Bss[0][0]][Bss[0][1]].BackgroundImage = Resources.red1Win;
                    listofButtons[Bss[1][0]][Bss[1][1]].BackgroundImage = Resources.red1Win;
                    listofButtons[Bss[2][0]][Bss[2][1]].BackgroundImage = Resources.red1Win;
                    listofButtons[Bss[3][0]][Bss[3][1]].BackgroundImage = Resources.red1Win;
                }
                else if (Currentcolor[1] == "Yellow")
                {
                    listofButtons[Bss[0][0]][Bss[0][1]].BackgroundImage = Resources.yellow1Win;
                    listofButtons[Bss[1][0]][Bss[1][1]].BackgroundImage = Resources.yellow1Win;
                    listofButtons[Bss[2][0]][Bss[2][1]].BackgroundImage = Resources.yellow1Win;
                    listofButtons[Bss[3][0]][Bss[3][1]].BackgroundImage = Resources.yellow1Win;
                }
                else
                {
                    listofButtons[Bss[0][0]][Bss[0][1]].BackgroundImage = Resources.BlueWin;
                    listofButtons[Bss[1][0]][Bss[1][1]].BackgroundImage = Resources.BlueWin;
                    listofButtons[Bss[2][0]][Bss[2][1]].BackgroundImage = Resources.BlueWin;
                    listofButtons[Bss[3][0]][Bss[3][1]].BackgroundImage = Resources.BlueWin;
                }

            }
        }
        public async void Push(int column)
        {
            if (!gameOver)
            {
                if (nextColor % 2 == 0)
                {
                    Boolean valid = players[0].taketurn(connectfour, column, Currentcolor[(nextColor) % 2]);
                    Boolean winner = false;
                    if (valid)
                    {
                        playSimpleSound();
                        String[][] Board = connectfour.getBoard2;

                        changeColor(column);
                        
                        winner = connectfour.winCheck();
                        if (winner == true)
                        {
                            WinnerButtonsChange();
                            textBox2.Text = Currentcolor[(nextColor) % 2] + " wins!";
                            //label2.Text = (getTurn() + 1).ToString();
                            DialogResult saved;
                            if (multiplayer && MyTurn)
                            {
                                textBox1.Text = "You Win!";
                                network.Send(column.ToString());
                                network.CloseConnections();
                                connectionEst = false;
                                MyTurn = false;
                                gameOver = true;
                                network.legal = false;
                                saved = MessageBox.Show(Currentcolor[(nextColor) % 2] + "(You) wins!, Would you like to start a new game?", "Winner!!", MessageBoxButtons.YesNo, MessageBoxIcon.Information);

                            }
                            else if (multiplayer && !MyTurn)
                            {
                                connectionEst = false;
                                network.CloseConnections();
                                textBox1.Text = "Your Opponent Wins!";
                                saved = MessageBox.Show(Currentcolor[(nextColor) % 2] + "(Your Opponent) wins!, Would you like to start a new game?", "Darn it!!", MessageBoxButtons.YesNo, MessageBoxIcon.Information);

                            }
                            else
                            {
                                saved = MessageBox.Show(Currentcolor[(nextColor) % 2] + " wins!, Would you like to start a new game?", "Winner!!", MessageBoxButtons.YesNo, MessageBoxIcon.Information);
                            }
                            
                            textBox2.Text = Currentcolor[(nextColor) % 2] + " wins!";
                            
                            gameOver = true;
                            
                            //gameover = true, stops the button pushes from doing anything
                            if (saved == DialogResult.Yes)
                            {
                                
                                this.Close();

                                
                                Boolean rep = false;
                                CFBoard newGame;
                                await Task.Delay(1500);
                                
                                
                                string startupPath = System.IO.Directory.GetCurrentDirectory();
                                string ad = @"bin\Debug";
                                string path = startupPath.Replace(ad, "");
                                //textBox1.Text = result+"gamelog.txt";
                                String GlobalFile = path + "gamelog.txt";
                                FileStream fk = new FileStream(GlobalFile, FileMode.Create);
                                fk.Close();
                                if (multiplayer)
                                {
                                    newGame = new CFBoard("Multiplayer", opponentIP);
                                }
                                else
                                {
                                    newGame = new CFBoard(rep);
                                }
                                DialogResult result = newGame.ShowDialog();
                            }
                        }
                        else
                        {

                            turn = turn + 1;
                            nextColor = nextColor + 1;
                             label2.Text = getTurn().ToString();
                            
                            textBox2.Text = "It is " + Currentcolor[(nextColor) % 2] + "'s turn!";
                            
                            //computer takes a turn now
                            if ((ResumeComplete) && (players[1] is CFComputer))
                            {
                                int compColumn = ran.Next(1, 8);
                                gameOver = true;
                                await Task.Delay(1000);
                                gameOver = false;
                                Push(compColumn);
                            }
                            else if ((ResumeComplete) && (players[1] is CFGoodComputer))
                            {
                                int compColumn = players[1].GetColumn(connectfour);
                                gameOver = true;
                                await Task.Delay(1000);
                                gameOver = false;
                                Push(compColumn);
                            }
                            else if ((ResumeComplete) && (players[1] is CFPlayer) && (MyTurn))
                            {
                                gameOver = true;

                                //wait to do the other player's move
                            }
                            if (multiplayer && MyTurn)
                            {
                                network.Send(column.ToString());
                                textBox1.Text = "Waiting on opponent to Move!";
                                MyTurn = false;
                            }
                        }
                        
                    }
                    else
                    {
                        textBox2.Text = Currentcolor[nextColor % 2] + " invalid move";
                        if ((ResumeComplete) && (players[0] is CFComputer)&&(!gameOver))
                        {
                            int compColumn = ran.Next(1, 8);
                            gameOver = true;
                            await Task.Delay(1000);
                            gameOver = false;
                            Push(compColumn);
                        }
                    }

                }
                else
                {
                    Boolean valid = players[1].taketurn(connectfour, column, Currentcolor[(nextColor) % 2]);
                    Boolean winner = false;
                    if (valid)
                    {
                        playSimpleSound();
                        String[][] Board = connectfour.getBoard2;

                        changeColor(column);
                        
                        winner = connectfour.winCheck();
                        if (winner == true)
                        {
                            WinnerButtonsChange();
                            textBox2.Text = Currentcolor[(nextColor) % 2] + " wins!";
                            DialogResult saved;
                            if (multiplayer && MyTurn)
                            {
                                textBox1.Text = "You Win!";
                                network.Send(column.ToString());
                                network.CloseConnections();
                                connectionEst = false;
                                MyTurn = false;
                                gameOver = true;
                                network.legal = false;
                                saved = MessageBox.Show(Currentcolor[(nextColor) % 2] + "(You) wins!, Would you like to start a new game?", "Winner!!", MessageBoxButtons.YesNo, MessageBoxIcon.Information);

                            }
                            else if (multiplayer && !MyTurn)
                            {
                                connectionEst = false;
                                network.CloseConnections();
                                textBox1.Text = "Your Opponent Wins!";
                                saved = MessageBox.Show(Currentcolor[(nextColor) % 2] + "(Your Opponent) wins!, Would you like to start a new game?", "Darn it!!", MessageBoxButtons.YesNo, MessageBoxIcon.Information);

                            }
                            else
                            {
                                saved = MessageBox.Show(Currentcolor[(nextColor) % 2] + " wins!, Would you like to start a new game?", "Winner!!", MessageBoxButtons.YesNo, MessageBoxIcon.Information);
                            }
                            textBox2.Text = Currentcolor[(nextColor) % 2] + " wins!";
                            

                            gameOver = true;
                            //gameover = true, stops the button pushes from doing anything
                            if (saved == DialogResult.Yes)
                            {
                               
                                this.Close();

                                
                                Boolean rep = false;
                                CFBoard newGame;
                                await Task.Delay(1500);
                                
                                string startupPath = System.IO.Directory.GetCurrentDirectory();
                                string ad = @"bin\Debug";
                                string path = startupPath.Replace(ad, "");
                                //textBox1.Text = result+"gamelog.txt";
                                String GlobalFile = path + "gamelog.txt";
                                FileStream fk = new FileStream(GlobalFile, FileMode.Create);
                                fk.Close();
                                if (multiplayer)
                                {
                                    newGame = new CFBoard("Multiplayer", opponentIP);
                                }
                                else
                                {
                                    newGame = new CFBoard(rep);
                                }
                                DialogResult result = newGame.ShowDialog();
                            }
                        }
                        else
                        {

                            turn = turn + 1;
                            nextColor = nextColor + 1;
                            label2.Text = getTurn().ToString();
                            textBox2.Text = "It is " + Currentcolor[(nextColor) % 2] + "'s turn!";
                            
                            //computer takes a turn now
                            if ((ResumeComplete) && (players[0] is CFComputer))
                            {
                                int compColumn = ran.Next(1, 8);
                                gameOver = true;
                                await Task.Delay(1000);
                                gameOver = false;
                                Push(compColumn);
                            }
                            else if ((ResumeComplete) && (players[0] is CFGoodComputer))
                            {
                                int compColumn = players[1].GetColumn(connectfour);
                                gameOver = true;
                                await Task.Delay(1000);
                                gameOver = false;
                                Push(compColumn);
                            }
                            else if ((ResumeComplete) && (players[0] is CFPlayer)&&(MyTurn))
                            {
                                gameOver = true;
                                //wait to do the other player's move
                            }
                            if (multiplayer && MyTurn)
                            {
                                network.Send(column.ToString());
                                textBox1.Text = "Waiting on opponent to Move!";
                                MyTurn = false;
                            }
                        }
                        
                    }
                    else
                    {
                        textBox2.Text = Currentcolor[nextColor % 2] + " invalid move";
                        if ((ResumeComplete) && (players[1] is CFComputer)&&(!gameOver))
                        {
                            int compColumn = ran.Next(1, 8);
                            gameOver=true;
                            await Task.Delay(1000);
                            gameOver=false;

                            Push(compColumn);
                        }
                    }
                }
            }
            
        }
        
        
        private void CFBoard_Load(object sender, EventArgs e)
        {
            if (multiplayer)
            {
                Thread thread = new Thread(new ThreadStart(CreateNetwork));
                thread.Start();
            }
        }
        private void loadsettings()
        {
            string startupPath = System.IO.Directory.GetCurrentDirectory();
            string ad = @"bin\Debug";
            string result = startupPath.Replace(ad, "");
            String xmlfile = result + "settings.xml";
            XmlReaderSettings settings = new XmlReaderSettings();
            settings.IgnoreWhitespace = true;
            settings.DtdProcessing = DtdProcessing.Parse;

            settings.ValidationType = ValidationType.DTD;
            XmlReader reader = XmlReader.Create(xmlfile, settings);
            //read the colors from xml file
            while (reader.Read())
            {
                //only looking for elements...
                if (reader.NodeType.Equals(XmlNodeType.Element))
                {

                    if (reader.Name.CompareTo("player1color") == 0)
                    {
                        Currentcolor[0] = reader.ReadElementContentAsString();
                    }
                    if (reader.Name.CompareTo("player2color") == 0)
                    {
                        Currentcolor[1] = reader.ReadElementContentAsString();
                    }
                    
                }
            }
            reader.Close();
        }
        #region
        private void button1_Click(object sender, EventArgs e)
        {
            //sends column 1
            Push(1);
        }
        private void button1_MouseEnter(object sender, EventArgs e)
        {
            
        }

        private void goButton_MouseLeave(object sender, EventArgs e)
        {
            
        }
        private void button2_Click(object sender, EventArgs e)
        {
            //sends column 2
            Push(2);
        }

        private void button3_Click(object sender, EventArgs e)
        {
            //sends column 3
            Push(3);
        }

        private void button4_Click(object sender, EventArgs e)
        {
            //sends column 4
            Push(4);
        }

        private void button5_Click(object sender, EventArgs e)
        {
            //sends column 5
            Push(5);
        }

        private void button6_Click(object sender, EventArgs e)
        {
            //sends column 6
            Push(6);
        }

        private void button7_Click(object sender, EventArgs e)
        {
            //sends column 7
            Push(7);
        }

        private void button8_Click(object sender, EventArgs e)
        {

        }

        private void button00_Click(object sender, EventArgs e)
        {
            //sends column 1
            Push(1);
            
        }

        private void button10_Click(object sender, EventArgs e)
        {
            //sends column 1
            Push(1);
        }

        private void button20_Click(object sender, EventArgs e)
        {
            //sends column 1
            Push(1);
        }

        private void button35_Click(object sender, EventArgs e)
        {
            //sends column 1
            Push(6);
        }

        private void button43_Click(object sender, EventArgs e)
        {
            //sends column 1
            Push(4);
        }

        private void button50_Click(object sender, EventArgs e)
        {
            //sends column 1
            Push(1);
        }

        private void button01_Click(object sender, EventArgs e)
        {
            //sends column 2
            Push(2);
        }

        private void button11_Click(object sender, EventArgs e)
        {
            //sends column 2
            Push(2);
        }

        private void button21_Click(object sender, EventArgs e)
        {
            //sends column 2
            Push(2);
        }

        private void button36_Click(object sender, EventArgs e)
        {
            //sends column 2
            Push(7);
        }

        private void button42_Click(object sender, EventArgs e)
        {
            //sends column 2
            Push(3);
        }

        private void button51_Click(object sender, EventArgs e)
        {
            //sends column 2
            Push(2);
        }

        private void button02_Click(object sender, EventArgs e)
        {
            //sends column 3
            Push(3);
        }

        private void button12_Click(object sender, EventArgs e)
        {
            //sends column 3
            Push(3);
        }

        private void button22_Click(object sender, EventArgs e)
        {
            //sends column 3
            Push(3);
        }

        private void button34_Click(object sender, EventArgs e)
        {
            //sends column 3
            Push(5);
        }

        private void button41_Click(object sender, EventArgs e)
        {
            //sends column 3
            Push(2);
        }

        private void button52_Click(object sender, EventArgs e)
        {
            //sends column 3
            Push(3);
        }

        private void button03_Click(object sender, EventArgs e)
        {
            //sends column 4
            Push(4);
        }

        private void button13_Click(object sender, EventArgs e)
        {
            //sends column 4
            Push(4);
        }

        private void button23_Click(object sender, EventArgs e)
        {
            //sends column 4
            Push(4);
        }

        private void button33_Click(object sender, EventArgs e)
        {
            //sends column 4
            Push(4);
        }

        private void button40_Click(object sender, EventArgs e)
        {
            //sends column 4
            Push(1);
        }

        private void button53_Click(object sender, EventArgs e)
        {
            //sends column 4
            Push(4);
        }

        private void button04_Click(object sender, EventArgs e)
        {
            //sends column 5
            Push(5);
        }

        private void button14_Click(object sender, EventArgs e)
        {
            //sends column 5
            Push(5);
        }

        private void button24_Click(object sender, EventArgs e)
        {
            //sends column 5
            Push(5);
        }

        private void button32_Click(object sender, EventArgs e)
        {
            //sends column 5
            Push(3);
        }

        private void button44_Click(object sender, EventArgs e)
        {
            //sends column 5
            Push(5);
        }

        private void button54_Click(object sender, EventArgs e)
        {
            //sends column 5
            Push(5);
        }

        private void button05_Click(object sender, EventArgs e)
        {
            //sends column 6
            Push(6);
        }

        private void button15_Click(object sender, EventArgs e)
        {
            //sends column 6
            Push(6);
        }

        private void button25_Click(object sender, EventArgs e)
        {
            //sends column 6
            Push(6);
        }

        private void button31_Click(object sender, EventArgs e)
        {
            Push(2);
        }

        private void button45_Click(object sender, EventArgs e)
        {
            //sends column 6
            Push(6);
        }

        private void button55_Click(object sender, EventArgs e)
        {
            //sends column 6
            Push(6);
        }

        private void button06_Click(object sender, EventArgs e)
        {
            //sends column 7
            Push(7);
        }

        private void button16_Click(object sender, EventArgs e)
        {
            //sends column 7
            Push(7);
        }

        private void button26_Click(object sender, EventArgs e)
        {
            //sends column 7
            Push(7);
        }

        private void button30_Click(object sender, EventArgs e)
        {
            //sends column 7
            Push(1);
        }

        private void button46_Click(object sender, EventArgs e)
        {
            //sends column 7
            Push(7);
        }

        private void button56_Click(object sender, EventArgs e)
        {
            //sends column 7
            Push(7);
        }

        private void pictureBox1_Click(object sender, EventArgs e)
        {

        }

        private void label2_Click(object sender, EventArgs e)
        {

        }

        #endregion
        
        public void GameClose(WaitingForPlayer waiting)
        {
            if (multiplayer)
            {
                waitingAlreadyClosed = true;
                network.CloseConnections();
                waiting.Invoke((MethodInvoker)delegate
                {
                    waiting.Close();
                });
                
                this.Close();
            }
        }
        private void button17_Click(object sender, EventArgs e)
        {
            if (multiplayer)
            {
                network.CloseConnections();
                this.Invoke((MethodInvoker)delegate
                {
                    network.CloseWaiting();
                });
            }
            this.Close();

        }
        public Boolean connectionEst = false;
        private void CFBoard_FormClosing(object sender, FormClosingEventArgs e)
        {
            if (multiplayer && !waitingAlreadyClosed)
            {
                
                //MessageBox.Show("adf");
                network.CloseConnections();
                this.Invoke((MethodInvoker)delegate
                {
                    network.CloseWaiting();
                });
            }
            //if (connectionEst)
            //{
            //    Random rand = new Random();
            //    network.Send(rand.Next(int.MinValue, 0).ToString());
            //}
        }
        private void button8_Click_1(object sender, EventArgs e)
        {
            if ((replayIndex > 0) && (replayIndex <= repturns.Length))
            {
                playSimpleSound();
                replayIndex = replayIndex - 1;
                int replayrow = 0;
                String[][] board = connectfour.getBoard2;
                while ((board[replayrow][repturns[replayIndex] - 1] == ".") || (board[replayrow][repturns[replayIndex] - 1] == "."))
                {
                    replayrow = replayrow + 1;
                    //textBox2.Text = replayrow + "";
                    if (replayrow == 5)
                    {
                        break;
                    }
                }
                
                this.listofButtons[replayrow][repturns[replayIndex] - 1].BackgroundImage = Resources.white;
                board[replayrow][repturns[replayIndex] - 1] = ".";
                

                
                
                
                if (replayIndex >= 0)
                {
                    turn = turn - 1;
                    label2.Text = getTurn().ToString();
                    textBox2.Text = Currentcolor[(nextColor+1) % 2] + "'s turn is next";
                    nextColor = nextColor + 1;
                }
            }
        }
        public void replaychangeColor(int replayrow, int replayindex)
        {
            if (nextColor % 2 == 0)
            {
                if (Currentcolor[0] == "Red")
                {
                    this.listofButtons[replayrow][repturns[replayIndex] - 1].BackgroundImage = Resources.red1;
                }
                else if (Currentcolor[0] == "Yellow")
                {
                    this.listofButtons[replayrow][repturns[replayIndex] - 1].BackgroundImage = Resources.yellow1;
                }
                else
                {
                    this.listofButtons[replayrow][repturns[replayIndex] - 1].BackgroundImage = Resources.Blue;
                }
            }
            //change second player
            else
            {
                if (Currentcolor[1] == "Red")
                {
                    this.listofButtons[replayrow][repturns[replayIndex] - 1].BackgroundImage = Resources.red1;
                }
                else if (Currentcolor[1] == "Yellow")
                {
                    this.listofButtons[replayrow][repturns[replayIndex] - 1].BackgroundImage = Resources.yellow1;
                }
                else
                {
                    this.listofButtons[replayrow][repturns[replayIndex] - 1].BackgroundImage = Resources.Blue;
                }
            }
        }
        private void button9_Click(object sender, EventArgs e)
        {
            if ((replayIndex >= 0) && (replayIndex < repturns.Length))
            {
                playSimpleSound();
                int replayrow = 5;
                String[][] board = connectfour.getBoard2;
                while ((board[replayrow][repturns[replayIndex] - 1] == "Red") || (board[replayrow][repturns[replayIndex] - 1] == "Yellow") || (board[replayrow][repturns[replayIndex] - 1] == "Blue"))
                {
                    replayrow = replayrow - 1;
                    textBox2.Text = replayrow+"";
                }
                if (nextColor % 2 == 0)
                {
                    replaychangeColor(replayrow, replayIndex);
                    //this.listofButtons[replayrow][repturns[replayIndex]-1].BackgroundImage = Resources.red1;
                    board[replayrow][repturns[replayIndex] - 1] = Currentcolor[0];
                    turn = turn + 1;
                    label2.Text = (getTurn()).ToString();
                    textBox2.Text = Currentcolor[(nextColor+1) % 2] + "'s turn is next";

                }
                else
                {
                    replaychangeColor(replayrow, replayIndex);
                    //this.listofButtons[replayrow][repturns[replayIndex]-1].BackgroundImage = Resources.yellow1;
                    board[replayrow][repturns[replayIndex] - 1] = Currentcolor[1];
                    turn = turn + 1;
                    label2.Text = (getTurn()).ToString();
                    textBox2.Text = Currentcolor[(nextColor+1) % 2] + "'s turn is next";
                }
                nextColor = nextColor + 1;
                replayIndex = replayIndex + 1;

            }
        }

        private void label3_Click(object sender, EventArgs e)
        {

        }
        private void textBox1_TextChanged(object sender, EventArgs e)
        {

        }
    }
}
