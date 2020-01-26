#include <iostream>
#include <iomanip>
#include <cmath>
#include <vector>
#include <stdlib.h>
#include <stdio.h>
#include <string>
#include <fstream>

using namespace std;

typedef vector<double> vecd;
typedef vector<int> veci;
typedef vector< vector<double> > vecvecd;
typedef vector< vector<int> > vecveci;

void loeschend(vecd& v, int l);
void bereichloeschend(vecd& v, int a, int o);
void sortierend(vecd& v);
void loescheni(veci& v, int l);
void bereichloescheni(veci& v, int a, int o);
void sortiereni(veci& i);

void header();
void beispiel();

void histerstellen();
void ploterstellen();

class histogramm {
		vecd inhalt;
		int bins, overflow, underflow;
		double min, max;
		veci binhoehe;
		double binbreite;
	public:
		histogramm(int n, double a, double b){
			bins = n;
			if(n>70){cout << "Zu viele Bins! Histogramnn kann nicht angezeogt werden!" << endl;}
			if(n<=0){cout << "Binanzahl darf nur positive Werte annehmen!" << endl;}
			min = a;
			max = b;
			binbreite = (max - min)/bins;
			for(int k=0; k<bins; k++){
				binhoehe.push_back(0);
			}
			overflow=0;
			underflow=0;
		}
		void fuellen(double a){
			inhalt.push_back(a);
			if(a < min){underflow++;}
			else if(a > max){overflow++;}
			else{
				int i = 0;
				double klein, gross;
				klein = min;
				gross = min + binbreite;
				while(gross < a){
					i++;
					klein = klein + binbreite;
					gross = gross + binbreite;
					if(gross == max){break;}
				}
				binhoehe[i]++;
			}		
		}
		void zeichnen(int o=0, int maximalhoehe=40){
			bool offset;
			int kleinster = binhoehe[0], groesster = binhoehe[0], hoehe;
			double eintragsbreite, maximalhoehedo, MEAN=0., RMS=0., Median=0.;
			maximalhoehedo = maximalhoehe;
			//sortierend(inhalt);
			if(o==1){offset=true;}
			else{offset=false;}
			if(bins>70){cout << "Zu viele Bins! Histogramnn kann nicht angezeogt werden!" << endl;}
			else if(bins<=0){cout << "Binanzahl darf nur positive Werte annehmen!" << endl;}
			else if(maximalhoehe>40){cout << "Histogramm zu hoch eingestellt!" << endl;}
			else{
				cout << endl << endl << "-----------------------------HISTOGRAMM--------------------------------" << endl << endl;
				if(inhalt.size()==0){cout << "Keine Eintraege im Histogramm!" << endl;}
				else{
					for(int i = 0; i<inhalt.size(); i++){
						MEAN+=inhalt[i];
						RMS+=inhalt[i]*inhalt[i];
					}
					MEAN/=inhalt.size();
					RMS/=inhalt.size();
					RMS = sqrt(RMS-MEAN*MEAN);
					//Median=inhalt[inhalt.size()/2];
				}
				for(int i = 1; i < binhoehe.size(); i++){
					if(kleinster > binhoehe[i]){kleinster = binhoehe[i];}
					if(groesster < binhoehe[i]){groesster = binhoehe[i];}
				}
				if(offset==true){kleinster=0;}
				if(groesster <= maximalhoehe){hoehe = groesster; kleinster = 0; eintragsbreite = 1.;}
				else{eintragsbreite = (groesster - kleinster)/maximalhoehedo; hoehe=maximalhoehe;}
				for(int z=hoehe; z>=0; z--){
					cout << setw((70-bins-1)/2) << " ";
					cout << z%10 << "|";
					for(int b=0; b<binhoehe.size(); b++){
						if((binhoehe[b] < (z+0.5)*eintragsbreite+kleinster) && (binhoehe[b] >= (z-0.5)*eintragsbreite+kleinster)){
							if(binhoehe[b]==groesster){cout << "+";}
							else{cout << "-";}
						}
						else if((binhoehe[b] >= (z+0.5)*eintragsbreite+kleinster) && (binhoehe[b] >= (z-0.5)*eintragsbreite+kleinster)){
							cout << "I";
						}
						else{cout << " ";}
					}
					cout << endl;	
				}
				cout << setw((70-bins-1)/2) << " ";
				cout << "  ";
				for(int n=1; n<=bins; n++){cout << "=";}
				cout << endl << "  ";
				cout << setw((70-bins-1)/2) << " ";
				for(int c=1; c<=bins; c++){cout << c%10;}
				cout << endl;
				cout << endl;
				cout << "-----------------------------DATEN-------------------------------------" << endl;
				cout << endl;
				cout << "Eintraege   : " << setw(14) << setprecision(8) << inhalt.size() << setw(8) << " "; 
				cout << "Mittelwert         : " << setw(14) << setprecision(8) << MEAN << endl;
				cout << "Underflow   : " << setw(14) << setprecision(8) << underflow << setw(8) << " "; 
				cout << "Standardabweichung : " << setw(14) << setprecision(8) << RMS << endl;
				cout << "Overflow    : " << setw(14) << setprecision(8) << overflow << setw(8) << " "; 
				cout << "Median             : " << setw(14) << setprecision(8) << Median << endl;
				cout << endl;
				cout << setw(9) << " " << "  X-Achse" << setw(34) << " " << "  Y-Achse" << endl;
				cout << "Binbreite   : " << setw(14) << setprecision(8) << binbreite << setw(8) << " "; 
				cout << "Hoehenschritt      : " << setw(14) << setprecision(8) << eintragsbreite << endl;
				cout << "Minimalwert : " << setw(14) << setprecision(8) << min << setw(8) << " "; 
				cout << "Untergrenze        : " << setw(14) << setprecision(8) << kleinster << endl;
				cout << "Maximalwert : " << setw(14) << setprecision(8) << max << setw(8) << " "; 
				cout << "Obergrenze         : " << setw(14) << setprecision(8) << groesster << endl;
				cout << endl;
			}
		}
		void indateizeichnen(int o=0, int maximalhoehe=40, string dateiname="histogramm.txt"){
			bool einlesen=false;
			fstream datei;
			cout << endl;
			cout << "Einen Moment bitte." << endl << endl;
			datei.open(dateiname.c_str(), ios::out);
			cout << "Bei der Arbeit ... " << endl << endl;
			if(datei.is_open()){
				einlesen=true;
				cout << "Die Datei konnte geoeffnet werden!" << endl << endl;
			}
			else{cout << "Die Datei konnte nicht geoeffnet werden!" << endl << endl;}
			if(einlesen){
				bool offset;
				int kleinster = binhoehe[0], groesster = binhoehe[0], hoehe;
				double eintragsbreite, maximalhoehedo, MEAN=0., RMS=0., Median=0.;
				maximalhoehedo = maximalhoehe;
				//sortierend(inhalt);
				if(o==1){offset=true;}
				else{offset=false;}
				if(bins>70){cout << "Zu viele Bins! Histogramnn kann nicht angezeogt werden!" << endl;}
				else if(bins<=0){cout << "Binanzahl darf nur positive Werte annehmen!" << endl;}
				else if(maximalhoehe>40){cout << "Histogramm zu hoch eingestellt!" << endl;}
				else{
					datei << endl << endl << "-----------------------------HISTOGRAMM--------------------------------" << endl << endl;
					if(inhalt.size()==0){cout << "Keine Eintraege im Histogramm!" << endl;}
					else{
						for(int i = 0; i<inhalt.size(); i++){
							MEAN+=inhalt[i];
							RMS+=inhalt[i]*inhalt[i];
						}
						MEAN/=inhalt.size();
						RMS/=inhalt.size();
						RMS = sqrt(RMS-MEAN*MEAN);
						//Median=inhalt[inhalt.size()/2];
					}
					for(int i = 1; i < binhoehe.size(); i++){
						if(kleinster > binhoehe[i]){kleinster = binhoehe[i];}
						if(groesster < binhoehe[i]){groesster = binhoehe[i];}
					}
					if(offset==true){kleinster=0;}
					if(groesster <= maximalhoehe){hoehe = groesster; kleinster = 0; eintragsbreite = 1.;}
					else{eintragsbreite = (groesster - kleinster)/maximalhoehedo; hoehe=maximalhoehe;}
					for(int z=hoehe; z>=0; z--){
						datei << setw((70-bins-1)/2) << " ";
						datei << z%10 << "|";
						for(int b=0; b<binhoehe.size(); b++){
							if((binhoehe[b] < (z+0.5)*eintragsbreite+kleinster) && (binhoehe[b] >= (z-0.5)*eintragsbreite+kleinster)){
								if(binhoehe[b]==groesster){datei << "+";}
								else{datei << "-";}
							}
							else if((binhoehe[b]>=(z+0.5)*eintragsbreite+kleinster)&&(binhoehe[b]>=(z-0.5)*eintragsbreite+kleinster)){
								datei << "I";
							}
							else{datei << " ";}
						}
						datei << endl;	
					}
					datei << setw((70-bins-1)/2) << " ";
					datei << "  ";
					for(int n=1; n<=bins; n++){datei << "=";}
					datei << endl << "  ";
					datei << setw((70-bins-1)/2) << " ";
					for(int c=1; c<=bins; c++){datei << c%10;}
					datei << endl;
					datei << endl;
					datei << "-----------------------------DATEN-------------------------------------" << endl;
					datei << endl;
					datei << "Eintraege   : " << setw(14) << setprecision(8) << inhalt.size() << setw(8) << " "; 
					datei << "Mittelwert         : " << setw(14) << setprecision(8) << MEAN << endl;
					datei << "Underflow   : " << setw(14) << setprecision(8) << underflow << setw(8) << " "; 
					datei << "Standardabweichung : " << setw(14) << setprecision(8) << RMS << endl;
					datei << "Overflow    : " << setw(14) << setprecision(8) << overflow << setw(8) << " "; 
					datei << "Median             : " << setw(14) << setprecision(8) << Median << endl;
					datei << endl;
					datei << setw(9) << " " << "  X-Achse" << setw(34) << " " << "  Y-Achse" << endl;
					datei << "Binbreite   : " << setw(14) << setprecision(8) << binbreite << setw(8) << " "; 
					datei << "Hoehenschritt      : " << setw(14) << setprecision(8) << eintragsbreite << endl;
					datei << "Minimalwert : " << setw(14) << setprecision(8) << min << setw(8) << " "; 
					datei << "Untergrenze        : " << setw(14) << setprecision(8) << kleinster << endl;
					datei << "Maximalwert : " << setw(14) << setprecision(8) << max << setw(8) << " "; 
					datei << "Obergrenze         : " << setw(14) << setprecision(8) << groesster << endl;
					datei << endl;
				}
				datei.close();
				cout << "Datei wurde beschrieben!" << endl << endl;
			}
		}
		void rebin(int n, double a, double b){
			bins = n;
			if(n>70){cout << "Zu viele Bins! Histogramnn kann nicht angezeogt werden!" << endl;}
			if(n<=0){cout << "Binanzahl darf nur positive Werte annehmen!" << endl;}
			min = a;
			max = b;
			binbreite = (max - min)/bins;
			while(binhoehe.size()>0){
				loescheni(binhoehe, binhoehe.size()-1);
			}
			for(int k=0; k<bins; k++){
				binhoehe.push_back(0);
			}
			overflow=0;
			underflow=0;
			double g, klein, gross;
			int i;
			for(int u=0; u<inhalt.size(); u++){
				g=inhalt[u];
				if(g < min){underflow++;}
				else if(g > max){overflow++;}
				else{
					i = 0;
					klein = min;
					gross = min + binbreite;
					while(gross < g){
						i++;
						klein = klein + binbreite;
						gross = gross + binbreite;
						if(gross == max){break;}
					}
					binhoehe[i]++;
				}
			}		
		}
		void histstatus( int& eintraege, double& xmin, double& xmax, int& ymax, int& ymaxbin, double& mittel, double& standard){
			eintraege = inhalt.size();
			ymax = binhoehe[0];
			ymaxbin = 0;
			for(int i=1; i < binhoehe.size(); i++){
				if(ymax<binhoehe[i]){
					ymax=binhoehe[i];
					ymaxbin=i;
				}
			}
			mittel = 0.;
			standard = 0.;
			xmin = inhalt[0];
			xmax = inhalt[0];
			for(int i=0; i < inhalt.size(); i++){
				mittel += inhalt[i];
				standard += inhalt[i]*inhalt[i];
				if(i>0){
					if(xmin>inhalt[i]){xmin=inhalt[i];}
					if(xmax<inhalt[i]){xmax=inhalt[i];}
				}
			}
			mittel /= inhalt.size();
			standard = sqrt(standard/inhalt.size()+mittel*mittel);
		}
		int integrieren(double a, double b){
			int s=0;
			for(int i=0; i<inhalt.size(); i++){
				if((inhalt[i]>=a)&&(inhalt[i]<=b)){s++;}
			}
			return s;
		}	
		int binauslesed(double a){
			int n, i=0;
			if(a<min){n=underflow;}
			else if(a>max){n=overflow;}
			else{
				for(int c=0; c<bins; c++){
					if(a<min+binbreite*(c+1.)){i=c; break;}
				}
				n = binhoehe[i];
			}
			return n;
		}
		int binauslesei(int n){
			return binhoehe[n-1];
		}	
};

