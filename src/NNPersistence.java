import nnmodel2.*;

public class NNPersistence {
	private static String filename = "NN.AxonNetwork";
	
	public static void save(NeuralNetwork nn) {
		PersistenceObjectStream.serialize(nn);
	}
	
	public static NeuralNetwork load() {
		PersistenceObjectStream.setFilename(filename);
		NeuralNetwork nn = (NeuralNetwork) PersistenceObjectStream.deserialize();
		/*if (nn == null) {//If it can't be loaded for whatever reason
			nn = new Block223(); //then a new Block223 needs to be created.
		}
		else {
			block223.reinitialize();
		}*/
		return nn;
	}
	
	public static void setFilename(String newFilename) {
		filename = newFilename;
		PersistenceObjectStream.setFilename(newFilename);
	}
}
