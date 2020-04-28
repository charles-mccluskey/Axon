/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package nnmodel2;
import java.util.*;

// line 3 "../Model2.ump"
public class NeuralNetwork
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //NeuralNetwork Associations
  private List<Layer> layers;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public NeuralNetwork()
  {
    layers = new ArrayList<Layer>();
  }

  //------------------------
  // INTERFACE
  //------------------------
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

  // line 6 "../Model2.ump"
   public  NeuralNetwork(int numLayers, int nodesPerLayer){
    layers = new ArrayList<Layer>();
	   for(int i=0;i<numLayers;i++) {
		   addLayer();
		   for(int j=0;j<nodesPerLayer;j++) {
			   getLayer(i).addNeuron(0,0);
		   }
	   }
	   //neurons have been created, now to connect them.
	   Random rng = new Random();
	   for(int l=0;l<numLayers-1;l++) {
		   for(int n=0;n<getLayer(l).numberOfNeurons();n++) {//nodes in neural layer
			   for(int m=0;m<getLayer(l+1).numberOfNeurons();m++) {//nodes in adjacent neural layer
				   Connection con = new Connection(rng.nextDouble(),getLayer(l).getNeuron(n),getLayer(l+1).getNeuron(m));// randomly initialize weights
			   }
		   }
	   }
  }

}