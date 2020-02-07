/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package nnmodel;
import java.util.*;

// line 8 "../BaseModel.ump"
public class Node
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Node Attributes
  private double bias;
  private List<Double> weights;

  //Node Associations
  private Layer layer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Node(double aBias, Layer aLayer)
  {
    bias = aBias;
    weights = new ArrayList<Double>();
    boolean didAddLayer = setLayer(aLayer);
    if (!didAddLayer)
    {
      throw new RuntimeException("Unable to create node due to layer");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setBias(double aBias)
  {
    boolean wasSet = false;
    bias = aBias;
    wasSet = true;
    return wasSet;
  }
  /* Code from template attribute_SetMany */
  public boolean addWeight(Double aWeight)
  {
    boolean wasAdded = false;
    wasAdded = weights.add(aWeight);
    return wasAdded;
  }

  public boolean removeWeight(Double aWeight)
  {
    boolean wasRemoved = false;
    wasRemoved = weights.remove(aWeight);
    return wasRemoved;
  }

  public double getBias()
  {
    return bias;
  }
  /* Code from template attribute_GetMany */
  public Double getWeight(int index)
  {
    Double aWeight = weights.get(index);
    return aWeight;
  }

  public Double[] getWeights()
  {
    Double[] newWeights = weights.toArray(new Double[weights.size()]);
    return newWeights;
  }

  public int numberOfWeights()
  {
    int number = weights.size();
    return number;
  }

  public boolean hasWeights()
  {
    boolean has = weights.size() > 0;
    return has;
  }

  public int indexOfWeight(Double aWeight)
  {
    int index = weights.indexOf(aWeight);
    return index;
  }
  /* Code from template association_GetOne */
  public Layer getLayer()
  {
    return layer;
  }
  /* Code from template association_SetOneToMandatoryMany */
  public boolean setLayer(Layer aLayer)
  {
    boolean wasSet = false;
    //Must provide layer to node
    if (aLayer == null)
    {
      return wasSet;
    }

    if (layer != null && layer.numberOfNodes() <= Layer.minimumNumberOfNodes())
    {
      return wasSet;
    }

    Layer existingLayer = layer;
    layer = aLayer;
    if (existingLayer != null && !existingLayer.equals(aLayer))
    {
      boolean didRemove = existingLayer.removeNode(this);
      if (!didRemove)
      {
        layer = existingLayer;
        return wasSet;
      }
    }
    layer.addNode(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Layer placeholderLayer = layer;
    this.layer = null;
    if(placeholderLayer != null)
    {
      placeholderLayer.removeNode(this);
    }
  }


  /**
   * public Node(double aBias, Layer aLayer, Layer inputLayer)//constructor for hidden/output layers
   * {
   * 
   * bias = aBias;
   * weights = new ArrayList<Double>();
   * for(int i=0;i<inputLayer.numberOfNodes();i++) {
   * addWeight(1.0);
   * }
   * boolean didAddLayer = setLayer(aLayer);
   * if (!didAddLayer)
   * {
   * throw new RuntimeException("Unable to create node due to layer");
   * }
   * }
   */
  // line 30 "../BaseModel.ump"
  public void initializeWeights(int prevLayerSize){
	  while(numberOfWeights()<prevLayerSize) {
		  Random rng = new Random();
		  double init = rng.nextDouble();
		  addWeight(init);
	  }
  }

  // line 38 "../BaseModel.ump"
   private double sigmoid(double input){
    return 1 / (1 + Math.exp(-1*input));
  }

  // line 42 "../BaseModel.ump"
   public double process(List<Double> input){
	   double sum = getBias();
	   for(int i=0;i<input.size();i++) {//calculate input of sigmoid function: WX + b
		   sum += getWeight(i)*input.get(i);
	   }
	   //System.out.println("sum = "+sum);
	   return sigmoid(sum);
  }


  public String toString()
  {
    return super.toString() + "["+
            "bias" + ":" + getBias()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "layer = "+(getLayer()!=null?Integer.toHexString(System.identityHashCode(getLayer())):"null");
  }
}