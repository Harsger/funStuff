import java.awt.Component; 
import java.awt.Color; 
import java.awt.Graphics; 
import java.util.*;
import javax.swing.*;
import java.awt.image.BufferedImage;

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
	int x, y;
	static boolean periodic = true;
	static int min = 1;
	static int max = 100;
	public Dot(int setx, int sety){
		int forx = setx;
		int fory = sety;
		//System.out.println("\n forx = "+forx+"\n"+" fory = "+fory);
		if(periodic){
			if(setx < min){
				forx=max+(forx-min)%(max-min+1)+1;
			}
			if(setx > max){
				forx=min+(forx-max)%(max-min+1)-1;
			}
			if(sety < min){
				fory=max+(fory-min)%(max-min+1)+1;
			}
			if(sety > max){
				fory=min+(fory-max)%(max-min+1)-1;
			}
			//System.out.println("\n forx = "+forx+"\n"+" fory = "+fory);
		}
		else{
			boolean outofborder = false;
			if(setx < min){
				forx=min;
				outofborder=true;
			}
			if(setx > max){
				forx=max;
				outofborder=true;
			}
			if(sety < min){
				fory=min;
				outofborder=true;
			}
			if(sety > max){
				fory=max;
				outofborder=true;
			}
			if(outofborder){System.out.println("\n Dot out of borders! "+"\n x : "+setx+" -> "+forx+"\n y : "+sety+" -> "+fory+"\n");}
		}
		this.x = forx;
		this.y = fory;
	}
	public String toString(){
		String s = " Dot ( "+this.x+" , "+this.y+" ) ";
		return s;
	}
	public void move(int dx, int dy){
		//System.out.println("\n move: dx = "+dx+"\n move: dy = "+dy);
		//System.out.println("\n move: dx%(max-min+1) = "+dx%(max-min+1)+"\n move: dy%(max-min+1) = "+dy%(max-min+1));
		int forx = this.x + dx;
		int xdx = this.x + dx;
		int fory = this.y + dy;
		int ydy = this.y + dy;
		//System.out.println("\n move: forx = "+forx+"\n move: fory = "+fory);
		if(periodic){
			if(forx < min){
				forx=max+(forx-min)%(max-min+1)+1;
			}
			if(forx > max){
				forx=min+(forx-max)%(max-min+1)-1;
			}
			if(fory < min){
				fory=max+(fory-min)%(max-min+1)+1;
			}
			if(fory > max){
				fory=min+(fory-max)%(max-min+1)-1;
			}
		}
		else{
			boolean outofborder = false;
			if(forx < min){
				forx=min;
				outofborder=true;
			}
			if(forx > max){
				forx=max;
				outofborder=true;
			}
			if(fory < min){
				fory=min;
				outofborder=true;
			}
			if(fory > max){
				fory=max;
				outofborder=true;
			}
			if(outofborder){System.out.println("\n Dot out of borders! "+"\n x : "+xdx+" -> "+forx+"\n y : "+ydy+" -> "+fory+"\n");}
		}
		this.x = forx;
		this.y = fory;
	}
	public void move(int dir){
		if(dir == 1){
			this.move(-1,-1);
		}
		else if(dir == 2){
			this.move(0,-1);
		}
		else if(dir == 3){
			this.move(1,-1);
		}
		else if(dir == 4){
			this.move(-1,0);
		}
		//else if(dir == 5){}
		else if(dir == 6){
			this.move(1,0);
		}
		else if(dir == 7){
			this.move(-1,1);
		}
		else if(dir == 8){
			this.move(0,1);
		}
		else if(dir == 9){
			this.move(1,1);
		}
	}
	public void setPeriodicity(boolean choose){
		this.periodic = choose;
	}
}

class Plant extends Dot{
	Color col = Color.GREEN;
	static double bornrate = 0.7;
	public Plant(int setx, int sety){
		super(setx, sety);
	}
	public Plant reproduce(Plant pl, int cx, int cy){
		Plant child = new Plant(cx,cy);
		if(Math.abs(this.x-pl.x)>=2 || Math.abs(this.y-pl.y)>=2){
			System.out.println("\n Plants zu weit entfernt!");
		}
		if(Math.abs(this.x-cx)>=2 || Math.abs(this.y-cy)>=2){
			System.out.println("\n Child zu weit entfernt von Mother!");
		}
		if(Math.abs(cx-pl.x)>=2 || Math.abs(cy-pl.y)>=2){
			System.out.println("\n Child zu weit entfernt von Father!");
		}
		return child;
	}
	public Plant reproduce(Plant pl){
		Plant child;
		int nx, ny;
		if(Math.abs(this.x-pl.x)<2 || Math.abs(this.y-pl.y)<2){
			if(this.x==pl.x){
				int high;
				if(this.y>pl.y){high=this.y;}
				else{high=pl.y;}
				double zufall1 = Math.random();
				double zufall2 = Math.random();
				if(zufall1<0.25){
					ny=high-2;
					nx=this.x;
				}
				else if(zufall1<0.5){
					ny=high-1;
					if(zufall2<0.5){nx=this.x-1;}
					else{nx=this.x+1;}
				}
				else if(zufall1>0.75){
					ny=high+1;
					nx=this.x;
				}
				else{
					ny=high;
					if(zufall1<0.5){nx=this.x-1;}
					else{nx=this.x+1;}
				}
			}
			else if(this.y==pl.y){
				int high;
				if(this.x>pl.x){high=this.x;}
				else{high=pl.x;}
				double zufall1 = Math.random();
				double zufall2 = Math.random();
				if(zufall1<0.25){
					nx=high-2;
					ny=this.y;
				}
				else if(zufall1<0.5){
					nx=high-1;
					if(zufall2<0.5){ny=this.y-1;}
					else{ny=this.y+1;}
				}
				else if(zufall1>0.75){
					nx=high+1;
					ny=this.y;
				}
				else{
					nx=high;
					if(zufall2<0.5){ny=this.x-1;}
					else{ny=this.y+1;}
				}
			}
			else{
				double zufall = Math.random();
				if(zufall<0.5){
					nx=this.x;
					ny=pl.y;
				}
				else{
					nx=pl.x;
					ny=this.y;
				}
			}
		}
		else{
			System.out.println("\n Plants zu weit entfernt!");
			nx=(this.x+pl.x)/2;
			ny=(this.y+pl.y)/2;
			double zufall1 = Math.random();
			double zufall2 = Math.random();
			if(zufall1<0.5){nx++;}
			if(zufall2<0.5){ny++;}
		}
		child = new Plant(nx,ny);
		return child;
	}	
	public Plant reproduce(int setx, int sety){
		return(new Plant(setx,sety));
	}
	public String toString(){
		String s = " Plant [ "+super.toString()+" ] ";
		return s;
	}
	public void die(){
		this.col = Color.BLACK;
	}
	//public Color getCol(){
	//	return this.col;
	//}
}

