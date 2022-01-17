package API;

import java.util.*;
import java.io.*;
import nnmodel2.*;

public class AxonController {
	
	private static List<NeuralNetwork> networks;
	private NeuralNetwork bestNetwork;
	private static int activeNetwork;
	
	public AxonController() {
		networks = new ArrayList<NeuralNetwork>();
	}
	
	/** test private controller methods.
	 * 
	 */
	public void testController() {
		printNNTwo(networks.get(0));
		NeuralNetwork test = networks.get(0).deepClone();
		addRandomConnection(test);
		printNNTwo(test);
	}
	
	/** Used for evolutionary algorithms.
	 * Generate an initial fully connected neural network for your project. Sets primary network to active network.
	 * This method should only be used to initialize your project!
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
			activeNetwork = 0;
			return true;
		}
		return false;
	}
	
	/** Used for evolutionary algorithms.
	 * Save the current primary network to a file. If name is empty, it will use the previously used name (when either loading or saving) or default to "NN.AxonNetwork".
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
	
	/** Used for evolutionary algorithms.
	 * Save the best overall network to a file. Remember to set the best network beforehand!
	 * If name is empty, it will use the previously used name (when either loading or saving) or default to "NN.AxonNetwork".
	 * 
	 * @param name - desired name of network save file.
	 * @return true if network has been saved.
	 */
	public boolean saveBestNetwork(String name) {
		if(bestNetwork != null) {
			if(!name.isEmpty()) {
				NNPersistence.setFilename(name);
			}
			NNPersistence.save(bestNetwork);
			return true;
		}
		return false;
	}
	
	/** Used for evolutionary algorithms.
	 * load the desired neural network to your program as primary network.
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
	
	/** Used for evolutionary algorithms.
	 * Replace the existing list of networks Axon remembers with a new list of networks, which are mutants of the primary network (network in slot 0).
	 * 
	 * @param generationSize
	 * @return
	 */
	public boolean mutatePrimaryNetwork(int generationSize) {
		if(networks.isEmpty()) {
			return false;
		}//return false if the primary network hasn't been generated.
		
		//initialize necessary objects
		Random rng = new Random();
		int primaryMutationRate = networks.get(0).getMutationRate();
		List<NeuralNetwork> nextGen = new ArrayList<NeuralNetwork>();
		for(int g=0;g<generationSize;g++) {
			NeuralNetwork network = networks.get(0).deepClone();//clone the primary network
			for(int m=0;m<primaryMutationRate;m++) {//apply randomly selected mutations to it
				int mutation = rng.nextInt(9);
				if(mutation == 0) {
					addRandomNeuron(network);
				}else if(mutation == 1) {
					removeRandomNeuron(network);
				}else if(mutation == 2) {
					addRandomLayer(network);
				}else if(mutation == 3) {
					removeRandomLayer(network);
				}else if(mutation == 4) {
					changeMutationRate(network);
				}else if(mutation == 5) {
					changeActivations(network);
				}else if(mutation == 6) {
					changeLearningRateRandom(network);
				}else if(mutation == 7) {
					removeRandomConnection(network);
				}else if(mutation == 8) {
					addRandomConnection(network);
				}
			}
			nextGen.add(network);
		}
		//the nextGen list has been loaded with randomly mutated networks based off primary network from previous generation
		networks.clear();
		networks.addAll(nextGen);
		return true;
	}

	/** Used for evolutionary algorithms.
	 * Sets next network in the generation as active. Use to iterate through the generation.
	 * 
	 * @return True if next network has been set as active. False if there is no next network.
	 */
	public boolean nextNetwork() {
		if(activeNetwork+1 > networks.size()) {
			return false;
		}else {
			activeNetwork++;
			return true;
		}
	}
	
	/** Used for evolutionary algorithms.
	 * Manually set which network is the active network
	 * 
	 * @param index
	 * @return True if index corresponds to a valid network. False if the index exceeds the number of networks in a generation or is negative.
	 */
	public boolean setActiveNetwork(int index) {
		if(index >= networks.size() || index < 0) {
			return false;
		}else {
			activeNetwork = index;
			return true;
		}
	}
	
