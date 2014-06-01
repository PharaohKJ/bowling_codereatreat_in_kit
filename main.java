public class main
{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Score score = new Score();
		score.ballThrow(10);
		score.ballThrow(1);
		score.ballThrow(2);
		score.ballThrow(3);
		score.ballThrow(4);
		score.ballThrow(4);
		score.ballThrow(3);
		score.ballThrow(2);
		score.ballThrow(1);
		score.ballThrow(0);
		score.ballThrow(8);
		score.ballThrow(0);
		score.ballThrow(4);
		score.ballThrow(5);
		score.ballThrow(5);
		score.ballThrow(5);
		score.ballThrow(4);
		score.ballThrow(10);
		score.ballThrow(10);
		score.ballThrow(10);

		int point = score.score(true);
		
		System.out.println(point + "point!");
		
	}

}

class BallThrow
{
	int pin = 0;
	boolean waiting = false;
	public void setPin(int x){ waiting = true; pin = x;};
	public void got(int x){ setPin(x);};
	public boolean isWaiting(){ return waiting;};
	public int pin(){ return pin;};
	public int point(){ return pin();};
	public boolean isStrike(){if (pin==10) return true; return false;};
}

class Frame 
{
	public static enum Type
	{
		Spare, Strike, Normal
	}
	
	BallThrow ballThrows[] = new BallThrow[3];
	Frame nextFrame;
	int currentBallThrow = 0;
	boolean isLast = false;
	
	public void setLast(boolean v)
	{
		isLast = v;
	}
	public Frame()
	{
		ballThrows[0] = new BallThrow();
		ballThrows[1] = new BallThrow();
		ballThrows[2] = new BallThrow();
		nextFrame = null;
	}
	protected BallThrow first(){ return ballThrows[0];};
	protected BallThrow second(){ return ballThrows[1];};
	protected BallThrow third(){ return ballThrows[2];};
	public void setNext(Frame n)
	{
		nextFrame = n;
	}
	
	public String resultString()
	{
		String out;
		if (this.isStrike()) {
			out = "|X|-|";
		} else {
			out = "|"+ this.first().point() +"|"+ this.second().point() +"|";
		}
		if (this.isLast) {
			out += this.third().point()+"|";
		} else {
			out += ".|";			
		}
		
		out += "  ->  " + this.point();
		
		return out;
	}
	
	public int strikeBonus()
	{
		int out  = 0;
		if ( this.isStrike() ) {		
			if ( !this.isLast) {			
				if (nextFrame.isStrike()) {
					out = 20;
				}
				out = nextFrame.originalPoint();
			}
		}
		return out;
	}
	
	public int spareBonus()
	{
		int out = 0;
		
		if ( this.isStrike() )
			return out;
		
		if ( this.isSpare() == false)
			return out;
		
		return nextFrame.first().point();
	}
	
	public int originalPoint()
	{
		return this.first().point() + this.second().point() + this.third().point();
	}
	
	public int point()
	{
		return this.originalPoint() + spareBonus() + strikeBonus();
	}
	
	public void gotPin(int inPin)
	{
		this.current().got(inPin);
		currentBallThrow++;
	}
	
	public boolean isEnd()
	{
		if (isLast) {
			if ( currentBallThrow == 3) return true;
		} else {
			if ( this.isStrike()) return true;
			if ( currentBallThrow == 2) return true;
		}
		return false;
	}
	
	public BallThrow current()
	{
		return ballThrows[currentBallThrow];
	}
	
	public boolean isStrike()
	{
		if ( ballThrows[0].pin() == 10 ) return true;
		return false;
	}
	
	public boolean isSpare()
	{
		if ( this.isStrike() == false) {
			if ( (first().pin() + second().pin()) == 10) return true;
		}
		return false;		
	}
}

class Score
{
	Frame frames[] = new Frame[10];
	int currentGame = 0;

	public void start(){
	}
	
	public Score()
	{
		for(int i=0; i<10; i++)
			frames[i] = new Frame();
		
		for( int i=0; i<9; i++)
			frames[i].setNext(frames[i+1]);
		
		frames[9].setLast(true);
	}
	
	public boolean isLast()
	{
		if ( currentGame == 9)
			return true;
		
		return false;
	}
	
	public Frame current()
	{
		return frames[currentGame];
	}
	
	public void ballThrow(int gotPin)
	{
		if (this.isLast() && this.current().isEnd()) return;
		if ( this.current().isEnd()) currentGame++;
		this.current().gotPin(gotPin);		
	}
	
	public int score(boolean p)
	{
		int out = 0;
		for(Frame game : frames) {
			if (p) System.out.println( game.resultString() );
			out += game.point();
		}		
		return out;
	}
}

