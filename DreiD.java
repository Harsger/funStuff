import java.awt.Component; 
import java.awt.Color; 
import java.awt.Graphics; 
import java.util.*;
import java.io.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

class OfUse{
	public static double maximum = Math.pow(10,6);
	public static double minimum = Math.pow(10,-12);
}

class Bild extends Component{
	BufferedImage img;
	int offx, offy;
	public Bild(BufferedImage img, int offx, int offy){
		this.img = img;	
		this.offx = offx;
		this.offy = offy;
	}
	public void paint(Graphics g) {
		g.drawImage(img, offx, offy, null);
	}
}

class Dot{
	double[] koord;
	int col;
	public Dot(double a, double b, int c){
		koord = new double[2];
		koord[0] = a;
		koord[1] = b;
		col = c;
	}
	public String toString(){
		return " Dot ( "+this.koord[0]+" , "+this.koord[1]+" ) mit Farbe : "+col;
	}
} 

class Punkt{
	double[] koord;
	int col;
	public Punkt(double a, double b, double c, int d){
		koord = new double[3];
		koord[0] = a;
		koord[1] = b;
		koord[2] = c;
		col = d;
	}
	public Punkt(double[] ueb, int d){
		koord = new double[3];
		for(int i=0; i<3; i++){
			koord[i] = ueb[i];
		}
		col = d;
	}
	public Punkt(Punkt pu){
		this.koord = new double[3];
		for(int i=0; i<3; i++){
			this.koord[i] = pu.koord[i];
		}
		this.col = pu.col;
	}
	public void mult(double mal){
		for(int i=0; i<3; i++){
			this.koord[i] = mal*this.koord[i];
		}
	}
	public Punkt multNew(double mal){
		Punkt rueck = this;
		for(int i=0; i<3; i++){
			rueck.koord[i] = mal*this.koord[i];
		}
		return rueck;
	}
	public Punkt adi(Punkt pu){
		Punkt rueck = new Punkt(1.,1.,1.,0);
		for(int i=0; i<3; i++){
			rueck.koord[i] = this.koord[i] + pu.koord[i];
		}
		//rueck.col = this.col + pu.col;
		return rueck;
	}
	public Punkt subt(Punkt pu){
		Punkt rueck = new Punkt(1.,1.,1.,0);
		for(int i=0; i<3; i++){
			rueck.koord[i] = this.koord[i] - pu.koord[i];
		}
		//rueck.col = this.col - pu.col;
		return rueck;
	}
	public double prod(Punkt pu){
		double rueck=0.;
		for(int i=0; i<3; i++){
			rueck += this.koord[i]*pu.koord[i];
		}
		return rueck;
	}
	
	public Punkt cross(Punkt pu){
		Punkt rueck = new Punkt(1.,1.,1.,0);
		rueck.col = this.col * pu.col;
		rueck.koord[0] = this.koord[1]*pu.koord[2] - this.koord[2]*pu.koord[1];
		rueck.koord[1] = this.koord[2]*pu.koord[0] - this.koord[0]*pu.koord[2];
		rueck.koord[2] = this.koord[0]*pu.koord[1] - this.koord[1]*pu.koord[0];
		return rueck;
	}
	public double norm(){
		double rueck=Math.sqrt(this.prod(this));
		return rueck;
	}
	public Punkt normal(){
		Punkt rueck = this;
		double norm = this.norm();
		if(norm>OfUse.minimum){
			rueck.mult(1./norm);
		}
		else{
			System.out.println(" Nullvektor! ");
		}
		return rueck;
	}
	public double dist(Punkt pu){
		Punkt arbeiter = this.subt(pu);
		double rueck = arbeiter.norm();
		return rueck;
	}
	public double winkel(Punkt pu){
		double rueck=0.;
		rueck = this.prod(pu)/this.norm()/pu.norm();
		return rueck;
	}
	public String toString(){
		return " Punkt ( "+this.koord[0]+" , "+this.koord[1]+" , "+this.koord[2]+" ) mit Farbe : "+col;
	}
	public void niceOut(){
		System.out.print(" ");
		System.out.format("%5.1f", this.koord[0]);
		System.out.print(", ");
		System.out.format("%5.1f", this.koord[1]);
		System.out.print(", ");
		System.out.format("%5.1f", this.koord[2]);
	}
}

