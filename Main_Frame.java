import javax.swing.JFrame;				import java.awt.Color;
import javax.swing.JLabel;				import java.awt.Font;
import javax.swing.JTextField;			import java.awt.event.ActionListener;
import javax.swing.JPanel;  			import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.sql.DriverManager;  		import java.sql.Connection;
import java.sql.Statement;      		import java.sql.SQLException;
import java.sql.ResultSet;      		import java.sql.ResultSetMetaData;
import javax.swing.JFrame;				import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.event.KeyListener;		import java.awt.event.KeyEvent;
import java.awt.Toolkit;				import java.awt.Dimension;
class Main_Frame
{
	public static void main(String args[])
	{
	  JFrame JF = new JFrame("Enter Details");
	  JF.setBounds(100,100,500,500);
	  JF.setLayout(null);
	  JF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	  JF.setVisible(true);

	  JPanel JP = new JPanel();
	  JP.setSize(JF.getSize());
	  JP.setLayout(null);
	  JP.setBackground(Color.black);
	  JF.add(JP);

	  JLabel JL1 = new JLabel("Name :");
	  JL1.setBounds(100,100,65,20);
	  JL1.setForeground(Color.white);
	  JL1.setFont(new Font("Arial",Font.BOLD,19));
	  JP.add(JL1);

	  JTextField JT1 = new JTextField();
	  JT1.setBounds(JL1.getX()+JL1.getWidth()+5,JL1.getY(),250,25);
	  JT1.setForeground(Color.red);
	  JT1.setFont(new Font("Arial",Font.BOLD,19));
	  JP.add(JT1);

	  JButton JB1 = new JButton("Proceed");
	  JB1.setBounds(255,135,100,20);
	  JB1.setForeground(Color.black);
	  JB1.setFont(new Font("Arial",Font.BOLD,15));
	  JP.add(JB1);

	  JB1.addActionListener(new ActionListener()
	  {
	  	public void actionPerformed(ActionEvent e)
	  	{
	  		if(JT1.getText().length()!=0)
	  		{
	  			String P[] = new String[4] ;
	  			P[0] = JT1.getText();
	  			Enter_Game E_G = new Enter_Game();
	  			E_G.EG(JF,P);
	  		}
	  	}
	  });
	  JP.setVisible(false);
	  JP.setVisible(true);
	}
}

class Enter_Game
{
	public void EG(JFrame JF, String Q[])
	{
		Snake_Game S_G = new Snake_Game();
		JF.removeAll();
		JF.setVisible(false);
		S_G.main(Q);
		Thread Y = new Thread()
		{
			public void run()
			{
				String m = S_G.z ;
				while(true)
				{
					m = S_G.z ;
					if(m.compareTo("WIN")==0)  {  break ; }
					else if(m.compareTo("LOOSE")==0) { break; }
					else { continue ; }
				}
				Int_to_string I_s = new Int_to_string();
				Q[1] = m ;
				int Q2[] = new int[3] ;
				Q2[0] = S_G.n ;
				Q2[1] = S_G.Min ;
				Q2[2] = S_G.Sec ;
				Q[3] = I_s.Int_to_string(S_G.Min)+" min and "+I_s.Int_to_string(--S_G.Sec)+" secs" ;
				if(m.compareTo("WIN")==0 || m.compareTo("LOOSE")==0)
				{
					UpdateDataBase UDB = new UpdateDataBase();
					UDB.udb(Q,Q2);
				}
			}
		}; Y.start();
	}
}
class UpdateDataBase // Database = gamerslist | table = T1 
{
	
