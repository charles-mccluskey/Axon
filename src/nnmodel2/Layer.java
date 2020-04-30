/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package nnmodel2;
import java.io.Serializable;
import java.util.*;

// line 25 "../Persistence.ump"
// line 61 "../Model2.ump"
public class Layer implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Layer Associations
  private List<Neuron> neurons;
  private NeuralNetwork neuralNetwork;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Layer(NeuralNetwork aNeuralNetwork)
  {
    neurons = new ArrayList<Neuron>();
    boolean didAddNeuralNetwork = setNeuralNetwork(aNeuralNetwork);
    if (!didAddNeuralNetwork)
    {
      throw new RuntimeException("Unable to create layer due to neuralNetwork");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetMany */
  public Neuron getNeuron(int index)
  {
    Neuron aNeuron = neurons.get(index);
    return aNeuron;
  }

  public List<Neuron> getNeurons()
  {
    List<Neuron> newNeurons = Collections.unmodifiableList(neurons);
    return newNeurons;
  }

  public int numberOfNeurons()
  {
    int number = neurons.size();
    return number;
  }

  public boolean hasNeurons()
  {
    boolean has = neurons.size() > 0;
    return has;
  }

  public int indexOfNeuron(Neuron aNeuron)
  {
    int index = neurons.indexOf(aNeuron);
    return index;
  }
  /* Code from template association_GetOne */
  public NeuralNetwork getNeuralNetwork()
  {
    return neuralNetwork;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfNeuronsValid()
  {
    boolean isValid = numberOfNeurons() >= minimumNumberOfNeurons();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfNeurons()
  {
    return 1;
  }
  /* Code from template association_AddMandatoryManyToOne */
  public Neuron addNeuron(double aBias, double aActivation)
  {
    Neuron aNewNeuron = new Neuron(aBias, aActivation, this);
    return aNewNeuron;
  }

  public boolean addNeuron(Neuron aNeuron)
  {
    boolean wasAdded = false;
    if (neurons.contains(aNeuron)) { return false; }
    Layer existingLayer = aNeuron.getLayer();
    boolean isNewLayer = existingLayer != null && !this.equals(existingLayer);

    if (isNewLayer && existingLayer.numberOfNeurons() <= minimumNumberOfNeurons())
    {
      return wasAdded;
    }
    if (isNewLayer)
    {
      aNeuron.setLayer(this);
    }
    else
    {
      neurons.add(aNeuron);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeNeuron(Neuron aNeuron)
  {
    boolean wasRemoved = false;
    //Unable to remove aNeuron, as it must always have a layer
    if (this.equals(aNeuron.getLayer()))
    {
      return wasRemoved;
    }

    //layer already at minimum (1)
    if (numberOfNeurons() <= minimumNumberOfNeurons())
    {
      return wasRemoved;
    }

    neurons.remove(aNeuron);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addNeuronAt(Neuron aNeuron, int index)
  {  
    boolean wasAdded = false;
    if(addNeuron(aNeuron))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfNeurons()) { index = numberOfNeurons() - 1; }
      neurons.remove(aNeuron);
      neurons.add(index, aNeuron);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveNeuronAt(Neuron aNeuron, int index)
  {
    boolean wasAdded = false;
    if(neurons.contains(aNeuron))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfNeurons()) { index = numberOfNeurons() - 1; }
      neurons.remove(aNeuron);
      neurons.add(index, aNeuron);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addNeuronAt(aNeuron, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOneToMany */
  public boolean setNeuralNetwork(NeuralNetwork aNeuralNetwork)
  {
    boolean wasSet = false;
    if (aNeuralNetwork == null)
    {
      return wasSet;
    }

    NeuralNetwork existingNeuralNetwork = neuralNetwork;
    neuralNetwork = aNeuralNetwork;
    if (existingNeuralNetwork != null && !existingNeuralNetwork.equals(aNeuralNetwork))
    {
      existingNeuralNetwork.removeLayer(this);
    }
    neuralNetwork.addLayer(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    for(int i=neurons.size(); i > 0; i--)
    {
      Neuron aNeuron = neurons.get(i - 1);
      aNeuron.delete();
    }
    NeuralNetwork placeholderNeuralNetwork = neuralNetwork;
    this.neuralNetwork = null;
    if(placeholderNeuralNetwork != null)
    {
      placeholderNeuralNetwork.removeLayer(this);
    }
  }
  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 28 "../Persistence.ump"
  private static final long serialVersionUID = 1959250384912895829L ;

  
}