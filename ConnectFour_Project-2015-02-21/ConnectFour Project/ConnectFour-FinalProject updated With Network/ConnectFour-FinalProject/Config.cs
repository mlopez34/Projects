using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Xml;
using System.Xml.Linq;

namespace ConnectFour_FinalProject
{
    public partial class Config : Form
    {
        private String xmlfile;
        String player1color;
        String player2color;
        String boardsize;

        public Config()
        {
            InitializeComponent();
            comboBox1.Items.Add("Red");
            comboBox1.Items.Add("Yellow");
            comboBox1.Items.Add("Blue");

            comboBox2.Items.Add("Red");
            comboBox2.Items.Add("Yellow");
            comboBox2.Items.Add("Blue");

            comboBox3.Items.Add("Small");
            comboBox3.Items.Add("Big");
            //the choices in these comboboxes will be stored inside an XML file, the game will read this XML file everytime it starts up
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            player1color = comboBox1.SelectedItem.ToString();
            if (player1color == "Red")
            {
                comboBox1.SelectedIndex = 0;
                pictureBox2.BackgroundImage = Properties.Resources.red1;
            }
            else if (player1color == "Yellow")
            {
                comboBox1.SelectedIndex = 1;
                pictureBox2.BackgroundImage = Properties.Resources.yellow1;
            }
            else
            {
                comboBox1.SelectedIndex = 2;
                pictureBox2.BackgroundImage = Properties.Resources.Blue;
            }
        }

        private void comboBox2_SelectedIndexChanged(object sender, EventArgs e)
        {
            player2color = comboBox2.SelectedItem.ToString();
            if (player2color == "Red")
            {
                comboBox2.SelectedIndex = 0;
                pictureBox3.BackgroundImage = Properties.Resources.red1;
            }
            else if (player2color == "Yellow")
            {
                comboBox2.SelectedIndex = 1;
                pictureBox3.BackgroundImage = Properties.Resources.yellow1;
            }
            else
            {
                comboBox2.SelectedIndex = 2;
                pictureBox3.BackgroundImage = Properties.Resources.Blue;
            }
        }

        private void comboBox3_SelectedIndexChanged(object sender, EventArgs e)
        {
            boardsize = comboBox3.SelectedItem.ToString();
            if (boardsize == "Small")
            {
                comboBox3.SelectedIndex = 0;
            }

            else
            {
                comboBox3.SelectedIndex = 1;
            }
        }

        private void button1_Click(object sender, EventArgs e)
        {
            if (player1color != player2color)
            {
                //writes to XML file the chosen config options
                DialogResult saved = MessageBox.Show("Your settings have been saved!", "Saved", MessageBoxButtons.OK, MessageBoxIcon.Information);
                
                XDocument xdoc = XDocument.Load(xmlfile);
                xdoc.Descendants("player1color").First().Value = player1color;
                xdoc.Descendants("player2color").First().Value = player2color;
                xdoc.Descendants("boardsize").First().Value = boardsize;
                xdoc.Save(xmlfile);
                this.Close();
            }
            else
            {
                DialogResult invalid = MessageBox.Show("Player 1 and Player 2 colors cannot be the same, please change one of them!", "Invalid selection!", MessageBoxButtons.OK, MessageBoxIcon.Information);
                
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {
            this.Close();
            //closes the form without saving options
        }

        private void Config_Load(object sender, EventArgs e)
        {
            //load xml file
            string startupPath = System.IO.Directory.GetCurrentDirectory();
            string ad = @"bin\Debug";
            string result = startupPath.Replace(ad, "");
            xmlfile = result + "settings.xml";
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
                        player1color = reader.ReadElementContentAsString();
                        //textBox1.Text = textBox1.Text + player1color;
                    }
                    if (reader.Name.CompareTo("player2color") == 0)
                    {
                        player2color = reader.ReadElementContentAsString();
                        //textBox1.Text = textBox1.Text + player2color;
                    }
                    if (reader.Name.CompareTo("boardsize") == 0)
                    {
                        boardsize = reader.ReadElementContentAsString();
                        //textBox1.Text = textBox1.Text + boardsize;
                    }
                }
            }
            //set the pictures according to colors and boardsize
            reader.Close();
            //player 1 color
            if (player1color == "Red")
            {
                comboBox1.SelectedIndex = 0;
                pictureBox2.BackgroundImage = Properties.Resources.red1;
            }
            else if (player1color == "Yellow")
            {
                comboBox1.SelectedIndex = 1;
                pictureBox2.BackgroundImage = Properties.Resources.yellow1;
            }
            else
            {
                comboBox1.SelectedIndex = 2;
                pictureBox2.BackgroundImage = Properties.Resources.Blue;
            }
            //player 2 color
            if (player2color == "Red")
            {
                comboBox2.SelectedIndex = 0;
                pictureBox3.BackgroundImage = Properties.Resources.red1;
            }
            else if (player2color == "Yellow")
            {
                comboBox2.SelectedIndex = 1;
                pictureBox3.BackgroundImage = Properties.Resources.yellow1;
            }
            else
            {
                comboBox2.SelectedIndex = 2;
                pictureBox3.BackgroundImage = Properties.Resources.Blue;
            }
            //board size
            if (player1color == "Small")
            {
                comboBox3.SelectedIndex = 0;
            }
            
            else
            {
                comboBox3.SelectedIndex = 1;
            }
        }

        private void pictureBox2_Click(object sender, EventArgs e)
        {

        }

        private void pictureBox3_Click(object sender, EventArgs e)
        {

        }

        private void label1_Click(object sender, EventArgs e)
        {

        }
    }
}