class Matrix{
	int zeile;
	int spalte;
	double[][] mat;
	public Matrix(int a, int b){
		this.zeile = a;
		this.spalte = b;
		this.mat = new double[a][b]; 
	}
	public Matrix(Punkt pu, double wi){
		this.zeile = 3;
		this.spalte = 3;
		double ca = Math.cos(wi);
		double sa = Math.sin(wi);
		Punkt npu = pu.normal();
		this.mat = new double[3][3];
		//
		this.mat[0][0] = pu.koord[0]*pu.koord[0]*(1.-ca)+ca;
		this.mat[0][1] = pu.koord[0]*pu.koord[1]*(1.-ca)-pu.koord[2]*sa;
		this.mat[0][2] = pu.koord[0]*pu.koord[2]*(1.-ca)+pu.koord[1]*sa;
		//
		this.mat[1][0] = pu.koord[1]*pu.koord[0]*(1.-ca)+pu.koord[2]*sa;
		this.mat[1][1] = pu.koord[1]*pu.koord[1]*(1.-ca)+ca;
		this.mat[1][2] = pu.koord[1]*pu.koord[2]*(1.-ca)-pu.koord[0]*sa;
		//
		this.mat[2][0] = pu.koord[2]*pu.koord[0]*(1.-ca)-pu.koord[1]*sa;
		this.mat[2][1] = pu.koord[2]*pu.koord[1]*(1.-ca)+pu.koord[0]*sa;
		this.mat[2][2] = pu.koord[2]*pu.koord[2]*(1.-ca)+ca;
	}
	public Matrix(double[][] emat){
		this.zeile = emat.length;
		this.spalte = emat[0].length;
		for(int i=1; i<this.spalte; i++){
			if(emat[i].length<this.spalte){this.spalte=emat[i].length;}
		}
		this.mat = new double[this.zeile][this.spalte];
		for(int i=0; i<this.zeile; i++){
			for(int j=0; j<this.spalte; j++){
				this.mat[i][j]=emat[i][j];
			}
		}
	}
	public void ein(int a, int b, double c){
		this.mat[a][b] = c;
	}
	public void ausgabe(){
		for(int i=0; i<this.mat.length; i++){
			System.out.print(" [ ");
			for(int j=0; j<this.mat[i].length; j++){
				System.out.printf("%s %8.4f %s"," ",mat[i][j]," ");
			}
			System.out.println(" ]");
		}
	}
	public Matrix mult(Matrix mal){
		Matrix rueck = new Matrix(this.zeile, mal.spalte);
		if(this.spalte==mal.zeile){
			for(int i=0; i<this.zeile; i++){
				for(int j=0; j<mal.spalte; j++){
					double sum=0.;
					for(int k=0; k<this.spalte; k++){
						sum += this.mat[i][k] * mal.mat[k][j];
					}
					//System.out.print(sum+" ");
					rueck.ein( i, j, sum);
				}
			}
			//System.out.println(" ");
		}
		else{
			System.out.println(" Dimensionalitaeten passen nicht! ");
			for(int i=0; i<this.zeile; i++){
				for(int j=0; j<mal.spalte; j++){
					if(i==j){
						rueck.ein( i, j, 1.);
					}
					else{
						rueck.ein( i, j, 0.);
					}
				}
			}
		}
		return rueck;
	}
	public Dot mulDtoD(Dot doot){
		Dot rueck = new Dot(1., 1., 0);
		rueck.col = doot.col;
		if(this.zeile==2 && this.spalte==2){
			for(int i=0; i<2; i++){
				rueck.koord[i]=0;
				for(int j=0; j<2; j++){
					rueck.koord[i] += this.mat[i][j] * doot.koord[j];
				}
			}
		}
		else{
			System.out.println(" Dimensionalitaeten passen nicht! ");
		}
		return rueck;
	}
	public Punkt mulPtoP(Punkt pu){
		Punkt rueck = new Punkt( 1., 1., 1., 0);
		rueck.col = pu.col;
		if(this.zeile==3 && this.spalte==3){
			for(int i=0; i<3; i++){
				rueck.koord[i]=0;
				for(int j=0; j<3; j++){
					rueck.koord[i] += this.mat[i][j] * pu.koord[j];
				}
			}
		}
		else{
			System.out.println(" Dimensionalitaeten passen nicht! ");
		}
		return rueck;
	}
	public Dot mulPtoD(Punkt pu){
		Dot rueck = new Dot(1., 1., 0);
		rueck.col = pu.col;
		if(this.zeile==2 && this.spalte==3){
			for(int i=0; i<2; i++){
				rueck.koord[i]=0;
				for(int j=0; j<3; j++){
					rueck.koord[i] += this.mat[i][j] * pu.koord[j];
				}
			}
		}
		else{
			System.out.println(" Dimensionalitaeten passen nicht! ");
		}
		return rueck;
	}
	public String toString(){
		return " Zeilen : "+this.zeile+" ; Spalten : "+this.spalte;
	}
}

