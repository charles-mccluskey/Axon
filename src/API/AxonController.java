package API;

import java.util.*;
import java.io.*;
import nnmodel2.*;

public class AxonController {
	
	private static List<NeuralNetwork> networks;
	private NeuralNetwork bestNetwork;
	
	public AxonController() {
		networks = new ArrayList<NeuralNetwork>();
	}
	/** Generate an initial fully connected neural network for your project. 
	 * 
	 * @param numInputs - The number of inputs your network requires; this will never change.
	 * @param numHiddenLayers - The number of initial hidden layers the network will have; this is changeable via mutation, but the number of initial hidden layers should be
	 *  proportional to the complexity of the task of the network.
	 * @param nodesPerLayer - The number of initial nodes per hidden layer. Changeable via mutation.
	 * @param numOutputs - The number of outputs the network will have; this will never change.
	 * @param learningRate - The learning rate of the network. Changeable via mutation.
	 * @return true if network is successfully created.
	 */
	public boolean createPrimaryNetwork(int numInputs, int numHiddenLayers, int nodesPerLayer, int numOutputs, double learningRate) {
		if(networks.isEmpty()) {
			NeuralNetwork network = new NeuralNetwork(numInputs, numHiddenLayers, nodesPerLayer, numOutputs, learningRate);
			networks.add(network);
			return true;
		}
		return false;
	}
	
	/** Save the current primary network to a file. If name is empty, it will use the previously used name (when either loading or saving) or default to "NN.AxonNetwork".
	 * 
	 * @param name - desired name of network save file.
	 * @return true if network has been saved.
	 */
	public boolean savePrimaryNetwork(String name) {
		if(!networks.isEmpty()) {
			if(!name.isEmpty()) {
				NNPersistence.setFilename(name);
			}
			NNPersistence.save(networks.get(0));
			return true;
		}
		return false;
	}
	
	/** load the desired Axon network to your program.
	 * 
	 * @param name - The name of the Axon network you wish to load.
	 * @return true if network is successfully loaded.
	 */
	public boolean loadPrimaryNetwork(String name) {
		if(!name.isEmpty()) {
			NNPersistence.setFilename(name);			
		}
		try {
			networks.add(0, NNPersistence.load());
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("Network with name \""+name+"\" not found.");
		}
		return false;
	}
	
	public boolean mutatePrimaryNetwork(int generationSize) {
		if(networks.isEmpty()) {
			return false;
		}
		Random rng = new Random();
		int mutationRate = networks.get(0).getMutationRate();
		List<NeuralNetwork> nextGen = new ArrayList<NeuralNetwork>();
		for(int g=0;g<generationSize;g++) {
			for(int m=0;m<mutationRate;m++) {
				
			}
		}

		
		
		
		return false;
	}
	
