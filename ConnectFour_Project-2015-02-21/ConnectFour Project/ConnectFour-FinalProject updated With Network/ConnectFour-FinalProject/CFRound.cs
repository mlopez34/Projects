using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using System.Xml;
using System.Xml.Linq;
namespace ConnectFour_FinalProject
{
    public class CFRound
    {
        private String[][] cflist = new String[6][];
        private String[] linearray = new String[7];
        private int turnNumber = 1;
        private int globalRow = 0;
        private String player1;
        private String player2;
        private String GlobalFile;
        private String xmlfile;
        int[][] ButtonsOfInterest;

        public int[][] buttonsOfInterest
        {
            get { return this.ButtonsOfInterest; }
        }
        public String setplayer1
        {
            set { player1 = value; }
        }
        public String setplayer2
        {
            set { player2 = value; }
        }
        public int getglobalRow
        {
            get {return globalRow;}
            set { globalRow = value; }
        }
        public CFRound()
        {
            //constructs the connect four board
            for (int j = 0; j < cflist.Length; j++)
            {
                for (int i = 0; i < linearray.Length; i++)
                {
                    linearray[i] = ".";
                }

                cflist[j] = new[] { linearray[0], linearray[1], linearray[2], linearray[3], linearray[4], linearray[5], linearray[6], };
                string startupPath = System.IO.Directory.GetCurrentDirectory();
                string ad = @"bin\Debug";
                string result = startupPath.Replace(ad, "");
                this.GlobalFile = result + "gamelog.txt";
                this.xmlfile = result + "settings.xml";
            }
        }
        public String[][] getBoard2
        {
            get { return cflist; }
            set { cflist = value; }
        }
        