class RotMat extends Matrix{
	int achse;
	double winkel;
	//static super.zeile = 3;
	//static super.spalte = 3;
	public RotMat(int a, double p){
		super(3,3);
		achse = a%3;
		winkel = p%(2*Math.PI);
		for(int i=0; i<3; i++){
			for(int j=0; j<3; j++){
				if(i==achse){
					if(j==achse){
						super.mat[i][j]=1.;
					}
					else{
						super.mat[i][j]=0.;
					}
				}
				else if(j==achse){
					super.mat[i][j]=0.;
				}
				else if(i==j){
					super.mat[i][j]=Math.cos(winkel);
				}
				else if(i>j){
					if(achse==1){super.mat[i][j]=Math.sin(winkel);}
					else{super.mat[i][j]=-Math.sin(winkel);}
				}
				else{
					if(achse==1){super.mat[i][j]=-Math.sin(winkel);}
					else{super.mat[i][j]=Math.sin(winkel);}
				}
			}
		}
	}
	public String toString(){
		String ax;
		if(this.achse==0){
			ax = "x";
		}
		if(this.achse==1){
			ax = "y";
		}
		if(this.achse==2){
			ax = "z";
		}
		else{
			ax = "unbekannte";
		}
		double wingra = this.winkel*180/2/Math.PI;
		return " Rotationsmatrix um "+ax+"-Achse um "+wingra+" Grad ";
	}
}

class Gitter{
	String name;
	int max = 300;
	int hintergrund = Color.BLACK.getRGB();
	JFrame frame;
	BufferedImage img = new BufferedImage(2*max, 2*max, BufferedImage.TYPE_INT_RGB );
	Bild bi;
	double bereich;
	int[][] git;
	public Gitter(String setName, double ber, int hin){
		this.name = setName;
		this.frame = new JFrame(this.name);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(725, 725);
		int rmax = 2*this.max;
		git = new int[rmax][rmax];
		hintergrund = hin;
		for(int cx=0; cx<rmax; cx++){
			for(int cy=0; cy<rmax; cy++){
				git[cx][cy]=hintergrund;

			}
		}
		bereich = ber;
	}
	public void zuruecksetzen(){
		int rmax = 2*this.max;
		for(int cx=0; cx<rmax; cx++){
			for(int cy=0; cy<rmax; cy++){
				git[cx][cy]=hintergrund;

			}
		}
	}
	public void doteingabe(ArrayList<Dot> dots){
		this.zuruecksetzen();
		double tx, ty;
		//double ber = this.bereich/(double)max;
		double ber = 1.;
		double rmax = 300.;
		int gx, gy;
		for(int k=dots.size()-1; k>0; k--){
			tx = dots.get(k).koord[0];
			ty = dots.get(k).koord[1];	
			//System.out.println(Math.abs(tx)+" "+ber+" "+Math.abs(ty));
			if(Math.abs(tx)<=ber && Math.abs(ty)<=ber){
				//gx = (int)(rmax + rmax * tx / ber);
				//gy = (int)(rmax - rmax * ty / ber);
				gx = (int)(rmax + rmax * tx);
				gy = (int)(rmax - rmax * ty);
				git[gx][gy] = dots.get(k).col;	
				//System.out.println(gx+" "+gy+"    "+git[gx][gy]);
			}
			else{
				//System.out.println("OUT"+tx+ty);
			}
		}
	}
	public void initialisieren(){
		int rmax = 2*this.max;
		for(int cx=0; cx<rmax; cx++){
			for(int cy=0; cy<rmax; cy++){
				img.setRGB(cx, cy, 0);
			}
		}
		bi = new Bild(img,50,50);
		frame.getContentPane().add(bi);
		frame.setVisible(true);
	}
	public void zeichnen(){
		int rmax = 2*this.max;
		for(int cx=0; cx<rmax; cx++){
			for(int cy=0; cy<rmax; cy++){
				img.setRGB(cx, cy, git[cx][cy]);
			}
		}
		bi = new Bild(img,50,50);
		frame.getContentPane().add(bi);
		//frame.setVisible(true);
		frame.validate();	
	}
	public String toString(){
		return this.name;
	}
}

