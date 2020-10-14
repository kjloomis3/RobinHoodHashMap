package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import util.RobinHoodHashMap;

/**
 * Performs a series of tests on the RobinHoodHashMap.
 * 
 * @author Ken Loomis (https://github.com/kjloomis3)
 */
public class RobinHoodHashMapTest {

	private RobinHoodHashMap<String, Integer> map1;
	private RobinHoodHashMap<String, Integer> map5;
	private RobinHoodHashMap<String, Integer> mapd;
	private RobinHoodHashMap<String, Integer> mapfilled5;
	
	private static final String [] array = { "AB", "AA", "BA", "CA", "LM", 
			 												  "MN", "NM", "KJ", "PO", "RL",
			 												  "RN", "EM", "ZA", "TO", "WL",
															  "OL", "XY", "ZY", "YZ",  "ZZ"};
	
	private static ArrayList<String> list;
	
	@Before
	public void setUp() throws Exception {
		map1 = new RobinHoodHashMap<String, Integer>(1);
		map5 = new RobinHoodHashMap<String, Integer>(5);
		mapd = new RobinHoodHashMap<String, Integer>( );
		mapfilled5 = new RobinHoodHashMap<String, Integer>( );
		
		list = new ArrayList<String>(array.length);
		
		if ( list.size() == 0) {
			for ( int i=0; i<array.length;i++)
				list.add(array[i]);
		}
		
		if ( mapfilled5.size() < 5 )
		{
			for ( int i=0; i<5;i++)
				mapfilled5.put(array[i], i);
		}
		
	}

	/**
	 * Test method for {@link util.RobinHoodHashMap#RobinHoodHashMap()}.
	 */
	@Test
	public void testRobinHoodHashMap() {
		assertNotNull (mapd);
	}
	
	/**
	 * Test method for {@link util.RobinHoodHashMap#RobinHoodHashMap(int)}.
	 */
	@Test
	public void testRobinHoodHashMapInt() {
		assertNotNull (map1);
		assertNotNull (map5);
		
		RobinHoodHashMap<String, Integer> setBad;
		try {
			setBad = new RobinHoodHashMap<String, Integer>(-1);
			fail ("Cannot instantiate set with negative size.");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid size for set", e.getMessage());
		}
	}

	/**
	 * Test method for {@link util.RobinHoodHashMap#size()}.
	 */
	@Test
	public void testSize() {
		assertEquals(0, map1.size());
		assertEquals(0, map5.size());
		assertEquals(0, mapd.size());
		
		int i=1;
		for ( String s: list )
		{
				mapd.put( s, i );
				assertEquals(i++, mapd.size() );
		}
	}

	@Test
	public void testIsEmpty() {
		assertTrue( map1.isEmpty() );
		assertTrue( map5.isEmpty() );
		assertTrue( mapd.isEmpty() );
		mapd.put( list.get(0), 0);
		assertFalse( mapd.isEmpty() );
	}

	@Test
	public void testIsFull() {
		assertFalse( map1.isFull() );
		assertFalse( map5.isFull() );
		assertFalse( mapd.isFull() );
		map1.put( list.get(0), 0);
		assertTrue( map1.isFull() );
	}

	@Test
	public void testGet() {
		for ( int i=0; i<5;i++)
			assertEquals( i, (int) mapfilled5.get(array[i] ) );
		
		assertNull ( map1.get( "bad key" ) );
		assertNull ( mapfilled5.get( "bad key" ) );
	}

	@Test
	public void testContainsKey() {
		for ( int i=0; i<5;i++)
			assertTrue( mapfilled5.containsKey(array[i] ) );
		
		assertFalse ( map1.containsKey( "bad key" ) );
		assertFalse ( mapfilled5.containsKey( "bad key" ) );
	}

	@Test
	public void testContainsValue() {
		for ( int i=0; i<5;i++)
			assertTrue( mapfilled5.containsValue( i ) );
		
		assertFalse ( map1.containsValue( -1 ) );
		assertFalse ( mapfilled5.containsValue( 100 ) );
	}

	/**
	 * Test method for {@link util.RobinHoodHashMap#put()}.
	 */
	@Test
	public void testPut() {
			for ( int i=0; i<5;i++)
			{
				assertEquals(i, (int) map5.put(array[i], i));
				assertTrue ( map5.containsKey(array[i]) );
				assertTrue ( map5.containsValue( i ) );
				assertEquals(i, (int) map5.get( array[i] ) );
			}
		map5.put( "Too many", 100 );
		assertFalse ( map5.containsKey( "Too many") );
		assertEquals(100, (int) map5.put ( array[0], 100));
		assertTrue ( map5.containsKey(array[0]) );
		assertTrue ( map5.containsValue( 100 ) );
		assertEquals(100, (int) map5.get( array[0] ) );
	}

	@Test
	public void testRemove() {
		
		assertNull ( map1.remove(null));
		assertNull ( mapfilled5.remove(null));
		
		assertNull ( mapfilled5.remove("Bad Key"));
		
		for ( int i=0; i<5;i++)
		{
			assertEquals(i, (int) mapfilled5.remove(array[i]));
			assertFalse( mapfilled5.containsKey(array[i]) );
			assertFalse ( map5.containsValue( i ) );
		}
		assertTrue( mapfilled5.isEmpty() );
	}

	@Test
	public void testPutAll() {
		mapd.putAll(mapfilled5);
		for ( Integer value: mapd.values() ) {
			assertTrue( mapfilled5.containsValue(value));
		}
		for ( Integer value: mapfilled5.values() ) {
			assertTrue( mapd.containsValue(value));
		}
		for ( String key: mapd.keySet() ) {
			assertTrue( mapfilled5.containsKey(key));
		}
		for ( String key: mapfilled5.keySet()) {
			assertTrue( mapd.containsKey(key));
		}
		
		
	}

	@Test
	public void testKeySet() {
		
	}

	@Test
	public void testKeySetToArray() {

		Object[] test = map1.keySet().toArray();
		assertEquals ( 0, test.length );
		
		test = mapfilled5.keySet().toArray();
		assertEquals ( 5, test.length );
		for ( int i=0; i<test.length; i++ )
		{
			assertTrue ( mapfilled5.containsKey( test[i]) );
		}

	}
	
	@Test
	public void testKeySetToTArray() {
		Object[]  test = new String[ 10 ];
		test = mapfilled5.keySet().toArray(test);
		for ( int i=0; i<test.length && test[i] != null; i++ )
		{
			assertTrue ( mapfilled5.containsKey( test[i]) );
		}
	}
	
	@Test
	public void testKeySetIterator() {
		for ( String key: mapfilled5.keySet() )
		{
			assertTrue ( mapfilled5.containsKey(key) );
		}
	}


	
	@Test
	public void testValues() {
		fail("Not yet implemented");
	}

	@Test
	public void testEntrySet() {
		fail("Not yet implemented");
	}

	@Test
	public void testClear() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

	
}