	String S1 = "gamerslist" ;
    String S2 = "SrNo" ;
    String S3 = "Name" ;
    String S4 = "T&C" ;
    String S5 = "t1" ;
    static UpdateDataBase UDB ;
	public void udb(String A[], int B[])
	{
	  try
	  {
	    Class.forName("com.mysql.cj.jdbc.Driver");
	    Connection Conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","Shubham@1999");
	    Statement stmt = Conn.createStatement();
	    ResultSet Rs = stmt.executeQuery("show databases ;");
		System.out.println(Rs);
	    boolean D = false ;
	    UDB = new UpdateDataBase();
	    while(Rs.next())
	    {
	    	if(Rs.getString(1).compareTo(UDB.S1)==0)
	    	{
	    		D = true ;
				break ;
	    	}
	    }
    	if(D)
    	{
    		stmt.executeUpdate("use "+UDB.S1+" ;");
    		Rs = stmt.executeQuery("show tables ;");
    		D = false ;
    		while(Rs.next())
    		{
    			if(Rs.getString(1).compareTo(UDB.S5)==0)
    			{
    				D = true ; break ;
    			}
    		}
    		if(D)
    		{
    			UDB.TableUpdate(Rs,stmt,UDB.S1,A,B);
    		}
    		else
    		{
    			UDB.TableCreate(Rs,stmt,UDB.S1);
    			UDB.TableUpdate(Rs,stmt,UDB.S1,A,B);
    		}
    	}
    	else
    	{
    		stmt.executeUpdate("create database "+UDB.S1+" ;");
    		UDB.TableCreate(Rs,stmt,UDB.S1);
    		UDB.TableUpdate(Rs,stmt,UDB.S1,A,B);
    	}
	  }
	  catch(SQLException e) { System.out.println(e); }
	  catch(ClassNotFoundException ee) {}
	}

	public void TableCreate(ResultSet Rs, Statement s, String S)
	{
		try
		{
			s.executeUpdate("use "+S+" ;");
			String SS = "create table "+UDB.S5+" (SrNo int(100),Name varchar(500),Score int(100),Result varchar(100),Time varchar(150),Min int(100),Sec int(100));" ;
			s.executeUpdate(SS);
		}
		catch(SQLException e) { System.out.println(e); }
	}

	public void TableUpdate(ResultSet Rs, Statement s, String S, String L[], int L2[])
	{
		try
		{
			s.executeUpdate("use "+S+" ;");
			Rs = s.executeQuery("select * from "+UDB.S5+" ;");
			int SrNo = 0 ;
			while(Rs.next())
			{
				SrNo = Rs.getInt(1);
			}
			SrNo++ ;
			String SS = "insert into "+UDB.S5+"(SrNo,Name,Score,Result,Time,Min,Sec) value ("+SrNo+",'"+L[0]+"',"+L2[0]+",'"+L[1]+"'"+",'"+L[3]+"',"+L2[1]+","+--L2[2]+");" ;
			s.executeUpdate(SS);
		}
		catch(SQLException e) { System.out.println(e); }
	}
}
class Snake_Game extends Canvas implements KeyListener
{
	static Snake_Game S_G ;					static int W, H, SIZE_w, SIZE_h;
	static JLabel L1,L2,L3 ;				static boolean FLAG,q;
	static Thread ride,Random ;				static JPanel JP ;
	static JFrame F0 ;						static int extra_w, extra_h, width, height ;
	static int ARR[],M[],N[],D1[],D2[];		static String z ="null" ;
	static int n = 0 ;
	static Int_to_string I_s = new Int_to_string();
	static int Sec = 0, Min = 0 ;
	public Snake_Game()
	{
	  setBounds(extra_w,extra_h,width,height);
	  setBackground(Color.cyan);
	  addKeyListener(this);
	}