	/** Used for evolutionary algorithms.
	 * Makes the current active network the primary network by swapping their locations in the list of networks. Important to call before mutating!
	 */
	public void setActiveAsPrimary() {
		if(activeNetwork == 0) {//if already primary
			return;
		}
		NeuralNetwork temp = networks.remove(activeNetwork);
		NeuralNetwork primary = networks.remove(0);
		networks.add(0,temp);
		networks.add(activeNetwork, primary);
	}
	
	/** Used for evolutionary algorithms.
	 * Sets the primary network as the overall best network. 
	 */
	public void setPrimaryAsBest() {
		bestNetwork = networks.get(0);
	}
	
	/** Used for evolutionary algorithms.
	 * Sets the best overall network as the primary network.
	 * Used to reintroduce the best network to the genepool as a sort of version control.
	 * Call if no updates to best network have been made after a time of your choosing.
	 */
	public void setBestAsPrimary() {
		networks.set(0, bestNetwork);
	}
	
	/** Used for evolutionary algorithms.
	 * Sets the best overall network as the primary network and sets it as active.
	 */
	public void setBestAsPrimaryAndActive() {
		this.setBestAsPrimary();
		activeNetwork = 0;
	}
	
	/** Used for evolutionary algorithms.
	 * @return the number of networks in the current generation
	 */
	public int getGenerationSize() {
		return networks.size();
	}
	
	/** Used for evolutionary algorithms.
	 * Provide an input for the neural network to process and receive its results. Method not necessarily used for training.
	 * 
	 * @param input A list of doubles that represents the system you wish the network to interpret.
	 * @return a list of doubles that represents the network's response to the input.
	 */
	public List<Double> processInputActive(List<Double> input){
		try {
			//List<Double> toReturn = forwardPropagation(networks.get(activeNetwork),input);
			List<Double> toReturn = forwardPropagationActive(input);
			return toReturn;
		} catch (InvalidPropertiesFormatException e) {
			return null;
		}
	}
	
	/** Used for evolutionary algorithms.
	 * Perform back propagation on the active network to train it on the most recent example that was fed forward. Input MUST be expected result from feeding forward.
	 * @param expected
	 * @return
	 * @throws InvalidPropertiesFormatException 
	 */
	public void trainActiveOnExample(List<Double> input, List<Double> expected) throws InvalidPropertiesFormatException {
		if(networks.get(activeNetwork) == null) {//if there is no network to train
			throw new InvalidPropertiesFormatException("No network to train. Create or load a network.");
		}
		if(networks.get(activeNetwork).getInputLayer().numberOfNeurons() != input.size()) {//verify input sizes match
			throw new InvalidPropertiesFormatException("Input size does not match network input size.");
		}
		if(networks.get(activeNetwork).getOutputLayer().numberOfNeurons() != expected.size()) {//verify output sizes match
			throw new InvalidPropertiesFormatException("ExpectedOutput size "+expected.size()+"; network output size "+networks.get(activeNetwork).getOutputLayer().numberOfNeurons());
		}
		//If we don't have the above problems, we're good to go!
		forwardPropagationActive(input);//performs forward propagation on the given input. All calculated values are saved in the network itself. Do not need output.
		backPropagationActive(expected);//performs back propagation and changes weights in the network.
	}
	
	private static List<Double> forwardPropagationActive(List<Double> input) throws InvalidPropertiesFormatException {
		if(networks.get(activeNetwork) == null) {
			System.out.println("Primary Neural Network not loaded. Returning null.");
			return null;
		}
		List<Double> output = new ArrayList<Double>();
		
		if(input.size()!=networks.get(activeNetwork).getLayer(0).numberOfNeurons()) {//if input array does not equal neural network input layer size
			throw new InvalidPropertiesFormatException("Input size does not match network input size. Expected "+networks.get(activeNetwork).getInputLayer().numberOfNeurons()+", got "+input.size());
		}
		for(int i=0;i<input.size();i++) {
			networks.get(activeNetwork).getLayer(0).getNeuron(i).setActivation(networks.get(activeNetwork).getLayer(0).getNeuron(i).activationFunction(input.get(i)));
		}
		//Input loaded
		//now perform forward propagation
		for(int l=1;l<networks.get(activeNetwork).numberOfLayers();l++) {//for each layer
			for(int n=0;n<networks.get(activeNetwork).getLayer(l).numberOfNeurons();n++) {//for each neuron in the layer
				networks.get(activeNetwork).getLayer(l).getNeuron(n).processInputs();
			}
		}
		//calculations are done. Retrieve output.
		int lastLayer = networks.get(activeNetwork).getLayer((networks.get(activeNetwork).numberOfLayers()-1)).numberOfNeurons();
		for(int i=0;i<lastLayer;i++) {
			output.add(networks.get(activeNetwork).getLayer((networks.get(activeNetwork).numberOfLayers()-1)).getNeuron(i).getActivation());
		}
		//perform softmax on the output
		double sum=0;
		for(int i=0;i<output.size();i++) {
			output.set(i, Math.exp(output.get(i)));
			sum+=output.get(i);
		}
		for(int i=0;i<output.size();i++) {
			output.set(i, output.get(i)/sum);
			networks.get(activeNetwork).getOutputLayer().getNeuron(i).setSoftmaxValue(output.get(i));
		}
		return output;
	}
	
