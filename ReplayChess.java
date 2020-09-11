import javax.swing.* ;
import java.awt.* ;
import java.util.Scanner;
import java.util.Vector;

/*
This program is a front end chess game program it supports the .pgn format (portable gaming notation format for chess)
Originally i chose the name ReplayChess because it allows one to rewind/Replay moves or replay chess games step thru the moves in the .pgn format.

Most online chess games support and use pgn format its universal notation for playing chess with just symbol representation
I choice to uses the long algebraic notion so moves are always specified with piece letter followed by source square followed by destination square.
This is a few extra letters but its to me much easier for humans to read and it still portable for computers to parse

I make 2 addition updates or changes to the algebraic notion 
1) pawns where never given a piece letter they where the absense of a piece letter so e4e5 would mean a pawn moved from e4 to e5 i feel like its better to add 
the one extra letter at the beginning keeping it like the other piece so I write Pe4e5 for pawn move instead of just e4e5
All other piece king = 'K' ,queen ='Q' , knight='N' , rook='R' , bishop='B' have a unique identifing letter so why shouldnt P=pawn ? well now it does

2) when it wasnt ambigious one could uses short hand like e5 to me e4e5 which my notation you right Pe4e5 because the only move to e5 could be done by a pawn at e4 
for a particular board configuration ...Well my notation only allows for the whole source followed by destination not when unambigous to uses the shorthand
i just find the short hand makes it less human readable then a two extra letter 

Other then those few tweaks in long algebraic notion its the same universal format but note because i did these small changes the pgn format might need to be tweaked 
to get it to work with all online or software based chess games but i am willing to write a small pgn parse to make the incompatiblities compatible again in the event 
it comes down to it. No big problem :)

You can almost do it by hand as most games last for less the 50 moves anyway

*/