	public void put(Graphics g)
	{
		Random = new Thread()
		{
			public void run()
			{
			    
				int i = SIZE_w/2, j = SIZE_h/2 ;
				try
				{
					this.sleep(50);
					while(ARR[3]<SIZE_h*SIZE_w && n<50)
					{
						while(true && n<50)
						{
							if(!S_G.presenT(D1[i],D2[j]))
							{
								g.setColor(Color.red);
								g.fillOval(D1[i],D2[j],W,H);
								while(true && n<50)
								{
									if(ARR[1]==D1[i] && ARR[2]==D2[j])
									{
										L3.setText(I_s.Int_to_string(++n)+" /50");
										g.setColor(Color.cyan);
										g.fillOval(D1[i],D2[j],W,H);
										ARR[3]++ ;
										if(i<=SIZE_w/2 && j<=SIZE_h/2)  { j+=2 ; i = i + 5 ; }
										else if(i<=SIZE_w/2 && j>SIZE_h/2)  { j = j - 2 ; i = i + 4 ; }
										else if(i>SIZE_w/2 && j<=SIZE_h/2)  { j+=3 ; i = i + 3 ; }
										else if(i>SIZE_w/2 && j>SIZE_h/2)  { j = j - 4 ; i = i - 5 ; }
										else if(i>SIZE_w/2)  { j+=2 ; i = i + 3 ; }
										if(i>=SIZE_w || i<0)  { i = 0 ; }
										if(j>=SIZE_h || j<0)  { j = 0 ; }
										break ;
									}
								}
								break ;
							}
							else
							{
								i++ ;
								if(i>=SIZE_w)  { i = 0 ; }
							}
						}
						j++ ;
						if(j>=SIZE_h)  { j = 0 ; }
					}
					L2.setVisible(true);
					L2.setText("YOU WIN !");
					z = "WIN" ;
					TimeRemaning.interrupt();
					ride.interrupt();
					this.interrupt();
				}
				catch(InterruptedException e) {}
			}
		}; Random.start() ;
	}

	public boolean presenT(int X, int Y)
	{
		int H = 0 ;
		while(H<ARR[3])
		{
			if(M[H] ==X && N[H]==Y)
			{
				return true ;
			}
			H++ ;
		}
		return false ;
	}
 
	static Thread TimeRemaning = new Thread()
	{
		public void run()
		{
			try
			{
				JLabel LLL = new JLabel("Time :");
				LLL.setFont(new Font("Consolas",Font.BOLD,20));
				LLL.setBounds(545,5,250,25);
				LLL.setForeground(Color.yellow);
				JP.add(LLL);
				LLL.setVisible(false);
				LLL.setVisible(true);				
				while(z.compareTo("WIN")!=0 || z.compareTo("LOOSE")!=0)	
				{
					Sec = 0 ;
					while(Sec<60)
					{
						LLL.setText("Time |- "+" 0"+I_s.Int_to_string(Min)+":"+I_s.Int_to_string(Sec++));
						this.sleep(1000);
					}
					Min++ ;
				}
				ride.interrupt();
				L2.setVisible(true);
				Random.interrupt();
				this.interrupt();
			}
			catch(InterruptedException e) {}
		}
	};