class plot{
		vecvecd inhalt;
		double xmin, xmax, ymin, ymax;
		int xbin, ybin;
		int xu, xo, yu, yo, xoyo, xuyo, xuyu, xoyu, sum;
		double xschritt, yschritt;
		vecveci bin;
	public:
		plot(int xb, double xk, double xg, int yb, double yk, double yg){
			if(xb>70){cout << "Zu viele Bins! Histogramnn kann nicht angezeogt werden!" << endl;}
			if(xb<=0){cout << "Binanzahl darf nur positive Werte annehmen!" << endl;}
			if(yb>40){cout << "Zu viele Bins! Histogramnn kann nicht angezeogt werden!" << endl;}
			if(yb<=0){cout << "Binanzahl darf nur positive Werte annehmen!" << endl;}
			xbin=xb;
			xmin=xk;
			xmax=xg;
			xschritt=(xmax-xmin)/xbin;
			ybin=yb;
			ymin=yk;
			ymax=yg;
			yschritt=(ymax-ymin)/ybin;
			xu=0;
			xo=0;
			yu=0;
			yo=0;
			xoyo=0;
			xuyo=0;
			xuyu=0;
			xoyu=0;
			sum=0;
			veci v(ybin);
			for(int i=0; i<xbin; i++){
				bin.push_back(v);
			}
		}
		void einlesen(double x, double y){
			vecd k;
			k.push_back(x);
			k.push_back(y);
			inhalt.push_back(k);
			if(x<xmin){
				if(y<ymin){xuyu++;}
				else if(y>ymax){xuyo++;}
				else{xu++;}
			}
			else if(x>xmax){
				if(y<ymin){xoyu++;}
				else if(y>ymax){xoyo++;}
				else{xo++;}
			}
			else if(y<ymin){yu++;}
			else if(y>ymax){yo++;}
			else{
				sum++;
				double xgrenze=xmin, ygrenze=ymin;
				int i=-1, j=-1;
				while(x>xgrenze){
					xgrenze += xschritt;
					i++;
					if(xgrenze>=xmax){break;}
				}
				while(y>ygrenze){
					ygrenze += yschritt;
					j++;
					if(ygrenze>=ymax){break;}
				}
				bin[i][j]++;
			}
		}
		void plotten(int o=0){
			if(inhalt.size()==0){cout << "Plot leer!" << endl;}
			if(xbin>70){cout << "Zu viele Bins! Histogramnn kann nicht angezeogt werden!" << endl;}
			else if(xbin<=0){cout << "Binanzahl darf nur positive Werte annehmen!" << endl;}
			else if(ybin>40){cout << "Zu viele Bins! Histogramnn kann nicht angezeogt werden!" << endl;}
			else if(ybin<=0){cout << "Binanzahl darf nur positive Werte annehmen!" << endl;}
			else{
				cout << endl << endl << "-----------------------------PLOT--------------------------------------" << endl << endl;
				int i, j;
				for(int k=ybin; k>=-1; k--){
					cout << setw((70-xbin-1)/2) << " ";
					if(k==0){cout << "  ";}
					else if(k==-1){cout << "  ";}
					else{
						j=k-1;
						cout << k%10 << "|";
					}
					for(i=0; i<xbin; i++){
						if(k==0){cout << "=";}
						else if(k==-1){cout << (i+1)%10;}
						else{
							if(o==1){
								if(bin[i][j]==1){cout << "+";}
								else if((bin[i][j]>1)&&(bin[i][j]<10)){cout << bin[i][j];}
								else if(bin[i][j]>=10){cout << "H";}
								else{cout << " ";}
							}
							else if(o==2){
								if((bin[i][j]>0)&&(bin[i][j]<10)){cout << "+";}
								else if((bin[i][j]>=10)&&(bin[i][j]<100)){cout << bin[i][j]/10;}
								else if(bin[i][j]>=100){cout << "H";}
								else{cout << " ";}
							}
							else{
								if(bin[i][j]>0){cout << "+";}
								else{cout << " ";}
							}
						}
					}
					cout << endl;
				}
				cout << endl;
				if(o==1){
					cout << "\"+\" kennzeichnet Bins mit einem Eintrag." << endl;
					cout << "Zahlen geben die Menge der Eintraege im jeweiligen Bin an." << endl;
					cout <<"\"H\" bedeutet mehr als zehn Eintraege." << endl;
				}
				else if(o==2){
					cout << "\"+\" kennzeichnet Bins mit weniger als zehn Eintraegen. (mindestens einen)" << endl;
					cout << "Zahlen geben die Menge der Eintraege im jeweiligen Bin in Zehnerschritten an." << endl;
					cout << "\"H\" bedeutet mehr als hundert Eintraege." << endl;
				}
				else{cout << "\"+\" kennzeichnet Bins mit mindestens einem Eintrag." << endl;}
				cout << endl;
				cout << endl;
				cout << "-----------------------------DATEN-------------------------------------" << endl;
				cout << endl;
				cout << setw(13) << " ";
				cout << setw(16) << " " << "Outflow-Tabelle" << endl; 
				cout << endl;
				cout << setw(13) << " ";
				cout << setw(14) << setprecision(8) << xuyo << " | ";
				cout << setw(14) << setprecision(8) << yo << " | ";
				cout << setw(14) << setprecision(8) << xoyo << endl;
				cout << setw(13) << " ";
				for(int c=0; c<49; c++){cout << "=";}
				cout << endl;
				cout << setw(13) << " ";
				cout << setw(14) << setprecision(8) << xu << " | ";
				cout << setw(14) << setprecision(8) << sum << " | ";
				cout << setw(14) << setprecision(8) << xo << endl;
				cout << setw(13) << " ";
				for(int c=0; c<49; c++){cout << "=";}
				cout << endl;
				cout << setw(13) << " ";
				cout << setw(14) << setprecision(8) << xuyu << " | ";
				cout << setw(14) << setprecision(8) << yu << " | ";
				cout << setw(14) << setprecision(8) << xoyu << endl;
				cout << endl;
				cout << setw(21) << " ";
				cout << "Gesamteintraege : " << setw(14) << setprecision(8) << inhalt.size() << endl;
				cout << endl;
				cout << setw(7) << " " << "  X-Achse" << setw(36) << " " << "  Y-Achse" << endl;
				cout << "X-Schritt : " << setw(14) << setprecision(8) << xschritt << setw(19) << " "; 
				cout << "Y-Schritt : " << setw(14) << setprecision(8) << yschritt << endl;
				cout << "X-Minimum : " << setw(14) << setprecision(8) << xmin << setw(19) << " "; 
				cout << "Y-Minimum : " << setw(14) << setprecision(8) << ymin << endl;
				cout << "X-Maximum : " << setw(14) << setprecision(8) << xmax << setw(19) << " "; 
				cout << "Y-Maximum : " << setw(14) << setprecision(8) << ymax << endl;
				cout << endl;
			}
		}
		void indateiplotten(int o=0, string dateiname="plotdatei.txt"){
			bool einlesen=false;
			fstream datei;
			cout << endl;
			cout << "Einen Moment bitte." << endl << endl;
			datei.open(dateiname.c_str(), ios::out);
			cout << "Bei der Arbeit ... " << endl << endl;
			if(datei.is_open()){
				einlesen=true;
				cout << "Die Datei konnte geoeffnet werden!" << endl << endl;
			}
			else{cout << "Die Datei konnte nicht geoeffnet werden!" << endl << endl;}
			if(einlesen){
				if(inhalt.size()==0){cout << "Plot leer!" << endl;}
				if(xbin>70){cout << "Zu viele Bins! Histogramnn kann nicht angezeogt werden!" << endl;}
				else if(xbin<=0){cout << "Binanzahl darf nur positive Werte annehmen!" << endl;}
				else if(ybin>40){cout << "Zu viele Bins! Histogramnn kann nicht angezeogt werden!" << endl;}
				else if(ybin<=0){cout << "Binanzahl darf nur positive Werte annehmen!" << endl;}
				else{
					datei << endl << endl << "-----------------------------PLOT--------------------------------------" << endl << endl;
					int i, j;
					for(int k=ybin; k>=-1; k--){
						datei << setw((70-xbin-1)/2) << " ";
						if(k==0){datei << "  ";}
						else if(k==-1){datei << "  ";}
						else{
							j=k-1;
							datei << k%10 << "|";
						}
						for(i=0; i<xbin; i++){
							if(k==0){datei << "=";}
							else if(k==-1){datei << (i+1)%10;}
							else{
								if(o==1){
									if(bin[i][j]==1){datei << "+";}
									else if((bin[i][j]>1)&&(bin[i][j]<10)){datei << bin[i][j];}
									else if(bin[i][j]>=10){datei << "H";}
									else{datei << " ";}
								}
								else if(o==2){
									if((bin[i][j]>0)&&(bin[i][j]<10)){datei << "+";}
									else if((bin[i][j]>=10)&&(bin[i][j]<100)){datei << bin[i][j]/10;}
									else if(bin[i][j]>=100){datei << "H";}
									else{datei << " ";}
								}
								else{
									if(bin[i][j]>0){datei << "+";}
									else{datei << " ";}
								}
							}
						}
						datei << endl;
					}
					datei << endl;
					if(o==1){
						datei << "\"+\" kennzeichnet Bins mit einem Eintrag." << endl;
						datei << "Zahlen geben die Menge der Eintraege im jeweiligen Bin an." << endl;
						datei <<"\"H\" bedeutet mehr als zehn Eintraege." << endl;
					}
					else if(o==2){
						datei << "\"+\" kennzeichnet Bins mit weniger als zehn Eintraegen. (mindestens einen)" << endl;
						datei << "Zahlen geben die Menge der Eintraege im jeweiligen Bin in Zehnerschritten an." << endl;
						datei << "\"H\" bedeutet mehr als hundert Eintraege." << endl;
					}
					else{datei << "\"+\" kennzeichnet Bins mit mindestens einem Eintrag." << endl;}
					datei << endl;
					datei << endl;
					datei << "-----------------------------DATEN-------------------------------------" << endl;
					datei << endl;
					datei << setw(13) << " ";
					datei << setw(16) << " " << "Outflow-Tabelle" << endl; 
					datei << endl;
					datei << setw(13) << " ";
					datei << setw(14) << setprecision(8) << xuyo << " | ";
					datei << setw(14) << setprecision(8) << yo << " | ";
					datei << setw(14) << setprecision(8) << xoyo << endl;
					datei << setw(13) << " ";
					for(int c=0; c<49; c++){datei << "=";}
					datei << endl;
					datei << setw(13) << " ";
					datei << setw(14) << setprecision(8) << xu << " | ";
					datei << setw(14) << setprecision(8) << sum << " | ";
					datei << setw(14) << setprecision(8) << xo << endl;
					datei << setw(13) << " ";
					for(int c=0; c<49; c++){datei << "=";}
					datei << endl;
					datei << setw(13) << " ";
					datei << setw(14) << setprecision(8) << xuyu << " | ";
					datei << setw(14) << setprecision(8) << yu << " | ";
					datei << setw(14) << setprecision(8) << xoyu << endl;
					datei << endl;
					datei << setw(21) << " ";
					datei << "Gesamteintraege : " << setw(14) << setprecision(8) << inhalt.size() << endl;
					datei << endl;
					datei << setw(7) << " " << "  X-Achse" << setw(36) << " " << "  Y-Achse" << endl;
					datei << "X-Schritt : " << setw(14) << setprecision(8) << xschritt << setw(19) << " "; 
					datei << "Y-Schritt : " << setw(14) << setprecision(8) << yschritt << endl;
					datei << "X-Minimum : " << setw(14) << setprecision(8) << xmin << setw(19) << " "; 
					datei << "Y-Minimum : " << setw(14) << setprecision(8) << ymin << endl;
					datei << "X-Maximum : " << setw(14) << setprecision(8) << xmax << setw(19) << " "; 
					datei << "Y-Maximum : " << setw(14) << setprecision(8) << ymax << endl;
					datei << endl;
				}
				datei.close();
				cout << "Datei wurde beschrieben!" << endl << endl;
			}
		}
		void rebin(int xb, double xk, double xg, int yb, double yk, double yg){
			if(xb>70){cout << "Zu viele Bins! Histogramnn kann nicht angezeogt werden!" << endl;}
			if(xb<=0){cout << "Binanzahl darf nur positive Werte annehmen!" << endl;}
			if(yb>40){cout << "Zu viele Bins! Histogramnn kann nicht angezeogt werden!" << endl;}
			if(yb<=0){cout << "Binanzahl darf nur positive Werte annehmen!" << endl;}
			double x, y;
			int altxbin=xbin;
			xbin=xb;
			xmin=xk;
			xmax=xg;
			xschritt=(xmax-xmin)/xbin;
			ybin=yb;
			ymin=yk;
			ymax=yg;
			yschritt=(ymax-ymin)/ybin;
			xu=0;
			xo=0;
			yu=0;
			yo=0;
			xoyo=0;
			xuyo=0;
			xuyu=0;
			xoyu=0;
			sum=0;
			for(int i=0; i<altxbin; i++){
				bin.pop_back();
			}
			veci v(ybin);
			for(int i=0; i<xbin; i++){
				bin.push_back(v);
			}
			for(int m=0; m<inhalt.size(); m++){
				x = inhalt[m][0];
				y = inhalt[m][1];
				if(x<xmin){
					if(y<ymin){xuyu++;}
					else if(y>ymax){xuyo++;}
					else{xu++;}
				}
				else if(x>xmax){
					if(y<ymin){xoyu++;}
					else if(y>ymax){xoyo++;}
					else{xo++;}
				}
				else if(y<ymin){yu++;}
				else if(y>ymax){yo++;}
				else{
					sum++;
					double xgrenze=xmin, ygrenze=ymin;
					int i=-1, j=-1;
					while(x>xgrenze){
						xgrenze += xschritt;
						i++;
						if(xgrenze>=xmax){break;}
					}
					while(y>ygrenze){
						ygrenze += yschritt;
						j++;
						if(ygrenze>=ymax){break;}
					}
					bin[i][j]++;
				}
			}
		}
		void plotstatus(int& anzahl, double& xm, double& xs, double& xk, double& xg, double& ym, double& ys, double& yk, double& yg){
			anzahl = inhalt.size();
			for(int i=0; i<inhalt.size(); i++){
				if(i==0){
					xm = inhalt[i][0];
					xs = inhalt[i][0]*inhalt[i][0];
					xk = inhalt[i][0];
					xg = inhalt[i][0];
					ym = inhalt[i][1];
					ys = inhalt[i][1]*inhalt[i][1];
					yk = inhalt[i][1];
					yg = inhalt[i][1];
				}
				else{
					xm += inhalt[i][0];
					xs += inhalt[i][0]*inhalt[i][0];
					if(xk>inhalt[i][0]){xk = inhalt[i][0];}
					if(xg<inhalt[i][0]){xg = inhalt[i][0];}
					ym += inhalt[i][1];
					ys += inhalt[i][1]*inhalt[i][1];
					if(yk>inhalt[i][1]){yk = inhalt[i][1];}
					if(yg<inhalt[i][1]){yg = inhalt[i][1];}
				}
			}
			xm /= inhalt.size();
			xs = sqrt(xs/inhalt.size()- xm*xm);
			ym /= inhalt.size();
			ys = sqrt(ys/inhalt.size()- ym*ym);
		}
		double integral(double a, double b, int o=0){
			double summe=0.;
			int k;
			if(o%2==1){k=1;}
			else{k=0;}
			if(o==2){
				for(int i=0; i<inhalt.size(); i++){
					if((inhalt[i][k]<=b)&&(inhalt[i][k]>=a)){summe+=inhalt[i][1];}
				}
			}
			else if(o==3){
				for(int i=0; i<inhalt.size(); i++){
					if((inhalt[i][k]<=b)&&(inhalt[i][k]>=a)){summe+=inhalt[i][0];}
				}
			}
			else{
				for(int i=0; i<inhalt.size(); i++){
					if((inhalt[i][k]<=b)&&(inhalt[i][k]>=a)){summe+=1.;}
				}
			}
			return summe;
		}
		double flaechenintegral(double xa, double xb, double ya, double yb){
			double summe=0.;
			for(int i=0; i<inhalt.size(); i++){
				if(((inhalt[i][0]>=xa)&&(inhalt[i][0]<=xb))&&((inhalt[i][1]>=ya)&&(inhalt[i][1]<=yb))){summe+=1.;}
			}
			return summe;
		}
};