	private static void backPropagationActive(List<Double> trainingOutputs) {
		if(networks.get(activeNetwork) == null) {
			System.out.println("Neural Network not loaded. Returning.");
			return;
		}
		//calculate output error:
		for(int i=0;i<networks.get(activeNetwork).getLayer((networks.get(activeNetwork).numberOfLayers()-1)).numberOfNeurons();i++) {//for each output neuron
			//double output = network.getLayer((network.numberOfLayers()-1)).getNeuron(i).getActivation();
			//network.getOutputLayer().getNeuron(i).setError(2*(trainingOutputs.get(i) - output));//derivative of cost function (y-out)^2
			
			double softmaxOutput = networks.get(activeNetwork).getOutputLayer().getNeuron(i).getSoftmaxValue();
			networks.get(activeNetwork).getOutputLayer().getNeuron(i).setError(-(softmaxOutput-trainingOutputs.get(i)));
		}
		//output layer cost has been set. Now go backwards through the network
		for(int l=networks.get(activeNetwork).numberOfLayers()-1;l>0;l--) {//iterate backwards through the layers
			for(int n=0;n<networks.get(activeNetwork).getLayer(l).numberOfNeurons();n++) {//iterate through the neurons;
				double errorSum=0;
				double activationDerivative = networks.get(activeNetwork).getLayer(l).getNeuron(n).activationFunctionDerivative(networks.get(activeNetwork).getLayer(l).getNeuron(n).getInput());;
				for(int c=0;c<networks.get(activeNetwork).getLayer(l).getNeuron(n).numberOfInputConnections();c++) {//iterate through input connections of current neuron
					double previousActivation = networks.get(activeNetwork).getLayer(l).getNeuron(n).getInputConnection(c).getInputNeuron().getActivation();
					errorSum = networks.get(activeNetwork).getLayer(l).getNeuron(n).sumErrors();
					double change = activationDerivative*previousActivation*errorSum; //calculate change in weight

					networks.get(activeNetwork).getLayer(l).getNeuron(n).getInputConnection(c).getWeight().setChange(
							networks.get(activeNetwork).getLearningRate()*change //set change in weight, but don't update the weight yet!
						);
				}
				double biasChange = errorSum*activationDerivative; //calculate new change in bias
				double newBias = networks.get(activeNetwork).getLayer(l).getNeuron(n).getBias() + (networks.get(activeNetwork).getLearningRate()*biasChange); //calculate new bias
				networks.get(activeNetwork).getLayer(l).getNeuron(n).setBias(newBias); //update bias
			}
		}
		//change has been calculated for all weights
		//update all weights to reflect the changes
		for(int l=(networks.get(activeNetwork).numberOfLayers()-1);l>0;l--) {//iterate backwards through the layers
			for(int n=0;n<networks.get(activeNetwork).getLayer(l).numberOfNeurons();n++) {//iterate through the neurons
				for(int c=0;c<networks.get(activeNetwork).getLayer(l).getNeuron(n).numberOfInputConnections();c++) {//iterate through input connections of current node
					
					networks.get(activeNetwork).getLayer(l).getNeuron(n).getInputConnection(c).updateWeight();
					
				}
			}
		}
	}
	
	//---------------------------------------------------------------
	//MUTATIONS
	