	public void RIDE(Graphics j1, Graphics j2)
	{
	  ride = new Thread()
		{
			public void run()
			{
				try
				{
				   int x = ARR[1], y = ARR[2], i = 0, j ;
				   int LEFT = 0, RIGHT = getWidth()-1-W, TOP = 0, DOWN = getHeight()-1-H ;
				   M = new int[SIZE_w*SIZE_h] ;
				   N = new int[SIZE_w*SIZE_h] ;
				   int x1 = x, y1 = y ;
				   while(i<ARR[3])
				   {
				   	 j1.setColor(Color.black);
					 j1.drawRect(ARR[1],ARR[2],W,H);
					 M[i] = ARR[1] ; N[i] = ARR[2] ;
					 x = ARR[1] ; y = ARR[2] ;
					 if(ARR[4] == 6)
						{
							x = x + W + 1 ;
					    }

					   else if(ARR[4] == 4)
						{
							x = x - W - 1 ;
					    }

					   else if(ARR[4] == 8)
						{
							y = y - H - 1 ;
					    }

					   else if(ARR[4] == 2)
						{
							y = y + H + 1 ;
					    }
					    ARR[1] = x;  ARR[2] = y;
						    if(ARR[1] < LEFT)
							{
								ARR[1] = RIGHT ;
							}
							else if(ARR[1] > RIGHT)
							{
								ARR[1] = LEFT ;
							}
							else if(ARR[2] < TOP)
							{
								ARR[2] = DOWN ;
							}
							else if(ARR[2] > DOWN)
							{
								ARR[2] = TOP ;
							}
					    j = 3 ;
						while(j<ARR[3])
						{
							j++ ;
							if(ARR[1]==M[ARR[3]-j] && ARR[2]==N[ARR[3]-j])
							{
								ride.interrupt();
								L2.setVisible(true);
								z = "LOOSE" ;
								TimeRemaning.interrupt();
								Random.interrupt();
								this.interrupt();
							}
						}
					 i++ ;
					 this.sleep(50);
				   }
				    i = 0 ;
					while(true)
					{
						if(FLAG)
						{
							j1.setColor(Color.black);
							j1.drawRect(ARR[1],ARR[2],W,H);
							x = ARR[1] ; y = ARR[2] ;
						}
						j2.setColor(Color.cyan);
						j2.drawRect(x1,y1,W,H);
						i = 0 ;
						for(i = 0; i<ARR[3]-1; i++)
						{
							M[i] = M[i+1] ; N[i] = N[i+1] ;
						}
						x1 = M[0] ; y1 = N[0] ;
						M[ARR[3]-1] = x ; N[ARR[3]-1] = y ;
						if(ARR[4] == 6 && FLAG)
						{
							x = x + W + 1 ;
					    }

					   else if(ARR[4] == 4 && FLAG)
						{
							x = x - W - 1 ;
					    }

					   else if(ARR[4] == 8 && FLAG)
						{
							y = y - H - 1 ;
					    }

					   else if(ARR[4] == 2 && FLAG)
						{
							y = y + H + 1 ;
					    }
					    ARR[1] = x;  ARR[2] = y;
						    if(ARR[1] < LEFT)
							{
								ARR[1] = RIGHT ;
							}
							else if(ARR[1] > RIGHT)
							{
								ARR[1] = LEFT ;
							}
							else if(ARR[2] < TOP)
							{
								ARR[2] = DOWN ;
							}
							else if(ARR[2] > DOWN)
							{
								ARR[2] = TOP ;
							}
					    j = 3 ;
					    while(j<ARR[3])
						{
							j++ ;
							if(ARR[1]==M[ARR[3]-j] && ARR[2]==N[ARR[3]-j])
							{
								ride.interrupt();
								L2.setVisible(true);
								z = "LOOSE" ;
								TimeRemaning.interrupt();
								Random.interrupt();
								this.interrupt();
							}
						}
					    this.sleep(50);
					}
				}
				catch(InterruptedException e0) {}
			} // public void run() close
		}; // Thread ride close
		ride.start();	
	}

	public void keyPressed(KeyEvent e)
	{
		// System.out.println(ARR[1]);
		if(e.getKeyChar() == '?' && q)
		{			
			q = false ;
			FLAG = true ;
			L2.setVisible(false);
			L3.setVisible(true);
			L3.setText("0/50");
			L1.setText("SCORE : ");	L1.setBounds(F0.getWidth()/4,5,90,25);
			L3.setBounds(L1.getX()+L1.getWidth(),L1.getY(),130,25);
			ARR[1] = 0 ; ARR[2] = D2[SIZE_h/2] ; ARR[3] = 12 ; ARR[4] = 6 ;
			TimeRemaning.start();
			Graphics G = getGraphics();
			S_G.RIDE(G,G);
			S_G.put(G);
		} // if(e.getKeyChar() == '?') close

		if(ARR[4] == 6 || ARR[4] == 4)
		{
			if(e.getKeyCode()==38)
		    {
			    ARR[4] = 8 ;
			    try { Thread.sleep(55);}
			    catch(InterruptedException e1) {}
			}
		  	if(e.getKeyCode()==40)
		  	{
			  	ARR[4] = 2 ;
			    try { Thread.sleep(55);}
			    catch(InterruptedException e1) {}
		  	}
		}
		else if(ARR[4] == 8 || ARR[4] == 2)
		{
			if(e.getKeyCode()==37)
			{
			  	ARR[4] = 4 ;
			    try { Thread.sleep(55);}
			    catch(InterruptedException e1) {}
		    }
			if(e.getKeyCode()==39)
			{
			  	ARR[4] = 6 ;
			    try { Thread.sleep(55);}
			    catch(InterruptedException e1) {}
		    }
		}
	// System.out.println(e.getKeyCode());
	} // KeyEvent e close
	public void keyReleased(KeyEvent e)  {}
	public void keyTyped(KeyEvent e) {}

