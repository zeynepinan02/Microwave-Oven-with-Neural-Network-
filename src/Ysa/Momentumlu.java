package Ysa;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.MomentumBackpropagation;
import org.neuroph.util.TransferFunctionType;


public class Momentumlu {
	
	private int araKatmanNoronSayisi;
	private MomentumBackpropagation mbp;
	
	private DataSet egitimVeriSeti;
	private DataSet testVeriSeti;

	
	public Momentumlu( int araKatmanNoronSayisi,double momentum,double ogrenmeKatsayisi, double maxHata, int epoch, DataSet egitimVeriSeti, DataSet testVeriSeti) throws IOException
	{		
		this.egitimVeriSeti= egitimVeriSeti;
		this.testVeriSeti = testVeriSeti;
		
		this.araKatmanNoronSayisi = araKatmanNoronSayisi;
		
		mbp = new MomentumBackpropagation();
		mbp.setLearningRate(ogrenmeKatsayisi);
		mbp.setMaxError(maxHata);
		mbp.setMaxIterations(epoch);
		mbp.setMomentum(momentum);
	}
	
	public void egit() {
		MultiLayerPerceptron sinirselAg = new MultiLayerPerceptron(TransferFunctionType.SIGMOID,4,araKatmanNoronSayisi,1);
		sinirselAg.setLearningRule(mbp);
		sinirselAg.learn(egitimVeriSeti);
		sinirselAg.save("momentumlu.nnet");
		System.out.println("Momentumlu egitim tamamlandı");
	}
	
	public double test() {
		NeuralNetwork sinirselAg = NeuralNetwork.createFromFile("momentumlu.nnet");
		double toplamHata=0;
		var satirlar = testVeriSeti.getRows();
		for(DataSetRow satir: satirlar) {
			sinirselAg.setInput(satir.getInput());
			sinirselAg.calculate();
			toplamHata+=mse(satir.getDesiredOutput(),sinirselAg.getOutput());
			
		}
		return toplamHata/testVeriSeti.size();
	}
	
	public double egitimHata()
	{
		return mbp.getTotalNetworkError();
	}
	
	private double mse(double[] beklenen, double[] uretilen)
	{
		double birVeridekiHata = 0;
		for(int i=0;i< beklenen.length;i++) {
			birVeridekiHata += Math.pow(beklenen[i]-uretilen[i],2); 
		}
		return birVeridekiHata/beklenen.length;
	}



}
