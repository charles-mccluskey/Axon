import java.util.*;
import nnmodel.*;

public class Controller {

	public static void main(String[] args) {
		
		//gradient descent: w_j = w_j * (step size) * (d_error / d_w_j) for all j
		int inputSize = 3;
		double[] input = {1,1,1};
		List<Double> inputList = new ArrayList<Double>();
		for(int i=0;i<input.length;i++) {
			inputList.add(input[i]);
		}
		
		NeuralNetwork test = new NeuralNetwork();
		test.addLayer();
		test.addLayer();
		test.getLayer(0).addNode(0);
		test.getLayer(0).addNode(0);
		test.getLayer(1).addNode(0);
		for(int i=0;i<test.numberOfLayers();i++) {
			for(int j=0;j<test.getLayer(i).numberOfNodes();j++) {
				if(i!=0) {//if it's not the input layer...
					test.getLayer(i).getNode(j).initializeWeights((test.getLayer(i-1).numberOfNodes()));
				}else {//if it's the input layer
					test.getLayer(i).getNode(j).initializeWeights(inputList.size());
				}
			}
		}
		forwardPropagation(test,inputList);
		
	/*	int inputSize = 5;
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
		System.out.println("number of weights of mid-node: "+mid.getNode(0).numberOfWeights());
		System.out.println("number of weights of last-node: "+last.getNode(0).numberOfWeights());

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
		}*/
	}
	
	private static NeuralNetwork initializeRecNetwork(int layers, int nodesPerLayer, int inputsize) {
		NeuralNetwork network = new NeuralNetwork();
		
		for(int i=0;i<layers;i++) {
			network.addLayer();
			for(int j=0;j<nodesPerLayer;j++) {
				network.getLayer(i).addNode(0);
			}
		}
		
		for(int i=0;i<network.numberOfLayers();i++) {
			for(int j=0;j<network.getLayer(i).numberOfNodes();j++) {
				if(i!=0) {//if it's not the input layer...
					network.getLayer(i).getNode(j).initializeWeights((network.getLayer(i-1).numberOfNodes()));
				}else {//if it's the input layer
					network.getLayer(i).getNode(j).initializeWeights(inputsize);
				}
			}
		}
		
		return network;
	}
	
	private static void forwardPropagation(NeuralNetwork nn, List<Double> input) {
		List<Double> outs = new ArrayList<Double>();
		List<Double> tmp = new ArrayList<Double>();
		outs = input;
		for(int i=0;i<nn.numberOfLayers();i++) {//iterate through layer
			tmp.addAll(outs);
			outs.clear();
			for(int n=0;n<nn.getLayer(i).numberOfNodes();n++) {
				outs.add(nn.getLayer(i).getNode(n).process(tmp));
			}
			tmp.clear();
		}
		for(int i=0;i<outs.size();i++) {
			System.out.println("output "+i+" = "+outs.get(i));
		}
	}

}
