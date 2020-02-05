/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package nnmodel;
import java.util.*;

// line 43 "../BaseModel.ump"
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
  private List<Layer> inputNeighbors;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Layer()
  {
    id = nextId++;
    nodes = new ArrayList<Node>();
    inputNeighbors = new ArrayList<Layer>();
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
  /* Code from template association_GetMany */
  public Layer getInputNeighbor(int index)
  {
    Layer aInputNeighbor = inputNeighbors.get(index);
    return aInputNeighbor;
  }

  public List<Layer> getInputNeighbors()
  {
    List<Layer> newInputNeighbors = Collections.unmodifiableList(inputNeighbors);
    return newInputNeighbors;
  }

  public int numberOfInputNeighbors()
  {
    int number = inputNeighbors.size();
    return number;
  }

  public boolean hasInputNeighbors()
  {
    boolean has = inputNeighbors.size() > 0;
    return has;
  }

  public int indexOfInputNeighbor(Layer aInputNeighbor)
  {
    int index = inputNeighbors.indexOf(aInputNeighbor);
    return index;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfNodes()
  {
    return 0;
  }
  /* Code from template association_AddManyToOne */
  public Node addNode(double aBias)
  {
    return new Node(aBias, this);
  }

  public boolean addNode(Node aNode)
  {
    boolean wasAdded = false;
    if (nodes.contains(aNode)) { return false; }
    Layer existingLayer = aNode.getLayer();
    boolean isNewLayer = existingLayer != null && !this.equals(existingLayer);
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
    if (!this.equals(aNode.getLayer()))
    {
      nodes.remove(aNode);
      wasRemoved = true;
    }
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
  /* Code from template association_SetOptionalOneToMany */
  public boolean setOutputNeighbor(Layer aOutputNeighbor)
  {
    boolean wasSet = false;
    Layer existingOutputNeighbor = outputNeighbor;
    outputNeighbor = aOutputNeighbor;
    if (existingOutputNeighbor != null && !existingOutputNeighbor.equals(aOutputNeighbor))
    {
      existingOutputNeighbor.removeInputNeighbor(this);
    }
    if (aOutputNeighbor != null)
    {
      aOutputNeighbor.addInputNeighbor(this);
    }
    wasSet = true;
    return wasSet;
  }
  /* Code from template association_MinimumNumberOfMethod */
  public static int minimumNumberOfInputNeighbors()
  {
    return 0;
  }
  /* Code from template association_AddManyToOptionalOne */
  public boolean addInputNeighbor(Layer aInputNeighbor)
  {
    boolean wasAdded = false;
    if (inputNeighbors.contains(aInputNeighbor)) { return false; }
    Layer existingOutputNeighbor = aInputNeighbor.getOutputNeighbor();
    if (existingOutputNeighbor == null)
    {
      aInputNeighbor.setOutputNeighbor(this);
    }
    else if (!this.equals(existingOutputNeighbor))
    {
      existingOutputNeighbor.removeInputNeighbor(aInputNeighbor);
      addInputNeighbor(aInputNeighbor);
    }
    else
    {
      inputNeighbors.add(aInputNeighbor);
    }
    wasAdded = true;
    return wasAdded;
  }

  public boolean removeInputNeighbor(Layer aInputNeighbor)
  {
    boolean wasRemoved = false;
    if (inputNeighbors.contains(aInputNeighbor))
    {
      inputNeighbors.remove(aInputNeighbor);
      aInputNeighbor.setOutputNeighbor(null);
      wasRemoved = true;
    }
    return wasRemoved;
  }
  /* Code from template association_AddIndexControlFunctions */
  public boolean addInputNeighborAt(Layer aInputNeighbor, int index)
  {  
    boolean wasAdded = false;
    if(addInputNeighbor(aInputNeighbor))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfInputNeighbors()) { index = numberOfInputNeighbors() - 1; }
      inputNeighbors.remove(aInputNeighbor);
      inputNeighbors.add(index, aInputNeighbor);
      wasAdded = true;
    }
    return wasAdded;
  }

  public boolean addOrMoveInputNeighborAt(Layer aInputNeighbor, int index)
  {
    boolean wasAdded = false;
    if(inputNeighbors.contains(aInputNeighbor))
    {
      if(index < 0 ) { index = 0; }
      if(index > numberOfInputNeighbors()) { index = numberOfInputNeighbors() - 1; }
      inputNeighbors.remove(aInputNeighbor);
      inputNeighbors.add(index, aInputNeighbor);
      wasAdded = true;
    } 
    else 
    {
      wasAdded = addInputNeighborAt(aInputNeighbor, index);
    }
    return wasAdded;
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
      Layer placeholderOutputNeighbor = outputNeighbor;
      this.outputNeighbor = null;
      placeholderOutputNeighbor.removeInputNeighbor(this);
    }
    while( !inputNeighbors.isEmpty() )
    {
      inputNeighbors.get(0).setOutputNeighbor(null);
    }
  }

  // line 51 "../BaseModel.ump"
   public  Layer(Layer inputLayer){
    id = nextId++;
    nodes = new ArrayList<Node>();
    inputNeighbors = new ArrayList<Layer>();
    inputNeighbors.add(inputLayer);
    inputLayer.setOutputNeighbor(this);
  }


  public String toString()
  {
    return super.toString() + "["+
            "id" + ":" + getId()+ "]";
  }
}