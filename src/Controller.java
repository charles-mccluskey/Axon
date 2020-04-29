import java.util.*;

//import nnmodel.*;
import nnmodel2.*;

public class Controller {

	public static void main(String[] args) {
		
		NeuralNetwork example = new NeuralNetwork(3,3,2,2);
		printNN(example);
		List<Double> input = new ArrayList<Double>();
		input.add(2.0);
		input.add(2.0);
		List<Double> output = new ArrayList<Double>();
		try {
			output = forwardPropagation(example,input);
		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		}
		for(int i=0;i<output.size();i++) {
			System.out.println("output "+i+": "+output.get(i));
		}
	}
	
	private static void printNN(NeuralNetwork example) {
		for(int i=0;i<example.numberOfLayers();i++) {
			System.out.println("Layer "+i+": "+example.getLayer(i).numberOfNeurons()+" neurons");
			for(int j=0;j<example.getLayer(i).numberOfNeurons();j++) {
				System.out.println("Connections out of neuron "+j+": "+example.getLayer(i).getNeuron(j).numberOfOutputConnections());
				for(int c=0;c<example.getLayer(i).getNeuron(j).numberOfOutputConnections();c++) {
					System.out.println("Weight: "+example.getLayer(i).getNeuron(j).getOutputConnection(c).getWeight().getValue());
				}
			}
		}
	}
	
	private static List<Double> forwardPropagation(NeuralNetwork nn, List<Double> input) throws InvalidPropertiesFormatException {
		List<Double> output = new ArrayList<Double>();
		
		if(input.size()!=nn.getLayer(0).numberOfNeurons()) {
			throw new InvalidPropertiesFormatException("Input size does not match network input size");
		}
		for(int i=0;i<input.size();i++) {
			nn.getLayer(0).getNeuron(i).setNeuralValue(input.get(i));
		}
		//Input loaded
		
		for(int l=1;l<nn.numberOfLayers();l++) {//for each layer
			for(int n=0;n<nn.getLayer(l).numberOfNeurons();n++) {//for each neuron in the layer
				nn.getLayer(l).getNeuron(n).processInputs();
			}
		}
		//calculations are done. Retrieve output.
		int lastLayer = nn.getLayer((nn.numberOfLayers()-1)).numberOfNeurons();
		for(int i=0;i<lastLayer;i++) {
			output.add(nn.getLayer((nn.numberOfLayers()-1)).getNeuron(i).getNeuralValue());
		}
		return output;
	}
	
	
	/*
	private static void NN1test() {
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
*/
}
