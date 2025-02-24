import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Scanner;

import iut.algo.Clavier;

public class JeuxMemory
{
	private char[] tabSymbole;
	private int TAILLE = 16;
	private Carte[][] plateau;

	public JeuxMemory()
	{
		this.tabSymbole = new char[TAILLE];
		this.plateau = new Carte[4][4];
		initialiserTableau();
		melangerCarte();
	}

	private void initialiserTableau()
	{
		char symbole = 'A';
		for (int cpt = 0; cpt < this.tabSymbole.length; cpt += 2)
		{
			this.tabSymbole[cpt] = symbole;
			this.tabSymbole[cpt + 1] = symbole;
			symbole++;
		}
	}

	public void melangerCarte()
	{
		int hasard;
		char temp;
		int cptMelange;
		cptMelange = 0;
		hasard = 0;
		// Mélange les symboles dans tabSymbole
		while (cptMelange < 1000)
		{

			for (int cpt = 0; cpt < this.tabSymbole.length; cpt++)
			{
				hasard = (int) (Math.random() * TAILLE);
				temp = this.tabSymbole[cpt];
				this.tabSymbole[cpt] = this.tabSymbole[hasard];
				this.tabSymbole[hasard] = temp;
			}
			cptMelange++;
		}

		// Remplit le plateau avec des cartes mélangées
		cptMelange = 0;
		for (int lig = 0; lig < this.plateau.length; lig++)
		{
			for (int col = 0; col < this.plateau[lig].length; col++)
			{
				this.plateau[lig][col] = new Carte(cptMelange, this.tabSymbole[cptMelange], false);
				cptMelange++;
			}
		}
	}

	public String toString()
	{
		/* ----------- */
		/* Données */
		/* ----------- */
		String sRet = "";
		String ligneHori = "";

		/* ------------ */
		/* Instructions */
		/* ------------ */

		/*----------------------------------*/
		/* Formatage de l'entête avec numéros de colonnes */
		/*----------------------------------*/
		sRet += "    "; // Espace initial pour aligner les numéros de colonnes
		for (int nbCol = 0; nbCol < this.plateau[0].length; nbCol++)
		{
			sRet += String.format(" %-2d ", nbCol + 1); // Colonnes commencent à
														// 1
		}
		sRet += "\n";

		/*----------------------------------*/
		/* Formatage de la ligne du dessus. */
		/*----------------------------------*/
		ligneHori += "   +";
		for (int nbCol = 0; nbCol < this.plateau[0].length; nbCol++)
		{
			ligneHori += "---+";
		}
		sRet += ligneHori + "\n";

		/*---------------------------------------------*/
		/* Formate le tableau en entier jusqu'à la fin */
		/*---------------------------------------------*/
		for (int cptLig = 0; cptLig < this.plateau.length; cptLig++)
		{
			sRet += String.format("%-3d|", cptLig + 1); // Lignes commencent à 1

			for (int cptCol = 0; cptCol < this.plateau[0].length; cptCol++)
			{
				// Utilisation d'un if pour vérifier si la carte est visible
				if (this.plateau[cptLig][cptCol].getEstVisible())
				{
					sRet += String.format(" %-2s|", this.plateau[cptLig][cptCol].getSymbole());
				}
				else
				{
					sRet += String.format(" %-2s|", "*");
				}
			}

			sRet += "\n" + ligneHori + "\n"; // Ajouter la ligne horizontale
												// après chaque ligne
		}

		return sRet;
	}

	public void estPair(int lig, int col, int lig2, int col2)
	{
		if (lig != lig2 || col2 != col)
		{
			if(this.plateau[lig][col].getSymbole() == this.plateau[lig2][col2].getSymbole())
			{
				this.plateau[lig][col].setEstVisible(true);
				this.plateau[lig2][col2].setEstVisible(true);
			}
			else
			{
				try {Thread.sleep(5000);} catch (Exception e) {}
				System.out.println(toString());
				this.plateau[lig][col].setEstVisible(false);
				this.plateau[lig2][col2].setEstVisible(false);
			}
			
		}
	}

