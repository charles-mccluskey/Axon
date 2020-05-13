/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package nnmodel2;
import java.io.Serializable;
import java.util.*;

// line 18 "../Persistence.ump"
// line 40 "../Model2.ump"
public class Neuron implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Neuron Attributes
  private double bias;
  private double activation;
  private double error;

  //Neuron Associations
  private List<Connection> outputConnections;
  private List<Connection> inputConnections;
  private Layer layer;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Neuron(double aBias, double aActivation, double aError, Layer aLayer)
  {
    bias = aBias;
    activation = aActivation;
    error = aError;
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

  public boolean setActivation(double aActivation)
  {
    boolean wasSet = false;
    activation = aActivation;
    wasSet = true;
    return wasSet;
  }

  public boolean setError(double aError)
  {
    boolean wasSet = false;
    error = aError;
    wasSet = true;
    return wasSet;
  }

  public double getBias()
  {
    return bias;
  }

  public double getActivation()
  {
    return activation;
  }

  public double getError()
  {
    return error;
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

  // line 49 "../Model2.ump"
   private double sigmoid(double input){
    return 1 / (1 + Math.exp(-1*input));
  }

  // line 53 "../Model2.ump"
   public double sigPrime(double input){
    return sigmoid(input) + (1- sigmoid(input));
  }

  // line 57 "../Model2.ump"
   public void processInputs(){
    double sum = getBias();
		List<Connection> connections = getInputConnections();
		for(int i=0;i<connections.size();i++) {
			sum += connections.get(i).getInputNeuron().getActivation()*connections.get(i).getWeight().getValue(); 
		}
		setActivation(sigmoid(sum));
  }

  // line 66 "../Model2.ump"
   public double getInput(){
    double sum = getBias();
	   List<Connection> connections = getInputConnections();
	   for(int i=0;i<connections.size();i++) {
		   sum += connections.get(i).getInputNeuron().getActivation()*connections.get(i).getWeight().getValue(); 
	   }
	   return sum;
  }
   
   public double sumErrors() {
	   double sum = 0;
	   for(int i=0;i<numberOfOutputConnections();i++) {
		   sum += getOutputConnection(i).getOutputNeuron().getError()
				   * getOutputConnection(i).getWeight().getValue()
				   * sigPrime(getOutputConnection(i).getOutputNeuron().getInput());
	   }
	   return sum;
   }


  public String toString()
  {
    return super.toString() + "["+
            "bias" + ":" + getBias()+ "," +
            "activation" + ":" + getActivation()+ "," +
            "error" + ":" + getError()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "layer = "+(getLayer()!=null?Integer.toHexString(System.identityHashCode(getLayer())):"null");
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 21 "../Persistence.ump"
  private static final long serialVersionUID = 3389752283403781197L ;

  
}