/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package nnmodel2;
import java.util.*;

// line 26 "../Model2.ump"
public class Neuron
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Neuron Attributes
  private double bias;
  private double neuralValue;

  //Neuron Associations
  private List<Connection> outputConnections;
  private List<Connection> inputConnections;
  private Layer layer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Neuron(double aBias, double aNeuralValue, Layer aLayer)
  {
    bias = aBias;
    neuralValue = aNeuralValue;
    outputConnections = new ArrayList<Connection>();
    inputConnections = new ArrayList<Connection>();
    boolean didAddLayer = setLayer(aLayer);
    if (!didAddLayer)
    {
      throw new RuntimeException("Unable to create neuron due to layer");
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

  public boolean setNeuralValue(double aNeuralValue)
  {
    boolean wasSet = false;
    neuralValue = aNeuralValue;
    wasSet = true;
    return wasSet;
  }

  public double getBias()
  {
    return bias;
  }

  public double getNeuralValue()
  {
    return neuralValue;
  }
  /* Code from template association_GetMany */
  public Connection getOutputConnection(int index)
  {
    Connection aOutputConnection = outputConnections.get(index);
    return aOutputConnection;
  }

  public List<Connection> getOutputConnections()
  {
    List<Connection> newOutputConnections = Collections.unmodifiableList(outputConnections);
    return newOutputConnections;
  }

  public int numberOfOutputConnections()
  {
    int number = outputConnections.size();
    return number;
  }

  public boolean hasOutputConnections()
  {
    boolean has = outputConnections.size() > 0;
    return has;
  }

  public int indexOfOutputConnection(Connection aOutputConnection)
  {
    int index = outputConnections.indexOf(aOutputConnection);
    return index;
  }
  /* Code from template association_GetMany */
  public Connection getInputConnection(int index)
  {
    Connection aInputConnection = inputConnections.get(index);
    return aInputConnection;
  }

  public List<Connection> getInputConnections()
  {
    List<Connection> newInputConnections = Collections.unmodifiableList(inputConnections);
    return newInputConnections;
  }

  public int numberOfInputConnections()
  {
    int number = inputConnections.size();
    return number;
  }

  public boolean hasInputConnections()
  {
    boolean has = inputConnections.size() > 0;
    return has;
  }

  public int indexOfInputConnection(Connection aInputConnection)
  {
    int index = inputConnections.indexOf(aInputConnection);
    return index;
  }
  /* Code from template association_GetOne */
  public Layer getLayer()
  {
    return layer;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfOutputConnections()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Connection addOutputConnection(Weight aWeight, Neuron aOutputNeuron)
  {
    return new Connection(aWeight, this, aOutputNeuron);
  }

  public boolean addOutputConnection(Connection aOutputConnection)
  {
    boolean wasAdded = false;
    if (outputConnections.contains(aOutputConnection)) { return false; }
    Neuron existingInputNeuron = aOutputConnection.getInputNeuron();
    boolean isNewInputNeuron = existingInputNeuron != null && !this.equals(existingInputNeuron);
    if (isNewInputNeuron)
    {
      aOutputConnection.setInputNeuron(this);
    }
    else
    {
      outputConnections.add(aOutputConnection);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeOutputConnection(Connection aOutputConnection)
  {
    boolean wasRemoved = false;
    //Unable to remove aOutputConnection, as it must always have a inputNeuron
    if (!this.equals(aOutputConnection.getInputNeuron()))
    {
      outputConnections.remove(aOutputConnection);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addOutputConnectionAt(Connection aOutputConnection, int index)
  {  
    boolean wasAdded = false;
    if(addOutputConnection(aOutputConnection))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOutputConnections()) { index = numberOfOutputConnections() - 1; }
      outputConnections.remove(aOutputConnection);
      outputConnections.add(index, aOutputConnection);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveOutputConnectionAt(Connection aOutputConnection, int index)
  {
    boolean wasAdded = false;
    if(outputConnections.contains(aOutputConnection))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfOutputConnections()) { index = numberOfOutputConnections() - 1; }
      outputConnections.remove(aOutputConnection);
      outputConnections.add(index, aOutputConnection);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addOutputConnectionAt(aOutputConnection, index);
    }
    return wasAdded;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfInputConnections()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Connection addInputConnection(Weight aWeight, Neuron aInputNeuron)
  {
    return new Connection(aWeight, aInputNeuron, this);
  }

  public boolean addInputConnection(Connection aInputConnection)
  {
    boolean wasAdded = false;
    if (inputConnections.contains(aInputConnection)) { return false; }
    Neuron existingOutputNeuron = aInputConnection.getOutputNeuron();
    boolean isNewOutputNeuron = existingOutputNeuron != null && !this.equals(existingOutputNeuron);
    if (isNewOutputNeuron)
    {
      aInputConnection.setOutputNeuron(this);
    }
    else
    {
      inputConnections.add(aInputConnection);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeInputConnection(Connection aInputConnection)
  {
    boolean wasRemoved = false;
    //Unable to remove aInputConnection, as it must always have a outputNeuron
    if (!this.equals(aInputConnection.getOutputNeuron()))
    {
      inputConnections.remove(aInputConnection);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addInputConnectionAt(Connection aInputConnection, int index)
  {  
    boolean wasAdded = false;
    if(addInputConnection(aInputConnection))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfInputConnections()) { index = numberOfInputConnections() - 1; }
      inputConnections.remove(aInputConnection);
      inputConnections.add(index, aInputConnection);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveInputConnectionAt(Connection aInputConnection, int index)
  {
    boolean wasAdded = false;
    if(inputConnections.contains(aInputConnection))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfInputConnections()) { index = numberOfInputConnections() - 1; }
      inputConnections.remove(aInputConnection);
      inputConnections.add(index, aInputConnection);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addInputConnectionAt(aInputConnection, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMandatoryMany */
  public boolean setLayer(Layer aLayer)
  {
    boolean wasSet = false;
    //Must provide layer to neuron
    if (aLayer == null)
    {
      return wasSet;
    }

    if (layer != null && layer.numberOfNeurons() <= Layer.minimumNumberOfNeurons())
    {
      return wasSet;
    }

    Layer existingLayer = layer;
    layer = aLayer;
    if (existingLayer != null && !existingLayer.equals(aLayer))
    {
      boolean didRemove = existingLayer.removeNeuron(this);
      if (!didRemove)
      {
        layer = existingLayer;
        return wasSet;
      }
    }
    layer.addNeuron(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    for(int i=outputConnections.size(); i > 0; i--)
    {
      Connection aOutputConnection = outputConnections.get(i - 1);
      aOutputConnection.delete();
    }
    for(int i=inputConnections.size(); i > 0; i--)
    {
      Connection aInputConnection = inputConnections.get(i - 1);
      aInputConnection.delete();
    }
    Layer placeholderLayer = layer;
    this.layer = null;
    if(placeholderLayer != null)
    {
      placeholderLayer.removeNeuron(this);
    }
  }

  // line 34 "../Model2.ump"
   private double sigmoid(double input){
    return 1 / (1 + Math.exp(-1*input));
  }

  // line 38 "../Model2.ump"
   public double process(List<Double> input){
    double sum = getBias();
	   List<Connection> connections = getInputConnections();
	   List<Double> weights = new ArrayList<Double>();
	   for(int i=0;i<connections.size();i++) {
		   weights.add(connections.get(i).getWeight().getValue());
	   }
	   for(int i=0;i<input.size();i++) {//calculate input of sigmoid function: WX + b
		   sum += weights.get(i)*input.get(i);
	   }
	   //System.out.println("sum = "+sum);
	   return sigmoid(sum);
  }


  public String toString()
  {
    return super.toString() + "["+
            "bias" + ":" + getBias()+ "," +
            "neuralValue" + ":" + getNeuralValue()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "layer = "+(getLayer()!=null?Integer.toHexString(System.identityHashCode(getLayer())):"null");
  }
}