package es.csic.iiia.normlab.traffic.utils;


/**
 * Enumeration to manage the velocity of the cars in the simulation
 */
public enum Speed {
	None, Medium, High, VeryHigh,
	MuchLower, Lower, Equal, Higher, MuchHigher;

	/**
	 * To string method
	 */
	public String toString() {
		switch(this) {
		case None: 				return "None";
		case Medium: 			return "Medium";
		case High: 				return "High";
		case VeryHigh:		return "VeryHigh";
		case MuchLower:		return "MuchLower";
		case Lower:				return "Lower";
		case Equal:				return "Equal";
		case Higher:			return "Higher";
		case MuchHigher:	return "MuchHigher";
		default: 					return "";
		}
	}

	/**
	 * To string with a minimized word
	 */
	public String toStringMinimized() {
		switch(this) {
		case MuchLower:		return "ML";
		case Lower:				return "LO";
		case Equal:				return "EQ";
		case Higher:			return "HI";
		case MuchHigher:	return "MH";
		case None:				return "ST";
		default:					return "  ";
		}
	}
	
	/**
	 * To string with one only character
	 */
	public String toStringOneCharacter() {
		switch(this) {
		case None: 				return "n";
		case Medium: 			return "m";
		case High: 				return "h";
		case VeryHigh:		return "H";
		default:					return " ";
		}
	}
}

