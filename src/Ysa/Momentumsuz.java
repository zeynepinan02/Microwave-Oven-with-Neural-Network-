package Ysa;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

public class Momentumsuz {
	private DataSet egitimVeriSeti;
	private DataSet testVeriSeti;

	private int maxEpoch;
	private double[] epochHatalar;

	BackPropagation bp;
	private int araKatmanNoronSayisi;

	
	public Momentumsuz(int maxEpoch,int araKatmanNoronSayisi,double ogrenmeKatsayisi, DataSet egitimVeriSeti, DataSet testVeriSeti) {
		this.maxEpoch=maxEpoch;
		epochHatalar=new double[maxEpoch];
		
		this.araKatmanNoronSayisi = araKatmanNoronSayisi;
		
		this.egitimVeriSeti= egitimVeriSeti;
		this.testVeriSeti = testVeriSeti;
		
		bp= new BackPropagation();
		bp.setLearningRate(ogrenmeKatsayisi);
	}
	
	public void egit() throws FileNotFoundException {
		MultiLayerPerceptron ag=new MultiLayerPerceptron(TransferFunctionType.SIGMOID,4,araKatmanNoronSayisi,1); 																			
	    
	    ag.setLearningRule(bp);
	
		for(int epoch=0;epoch<maxEpoch;epoch++) {
			ag.getLearningRule().doOneLearningIteration(egitimVeriSeti);
		    epochHatalar[epoch]=ag.getLearningRule().getPreviousEpochError();
		}
		
		ag.learn(egitimVeriSeti);
		ag.save("momentumsuz.nnet");
		System.out.println("momentumsuz egitim tamalandi");

	}
	
	public double[] hatalar(){
		return epochHatalar;
	}
	
	public double sonHata() {
		return bp.getTotalNetworkError();
	}
	
	public double test() {
		var ag = NeuralNetwork.createFromFile("momentumsuz.nnet");  
		double toplamHata=0;
		var satirlar = testVeriSeti.getRows();
		for(DataSetRow satir: satirlar) {
			ag.setInput(satir.getInput());
			ag.calculate();
			toplamHata+=mse(satir.getDesiredOutput(),ag.getOutput());
			
		}
		return toplamHata/testVeriSeti.size(); 
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