	private static void accuracyTest() {
		NeuralNetwork example;
		NNPersistence.setFilename("NN");
		try {
			example = NNPersistence.load();
		} catch (FileNotFoundException e) {
			example = new NeuralNetwork(2,3,4,4,0.1);
		}
		
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
				line = reader.readLine();
			}
			for(int i=0;i<lines.size();i++) {
				xVals.add(Integer.parseInt(lines.get(i)[0]));
				yVals.add(Integer.parseInt(lines.get(i)[1]));
				quadrants.add(Integer.parseInt(lines.get(i)[2]));
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//training set has been retrieved in the form of 3 lists
		int corrects=0;
		int incorrects=0;
		
		try {
			for(int i=0;i<quadrants.size();i++) {
				List<Double> testInput = new ArrayList<Double>();
				testInput.add( (double) xVals.get(i));
				testInput.add( (double) yVals.get(i));	
				List<Double> results = forwardPropagation(example,testInput);
				
				int quad=1;
				double max=results.get(0);
				for(int r=1;r<results.size();r++) {
					if(results.get(r)>max) {
						max=results.get(r);
						quad=r+1;
					}
				}
				
				if(quad==quadrants.get(i)) {
					corrects++;
				}else {
					incorrects++;
				}
			}
			//What percentage are correct?
			double accuracy = (double) corrects / ((double) corrects+(double) incorrects);
			System.out.println("NN accuracy on training data: "+accuracy);
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void simpleTest() {
		NeuralNetwork example;
		NNPersistence.setFilename("SimpleTest");
		try {
			example = NNPersistence.load();
		} catch (FileNotFoundException e) {
			example = new NeuralNetwork(1,1,1,1,0.1);
		}
		//printNN(example);
		//NN created
		//now create test values
		List<Double> input = new ArrayList<Double>();
		input.add(99999999.0);
		List<Double> expected = new ArrayList<Double>();
		expected.add(0.0);
		
		//Now run the test
		try {
			List<Double> results = forwardPropagation(example,input);
			System.out.println("Results of simple test: "+results.get(0));
			System.out.println("-----------");
			printNN(example);
			System.out.println("-----------\n");
			
			System.out.println("PERFORMING BACKPROPAGATION");
			example = backPropagation(example,expected);
			System.out.println("BACKPROPAGATION COMPLETE\n");
			
			System.out.println("Performing new test");
			results = forwardPropagation(example,input);
			System.out.println("Results of simple test: "+results.get(0));
			System.out.println("-----------");
			printNN(example);
			System.out.println("-----------");
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NNPersistence.save(example);
		
	}
	
	private static void quadrantTest() {
		NeuralNetwork example;
		NNPersistence.setFilename("NN");
		try {
			example = NNPersistence.load();
		} catch (FileNotFoundException e) {
			example = new NeuralNetwork(2,3,4,4,0.1);
		}
		
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
				line = reader.readLine();
			}
			for(int i=0;i<lines.size();i++) {
				xVals.add(Integer.parseInt(lines.get(i)[0]));
				yVals.add(Integer.parseInt(lines.get(i)[1]));
				quadrants.add(Integer.parseInt(lines.get(i)[2]));
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//training set has been retrieved in the form of 3 lists
		
		List<Double> testInput = new ArrayList<Double>();
		testInput.add( (double) xVals.get(6));
		testInput.add( (double) yVals.get(6));
		List<Double> testOutput = convertQuadrantToList(quadrants,6);
		//extracted single test case. Ready for test.
		
		try {
			System.out.println("input: "+testInput.get(0)+","+testInput.get(1));
			List<Double> results = forwardPropagation(example,testInput);
			System.out.println("\n--------------------------\noutput results:");
			for(int i=0;i<results.size();i++) {
				System.out.println(results.get(i));
			}
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void quadrantTestTraining() {
		
		NeuralNetwork example;
		NNPersistence.setFilename("NN");
		try {
			example = NNPersistence.load();
		} catch (FileNotFoundException e) {
			example = new NeuralNetwork(2,3,4,4,0.1);
		}

		//NNPersistence.save(example);
			
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
				line = reader.readLine();
			}
			for(int i=0;i<lines.size();i++) {
				xVals.add(Integer.parseInt(lines.get(i)[0]));
				yVals.add(Integer.parseInt(lines.get(i)[1]));
				quadrants.add(Integer.parseInt(lines.get(i)[2]));
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//training set has been retrieved in the form of 3 lists
		
		for(int i=0;i<quadrants.size();i++) {
			List<Double> testInput = new ArrayList<Double>();
			testInput.add( (double) xVals.get(i));
			testInput.add( (double) yVals.get(i));
			List<Double> testOutput = convertQuadrantToList(quadrants,i);
			try {
				List<Double> results = forwardPropagation(example,testInput);
				example = backPropagation(example,testOutput);
			} catch (InvalidPropertiesFormatException e) {
				e.printStackTrace();
			}
		}
		NNPersistence.save(example);
		/*
		List<Double> testInput = new ArrayList<Double>();
		testInput.add( (double) xVals.get(0));
		testInput.add( (double) yVals.get(0));
		List<Double> testOutput = convertQuadrantToList(quadrants,0);
		//extracted single test case. Ready for test.

		
		System.out.println("NETWORK BEFORE TEST:\n--------------------------\n\n");
		//printNN(example);
		try {
			List<Double> results = forwardPropagation(example,testInput);
			System.out.println("\n--------------------------\n output results:");
			for(int i=0;i<results.size();i++) {
				System.out.println(results.get(i));
			}
			System.out.println("\nPerforming back propagation");
			example = backPropagation(example,testOutput);
			System.out.println("\nmodified network:\n--------------------------\n\n");
			//printNN(example);
			System.out.println("\nnew results with same test:\n");
			results = forwardPropagation(example,testInput);
			for(int i=0;i<results.size();i++) {
				System.out.println(results.get(i));
			}
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
	private static List<Double> convertQuadrantToList(List<Integer> quadrants,int index){
		List<Double> output = new ArrayList<Double>();
		if(quadrants.get(index) == 1) {
			output.add(1.0);
			output.add(0.0);
			output.add(0.0);
			output.add(0.0);
		}else if(quadrants.get(index) == 2) {
			output.add(0.0);
			output.add(1.0);
			output.add(0.0);
			output.add(0.0);			
		}else if(quadrants.get(index) == 3) {
			output.add(0.0);
			output.add(0.0);
			output.add(1.0);
			output.add(0.0);			
		}else if(quadrants.get(index) == 4) {
			output.add(0.0);
			output.add(0.0);
			output.add(0.0);
			output.add(1.0);			
		}
		return output;
	}
	
	private static void printNN(NeuralNetwork example) {
		for(int i=0;i<example.numberOfLayers();i++) {
			System.out.println("Layer "+i+": "+example.getLayer(i).numberOfNeurons()+" neurons");
			for(int j=0;j<example.getLayer(i).numberOfNeurons();j++) {
				System.out.println("Connections out of neuron "+j+" with bias "+example.getLayer(i).getNeuron(j).getBias()+": "+example.getLayer(i).getNeuron(j).numberOfOutputConnections());
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
			nn.getLayer(0).getNeuron(i).setActivation(nn.getLayer(0).getNeuron(i).activationFunction(input.get(i)));
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
	
	//sigmoid derivative: s`(x) = s(x) * (1 - s(x))
	private static NeuralNetwork backPropagation(NeuralNetwork nn, List<Double> testOutputs) {
		//calculate output error:
		for(int i=0;i<nn.getLayer((nn.numberOfLayers()-1)).numberOfNeurons();i++) {//for each output neuron
			double output = nn.getLayer((nn.numberOfLayers()-1)).getNeuron(i).getActivation();
			nn.getLayer((nn.numberOfLayers()-1)).getNeuron(i).setError(2*(testOutputs.get(i) - output));//derivative of cost function (y-out)^2
		}
		//TEMPORARY CODE
		/*for(int i=0;i<nn.getLayer(nn.numberOfLayers()-1).numberOfNeurons();i++) {
			System.out.println("Error "+i+": "+nn.getLayer(nn.numberOfLayers()-1).getNeuron(i).getError());
		}*/
		//TEMPORARY CODE
		//output layer cost has been set. Now go backwards through the network
		for(int l=nn.numberOfLayers()-1;l>0;l--) {//iterate backwards through the layers
			for(int n=0;n<nn.getLayer(l).numberOfNeurons();n++) {//iterate through the neurons;
				double errorSum=0;
				for(int c=0;c<nn.getLayer(l).getNeuron(n).numberOfInputConnections();c++) {//iterate through input connections of current neuron
					double activationDerivative = nn.getLayer(l).getNeuron(n).activationFunctionDerivative(nn.getLayer(l).getNeuron(n).getInput());
					//System.out.println("Activation derivative: "+activationDerivative);
					double previousActivation = nn.getLayer(l).getNeuron(n).getInputConnection(c).getInputNeuron().getActivation();
					//System.out.println("Previous activation: "+previousActivation);
					errorSum = nn.getLayer(l).getNeuron(n).sumErrors();
					//System.out.println("Error sum: "+errorSum);
					double change = activationDerivative*previousActivation*errorSum;
					//System.out.println("weight change: "+change);

					nn.getLayer(l).getNeuron(n).getInputConnection(c).getWeight().setChange(
							nn.getLearningRate()*change
						);
				}
				double biasChange = errorSum*nn.getLayer(l).getNeuron(n).activationFunctionDerivative(nn.getLayer(l).getNeuron(n).getInput());
				//System.out.println("Bias change: "+biasChange);
				double newBias = nn.getLayer(l).getNeuron(n).getBias() + (nn.getLearningRate()*biasChange);
				nn.getLayer(l).getNeuron(n).setBias(newBias);
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
		return nn;
	}	
}