public class ReplayChess extends JFrame {


/**
	 * 
	 */
private static final long serialVersionUID = 8823557688863973765L;
//Player ones pieces 
//unicode values come in really handy for chess games else i have to download an image of each players piece 
//and set the background to that image accordingly :)
private static final String king1   = "\u2654" ;
private static final String queen1  = "\u2655" ;
private static final String rook1   = "\u2656" ;
private static final String bishop1 = "\u2657" ;
private static final String knight1 = "\u2658" ;
private static final String pond1   = "\u2659" ;

//Player two pieces 
private static final String king2   = "\u265A" ;
private static final String queen2  = "\u265B" ;
private static final String rook2   = "\u265C" ;
private static final String bishop2 = "\u265D" ;
private static final String knight2 = "\u265E" ;
private static final String pond2   = "\u265F" ;

//board size designed for the traditional 8x8 board 
private static final int size = 8 ;
private JLabel cboard[][] = null ;
private static final String IMAGEFILE = "pictic.jpg" ;
private static final int gui_size = 500 ; //Change this number if you want a different size GUI chess board image the default size is 500 which should be fine!

//Internal Variables and Functions for validating castling moves 
//Only purpose is to validate if castling moves are ok or to disable them
private static boolean player1kings_firstmove = true ;
private static boolean player1kingside_rooksfirstmove = true ;
private static boolean player1queenside_rooksfirstmove = true ;

private static boolean player2kings_firstmove = true ;
private static boolean player2kingside_rooksfirstmove = true ;
private static boolean player2queenside_rooksfirstmove = true ;

private void setplayer1kings_firstmove( boolean isfirstmove ) 
{
	player1kings_firstmove = isfirstmove ;
}
private void setplayer1kingside_rooksfirstmove( boolean isfirstmove ) 
{
	player1kingside_rooksfirstmove = isfirstmove ;
}
private void setplayer1queenside_rooksfirstmove( boolean isfirstmove ) 
{
	player1queenside_rooksfirstmove = isfirstmove ;
}

private void setplayer2kings_firstmove( boolean isfirstmove ) 
{
	player2kings_firstmove = isfirstmove ;
}
private void setplayer2kingside_rooksfirstmove( boolean isfirstmove ) 
{
	player2kingside_rooksfirstmove = isfirstmove ;
}
private void setplayer2queenside_rooksfirstmove( boolean isfirstmove ) 
{
	player2queenside_rooksfirstmove = isfirstmove ;
}

/////////////////////////////////////////////////////////////////////


//Variables used for the draw by 50 moves rule of chess..............................
private static int      lastpawnmove = 0 ;
private static int lastcapturedpiece = 0 ;
//...........................................................


public ReplayChess()
{
super( "Chess" ) ;
Image icon = new javax.swing.ImageIcon(IMAGEFILE).getImage();
this.setIconImage(icon);
this.setResizable(false);
//this.setUndecorated(true);
//this.setLocation(300,400) ;
this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//Builds the the GUI board for the game
setSize(gui_size,gui_size) ;
cboard = new JLabel[size][size] ;
this.setLayout(new GridLayout(size+1,size+1));

for( int i = 0 ; i < cboard.length ; i++ )
{

 JLabel tmp = new JLabel( "" + (size - i) + "  " ) ;
 tmp.setHorizontalAlignment(SwingConstants.RIGHT);
 this.add( tmp ) ;
   
   for( int j = 0 ; j < cboard.length ; j++ )
   {

     
      if( (i+j) % 2 == 0 )
      { 
      cboard[i][j] = new JLabel("") ;
      cboard[i][j].setBackground(Color.BLACK) ;
      cboard[i][j].setForeground(Color.BLUE) ;
      cboard[i][j].setOpaque(true);
      cboard[i][j].setFont(new Font("Serif", Font.BOLD, 40));
      cboard[i][j].setHorizontalAlignment(SwingConstants.CENTER);
      }
      else
      {
      cboard[i][j] = new JLabel("") ;
      cboard[i][j].setBackground(Color.WHITE) ;
      cboard[i][j].setForeground(Color.BLUE) ;
      cboard[i][j].setOpaque(true);
      cboard[i][j].setFont(new Font("Serif", Font.BOLD, 40));
      cboard[i][j].setHorizontalAlignment(SwingConstants.CENTER);
      }
      
      
      
      this.add( cboard[i][j] ) ;

   }





}

//set the pieces on the board up for each player alot faster then humans setting a board up :) lol
//also adds in the column letter labels for each column of the GUI chess board for the game

cboard[0][0].setText(rook1) ;
cboard[0][1].setText(knight1) ;
cboard[0][2].setText(bishop1) ;
cboard[0][3].setText(queen1) ;
cboard[0][4].setText(king1) ;
cboard[0][5].setText(bishop1) ;
cboard[0][6].setText(knight1) ;
cboard[0][7].setText(rook1) ;

cboard[1][0].setText(pond1) ;
cboard[1][1].setText(pond1) ;
cboard[1][2].setText(pond1) ;
cboard[1][3].setText(pond1) ;
cboard[1][4].setText(pond1) ;
cboard[1][5].setText(pond1) ;
cboard[1][6].setText(pond1) ;
cboard[1][7].setText(pond1) ;




cboard[7][0].setText(rook2) ;
cboard[7][1].setText(knight2) ;
cboard[7][2].setText(bishop2) ;
cboard[7][3].setText(queen2) ;
cboard[7][4].setText(king2) ;
cboard[7][5].setText(bishop2) ;
cboard[7][6].setText(knight2) ;
cboard[7][7].setText(rook2) ;

cboard[6][0].setText(pond2) ;
cboard[6][1].setText(pond2) ;
cboard[6][2].setText(pond2) ;
cboard[6][3].setText(pond2) ;
cboard[6][4].setText(pond2) ;
cboard[6][5].setText(pond2) ;
cboard[6][6].setText(pond2) ;
cboard[6][7].setText(pond2) ;


this.add( new JLabel("") ) ;

JLabel tmp = null ;
tmp = new JLabel("a") ;
tmp.setHorizontalAlignment(SwingConstants.CENTER);
this.add(tmp) ;
tmp = new JLabel("b") ;
tmp.setHorizontalAlignment(SwingConstants.CENTER);
this.add(tmp) ;
tmp = new JLabel("c") ;
tmp.setHorizontalAlignment(SwingConstants.CENTER);
this.add(tmp) ;
tmp = new JLabel("d") ;
tmp.setHorizontalAlignment(SwingConstants.CENTER);
this.add(tmp) ;
tmp = new JLabel("e") ;
tmp.setHorizontalAlignment(SwingConstants.CENTER);
this.add(tmp) ;
tmp = new JLabel("f") ;
tmp.setHorizontalAlignment(SwingConstants.CENTER);
this.add(tmp) ;
tmp = new JLabel("g") ;
tmp.setHorizontalAlignment(SwingConstants.CENTER);
this.add(tmp) ;
tmp = new JLabel("h") ;
tmp.setHorizontalAlignment(SwingConstants.CENTER);
this.add(tmp) ;


setVisible(true);


}


//gets the column number from the corrosponding a,b,c,d,e,f,g,h column labels 
private int getColNum( char c )
{

//System.out.println( "getColNum = " + c ) ;

if( c == 'a' )
return 0 ;

if( c == 'b' )
return 1 ;

if( c == 'c' )
return 2 ;

if( c == 'd' )
return 3 ;

if( c == 'e' )
return 4 ;

if( c == 'f' )
return 5 ;

if( c == 'g' )
return 6 ;

if( c == 'h' )
return 7 ;

return -1 ;

}


//Method for getting the letter associated with the column of the chess board
private String getColLetter( int c )
{

//System.out.println( "getColLetter = " + c ) ;

if( c == 0 )
return "a" ;

if( c == 1 )
return "b" ;

if( c == 2 )
return "c" ;

if( c == 3 )
return "d" ;

if( c == 4 )
return "e" ;

if( c == 5 )
return "f" ;

if( c == 6 )
return "g" ;

if( c == 7 )
return "h" ;

return null ;


}


//gets the row number similar to getColNum function but for rows instead of columns 
//note this is a little backwards the proper row number would be size - the return value 
//because the top most row on the gui is really the zeroth row and the bottom most row on the gui is really the 7th row (aka 8 - 1 because of computer arrays starting at zero )
//Little confusing on rows on chessboard game 
//TODO: think about this function not a big deal now and if changed could take recoding a few things maybe not worth doing
private int getRowNum( char c )
{
//System.out.println( "getRowNum = " + c ) ;
return Integer.parseInt( String.valueOf(c) ) - 1 ;

}

//This function takes in a move in pgn format and does the action of moving the piece!
//Note this function does not check if the move is valid only does the move
//You should call move( ... ) function instead rather then directly calling this function if your playing a game or watching a pgn previous game
//This function can be used to get the board into weird configurations that are not possible in regular chess so be careful
//Uses move function instead in 99% of cases
//This function will not move any pieces and return -1 if the player is different then 1 or 2
//Or the pieces arent K , Q , R, N, B , P type one could in theory add totally different nonstandard chess piece to this function
//though that would call for additional validation functions for the new piece or a completely new game piece with new gaming rules
// so if your coding a chess like game off this code here is where you add in new pieces and define how they move
public int movepiece( String move , int player )
{

int pcol = -1 ;
int prow = -1 ;
int col  = -1 ;
int row  = -1 ; 

if( move.charAt(0) == 'K' )
{

pcol = getColNum( move.charAt(1) );
prow = getRowNum( move.charAt(2) );
col = getColNum( move.charAt(3) );
row = getRowNum( move.charAt(4) );

if( player == 1 )
{
cboard[size - row - 1][col].setText(king1) ;
cboard[size - prow - 1][pcol].setText("") ;
//Used to keep track of first move by king for castling based moves
//////////////////////////////////////////////////////////////////
player1kings_firstmove = false ;
//////////////////////////////////////////////////////////////////
return 0 ;
}

if( player == 2 )
{
cboard[size - row - 1][col].setText(king2) ;
cboard[size - prow - 1][pcol].setText("") ;
//Used to keep track of first move by king for castling based moves
//////////////////////////////////////////////////////////////////
player2kings_firstmove = false ;
//////////////////////////////////////////////////////////////////
return 0 ;
}

return -1 ;


}

if( move.charAt(0) == 'Q' )
{

pcol = getColNum( move.charAt(1) );
prow = getRowNum( move.charAt(2) );
col = getColNum( move.charAt(3) );
row = getRowNum( move.charAt(4) );

if( player == 1 )
{
cboard[size - row - 1][col].setText(queen1) ;
cboard[size - prow - 1][pcol].setText("") ;
return 0 ;
}

if( player == 2 )
{
cboard[size - row - 1][col].setText(queen2) ;
cboard[size - prow - 1][pcol].setText("") ;
return 0 ;
}

return -1 ;


}

if( move.charAt(0) == 'R' )
{

pcol = getColNum( move.charAt(1) );
prow = getRowNum( move.charAt(2) );
col = getColNum( move.charAt(3) );
row = getRowNum( move.charAt(4) );

if( player == 1 )
{
cboard[size - row - 1][col].setText(rook1) ;
cboard[size - prow - 1][pcol].setText("") ;

//Logic to keep track of when a rooks first move is done
//Used for castling moves only but important for the castling move
////////////////////////////////////////////////////////////////
if( pcol == getColNum('h') && prow == getRowNum('8') )
	player1kingside_rooksfirstmove = false ;
	
if( pcol == getColNum('a') && prow == getRowNum('8') )
	player1queenside_rooksfirstmove = false ;
////////////////////////////////////////////////////////////////

return 0 ;
}

if( player == 2 )
{
cboard[size - row - 1][col].setText(rook2) ;
cboard[size - prow - 1][pcol].setText("") ;

//Logic to keep track of when a rooks first move is done
//Used for castling moves only but important for the castling move
////////////////////////////////////////////////////////////////
if( pcol == getColNum('h') && prow == getRowNum('1') )
	player2kingside_rooksfirstmove = false ;
	
if( pcol == getColNum('a') && prow == getRowNum('1') )
	player2queenside_rooksfirstmove = false ;
////////////////////////////////////////////////////////////////

return 0 ;
}

return -1 ;


}


if( move.charAt(0) == 'B' )
{

pcol = getColNum( move.charAt(1) );
prow = getRowNum( move.charAt(2) );
col = getColNum( move.charAt(3) );
row = getRowNum( move.charAt(4) );

if( player == 1 )
{
cboard[size - row - 1][col].setText(bishop1) ;
cboard[size - prow - 1][pcol].setText("") ;
return 0 ;
}

if( player == 2 )
{
cboard[size - row - 1][col].setText(bishop2) ;
cboard[size - prow - 1][pcol].setText("") ;
return 0 ;
}

return -1 ;


}

if( move.charAt(0) == 'N' )
{

pcol = getColNum( move.charAt(1) );
prow = getRowNum( move.charAt(2) );
col = getColNum( move.charAt(3) );
row = getRowNum( move.charAt(4) );

if( player == 1 )
{
cboard[size - row - 1][col].setText(knight1) ;
cboard[size - prow - 1][pcol].setText("") ;
return 0 ;
}

if( player == 2 )
{
cboard[size - row - 1][col].setText(knight2) ;
cboard[size - prow - 1][pcol].setText("") ;
return 0 ;
}

return -1 ;


}

if( move.charAt(0) == 'P' )
{

pcol = getColNum( move.charAt(1) );
prow = getRowNum( move.charAt(2) );
col = getColNum( move.charAt(3) );
row = getRowNum( move.charAt(4) );

if( player == 1 )
{
cboard[size - row - 1][col].setText(pond1) ;
cboard[size - prow - 1][pcol].setText("") ;
return 0 ;
}

if( player == 2 )
{
cboard[size - row - 1][col].setText(pond2) ;
cboard[size - prow - 1][pcol].setText("") ;
return 0 ;
}

return -1 ;

}

return -1 ;

}

//Method to test if the pawn is moving for the first time for a given player
//Is mainly used in the isValidMove(...) to allow a pawn to move two squares for his first move
//All other pawn moves after its first move allow only one square for a move 
private boolean pawnfirstMove( String m , int player )
{

int prow = -1 ;
prow = getRowNum( m.charAt(2) );

if( player == 2 && prow == 1 && m.charAt(0) == 'P' )
return true ;

if( player == 1 && prow == 6 && m.charAt(0) == 'P' )
return true ;

return false ;

}


//Method that check to see that the castle move can be done
//Is in a valid state of doing the castle move before the move is actually done by doCastleMove(...)
//If this method returns false then you shouldn't do the move aka doCastleMove(...)
//This method should always be called before doCastleMove(...) is done!
public boolean isValidCastleMove( String m , int player )
{
	int p2row = getRowNum('1') ; //first row of second player pieces
	int p1row = getRowNum('8') ; //first row of first  player pieces
	int d = getColNum('d');
	int f = getColNum('f');
	int c = getColNum('c');
	int b = getColNum('b');
	int g = getColNum('g');
	

	if( m.equals("O-O-O") == true && player == 1  && ( player1kings_firstmove != true || player1queenside_rooksfirstmove != true ) )
	return false ;
	
	if( m.equals("O-O") == true && player == 1  && ( player1kings_firstmove != true || player1kingside_rooksfirstmove != true ) )
	return false ;
	
	if( m.equals("O-O-O") == true && player == 2  && ( player2kings_firstmove != true || player2queenside_rooksfirstmove != true ) )
	return false ;
		
	if( m.equals("O-O") == true && player == 2  && ( player2kings_firstmove != true || player2kingside_rooksfirstmove != true ) )
	return false ;
	
	
	//queenside castling  O-O-O for player 1 check
	if( m.equals("O-O-O") == true && cboard[size - p1row-1][d].getText().equals("") == true && cboard[size - p1row-1][c].getText().equals("") == true && cboard[size - p1row-1][b].getText().equals("") == true && player == 1 )
	{
		if( isInCheck(player) != true )
		{
			movepiece("Ke8d8",player) ;
			if( isInCheck(player) != true )
			{
				movepiece("Kd8c8",player) ;
				if( isInCheck(player) != true )
				{
					movepiece("Kc8e8",player) ;
					return true ;
				}
			}
		}
	
		movepiece("K" + getKing(player) + "e8",player) ;
		setplayer1kings_firstmove(true) ;
		setplayer1queenside_rooksfirstmove(true) ;
		return false ;
	
	}
	
	//queenside castling  O-O-O for player 2 check
	if( m.equals("O-O-O") == true && cboard[size - p2row-1][d].getText().equals("") == true && cboard[size - p2row-1][c].getText().equals("") == true && cboard[size - p2row-1][b].getText().equals("") == true && player == 2 )
	{
		if( isInCheck(player) != true )
		{
			movepiece("Ke1d1",player) ;
			if( isInCheck(player) != true )
			{
				movepiece("Kd1c1",player) ;
				if( isInCheck(player) != true )
				{
					movepiece("Kc1e1",player) ;
					return true ;
				}
			}
		}
	
		movepiece("K" + getKing(player) + "e1",player) ;
		setplayer2kings_firstmove(true) ;
		setplayer2queenside_rooksfirstmove(true) ;
		return false ;
	
	}
	
	//kingside castling O-O for player 1 check
	if( m.equals("O-O") == true && cboard[size - p1row-1][f].getText().equals("") == true && cboard[size - p1row-1][g].getText().equals("") == true && player == 1 )
	{
		if( isInCheck(player) != true )
		{
			movepiece("Ke8f8",player) ;
			if( isInCheck(player) != true )
			{
				movepiece("Kf8g8",player) ;
				if( isInCheck(player) != true )
				{
					movepiece("Kg8e8",player) ;
					return true ;
				}
				
			}
				
				
		}
		
		movepiece("K" + getKing(player) + "e8",player) ;
		setplayer1kings_firstmove(true) ;
		setplayer1kingside_rooksfirstmove(true) ;
		return false ;
		
	}
	
	//kingside castling O-O for player 2 check
	if( m.equals("O-O") == true && cboard[size - p2row-1][f].getText().equals("") == true && cboard[size - p2row-1][g].getText().equals("") == true && player == 2 )
	{
		if( isInCheck(player) != true )
		{
			movepiece("Ke1f1",player) ;
			if( isInCheck(player) != true )
			{
				movepiece("Kf1g1",player) ;
				if( isInCheck(player) != true )
				{
					movepiece("Kg1e1",player) ;
					return true ;
				}
				
			}
				
				
		}
		
		
		movepiece("K" + getKing(player) + "e1",player) ;
		setplayer2kings_firstmove(true) ;
		setplayer2kingside_rooksfirstmove(true) ;
		return false ;
		
	}

	
	
	
return false ;

}


//This method does the move for the castling move
//Takes in the String m = "O-O-O" or "O-O" and int player 1 or 2
//Moves the king and rook accordingly if its a kingside or queenside castling move.
//Note: this method only does the move it does not check if its valid
//So one should always call isValidCastleMove(...) to check if its valid to do the castle move in the first place!!!
//Then if thats good call this function to actually do the move
public void doCastleMove( String m , int player )
{

//kingside castling O-O for player 1
if( m.equals("O-O" ) == true && player == 1 )
{

int pking = getColNum( 'e' ) ;
int prook = getColNum( 'h' ) ;

int eking = getColNum( 'g' ) ;
int erook = getColNum( 'f' ) ;
int row = getRowNum('8') ;

cboard[size - row - 1][pking].setText("") ;
cboard[size - row - 1][prook].setText("") ;
cboard[size - row - 1][eking].setText(king1) ;
cboard[size - row - 1][erook].setText(rook1) ;

return ;

}

//kingside castling O-O for player 2
if( m.equals("O-O" ) == true && player == 2 )
{

int pking = getColNum( 'e' ) ;
int prook = getColNum( 'h' ) ;

int eking = getColNum( 'g' ) ;
int erook = getColNum( 'f' ) ;
int row = getRowNum('1') ;

cboard[size - row - 1][pking].setText("") ;
cboard[size - row - 1][prook].setText("") ;
cboard[size - row - 1][eking].setText(king2) ;
cboard[size - row - 1][erook].setText(rook2) ;

return ;

}



//queenside castling  O-O-O for player 1
if( m.equals("O-O-O" ) == true && player == 1 )
{

int pking = getColNum( 'e' ) ;
int prook = getColNum( 'a' ) ;

int eking = getColNum( 'c' ) ;
int erook = getColNum( 'd' ) ;
int row = getRowNum('8') ;

cboard[size - row - 1][pking].setText("") ;
cboard[size - row - 1][prook].setText("") ;
cboard[size - row - 1][eking].setText(king1) ;
cboard[size - row - 1][erook].setText(rook1) ;

return ;

}

//queenside castling  O-O-O for player 2
if( m.equals("O-O-O" ) == true && player == 2 )
{

int pking = getColNum( 'e' ) ;
int prook = getColNum( 'a' ) ;

int eking = getColNum( 'c' ) ;
int erook = getColNum( 'd' ) ;
int row = getRowNum('1') ;

cboard[size - row - 1][pking].setText("") ;
cboard[size - row - 1][prook].setText("") ;
cboard[size - row - 1][eking].setText(king2) ;
cboard[size - row - 1][erook].setText(rook2) ;

return ;

}



//should never get here 
return ;

}

//This function checks to see if its a pawn promotion based move for a given player
public boolean isPawnPromotionMove( String m , int player )
{

int row = -1 ;
row = getRowNum( m.charAt(4) );

if( player == 1 && row == 7 && m.charAt(0) == 'P' )
return true ;

if( player == 2 && row == 0 && m.charAt(0) == 'P' )
return true ;

return false ;

}

//This function test to see if its a valid promotion piece to promote the pawn to 
public boolean isValidPawnPromotion( String m , int player )
{

if( m.indexOf( "=Q" ) == -1 && m.indexOf( "=R" ) == -1 && m.indexOf( "=B" ) == -1 && m.indexOf( "=N" ) == -1 )
return false ;

return true ;
}


//This function does the actual pawn promotion
public void doPawnPromotion( String m , int player )
{

int col  = -1 ;
int row  = -1 ;
col = getColNum( m.charAt(3) );
row = getRowNum( m.charAt(4) );



if( player == 1 )
{

if( m.indexOf( "=Q" ) != -1 )
{
cboard[size - row - 1][col].setText(queen1) ; 
return ;
}

if( m.indexOf( "=R" ) != -1 )
{
cboard[size - row - 1][col].setText(rook1) ; 
return ;
}

if( m.indexOf( "=B" ) != -1 )
{
cboard[size - row - 1][col].setText(bishop1) ; 
return ;
}

if( m.indexOf( "=N" ) != -1 )
{
cboard[size - row - 1][col].setText(knight1) ; 
return ;
}

}



if( player == 2 )
{

if( m.indexOf( "=Q" ) != -1 )
{
cboard[size - row - 1][col].setText(queen2) ; 
return ;
}

if( m.indexOf( "=R" ) != -1 )
{
cboard[size - row - 1][col].setText(rook2) ; 
return ;
}

if( m.indexOf( "=B" ) != -1 )
{
cboard[size - row - 1][col].setText(bishop2) ; 
return ;
}

if( m.indexOf( "=N" ) != -1 )
{
cboard[size - row - 1][col].setText(knight2) ; 
return ;
}

}


return  ;
}



/*
This function should be called instead of movepiece directly because move(...) test the validity of executing the move before it is executed.
movepiece only executes the moves sent to it in pgn format and has limited sophistication in checking if the moves are allowed
move(...) function aka this function should be called if you want sophistication , validatation of moves , and to play a game of chess
returns 0 if it was successful in calling movepiece and executing the moves out.

return types are
invalid player  -4           if a player is not 1 or 2 then there are to many player chess only has exactly to players
invalid castlemove -2        if the castlemove cannt be done
invalid pawnpromotion -3     if the pawn promotion piece is not correct or if there was a problem with pawn promotion
invalid move -1              if a move cannt be done because its not a valid chess move or puts your king in check position
valid move 0                 if this function returns this it means the move was successfully done and was a valid move YOU SHOULD SEE THE CHANGE ON THE GUI TAKE AFFECT IN THIS CASE
*/

public int move(String m , int player )
{
//Ph7-h8=Q
//Pg7xf8=R+
//Nh7xf6+

//simple check to see that its either player 1 or player 2 turn if its some other player other then that its an invalid player
if( player != 1 && player != 2 )
return -4 ;

boolean isvalidmove = isValidMove( m , player ) ;


if( isvalidmove == true ) 
{

//Do the castle move
if( m.equals( "O-O-O" ) == true || m.equals("O-O") == true )
{
lastpawnmove = 0 ;
lastcapturedpiece = 0 ;
doCastleMove( m , player ) ;
return 0 ;
}

//Do the pawn promotion move or return invalid pawn promotion chosen as -3
if( isPawnPromotionMove( m , player) == true )
{

if( isValidPawnPromotion( m , player ) == true )
{
movepiece( m , player ) ;
doPawnPromotion( m , player ) ;
lastpawnmove = 0 ;
lastcapturedpiece = 0 ;
return 0 ;
}

return -3 ;

}

//Check to see if the move was a pawn or a capture move
//If so reset the lastpawnmove and lastcapturepiece field for the draw by 50 rule function
/////////////////////////For Draw by 50 move logic /////////////////////////////////////////////
int col = getColNum( m.charAt(3) );
int row = getRowNum( m.charAt(4) );
if( m.charAt(0) == 'P' || isOpponent( cboard[size - row - 1][col].getText() , player ) == true )
{
	lastpawnmove = 0 ;
	lastcapturedpiece = 0 ;
	movepiece( m , player ) ;
	return 0 ;
}
////////////////////////////////////////////////////////////////////////////////////////////////

//Do the valid piece move
lastpawnmove++ ;
lastcapturedpiece++ ;
movepiece( m , player ) ;

return 0 ;
}

//return invalid castle move -2
if( m.equals( "O-O-O" ) == true || m.equals("O-O") == true )
return -2 ;

//return invalid move that isnt a special move like a castle or pawn promotion error
return -1 ;

}

//Most Important function used to validate the moves of the chess game at a given state of play 
//Called on by move(...) function as the workhorse function for validating game moves before move(...) calls the movepiece(...) function
//to actually move the gui board piece
//return false if a problem in executing that particular move with that particular player
//returns true if there is no issue in executing the move
public boolean isValidMove( String m , int player )
{

//test to see if the move is a special castling move
//if so checks the validate of it in the current game state
if( m.equals( "O-O-O" ) == true || m.equals("O-O") == true )
{

if( isValidCastleMove( m , player ) == true )
return true ;

return false ;

}


int pcol = -1 ;
int prow = -1 ;
int col  = -1 ;
int row  = -1 ;
pcol = getColNum( m.charAt(1) );
prow = getRowNum( m.charAt(2) );
col = getColNum( m.charAt(3) );
row = getRowNum( m.charAt(4) );

//logic to check the source and destination square are valid board squares
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
if( pcol == col && prow == row ) //add that here!!!
return false ;

if( pcol < 0 || pcol > 7)
return false ;

if( col < 0 || col > 7)
return false ;
	
if( prow < 0 || prow > 7)
return false ;

if( row < 0 || row > 7)
return false ;
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


String bpiece = cboard[size - prow - 1][pcol].getText() ; 
//System.out.println( "bpiece = " + bpiece  ) ;



if( m.charAt(0) == 'K' && (bpiece.equals(king1) == true || bpiece.equals(king2) == true ) )
{

//System.out.println( "isValidMove func = " + "pcol = " + pcol + " prow = " + prow ) ;
//System.out.println( "isValidMove func = " + "col = " + col + " row = " + row ) ;

if( (bpiece.equals( king2 ) == true && player == 1) || (bpiece.equals( king1 ) == true && player == 2)) 
return false ;

if( Math.abs( col-pcol ) <= 1 && Math.abs( row-prow ) <= 1 )
{
if( cboard[size - row - 1][col].getText().equals("") == true )
{
if( notKilledmove( m , player ) == true )
return true ;

return false ;
}

if( isOpponent( cboard[size - row - 1][col].getText() , player ) == true )
{
if( notKilledmove( m , player ) == true )
return true ;

return false ;
}


return false ;

}

return false ;

}

if( m.charAt(0) == 'Q' && (bpiece.equals(queen1) == true || bpiece.equals(queen2) == true ))
{

//System.out.println( "isValidMove func = " + "pcol = " + pcol + " prow = " + prow ) ;
//System.out.println( "isValidMove func = " + "col = " + col + " row = " + row ) ;

if( (bpiece.equals( queen2 ) == true && player == 1) || (bpiece.equals( queen1 ) == true && player == 2)) 
return false ;

if( pcol == col || prow == row || ( Math.abs( col - pcol ) / Math.abs( row - prow ) == 1 && Math.abs( col - pcol ) % Math.abs( row - prow ) == 0 )  )
{
if( cboard[size - row - 1][col].getText().equals("") == true && hasPathToSquare( m.charAt(0) , pcol , prow , col , row ) == true )
{
if( notKilledmove( m , player ) == true )
return true ;

return false ;
}


if( isOpponent( cboard[size - row - 1][col].getText() , player ) == true && hasPathToSquare( m.charAt(0) , pcol , prow , col , row ) == true)
{
if( notKilledmove( m , player ) == true )
return true ;

return false ;
}


return false ;

}


return false ;

}


if( m.charAt(0) == 'R' && (bpiece.equals(rook1) == true || bpiece.equals(rook2) == true ))
{

//System.out.println( "isValidMove func = " + "pcol = " + pcol + " prow = " + prow ) ;
//System.out.println( "isValidMove func = " + "col = " + col + " row = " + row ) ;

if( (bpiece.equals( rook2 ) == true && player == 1) || (bpiece.equals( rook1 ) == true && player == 2)) 
return false ;

if( pcol == col || prow == row )
{
if( cboard[size - row - 1][col].getText().equals("") == true && hasPathToSquare( m.charAt(0) , pcol , prow , col , row ) == true )
{
if( notKilledmove( m , player ) == true )
return true ;

return false ;
}


if( isOpponent( cboard[size - row - 1][col].getText() , player) == true && hasPathToSquare( m.charAt(0) , pcol , prow , col , row ) == true )
{
if( notKilledmove( m , player ) == true )
return true ;

return false ;
}


return false ;

}


return false ;



}


if( m.charAt(0) == 'B' && (bpiece.equals(bishop1) == true || bpiece.equals(bishop2) == true ) )
{

//System.out.println( "isValidMove func = " + "pcol = " + pcol + " prow = " + prow ) ;
//System.out.println( "isValidMove func = " + "col = " + col + " row = " + row ) ;

if( (bpiece.equals( bishop2 ) == true && player == 1) || (bpiece.equals( bishop1 ) == true && player == 2)) 
return false ;

if( row != prow && ( Math.abs( col - pcol ) / Math.abs( row - prow ) == 1 && Math.abs( col - pcol ) % Math.abs( row - prow ) == 0 ) )
{
if( cboard[size - row - 1][col].getText().equals("") == true && hasPathToSquare( m.charAt(0) , pcol , prow , col , row ) == true )
{
if( notKilledmove( m , player ) == true )
return true ;

return false ;
}


if( isOpponent( cboard[size - row - 1][col].getText() , player ) == true && hasPathToSquare( m.charAt(0) , pcol , prow , col , row ) == true )
{
if( notKilledmove( m , player ) == true )
return true ;

return false ;
}


return false ;

}


return false ;

}


if( m.charAt(0) == 'N' && (bpiece.equals(knight1) == true || bpiece.equals(knight2) == true ) )
{

//System.out.println( "isValidMove func = " + "pcol = " + pcol + " prow = " + prow ) ;
//System.out.println( "isValidMove func = " + "col = " + col + " row = " + row ) ;

if( (bpiece.equals( knight2 ) == true && player == 1) || (bpiece.equals( knight1 ) == true && player == 2)) 
return false ;

if( (Math.abs( row - prow ) == 1 && Math.abs( col - pcol ) == 2) || (Math.abs( row - prow ) == 2 && Math.abs( col - pcol ) == 1) )
{
if( cboard[size - row - 1][col].getText().equals("") == true )
{
if( notKilledmove( m , player ) == true )
return true ;

return false ;
}


if( isOpponent( cboard[size - row - 1][col].getText() , player ) == true )
{
if( notKilledmove( m , player ) == true )
return true ;

return false ;
}


return false ;

}


return false ;



}

if( m.charAt(0) == 'P' && (bpiece.equals(pond1) == true || bpiece.equals(pond2) == true ))
{

//System.out.println( "isValidMove func = " + "pcol = " + pcol + " prow = " + prow ) ;
//System.out.println( "isValidMove func = " + "col = " + col + " row = " + row ) ;

if( (bpiece.equals( pond2 ) == true && player == 1) || (bpiece.equals( pond1 ) == true && player == 2)) 
return false ;

if( col == pcol && ( ( (row - prow ) == 1 && player == 2 ) || ( (row - prow ) == -1 && player == 1 ) ) )
{
if( cboard[size - row - 1][col].getText().equals("") == true )
{
if( notKilledmove( m , player ) == true )
return true ;

return false ;
}


return false ;

}

//logic for pawn to move one square diagnol and take a capture
if( Math.abs( col - pcol ) == 1 && ( ( (row - prow ) == 1 && player == 2 ) || ( (row - prow ) == -1 && player == 1 ) ) )
{
if( isOpponent( cboard[size - row - 1][col].getText() , player ) == true || ( cboard[size - row - 1][col].getText().equals("") == true && en_passant_move( m , player ) == true ) )
{

if( notKilledmove( m , player ) == true )
return true ;

return false ;

}

return false ;

}


//logic the first pawn move is allowed to move two squares up just for the first move only
if( Math.abs( row - prow ) == 2 && ( pcol == col ) && pawnfirstMove( m , player ) == true && notKilledmove( m , player ) == true )
{

if( player == 1 && cboard[size - (row+1) - 1][col].getText().equals("") == false )	
return false ;

if( player == 2 && cboard[size - (row-1) - 1][col].getText().equals("") == false )	
return false ;

return true ;

}


return false ;



}

return false ;

}

//add logic to check for En passant move **************************************************************************************************************
//Not yet implemented !!!!!!!!!!!!!!!!!
//TODO implement this En passant method 
private boolean en_passant_move( String m , int player )
{
return false ;

}


//Method that test to see if the piece is the opponents piece or not
private boolean isOpponent( String piece , int player )
{
if( player == 1 && ( piece.equals( king2 ) == true || piece.equals( queen2 ) == true || piece.equals( rook2 ) == true || piece.equals( bishop2 ) == true || piece.equals( knight2 ) == true || piece.equals( pond2 ) == true ) )
return true ;


if( player == 2 && ( piece.equals( king1 ) == true || piece.equals( queen1 ) == true || piece.equals( rook1 ) == true || piece.equals( bishop1 ) == true || piece.equals( knight1 ) == true || piece.equals( pond1 ) == true ) )
return true ;

return false ;

}


//Important Utility method for isValidMove(...) function to test if the state of executing the move puts the players king in check
//If so returns true and isValidMove(...) will not allow any further processing of the move and the move becomes not a valid move
//else it returns false and the isValidMove(...)  will allow further processing of the move to determine if its a valid move
//Very clever method with isValidMove both methods are truely the workhorses of the validation a move at a particular state of the game!!!
private boolean notKilledmove(String m , int player )
{

String tmpboard[][] = new String[size][size] ;
for( int i = 0 ; i < size ; i++ )
for( int j = 0 ; j < size ; j++ )
tmpboard[i][j] = cboard[i][j].getText() ;

movepiece( m , player ) ;

boolean isnotkilled = true ;

String king = getKing(player) ;
int oplayer = getOpponentPlayer( player ) ;
String opponentpieces[] = getPieces( oplayer ) ;

for( int i = 0 ; i < opponentpieces.length ; i++ )
{

if( opponentpieces[i] == null )
break ;

if( isValidMove( opponentpieces[i] + king , oplayer ) == true )
{
isnotkilled = false ;
break ;
}

}

for( int i = 0 ; i < size ; i++ )
for( int j = 0 ; j < size ; j++ )
cboard[i][j].setText( tmpboard[i][j] ) ;


return isnotkilled ;
}



//Method for testing if a given player is in check
public boolean isInCheck( int player )
{

String king = getKing(player) ;
int oplayer = getOpponentPlayer( player ) ;
String opponentpieces[] = getPieces( oplayer ) ;

for( int i = 0 ; i < opponentpieces.length ; i++ )
{

if( opponentpieces[i] == null )
break ;

if( isValidMove( opponentpieces[i] + king , oplayer ) == true )
return true ;

}

return false ;


}

//This function test to see if the players in the game haven't captured any piece in less then 50 moves 
//Or the player hasn't move a pawn in the last 50 moves if either condition is true
//Then the game should end in a draw by the 50 move rule
//Very important rule to make chess game have a finite game tree to enumerate.
//IF WE DID NOT HAVE A DRAW BY 50 LIKE RULE FOR CHESS THEN THERE COULD BE A POSSIBLE INFINITE DEPTH TO THE GAME ENUMERATION TREE
//AT THAT POINT CHESS COULD POSSIBLE HAVE GAMES THAT NEVER END!!!
//THANKFULLY WITH CHECKMATE , STALEMATE , AND THIS DRAW BY 50 RULE THE GAME OF CHESS BECOMES A FINITE MOVE GAME :)
public boolean isDrawBy50()
{
	if( lastpawnmove < 50 || lastcapturedpiece < 50 )
	return false ;
	
	return true ;
	
}

//This function takes in the player and checks if the player has any more moves left
//returns true if no move is possible from player
//returns false if the player has a possible move
//TODO: make more efficient hasNoMoveLeft but it works for now!
public boolean hasNoMoveLeft(int player)
{
	
	String ppiece[] = getPieces( player ) ;
	String tmpmove = null ;
	String ktmpmove = null ;
	for( int i = 0 ; i < ppiece.length ; i++ )
	{
		if( ppiece[i] == null )
		break ;
		
		for( int j = 0 ; j < size ; j++ )
			for( int k = 0 ; k < size ; k++ ) 
		     {  
				tmpmove = ppiece[i] + getColLetter(j) + (k + 1) ;
		        if( isValidMove( tmpmove , player) == true ) 
			    return false ;
		     }
		
	}
	
	tmpmove = "K" + getKing( player ) ;
	for( int j = 0 ; j < size ; j++ )
		for( int k = 0 ; k < size ; k++ ) 
	     {  
			ktmpmove = tmpmove + getColLetter(j) + (k + 1) ;
	        if( isValidMove( ktmpmove , player) == true ) 
		    return false ;
	     }

	return true ;
	
}



//Method that gives players king position at particular instance of the game
//returns the king position without the K so for example Ka3 would be a3 no need for K as we already know where getting the King position
//if we want the K we can always prepend it to the a3 again later
public String getKing( int player )
{

if( player == 1 )
{
for( int i = 0 ; i < size ; i++ )
for( int j = 0 ; j < size ; j++ )
if( cboard[size - i - 1][j].getText().equals( king1 ) == true )
return "" + getColLetter(j) + "" + (i+1) ;

//should never get here something seriously is wrong where is your king !!!
return null ;
}

if( player == 2 )
{
for( int i = 0 ; i < size ; i++ )
for( int j = 0 ; j < size ; j++ )
if( cboard[size - i - 1][j].getText().equals( king2 ) == true )
return "" + getColLetter(j) + "" + (i+1) ;

//should never get here something seriously is wrong where is your king !!!
return null ;

}

//inputted player wrong should be 1 or 2
return null ;


}


//Simple Method to get the opponent player given the current int player
//Only two players in the game of chess either 1 or 2. If the input is other then that this method will return -1
//indicating you have serious player issues dude :) 
public int getOpponentPlayer( int player )
{
if( player == 1 )
return 2 ;

if( player == 2 )
return 1 ;

return -1 ;

}


//Method that gets a given players pieces and returns them in a string array
//Note the array is size 16 because there is 16 pieces max for each player ever on the board
//But during the game most pieces get captured eventually so its far less then the complete size 16 starting board.
//However a null value means you reached the end of any more pieces in the array
//So just because the array is size 16 one should for loop to the first null value to get the proper number of pieces
//if your objective is to know exactly how many piece a player has
//each array element that is a valid piece on the board will be a string of the form Qe4 or Ra5 for example
//aka there the piece letter followed by the square they occupy

//Also note this method does not get king pieces for the player one should uses getKing(...) method to retrieve the king
//But space in the piece array return by this function gives a space for adding the king to the array
//You may be asking yourself why i didnt just allocate 15 space or uses a vector to dynamically get the exact number of squares to uses for the array
//reason is the few extra square arent much and the overhead of using Vector class would be over kill and internally would probably allocate a bigger size internal array
//anyway so i choose to just keep an array of size 16 which holds the exact amount of piece you start the game off with and is the max amount you can have on the board at
//any time  
public String[] getPieces( int player )
{

String pieces[] = new String[16] ;
int k = 0 ;

if( player == 1 )
{
for( int i = 0 ; i < size ; i++ )
for( int j = 0 ; j < size ; j++ )
{ 

if( cboard[size - i - 1][j].getText().equals( queen1 ) == true )
pieces[k++] = "Q" + getColLetter(j) + "" + (i+1) ;

if( cboard[size - i - 1][j].getText().equals( rook1 ) == true )
pieces[k++] = "R" + getColLetter(j) + "" + (i+1) ;

if( cboard[size - i - 1][j].getText().equals( bishop1 ) == true )
pieces[k++] = "B" + getColLetter(j) + "" + (i+1) ;

if( cboard[size - i - 1][j].getText().equals( knight1 ) == true )
pieces[k++] = "N" + getColLetter(j) + "" + (i+1) ;

if( cboard[size - i - 1][j].getText().equals( pond1 ) == true )
pieces[k++] = "P" + getColLetter(j) + "" + (i+1) ;

}

return pieces ;

}

if( player == 2 )
{
for( int i = 0 ; i < size ; i++ )
for( int j = 0 ; j < size ; j++ )
{ 

if( cboard[size - i - 1][j].getText().equals( queen2 ) == true )
pieces[k++] = "Q" + getColLetter(j) + "" + (i+1) ;

if( cboard[size - i - 1][j].getText().equals( rook2 ) == true )
pieces[k++] = "R" + getColLetter(j) + "" + (i+1) ;

if( cboard[size - i - 1][j].getText().equals( bishop2 ) == true )
pieces[k++] = "B" + getColLetter(j) + "" + (i+1) ;

if( cboard[size - i - 1][j].getText().equals( knight2 ) == true )
pieces[k++] = "N" + getColLetter(j) + "" + (i+1) ;

if( cboard[size - i - 1][j].getText().equals( pond2 ) == true )
pieces[k++] = "P" + getColLetter(j) + "" + (i+1) ;

}

return pieces ;


}

pieces=null ;
return null ;

}


//This function is a utility function uses to check if there is any pieces of opponent blocking the moving path of a given piece to its destination square
//Mostly for rook's , bishop's , and queen piece/moves 
//knights moves cannt really be block like a rook , bishop , or queen can be
//pawns and kings only move one square at a time usually so there is no path function need for these pieces
//Important this function doesnt check the last square aka (col,row) where your moving to if there is
//a piece occupying it !!! it only check the square leading up to your destination (col,row) square
//The square itself your moving to is check in the isValidMove(...) by isOpponent(...) and cboard[size - prow - 1][i].getText().equals("") to see if the square has an enemy or is empty
private boolean hasPathToSquare( char piece , int pcol , int prow , int col , int row ) 
{

//for rook path testing 
if( piece == 'R' )
{

     if( prow == row && pcol < col )
     {

       for( int i = pcol+1 ; i < col ; i++ )
       {
         if( cboard[size - prow - 1][i].getText().equals("") == false )
         return false ;

       }

      return true ;

     }


     if( prow == row && pcol > col )
     {

        for( int i = pcol-1 ; i > col ; i-- )
        {
          if( cboard[size - prow - 1][i].getText().equals("") == false )
          return false ;

        }

       return true ;

     }



     if( prow < row && pcol == col )
     {

         for( int i = prow+1 ; i < row ; i++ )
         {
           if( cboard[size - i - 1][pcol].getText().equals("") == false )
           return false ;

         }

         return true ;

      }

     
     if( prow > row && pcol == col )
     {

         for( int i = prow-1 ; i > row ; i-- )
         {
           if( cboard[size - i - 1][pcol].getText().equals("") == false )
           return false ;

         }

         return true ;

      }

return false ;

}

//for bishop path testing
if( piece == 'B' )
{

    if( pcol < col && prow < row )
    {
      int tmpcol = pcol + 1 ;
      for( int i =  prow+1 ; i < row && tmpcol < col ; i++ )
      {
        if( cboard[size - i - 1][tmpcol++].getText().equals("") == false )
        return false ;
      }

     return true ;

    }

    if( pcol > col && prow < row )
    {
       int irow = prow + 1 ;
       for( int i =  pcol-1 ; i > col && irow < row ; i-- )
       {
         if( cboard[size - irow++ - 1][i].getText().equals("") == false )
         return false ;
       }

     return true ;

    }


    if( pcol > col && prow > row )
    {
       int temprow = prow - 1 ;
       for( int i =  pcol-1 ; i > col && temprow > row ; i-- )
       {
         if( cboard[size - (temprow--) - 1][i].getText().equals("") == false )
         return false ;
       }

     return true ;

    }


    if( pcol < col && prow > row )
    {

       int temprow = prow - 1 ;
       for( int i =  pcol+1 ; i < col && temprow > row ; i++ )
       { System.out.println("pcol = " + pcol + " prow = " + prow + " col = " + col + " row = " + row + " i = " + i + " irow = " + temprow ) ;
         if( cboard[size - (temprow--) - 1][i].getText().equals("") == false )
         return false ;
       }

     return true ;

    }

    
return false ;
   

}

//for queen path testing
if( piece == 'Q' )
{
      //queen = a bishop and a rooks moves so kind of cool you call hasPathToSquare again to look if any bishop or rook paths are possible
      // if not then the queen doesnt have a path either !
	  if( pcol == col || prow == row )
	  {
		  if(hasPathToSquare( 'R' , pcol , prow , col , row ) == false)
	      return false ;
		  
		  return true ;
	  }
      
	  if( ( Math.abs( col - pcol ) / Math.abs( row - prow ) == 1 && Math.abs( col - pcol ) % Math.abs( row - prow ) == 0 ) )
	  {
		  if( hasPathToSquare( 'B' , pcol , prow , col , row ) == false )
	      return false ;

          return true ;
	  }
	  
	  return false ;

}

// if you got here that means you where not a rook , bishop or queen piece and called this function
// rethink why your calling this function if its not a rook , bishop or queen chances are your pgn string is wrong invalid character piece or something in it!
return false ;

}



//Method to beable to set any board square with any piece
//Mostly to set a board configuration up in a nonstandard way for analysis
//Not used in traditional chess game only in the case you know the board configuration to start off at yet not the 
//PGN moves to get you to that configuration.
//The main way is to step thru the PGN moves back to where you left of thru calling move(...) with the png moves repeatedly
public boolean setBoardSquare( String square , String piece )
{
	
	int col  = -1 ;
	int row  = -1 ;
	col = getColNum( square.charAt(0) );
	row = getRowNum( square.charAt(1) );

	//logic to check the square is a valid board squares and the piece is a valid board piece for either player 1 or 2
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	if( col < 0 || col > 7)
	return false ;
		
	if( row < 0 || row > 7)
	return false ;
	
	if( piece != null && isOpponent( piece , 1 ) == false && isOpponent( piece , 2 ) == false )
	return false ;
		
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	if( piece == null )
	{
		cboard[size - row - 1][col].setText("");
	    return true ;
	}
	
	cboard[size - row - 1][col].setText(piece);
	return true ;
	
}


//Simple Utility Method for clearing the chess board.
//Not need for the game but might be need to setup board for mathematical analysis
//In conjunction with setBoardSquare(...) to set a specific piece at a specific square
public void clearBoard()
{
	for( int i = 0 ; i < size ; i++ )
		for( int j = 0 ; j < size ; j++ )
		{
		 cboard[size - i - 1][j].setText("");
		}
			
}


//Hold the ply's / moves of the entire chess game being played!!!!
private static Vector<String> gamemoves = new Vector<String>() ;

//Print all the possible moves a player has at a given instance of the game
public void allPossibleMoves(int player) 
{
	String ppiece[] = getPieces( player ) ;
	String tmpmove = null ;
	String ktmpmove = null ;
	StringBuffer allmovesbuf = new StringBuffer(2000) ;
	int totalmovecount = 0 ;
	for( int i = 0 ; i < ppiece.length ; i++ )
	{
		if( ppiece[i] == null )
		break ;
		
		for( int j = 0 ; j < size ; j++ )
			for( int k = 0 ; k < size ; k++ ) 
		     {  
				tmpmove = ppiece[i] + getColLetter(j) + (k + 1) ;
		        if( isValidMove( tmpmove , player) == true ) 
		        {
		        allmovesbuf.append(tmpmove) ;
		        allmovesbuf.append("  ") ;
		        totalmovecount++ ;
		        }
		     }
		
	}
	
	tmpmove = "K" + getKing( player ) ;
	for( int j = 0 ; j < size ; j++ )
		for( int k = 0 ; k < size ; k++ ) 
	     {  
			ktmpmove = tmpmove + getColLetter(j) + (k + 1) ;
	        if( isValidMove( ktmpmove , player) == true ) 
		    {
	        	allmovesbuf.append(ktmpmove) ;
		        allmovesbuf.append("  ") ;
		        totalmovecount++ ;
		    }
	     }
    
	allmovesbuf.append( "total amount of possible moves is " + totalmovecount ) ;
	System.out.println( allmovesbuf ) ;
    return ;
	
	
}



public static void main(String args[] )
{
//Create the chess game and gui and show it
ReplayChess r = new ReplayChess() ;

//Console Input Terminal and Chess game
//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
 // Using Console to input chess moves from user a simple terminal input method that displays on the chessboard GUI 
//Please used pgn long notation if you dont understand the notation it is the letter of the piece pawn = 'P' , knight = 'N' ....
//followed by the square source square followed by the destination square
String pmove = null ;
int i = 2 ;
int movesuccess = -1 ;
boolean nomovesleft = false ;
Scanner scanner = new Scanner(System.in);

System.out.println( "Welcome to the Game of Chess$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$") ;
System.out.println( "Make a move bottom player on GUI is player 1 and goes first ") ;
System.out.println( "Moves need to be in chess algebraic long notation aka pgn modified format") ;
System.out.println( "If you have any problems with the notation contact the coder :) ") ;
System.out.println( "Moves:");

//Used for dumping the particular game moves before program exits / JVM gets killed or any other exiting of the program
//Mostly for replaying , saving , analysizing or sharing games 
Runtime.getRuntime().addShutdownHook(new ChessMessage()) ;

/***************************************************************************************************
r.clearBoard() ;
//the game state with the most number of possible moves for a player which is 216
//pmove = scanner.nextLine() ;
r.setBoardSquare("a8", rook2);
r.setBoardSquare("a3", queen2);
r.setBoardSquare("a2", pond1);
r.setBoardSquare("a1", king1);
r.setBoardSquare("b6", queen2);
r.setBoardSquare("b2", pond1);
r.setBoardSquare("b1", bishop2);
r.setBoardSquare("c4", queen2);
r.setBoardSquare("c1", knight2);
r.setBoardSquare("d7", queen2);
r.setBoardSquare("d2", queen2);
r.setBoardSquare("d1", knight2);
r.setBoardSquare("e5", queen2);
r.setBoardSquare("e1", king2);
r.setBoardSquare("f3", queen2);
r.setBoardSquare("g6", queen2);
r.setBoardSquare("g1", bishop2);
r.setBoardSquare("h8", rook2);
r.setBoardSquare("h4", queen2);
r.allPossibleMoves(2) ;
pmove = scanner.nextLine() ;
System.out.println("Is in check " + r.isInCheck(1) + " Has no moves left " + r.hasNoMoveLeft(1)) ;

r.clearBoard() ;
//pmove = scanner.nextLine() ;
r.setBoardSquare("a2", pond2);
r.setBoardSquare("b5", king2);
r.setBoardSquare("b1", king1);
r.setBoardSquare("c7", queen2);
r.setBoardSquare("d5", bishop2);
r.setBoardSquare("d4", bishop2);
r.setBoardSquare("d3", knight2);
r.setBoardSquare("e8", rook2);
r.setBoardSquare("f5", knight2);
r.setBoardSquare("g6", rook2);
r.setBoardSquare("h5", pond2);
r.setBoardSquare("h2", pond2);
r.allPossibleMoves(2) ;
pmove = scanner.nextLine() ;
System.out.println("Is in check " + r.isInCheck(1) + " Has no moves left " + r.hasNoMoveLeft(1)) ;

*********************************************************************************************************/
//r.clearBoard();
//r.setBoardByFEN( "8/8/k7/8/8/8/6K1/8" ) ; // "2R5/PK1Pkpp1/4p2N/4p1Rp/4B3/q7/3p1B2/8" ) ; // b KQkq - 1 2" ) ;
//r.getBoardFEN();

//r.allPossibleMoves(1) ;

while( true )
{
        	
        pmove = scanner.nextLine() ;
        
	    movesuccess = r.move( pmove , i ) ;
        
        if( i == 2 &&  movesuccess != 0 )
	    System.out.println( "Player " + (i-1) + " BAD MOVE TRY RE-ENTERING THE PGN NOTATION FOR THE MOVE " + pmove ) ;

        if( i == 1 && movesuccess != 0 )
        System.out.println( "Player " + (i+1) + " BAD MOVE TRY RE-ENTERING THE PGN NOTATION FOR THE MOVE " + pmove ) ;
        
        if( i == 1 && movesuccess == 0 )
        {
        	gamemoves.addElement(pmove) ;
        	i = 2 ;
        }
        else if( i == 2 && movesuccess == 0 )
        {
        	i-- ;
        	gamemoves.addElement(pmove) ;
        }

        nomovesleft = r.hasNoMoveLeft(i) ;
        
        if( movesuccess == 0 && ( r.isInCheck(i) == false && nomovesleft == true ) )
        {
        	System.out.println("End of Game: stalemate tie  1/2-1/2 ") ;
        	scanner.close();
        	//printMoves(gamemoves) ;
        	System.exit(0);
        	
        }
        	
        if( movesuccess == 0 && ( r.isInCheck(i) == true && nomovesleft == true ) )
        {
        	System.out.println("End of Game: player " + r.getOpponentPlayer(i) + "win") ;
        	scanner.close();
        	//printMoves(gamemoves) ;
        	System.exit(0);
        	
        }
        
        if( movesuccess == 0 && r.isDrawBy50() == true )
        {
        	System.out.println("End of Game: players draw by 50 move rule 1/2-1/2 " ) ;
        	scanner.close();
        	//printMoves(gamemoves) ;
        	System.exit(0);
        	
        }
        
        
        
}


//End of Console Input Terminal and Chess game
//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$


//TODO: Possible implement Draw by insufficient material        
//TODO: Possible implement Draw by threefold repetition
//TODO: Possible implement Draw by Perpetual check        
        
}


//Simple print method to dump all the game moves to the console
//Can be used to copy the pgn moves to play over again or replay a game or to save game...etc
//With a little tweaking they should also be portable to export to online or other chess programs for analysis
public static void printMoves( Vector<String>moves )
{
	System.out.println() ;
	System.out.println("DUMPED GAME MOVES BELOW FOR SAVING,REPLAYING, OR ANALYSIS OF GAME PURPOSES") ;
	for( int i = 0 ; i < moves.size() ; i++ )
		System.out.print(moves.elementAt(i) + " ") ;
	
	System.out.println();
	return ;
	
}


//Used to dump moves to the terminal before the program exists
//This can be useful if you stop the game by ctrl+c or other signals 
//In the middle of a game you can then copy the console pgn moves and replay or restart the game at a particular point.
static class ChessMessage extends Thread {

