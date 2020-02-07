/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package nnmodel;
import java.util.*;

// line 56 "../BaseModel.ump"
public class Layer
{

  //------------------------
  // STATIC VARIABLES
  //------------------------

  private static int nextId = 1;

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Autounique Attributes
  private int id;

  //Layer Associations
  private List<Node> nodes;
  private Layer outputNeighbor;
  private NeuralNetwork neuralNetwork;
  private Layer inputNeighbor;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Layer(NeuralNetwork aNeuralNetwork)
  {
    id = nextId++;
    nodes = new ArrayList<Node>();
    boolean didAddNeuralNetwork = setNeuralNetwork(aNeuralNetwork);
    if (!didAddNeuralNetwork)
    {
      throw new RuntimeException("Unable to create layer due to neuralNetwork");
    }
  }

  //------------------------
  // INTERFACE
  //------------------------

  public int getId()
  {
    return id;
  }
  /* Code from template association_GetMany */
  public Node getNode(int index)
  {
    Node aNode = nodes.get(index);
    return aNode;
  }

  public List<Node> getNodes()
  {
    List<Node> newNodes = Collections.unmodifiableList(nodes);
    return newNodes;
  }

  public int numberOfNodes()
  {
    int number = nodes.size();
    return number;
  }

  public boolean hasNodes()
  {
    boolean has = nodes.size() > 0;
    return has;
  }

  public int indexOfNode(Node aNode)
  {
    int index = nodes.indexOf(aNode);
    return index;
  }
  /* Code from template association_GetOne */
  public Layer getOutputNeighbor()
  {
    return outputNeighbor;
  }

  public boolean hasOutputNeighbor()
  {
    boolean has = outputNeighbor != null;
    return has;
  }
  /* Code from template association_GetOne */
  public NeuralNetwork getNeuralNetwork()
  {
    return neuralNetwork;
  }
  /* Code from template association_GetOne */
  public Layer getInputNeighbor()
  {
    return inputNeighbor;
  }

  public boolean hasInputNeighbor()
  {
    boolean has = inputNeighbor != null;
    return has;
  }
  /* Code from template association_IsNumberOfValidMethod */
  public boolean isNumberOfNodesValid()
  {
    boolean isValid = numberOfNodes() >= minimumNumberOfNodes();
    return isValid;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfNodes()
  {
    return 1;
  }
  /* Code from template association_AddMandatoryManyToOne */
  public Node addNode(double aBias)
  {
    Node aNewNode = new Node(aBias, this);
    return aNewNode;
  }

  public boolean addNode(Node aNode)
  {
    boolean wasAdded = false;
    if (nodes.contains(aNode)) { return false; }
    Layer existingLayer = aNode.getLayer();
    boolean isNewLayer = existingLayer != null && !this.equals(existingLayer);

    if (isNewLayer && existingLayer.numberOfNodes() <= minimumNumberOfNodes())
    {
      return wasAdded;
    }
    if (isNewLayer)
    {
      aNode.setLayer(this);
    }
    else
    {
      nodes.add(aNode);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeNode(Node aNode)
  {
    boolean wasRemoved = false;
    //Unable to remove aNode, as it must always have a layer
    if (this.equals(aNode.getLayer()))
    {
      return wasRemoved;
    }

    //layer already at minimum (1)
    if (numberOfNodes() <= minimumNumberOfNodes())
    {
      return wasRemoved;
    }

    nodes.remove(aNode);
    wasRemoved = true;
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addNodeAt(Node aNode, int index)
  {  
    boolean wasAdded = false;
    if(addNode(aNode))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfNodes()) { index = numberOfNodes() - 1; }
      nodes.remove(aNode);
      nodes.add(index, aNode);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveNodeAt(Node aNode, int index)
  {
    boolean wasAdded = false;
    if(nodes.contains(aNode))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfNodes()) { index = numberOfNodes() - 1; }
      nodes.remove(aNode);
      nodes.add(index, aNode);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addNodeAt(aNode, index);
    }
    return wasAdded;
  }
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setOutputNeighbor(Layer aNewOutputNeighbor)
  {
    boolean wasSet = false;
    if (aNewOutputNeighbor == null)
    {
      Layer existingOutputNeighbor = outputNeighbor;
      outputNeighbor = null;
      
      if (existingOutputNeighbor != null && existingOutputNeighbor.getInputNeighbor() != null)
      {
        existingOutputNeighbor.setInputNeighbor(null);
      }
      wasSet = true;
      return wasSet;
    }

    Layer currentOutputNeighbor = getOutputNeighbor();
    if (currentOutputNeighbor != null && !currentOutputNeighbor.equals(aNewOutputNeighbor))
    {
      currentOutputNeighbor.setInputNeighbor(null);
    }

    outputNeighbor = aNewOutputNeighbor;
    Layer existingInputNeighbor = aNewOutputNeighbor.getInputNeighbor();

    if (!equals(existingInputNeighbor))
    {
      aNewOutputNeighbor.setInputNeighbor(this);
    }
    wasSet = true;
    return wasSet;
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
  /* Code from template association_SetOptionalOneToOptionalOne */
  public boolean setInputNeighbor(Layer aNewInputNeighbor)
  {
    boolean wasSet = false;
    if (aNewInputNeighbor == null)
    {
      Layer existingInputNeighbor = inputNeighbor;
      inputNeighbor = null;
      
      if (existingInputNeighbor != null && existingInputNeighbor.getOutputNeighbor() != null)
      {
        existingInputNeighbor.setOutputNeighbor(null);
      }
      wasSet = true;
      return wasSet;
    }

    Layer currentInputNeighbor = getInputNeighbor();
    if (currentInputNeighbor != null && !currentInputNeighbor.equals(aNewInputNeighbor))
    {
      currentInputNeighbor.setOutputNeighbor(null);
    }

    inputNeighbor = aNewInputNeighbor;
    Layer existingOutputNeighbor = aNewInputNeighbor.getOutputNeighbor();

    if (!equals(existingOutputNeighbor))
    {
      aNewInputNeighbor.setOutputNeighbor(this);
    }
    wasSet = true;
    return wasSet;
  }

  public void delete()
  {
    for(int i=nodes.size(); i > 0; i--)
    {
      Node aNode = nodes.get(i - 1);
      aNode.delete();
    }
    if (outputNeighbor != null)
    {
      outputNeighbor.setInputNeighbor(null);
    }
    NeuralNetwork placeholderNeuralNetwork = neuralNetwork;
    this.neuralNetwork = null;
    if(placeholderNeuralNetwork != null)
    {
      placeholderNeuralNetwork.removeLayer(this);
    }
    if (inputNeighbor != null)
    {
      inputNeighbor.setOutputNeighbor(null);
    }
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "]" + System.getProperties().getProperty("line.separator") +
            "  " + "neuralNetwork = "+(getNeuralNetwork()!=null?Integer.toHexString(System.identityHashCode(getNeuralNetwork())):"null");
  }
}