class RaumEbene{
	Gitter git;
	Punkt beob;
	Punkt forward;
	Punkt upDir;
	ArrayList<Punkt> orte;
	ArrayList<Punkt> zBuffer;
	ArrayList<Dot> zweiD;
	public RaumEbene(String name, double ber, int hin, Punkt be, Punkt fow, Punkt up){
		git = new Gitter(name, ber, hin);
		beob = be;
		forward = fow;
		upDir = up;
		orte = new ArrayList<Punkt>();
		zBuffer = new ArrayList<Punkt>();
		zweiD = new ArrayList<Dot>();
	}
	public RaumEbene(String name, double ber, int hin){
		git = new Gitter(name, ber, hin);
		beob = new Punkt(0., 0., ber, 0);
		forward = new Punkt(0., 0., -1, 0);
		upDir = new Punkt(0., 1., 0., 0);
		orte = new ArrayList<Punkt>();
		zBuffer = new ArrayList<Punkt>();
		zweiD = new ArrayList<Dot>();
	}
	public boolean readOrte(String dateiname, int gcol){
		boolean goodRead = false;
		Scanner s;
		String line;
		String columns[];
		double coord[] = new double[3];
		int tcol;
		ArrayList<Punkt> or;
		try {
			s = new Scanner(new File(dateiname));
		}
		catch (IOException e) {
			System.out.println(" Datei konnte nicht geoeffnet werden! " );
			return goodRead;
		}
		while ( s.hasNextLine() ) {
			line = s.nextLine();
			columns = line.split(" ");
			if(columns.length<3){System.out.println(" Datei enthaelt zu wenige Spalten! ");}
			else if(columns.length<4){
				for(int i=0; i<3; i++){
					coord[i] = Double.parseDouble(columns[i]);
				}
				tcol = gcol;
				orte.add(new Punkt(coord, tcol));
				goodRead = true;
			}
			else if(columns.length<5){
				for(int i=0; i<3; i++){
					coord[i] = Double.parseDouble(columns[i]);
				}
				tcol = Integer.parseInt(columns[3]);
				orte.add(new Punkt(coord, tcol));
				goodRead = true;
			}
			else{
				System.out.println(" Datei enthaelt zu viele Spalten! ");
				System.out.println(" Es koennen nur bestimmte Formate verarbeitet werden! ");
			}
		}           
		s.close();
		return goodRead;
	}
	public void projektion(){

		///////////////////////////////define variables/////////////////////////////////////////////
		double rx, ry, rz;
		int rc;
		double near = git.bereich / 100;
		double far = 2. * git.bereich ;
		double oefwin = 120.*Math.PI/180./2.;
		double ow = 0.;
		double setver = 1.;
		double sv = 0.;
		Punkt zax = new Punkt(this.forward);
		zax = zax.multNew(-1.).normal();
		Punkt yax = new Punkt(this.upDir);
		yax = yax.normal();
		Punkt xax = yax.cross(zax).normal();
		Matrix camTrafo = new Matrix(4,4);
		Matrix projMat = new Matrix(4,4);
		double[] homCoord = new double[4];
		double[] projHom = new double[4];
		double win=0.;
		double distance=0.;
		double wver=Math.cos(oefwin);
		//System.out.println(" near:"+near+" far:"+far);
		///////////////////////////////define variables END/////////////////////////////////////////////

		if( Math.abs(Math.tan(oefwin)) > OfUse.minimum ){ow=1./Math.tan(oefwin);}
		else{ow=OfUse.maximum;}
		if( Math.abs(Math.tan(setver)) > OfUse.minimum ){sv=1./Math.tan(setver);}
		else{sv=OfUse.maximum;}

		///////////////////////////////calculate trafomatrices////////////////////////////////
		for(int i=0; i<4; i++){
			for(int j=0; j<4; j++){
				//Kameratransformationsmatrix
				if(i==0){
					if(j<3){camTrafo.mat[i][j]=xax.koord[j];}
					else{camTrafo.mat[i][j]=-xax.prod(beob);}
				}
				else if(i==1){
					if(j<3){camTrafo.mat[i][j]=yax.koord[j];}
					else{camTrafo.mat[i][j]=-yax.prod(beob);}
				}
				else if(i==2){
					if(j<3){camTrafo.mat[i][j]=zax.koord[j];}
					else{camTrafo.mat[i][j]=-zax.prod(beob);}
				}
				else{
					if(j<3){camTrafo.mat[i][j]=0.;}
					else{camTrafo.mat[i][j]=1.;}
				}
				//Projektionsmatrix
				if(i==0 && j==0){projMat.mat[i][j]=sv;}
				else if(i==1 && j==1){projMat.mat[i][j]=ow;}
				else if(i==2 && j==2){projMat.mat[i][j]=far/(near-far);}
				else if(i==2 && j==3){projMat.mat[i][j]=near*far/(near-far);}
				else if(i==3 && j==2){projMat.mat[i][j]=-1.;}
				else{projMat.mat[i][j]=0.;}
				/*
				if(i==j){projMat.mat[i][j]=1.;}
				else{projMat.mat[i][j]=0.;}
				*/
			}
		}
		projMat = projMat.mult(camTrafo);
		/*
		camTrafo.ausgabe();
		System.out.println("OUT!");
		projMat.ausgabe();
		System.out.println("OUT!");
		projMat.ausgabe();
		System.out.println("OUT! \n"+forward);
		System.out.println("OUT! \n"+upDir);
		System.out.println("OUT! \n"+zax);
		*/
		///////////////////////////////calculate trafomatrices END////////////////////////////////

		//////////////////////////////projekt orte to zBuffer///////////////////////////////
		zBuffer.clear();
		for(int k=0; k<orte.size(); k++){
			win = this.forward.winkel(this.orte.get(k).subt(this.beob));
			distance = this.orte.get(k).dist(this.beob);
			//System.out.println(" win:"+win+" dis:"+distance);
			if( (distance>=near && distance<=far) && win>=wver ){
				//Kamtrafo und Projektion
				//System.out.println(" ");
				for(int i=0; i<4; i++){
					if(i<3){homCoord[i]=orte.get(k).koord[i];}
					else{homCoord[i]=1.;}
					//System.out.print(" "+homCoord[i]);
				}
				//System.out.println(" ");
				for(int i=0; i<projHom.length; i++){
					projHom[i]=0.;
					for(int j=0; j<projMat.mat[i].length; j++){
						projHom[i] += projMat.mat[i][j] * homCoord[j];
					}	
					//System.out.print(" "+projHom[i]);
				}
				//System.out.println(" ");
				rc = orte.get(k).col;
				zBuffer.add(new Punkt(projHom, rc));
				//zBuffer.add(new Punkt(projHom[0], projHom[1], projHom[3], rc));
			}
		}
		//////////////////////////////projekt orte to zBuffer END///////////////////////////////
	}
	public void zBuffering(){
		zweiD.clear();
		double rx, ry, cz;
		int rc;
		int buffer;
		int num = zBuffer.size();
		int newnum = 0;
		int order[] = new int[num];
		double values[] = new double[num];
		for(int i=1; i<num; i++){
			order[i] = i;
			values[i] = zBuffer.get(i).koord[2];
		}
		////////////////////////////////bubble sort//////////////////////////////////
		while(num!=0){
			newnum = 0;
			for(int i=1; i<num; i++){
				if( values[order[i]] > values[order[i-1]] ){
					buffer = order[i];
					order[i] = order[i-1];
					order[i-1] = buffer;
					newnum = i;
				}
				/*
				if(zBuffer.get(i-1).koord[2] > zBuffer.get(i).koord[2]){
					buffer = zBuffer.get(i);
					zBuffer.get(i) = zBuffer.get(i-1);
					zBuffer.get(i-1) = buffer;
					newnum = i;
				}
				*/
			}
			num = newnum;
		}
		////////////////////////////////bubble sort END//////////////////////////////////
		num = zBuffer.size();
		for(int i=num-1; i>=0; i--){
			cz = zBuffer.get(order[i]).koord[2];
			//System.out.println(" cz:"+cz);
			if(cz > OfUse.minimum){
				rx = zBuffer.get(order[i]).koord[0] / cz;
				ry = zBuffer.get(order[i]).koord[1] / cz;
			}
			else{
				rx = OfUse.maximum;
				ry = OfUse.maximum;
			}
			rc = zBuffer.get(order[i]).col;
			zweiD.add( new Dot(rx ,ry, rc));
		}
	}
	public void initialize(){
		this.git.initialisieren();
		this.projektion();
		this.zBuffering();
		this.git.doteingabe(zweiD);
		this.git.zeichnen();
	}
	public void draw(){
		this.projektion();
		this.zBuffering();
		this.git.doteingabe(zweiD);
		this.git.zeichnen();
	}
	public void rotateView(Punkt rotAx, double angle){
		Matrix rotMat = new Matrix(rotAx, angle);
		this.forward = rotMat.mulPtoP(forward);
		this.upDir = rotMat.mulPtoP(upDir);
		this.draw();
	}
	public void rotatePos(Punkt rotAx, double angle){
		Matrix rotMat = new Matrix(rotAx, angle);
		//Matrix rotMatView = new Matrix(rotAx, angle);
		this.beob = rotMat.mulPtoP(beob);
		this.forward = rotMat.mulPtoP(forward);
		this.upDir = rotMat.mulPtoP(upDir);
		this.draw();
	}
	public void movePos(Punkt direction, double stepsize){
		Punkt step = new Punkt(direction);
		step.normal().multNew(stepsize);
		this.beob = beob.adi(step);
		this.draw();
	}
	public boolean userOrder(String order){
		boolean rueck = false;
		double minAngle = 2.*Math.PI/16./10.;
		if(order.equals("w")){
			for(int i=0; i<10; i++){
				this.movePos( this.forward, this.git.bereich/200.);
			}
			rueck = true;
		}
		else if(order.equals("s")){
			for(int i=0; i<10; i++){
				this.movePos( this.forward, -this.git.bereich/200.);
			}
			rueck = true;
		}
		else if(order.equals("a")){
			for(int i=0; i<10; i++){
				this.movePos( this.upDir.cross(this.forward), this.git.bereich/200.);
			}
			rueck = true;
		}
		else if(order.equals("d")){
			for(int i=0; i<10; i++){
				this.movePos( this.forward.cross(this.upDir), this.git.bereich/200.);
			}
			rueck = true;
		}
		else if(order.equals("i")){
			for(int i=1; i<11; i++){
				this.rotateView( this.upDir.cross(this.forward), minAngle);
			}
			rueck = true;
		}
		else if(order.equals("k")){
			for(int i=1; i<11; i++){
				this.rotateView( this.forward.cross(this.upDir), minAngle);
			}
			rueck = true;
		}
		else if(order.equals("j")){
			for(int i=1; i<11; i++){
				this.rotateView( this.upDir, minAngle);
			}
			rueck = true;
		}
		else if(order.equals("l")){
			for(int i=1; i<11; i++){
				this.rotateView( this.upDir, -minAngle);
			}
			rueck = true;
		}
		else if(order.equals("u")){
			for(int i=1; i<11; i++){
				this.rotateView( this.forward, -minAngle);
			}
			rueck = true;
		}
		else if(order.equals("o")){
			for(int i=1; i<11; i++){
				this.rotateView( this.forward, minAngle);
			}
			rueck = true;
		}
		else if(order.equals("4")){
			for(int i=1; i<11; i++){
				this.rotatePos( this.upDir, -minAngle);
			}
			rueck = true;
		}
		else if(order.equals("6")){
			for(int i=1; i<11; i++){
				this.rotatePos( this.upDir, minAngle);
			}
			rueck = true;
		}
		else if(order.equals("8")){
			Punkt rotAx = this.upDir.cross(this.forward);
			for(int i=1; i<11; i++){
				this.rotatePos( rotAx, minAngle);
			}
			rueck = true;
		}
		else if(order.equals("2")){
			Punkt rotAx = this.upDir.cross(this.forward);
			for(int i=1; i<11; i++){
				this.rotatePos( rotAx, minAngle);
			}
			rueck = true;
		}
		else if(order.equals("5")){
			this.beob = new Punkt(0., 0., this.git.bereich, 0);
			this.forward = new Punkt(0., 0., -1, 0);
			this.upDir = new Punkt(0., 1., 0., 0);
			this.initialize();
			rueck = true;
		}
		return rueck;
	}
}