class Carnivor extends Dot{    //ich weis, heist eigentlich Fleischfresser, obwohl ich als Gegenstueck Planzen habe ... klingt aber cooler
	Color col = Color.RED;
	static int fullife = 10000; //absichtlich mit nur zwei l
	int life = fullife;
	static double dierate = 0.25;
	static double bornrate = 0.75;
	static int loss = (int)((double)fullife*dierate);
	static int bornlife = (int)((double)fullife*bornrate);
	static int eatingrate = 2;
	static boolean dieprobable = true;
	static boolean probabilitydie = false;
	public Carnivor(int setx, int sety){
		super(setx, sety);
	}
	public String toString(){
		String s = " Carnivor [ "+super.toString()+" ] ";
		return s;
	}
	public void die(){
		this.col = Color.BLACK;
		life = 0;
	}
	public void getOlder(){
		boolean died = false;
		//System.out.println((int)((double)life*dierate));
		//System.out.println(life);
		if(life > 0){
			life -= loss;
			if(dieprobable){
				int zufall = (int)((double)fullife*Math.random());
				//System.out.println(" "+(double)life+" "+zufall);
				if(probabilitydie){
					if(zufall>life){died = true;}
				}
				else{
					if(zufall<loss){died = true;}
				}
			}
		}
		if(life <= 0 || died){this.die();}
	}
	public void eat(Plant pl, boolean goTo){
		pl.die();
		this.life += loss;
		if(goTo){
			super.x = pl.x;
			super.y = pl.y; 
		}
	}
	public Carnivor eat(Plant pl){
		pl.die();
		this.life += loss;
		int nx = pl.x;
		int ny = pl.y;
		Carnivor car = new Carnivor(nx,ny);
		car.life = bornlife;
		//System.out.print("e");
		return(car); 
	}
	//public Color getCol(){
	//	return this.col;
	//}
}

