/**
* The Town Class is where it all happens.
* The Town is designed to manage all of the things a Hunter can do in town.
* This code has been adapted from Ivan Turner's original program -- thank you Mr. Turner!
*/
public class Town
{
  //instance variables
  private Hunter hunter;
  private Shop shop;
  private Terrain terrain;
  private String printMessage;
  private boolean toughTown;
  private boolean searchedForTreasure;
  private boolean cheatMode;
   
  //Constructor
  /**
  * The Town Constructor takes in a shop and the surrounding terrain, but leaves the hunter as null until one arrives.
  * @param s The town's shoppe.
  * @param t The surrounding terrain.
  * @param c If cheat mode is activated
  */
  public Town(Shop shop, double toughness, boolean cheatMode)
  {
    this.shop = shop;
    this.terrain = getNewTerrain();
    this.cheatMode = cheatMode;
    
    // the hunter gets set using the hunterArrives method, which
    // gets called from a client class
    hunter = null;
    
    printMessage = "";
    searchedForTreasure = false;
    
    // higher toughness = more likely to be a tough town
    toughTown = (Math.random() < toughness);
  }
  
  public String getLatestNews()
  {
    return printMessage;
  }
   
  /**
  * Assigns an object to the Hunter in town.
  * @param h The arriving Hunter.
  */
  public void hunterArrives(Hunter hunter)
  {
    this.hunter = hunter;
    printMessage = "Welcome to town, " + hunter.getHunterName() + ".";
      
    if (toughTown)
    {
      printMessage += "\nIt's pretty rough around here, so watch yourself.";
    }
    else
    {
      printMessage += "\nWe're just a sleepy little town with mild mannered folk.";
    }
  }
   
  /*
   * Generatres treasure for the town
   */
  public String searchForTreasure() 
  {
    String treasure = null;

    if (!searchedForTreasure)
    {
      int rand = (int) (Math.random() * 4);
      if (rand == 0)
      {
        treasure = "Diamond";
      }
      else if (rand == 1)
      {
        treasure = "Gold";
      }
      else if (rand == 2)
      {
        treasure = "Jewelery";
      }
      else 
      {
        treasure = "";
      }
      searchedForTreasure = true;
    }
    return treasure;
  }
  /**
  * Handles the action of the Hunter leaving the town.
  * @return true if the Hunter was able to leave town.
  */
  public boolean leaveTown()
  {
    boolean canLeaveTown = terrain.canCrossTerrain(hunter);
    if (canLeaveTown)
    {
      String item = terrain.getNeededItem();
      printMessage = "You used your " + item + " to cross the " + terrain.getTerrainName() + ".";
      if (checkItemBreak())
      {
        hunter.removeItemFromKit(item);
        printMessage += "\nUnfortunately, your " + item + " broke.";
      }
            
      return true;
    }
      
    printMessage = "You can't leave town, " + hunter.getHunterName() + ". You don't have a " + terrain.getNeededItem() + ".";
    return false;
  }
  
  public void enterShop(String choice)
  {
    shop.enter(hunter, choice);
  }
  
  /**
  * Gives the hunter a chance to fight for some gold.<p>
  * The chances of finding a fight and winning the gold are based on the toughness of the town.<p>
  * The tougher the town, the easier it is to find a fight, and the harder it is to win one.
  */
  public void lookForTrouble()
  {
    double noTroubleChance;
    if (toughTown)
    {
      noTroubleChance = 0.66;
    }
    else
    {
      noTroubleChance = 0.33;
    }
    
    if (Math.random() > noTroubleChance)
    {
      printMessage = "You couldn't find any trouble";
    }
    else
    {
      printMessage = "You want trouble, stranger!  You got it!\n" + getFightAnimation();
      int goldDiff = (int)(Math.random() * 10) + 1;

      if (cheatMode)
      {
        goldDiff = 100;
        printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
        printMessage += "\nYou won the brawl and receive " +  goldDiff + " gold.";
        hunter.changeGold(goldDiff);
      }
      else if (Math.random() > noTroubleChance)
      {
        printMessage += "Okay, stranger! You proved yer mettle. Here, take my gold.";
        printMessage += "\nYou won the brawl and receive " +  goldDiff + " gold.";
        hunter.changeGold(goldDiff);
      }
      else
      {
        printMessage += "That'll teach you to go lookin' fer trouble in MY town! Now pay up!";
        printMessage += "\nYou lost the brawl and pay " +  goldDiff + " gold.";
        hunter.changeGold(-1 * goldDiff);
      }
    }
  }
   
  public String toString()
  {
    return "This nice little town is surrounded by " + terrain.getTerrainName() + ".";
  }
  
  /**
  * Determines the surrounding terrain for a town, and the item needed in order to cross that terrain.
  * 
  * @return A Terrain object.
  */
  private Terrain getNewTerrain()
  {
    double rnd = Math.random();
    if (rnd < 0.17)
    {
      return new Terrain("Mountains", "rope");
    }
    else if (rnd < 0.33)
    {
      return new Terrain("Ocean", "boat");
    }
    else if (rnd < 0.5)
    {
      return new Terrain("Plains", "horse");
    }
    else if (rnd < 0.66)
    {
      return new Terrain("Desert", "water");
    }
    else if (rnd < 0.83)
    {
      return new Terrain("Jungle", "machete");
    }
    else 
    {
      return new Terrain("Caves", "torch");
    }
  }
  
  /**
  * Determines whether or not a used item has broken.
  * @return true if the item broke.
  */
  private boolean checkItemBreak()
  {
    double rand = Math.random();
    return (rand < 0.5);
  }

  /* 
   * Returns different fight "animations" depending on the terrain
   */
  private String getFightAnimation() 
  {
    String terrainName = terrain.getTerrainName();

    if (terrainName.equals("Mountains"))
    {
      return "Wind Foosh Foosh\n";
    }
    else if (terrainName.equals("Ocean"))
    {
      return "Water glug glug\n";
    }
    else if (terrainName.equals("Plains"))
    {
      return "Grass shi shi\n";
    }
    else if (terrainName.equals("Desert"))
    {
      return "Tumbleweed tumble tumble\n";
    }
    else if (terrainName.equals("Jungle"))
    {
      return "Monkey Ooh Ahh\n";
    }
    else 
    {
      return "Rock Crack Boom\n";
    }
  }
}