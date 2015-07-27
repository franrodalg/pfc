package dflibrary.utils.security;

/**
 * Provides singleton objects for representing the different options
 * for padding arrays
 * @author Francisco Rodriguez Algarra
 *
 */
public enum PaddingMode {

    /**
     * Padding with zeros 
     */
    ZEROPadding{},
    /**
     * Padding with zeros and 0x80
     */
    EIGHTPadding{};
	
}