public class DreiD{
	public static void main(String[] args){
		if(args.length<1){
			System.out.println("\n	Welcome User! ");
			System.out.println("\n Dieses Programm dient zur zweidimensionalen Dartstellung von ");
			System.out.println(" dreidimensionalen Punkten aus verschiedenen Blickwinkeln. ");
			System.out.println("\n Zur Steuerung des Blickwinkels dienen drei Steurkreuze: ");
			System.out.println("\n  - Die Eingabe von 'w', 'a', 's' oder 'd' dient zur  ");
			System.out.println("    Positionierung des Beobachtungspunktes.  ");
			System.out.println(" \n  - Die Eingabe von 'i', 'j', 'k' oder 'l' dient zur  ");
			System.out.println("    Ausrichtung des Blickwinkels.  ");
			System.out.println(" \n  - Die Eingabe von '8', '4', '2' oder '6' dient zur  ");
			System.out.println("    Rotation des Beobachtungspunktes um die Koordinatenachsen.  ");
			System.out.println(" \n Zudem kann die Deckenrichtung mit 'u' und 'o' rotiert werden. ");
			System.out.println(" Weiterhin ist es moeglich mit '5' an die Startposition zureckzukehren. ");
			System.out.println(" Um zum Hauptmenue zurueckzukehren ist die Eingabe einer beliebig ");
			System.out.println(" anderen Buchstaben- oder Zahltaste noetig. ");
			System.out.println(" \n Um diesen Header beim naechsten Start nicht angezeigt zu bekommen ");
			System.out.println(" '-l' an den Startcommand anhaengen. ");
		}
		boolean choosing = true;
		String read;
		while(choosing){
			System.out.println(" \n [1] : Beispielflug ");
			System.out.println(" [2] : Testflug ");
			System.out.println(" [3] : Dateneinlese und Flug ");
			System.out.println(" [0] : Ende ");
			System.out.print(" \n Eingabe : ");
			read = lesString();
			System.out.println(" ");
			if(read.equals("1")){show();}
			else if(read.equals("2")){example();}
			else if(read.equals("3")){readAndShow();}
			else{choosing = false;}
		} 
		System.out.println(" \n Vielen Dank, dass Sie dieses Programm genutzt haben. \n ");
	}
	public static void show(){
		int hint = Color.BLACK.getRGB();
		Punkt beob = new Punkt( 0., 0., 300., 0);
		Punkt forward = new Punkt( 0., 0., -1., 0);
		Punkt upDir = new Punkt( 0., 1., 0., 0);
		RaumEbene raster = new RaumEbene("DreiD", 300., hint, beob, forward, upDir);
		ArrayList<Punkt> orte = new ArrayList<Punkt>();
		int anzahl = 2000;
		double[] fk = new double[3];
		int fc1 = Color.RED.getRGB();
		int fc2 = Color.BLUE.getRGB();
		int fc3 = Color.YELLOW.getRGB();
		double cx = 0.;
		double cy = 0.;
		double cz = 150.;
		for(int k=0; k<anzahl; k++){
			fk[0] = zufaellig(cx-25., cx+25., 3);
			fk[1] = zufaellig(cy-25., cy+25., 3);
			fk[2] = zufaellig(cz-25., cz+25., 3);
			orte.add(new Punkt(fk[0], fk[1], fk[2], fc1));
		}
		cx = 100.;
		cy = 0.;
		cz = -100.;
		for(int k=0; k<anzahl; k++){
			fk[0] = zufaellig(cx-25., cx+25., 3);
			fk[1] = zufaellig(cy-25., cy+25., 3);
			fk[2] = zufaellig(cz-25., cz+25., 3);
			orte.add(new Punkt(fk[0], fk[1], fk[2], fc2));
		}
		cx = -100.;
		cy = 0.;
		cz = -100.;
		for(int k=0; k<anzahl; k++){
			fk[0] = zufaellig(cx-25., cx+25., 3);
			fk[1] = zufaellig(cy-25., cy+25., 3);
			fk[2] = zufaellig(cz-25., cz+25., 3);
			orte.add(new Punkt(fk[0], fk[1], fk[2], fc3));
		}
		raster.orte = orte;
		raster.initialize();
		raster.userOrder("w");
		raster.userOrder("w");
		raster.userOrder("w");
		raster.userOrder("w");
		raster.userOrder("w");
		raster.userOrder("w");
		raster.userOrder("w");
		raster.userOrder("6");
		raster.userOrder("6");
		raster.userOrder("6");
		raster.userOrder("6");
		raster.userOrder("8");
		raster.userOrder("8");
		raster.userOrder("8");
		raster.userOrder("8");
		raster.userOrder("8");
		raster.userOrder("8");
		raster.userOrder("8");
		raster.userOrder("8");
		raster.userOrder("4");
		raster.userOrder("4");
		raster.userOrder("4");
		raster.userOrder("4");
		raster.userOrder("s");
		raster.userOrder("s");
		raster.userOrder("s");
		raster.userOrder("s");
		raster.userOrder("s");
		raster.userOrder("s");
		raster.userOrder("s");
	}
	public static void example(){
		int hint = Color.BLACK.getRGB();
		Punkt beob = new Punkt( 0., 0., 300., 0);
		Punkt forward = new Punkt( 0., 0., -1., 0);
		Punkt upDir = new Punkt( 0., 1., 0., 0);
		RaumEbene raster = new RaumEbene("DreiD", 300., hint, beob, forward, upDir);
		ArrayList<Punkt> orte = new ArrayList<Punkt>();
		int anzahl = 2000;
		double[] fk = new double[3];
		int fc1 = Color.RED.getRGB();
		int fc2 = Color.BLUE.getRGB();
		int fc3 = Color.YELLOW.getRGB();
		double cx = zufaellig(-100, 100, 1);
		double cy = zufaellig(-100, 100, 1);
		double cz = zufaellig(-100, 100, 1);
		for(int k=0; k<anzahl; k++){
			fk[0] = zufaellig(cx-25., cx+25., 3);
			fk[1] = zufaellig(cy-25., cy+25., 3);
			fk[2] = zufaellig(cz-25., cz+25., 3);
			orte.add(new Punkt(fk[0], fk[1], fk[2], fc1));
		}
		cx = zufaellig(-100, 100, 1);
		cy = zufaellig(-100, 100, 1);
		cz = zufaellig(-100, 100, 1);
		for(int k=0; k<anzahl; k++){
			fk[0] = zufaellig(cx-25., cx+25., 3);
			fk[1] = zufaellig(cy-25., cy+25., 3);
			fk[2] = zufaellig(cz-25., cz+25., 3);
			orte.add(new Punkt(fk[0], fk[1], fk[2], fc2));
		}
		cx = zufaellig(-100, 100, 1);
		cy = zufaellig(-100, 100, 1);
		cz = zufaellig(-100, 100, 1);
		for(int k=0; k<anzahl; k++){
			fk[0] = zufaellig(cx-25., cx+25., 3);
			fk[1] = zufaellig(cy-25., cy+25., 3);
			fk[2] = zufaellig(cz-25., cz+25., 3);
			orte.add(new Punkt(fk[0], fk[1], fk[2], fc3));
		}
		raster.orte = orte;
		raster.initialize();
		boolean choosing = true;
		Scanner s = new Scanner(System.in);
		String read;
		while(choosing){
			System.out.print(" \n Beobachtungspunkt : "); 
			raster.beob.niceOut();
			System.out.print(" \n Blickrichtung     : "); 
			raster.forward.niceOut();
			System.out.print(" \n \n Eingabe : ");
			read = s.next();
			choosing = raster.userOrder(read);
			System.out.println(" "); 
		} 
	}
	public static void readAndShow(){
		boolean choosing = true;
		double bereich;
		System.out.println(" In welchen Zahlenbereich liegen die Daten? ");
		System.out.println(" ");
		System.out.println(" ( Der Werte wird als Z-Wert des Startpunktes ");
		System.out.println("  gesetzt und zur Skalierung verwendet. ) ");
		System.out.print(" \n Bereich :");
		bereich = lesDouble(OfUse.minimum, OfUse.maximum, "Bereich");
		System.out.println(" ");
		Scanner s = new Scanner(System.in);
		String read;
		System.out.println(" Wie heisst die Datei aus der die Daten verwendet werden sollen? ");
		System.out.println(" ");
		System.out.println(" ( ACHTUNG : Die Daten muessen in der Form ");
		System.out.println("  X-Wert Leerzeichen Y-Wert Leerzeichen Z-Wert ");
		System.out.println("  vorliegen! Evtl. den Farbecode (in JavaCol) dahinter. )");
		System.out.println(" ");
		System.out.print(" Dateiname : ");
		read = s.next();
		System.out.println(" \n ");
		int hint = Color.BLACK.getRGB();
		RaumEbene raster = new RaumEbene(read, bereich, hint);
		choosing = raster.readOrte(read, Color.RED.getRGB());
		if(choosing){raster.initialize();}
		while(choosing){
			System.out.print(" \n Beobachtungspunkt : "); 
			raster.beob.niceOut();
			System.out.print(" \n Blickrichtung     : "); 
			raster.forward.niceOut();
			System.out.print(" \n \n Eingabe : ");
			read = s.next();
			choosing = raster.userOrder(read);
			System.out.println(" "); 
		} 
		
	}
	public static double zufaellig(double unter, double ober, int vert){
		double rueck = 0.;
		if(vert<1){rueck=(ober-unter)/2.;}
		else{
			for(int i=0; i<vert; i++){
				rueck += unter + Math.random() * (ober - unter);
			}
			rueck /= vert;
		}
		return rueck;
	}
	public static double lesDouble(double kl, double gr, String name){
		double gelesen=0.;
		Scanner s = new Scanner(System.in);
		boolean schlecht = true;
		while(schlecht){
			System.out.print(" ");
			try{
				gelesen = s.nextDouble();
				if(kl<=gelesen && gelesen<=gr){schlecht = false;}
				else{
					System.out.println(" Der Wert muss zwischen "+kl+" und "+gr+" liegen!");
					System.out.print(" "+name+" :");
				}
			}
			catch(InputMismatchException e){
				String ausnahme = s.nextLine();
				System.out.println("\n Sie muessen eine Zahl eingeben! \n Das Dezimaltrennzeichen kommt auf Ihr Betriebssystem an!");
				System.out.print(" "+name+" :");
				schlecht = false;
			}
		}
		return gelesen;
	}
	public static String lesString(){
		Scanner s = new Scanner(System.in);
		String read;
		read = s.next();
		return read;
	}
}
