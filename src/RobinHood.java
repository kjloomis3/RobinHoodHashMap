import util.RobinHoodHashSet;

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