int main(int argc, char* argv[]){
	cout << endl;
	string wahl;
	if(argc>1){wahl = argv[1];}
	if(wahl!="light"){
		header();
		cout << "Moechten Sie einige Beispiele angezeigt bekommen? Ja [j], Nein [n]" << endl << endl;
		cin >> wahl;
		cout << endl;
		if(wahl=="j"){beispiel();}
		cout << endl << "Falls Sie beim naechsten Programmstart den Kopf und die Biespiele nicht mehr " << endl;
		cout << "angezeigt bekommen moechten, koennen Sie \" light\" dem Programmname hinzufuegen." << endl;
		cout << endl;
	}
	int check=1;
	while(check!=0){
		cout << "Was moechten Sie als naechstes tun?" << endl;
		cout << "[1] : Histogramm erstellen" << endl;
		cout << "[2] : Korrelationsplot erstellen" << endl;
		cout << "[0] : Programm beenden" << endl << endl;
		cin >> check;
		if(check==1){histerstellen();}
		if(check==2){ploterstellen();}
        	cin.clear();
	}
	cout << endl << "Vielen Dank, dass Sie dieses Programm genutzt haben." << endl;
	return 0;
}

void loeschend(vecd& v, int l){
	for(int i=l; i<v.size(); i++){
		if(i==v.size()-1){v.pop_back();}
		else{v[i]=v[i+1];}
	}
}