class Raster{
	//int eating = 0;
	//int neighbooor = 0;
	String name;
	int min = 1;
	int max = 100;
	int offset = 10+2+5;
	int width = max+offset+offset; //fuer den Rahmen
	int height = width;
	int steps = 0;
	int allsteps = 0;
	int gefressen = 0;
	int gestorben = 0;
	private boolean periodic = true;
	boolean verhindert = false;
	JFrame frame;
	BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB );
	Bild bi;
	ArrayList<Plant> plants = new ArrayList<Plant>();
	ArrayList<Carnivor> carnivors = new ArrayList<Carnivor>();
	ArrayList<Carnivor> hindernisse = new ArrayList<Carnivor>();
	int[][][] gitter = new int[max][max][2];
	JLabel plantlabel = new JLabel(" Pflanzen     : "+plants.size()); //
	JLabel eatplantlabel = new JLabel(" - gegessen : "+gefressen); //
	JLabel carnlabel = new JLabel(" Vegetarier : "+carnivors.size()); //
	JLabel diecarnlabel = new JLabel(" - gestorben: "+gestorben); //
	JLabel hindlabel = new JLabel(" Hindernisse : "+hindernisse.size()); //
	JLabel steplabel = new JLabel(" Schritte : "+steps+" / "+allsteps); //
	/*
	*/
	public Raster(String setName, int finalstep){
		allsteps = finalstep;
		this.name = setName;
		this.frame = new JFrame(name);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(500, 300);
		for(int cx=0; cx<max; cx++){
			for(int cy=0; cy<max; cy++){
				gitter[cx][cy][0]=0;
				gitter[cx][cy][1]=0;

			}
		}
	}
	public String toString(){
		String s = " Raster:"+name+" ";
		return s;
	}
	public void addPlant(int x, int y){
		Plant pl = new Plant(x,y);
		plants.add(pl);
		gitter[pl.x-1][pl.y-1][0]=1;
		gitter[pl.x-1][pl.y-1][1]=plants.size()-1;
	}
	public void addPlant(Plant pl){
		plants.add(pl);
		//System.out.print(" ("+pl.x+","+pl.y+") ");
		gitter[pl.x-1][pl.y-1][0]=1;
		gitter[pl.x-1][pl.y-1][1]=plants.size()-1;
	}
	public void removePlant(int num){
		plants.remove(num);
		for(int i=num; i<plants.size(); i++){
			gitter[plants.get(i).x-1][plants.get(i).y-1][1]=i;
		}
	}
	public void addCarnivor(int x, int y){
		Carnivor car = new Carnivor(x,y);
		carnivors.add(car);
		gitter[car.x-1][car.y-1][0]=2;
		gitter[car.x-1][car.y-1][1]=carnivors.size()-1;
	}
	public void addCarnivor(Carnivor car){
		carnivors.add(car);
		gitter[car.x-1][car.y-1][0]=2;
		gitter[car.x-1][car.y-1][1]=carnivors.size()-1;
	}
	public void removeCarnivor(int num){
		carnivors.remove(num);
		for(int i=num; i<carnivors.size(); i++){
			gitter[carnivors.get(i).x-1][carnivors.get(i).y-1][1]=i;
		}
	}
	public void addHindernis(int x, int y){
		Carnivor car = new Carnivor(x,y);
		car.col = Color.BLACK;
		hindernisse.add(car);
		gitter[car.x-1][car.y-1][0]=2;
		gitter[car.x-1][car.y-1][1]=hindernisse.size()-1;
	}
	public void addHindernis(Carnivor car){
		car.col = Color.BLACK;
		hindernisse.add(car);
		gitter[car.x-1][car.y-1][0]=2;
		gitter[car.x-1][car.y-1][1]=hindernisse.size()-1;
	}
	public void step(){
		steps++;
		if(steps%10==0){
			//System.out.println(" Schritt : "+steps);
		}
		int count;
		int plantspop = plants.size();
		int plpop = plantspop;
		int carnivorspop = carnivors.size();
		int carpop = carnivorspop;
		int population = plants.size()+carnivors.size();
		int pop = population;
		//boolean carn = false
		for(int cc=0; cc<population; cc++){
			count = (int)((double)pop*Math.random());
			if(count>=plpop&&carpop>0){
				count -= plpop;
				ArrayList<int[]> possibilities = new ArrayList<int[]>();
				//System.out.print(" car("+count+","+carnivors.size()+","+carpop+") ");
				for(int cx=-1; cx<2; cx++){
					int ccx = cx+carnivors.get(count).x;
					if(ccx>max){ccx=min;}
					else if(ccx<min){ccx=max;}
					for(int cy=-1; cy<2; cy++){
						int ccy = cy+carnivors.get(count).y;
						if(ccy>max){ccy=min;}
						else if(ccy<min){ccy=max;}
						int fuellung = gitter[ccx-1][ccy-1][0];
						if(fuellung==1){
							int[] fill = {1,ccx,ccy};
							for(int eat=0; eat<Carnivor.eatingrate; eat++){
								possibilities.add(fill);
							}
							//if(steps>1&&steps<10){
							//	System.out.println(" pl : "+ccx+","+ccy);
							//}
							//if(ccx<5 && ccy<5){
							//	System.out.println(" ("+carnivors.get(count).x+","+carnivors.get(count).y+") ");
							//}
						}
						else if(fuellung==0){
							int[] fill = {0,ccx,ccy};
							possibilities.add(fill);
						}
					}
				}
				if(possibilities.size()>0){
					int wahl = (int)((double)possibilities.size()*Math.random());
					if(possibilities.get(wahl)[0]==1){
						int num = gitter[possibilities.get(wahl)[1]-1][possibilities.get(wahl)[2]-1][1];
						//System.out.println("\n"+"type "+gitter[possibilities.get(wahl)[1]-1][possibilities.get(wahl)[2]-1][0]);
						//System.out.println("num "+gitter[possibilities.get(wahl)[1]-1][possibilities.get(wahl)[2]-1][1]);
						Carnivor ncar = carnivors.get(count).eat(plants.get(num));
						gefressen++;
						this.removePlant(num);
						if(num<plpop){
							plpop--;
							population--;
						}
						this.addCarnivor(ncar);
						//if(steps<100||steps>900){);
						Carnivor ocar = carnivors.get(count);
						//if(steps>1&&steps<10){
							//System.out.println(" o : "+ocar);
							//System.out.println((possibilities.get(wahl)[1]-1)+","+(possibilities.get(wahl)[2]-1));
							//System.out.println(" p : "+plants.get(num));
							//System.out.println(" n : "+ncar);
						//}
						//System.out.println("eat "+(++eating));
						//System.out.println("choices "+possibilities.size());
						//System.out.println("choice "+wahl);
					}
					else if(possibilities.get(wahl)[0]==0){	
						int ox = carnivors.get(count).x;
						int oy = carnivors.get(count).y;
						int nx = possibilities.get(wahl)[1];
						int ny = possibilities.get(wahl)[2];
						carnivors.get(count).move(nx-ox,ny-oy);
						gitter[ox-1][oy-1][0]=0;
						gitter[ox-1][oy-1][1]=0;
						gitter[nx-1][ny-1][0]=2;
						gitter[nx-1][ny-1][1]=count;
					}
					else{
						//System.out.println(possibilities.get(wahl)[0]);
					}
				}
				Carnivor ocar = carnivors.get(count);
				this.addCarnivor(ocar);
				this.removeCarnivor(count);
				carpop--;
			}
			else{
				ArrayList<int[]> possibilities = new ArrayList<int[]>();
				//System.out.print(" pl("+count+","+plants.size()+","+plpop+") ");
				for(int cx=-1; cx<2; cx++){
					int ccx = cx+plants.get(count).x;
					if(ccx>max){ccx=min;}
					else if(ccx<min){ccx=max;}
					for(int cy=-1; cy<2; cy++){
						int ccy = cy+plants.get(count).y;
						if(ccy>max){ccy=min;}
						else if(ccy<min){ccy=max;}
						int fuellung = gitter[ccx-1][ccy-1][0];
						if(fuellung==0){
							//System.out.println(" "+ccx+" "+ccy);
							int[] fill = {ccx,ccy};
							possibilities.add(fill);
						}
					}
				}
				if(possibilities.size()>0){
					int wahl = (int)((double)possibilities.size()*Math.random());
					double ob = Math.random();
					if(ob<Plant.bornrate){
						int nx = possibilities.get(wahl)[0];
						int ny = possibilities.get(wahl)[1];
						Plant npl = plants.get(count).reproduce(nx,ny);
						this.addPlant(npl);
					}
				}
				Plant opl = plants.get(count);
				this.addPlant(opl);
				this.removePlant(count);
				plpop--;
			}
			//carn = false;
			//if(carpop<0){System.out.print(" carn :"+carpop);}
			//if(plpop<0){System.out.print(" pl :"+plpop);}
			pop=carpop+plpop;
		}
	///////////////////////////////////////////////////////////////////////////////////
		for(int cc=0; cc<carnivors.size(); cc++){
			carnivors.get(cc).getOlder();
			if(carnivors.get(cc).col.getRGB()==Color.BLACK.getRGB()){
				carnivors.remove(cc);
				gestorben++;
			}
		}
		for(int cx=0; cx<max; cx++){
			for(int cy=0; cy<max; cy++){
				gitter[cx][cy][0]=0;
				gitter[cx][cy][1]=0;
			}
		}
		//System.out.println("Test");
		for(int pl=0; pl<plants.size(); pl++){
			gitter[plants.get(pl).x-1][plants.get(pl).y-1][0]=1;
			gitter[plants.get(pl).x-1][plants.get(pl).y-1][1]=pl;
		}
		for(int car=0; car<carnivors.size(); car++){
			//System.out.print(" ("+(carnivors.get(car).x-1)+","+(carnivors.get(car).y-1)+") ");
			gitter[carnivors.get(car).x-1][carnivors.get(car).y-1][0]=2;
			gitter[carnivors.get(car).x-1][carnivors.get(car).y-1][1]=car;
		}
		for(int hin=0; hin<hindernisse.size(); hin++){
			gitter[hindernisse.get(hin).x-1][hindernisse.get(hin).y-1][0]=2;
			gitter[hindernisse.get(hin).x-1][hindernisse.get(hin).y-1][1]=hin;
		}
	}
	public void initializeImage(){
		int farbe = Color.LIGHT_GRAY.getRGB();
		int grenze = max+offset+offset;
		int ystart=0;
		int yend=10;
		int xstart=ystart;
		int xend=yend;
		//System.out.println("Schritt 1");
		for(int cy = ystart; cy < yend; cy++){
			for(int cx = cy; cx < grenze-cy; cx++){
				img.setRGB(cx, cy, farbe);
				//System.out.print(" ("+cx+","+cy+") ");
			}
		}
		//System.out.println("Schritt 2");
		for(int cx = xstart; cx < xend; cx++){
			for(int cy = cx; cy < grenze-cx; cy++){
				img.setRGB(cx, cy, farbe);
				//System.out.print(" ("+cx+","+cy+") ");
			}
		}
		ystart = grenze-11;
		yend = grenze-13;
		//System.out.println("Schritt 3");
		for(int cy = ystart; cy > yend; cy--){
			for(int cx = cy; cx > grenze-cy-2; cx--){
				img.setRGB(cx, cy, farbe);
				//System.out.print(" ("+cx+","+cy+") ");
			}
		}
		xstart = ystart;
		xend = yend;
		//System.out.println("Schritt 4");
		for(int cx = xstart; cx > xend; cx--){
			for(int cy = cx; cy > grenze-cx-2; cy--){
				img.setRGB(cx, cy, farbe);
				//System.out.print(" ("+cx+","+cy+") ");
			}
		}
	////////////////////////////////////////////////////////////////
		farbe = Color.DARK_GRAY.getRGB();
		ystart = grenze-1;
		yend = grenze-11;
		//System.out.println("Schritt 5");
		for(int cy = ystart; cy > yend; cy--){
			for(int cx = cy; cx > grenze-cy-2; cx--){
				img.setRGB(cx, cy, farbe);
				//System.out.print(" ("+cx+","+cy+") ");
			}
		}
		xstart = ystart;
		xend = yend;
		//System.out.println("Schritt 6");
		for(int cx = xstart; cx > xend; cx--){
			for(int cy = cx; cy > grenze-cx-2; cy--){
				img.setRGB(cx, cy, farbe);
				//System.out.print(" ("+cx+","+cy+") ");
			}
		}
		ystart = 10;
		yend = 12;
		//System.out.println("Schritt 7");
		for(int cy = ystart; cy < yend; cy++){
			for(int cx = cy; cx < grenze-cy; cx++){
				img.setRGB(cx, cy, farbe);
				//System.out.print(" ("+cx+","+cy+") ");
			}
		}
		xstart = ystart;
		xend = yend;
		//System.out.println("Schritt 8");
		for(int cx = xstart; cx < xend; cx++){
			for(int cy = cx; cy < grenze-cx; cy++){
				img.setRGB(cx, cy, farbe);
				//System.out.print(" ("+cx+","+cy+") ");
			}
		}
	//////////////////////////////////////////////////////////////////
		farbe = Color.BLACK.getRGB();
		ystart = grenze-13;
		yend = grenze-18;
		//System.out.println("Schritt 9");
		for(int cy = ystart; cy > yend; cy--){
			for(int cx = cy; cx > grenze-cy; cx--){
				img.setRGB(cx, cy, farbe);
				//System.out.print(" ("+cx+","+cy+") ");
			}
		}
		xstart = ystart;
		xend = yend;
		//System.out.println("Schritt 10");
		for(int cx = xstart; cx > xend; cx--){
			for(int cy = cx; cy > grenze-cx; cy--){
				img.setRGB(cx, cy, farbe);
				//System.out.print(" ("+cx+","+cy+") ");
			}
		}
		ystart = 13;
		yend = 18;
		//System.out.println("Schritt 11");
		for(int cy = ystart; cy < yend; cy++){
			for(int cx = cy; cx < grenze-cy; cx++){
				img.setRGB(cx, cy, farbe);
				//System.out.print(" ("+cx+","+cy+") ");
			}
		}
		xstart = ystart;
		xend = yend;
		//System.out.println("Schritt 12");
		for(int cx = xstart; cx < xend; cx++){
			for(int cy = cx; cy < grenze-cx; cy++){
				img.setRGB(cx, cy, farbe);
				//System.out.print(" ("+cx+","+cy+") ");
			}
		}
	////////////////////////////////////////////////////////////////////
		int farbe1 = Color.WHITE.getRGB();
		int farbe2 = Color.DARK_GRAY.getRGB();
		double zufall; 
		//System.out.println("Schritt 13");
		ystart = offset;
		yend = ystart+max;
		xstart = ystart;
		xend = yend;	
		for(int cx = xstart; cx < xend; cx++){
			for(int cy = ystart; cy < yend; cy++){
				zufall = Math.random();
				if(zufall<1./3.){img.setRGB(cx, cy, farbe);}
				else if(zufall<2./3.){img.setRGB(cx, cy, farbe1);}
				else{img.setRGB(cx, cy, farbe2);}
				//System.out.print(" ("+cx+","+cy+") ");
			}
		}
	////////////////////////////////////////////////////////////////////
		bi = new Bild(img, 50, 50);
	///////////////////////////////////////////////////////////Labels
		plantlabel.setVerticalAlignment(SwingConstants.TOP);
		plantlabel.setBounds(200,60,150,20);
		frame.getContentPane().add(plantlabel);
		eatplantlabel.setVerticalAlignment(SwingConstants.TOP);
		eatplantlabel.setBounds(200,85,150,20);
		frame.getContentPane().add(eatplantlabel);
		carnlabel.setVerticalAlignment(SwingConstants.TOP);
		carnlabel.setBounds(200,110,150,20);
		frame.getContentPane().add(carnlabel);
		diecarnlabel.setVerticalAlignment(SwingConstants.TOP);
		diecarnlabel.setBounds(200,135,150,20);
		frame.getContentPane().add(diecarnlabel);
		if(verhindert){
			hindlabel.setVerticalAlignment(SwingConstants.TOP);
			hindlabel.setBounds(200,160,150,20);
			frame.getContentPane().add(hindlabel);
		}
		steplabel.setVerticalAlignment(SwingConstants.TOP);
		steplabel.setBounds(60,200,150,20);
		/*
		*/
	///////////////////////////////////////////////////////////Labels
		frame.getContentPane().add(bi);
		frame.setVisible(true);
		//frame.validate();
	}
	public void draw(){
		int farbe = Color.WHITE.getRGB();
		int ystart = offset;
		int yend = ystart+max;
		int xstart = ystart;
		int xend = yend;	
		for(int cx = xstart; cx < xend; cx++){
			for(int cy = ystart; cy < yend; cy++){
				img.setRGB(cx, cy, farbe);
			}
		}
		for(int i=0; i<plants.size(); i++){
			img.setRGB(plants.get(i).x+offset-1, plants.get(i).y+offset-1, plants.get(i).col.getRGB());
		}
		for(int i=0; i<carnivors.size(); i++){
			img.setRGB(carnivors.get(i).x+offset-1, carnivors.get(i).y+offset-1, carnivors.get(i).col.getRGB());
		}
		for(int i=0; i<hindernisse.size(); i++){
			img.setRGB(hindernisse.get(i).x+offset-1, hindernisse.get(i).y+offset-1, hindernisse.get(i).col.getRGB());
		}
		bi = new Bild(img, 50, 50);
	///////////////////////////////////////////////////////////Labels
		frame.getContentPane().remove(plantlabel);
		plantlabel = new JLabel(" Pflanzen     : "+plants.size()); //
		plantlabel.setBounds(200,60,150,20);
		frame.getContentPane().add(plantlabel);
		frame.getContentPane().remove(eatplantlabel);
		eatplantlabel = new JLabel(" - gegessen : "+gefressen); //
		eatplantlabel.setBounds(200,85,150,20);
		frame.getContentPane().add(eatplantlabel);
		frame.getContentPane().remove(carnlabel);
		carnlabel = new JLabel(" Vegetarier : "+carnivors.size()); //
		carnlabel.setBounds(200,110,150,20);
		frame.getContentPane().add(carnlabel);
		frame.getContentPane().remove(diecarnlabel);
		diecarnlabel = new JLabel(" - gestorben: "+gestorben); //
		diecarnlabel.setBounds(200,135,150,20);
		frame.getContentPane().add(diecarnlabel);
		if(verhindert){
			frame.getContentPane().remove(hindlabel);
			hindlabel = new JLabel(" Hindernisse : "+hindernisse.size()); //
			hindlabel.setBounds(200,160,150,20);
			frame.getContentPane().add(hindlabel);
		}
		frame.getContentPane().remove(steplabel);
		steplabel = new JLabel(" Schritte : "+steps+" / "+allsteps); //
		steplabel.setBounds(60,200,150,20);
		frame.getContentPane().add(steplabel);
		/*
		*/
	///////////////////////////////////////////////////////////Labels
		frame.getContentPane().add(bi);
		//frame.setVisible(true);
		frame.validate();		
	}
	public void setPeriodicity(boolean period){
		periodic = period;
		Dot.periodic = period;
	}
	public void outgitter(){
		for(int cx=0; cx<max; cx++){
			for(int cy=0; cy<max; cy++){
				System.out.print(" "+gitter[cx][cy][0]);
			}
			System.out.println(" ");
		}
	}
}

