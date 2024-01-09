package Ysa;

import java.io.IOException;
import java.util.Scanner;

import org.neuroph.core.data.DataSet;

public class Program {

	public static void main(String[] args) throws IOException {

			DataSetAyir ds = new DataSetAyir();
			
			DataSet egitimVeriSeti=ds.egitimVeriSeti;
			DataSet testVeriSeti = ds.testVeriSeti;
			
			
			System.out.println("Momentumlu başladı");
			Momentumlu momentumlu = new Momentumlu(15,0.4,0.3,0.0001,1000,egitimVeriSeti,testVeriSeti);
			momentumlu.egit();
			System.out.println("Momentumlu egitimdeki hata: "+momentumlu.egitimHata());
			System.out.println("Momentumlu test hata: "+momentumlu.test());
			
			System.out.println("Momentumsuz başladı");
			Momentumsuz momentumsuz = new Momentumsuz(1000,15,0.3,egitimVeriSeti,testVeriSeti);
			momentumsuz.egit();
			System.out.println("Momentumsuz egitimdeki hata: "+momentumsuz.sonHata());
			System.out.println("Momentumsuz test hata: "+momentumsuz.test());
			System.out.println("Epoch hatalar");
			
			double[] hatalar=momentumsuz.hatalar();
			
			for(var hata:hatalar) {
				System.out.println(hata);
			}
			
		
	}

}
