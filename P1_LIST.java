import javax.swing.JFrame;		import javax.swing.JLabel;	import javax.swing.JTextField;	import javax.swing.JButton;
import java.awt.Color;			import java.awt.Font;
import java.sql.DriverManager;	import java.sql.Connection; import java.sql.Statement;  import java.sql.ResultSet;
import java.sql.SQLException;	
import java.awt.event.ActionListener;	import java.awt.event.ActionEvent;
import java.io.FileWriter;				import java.io.BufferedWriter;		import java.io.File;
import java.io.IOException;
class P1_LIST
{
	public static void main(String args[])
	{
	   int i = 0,j = 0 ;
	   JFrame JF = new JFrame();
	   JF.setVisible(true);
	   JF.setBounds(100,100,500,500);
	   JF.setLayout(null);
	   JF.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   JLabel JL[] = new JLabel[4] ;
	   JTextField JT[] = new JTextField[2];
	   Font F = new Font("Consolas",Font.BOLD,15);
	   
	   JT[i] = new JTextField();	JT[i+1] = new JTextField();		JT[i].setBounds(320,100,130,25);	JT[i+1].setBounds(320,130,130,25);
	   JL[i++] = new JLabel() ;   JL[i++] = new JLabel() ;  JL[i++] = new JLabel() ;	JL[i] = new JLabel() ;
	   i = 0 ;
	   JL[i].setBounds(50,100,145,25);  JL[i].setText("(Invalid database)");JL[i].setForeground(Color.red);     JL[i++].setFont(F);
	   JL[i].setBounds(220,100,90,25); JL[i].setText("DATABASE : ");  	    JL[i].setForeground(Color.black);   JL[i++].setFont(F);
	   JL[i].setBounds(50,130,135,25);  JL[i].setText("(Invalid table)");  	JL[i].setForeground(Color.red);     JL[i++].setFont(F);
	   JL[i].setBounds(220,130,70,25); JL[i].setText("table : ");  		    JL[i].setForeground(Color.black);   JL[i].setFont(F);
	   JT[0].setText("gamerslist") ;    JT[1].setText("t1") ;
	   JLabel L = new JLabel(); L.setBounds(250,50,100,25); L.setFont(F); JF.add(L);
	   i = 0 ;
	   while(i<4)
	   {
	   	  JF.add(JL[i++]);
	   	  if(j<2)
	   	  {
	   	  	JF.add(JT[j]);
	   	  	JT[j++].setFont(F);
	   	  }
	   }
	   JL[0].setVisible(false); JL[2].setVisible(false);
	   JButton JB = new JButton("PROCEDD");  JB.setBounds(250,175,100,25); JB.setFont(F);  JF.add(JB);
	   JB.addActionListener(new ActionListener()
	   {
	   	 public void actionPerformed(ActionEvent e)
	   	 {
	   	 	if(Check(JT[0],JT[1],JL[0],JL[2]))
	   	 	{
	   	 		L.setText("Created....");
	   	 	}
	   	 	else
	   	 	{
	   	 		L.setText("Not Created....");
	   	 	}
	   	 }
	   });
	   JF.setVisible(false); JF.setVisible(true);
	}	

	public static boolean Check(JTextField JT1, JTextField JT2, JLabel JL1, JLabel JL2)
	{
		try
		{
			Boolean B1 = false, B2 = B1 ;
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection C = DriverManager.getConnection("jdbc:mysql://localhost:3306/","root","Shubham@1999");
			Statement S = C.createStatement();
			if(C!=null)
			{
				ResultSet Rs = S.executeQuery("show databases ;");
				while(Rs.next())
				{
					if(JT1.getText().compareTo(Rs.getString(1))==0)
					{
						B1 = true ; break ;
					}
				}
				if(!B1) { JL1.setVisible(true); return false ; }
				else
				{
					S.executeUpdate("use "+JT1.getText()+" ;");
					Rs = S.executeQuery("show tables ;");
					while(Rs.next())
					{
						if(JT2.getText().compareTo(Rs.getString(1))==0)
						{
							B2 = true ; break ;
						}
					}
					if(!B2) { JL2.setVisible(true); return false; }
					else { COLLECT_DATA(Rs,S,JT1,JT2); return true ; }
				}
			}
			else
			{
				return false ;
			}
		}
		catch(ClassNotFoundException e)
		{
			System.out.println(e); return false ;
		}
		catch(SQLException e)
		{
			System.out.println(e); return false ;
		}
	}

