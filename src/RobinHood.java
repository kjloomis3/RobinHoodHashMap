import util.RobinHoodHashSet;

/**
 * This is a test program for the RobinHoodHashSet. It performs some common
 * operations on the set, but is not an exhaustive test. See RobinHoodHashSetTest
 * for more testing. 
 * 
 * @author Ken Loomis (https://github.com/kjloomis3)
 */
public class RobinHood {

	public static void main(String[] args) 
	{
		
		final String [] list = { "AB", "AA", "BA", "CA", "LM", 
				  "MN", "NM", "KJ", "PO", "RL",
				  "RN", "EM", "ZA", "TO", "WL",
				  "OL", "XY", "ZY", "YZ",  "ZZ"};
		RobinHoodHashSet<String> table = new RobinHoodHashSet<String> ( );
		
		for ( int i = 0; i < list.length; i++ )
		{
			String s = list[i];
			if ( table.add( s ) )
			{
				System.out.println( "Table added " + s );
			}
			if (  table.contains( s ) )
			{
				System.out.println( "Table contains " + s );
			}
			else
			{
				System.out.println( "Table doesn't contain " + s );
				System.out.println ( table );
				table.contains( s );
			}
			System.out.println ( table );
		}

		for ( int i = 0; i < list.length; i++ )
		{
			String s = list[i];
			if ( table.remove( s ) )
			{
				System.out.println( "Table removed " + s );
			}
			if (  !table.contains( s ) )
			{
				System.out.println( "Table doesn't contain " + s );
			}
			System.out.println ( table );
		}
		
	}
	
}
