package API;

import java.io.FileNotFoundException;

import nnmodel2.*;

public class NNPersistence {
	private static String filename = "NN.AxonNetwork";
	
	public static void save(NeuralNetwork nn) {
		PersistenceObjectStream.serialize(nn);
	}
	
	public static NeuralNetwork load() throws FileNotFoundException{
		PersistenceObjectStream.setFilename(filename);
		NeuralNetwork nn = (NeuralNetwork) PersistenceObjectStream.deserialize();
		if (nn == null) {//If it can't be loaded for whatever reason
			throw new FileNotFoundException();
		}
		return nn;
	}
	
	public static void setFilename(String newFilename) {
		filename = newFilename+".AxonNetwork";
		PersistenceObjectStream.setFilename(newFilename);
	}
}