	public static void COLLECT_DATA(ResultSet Rs, Statement S, JTextField JT1, JTextField JT2)
	{
		try
		{
			int i = 0, j , k ;
			S.executeUpdate("show databases ;");
			S.executeUpdate("use "+JT1.getText()+" ;");
			Rs = S.executeQuery("select SrNo from "+JT2.getText()+" ;");
			while(Rs.next())
			{
				i++ ;
			}
			String R_N_T[][] = new String[3][i] ;
			int s_m_s[][] = new int[3][i] ;
			i = 0 ;
			S.executeUpdate("show databases ;");
			S.executeUpdate("use "+JT1.getText()+" ;");
			Rs = S.executeQuery("select * from "+JT2.getText()+" ;");
			while(Rs.next())
			{
				R_N_T[0][i] = Rs.getString(4);	R_N_T[1][i] = Rs.getString(2);	R_N_T[2][i] = Rs.getString(5) ;
				s_m_s[0][i] = Rs.getInt(3);	s_m_s[1][i] = Rs.getInt(6);	s_m_s[2][i++] = Rs.getInt(7);
			}
			j = 0 ;
			k = 0 ;
			String SWP ; int swp ;
			while(j<i)
			{
				if(R_N_T[0][j].compareTo("WIN")==0)
				{
					R_N_T = S(R_N_T,j,k,3);
					s_m_s = I(s_m_s,j,k++,3);
				}
				j++ ;
			}
			j = 0 ;
			while(j<i)
			{
				if(R_N_T[0][j].compareTo("LOOSE")==0) { break ; } j++;
			}
			int WIN_s = j ;
			j = 0 ;
			while(j<WIN_s)
			{
				k = j+1 ;
				while(k<WIN_s)
				{
					if(s_m_s[0][j]<s_m_s[0][k])
					{
						R_N_T = S(R_N_T,j,k,3);
						s_m_s = I(s_m_s,j,k,3);
					}
					else if(s_m_s[0][j]==s_m_s[0][k])
					{
						if(s_m_s[1][j]>s_m_s[1][k])
						{
							R_N_T = S(R_N_T,j,k,3);
							s_m_s = I(s_m_s,j,k,3);
						}
						else if(s_m_s[1][j]==s_m_s[1][k])
						{
							if(s_m_s[2][j]>s_m_s[2][k])
							{
								R_N_T = S(R_N_T,j,k,3);
								s_m_s = I(s_m_s,j,k,3);
							}
						}
					}
					k++ ;
				}
			j++ ;
			}
			j = WIN_s ;
			while(j<i)
			{
				k = j + 1 ;
				while(k<i)
				{
					if(s_m_s[0][j]<s_m_s[0][k])
					{
						R_N_T = S(R_N_T,j,k,3);
						s_m_s = I(s_m_s,j,k,3);
					}
					else if(s_m_s[0][j]==s_m_s[0][k])
					{
						if(s_m_s[1][j]>s_m_s[1][k])
						{
							R_N_T = S(R_N_T,j,k,3);
							s_m_s = I(s_m_s,j,k,3);
						}
						else if(s_m_s[1][j]==s_m_s[1][k])
						{
							if(s_m_s[2][j]>=s_m_s[2][k])
							{
								R_N_T = S(R_N_T,j,k,3);
								s_m_s = I(s_m_s,j,k,3);
							}
						}
					}
					k++ ;
				}
				j++ ;
			}
			CREATE_HTML(i,s_m_s,R_N_T);
		}
		catch(SQLException e) { System.out.println(e); }
	}
	public static String[][] S(String ARR[][],int j, int k, int n)
	{
		String SWP ; int i = 0 ;
		while(i<n)
		{
			SWP = ARR[i][j] ;	ARR[i][j] = ARR[i][k] ;	ARR[i++][k] = SWP ;
		}
		return ARR ;
	}

	public static int[][] I(int ARR[][],int j, int k, int n)
	{
		int SWP, i = 0 ;
		while(i<n)
		{
			SWP = ARR[i][j] ;	ARR[i][j] = ARR[i][k] ;	ARR[i++][k] = SWP ;
		}
		return ARR ;
	}

	public static void CREATE_HTML(int i, int I[][], String S[][])
	{
		File F = new File("RANK-LIST.html");
		if(F.exists()) { F.delete(); CREATE_HTML(i,I,S); }
		else
		{
			try
			{
				FileWriter FW = new FileWriter("RANK-LIST.html");
				FW.write("<!DOCTYPE html>\n<html>\n<head><h1 style=\"text-align:center;\">Rank-List</h1><title>Rank-List</title></head>\n<body>\n");
				FW.write("<style> .C { font-weight:bold; font-size:20px; text-align:center; font-family:calibri;\n");
				FW.write("</style>\n");
				FW.write("<div style=\"text-align:center;\">\n");
				FW.write("<div style=\"display:inline-block; border:2px solid red;	\">\n");
				FW.write("<table class=\"C\" cellspacing=\"0px\" border=\"2\" cellpadding=\"3\" width=\"950px\">\n");
				FW.write("<tr><th>Rank</th><th>Name</th><th>Score</th><th>Result</th><th>Time</th></tr>\n");
				int rnk = 0 ;
				while(rnk<i)
				{
					FW.write("<tr><td>"+(rnk+1)+"</td><td>"+S[1][rnk]+"</td><td>"+I[0][rnk]+"</td>");
					FW.write("<td>"+S[0][rnk]+"</td><td>"+S[2][rnk++]+"</td></tr>\n");
				}
				FW.write("</table>\n</div>\n</div>\n</body>\n</html>");
				FW.close();
			}
			catch(IOException e) { System.out.println(e); }
		}
	}
}