void bereichloeschend(vecd& v, int a, int o){
	int d = o-a, g = v.size();
	for(int i=a; i<g; i++){
		if(i>=v.size()-d-1){v.pop_back();}
		else{v[i]=v[i+d+1];}
	}
}

void sortierend(vecd& v){
	double d;
	//cout << endl << "Der Vektor wird sortiert ... " << endl << endl;
	for(int i = 0; i<v.size()-1; i++){
		for(int j = i+1; j<v.size(); j++){
			if(v[i]>v[j]){
				d=v[i];
				v[i]=v[j];
				v[j]=d;
			}
		}
		//if(i==v.size()/100){cout << "1% sortiert" << endl;}
		//if(i==v.size()/10){cout << "10% sortiert" << endl;}
		//if(i==v.size()/4){cout << "25% sortiert" << endl;}
		//if(i==v.size()/2){cout << "50% sortiert" << endl;}
		//if(i==v.size()*3/4){cout << "75% sortiert" << endl;}
		//if(i==v.size()-2){cout << "100% sortiert" << endl;}
	}
	//cout << endl << "Der Vektor ist sortiert!" << endl << endl;
}

void loescheni(veci& v, int l){
	for(int i=l; i<v.size(); i++){
		if(i==v.size()-1){v.pop_back();}
		else{v[i]=v[i+1];}
	}
}

