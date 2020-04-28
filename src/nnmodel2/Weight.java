/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package nnmodel2;

// line 62 "../Model2.ump"
public class Weight
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Weight Attributes
  private double value;

  //Weight Associations
  private Connection connection;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Weight(double aValue, Connection aConnection)
  {
    value = aValue;
    if (aConnection == null || aConnection.getWeight() != null)
    {
      throw new RuntimeException("Unable to create Weight due to aConnection");
    }
    connection = aConnection;
  }

  public Weight(double aValue, Neuron aInputNeuronForConnection, Neuron aOutputNeuronForConnection)
  {
    value = aValue;
    connection = new Connection(this, aInputNeuronForConnection, aOutputNeuronForConnection);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setValue(double aValue)
  {
    boolean wasSet = false;
    value = aValue;
    wasSet = true;
    return wasSet;
  }

  public double getValue()
  {
    return value;
  }
  /* Code from template association_GetOne */
  public Connection getConnection()
  {
    return connection;
  }

  public void delete()
  {
    Connection existingConnection = connection;
    connection = null;
    if (existingConnection != null)
    {
      existingConnection.delete();
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "value" + ":" + getValue()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "connection = "+(getConnection()!=null?Integer.toHexString(System.identityHashCode(getConnection())):"null");
  }
}