package API;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PersistenceObjectStream {

	//private static String filename = "NN"+NNPersistence.extension;

	public static void serialize(Object object) {
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream(NNPersistence.getFullFileName());
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(object);
			out.close();
			fileOut.close();
		} catch (Exception e) {
			throw new RuntimeException("Could not save data to file '" + NNPersistence.getFullFileName() + "'.");
		}
	}

	public static Object deserialize() {
		Object o = null;
		ObjectInputStream in;
		try {
			FileInputStream fileIn = new FileInputStream(NNPersistence.getFullFileName());
			in = new ObjectInputStream(fileIn);
			o = in.readObject();
			in.close();
			fileIn.close();
		} catch (Exception e) {
			o = null;
		}
		return o;
	}
	
	//public static void setFilename(String newFilename) {
		//filename = newFilename;
	//}

}