   public void run() {
	   printMoves(gamemoves) ;
   }
}


//Not really used shutdownhook used instead kept here for the possibility of needing it
//for cleaning up netplay based chess games when the time comes doesnt really do anything significant
//for clean up now but in the future when many people are playing many games online thru sockets 
//may be a need for keeping resources really clean but shutdownhook seems to be the main way anyway
//Ok all stop my rambling :)
protected void finalize() throws Throwable
{
	try {
	printMoves(gamemoves) ;
	gamemoves = null ;
	cboard = null ;
	}
	finally {
	super.finalize();
	}
	
}

//Method that allows you to set the board to a given configuration by its fen notation
//Is easier and most of the time a lot less lines of code to set it this way then with setBoardSquare(...) method
//But if one doesnt understand fen notation the setBoardSquare(...) method is always a way for anybody to set the board configuration!!!
public void setBoardByFEN( String fen )
{
   	String a[] = fen.split("(/|\\s)" ) ; 
	for( int i = 0 ; i < a.length ; i++ )
   	{
	     setrow( a[i] , i ) ;
   	}
	//"nrbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2"
	
	
	
}

//This helper method sets the pieces on the given row on the board by its fen string for that given row!!!
//Takes String fenrow and the int row to set  
private void setrow( String fenrow , int row ) 
{
//System.out.println(fenrow) ;


String tmpfenrow = fenrow.replaceAll( "8" ,"xxxxxxxx" ) ;
tmpfenrow = tmpfenrow.replaceAll( "7" ,"xxxxxxx"  ) ;
tmpfenrow = tmpfenrow.replaceAll( "6" , "xxxxxx" ) ;
tmpfenrow = tmpfenrow.replaceAll( "5" , "xxxxx" ) ;
tmpfenrow = tmpfenrow.replaceAll( "4" , "xxxx" ) ;
tmpfenrow = tmpfenrow.replaceAll( "3" , "xxx" ) ;
tmpfenrow = tmpfenrow.replaceAll( "2" ,"xx" ) ;
tmpfenrow = tmpfenrow.replaceAll( "1" , "x" ) ;

//System.out.println(tmpfenrow) ;

for( int i = 0 ; i < tmpfenrow.length() ; i++ )
{
	
    if( tmpfenrow.charAt(i) == 'x' )	
	cboard[row ][i].setText("") ;
	

	if( tmpfenrow.charAt(i) == 'k' )			
		cboard[row ][i].setText(king1) ;
	if( tmpfenrow.charAt(i) == 'q' )
		cboard[row ][i].setText(queen1) ;	
	if( tmpfenrow.charAt(i) == 'b' )
		cboard[row ][i].setText(bishop1) ;			
	if( tmpfenrow.charAt(i) == 'n' )
		cboard[row ][i].setText(knight1) ;
	if( tmpfenrow.charAt(i) == 'r' )
		cboard[row ][i].setText(rook1) ;	
	if( tmpfenrow.charAt(i) == 'p' )
		cboard[row ][i].setText(pond1) ;		
	

	
	if( tmpfenrow.charAt(i) == 'K' )			
		cboard[row ][i].setText(king2) ;
	if( tmpfenrow.charAt(i) == 'Q' )
		cboard[row ][i].setText(queen2) ;	
	if( tmpfenrow.charAt(i) == 'B' )
		cboard[row ][i].setText(bishop2) ;			
	if( tmpfenrow.charAt(i) == 'N' )
		cboard[row ][i].setText(knight2) ;
	if( tmpfenrow.charAt(i) == 'R' )
		cboard[row ][i].setText(rook2) ;	
	if( tmpfenrow.charAt(i) == 'P' )
		cboard[row ][i].setText(pond2) ;	

	
	
}

//System.out.println(fenrow) ;
//System.out.println(tmpfenrow) ;


}

//This method get the FEN notation for the given board configuration
//returns the fen the board is currently in
// The website at http://bernd.bplaced.net/fengenerator/fengenerator.html 
//gives you random fen strings and a look at the board good site to uses AI/Machine learning chess engine analysis with
//As well as writting fen string parsers such as contained in this code :)
public void getBoardFEN()
{
	
	String tempfen = "" ;
	for( int i = 0 ; i < size ; i++ )
	{
		
		
		for( int j = 0 ; j < size ; j++ )
	    {
			if( cboard[i][j].getText().equals( "" ) == true )
				tempfen += "x" ;
			
			if( cboard[i][j].getText().equals( king1 ) == true )			
				tempfen += "k" ;
			if( cboard[i][j].getText().equals( queen1 ) == true )			
				tempfen += "q" ;	
			if( cboard[i][j].getText().equals( bishop1 ) == true )			
				tempfen += "b" ;			
			if( cboard[i][j].getText().equals( knight1 ) == true )			
				tempfen += "n" ;
			if( cboard[i][j].getText().equals( rook1 ) == true )			
				tempfen += "r" ;	
			if( cboard[i][j].getText().equals( pond1 ) == true )			
				tempfen += "p" ;		
			
		

			
			if( cboard[i][j].getText().equals( king2 ) == true )			
				tempfen += "K" ;
			if( cboard[i][j].getText().equals( queen2 ) == true )			
				tempfen += "Q" ;	
			if( cboard[i][j].getText().equals( bishop2 ) == true )			
				tempfen += "B" ;			
			if( cboard[i][j].getText().equals( knight2 ) == true )			
				tempfen += "N" ;
			if( cboard[i][j].getText().equals( rook2 ) == true )			
				tempfen += "R" ;	
			if( cboard[i][j].getText().equals( pond2 ) == true )			
				tempfen += "P" ;		

			
		}
		
		if( i  != 7)
		tempfen += "/" ;
			
	}
	
	//System.out.println( tempfen ) ;
	
	tempfen = tempfen.replaceAll("xxxxxxxx", "8") ;
	tempfen = tempfen.replaceAll("xxxxxxx", "7") ;
	tempfen = tempfen.replaceAll("xxxxxx", "6") ;
	tempfen = tempfen.replaceAll("xxxxx", "5") ;
	tempfen = tempfen.replaceAll("xxxx", "4") ;
	tempfen = tempfen.replaceAll("xxx", "3") ;
	tempfen = tempfen.replaceAll("xx", "2") ;
	tempfen = tempfen.replaceAll("x", "1") ;
	
	//System.out.println( tempfen ) ;
	
}


}
