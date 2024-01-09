package Ysa;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;

public class DataSetAyir {
	private static final File data = new File(DataSetAyir.class.getResource("DataSet.txt").getPath());

	public static List<String> egitimVerisi ;
	public static List<String> testVerisi ;

	private double[] minimumlarI;
	private double[] maksimumlarI;
	private double minimumlarO;
	private double maksimumlarO;
	
	protected DataSet egitimVeriSeti;
	protected DataSet testVeriSeti;

	public DataSetAyir() throws IOException {
		dataSetAyir(data);
		
		minimumlarI = new double[4]; //girdi sayisi 4 oldugu icin
		maksimumlarI = new double[4];
	
		for(int i=0;i<4;i++) {
			minimumlarI[i]= Double.MAX_VALUE;
			maksimumlarI[i]= Double.MIN_VALUE;
		}
		minimumlarO =Double.MAX_VALUE;
		maksimumlarO= Double.MIN_VALUE;
		
		MinimumveMaksimumlar(egitimVerisi);
		MinimumveMaksimumlar(testVerisi);
		
		egitimVeriSeti= listOku(egitimVerisi);
        testVeriSeti =listOku(testVerisi);
	}
	
	private void dataSetAyir(File dosya) throws IOException {
		
		// Dosyadan satırları oku
        List<String> satirlar = Files.readAllLines(dosya.toPath(), StandardCharsets.UTF_8);

        // Satırları karıştır (rastgele sırala)
        Collections.shuffle(satirlar);

        // Eğitim verisi için satır sayısı
        int egitimSatirSayisi = (int) (satirlar.size() * 0.75);

        // Eğitim verisi
        egitimVerisi = satirlar.subList(0, egitimSatirSayisi);

        // Test verisi
        testVerisi = satirlar.subList(egitimSatirSayisi, satirlar.size());
	}

	private DataSet listOku(List<String> satirListesi) {
		DataSet ds = new DataSet(4, 1);
	
	    for (String satir : satirListesi) {
	        Scanner scanner = new Scanner(satir);
	
	        double[] input = new double[4];
	        for (int i = 0; i < 4; i++) {
	            if (scanner.hasNextDouble()) {
	                double d = scanner.nextDouble();
	                input[i] = minMax(d, minimumlarI[i], maksimumlarI[i]);
	            }
	        }
	
	        if (scanner.hasNextDouble()) {
	            double o = scanner.nextDouble();
	            double[] output = new double[1];
	            output[0]=minMax(o, minimumlarO, maksimumlarO);
	            DataSetRow veri = new DataSetRow(input, output);
	            ds.add(veri);
	        }
	
	        scanner.close();
	    }
	    return ds;
	}
	
	private void MinimumveMaksimumlar(List<String> satirListesi) 
	{
		for (String satir : satirListesi) {
	        Scanner scanner = new Scanner(satir);

	        for (int i = 0; i < 4; i++) {
	            if (scanner.hasNextDouble()) {
	                double d = scanner.nextDouble();
	                if (d > maksimumlarI[i]) {
	                    maksimumlarI[i] = d;
	                }
	                if (d < minimumlarI[i]) {
	                    minimumlarI[i] = d;
	                }
	            }
	        }

	        if (scanner.hasNextDouble()) {
	            double outputValue = scanner.nextDouble();
	            if (outputValue > maksimumlarO) {
	                maksimumlarO = outputValue;
	            }
	            if (outputValue < minimumlarO) {
	                minimumlarO = outputValue;
	            }
	        }

	        scanner.close();
	    }
	}
	
	private double minMax(double d,double min,double max)
	{
		return (d-min)/(max-min);
	}
}
