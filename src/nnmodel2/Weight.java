/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package nnmodel2;
import java.io.Serializable;

// line 32 "../Persistence.ump"
// line 29 "../Model2.ump"
public class Weight implements Serializable
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Weight Attributes
  private double value;
  private double change;

  //Weight Associations
  private Connection connection;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Weight(double aValue, double aChange, Connection aConnection)
  {
    value = aValue;
    change = aChange;
    if (aConnection == null || aConnection.getWeight() != null)
    {
      throw new RuntimeException("Unable to create Weight due to aConnection");
    }
    connection = aConnection;
  }

  public Weight(double aValue, double aChange, Neuron aInputNeuronForConnection, Neuron aOutputNeuronForConnection)
  {
    value = aValue;
    change = aChange;
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

  public boolean setChange(double aChange)
  {
    boolean wasSet = false;
    change = aChange;
    wasSet = true;
    return wasSet;
  }

  public double getValue()
  {
    return value;
  }

  public double getChange()
  {
    return change;
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
            "value" + ":" + getValue()+ "," +
            "change" + ":" + getChange()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "connection = "+(getConnection()!=null?Integer.toHexString(System.identityHashCode(getConnection())):"null");
  }  
  //------------------------
  // DEVELOPER CODE - PROVIDED AS-IS
  //------------------------
  
  // line 35 "../Persistence.ump"
  private static final long serialVersionUID = 4495546738870249064L ;

  
}