	public void retournerCarte(int lig, int col)
	{
		this.plateau[lig][col].setEstVisible(true);
	}

	public boolean aGagne()
	{
		int choix;
		for (int lig = 0; lig < plateau.length; lig++)
		{
			for (int col = 0; col < plateau[lig].length; col++)
			{
				if (!plateau[lig][col].getEstVisible())
				{
					return false;
				}
			}
			choix = Clavier.lire_int();
			if(choix == 1)
			{
				return true;
			}
			if(choix == 2)
			{
				jouer();
				return false;
			}
		}
		return true;
	}

	public void jouer()
	{
		int lig;
		int col;
		int lig2;
		int col2;
		int choix;
		char save;
		
		System.out.println(" 1 : Commencer une partie. \n 2 : Charger une partie. \n4 3 : Quitter.");
		choix = Clavier.lire_int();

		if (choix == 1)
		{
			melangerCarte(); // Mélanger les cartes et initialiser le plateau
		}
		if (choix == 2)
		{
			charger();
		}
		while (!aGagne())
		{
			System.out.println(toString()); // Afficher le plateau
			System.out.println("Entrer une ligne");
			lig = Clavier.lire_int() - 1;
			System.out.println("Entrer une colonne");
			col = Clavier.lire_int() - 1;
			retournerCarte(lig, col);

			System.out.println(toString()); // Afficher le plateau

			System.out.println("Entrer une ligne");
			lig2 = Clavier.lire_int() - 1;
			System.out.println("Entrer une colonne");
			col2 = Clavier.lire_int() - 1;
			retournerCarte(lig2, col2);
			System.out.println(toString()); // Afficher le plateau
			estPair(lig, col, lig2, col2);
			System.out.println("Voulez vous sauvegarder? (O/N)");
			save = Clavier.lire_char();
			if (save == 'O')
			{
				sauvegarder();
			}
			System.out.println(toString()); // Afficher le plateau
		}

	}

	public void sauvegarder()
	{
		try
		{
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("jeux.txt"), "UTF8" ));

			for (int cptLig = 0; cptLig < this.plateau.length; cptLig++)
			{
				for (int cptCol = 0; cptCol < this.plateau[0].length; cptCol++)
				{
					System.out.println(this.plateau[cptLig][cptCol]);
					pw.println(this.plateau[cptLig][cptCol].getId() + ","+ this.plateau[cptLig][cptCol].getSymbole() + "," + this.plateau[cptLig][cptCol].getEstVisible());
				}
			}
			pw.close();
		}
	
		catch (Exception e){ e.printStackTrace(); }
	}

	public void charger()
	{
		String carte;
		char symbole;
		int id;
		boolean estVisible;

		try
		{
			Scanner sc = new Scanner(new FileInputStream("jeux.txt"));
			id = 0;

			while (sc.hasNextLine())
			{
				for (int cptLig = 0; cptLig < this.plateau.length; cptLig++)
				{
					for (int cptCol = 0; cptCol < this.plateau[0].length; cptCol++)
					{
						// Lire une ligne du fichier
						carte = sc.nextLine();

						// Extraire les informations depuis la chaîne
						id = Integer.parseInt(carte.split(",")[0]); // Premier
																	// élément :
																	// l'id
						symbole = carte.split(",")[1].charAt(0); // Deuxième
																	// élément :
																	// le
																	// symbole
						estVisible = Boolean.parseBoolean(carte.split(",")[2]); // Troisième
																				// élément
																				// :
																				// le
																				// boolean

						System.out.println(id + "," + symbole + "," + estVisible);
						// Créer une nouvelle carte et l'ajouter au plateau
						this.plateau[cptLig][cptCol] = new Carte(id, symbole, estVisible);
					}
				}
			}

			sc.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		JeuxMemory jeu = new JeuxMemory();
		jeu.jouer();
	}

}