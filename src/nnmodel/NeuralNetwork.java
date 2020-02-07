/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package nnmodel;
import java.util.*;

// line 3 "../BaseModel.ump"
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

}