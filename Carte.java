public class Carte {

	private int id;
	private char symbole;
	private boolean estVisible;

	public Carte(int id, char symbole, boolean estVisible)
	{
		this.id         = id         ;
		this.symbole    = symbole    ;
		this.estVisible = estVisible;

	}
	
	public int getId( )
	{
		return this.id;
	}
	public char getSymbole()
	{
		return this.symbole;
	}

	public boolean getEstVisible ()
	{
		return this.estVisible;
	}

	public void setEstVisible(boolean estVisible)
	{
		this.estVisible = estVisible;
	}

}