void bereichloescheni(veci& v, int a, int o){
	int d = o-a, g = v.size();
	for(int i=a; i<g; i++){
		if(i>=v.size()-d-1){v.pop_back();}
		else{v[i]=v[i+d+1];}
	}
}

void sortiereni(veci& v){
	int d;
	for(int i = 0; i<v.size()-1; i++){
		for(int j = i+1; j<v.size(); j++){
			if(v[i]>v[j]){
				d=v[i];
				v[i]=v[j];
				v[j]=d;
			}
		}
	}
}


void header(){
	for(int k=0; k<5; k++){
		cout << setw(9) << " ";
		if((k==0)||(k==4)){
			for(int j=0; j<50; j++){
				if(j==49){cout << "*" << endl;}
				else{cout << "*";}
			}
		}
		else if((k==1)||(k==3)){
			for(int j=0; j<50; j++){
				if(j==0){cout << "*";} 
				else if(j==49){cout << "*" << endl;} 
				else{cout << " ";}
			}
		}
		else{
			for(int j=0; j<20; j++){
				if(j==0){cout << "*";} 
				else if(j==19){cout << "*" << endl;}
				else if(j==9){cout << "HISTOGRAMME & KORRELATIONSPLOTS";} 
				else{cout << " ";}
			}
		}
	}
	cout << endl;
	cout << "Welcome User!" << endl << endl;
	cout << "Dieses Programm soll auf graphisch einfache Weise die Darstellung von " << endl;
	cout << "Haeufigkeitsverteilungen und Korrelationen innerhalb der Konsole ermoeglichen." << endl;
	cout << endl;
}

void beispiel(){
	string leer;
	cout << endl;
	cout << "-----------------------------BEISPIELE---------------------------------" << endl;
	//cout << "Die maximale Zufallszahl : ";
	//printf("%d", RAND_MAX);
	cout << endl << endl << "Eine Normalverteilung : " << endl;
	double zufall;
	int menge1=50, anzahl1=10000;
	histogramm verteilung(30, 0.3, 0.7);
	double maximalerzufall = RAND_MAX;
	for(int u=0; u<anzahl1; u++){
		zufall=0.;
		for(int k=0; k<menge1; k++){
			zufall+=rand();
		}
		zufall = zufall/menge1/maximalerzufall;
		verteilung.fuellen(zufall);
	}
	verteilung.zeichnen(0, 40);
	cout << "(weiter mit [w])" << endl << endl;
	cin >> leer;
	cout << endl << "Diesmal mit einer anderen Skalierung : " << endl;
	verteilung.rebin(50, 0.4, 0.6);
	verteilung.zeichnen(0, 30);
	cout << "(weiter mit [w])" << endl << endl;
	cin >> leer;
	cout << endl;
	int anzahl2=10000, menge2=20;
	double zufall21, zufall22;
	plot graph(30, 0.1, 0.9, 20, 0.1, 0.9);
	for(int u=0; u<anzahl2; u++){
		zufall21=0.;
		zufall22=0.;
		for(int k=0; k<menge2; k++){
			zufall21+=rand();
		}
		for(int k=0; k<menge2; k++){
			zufall22+=rand();
		}
		zufall21 = zufall21/menge2/maximalerzufall;
		zufall22 = zufall22/menge2/maximalerzufall;
		graph.einlesen(zufall21, zufall22);
	}
	cout << "Eine zweidimensionale Normalverteilung : " << endl;
	graph.plotten(0);
	cout << "(weiter mit [w])" << endl << endl;
	cin >> leer;
	graph.rebin(50, 0.35, 0.65, 30, 0.35, 0.65);
	cout << endl << "Wieder anders skaliert : " << endl;
	graph.plotten(1);
	cout << "(weiter mit [w])" << endl << endl;
	cin >> leer;
	cin.clear();
}