public class BioSim{
	public static void main(String[] args){
		boolean ende = false;
		boolean hindern = false;
		boolean berandung = false;
		boolean bkreis = false;
		boolean bquad = false;
		int schritte = 300;
		int verteilung = 0;
		int pflanzen = 1;
		int vegetarier = 1;
		int leerweis = 1;
		int hindernisse = 0;
		int radius = 10;
		Scanner s = new Scanner(System.in);
		System.out.println(" ");
		if(args.length<1){
			for(int zy=0; zy<5; zy++){
				for(int zx=0; zx<24; zx++){
					if( zx<2||( (zx>2&&zx<23)&&(zy==1||zy==3) ) ){System.out.print(" ");}
					else if(zy==0||zy==4){System.out.print("*");}
					else if(zy==2&&zx==4){System.out.print("  Bio - Simulation  ");}
					else if( (zx==2||zx==23)&&((zy==1||zy==3)||zy==2) ){System.out.print("*");}
				}
				System.out.print("\n");
			}
			System.out.println("\n Welcome User! \n \n Dieses Programm dient zur Simulation zweier Lebensformen. ");
			System.out.println(" Die eine vermehrt sich lediglich ohne sich zu bewegen oder zu altern, ");
			System.out.println(" die andere altert, bewegt sich und vermehrt sich durch Fressen der ersteren.");
		}
		while(!ende){
			int wahl = 0;
			Raster rast = new Raster("Bio-Simulation", schritte);
			rast.verhindert = hindern;
			//rast.initializeImage();
			System.out.println("\n"+" ========================================================================= "+"\n");
			System.out.println("\n"+" Was moechten Sie als naechstes tun? "+"\n");
			System.out.println(" [1] : Simulation starten");
			System.out.println("\n"+" Parameter - Pflanzen");
			System.out.println(" [2] : Nachwusrate                                                 : "+Plant.bornrate);
			System.out.println("\n"+" Parameter - Vegetarier");
			System.out.println(" [3] : Sterberate (gleichzeitig Heilung bei Fressen)               : "+Carnivor.dierate);//+" "+Carnivor.loss
			System.out.println(" [4] : Anteil des vollen Lebens der bei Assimilation erhalten wird : "+Carnivor.bornrate);
			System.out.println(" [5] : Faktor um dessen Fressen Bewgung vorgezogen wird            : "+Carnivor.eatingrate);
			System.out.print(" [6] : Zufaelliges sterben                                         : ");
			if(Carnivor.dieprobable){System.out.println("ON");}
			else{System.out.println("OFF");}
			System.out.print(" [7] : Erhoehtes sterben bei wenig leben                           : ");
			if(Carnivor.probabilitydie){System.out.println("ON");}
			else{System.out.println("OFF");}
			System.out.println("\n"+" Parameter - Simulation");
			System.out.println(" [8] : Schritte                                                    : "+schritte);
			System.out.println(" [9] : Verteilungen                                                : "+verteilung);
			if(verteilung==1){
				System.out.println("        - Pflanzen    : "+pflanzen);
				System.out.println("        - Vegetarier  : "+vegetarier);
				System.out.println("        - Leer        : "+leerweis);
				if(hindern){System.out.println("        - Hindernisse : "+hindernisse);}
			}
			else{
				System.out.println("        - Radius : "+radius);
			}
			System.out.print(" [10]: Hindernisse                                                 : ");
			if(hindern){
				System.out.println("ON");
				System.out.print("        - Rand                                                     : ");
				if(berandung){
					System.out.println("ON");
				}
				else{
					System.out.println("OFF");
				}
				System.out.print("        - Kreise                                                   : ");
				if(bkreis){
					System.out.println("ON");
				}
				else{
					System.out.println("OFF");
				}
				System.out.print("        - Quadrate                                                 : ");
				if(bquad){
					System.out.println("ON");
				}
				else{
					System.out.println("OFF");
				}
			}
			else{
				System.out.println("OFF");
			}
			System.out.println("\n"+" [0] : Programm beenden");
			System.out.print("\n"+" Auswahl :");
			wahl = lesInt(0,10);
			System.out.println(" ");
			if(wahl==1){
				rast.initializeImage();
				if(verteilung==1){
					double pflanzenverteilung = (double)pflanzen/((double)pflanzen+(double)vegetarier+(double)leerweis+(double)hindernisse);
					//System.out.println(pflanzenverteilung);
					double vegetarierverteilung = pflanzenverteilung + (double)vegetarier/((double)pflanzen+(double)vegetarier+(double)leerweis+(double)hindernisse);
					//System.out.println(vegetarierverteilung);
					double hindernisverteilung = vegetarierverteilung + (double)hindernisse/((double)pflanzen+(double)vegetarier+(double)leerweis+(double)hindernisse);
					int ug = 1;
					int og = 101;
					if(berandung){
						for(int cx=ug; cx<og; cx++){
							Carnivor dumb = new Carnivor(cx,1);
							dumb.die();
							rast.addHindernis(dumb);
							Carnivor dumber = new Carnivor(cx,100);
							dumber.die();
							rast.addHindernis(dumber);
						}
						for(int cy=ug+1; cy<og-1; cy++){
							Carnivor dumb = new Carnivor(1,cy);
							dumb.die();
							rast.addHindernis(dumb);
							Carnivor dumber = new Carnivor(100,cy);
							dumber.die();
							rast.addHindernis(dumber);
						}
						ug++;
						og--;
					}
					for(int cx=ug; cx<og; cx++){
						for(int cy=ug; cy<og; cy++){
							if(bkreis && (( Math.sqrt(((double)cx-25.)*((double)cx-25.)+((double)cy-25.)*((double)cy-25.))<10 )||( Math.sqrt(((double)cx-25.)*((double)cx-25.)+((double)cy-75.)*((double)cy-75.))<10 )||( Math.sqrt(((double)cx-75.)*((double)cx-75.)+((double)cy-25.)*((double)cy-25.))<10 )||( Math.sqrt(((double)cx-75.)*((double)cx-75.)+((double)cy-75.)*((double)cy-75.))<10 )) ){
								Carnivor dumb = new Carnivor(cx,cy);
								dumb.die();
								rast.addHindernis(dumb);
							}
							else if(bquad && ((enthalten(cx,15,35)&&(enthalten(cy,15,35)||enthalten(cy,65,85)))||(enthalten(cx,65,85)&&(enthalten(cy,15,35)||enthalten(cy,65,85))))){
								Carnivor dumb = new Carnivor(cx,cy);
								dumb.die();
								rast.addHindernis(dumb);
							}
							else{
								double zufall = Math.random();
								if(zufall<pflanzenverteilung){rast.addPlant(new Plant(cx,cy));}
								else if(zufall<vegetarierverteilung){rast.addCarnivor(new Carnivor(cx,cy));}
								else if(zufall<hindernisverteilung){
									Carnivor dumb = new Carnivor(cx,cy);
									dumb.die();
									rast.addHindernis(dumb);
								}
							}
						}
					}
				}
				else{
					int ug = 1;
					int og = 101;
					if(berandung){
						for(int cx=ug; cx<og; cx++){
							Carnivor dumb = new Carnivor(cx,1);
							dumb.die();
							rast.addHindernis(dumb);
							Carnivor dumber = new Carnivor(cx,100);
							dumber.die();
							rast.addHindernis(dumber);
						}
						for(int cy=ug+1; cy<og-1; cy++){
							Carnivor dumb = new Carnivor(1,cy);
							dumb.die();
							rast.addHindernis(dumb);
							Carnivor dumber = new Carnivor(100,cy);
							dumber.die();
							rast.addHindernis(dumber);
						}
						ug++;
						og--;
					}
					for(int cx=ug; cx<og; cx++){
						for(int cy=ug; cy<og; cy++){
							if(bkreis && (( Math.sqrt(((double)cx-25.)*((double)cx-25.)+((double)cy-25.)*((double)cy-25.))<10 )||( Math.sqrt(((double)cx-25.)*((double)cx-25.)+((double)cy-75.)*((double)cy-75.))<10 )||( Math.sqrt(((double)cx-75.)*((double)cx-75.)+((double)cy-25.)*((double)cy-25.))<10 )||( Math.sqrt(((double)cx-75.)*((double)cx-75.)+((double)cy-75.)*((double)cy-75.))<10 )) ){
								Carnivor dumb = new Carnivor(cx,cy);
								dumb.die();
								rast.addHindernis(dumb);
							}
							else if(bquad && ((enthalten(cx,15,35)&&(enthalten(cy,15,35)||enthalten(cy,65,85)))||(enthalten(cx,65,85)&&(enthalten(cy,15,35)||enthalten(cy,65,85))))){
								Carnivor dumb = new Carnivor(cx,cy);
								dumb.die();
								rast.addHindernis(dumb);
							}
							else{
								double zufall = Math.random();
								if( Math.sqrt( ((double)cx-50.)*((double)cx-50.)+((double)cy-50.)*((double)cy-50.)) <= radius){
									rast.addCarnivor(new Carnivor(cx,cy));
								}
								//else if( Math.sqrt( ((double)cx-20.)*((double)cx-20.)+((double)cy-30.)*((double)cy-30.)) < 20){
								else{
									rast.addPlant(new Plant(cx,cy));
								}
							}
						}
					}
				}
				rast.draw();
				for(int k=0; k<schritte; k++){
					rast.step();
					rast.draw();
				}
			}
			else if(wahl==2){
				System.out.println(" Geben Sie bitte die gewuenschte Nachwuchsrate an (zwischen 0 und 1)."+"\n");
				System.out.print(" Nachwuchsrate :");
				double pbrles = lesDouble(0.,1.);	
				Plant.bornrate = pbrles;
			}
			else if(wahl==3){
				System.out.println(" Geben Sie bitte die gewuenschte Sterberate an (zwischen 0 und 1)."+"\n");
				System.out.print(" Sterberate :");
				double drles = lesDouble(0.,1.);	
				Carnivor.dierate = drles;
				Carnivor.loss = (int)((double)Carnivor.fullife*drles);	
			}
			else if(wahl==4){
				System.out.println(" Geben Sie bitte den gewuenschten Anteil an (zwischen 0 und 1)."+"\n");
				System.out.print(" Anteil :");	
				double brles = lesDouble(0.,1.);
				Carnivor.bornrate = brles;
				Carnivor.bornlife = (int)((double)Carnivor.fullife*brles);	
			}
			else if(wahl==5){
				System.out.println(" Geben Sie bitte den gewuenschte Faktor an (zwischen 1 und 5)."+"\n");
				System.out.print(" Faktor :");	
				int erles = lesInt(1,5);
				Carnivor.eatingrate = erles;	
			}
			else if(wahl==6){
				if(Carnivor.dieprobable){
					Carnivor.dieprobable=false;
					Carnivor.probabilitydie=false;
				}
				else{Carnivor.dieprobable=true;}	
			}
			else if(wahl==7){
				if(Carnivor.dieprobable){
					if(Carnivor.probabilitydie){Carnivor.probabilitydie=false;}
					else{Carnivor.probabilitydie=true;}
				}
				else{System.out.println(" Kann nur aktiviert werden falls zufaelliges Sterben ON.");}	
			}
			else if(wahl==8){
				System.out.println(" Geben Sie bitte die gewuenschte Schrittzahl an (zwischen 0 und 1000)."+"\n");
				System.out.print(" Schritte :");
				int lessch = lesInt(0,1000);	
				schritte = lessch;
			}
			else if(wahl==9){
				System.out.println(" Waehlen Sie bitte aus den Moeglichkeiten : ");
				System.out.println("\n"+" [0] : Kreis mit Vegetariern gefuellt, darum Pflanzen.");
				System.out.println(" [1] : Zufaellige Verteilung beider (auch Freiraum), mit waehlbarem Verhaeltnis."+"\n");
				System.out.print(" Auswahl :");
				int vertles = lesInt(0,1);
				verteilung = vertles;
				System.out.println(" ");
				if(verteilung==1){
					System.out.println(" Geben Sie bitte ganze Zahlen (zwischen 0 und 10000) fuer die gewichtete Verteilung an: "+"\n");
					System.out.print(" Pflanzen    :");
					pflanzen = lesInt(0,10000);
					System.out.print(" Vegetarier  :");
					vegetarier = lesInt(0,10000);
					System.out.print(" Leer        :");
					leerweis = lesInt(0,10000);
					if(hindern){
						System.out.print(" Hindernisse :");
						hindernisse = lesInt(0,10000);
					}
					
				}
				else{
					System.out.println(" Geben Sie bitte eine ganzen Zahlen (zwischen 0 und 50) fuer den Radius an: "+"\n");
					System.out.print(" Radius :");
					radius = lesInt(0,50);
				}
			}
			else if(wahl==10){
				if(hindern){
					int wahlhind = 0;
					System.out.println(" Welche Art von Hindernissen moechten Sie? "+"\n");
					System.out.println(" [1] : Berandung (ueber den Rand wird nicht mehr periodisch fortgesetzt)");
					System.out.println(" [2] : Kreise oder Quadrate ");
					System.out.println(" [0] : Hindernisse deaktivieren \n");
					System.out.print(" Auswahl :");
					wahlhind = lesInt(0,2);
					System.out.print("\n");
					if(wahlhind==1){
						if(berandung){berandung=false;}
						else{berandung=true;}
					}
					else if(wahlhind==2){
						int wahlkrqu = 0;
						System.out.println(" Nur eins waehlbar! \n");
						System.out.println(" [0] : Kreise ");
						System.out.println(" [1] : Quadrate \n");
						System.out.print(" Auswahl :");
						wahlkrqu = lesInt(0,1);
						if(wahlkrqu==0){
							if(bkreis){
								bkreis=false;
							}
							else{
								bkreis=true;
								bquad=false;
							}
						}
						else {
							if(bquad){
								bquad=false;
							}
							else{
								bquad=true;
								bkreis=false;
							}
						}
					}
					else{
						hindern=false;
						berandung=false;
						bkreis=false;
						bquad=false;
					}
				}
				else{
					hindern=true;
					int wahlhind = 0;
					System.out.println(" Welche Art von Hindernissen moechten Sie? "+"\n");
					System.out.println(" [1] : Berandung (ueber den Rand wird nicht mehr periodisch fortgesetzt)");
					System.out.println(" [2] : Kreise oder Quadrate \n ");
					System.out.print(" Auswahl :");
					wahlhind = lesInt(1,2);
					System.out.print("\n");
					if(wahlhind==1){
						if(berandung){berandung=false;}
						else{berandung=true;}
					}
					else{
						int wahlkrqu = 0;
						System.out.println(" Nur eins waehlbar! \n");
						System.out.println(" [0] : Kreise ");
						System.out.println(" [1] : Quadrate \n");
						System.out.print(" Auswahl :");
						wahlkrqu = lesInt(0,1);
						if(wahlkrqu==0){
							if(bkreis){
								bkreis=false;
							}
							else{
								bkreis=true;
								bquad=false;
							}
						}
						else {
							if(bquad){
								bquad=false;
							}
							else{
								bquad=true;
								bkreis=false;
							}
						}
					}
				}
			}
			else{
				ende = true;
			}
		}
		System.out.println(" Danke, dass Sie dieses Programm genutzt haben. ");
	}
	public static int lesInt(int kl, int gr){
		int gelesen=0;
		Scanner s = new Scanner(System.in);
		boolean schlecht = true;
		while(schlecht){
			System.out.print(" ");
			try{
				gelesen = s.nextInt();
				if(kl<=gelesen && gelesen<=gr){schlecht = false;}
				else{System.out.println(" \n Der Wert muss zwischen "+kl+" und "+gr+" liegen! \n ");}
			}
			catch(InputMismatchException e){
				System.out.println("\n Sie muessen eine ganze Zahl eingeben! \n ");
				schlecht = false;
			}
		}
		return gelesen;
	}
	public static double lesDouble(double kl, double gr){
		double gelesen=0.;
		Scanner s = new Scanner(System.in);
		boolean schlecht = true;
		while(schlecht){
			System.out.print(" ");
			try{
				gelesen = s.nextDouble();
				if(kl<=gelesen && gelesen<=gr){schlecht = false;}
				else{System.out.println(" \n Der Wert muss zwischen "+kl+" und "+gr+" liegen! \n ");}
			}
			catch(InputMismatchException e){
				String ausnahme = s.nextLine();
				System.out.println(" \n Sie muessen eine Zahl eingeben! \n Das Dezimaltrennzeichen kommt auf die Einstellung an! \n ");
				schlecht = false;
			}
		}
		return gelesen;
	}
	public static boolean enthalten( double x, int a, int b){
		boolean ans = false;
		if( a<b && (x>=a&&x<=b) ){ans=true;}
		else if(a>b && (x>a||x<b)){ans=true;}
		return ans;			
	}
}
