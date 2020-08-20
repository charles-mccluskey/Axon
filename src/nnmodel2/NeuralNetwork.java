/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package nnmodel2;
import java.io.Serializable;
import java.io.*;
import java.util.*;

// line 4 "../Persistence.ump"
// line 5 "../ExtraCode.ump"
// line 6 "../Model2.ump"
public class NeuralNetwork implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //NeuralNetwork Attributes
  private int mutationRate;
  private double learningRate;

  //NeuralNetwork Associations
  private List<Layer> layers;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public NeuralNetwork(double aLearningRate)
  {
    mutationRate = 5;
    learningRate = aLearningRate;
    layers = new ArrayList<Layer>();
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setMutationRate(int aMutationRate)
  {
    boolean wasSet = false;
    mutationRate = aMutationRate;
    wasSet = true;
    return wasSet;
  }

  public boolean setLearningRate(double aLearningRate)
  {
    boolean wasSet = false;
    learningRate = aLearningRate;
    wasSet = true;
    return wasSet;
  }

  public int getMutationRate()
  {
    return mutationRate;
  }

  public double getLearningRate()
  {
    return learningRate;
  }
  /* Code from template association_GetMany */
  public Layer getLayer(int index)
  {
    Layer aLayer = layers.get(index);
    return aLayer;
  }

  public List<Layer> getLayers()
  {
    List<Layer> newLayers = Collections.unmodifiableList(layers);
    return newLayers;
  }

  public int numberOfLayers()
  {
    int number = layers.size();
    return number;
  }

  public boolean hasLayers()
  {
    boolean has = layers.size() > 0;
    return has;
  }

  public int indexOfLayer(Layer aLayer)
  {
    int index = layers.indexOf(aLayer);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfLayers()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Layer addLayer()
  {
    return new Layer(this);
  }

  public boolean addLayer(Layer aLayer)
  {
    boolean wasAdded = false;
    if (layers.contains(aLayer)) { return false; }
    NeuralNetwork existingNeuralNetwork = aLayer.getNeuralNetwork();
    boolean isNewNeuralNetwork = existingNeuralNetwork != null && !this.equals(existingNeuralNetwork);
    if (isNewNeuralNetwork)
    {
      aLayer.setNeuralNetwork(this);
    }
    else
    {
      layers.add(aLayer);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeLayer(Layer aLayer)
  {
    boolean wasRemoved = false;
    //Unable to remove aLayer, as it must always have a neuralNetwork
    if (!this.equals(aLayer.getNeuralNetwork()))
    {
      layers.remove(aLayer);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addLayerAt(Layer aLayer, int index)
  {  
    boolean wasAdded = false;
    if(addLayer(aLayer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLayers()) { index = numberOfLayers() - 1; }
      layers.remove(aLayer);
      layers.add(index, aLayer);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveLayerAt(Layer aLayer, int index)
  {
    boolean wasAdded = false;
    if(layers.contains(aLayer))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfLayers()) { index = numberOfLayers() - 1; }
      layers.remove(aLayer);
      layers.add(index, aLayer);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addLayerAt(aLayer, index);
    }
    return wasAdded;
  }

  public void delete()
  {
    while (layers.size() > 0)
    {
      Layer aLayer = layers.get(layers.size() - 1);
      aLayer.delete();
      layers.remove(aLayer);
    }
    
  }

  // line 10 "../ExtraCode.ump"
   public  NeuralNetwork(int numInputs, int numHiddenLayers, int nodesPerLayer, int numOutputs, double learningRate){
    mutationRate = 5;
		Random rng = new Random();
	   	layers = new ArrayList<Layer>();
    	//build input layer
    	addLayer();
    	for(int i=0;i<numInputs;i++) {
    		getLayer(0).addNeuron(rng.nextDouble(), 0, 0);
    	}
    	//build hidden layers
    	for(int i=1;i<=numHiddenLayers;i++) {
    		addLayer();
    		for(int j=0;j<nodesPerLayer;j++) {
    			getLayer(i).addNeuron(rng.nextDouble(), 0, 0);
    		}
    	}
    	//build output layer
    	addLayer();
    	for(int i=0;i<numOutputs;i++) {
    		getLayer(1+numHiddenLayers).addNeuron(rng.nextDouble(), 0, 0);
    	}
    	//neurons have been created, now to connect them.
    	for(int l=0;l<=numHiddenLayers;l++) {
    		for(int n=0;n<getLayer(l).numberOfNeurons();n++) {//nodes in neural layer
    			for(int m=0;m<getLayer(l+1).numberOfNeurons();m++) {//nodes in adjacent neural layer
    				Connection con = new Connection(rng.nextDouble(),0,getLayer(l).getNeuron(n),getLayer(l+1).getNeuron(m));// randomly initialize weights
    			}
    		}
    	}
    	setLearningRate(learningRate);
  }

  // line 42 "../ExtraCode.ump"
   public NeuralNetwork deepClone(){
    try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(this);

			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return (NeuralNetwork) ois.readObject();
		} catch (IOException e) {
			return null;
		} catch (ClassNotFoundException e) {
			return null;
		}
  }


  public String toString()
  {
    return super.toString() + "["+
            "mutationRate" + ":" + getMutationRate()+ "," +
            "learningRate" + ":" + getLearningRate()+ "]";
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 7 "../Persistence.ump"
  private static final long serialVersionUID = 6181302407834705923L ;

  
}