void histerstellen(){
	int wahlfuellen=0, bins=30, menge=2, anzahl=100, groesster=1000000, hoehe=30, off=0;
	double zmin=0., zmax=1., hmin=0., hmax=1., imin=0., imax=1., nutz, zufall;
	string offwahl="n";
	string dateiname;
	bool auslesen=false, bearbeiten=false;
	cout << endl << "Wie viele Bins sollen verwendet werden? (maximal 70)" << endl << endl;
	cin >> bins;
	cout << endl;
	if(bins<1){
		bins=1;
		cout << "Es wird ein Bin verwendet." << endl << endl;
	}
	if(bins>70){
		bins=70;
		cout << "Es werden siebzig Bins verwendet." << endl << endl;
	}
	cout << "In welchem Bereich soll das Histogramm dargestellt werden?" << endl << endl;
	cout << "Untergrenze : ";
	cin >> hmin;
	cout << "Obergrenze : ";
	cin >> hmax;
	if(hmax<hmin){
		nutz=hmax;
		hmax=hmin;
		hmin=nutz;
	}
	if(hmax==hmin){
		hmax+=1.;
		cout << endl << "Untergrenze : " << hmin << endl;
		cout << "Obergrenze : " << hmax << endl;
	}
	histogramm verteilung(bins, hmin, hmax);
	cout << endl << "Wie viele Hoehenschritte moechten sie fuer das Histogramm? (maximal 40)" << endl << endl;
	cin >> hoehe;
	if(hoehe>40){
		hoehe=40;
		cout << endl << "Das Histogramm wird mit einer Hoehe von 40 Schritten erzeugt." << endl;
	}
	if(hoehe<2){
		hoehe=2;
		cout << endl << "Das Histogramm wird mit einer Hoehe von 2 Schritten erzeugt." << endl;
	}
	cout << endl << "Soll das Histogramm mit einem Offset gezeichnet werden? " << endl;
	cout << "Ja [j], Nein [n]" << endl << endl;
	cin >> offwahl;
	cout << endl;
	if(offwahl=="j"){off=1;}
	cout << "Moechten Sie das Histogramm mit Zufallszahlen [0] oder " << endl;
	cout << "mit Werten aus einer Datei [1] fuellen?" << endl << endl;
	cin >> wahlfuellen;
	cout << endl;
	if(wahlfuellen==1){
		cout << "Wie heisst die Datei aus der Werte eingelesen werden sollen?" << endl;
		cout << "(ACHTUNG : Die Datei muss im gleichen Verzeichnis liegen, wie" << endl;
		cout << "das Programm und sollte nicht mehr als 1 Mio. Werte enthalten!)" << endl << endl;
		cin >> dateiname;
		cout << endl;
		cout << "Einen Moment bitte." << endl << endl;
		fstream auslesedatei;
		cout << "Bei der Arbeit ... " << endl << endl;
		auslesedatei.open(dateiname.c_str(), ios::in);
		if(auslesedatei.is_open()){
			auslesen=true;
			cout << "Die Datei konnte geoeffnet werden!" << endl << endl;
		}
		else{cout << "Die Datei konnte nicht geoeffnet werden!" << endl << endl;}
		if(auslesen){
			cout << "Beim auslesen ..." << endl << endl;
			while(auslesedatei >> nutz){
				verteilung.fuellen(nutz);		
			}
			cout << "... Auslesen beendet!" << endl << endl;
			auslesedatei.close();
			bearbeiten=true;
		}
	}
	else{
		cout << "Wie viele Zufallszahlen sollen generiert werden? (maximal 1 Mio.)" << endl << endl;
		cin >> anzahl;
		cout << endl;
		if(anzahl>groesster){
			anzahl=groesster;
			cout << "Es werden " << anzahl << " Zufallszahlen erzeugt." << endl << endl;
		}
		if(anzahl<1){
			anzahl=1;
			cout << "Es wird eine Zufallszahl erzeugt." << endl << endl;
		}
		cout << "In welchem Bereich sollen Zufallszahlen erzeugt werden?" << endl << endl;
		cout << "Untergrenze : ";
		cin >> zmin;
		cout << "Obergrenze : ";
		cin >> zmax;
		if(zmax<zmin){
			nutz=zmax;
			zmax=zmin;
			zmin=nutz;
		}
		if(zmax==zmin){
			zmax+=1.;
			cout << endl << "Untergrenze : " << zmin << endl;
			cout << "Obergrenze : " << zmax << endl;
		}
		cout << endl << "Aus wievielen Summanden sollen die Zufallszahlen erzeugt werden? (maximal 100)" << endl << endl;
		cin >> menge;
		if(menge<1){
			menge=1;
			cout << endl << "Die Zufallszahl wird nicht summiert." << endl;
		}
		if(menge>100){
			menge=100;
			cout << endl << "Die Zufallszahl wird aus hundert summiert." << endl;
		}
		cout << endl << "Die Zufallszahlen werden erzeugt ... " << endl << endl;
		double maximalerzufall = RAND_MAX;
		for(int u=0; u<anzahl; u++){
			zufall=0.;
			for(int k=0; k<menge; k++){
				zufall+=rand();
			}
			zufall = zmin+(zmax-zmin)*zufall/menge/maximalerzufall;
			verteilung.fuellen(zufall);
		}
		cout << endl << "Die Zufallszahlen sind erzeugt!" << endl << endl;
		bearbeiten=true;
	}
	if(bearbeiten==true){
		cout << endl << "Die Verteilung wird erzeugt ... " << endl << endl;
		verteilung.zeichnen(off, hoehe);
		string zubearbeiten="n";
		int bearbeitenwahl=0;
		cout << "Moechten Sie das Histogramm weiter bearbeiten? ";
		cout << "Ja [j], Nein [n]" << endl << endl;
		cin >> zubearbeiten;
		cout << endl;
		while(zubearbeiten=="j"){
			cout << "Wie moechten Sie weiter vorgehen?" << endl;
			cout << "[1] : Binning neu gestallten" << endl;
			cout << "[2] : Intervall integrieren" << endl;
			cout << "[3] : Histogramm in Datei schreiben" << endl;
			cout << "[4] : Histogrammstatus anzeigen" << endl;
			cout << "[0] : Bearbeiten beenden" << endl << endl;
			cin >> bearbeitenwahl;
			cout << endl;
			if(bearbeitenwahl==1){
				cout << "Wie viele Bins sollen verwendet werden? (maximal 70)" << endl << endl;
				cin >> bins;
				if(bins<1){
					bins=1;
					cout << endl << "Es wird ein Bin verwendet." << endl;
				}
				if(bins>70){
					bins=70;
					cout << endl << "Es werden siebzig Bins verwendet." << endl;
				}
				cout << endl;
				cout << "In welchem Bereich soll das Histogramm dargestellt werden?" << endl << endl;
				cout << "Untergrenze : ";
				cin >> hmin;
				cout << "Obergrenze : ";
				cin >> hmax;
				cout << endl;
				if(hmax<hmin){
					nutz=hmax;
					hmax=hmin;
					hmin=nutz;
				}
				if(hmax==hmin){
					hmax+=1.;
					cout << "Untergrenze : " << hmin << endl;
					cout << "Obergrenze : " << hmax << endl << endl;
				}
				verteilung.rebin(bins, hmin, hmax);
				cout << "Wie viele Hoehenschritte moechten sie fuer das Histogramm? (maximal 40)" << endl << endl;
				cin >> hoehe;
				if(hoehe>40){
					hoehe=40;
					cout << "Das Histogramm wird mit einer Hoehe von 40 Schritten erzeugt." << endl;
				}
				if(hoehe<2){
					hoehe=2;
					cout << "Das Histogramm wird mit einer Hoehe von 2 Schritten erzeugt." << endl;
				}
				cout << endl << "Soll das Histogramm mit einem Offset gezeichnet werden? " << endl;
				cout << "Ja [j], Nein [n]" << endl << endl;
				cin >> offwahl;
				cout << endl;
				if(offwahl=="j"){off=1;}
				else{off=0;}
				verteilung.zeichnen(off, hoehe);
				cout << endl;
				cin.clear();
			}
			else if(bearbeitenwahl==2){
				cout << "In welchem Bereich soll die Verteilung integriert werden?" << endl << endl;
				cout << "Untergrenze : ";
				cin >> imin;
				cout << "Obergrenze : ";
				cin >> imax;
				cout << endl;
				if(imax<imin){
					nutz=imax;
					imax=imin;
					imin=nutz;
				}
				if(imax==imin){
					imax+=1.;
					cout << "Untergrenze : " << imin << endl;
					cout << "Obergrenze : " << imax << endl << endl;
				}
				cout << "Das Intergral ueber den eingegebenen Bereich ergibt : " << verteilung.integrieren(imin, imax) << endl << endl;
			}
			else if(bearbeitenwahl==3){
				cout << "Wie heisst die Datei, in die geschrieben werden sollen?" << endl << endl;
				cin >> dateiname;
				cout << endl;
				verteilung.indateizeichnen(off, hoehe, dateiname);
			}
			else if(bearbeitenwahl==4){
				int eintraege, ygbin, yg;
				double xk, xg, xmittel, xstand;
				verteilung.histstatus(eintraege, xk, xg, ygbin, yg, xmittel, xstand);
				cout << "Histogrammstatus : " << endl << endl;
				cout << "Kleinster Eintrag : " << xk << endl;
				cout << "Groesster Eintrag : " << xg << endl;
			}
			else{zubearbeiten="n";}
		}
	}
	cin.clear();
}

