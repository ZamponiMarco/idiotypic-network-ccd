# INTRODUZIONE
# DOMINIO
# MODELLO
    Come siamo passati dal dominio alla simulazione
        Parisi, come abbiamo tradotto in programmazione ad agenti, spiegare determinate scelte  ...
        
# IMPLEMENTAZIONE   
    ## ALTO LIVELLO
        ### AGENTE ESTERNI
        ### SISTEMA IMMUNITARIO
        ### ANTIGENI
    ## BASSO LIVELLO 
        Immune System come environment del basso livello
        ## ANTICORPI

# SIMULAZIONE 
# CONCLUSIONI
# ULTERIORI SVILUPPI


# La scelta dell'Object Oriented.

The Object-Oriented (OO) paradigm regroups the data and capabilities of existing implicit structures in implementations via entities called “objects”. Every object created from the same “class” shares the same capabilities, while possessing its own parameters value. Heterogeneous agents fit perfectly to objects description. The translation of an envisioned ABM in OO is therefore often seamless.
Aside from the ease of implementation of ABMs, the benefits of OO programming are multiple: ease to maintain, to develop, to communicate, to split into team work packages, to modularize... at the cost of being slightly more demanding in process- ing power but this trade-off falls short in significance compared to the optimization routines often performed at each timestep when agents have to make decisions.
Also, programming languages offer OO specific “services” such as automatic deletion of unused objects, polymorphism, etc., for a more flexible and understandable implementation.

# java repast

Per realizzare questo progetto ci siamo affidati a Java Repast Symphony(S). Repast is a ,widely used free and open-source agent-based modeling ,and simulation toolkit. Three Repast platforms are currently available, namely, Repast for Java (Repast J), Repast for the Microsoft .NET framework (Repast .NET), and Repast for Python Scripting (Repast Py). Each of these ,platforms has the same ,core features. However, each platform provides a different environment for these features. Taken together, the Repast platform portfolio gives modelers a choice of model development and execution environments. Repast Simphony ,(Repast S) extends the Repast portfolio byoffering,a new ,approach ,to simulation ,development ,and execution. The Repast S runtime is designed to include advanced features for agent storage, display, and behavioral activation, as well as new facilities for data analysis and presentation. 


## iniziamo a descrivere i modelli 
Per la realizzazione di questo modello abbiamo cercato di seguire tutte le best practices presenti in [4]. Di seguito sescriveremo gli agenti che prendono parte alla nostra simulazione. 

# Antibody 

L'anticorpo è l'agente fondamentale della nostra rete idiotipica. Esso fa parte di un sistema immunitario ed è caratterizzato da un tipo e da un hValue (così come descritto da Parisi). Può essere vivo o morto e in uno stato di equilibrio o meno. Non avendo la possibilità di calcolare lo stato di equilibrio con la corretta funzione matematica (????????) abbiamo cercato di simulare tale algoritmo con una classe creata appositamente da noi, chiamata EquilibriumDataStructure. Una rappresentazione Object Oriented di un agente Anticorpo è: 

public class Antibody {

	public boolean alive;
	public int type;
	public double hValue;
	public EquilibriumDataStructure eq;

}

Questo agente ad ogni step non farà altro che influenzare tutti gli altri anticorpi presenti all'interno della rete idiotipica aggiornando il loro hValue, e cambiare il proprio stato da vivo a morto o viceversa in accordo al valore del proprio hValue dopo essere stato a sua volta influenzato da tutti gli altri anticorpi. In codice questo si traduce in: 

@ScheduledMethod(start = 1, interval = 2, priority = 2)
	public void step() {

		ImmuneSystem immuneSystem = this.getImmuneSystem();

		if (this.alive && !immuneSystem.globalEquilibrium) {
		
			this.getAntibodies().forEach(antibody -> antibody.hValue += immuneSystem.matrix[this.type][antibody.type]);
			
		}

	}

	@ScheduledMethod(start = 2, interval = 2, priority = 1)
	public void changeStatus() {
		if (!this.getImmuneSystem().globalEquilibrium) {
			if (this.hValue < 0) {
				this.alive = false;
			} else {
				this.alive = true;
			}

			eq.addState(this.alive ? "A" : "D");
			this.hValue=0;
		}
	}

# Equilibrium DataStructure

