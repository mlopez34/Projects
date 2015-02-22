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
using System.Xml;
using System.Xml.Linq;
using System.Threading;
using System.Net;
using System.Net.Sockets;
using System.Collections;

namespace ConnectFour_FinalProject
{
    public partial class Main : Form
    {
        private String GlobalFile;
        private String xmlfile;
        public Main()
        {
            InitializeComponent();
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.Main_FormClosing);
            //load options from XML file and show circles on the right showing the chosen colors
            //turns is written into the xml file only after a game is complete, resume will still read from txt file
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            string startupPath = System.IO.Directory.GetCurrentDirectory();
            string ad = @"bin\Debug";
            string result = startupPath.Replace(ad,"");
            GlobalFile = result + "gamelog.txt";
            xmlfile = result + "settings.xml";
            //now load the xml contents, 
            XmlReaderSettings settings = new XmlReaderSettings();
            settings.IgnoreWhitespace = true;
            settings.DtdProcessing = DtdProcessing.Parse;

            settings.ValidationType = ValidationType.DTD;
            // get your own IP
            IPHostEntry IPHost = Dns.GetHostByName(Dns.GetHostName());
            textBox1.Text = IPHost.AddressList[0].ToString();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            //new game
            //open game form, starts new game.
            //if there is a log in the gamelog file it will check to see if it is valid, if it is then it will prompt a messagebox 
            //saying last game will be overwritten

            string file = GlobalFile;
            FileStream fs = new FileStream(file, FileMode.OpenOrCreate);
            StreamReader turnreader = new StreamReader(fs);
            String line = turnreader.ReadLine();
            char[] seperators = { '.', ' ', ',' };
            Boolean doneGame = false;
            Boolean gamehasmoves = false;
            while (line != null)
            {
                gamehasmoves = true;
                //store words on a list
                String[] words = line.Split(seperators);
                if (words.Contains("Wins!"))
                {
                    doneGame = true;
                }
                //read next line
                line = turnreader.ReadLine();
            }
            fs.Close();
            turnreader.Close();
            if (File.Exists(file) && (!doneGame)&&(gamehasmoves))
            {
                DialogResult overwrite = MessageBox.Show("Starting a New Game will overwrite the previous game, are you sure?", "New Game", MessageBoxButtons.OKCancel, MessageBoxIcon.Information);
                if (overwrite == DialogResult.OK)
                {
                    Boolean rep = false;
                    
                    String fn = GlobalFile;
                    FileStream fk = new FileStream(fn, FileMode.Create);
                    fk.Close();
                    CFBoard newGame = new CFBoard(rep);
                    DialogResult result = newGame.ShowDialog();
                }

            }
            else
            {
                Boolean rep = false;
                
                String fn = GlobalFile;
                FileStream fk = new FileStream(fn, FileMode.Create);
                fk.Close();
                CFBoard newGame = new CFBoard(rep);
                DialogResult result = newGame.ShowDialog();
            }

        }

