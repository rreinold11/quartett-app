package data;

import config.ServiceLocator;
import contracts.data.DataProvider;
import contracts.game.ICard;
import contracts.game.ICardCategory;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Class to save the data about the communes (cards) in a database. H2 is a
 * in-memory database and with Ebean we can manage the communication with H2.
 * 
 * @author Witsch Daniel
 *
 */
@Entity
public class Card extends Model implements ICard {

    @Id
    public UUID ID = UUID.randomUUID();
    /** name of commune */
    private String name;
    /** population of last census */
    private int population;
    /** size of the whole town */
    private float area;
    /** indebtedness in percentage */
    private float indebtedness;
    /**
     * nights in hotels, apartements and other touristic overnight possibilities
     */
    private int nights;
    /** places where everybody can do sport activities like soccer, swimming,.. */
    private int sportFields;
    /** UUID of the ranking for this card in the database */
    private UUID ranking;
    /** attribute to make it easier to compute a query in the database */
    protected static Finder<UUID, Card> find = new Finder<UUID, Card>(UUID.class, Card.class);

    protected final void setName(String name) {
	this.name = name;
    }

    protected final void setPopulation(int population) {
	this.population = population;
    }

    protected final void setArea(float area) {
	this.area = area;
    }

    protected final void setIndebtedness(float indebtedness) {
	this.indebtedness = indebtedness;
    }

    protected final void setNights(int nights) {
	this.nights = nights;
    }

    protected final void setSportFields(int sportFields) {
	this.sportFields = sportFields;
    }

    protected final void setRanking(UUID ranking) {
	this.ranking = ranking;
    }

    public final UUID getID() {
	return ID;
    }

    public final String getName() {
	return name;
    }

    public final int getPopulation() {
	return population;
    }

    public final float getArea() {
	return area;
    }

    public final float getIndebtedness() {
	return indebtedness;
    }

    public final int getNights() {
	return nights;
    }

    public final int getSportFields() {
	return sportFields;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((ID == null) ? 0 : ID.hashCode());
	result = prime * result + Float.floatToIntBits(area);
	result = prime * result + Float.floatToIntBits(indebtedness);
	result = prime * result + ((name == null) ? 0 : name.hashCode());
	result = prime * result + nights;
	result = prime * result + population;
	result = prime * result + sportFields;
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Card other = (Card) obj;
	if (ID == null) {
	    if (other.ID != null)
		return false;
	} else if (!ID.equals(other.ID))
	    return false;
	if (Float.floatToIntBits(area) != Float.floatToIntBits(other.area))
	    return false;
	if (Float.floatToIntBits(indebtedness) != Float.floatToIntBits(other.indebtedness))
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
	    return false;
	if (nights != other.nights)
	    return false;
	if (population != other.population)
	    return false;
		//noinspection RedundantIfStatement
		if (sportFields != other.sportFields)
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "Card [ID=" + ID + ", name=" + name + ", population=" + population + ", area=" + area
		+ ", indebtedness=" + indebtedness + ", nights=" + nights + ", sportFields=" + sportFields + "]";
    }

    /**
     * @return returns the URL to the picture file of this card
     */
    @Override
    public String getImageUrl() {
	String res = "/images/"+ name;
		res = res.toLowerCase();
		res = res.replaceAll("ü", "ue");
		res = res.replaceAll("ö", "oe");
		res = res.replaceAll("ä", "ae");
		res = res.replaceAll("[ .]", "");
		res = res + ".jpg";
		
		return res;
    }

    /**
     * method to get all Categories of a card in strings
     * 
     * @return List of CardCategories in Strings to show the categories and the
     *         values in the view
     */
    @Override
    public List<ICardCategory> getCategories() {
	List<contracts.game.ICardCategory> categories = new LinkedList<>();
	categories.add(new CardCategory(0, "population", String.format("%,d", population)));
	categories.add(new CardCategory(1, "area", String.format("%.2f km²", area)));
	categories.add(new CardCategory(2, "indebtedness", String.format("%.2f%%", indebtedness)));
	categories.add(new CardCategory(3, "overnightstays", String.format("%,d", nights)));
	categories.add(new CardCategory(4, "sportfacilities", String.format("%,d",sportFields)));
	return categories;
    }

    /**
     * method to get the rankings of all categories of a card in a Array
     * 
     * @return the rankings in a Integer Array
     */
    @Override
    public Integer[] getRankingArray() {
	Integer[] array = new Integer[5];
	DataProvider db = ServiceLocator.getDataProvider();
	array[0] = db.getRankingsByID(this.ranking).getRankPopulation();
	array[1] = db.getRankingsByID(this.ranking).getRankArea();
	array[2] = db.getRankingsByID(this.ranking).getRankIndebtedness();
	array[3] = db.getRankingsByID(this.ranking).getRankNights();
	array[4] = db.getRankingsByID(this.ranking).getRankSportFields();
	return array;
    }
}