EquilibriumDataStructure è come suggerisce il nome la classe che ci consente di calcolare se un singolo agente si trova in uno stato di equilibrio o meno. Per fare questo dovendo trovare un compromesso tra prestazioni ed efficacia abbiao deciso di utilizzare un semplice algoritmo di pattern matching piuttosto che più sofisticati sistemi facenti uso di reti neurali. Il funzionamento è abbastanza intuitivo. Questa struttura dati memorizza gli ultimi n stati (decisi dall'utente) in cui si è trovato l'agente come stringa, associando il carattere 'A' ad indicare il fatto che l'agente sia vivo e 'D' and indicare che questo sia morto. Nel caso si ripeta per n volte la stessa lettera, o che queste si alternino per n volte l'agente sarà in uno stato di equilibrio.

In codice la struttura dati sarà come di seguito:

public class EquilibriumDataStructure {

	String lastStateString;
	int maxLength;
	List<String> checkerList;
	boolean equilibrium;

	public EquilibriumDataStructure(int maxLength) {

		this.lastStateString = "";
		this.maxLength = maxLength;

		String s1 = "";
		String s2 = "";
		String s3 = "";

		for (int i = 0; i < maxLength; i++) {
			s1 = s1.concat("A");
			s2 = s2.concat("D");
			s3 = s3.concat("AD");
		}

		this.checkerList = new ArrayList<String>();
		this.checkerList.add(s1);
		this.checkerList.add(s2);
		this.checkerList.add(s3.substring(0, maxLength));
		this.checkerList.add(s3.substring(1, maxLength + 1));

		this.equilibrium = false;
	}

	public void addState(String state) {
		lastStateString = lastStateString.concat(state);

		if (lastStateString.length() > maxLength) {
			lastStateString = lastStateString.substring(lastStateString.length() - maxLength, lastStateString.length());
		}
	}

	public boolean isEquilibrium() {
		this.equilibrium = this.checkerList.stream().anyMatch(string -> string.equals(this.lastStateString));
		return this.equilibrium;
				

	}
	
	public void reset() {
		this.lastStateString="";
	}
}

# Systema immunitario 

Il sistema immunitario in una visione a basso livello svolge quasi una funzione di ambiente. Vedendolo più ad alto livello svolge invece un ruolo attivo all'interno della simulazione. Per poter esprimerlo al meglio abbiamo deciso di creare entrambe le viste all'interno della nostra simulazione. 

Il sistema immunitario al momento della creazione genera casualmente la matrice di interazione tra anticorpi. Esso può trovarsi in due stati: in equilibrio o meno, ed infettato o meno. Una rappresentazione Object Oriented del sistema immunitario è: 

public class ImmuneSystem {

	public boolean globalEquilibrium;
	public double[][] matrix;
	public boolean isInfected;
	public int maxEquilibriumStateLength;

	public ImmuneSystem( int maxEquilibriumStateLength) {
		this.globalEquilibrium = false;
		this.isInfected = false;
		this.maxEquilibriumStateLength = maxEquilibriumStateLength;
}

Esso ad ogni step non farà altro che controllare se ogni aticorpo presente nella rete idiotipica sia in uno stato di equilibrio o meno. Nel caso in cui tutti siano in equilibrio contemporaneamente allora esso stesso sarà in uno stato di equilibrio. 

Inoltre svolgerà il ruolo di agente reattivo nella nostra visione ad altro livello. Infatti esso reagirà alla presenza di un antigene di un determinato tipo terminando il suo stato di equilibrio e considerandosi 'infettato', creerà quindi un anticorpo in grado di poter contrastare quel tipo di antigene nel caso non si fosse mai imbattuto in una minaccia del genere. In codice:

	@ScheduledMethod(start = 1, interval = 2, priority = 2)
	public void checkEquilibrium() {
		Context<Antibody> context = ContextUtils.getContext(this);
		this.globalEquilibrium = context.getObjectsAsStream(Antibody.class).allMatch(agent -> agent.eq.isEquilibrium());
		if (this.globalEquilibrium && this.isInfected) {
			this.removeAntigen();
			this.isInfected = false;
		}

	}

	@Watch(watcheeClassName = "idiotypicNetwork.Antigen", watcheeFieldNames = "moved", query = "within_moore 1", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void reactToAntigen() {
		this.getAntibodies().forEach(antibody -> antibody.eq.reset());
		this.globalEquilibrium = false;
		this.isInfected = true;

		Antigen antigen = this.getAntigen();
		if (antigen != null && antigen.type < this.matrix.length) {
			Context<Antibody> context = ContextUtils.getContext(this);

			this.addNewAntibodyToMatrix();
			context.add(new Antibody(antigen.type, this.maxEquilibriumStateLength));
		}

	}

# Antigene 

Un antigene è caratterizzato da un tipo. L'antigene non svolge un ruolo attivo in questa simulazione. Per motivi grafici abbiamo scelto di farlo avvicinare al sistema immunitario e di infettarlo nel momento in cui questi entrino in contatto, ma a livello di logica di business basterebbe la sua presenza all'interno dell'organismo per far scattare i processi del sistema immunitario in sua difesa. 

public class Antigen {

	int type;
	public Grid<Object> grid;
	boolean moved;
	
	
	public Antigen (int type, Grid<Object> grid) {
		
		this.grid= grid;
		this.type = type;
		this.moved = false;
		
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void step() {
		ImmuneSystem immuneSystem = this.getImmuneSystem();
		if(immuneSystem.globalEquilibrium) {
			
			GridPoint myPoint = grid.getLocation(this);
			GridPoint isPoint = grid.getLocation(immuneSystem);
			
			float deltaX = isPoint.getX() - myPoint.getX();
			float deltaY = isPoint.getY() - myPoint.getY();
			double angle = Math.atan2( deltaY, deltaX ); 

			
			grid.moveByVector(this, 1, angle);
			this.moved = true;
		}
	}
}

# Eternal Agent 

Abbiamo infine un agente che abbiamo inserito semplicemente per motivi di simulazione. Esso non fa altro che creare in maniera randomica un nuovo antigene ogni qual volta il sistema immunitario non sia infettato e si trovi in uno stato di equilibrio. Verificherà inoltre che il sistema immunitario venga attaccato da un solo antigene alla volta. In codice:

	@Watch(watcheeClassName = "idiotypicNetwork.ImmuneSystem", watcheeFieldNames = "globalEquilibrium", whenToTrigger = WatcherTriggerSchedule.IMMEDIATE)
	public void createAntigen() {

		Context<Antigen> context = ContextUtils.getContext(this);

		if (context.getObjectsAsStream(Antigen.class).count() == 0) {

			Random r = new Random();
			if (r.nextDouble() < this.newAntigenPercentage) {

				int antigenType = antigenTypeCount;
				context.add(new Antigen(antigenType, this.grid));
				antigenTypeCount++;
			} else {
				context.add(new Antigen(r.nextInt(antigenTypeCount),this.grid));
			}

		}
	}









	[STUDSECT]
AP Practice -  Giustificare il punteggio

Stimolo scritto -- Di seguito è riportata il punto 2d dell'AP Explore Performance Task.

Innovazione Tecnologica

2d. Utilizzando dettagli specifici, descrivi almeno un problema di archiviazione dati, un problema di riservatezza dati o un problema di sicurezza dei dati direttamente correlato all'innovazione informatica.



Esempio di Risposta degli Studenti -- di seguito una risposta di esempio di un ragazzo che ha sottolineato un problema dati :

L'app FaceSnap è autorizzata a utilizzare molte componenti del tuo smartphone, inclusa la fotocamera. Un problema di privacy dati che deriva da questo è che l'azienda potrebbe accedere alla telecamera per vedere cosa stai facendo a tua insaputa.



Zero punti !!! - in base alle linee guida per il punteggio elencate di seguito, lo studente NON otterrebbe un punto per la riga 6 e probabilmente NON otterrebbe un punto nemmeno per la riga 7.

Perché? Ecco le linee guida per il punteggio per le righe 6 e 7.



Giustificare il punteggio

Nello spazio fornito di seguito spiegare:

Perché allo studente non è stata assegnata nessuna delle due righe in base alle linee guida per il punteggio.
Dai uno o più suggerimenti su come modificare la risposta in modo che lo studente capisca il punto.



op _ _ _ _ _ _ _ _ _  : Hand Hand Hand Deck Deck Bool Bool Int Bool -> Game [ctor format ( ntr ontm onty onng onnc nn d d d d ) ] .





Il primo che parla fa una presentazione a mo di sommario di come sarà la presentazione.

Conviene che già da ora inziamo a fare un file che alrimenti ci perdiamo le idee... lui un paio ne ha dette

idee - 
	Analizzare per bene processo iterativo
	Mettere sviluppi futuri 

-Aggiungere qualcosa alla relazione sulla teoria in modo che lo indirizziamo altrimenti ci potrebbe chiedere di tutto