        public Boolean validColumnCheck(int column)
        {
            if (column > 7 || column < 1)
            {
                return false;
            }
            else
            {
                if (cflist[0][column - 1] == player1 || cflist[0][column - 1] == player2)
                {
                    return false;

                }
                else
                {
                    return true;
                }
            }

        }
        public void dropCircle(int column, String color)
        {
            int row = 5;
            while (cflist[row][column - 1] != ".")
            {
                row = row - 1;
                if (row < 0)
                {
                    break;
                }
            }
            if (row >= 0)
            {
                cflist[row][column - 1] = color;
                String turn = "turn " + turnNumber + " " + color + " " + row + "," + (column) + "";
                String fn = GlobalFile;
                StreamWriter o = new StreamWriter(fn, true);
                o.WriteLine(turn);
                o.Close();
                turnNumber = turnNumber + 1;
                globalRow = row;
            }
            
        }
        public void saveOrder(string  playerOrder)
        {
            String turn = playerOrder;
            String fn = GlobalFile;
            StreamWriter o = new StreamWriter(fn, true);
            o.WriteLine(turn);
            o.Close();
        }
        public void replaySave(String file)
        {
            //read contents of the file, then write turns to xml turns
            //line of turns
            String order = "";
            String turns = "";
            FileStream fs = new FileStream(GlobalFile, FileMode.Open);
            StreamReader turnreader = new StreamReader(fs);
            String line = turnreader.ReadLine();
            char[] seperators = { '.', ' ', ',', ' ' };
            if (line == "1" || line == "2")
            {
                order = line;
                line = turnreader.ReadLine();
            }
            while (line != null)
            {
                //store words on a list
                String[] words = line.Split(seperators);
                if (words.Count() > 2)
                {
                    turns = turns + words[(words.Length) - 1]+ " ";
                }
                //read next line
                line = turnreader.ReadLine();
            }
            turnreader.Close();
            fs.Close();

            XDocument xdoc = XDocument.Load(xmlfile);
            xdoc.Descendants("order").First().Value = order; //string containing order
            xdoc.Descendants("turns").First().Value = turns; //string of integers separated by spaces;
            xdoc.Save(xmlfile);
            
        }
        public Boolean winCheck()
        {
            Boolean colorWin = false;
            //check horizontal win
            for (int i = 0; i < cflist.Length; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    if (cflist[i][j] != ".")
                    {
                        //Whichever color is in the current slot
                        String color = cflist[i][j];
                        if (cflist[i][j + 1] == color && cflist[i][j + 2] == color && cflist[i][j + 3] == color)
                        {
                            //buttons of interest are the buttons which will be changed to winning color pictures
                            ButtonsOfInterest = new int[4][];
                            ButtonsOfInterest[0] = new int[2] { i, j  };
                            ButtonsOfInterest[1] = new int[2] { i, j + 1 };
                            ButtonsOfInterest[2] = new int[2] { i, j + 2 };
                            ButtonsOfInterest[3] = new int[2] { i, j + 3 };
                            //returns for horizontal winner
                            colorWin = true;
                            String wins = color + " Wins!";
                            String fn = GlobalFile;
                            StreamWriter o = new StreamWriter(fn, true);
                            o.WriteLine(wins);
                            o.Close();
                            //write game into XML file turns
                            replaySave(GlobalFile);
                            return colorWin;
                        }
                    }
                }
            }
            //check vertical win
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 7; j++)
                {
                    if (cflist[i][j] != ".")
                    {

                        //whichever color is in the current slot
                        String color = cflist[i][j];
                        if (cflist[i + 1][j] == color && cflist[i + 2][j] == color && cflist[i + 3][j] == color)
                        {
                            ButtonsOfInterest = new int[4][];
                            ButtonsOfInterest[0] = new int[2] { i, j };
                            ButtonsOfInterest[1] = new int[2] { i+1, j };
                            ButtonsOfInterest[2] = new int[2] { i+2, j };
                            ButtonsOfInterest[3] = new int[2] { i+3, j };
                            //returns for vertical winner
                            colorWin = true;
                            String wins = color + " Wins!";
                            String fn = GlobalFile;
                            StreamWriter o = new StreamWriter(fn, true);
                            o.WriteLine(wins);
                            o.Close();
                            //write game into XML file turns
                            
                            replaySave(GlobalFile);
                            return colorWin;
                        }

                    }
                }
            }
            //check diagonally right win
            for (int i = 0; i < 3; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    if (cflist[i][j] != ".")
                    {
                        //whichever color is in the current slot
                        String color = cflist[i][j];
                        if (cflist[i + 1][j + 1] == color && cflist[i + 2][j + 2] == color && cflist[i + 3][j + 3] == color)
                        {
                            ButtonsOfInterest = new int[4][];
                            ButtonsOfInterest[0] = new int[2] { i, j };
                            ButtonsOfInterest[1] = new int[2] { i+1, j + 1 };
                            ButtonsOfInterest[2] = new int[2] { i+2, j + 2 };
                            ButtonsOfInterest[3] = new int[2] { i+3, j + 3 };
                            //returns diagonally right winner
                            colorWin = true;
                            String wins = color + " Wins!";
                            String fn = GlobalFile;
                            StreamWriter o = new StreamWriter(fn, true);
                            o.WriteLine(wins);
                            o.Close();
                            //write game into xml file turns
                            
                            replaySave(GlobalFile);
                            return colorWin;
                        }
                    }
                }
            }
            //check for diagonally left win
            for (int i = 0; i < 3; i++)
            {
                for (int j = 3; j < 7; j++)
                {
                    if (cflist[i][j] != ".")
                    {
                        String color = cflist[i][j];
                        if (cflist[i + 1][j - 1] == color && cflist[i + 2][j - 2] == color && cflist[i + 3][j - 3] == color)
                        {
                            ButtonsOfInterest = new int[4][];
                            ButtonsOfInterest[0] = new int[2] { i, j };
                            ButtonsOfInterest[1] = new int[2] { i+1, j - 1 };
                            ButtonsOfInterest[2] = new int[2] { i+2, j - 2 };
                            ButtonsOfInterest[3] = new int[2] { i+3, j - 3 };
                            //returns diagonally left winner
                            colorWin = true;
                            String wins = color + " Wins!";
                            String fn = GlobalFile;
                            StreamWriter o = new StreamWriter(fn, true);
                            o.WriteLine(wins);
                            o.Close();
                            
                            replaySave(GlobalFile);
                            //write game into xml file turns
                            return colorWin;
                        }
                    }
                }
            }
            return colorWin;
        }

        
    }
}