	private static void addRandomNeuron(NeuralNetwork input) {
		Random rng = new Random();
		//pick random hidden layer
		int layer = rng.nextInt(input.numberOfLayers());
		while(true && input.numberOfLayers()>2) {
			//avoid input and output layers
			if(layer!=0 && layer!=input.numberOfLayers()-1) {
				break;
			}
			layer = rng.nextInt(input.numberOfLayers());
		}
		//random layer selected. Add neuron to layer.
		Neuron neuron = new Neuron(rng.nextDouble(), 0, 0, 0, input.getLayer(layer));
		//randomly assign it an activation function
		neuron.setCurrentFunction(rng.nextInt(neuron.getMaxFunctions()));
		//neuron added to layer, now fully connect to previous and next layers.
		for(int i=0;i<input.getLayer(layer-1).numberOfNeurons();i++) {//for all neurons in previous layer
			//Constructors form the objects within the data structure.
			Connection con = new Connection(rng.nextDouble(), 0, input.getLayer(layer-1).getNeuron(i), neuron); //connect all previous neurons to new neuron
		}
		for(int i=0;i<input.getLayer(layer+1).numberOfNeurons();i++) {//for all neurons in next
			Connection con = new Connection(rng.nextDouble(), 0, neuron, input.getLayer(layer+1).getNeuron(i)); //connect new neuron to all neurons in next layer
		}
		
	}
	
	private static void removeRandomNeuron(NeuralNetwork input) {
		Random rng = new Random();
		//pick random hidden layer
		int layer = rng.nextInt(input.numberOfLayers());
		while(true && input.numberOfLayers()>2) {
			//avoid input and output layers
			if(layer!=0 && layer!=input.numberOfLayers()-1) {
				break;
			}
			layer = rng.nextInt(input.numberOfLayers());
		}
		//random layer selected.
		//select random Neuron
		int selectedNeuron = rng.nextInt(input.getLayer(layer).numberOfNeurons());
		//remove neuron
		input.getLayer(layer).getNeuron(selectedNeuron).delete();
	}
	
	private static void addRandomLayer(NeuralNetwork input) {
		Random rng = new Random();
		//pick random layer to duplicate
		int layerIndex = rng.nextInt(input.numberOfLayers());
		//random layer selected.
		Layer originalLayer = input.getLayer(layerIndex);
		Layer newLayer = new Layer(input);
		for(int i=0;i<originalLayer.numberOfNeurons();i++) {//copy values of target layer
			newLayer.addNeuron(originalLayer.getNeuron(i).getBias(), 0, 0, 0);
		}
		//rewire input connections
		for(int n=0;n<originalLayer.numberOfNeurons();n++) {
			for(int c=0;c<originalLayer.getNeuron(n).numberOfInputConnections();c++) {
				originalLayer.getNeuron(n).getInputConnection(c).setOutputNeuron(newLayer.getNeuron(n));
			}
		}
		//inject new layer
		input.addOrMoveLayerAt(newLayer, layerIndex);
		//create new connections between new layer and old
		for(int n=0;n<originalLayer.numberOfNeurons();n++) {
			for(int m=0;m<originalLayer.numberOfNeurons();m++) {
				Connection con = new Connection(rng.nextDouble(), 0, input.getLayer(layerIndex).getNeuron(n), input.getLayer(layerIndex+1).getNeuron(m));
			}
		}
	}
	
	private static void removeRandomLayer(NeuralNetwork input) {
		//if there is only one hidden layer, immediately return
		if(input.numberOfLayers()<=3) {
			return;
		}
		Random rng = new Random();
		//pick random hidden layer
		int layerIndex = rng.nextInt(input.numberOfLayers());
		while(true && input.numberOfLayers()>2) {
			//avoid input and output layers
			if(layerIndex!=0 && layerIndex!=input.numberOfLayers()-1) {
				break;
			}
			layerIndex = rng.nextInt(input.numberOfLayers());
		}
		//random hidden layer selected.
		input.getLayer(layerIndex).delete();
		//Layer deleted. Rewire connections between existing layers
		for(int i=0;i<input.getLayer(layerIndex-1).numberOfNeurons();i++) {
			for(int j=0;j<input.getLayer(layerIndex).numberOfNeurons();j++) {
				Connection con = new Connection(rng.nextDouble(), 0, input.getLayer(layerIndex-1).getNeuron(i), input.getLayer(layerIndex).getNeuron(j));
			}
		}
	}
	
	private static void changeMutationRate(NeuralNetwork input) {
		Random rng = new Random();
		int mutationRate = input.getMutationRate();
		int alteration = rng.nextInt(5)-2;
		while(alteration==0) {
			alteration = rng.nextInt(5)-2;
		}
		if(mutationRate+alteration <= 0) {
			alteration *= -1;
		}
		mutationRate += alteration;
		input.setMutationRate(mutationRate);
	}
	