void ploterstellen(){
	double xmin=0., xmax=1., ymin=0., ymax=1., nutz, nutz2, groesster=1000000;
	double zxmin=0., zxmax=1., zymin=0., zymax=1., xzufall, yzufall;
	double imin=0., imax=1., i2min=0., i2max=1.;
	int xbin=40, ybin=30, fuellen=0, zufallmenge=100;
	int xzanzahl, yzanzahl, aufloesung=0, bearbeitenwahl=0, xywahl=0;
	string dateiname, arbeiten="n", wahlxy="X";
	bool auslesen=false, bearbeiten=false;
	cout << endl << "Wie viele Bins sollen verwendet werden? " << endl;
	cout << "(maximal in X-Richtung 70, in Y-Richtung 40 )" << endl << endl;
	cout << "X-Binanzahl : ";
	cin >> xbin;
	if(xbin<1){
		xbin=1;
		cout << endl << "Es wird ein Bin in X-Richtung verwendet." << endl;
	}
	if(xbin>70){
		xbin=70;
		cout << endl << "Es werden siebzig Bins in X-Richtung verwendet." << endl;
	}
	cout << "Y-Binanzahl : ";
	cin >> ybin;
	if(ybin<1){
		ybin=1;
		cout << endl << "Es wird ein Bin in Y-Richtung verwendet." << endl;
	}
	if(ybin>40){
		ybin=40;
		cout << endl << "Es werden siebzig Bins in Y-Richtung verwendet." << endl;
	}
	cout << endl << "In welchem Bereich soll der Plot dargestellt werden?" << endl << endl;
	cout << "X-Untergrenze : ";
	cin >> xmin;
	cout << "X-Obergrenze  : ";
	cin >> xmax;
	cout << endl;
	if(xmax<xmin){
		nutz=xmax;
		xmax=xmin;
		xmin=nutz;
	}
	if(xmax==xmin){
		xmax+=1.;
		cout << "X-Untergrenze : " << xmin << endl;
		cout << "X-Obergrenze  : " << xmax << endl << endl;
	} 
	cout << "Y-Untergrenze : ";
	cin >> ymin;
	cout << "Y-Obergrenze  : ";
	cin >> ymax;
	cout << endl;
	if(ymax<ymin){
		nutz=ymax;
		ymax=ymin;
		ymin=nutz;
	}
	if(ymax==ymin){
		ymax+=1.;
		cout << "Y-Untergrenze : " << ymin << endl;
		cout << "Y-Obergrenze  : " << ymax << endl << endl;
	} 
	cout << "Wie genau sollen die Verteilung aufgeloest werden?" << endl;
	cout << "[0] : \"+\" kennzeichnet ein gefuelltes Bin" << endl;
	cout << "[1] : Bins mit mehr als einem Wertepaar werden " << endl;
	cout << "      durch entsprechende Zahlen dargestellt" << endl;
	cout << "[2] : Bins mit mehr als einem Wertepaar werden " << endl;
	cout << "      durch entsprechende Zahlen in zehnerschritten dargestellt" << endl;
	cout << "(bei den letzten beiden Moeglichkeiten wird durch \"H\" " << endl;
	cout << "gekennzeichnet wenn es mehr Wertepaare als entsprechende Ziffern gibt)" << endl << endl;
	cin >> aufloesung;
	cout << endl;
	if(aufloesung>2){aufloesung=0;}
	plot graph(xbin, xmin, xmax, ybin, ymin, ymax);
	cout << "Moechten Sie den Plot mit Zufallszahlen [0] oder " << endl;
	cout << "mit Werten aus einer Datei [1] fuellen?" << endl << endl;
	cin >> fuellen;
	cout << endl;
	if(fuellen==1){
		cout << "Wie heisst die Datei aus der Werte eingelesen werden sollen?" << endl;
		cout << "(ACHTUNG : Die Datei muss im gleichen Verzeichnis liegen, wie" << endl;
		cout << "das Programm und sollte nicht mehr als 1 Mio. Werte enthalten!" << endl;
		cout << "Die Werte sollten abwechselnd X, Y entsprechen.)" << endl << endl;
		cin >> dateiname;
		cout << endl;
		cout << "Einen Moment bitte." << endl << endl;
		fstream auslesedatei;
		cout << "Bei der Arbeit ... " << endl << endl;
		auslesedatei.open(dateiname.c_str(), ios::in);
		if(auslesedatei.is_open()){
			auslesen=true;
			cout << "Die Datei konnte geoeffnet werden!" << endl << endl;
		}
		else{cout << "Die Datei konnte nicht geoeffnet werden!" << endl << endl;}
		if(auslesen){
			cout << "Beim auslesen ..." << endl << endl;
			while(auslesedatei >> nutz){
				auslesedatei >> nutz2;
				graph.einlesen(nutz, nutz2);		
			}
			cout << "... Auslesen beendet!" << endl << endl;
			auslesedatei.close();
			bearbeiten=true;
		}
	}
	else{
		cout << "Mit wie vielen Wertepaaren soll der Plot gefuellt werden? (maximal 1 Mio.)" << endl << endl;
		cin >> zufallmenge;
		cout << endl;
		if(zufallmenge>groesster){
			zufallmenge=groesster;
			cout << "Es werden " << zufallmenge << " Zufallszahlen erzeugt." << endl << endl;
		}
		if(zufallmenge<1){
			zufallmenge=1;
			cout << "Es wird eine Zufallszahl erzeugt." << endl << endl;
		}
		cout << "In welchem Bereich sollen Zufallszahlen generiert werden?" << endl << endl;
		cout << "X-Untergrenze : ";
		cin >> zxmin;
		cout << "X-Obergrenze  : ";
		cin >> zxmax;
		cout << endl;
		if(zxmax<zxmin){
			nutz=zxmax;
			zxmax=zxmin;
			zxmin=nutz;
		}
		if(zxmax==zxmin){
			zxmax+=1.;
			cout << "X-Untergrenze : " << zxmin << endl;
			cout << "X-Obergrenze  : " << zxmax << endl << endl;
		} 
		cout << "Y-Untergrenze : ";
		cin >> zymin;
		cout << "Y-Obergrenze  : ";
		cin >> zymax;
		cout << endl;
		if(zymax<zymin){
			nutz=zymax;
			zymax=zymin;
			zymin=nutz;
		}
		if(zymax==zymin){
			zymax+=1.;
			cout << "Y-Untergrenze : " << zymin << endl;
			cout << "Y-Obergrenze  : " << zymax << endl << endl;
		} 
		cout << "Aus wie vielen Summanden sollen die Zufallszahlen zusammengesetzt werden? " << endl;
		cout << "(maximal 100)" << endl << endl;
		cout << "X-Summanden : ";
		cin >> xzanzahl;
		if(xzanzahl<1){
			xzanzahl=1;
			cout << endl << "Es wird ein Summand fuer die X-Werte verwendet." << endl;
		}
		if(xzanzahl>100){
			xzanzahl=100;
			cout << endl << "Es werden hundert Summanden fuer die X-Werte verwendet." << endl;
		}
		cout << "Y-Summanden : ";
		cin >> yzanzahl;
		if(yzanzahl<1){
			yzanzahl=1;
			cout << endl << "Es wird ein Summand fuer die Y-Werte verwendet." << endl;
		}
		if(yzanzahl>100){
			yzanzahl=100;
			cout << endl << "Es werden hundert Summanden fuer die Y-Werte verwendet." << endl;
		}
		cout << endl << "Die Zufallszahlen werden erzeugt ... " << endl << endl;
		double maximalerzufall = RAND_MAX;
		for(int k=0; k<zufallmenge; k++){
			xzufall=0.;
			for(int j=0; j<xzanzahl; j++){
				xzufall+=rand()/maximalerzufall/xzanzahl;
			}
			xzufall=zxmin+xzufall*(zxmax-zxmin);
			yzufall=0.;
			for(int j=0; j<yzanzahl; j++){
				yzufall+=rand()/maximalerzufall/yzanzahl;
			}
			yzufall=zymin+yzufall*(zymax-zymin);
			graph.einlesen(xzufall, yzufall);
		}
		cout << endl << "Die Zufallszahlen sind erzeugt!" << endl << endl;
		bearbeiten=true;
	}
	if(bearbeiten==true){
		cout << endl << "Die Verteilung wird erzeugt!" << endl << endl;
		graph.plotten(aufloesung);
		cout << "Moechten Sie den Plot weiter bearbeiten? Ja [j], Nein [n]" << endl << endl;
		cin >> arbeiten;
		cout << endl;
		if(arbeiten=="j"){bearbeitenwahl=1;}
		while(bearbeitenwahl!=0){
			cout << "Wie moechten Sie weiter vorgehen?" << endl;
			cout << "[1] : Binning neu gestallten" << endl;
			cout << "[2] : Intervall integrieren" << endl;
			cout << "[3] : Bereich integrieren" << endl;
			cout << "[4] : Plot in Datei schreiben" << endl;
			cout << "[5] : Plotstatus anzeigen" << endl;
			cout << "[0] : Bearbeiten beenden" << endl << endl;
			cin >> bearbeitenwahl;
			cout << endl; 
			if(bearbeitenwahl==1){
				cout << endl << "Wie viele Bins sollen verwendet werden? " << endl;
				cout << "(maximal in X-Richtung 70, in Y-Richtung 40 )" << endl << endl;
				cout << "X-Binanzahl : ";
				cin >> xbin;
				if(xbin<1){
					xbin=1;
					cout << endl << "Es wird ein Bin in X-Richtung verwendet." << endl;
				}
				if(xbin>70){
					xbin=70;
					cout << endl << "Es werden siebzig Bins in X-Richtung verwendet." << endl;
				}
				cout << "Y-Binanzahl : ";
				cin >> ybin;
				if(ybin<1){
					ybin=1;
					cout << endl << "Es wird ein Bin in Y-Richtung verwendet." << endl;
				}
				if(ybin>40){
					ybin=40;
					cout << endl << "Es werden siebzig Bins in Y-Richtung verwendet." << endl;
				}
				cout << endl << "In welchem Bereich soll der Plot dargestellt werden?" << endl << endl;
				cout << "X-Untergrenze : ";
				cin >> xmin;
				cout << "X-Obergrenze  : ";
				cin >> xmax;
				cout << endl;
				if(xmax<xmin){
					nutz=xmax;
					xmax=xmin;
					xmin=nutz;
				}
				if(xmax==xmin){
					xmax+=1.;
					cout << "X-Untergrenze : " << xmin << endl;
					cout << "X-Obergrenze  : " << xmax << endl << endl;
				} 
				cout << "Y-Untergrenze : ";
				cin >> ymin;
				cout << "Y-Obergrenze  : ";
				cin >> ymax;
				cout << endl;
				if(ymax<ymin){
					nutz=ymax;
					ymax=ymin;
					ymin=nutz;
				}
				if(ymax==ymin){
					ymax+=1.;
					cout << "Y-Untergrenze : " << ymin << endl;
					cout << "Y-Obergrenze  : " << ymax << endl << endl;
				} 
				cout << "Wie genau sollen die Verteilung aufgeloest werden?" << endl;
				cout << "[0] : \"+\" kennzeichnet ein gefuelltes Bin" << endl;
				cout << "[1] : Bins mit mehr als einem Wertepaar werden " << endl;
				cout << "      durch entsprechende Zahlen dargestellt" << endl;
				cout << "[2] : Bins mit mehr als einem Wertepaar werden " << endl;
				cout << "      durch entsprechende Zahlen in zehnerschritten dargestellt" << endl;
				cout << "(bei den letzten beiden Moeglichkeiten wird durch \"H\" " << endl;
				cout << "gekennzeichnet wenn es mehr Wertepaare als entsprechende Ziffern gibt)" << endl << endl;
				cin >> aufloesung;
				cout << endl;
				if(aufloesung>2){aufloesung=0;}
				graph.rebin(xbin, xmin, xmax, ybin, ymin, ymax);
				graph.plotten(aufloesung);
				cin.clear();
			}
			else if(bearbeitenwahl==2){
				cout << "Moechten Sie die X- oder die Y-Achse integrieren?" << endl << endl;
				cin >> wahlxy;
				cout << endl;
				if(wahlxy=="Y"){xywahl=1;}
				else{wahlxy="X";}
				cout << "Welchen Bereich moechten Sie integrieren?" << endl << endl;
				cout << "Untergrenze : ";
				cin >> imin;
				cout << "Obergrenze  : ";
				cin >> imax;
				cout << endl;
				if(imax<imin){
					nutz=imax;
					imax=imin;
					imin=nutz;
				}
				if(imax==imin){
					imax+=1.;
					cout << "Untergrenze : " << imin << endl;
					cout << "Obergrenze  : " << imax << endl << endl;
				} 
				cout << "Das Integral ueber den gewaehlten " << wahlxy << "-Bereich ergibt : " << graph.integral(imin, imax, xywahl) << endl << endl;
				cin.clear();
			}
			else if(bearbeitenwahl==3){
				cout << "Welchen Bereich moechten Sie integrieren?" << endl << endl;
				cout << "X-Untergrenze : ";
				cin >> imin;
				cout << "X-Obergrenze  : ";
				cin >> imax;
				cout << endl;
				if(imax<imin){
					nutz=imax;
					imax=imin;
					imin=nutz;
				}
				if(imax==imin){
					imax+=1.;
					cout << "X-Untergrenze : " << imin << endl;
					cout << "X-Obergrenze  : " << imax << endl << endl;
				} 
				cout << "Y-Untergrenze : ";
				cin >> i2min;
				cout << "Y-Obergrenze  : ";
				cin >> i2max;
				cout << endl;
				if(i2max<i2min){
					nutz=i2max;
					i2max=i2min;
					i2min=nutz;
				}
				if(i2max==i2min){
					i2max+=1.;
					cout << "Y-Untergrenze : " << i2min << endl;
					cout << "Y-Obergrenze  : " << i2max << endl << endl;
				} 
				cout << "Das Intergral ueber den gewaelten Bereich ergibt : " << graph.flaechenintegral(imin, imax, i2min, i2max) << endl << endl;
				cin.clear();
			}
			else if(bearbeitenwahl==4){
				cout << "Wie heisst die Datei, in die geschrieben werden sollen?" << endl << endl;
				cin >> dateiname;
				cout << endl;
				graph.indateiplotten(aufloesung, dateiname);
			}
			else if(bearbeitenwahl==5){
				int eintraege;
				double xs, xm, xk, xg, ys, ym, yk, yg;
				graph.plotstatus(eintraege, xm, xs, xk, xg, ym, ys, yk, yg);
				cout << "Plotstatus : " << endl << endl;
				cout << "Kleinster X-Wert : " << setw(14) << setprecision(8) << xk << setw(8) << " "; 
				cout << "Groesster X-Wert     : " << setw(14) << setprecision(8) << xg << endl;
				cout << "X-Mittelwert     : " << setw(14) << setprecision(8) << xm << setw(8) << " "; 
				cout << "X-Standardabweichung : " << setw(14) << setprecision(8) << xs << endl;
				cout << endl;
				cout << "Kleinster Y-Wert : " << setw(14) << setprecision(8) << yk << setw(8) << " "; 
				cout << "Groesster Y-Wert     : " << setw(14) << setprecision(8) << yg << endl;
				cout << "Y-Mittelwert     : " << setw(14) << setprecision(8) << ym << setw(8) << " "; 
				cout << "Y-Standardabweichung : " << setw(14) << setprecision(8) << ys << endl;
				cout << endl;
			}
			cin.clear();
		}
	}
	cin.clear();
}
