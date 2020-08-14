/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package nnmodel2;
import java.io.Serializable;

// line 11 "../Persistence.ump"
// line 157 "../ExtraCode.ump"
// line 25 "../Model2.ump"
public class Connection implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Connection Associations
  private Weight weight;
  private Neuron inputNeuron;
  private Neuron outputNeuron;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Connection(Weight aWeight, Neuron aInputNeuron, Neuron aOutputNeuron)
  {
    if (aWeight == null || aWeight.getConnection() != null)
    {
      throw new RuntimeException("Unable to create Connection due to aWeight");
    }
    weight = aWeight;
    boolean didAddInputNeuron = setInputNeuron(aInputNeuron);
    if (!didAddInputNeuron)
    {
      throw new RuntimeException("Unable to create outputConnection due to inputNeuron");
    }
    boolean didAddOutputNeuron = setOutputNeuron(aOutputNeuron);
    if (!didAddOutputNeuron)
    {
      throw new RuntimeException("Unable to create inputConnection due to outputNeuron");
    }
  }

  public Connection(double aValueForWeight, double aChangeForWeight, Neuron aInputNeuron, Neuron aOutputNeuron)
  {
    weight = new Weight(aValueForWeight, aChangeForWeight, this);
    boolean didAddInputNeuron = setInputNeuron(aInputNeuron);
    if (!didAddInputNeuron)
    {
      throw new RuntimeException("Unable to create outputConnection due to inputNeuron");
    }
    boolean didAddOutputNeuron = setOutputNeuron(aOutputNeuron);
    if (!didAddOutputNeuron)
    {
      throw new RuntimeException("Unable to create inputConnection due to outputNeuron");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------
  /* Code from template association_GetOne */
  public Weight getWeight()
  {
    return weight;
  }
  /* Code from template association_GetOne */
  public Neuron getInputNeuron()
  {
    return inputNeuron;
  }
  /* Code from template association_GetOne */
  public Neuron getOutputNeuron()
  {
    return outputNeuron;
  }
  /* Code from template association_SetOneToMany */
  public boolean setInputNeuron(Neuron aInputNeuron)
  {
    boolean wasSet = false;
    if (aInputNeuron == null)
    {
      return wasSet;
    }

    Neuron existingInputNeuron = inputNeuron;
    inputNeuron = aInputNeuron;
    if (existingInputNeuron != null && !existingInputNeuron.equals(aInputNeuron))
    {
      existingInputNeuron.removeOutputConnection(this);
    }
    inputNeuron.addOutputConnection(this);
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_SetOneToMany */
  public boolean setOutputNeuron(Neuron aOutputNeuron)
  {
    boolean wasSet = false;
    if (aOutputNeuron == null)
    {
      return wasSet;
    }

    Neuron existingOutputNeuron = outputNeuron;
    outputNeuron = aOutputNeuron;
    if (existingOutputNeuron != null && !existingOutputNeuron.equals(aOutputNeuron))
    {
      existingOutputNeuron.removeInputConnection(this);
    }
    outputNeuron.addInputConnection(this);
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    Weight existingWeight = weight;
    weight = null;
    if (existingWeight != null)
    {
      existingWeight.delete();
    }
    Neuron placeholderInputNeuron = inputNeuron;
    this.inputNeuron = null;
    if(placeholderInputNeuron != null)
    {
      placeholderInputNeuron.removeOutputConnection(this);
    }
    Neuron placeholderOutputNeuron = outputNeuron;
    this.outputNeuron = null;
    if(placeholderOutputNeuron != null)
    {
      placeholderOutputNeuron.removeInputConnection(this);
    }
  }

  // line 159 "../ExtraCode.ump"
   public void updateWeight(){
    weight.setValue(weight.getValue()+weight.getChange());
  		weight.setChange(0);
  }
  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 14 "../Persistence.ump"
  private static final long serialVersionUID = 4267485601061759914L ;

  
}