	private static void changeActivations(NeuralNetwork input) {
		Random rng = new Random();
		//pick random hidden layer
		int layerIndex = rng.nextInt(input.numberOfLayers());
		while(true && input.numberOfLayers()>2) {
			//avoid input and output layers
			if(layerIndex!=0 && layerIndex!=input.numberOfLayers()-1) {
				break;
			}
			layerIndex = rng.nextInt(input.numberOfLayers());
		}
		//random layer selected.
		//select random Neuron
		int selectedNeuron = rng.nextInt(input.getLayer(layerIndex).numberOfNeurons());
		//change activation function
		input.getLayer(layerIndex).getNeuron(selectedNeuron).setCurrentFunction( rng.nextInt( input.getLayer(layerIndex).getNeuron(selectedNeuron).getMaxFunctions() ) );
	}
	
	//Needs more research.
	private static void changeLearningRateRandom(NeuralNetwork input) {
		Random rng = new Random();
		double currentRate = input.getLearningRate();
		double change = rng.nextDouble()*0.1;
		if(rng.nextInt(2)==0) {
			change *= -1;
		}
		currentRate += change;
		input.setLearningRate(currentRate);
	}
	
	private static void removeRandomConnection(NeuralNetwork input) {
		Random rng = new Random();
		//pick random layer
		int layerIndex = rng.nextInt(input.numberOfLayers());
		//random layer selected.
		int selectedNeuron = rng.nextInt(input.getLayer(layerIndex).numberOfNeurons());
		//random neuron selected
		//input or output connection?
		int side = rng.nextInt(2);//0 = input, 1 = output
		if(side == 0 && !input.getLayer(layerIndex).getNeuron(selectedNeuron).hasInputConnections()) {//if inputs chosen but none exist
			side = 1; //switch to outputs
		}else if(side == 1 && !input.getLayer(layerIndex).getNeuron(selectedNeuron).hasOutputConnections()) {//inverse rule
			side = 0;
		}
		if(side==0) {//now remove connection
			if(input.getLayer(layerIndex).getNeuron(selectedNeuron).numberOfInputConnections()<=1) {
				return;
			}
			int toDelete = rng.nextInt(input.getLayer(layerIndex).getNeuron(selectedNeuron).numberOfInputConnections());
			input.getLayer(layerIndex).getNeuron(selectedNeuron).getInputConnection(toDelete).delete();
		}else{
			if(input.getLayer(layerIndex).getNeuron(selectedNeuron).numberOfOutputConnections()<=1) {
				return;
			}
			int toDelete = rng.nextInt(input.getLayer(layerIndex).getNeuron(selectedNeuron).numberOfOutputConnections());
			input.getLayer(layerIndex).getNeuron(selectedNeuron).getOutputConnection(toDelete).delete();			
		}
	}
	
	private static void addRandomConnection(NeuralNetwork input) {
		Random rng = new Random();
		//pick two layers
		int layerOne = rng.nextInt(input.numberOfLayers());
		int layerTwo = rng.nextInt(input.numberOfLayers());
		while(layerOne == layerTwo) {
			layerTwo = rng.nextInt(input.numberOfLayers());
		}
		Neuron neuronOne;
		Neuron neuronTwo;
		if(layerOne < layerTwo) {//neuronOne comes before neuronTwo
			neuronOne = input.getLayer(layerOne).getNeuron(rng.nextInt(input.getLayer(layerOne).numberOfNeurons()));
			neuronTwo = input.getLayer(layerTwo).getNeuron(rng.nextInt(input.getLayer(layerTwo).numberOfNeurons()));
		}else {
			neuronTwo = input.getLayer(layerOne).getNeuron(rng.nextInt(input.getLayer(layerOne).numberOfNeurons()));
			neuronOne = input.getLayer(layerTwo).getNeuron(rng.nextInt(input.getLayer(layerTwo).numberOfNeurons()));			
		}
		//with the two random neuron selected, create a connection between them.
		Connection con = new Connection(rng.nextDouble(),0,neuronOne,neuronTwo);
	}
	