        private void button3_Click(object sender, EventArgs e)
        {
            //resume game
            //open game form but resumes the game from gamelog file log
            //all moves in the log are played instantly and gets back to the current move
            //puts the column into a int[] array and makes all the moves at once.
            List<int> turns = new List<int>();
            int order = 0;
            String file = GlobalFile;
            FileStream fs = new FileStream(file, FileMode.Open);
            StreamReader turnreader = new StreamReader(fs);
            String line = turnreader.ReadLine();
            char[] seperators = { '.', ' ', ','};

            Boolean doneGame = false;
            while (line != null)
            {
                //store words on a list
                String[] words = line.Split(seperators);
                if (line== "1" || line == "2")
                {
                    order = Convert.ToInt32(line);
                }
                if (words.Count() > 2)
                {
                    turns.Add(Convert.ToInt32(words[(words.Length)-1]));
                    
                }
                if (words.Contains("Wins!"))
                {
                    doneGame = true;
                }
                //read next line
                line = turnreader.ReadLine();
            }
            turnreader.Close();
            fs.Close();
            FileStream fl = new FileStream(file, FileMode.Create);
            fl.Close();
            Boolean rep = false;
            if (!doneGame)
            {
                CFBoard resumeGame = new CFBoard(turns.ToArray(), rep, order);
                DialogResult result = resumeGame.ShowDialog();
            }
            else
            {
                CFBoard resumeGame = new CFBoard(rep);
                DialogResult result = resumeGame.ShowDialog();
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            //config
            //open config form, stores configurations inside XML file
            Config options = new Config();
            DialogResult result = options.ShowDialog();
        }

        private void button4_Click(object sender, EventArgs e)
        {
            //replay
            //this will hold the columns inside the turns array, loaded from XML turns
            List<int> turns = new List<int>();
            int order = 0;
            char[] seperators = { '.', ' ', ',' };
            XmlReaderSettings settings = new XmlReaderSettings();
            settings.IgnoreWhitespace = true;
            settings.DtdProcessing = DtdProcessing.Parse;

            settings.ValidationType = ValidationType.DTD;
            XmlReader reader = XmlReader.Create(xmlfile, settings);
            //read the colors from xml file
            String turnline = "";
            while (reader.Read())
            {
                //only looking for elements...
                if (reader.NodeType.Equals(XmlNodeType.Element))
                {
                    if ((reader.Name.CompareTo("order") == 0))
                    {
                        order = reader.ReadElementContentAsInt();
                    }
                    if ((reader.Name.CompareTo("turns") == 0))
                    {
                        turnline = reader.ReadElementContentAsString();
                    }
                    
                }
            }
            reader.Close();
            String[] t = turnline.Split(seperators);
            for (int i = 0; i < t.Length; i++)
            {
                if (t[i] !="")
                {
                    turns.Add(Convert.ToInt32(t[i]));
                }
            }
            //
            if (turns.Count > 0)
            {
                Boolean rep = true;
                CFBoard replay = new CFBoard(turns.ToArray(), rep, order);
                DialogResult result = replay.ShowDialog();
            }
            else
            {
                DialogResult norep = MessageBox.Show("There is no saved game!", "No replay available", MessageBoxButtons.OK, MessageBoxIcon.Information);
            }
        }

        private void button5_Click(object sender, EventArgs e)
        {
            //multiplayer game
            string file = GlobalFile;
            FileStream fs = new FileStream(file, FileMode.OpenOrCreate);
            StreamReader turnreader = new StreamReader(fs);
            String line = turnreader.ReadLine();
            char[] seperators = { '.', ' ', ',' };
            Boolean doneGame = false;
            Boolean gamehasmoves = false;
            while (line != null)
            {
                gamehasmoves = true;
                //store words on a list
                String[] words = line.Split(seperators);
                if (words.Contains("Wins!"))
                {
                    doneGame = true;
                }
                //read next line
                line = turnreader.ReadLine();
            }
            fs.Close();
            turnreader.Close();
            if (File.Exists(file) && (!doneGame) && (gamehasmoves))
            {
                DialogResult overwrite = MessageBox.Show("Starting a New Game will overwrite the previous game, are you sure?", "New Game", MessageBoxButtons.OKCancel, MessageBoxIcon.Information);
                if (overwrite == DialogResult.OK)
                {
                    
                    string multiplayer = "Multiplayer";
                    CFBoard newGame = new CFBoard(multiplayer, textBox2.Text);
                    String fn = GlobalFile;
                    FileStream fk = new FileStream(fn, FileMode.Create);
                    fk.Close();
                    DialogResult result = newGame.ShowDialog();
                }
            }
            else
            {
                string multiplayer = "Multiplayer";
                CFBoard newGame = new CFBoard(multiplayer, textBox2.Text);
                String fn = GlobalFile;
                FileStream fk = new FileStream(fn, FileMode.Create);
                fk.Close();
                DialogResult result = newGame.ShowDialog();
            }
        }
        private void Main_FormClosing(object sender, FormClosingEventArgs e)
        {
            Application.Exit();
        }

    }
}
