import java.util.*;
import nnmodel.*;

public class Controller {
	static List<Layer> layers;

	public static void main(String[] args) {
		int inputSize = 5;
		layers = new ArrayList<Layer>();
		int[] layerSize = {4,6,3};
		
		for(int i=0;i<3;i++) {
			layers.add(new Layer());
			for(int j=0;j<layerSize[i];j++) {
				Node generate = new Node(j+1,layers.get(i));
			}
		}

		List<Double> inputs = new ArrayList<Double>();
		for(int i=0;i<inputSize;i++) {
			inputs.add((double) i+1);
			System.out.println(inputs.get(i));
			//inputs.add(2.0);
		}

		System.out.println("number of inputs: "+inputs.size());

		
		System.out.println("------------------");
		System.out.println("midOne layer size: "+layers.get(0).numberOfNodes());
		System.out.println("midTwo layer size: "+layers.get(1).numberOfNodes());
		System.out.println("last layer size: "+layers.get(2).numberOfNodes());
	/*	System.out.println("number of weights of mid-node: "+mid.getNode(0).numberOfWeights());
		System.out.println("number of weights of last-node: "+last.getNode(0).numberOfWeights());
*/
		//Now let's try forward propagation.
		List<Double> outs = new ArrayList<Double>();
		List<Double> tmp = new ArrayList<Double>();
		outs = inputs;
		for(int i=0;i<layers.size();i++) {//iterate through layer
			tmp=outs;
			outs.clear();
			for(int n=0;n<layers.get(i).numberOfNodes();n++) {
				outs.add(layers.get(i).getNode(n).process(tmp));
			}
		}
		System.out.println("Results of forward propagation:");
		for(int i=0;i<outs.size();i++) {
			System.out.println(outs.get(i));
		}
	}

}