	/** Used for evolutionary algorithms. Generally at the very beginning of the project just to start off with a little design.
	 * Set the activation function that the specified layer will use.
	 * 0 - Sigmoid
	 * 1 - Tanh
	 * 2 - softplus
	 * 3 - leakyReLU
	 * 4 - swish
	 * other - defaults to sigmoid
	 * 
	 * @param function
	 */
	public void setPrimaryNetworkLayerActivation(int layer, int function) {
		for(int i=0;i<networks.get(0).getLayer(layer).numberOfNeurons();i++) {
			networks.get(0).getLayer(layer).getNeuron(i).setCurrentFunction(function);
		}
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
			backPropagation(example,expected);
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
				backPropagation(example,testOutput);
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
	
	private static void printNNTwo(NeuralNetwork network) {
		System.out.println("-------PRINTING NETWORK-------");
		System.out.println("Number of layers: "+network.numberOfLayers());
		for(int i=0;i<network.numberOfLayers();i++) {
			System.out.println("-------------");
			System.out.println("Layer "+i+": "+network.getLayer(i).numberOfNeurons()+" neurons");
			for(int j=0;j<network.getLayer(i).numberOfNeurons();j++) {
				System.out.println("-----");
				System.out.println("Connections out of neuron "+j+" with bias "+network.getLayer(i).getNeuron(j).getBias()+": "+network.getLayer(i).getNeuron(j).numberOfOutputConnections());
				for(int c=0;c<network.getLayer(i).getNeuron(j).numberOfOutputConnections();c++) {
					System.out.println("Weight: "+network.getLayer(i).getNeuron(j).getOutputConnection(c).getWeight().getValue());
				}
			}
			System.out.println("-------------");
		}
		System.out.println("-------NETWORK PRINTED-------");
	}
	
	private static List<Double> forwardPropagation(NeuralNetwork nn, List<Double> input) throws InvalidPropertiesFormatException {
		List<Double> output = new ArrayList<Double>();
		
		if(input.size()!=nn.getInputLayer().numberOfNeurons()) {
			throw new InvalidPropertiesFormatException("Input size does not match network input size");
		}
		for(int i=0;i<input.size();i++) {
			nn.getInputLayer().getNeuron(i).setActivation(nn.getLayer(0).getNeuron(i).activationFunction(input.get(i)));
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
		//perform softmax on the output
		double sum=0;
		for(int i=0;i<output.size();i++) {
			output.set(i, Math.exp(output.get(i)));
			sum+=output.get(i);
		}
		for(int i=0;i<output.size();i++) {
			output.set(i, output.get(i)/sum);
			nn.getOutputLayer().getNeuron(i).setSoftmaxValue(output.get(i));
		}
		return output;
	}
	
	private static void backPropagation(NeuralNetwork nn, List<Double> trainingOutputs) {
		//calculate output error:
		for(int i=0;i<nn.getOutputLayer().numberOfNeurons();i++) {//for each output neuron
			double output = nn.getOutputLayer().getNeuron(i).getActivation();
			nn.getOutputLayer().getNeuron(i).setError(2*(trainingOutputs.get(i) - output));//derivative of cost function (y-out)^2
			
			double softmaxOutput = nn.getOutputLayer().getNeuron(i).getSoftmaxValue();
			nn.getOutputLayer().getNeuron(i).setError(-(softmaxOutput-trainingOutputs.get(i)));
		}
		//output layer cost has been set. Now go backwards through the network
		for(int l=nn.numberOfLayers()-1;l>0;l--) {//iterate backwards through the layers
			for(int n=0;n<nn.getLayer(l).numberOfNeurons();n++) {//iterate through the neurons;
				
				double errorSum=0;
				double activationDerivative = nn.getLayer(l).getNeuron(n).activationFunctionDerivative(nn.getLayer(l).getNeuron(n).getInput());
				for(int c=0;c<nn.getLayer(l).getNeuron(n).numberOfInputConnections();c++) {//iterate through input connections of current neuron
					double previousActivation = nn.getLayer(l).getNeuron(n).getInputConnection(c).getInputNeuron().getActivation();
					errorSum = nn.getLayer(l).getNeuron(n).sumErrors();
					double change = activationDerivative*previousActivation*errorSum;

					nn.getLayer(l).getNeuron(n).getInputConnection(c).getWeight().setChange(
							nn.getLearningRate()*change
						);
				}
				double biasChange = errorSum*activationDerivative;
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
	}	
}
