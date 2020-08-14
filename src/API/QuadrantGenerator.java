package API;

import java.io.*;
import java.util.*;

public class QuadrantGenerator {

	public static void main(String[] args) {
		
		int dataSize = 10000;
		
		List<Integer> xVals = GenerateList(dataSize);
		List<Integer> yVals = GenerateList(dataSize);
		List<Integer> quadrants = new ArrayList<Integer>();
		
		for(int i=0;i<dataSize;i++) {
			if(xVals.get(i)>=0 && yVals.get(i)>=0) {
				quadrants.add(1);
			}else if(xVals.get(i)<0 && yVals.get(i)>=0) {
				quadrants.add(2);
			}else if(xVals.get(i)<0 && yVals.get(i)<0) {
				quadrants.add(3);
			}else if(xVals.get(i)>=0 && yVals.get(i)<0) {
				quadrants.add(4);
			}
		}

		try {
			writeFile(xVals,yVals,quadrants);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static List<Integer> GenerateList(int size){
		Random rng = new Random();
		List<Integer> output = new ArrayList<Integer>();
		for(int i=0;i<size;i++) {
			output.add(rng.nextInt());
		}
		return output;
	}
	
	private static void writeFile(List<Integer> xVals, List<Integer> yVals, List<Integer> quadrants) throws IOException {
		File fout = new File("trainingData.txt");
		FileOutputStream fos = new FileOutputStream(fout);
	 
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
	 
		for (int i=0; i<quadrants.size(); i++) {
			bw.write(xVals.get(i)+";"+yVals.get(i)+";"+quadrants.get(i));
			bw.newLine();
		}
	 
		bw.close();
	}
}
