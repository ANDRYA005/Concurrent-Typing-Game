
public class WordRecord {
	private String text;
	private  int x;
	private int y;
	private int maxY;
	private boolean dropped;
	public boolean exceeded=false;

	private int fallingSpeed;
	private static int maxWait=1500;
	private static int minWait=100;

	public static WordDictionary dict;



	WordRecord() {
		text="";
		x=0;
		y=0;
		maxY=300;
		dropped=false;
		fallingSpeed=(int)((WordApp.score.getTotal()+1) * Math.random() * (maxWait-minWait)+minWait);
	}

	WordRecord(String text) {
		this();
		this.text=text;
	}

	WordRecord(String text,int x, int maxY) {
		this(text);
		this.x=x;
		this.maxY=maxY;
	}

// all getters and setters must be synchronized
	public synchronized  void setY(int y) {
		if (y>maxY) {
			y=maxY;
			dropped=true;
		}
		this.y=y;
	}

	public synchronized  void setX(int x) {
		this.x=x;
	}

	public synchronized  void setWord(String text) {
		this.text=text;
	}

	public synchronized  String getWord() {
		return text;
	}

	public synchronized  int getX() {
		return x;
	}

	public synchronized  int getY() {
		return y;
	}

	public synchronized  int getSpeed() {
		return fallingSpeed;
	}

	public synchronized void setPos(int x, int y) {
		setY(y);
		setX(x);
	}
	public synchronized void resetPos() {
		setY(0);
	}

	public synchronized void resetWord() {
		if (exceeded==true){
			text="";
		}
		else{
			resetPos();
			text=dict.getNewWord();
			dropped=false;
			fallingSpeed=(int)(Math.random() * (maxWait-minWait)+minWait);
		}

			//System.out.println(getWord() + " falling speed = " + getSpeed());
	}

	public synchronized boolean matchWord(String typedText) {
		//System.out.println("Matching against: "+text);
		if (typedText.equals(this.text)) {
				synchronized(this) {WordApp.wordCounter++;}
				if (exceededTest()){
					exceeded = true;
				}
				else{
					exceeded = false;
				}
				resetWord();
				return true;
		}
		else
			return false;
	}


	public synchronized  void drop(int inc) {
		setY(y+inc);
	}

	public synchronized  boolean dropped() {
		return dropped;
	}

	public boolean exceededTest(){
		// System.out.println("Counter= "+WordApp.wordCounter);
		// System.out.println("Total= "+(WordApp.totalWords));
		// System.out.println("TotalScore= "+(WordApp.score.getTotal()+1));
		if (WordApp.wordCounter>=WordApp.totalWords){
			// System.out.println("Exceeded!");
			if (WordApp.score.getTotal()+1 ==WordApp.totalWords){
				WordApp.finished=true;
			}
			return true;
		}
		else{
			// System.out.println("Not Exceeded!");
			return false;
		}
	}

}