	public static void main(String args[])
	{	  
	  F0 = new JFrame("Snake_Game");
	  F0.setLayout(null);
	  F0.setVisible(true);
	  F0.setResizable(false);
	  F0.setTitle("Designed & Created By : Rutuparn D");
	  F0.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	  JP = new JPanel();
	  JP.setLayout(null);
	  JP.setBackground(Color.black);
	  F0.add(JP);
	  L1 = new JLabel("Press '?' On the CANVAS To START");
	  L1.setFont(new Font("Arial",Font.BOLD,20));
	  L1.setForeground(Color.cyan);
	  L2 = new JLabel("GAME OVER");
	  L2.setBounds(365,5,120,25);
	  L2.setFont(new Font("Lucida Console",Font.BOLD,20));
	  L2.setForeground(Color.red);
	  L2.setVisible(false);
	  JP.add(L2);
	  L3 = new JLabel("0");
	  L3.setFont(new Font("Arial",Font.BOLD,20));
	  L3.setForeground(Color.green);
	  L3.setVisible(false);
	  JP.add(L3);
	  JLabel Name = new JLabel("Gamer : "+args[0]);
	  Name.setBounds(5,5,175,20);
	  Name.setFont(new Font("Arial",Font.BOLD,10));
	  Name.setForeground(Color.white);
	  JP.add(Name);
	  SIZE_w = 84-84/4 ; SIZE_h = 54-54/4 ;

	  ARR = new int[5] ;
	  q = true ;
	  W = 10; H = 10 ;
	  int n = 0 ;
		D1 = new int[SIZE_w] ;
		D2 = new int[SIZE_h] ;
		int x = 0, y = 0 ;
		int i = 0, j = 0 ;
		while(i<SIZE_w)
		{
			D1[n] = x ;
			x = x + W + 1 ;
			n++ ;
			i++ ;
		}
		n = 0 ; 
		while(j<SIZE_h)
		{
		    D2[n] = y ;
			y = y + H + 1 ;
			n++ ;
			j++ ;	
		}
		width = x ; height = y ;
		extra_w = 30 ; extra_h = 30 ;
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		int WIDTH = (int)size.getWidth(), HEIGHT = (int)size.getHeight() ;
		F0.setSize(x+2*extra_w+extra_w/2,y+2*extra_h+extra_h/2);
		F0.setBounds(WIDTH/2-F0.getWidth()/2,HEIGHT/2-F0.getHeight()/2,F0.getWidth(),F0.getHeight());
		JP.setSize(F0.getSize());	
	  L1.setBounds(0,5,408,25);
	  L1.setBounds(F0.getWidth()/2-L1.getWidth()/2,L1.getY(),L1.getWidth(),L1.getHeight());
	  S_G = new Snake_Game();
	  JP.add(L1);
	  JP.add(new Snake_Game());
	}
}

class Int_to_string
{
	public String Int_to_string(int a)
	{
		Int_to_string Rc = new Int_to_string() ;
		if(a==0)  { return "0"; }
        else
        {
			String s = "" ;
			while(a>0)
			{
	            int n = a%10 ;
	            s = s + Rc.check(n);
	            a = a / 10 ;
			}
			s = Rc.reverse(s);
			return s ;
		}	
	}

	public String reverse(String s)
	{
		int a = s.length()-1;
		String b = "" ;
		while(a>=0)
		{
			b = b + s.charAt(a);
			a-- ;
		}
		return b ;
	}

	public char check(int a)
	{
		if(1 == a)  { return '1'; }			else if(2 == a)  { return '2' ;}
		else if(3 == a)  { return '3' ;}	else if(4 == a)  { return '4' ;}
		else if(5 == a)  { return '5' ;}	else if(6 == a)  { return '6' ;}
		else if(7 == a)  { return '7' ;}	else if(8 == a)  { return '8' ;}
		else if(9 == a)  { return '9' ;}    else  {  return '0' ; }
	}
}