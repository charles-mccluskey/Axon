import java.util.*;
import java.io.*;
import nnmodel2.*;

public class Controller {

	public static void main(String[] args) {
		
		NeuralNetwork example;
		NNPersistence.setFilename("NN.AxonNetwork");
		try {
			example = NNPersistence.load();
			System.out.println();
		} catch (FileNotFoundException e) {
			example = new NeuralNetwork(2,3,3,4,0.1);
		}
		printNN(example);
		NNPersistence.save(example);
		/*	
		File currentDir = new File("");
		String path = currentDir.getAbsolutePath();
		String filePath = path+"/trainingData.txt";
		List<Integer> xVals = new ArrayList<Integer>();
		List<Integer> yVals = new ArrayList<Integer>();
		List<Integer> quadrants = new ArrayList<Integer>();
		try {
			BufferedReader reader = new BufferedReader (new FileReader(filePath));

			List<String[]> lines = new ArrayList<String[]>();
			String line = reader.readLine();
			while(line != null) {
				lines.add(line.split(";"));
			}
			for(int i=0;i<lines.size();i++) {
				xVals.add(Integer.parseInt(lines.get(i)[0]));
				yVals.add(Integer.parseInt(lines.get(i)[1]));
				quadrants.add(Integer.parseInt(lines.get(i)[2]));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		//training set has been retrieved in the form of 3 lists
		
		
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
			nn.getLayer(0).getNeuron(i).setActivation(input.get(i));
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
			output.add(nn.getLayer((nn.numberOfLayers()-1)).getNeuron(i).getActivation());
		}
		return output;
	}
	
	//sigmoid derivative: s`(x) = s(x) + (1 - s(x))
	private static void backPropagation(NeuralNetwork nn, List<Double> testOutputs) {
		//calculate output error:
		for(int i=0;i<nn.getLayer((nn.numberOfLayers()-1)).numberOfNeurons();i++) {
			double output = nn.getLayer((nn.numberOfLayers()-1)).getNeuron(i).getActivation();
			nn.getLayer((nn.numberOfLayers()-1)).getNeuron(i).setError(output - testOutputs.get(i));//simple cost function
		}
		//output layer cost has been set. Now go backwards through the network
		for(int l=(nn.numberOfLayers()-1);l>0;l--) {//iterate backwards through the layers
			for(int n=0;n<nn.getLayer(l).numberOfNeurons();n++) {//iterate through the neurons
				for(int c=0;c<nn.getLayer(l).getNeuron(n).numberOfInputConnections();c++) {//iterate through input connections of current node
					
					nn.getLayer(l).getNeuron(n).getInputConnection(c).getWeight().setChange(
							nn.getLayer(l).getNeuron(n).sigPrime(
									nn.getLayer(l).getNeuron(n).getInput())
							* nn.getLayer(l).getNeuron(n).getInputConnection(c).getInputNeuron().getActivation()
							* nn.getLayer(l).getNeuron(n).sumErrors()
						);
				}
			}
		}
		//change has been calculated for all weights (NOT BIASES)
		//update all weights to reflect the changes
		for(int l=(nn.numberOfLayers()-1);l>0;l--) {//iterate backwards through the layers
			for(int n=0;n<nn.getLayer(l).numberOfNeurons();n++) {//iterate through the neurons
				for(int c=0;c<nn.getLayer(l).getNeuron(n).numberOfInputConnections();c++) {//iterate through input connections of current node
					
					nn.getLayer(l).getNeuron(n).getInputConnection(c).updateWeight();
					
				}
			}
		}
	}	
}
