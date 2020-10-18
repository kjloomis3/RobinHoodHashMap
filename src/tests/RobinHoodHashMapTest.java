package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import util.RobinHoodHashMap;

/**
 * Performs a series of tests on the RobinHoodHashMap using a mapping
 * from String => Integer. 
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
	private static ArrayList<String>halfList;
	
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
		
		halfList = new ArrayList<String>(array.length);
		
		if ( halfList.size() == 0) {
			for ( int i=0; i<array.length;i+=2)
				halfList.add(array[i]);
		}
		
		if ( mapfilled5.size() < 5 )
		{
			for ( int i=0; i<5;i++)
				mapfilled5.put(array[i], i);
		}
		
	}

	/**
	 * Tests the default constructor to ensure that it does produce a map.
	 * Test method for {@link util.RobinHoodHashMap#RobinHoodHashMap()}.
	 */
	@Test
	public void testRobinHoodHashMap() {
		assertNotNull (mapd);
	}
	
	/**
	 * Tests the parameterized constructor to ensure that it does produce a map.
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
	 * Tests to ensure that the size of the map increases
	 * correctly when new elements are added and decreases correctly
	 * when elements are removed.
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
		for ( String s: list )
		{
				mapd.remove( s );
				assertEquals(i--, mapd.size() );
		}
		
	}

	/**
	 * Tests to ensure that the isEmpty method for a map
	 * produces the correct result:
	 * true: if empty
	 * false: if map contains at least 1 element. 
	 * Test method for {@link util.RobinHoodHashMap#isEmpty()}.
	 */
	@Test
	public void testIsEmpty() {
		assertTrue( map1.isEmpty() );
		assertTrue( map5.isEmpty() );
		assertTrue( mapd.isEmpty() );
		mapd.put( list.get(0), 0);
		assertFalse( mapd.isEmpty() );
	}

	/**
	 * Tests to ensure that the isFull method for a map
	 * produces the correct result:
	 * true: if map is at capacity
	 * false: if map contains at least 1 empty storage location
	 * Test method for {@link util.RobinHoodHashMap#isFull()}.
	 */
	@Test
	public void testIsFull() {
		assertFalse( map1.isFull() );
		assertFalse( map5.isFull() );
		assertFalse( mapd.isFull() );
		map1.put( list.get(0), 0);
		assertTrue( map1.isFull() );
	}

	/**
	 * Tests to ensure that the get method produces the correct value
	 * for the given key. If the key is invalid, tests that the returned
	 * value is null.
	 * Test method for {@link util.RobinHoodHashMap#get()}.
	 */
	@Test
	public void testGet() {
		for ( int i=0; i<5;i++)
			assertEquals( i, (int) mapfilled5.get(array[i] ) );
		
		assertNull ( map1.get( "bad key" ) );
		assertNull ( mapfilled5.get( "bad key" ) );
	}

	/**
	 * Tests to ensure that the contains key method returns true
	 * if the key has a value in the map and false otherwise.
	 * Test method for {@link util.RobinHoodHashMap#testContainsKey()}.
	 */
	@Test
	public void testContainsKey() {
		for ( int i=0; i<5;i++)
			assertTrue( mapfilled5.containsKey(array[i] ) );
		
		assertFalse ( map1.containsKey( "bad key" ) );
		assertFalse ( mapfilled5.containsKey( "bad key" ) );
	}

	/**
	 * Tests to ensure that the contains key method returns true
	 * if the value is in the map for at least 1 key and false otherwise.
	 * Test method for {@link util.RobinHoodHashMap#testContainsValue()}.
	 */
	@Test
	public void testContainsValue() {
		for ( int i=0; i<5;i++)
			assertTrue( mapfilled5.containsValue( i ) );
		
		assertFalse ( map1.containsValue( -1 ) );
		assertFalse ( mapfilled5.containsValue( 100 ) );
	}

	/**
	 * Tests that the put method correctly inserts the key-value pair
	 * into the map. Also tests that an insert of pair with an existing 
	 * key results in the value being updated in the map.
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

	/**
	 * Tests that the remove method correctly removes the given element
	 * from the map. Attempt to remove a key that doesn't exist in the map
	 * results in returning null.
	 * Test method for {@link util.RobinHoodHashMap#remove()}.
	 */
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

	/**
	 * Tests that the putAll method correctly inserts the key-value pairs
	 * from an existing map into this map. 
	 * Test method for {@link util.RobinHoodHashMap#put()}.
	 */
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
			assertEquals( mapfilled5.get(key), mapd.containsKey(key));
		}
	}

	/**
	 * Tests that the key set contains all the keys that have been
	 * inserted into the map. Also test the isEmpty and size methods
	 * of the key set.
	 * Test method for {@link util.RobinHoodHashMap#keySet()}.
	 */
	@Test
	public void testKeySet() {
		
		assertEquals ( 0, map1.keySet().size() );
		assertTrue ( map1.keySet().isEmpty() );
		
		Set<String> keySet = mapfilled5.keySet();
		assertFalse( keySet.isEmpty() );
		assertEquals ( mapfilled5.size(), keySet.size() );
		for ( int i=0; i<5;i++)
		{
			assertTrue ( keySet.contains(array[i]) );
		}
		
	}

	/**
	 * Tests that the key set contains all the keys that have been
	 * inserted into the map and can correctly be converted to 
	 * an an array.
	 * Test method for {@link util.RobinHoodHashMap#keySet()}.
	 */
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
	
	/**
	 * Tests that the key set doesn't allow for misuse of the 
	 * key set. Test that keys cannot be added to the key set
	 * without using the put method for the underlying map.
	 * Test method for {@link util.RobinHoodHashMap#keySet()}.
	 */
	@Test
	public void testKeySetBadUsage() {
		assertFalse ( mapd.keySet().add( "Bad" ) );
		assertFalse ( mapd.keySet().addAll( list ) );
	}
	
	
	/**
	 * Tests that the key set correctly allows for the set and the 
	 * underlying map to be cleared.
	 * Test method for {@link util.RobinHoodHashMap#keySet()}.
	 */
	@Test
	public void testKeySetClear() {
		
		mapd.keySet().clear();
		assertTrue ( mapd.isEmpty() );

		assertFalse ( mapfilled5.isEmpty() );
		mapfilled5.keySet().clear();
		assertTrue ( mapfilled5.isEmpty() );
		
	}
	
	/**
	 * Tests that the key set correctly allows for the removal of
	 * a key and that they key-value pair is removed for the underlying
	 * map.
	 * Test method for {@link util.RobinHoodHashMap#keySet()}.
	 */
	@Test
	public void testKeySetRemove() {
		
		for ( int i = 0; i < array.length; i++ )
			mapd.put ( array[i], i );
		
		int count = mapd.size();
		Set<String> keySet = mapd.keySet();
		for ( int i = 0; i < array.length; i++ ) {
			assertTrue (keySet.remove ( array[i] ) );
			assertEquals ( count-i-1, keySet.size() );
			assertEquals ( count-i-1, mapd.size() );
			assertFalse ( keySet.contains( array[i] ) );
			assertFalse ( mapd.containsKey( array[i] ) );
		}
		assertTrue ( keySet.isEmpty() );
		assertTrue ( mapd.isEmpty() );
	}
	
	/**
	 * Tests that the key set correctly allows for the removal of
	 * all keys from a collection of keys.
	 * Test method for {@link util.RobinHoodHashMap#keySet()}.
	 */
	@Test
	public void testKeySetRemoveAll() {
		
		for ( int i = 0; i < array.length; i++ )
			mapd.put ( array[i], i );
		
		Set<String> keySet = mapd.keySet();
		assertTrue ( keySet.removeAll( list ) );
		for ( int i = 0; i < array.length; i++ ) {
			assertFalse ( keySet.contains( array[i] ) );
			assertFalse ( mapd.containsKey( array[i] ) );
		}
		assertTrue ( keySet.isEmpty() );
		assertTrue ( mapd.isEmpty() );
	}
	
	/**
	 * Tests that the key set correctly allows for the removal of
	 * all keys not contained in a collection of keys.
	 * Test method for {@link util.RobinHoodHashMap#keySet()}.
	 */
	@Test
	public void testKeySetRetainAll() {
		
		for ( int i = 0; i < array.length; i++ )
			mapd.put ( array[i], i );
		
		Set<String> keySet = mapd.keySet();
		assertEquals ( mapd.size(), keySet.size() );
		assertTrue ( keySet.retainAll( list) );
		assertEquals ( mapd.size(), keySet.size() );
		assertEquals ( list.size(), keySet.size() );
		
		assertEquals ( mapd.size(), keySet.size() );
		assertTrue ( keySet.retainAll( halfList) );
		assertEquals ( halfList.size(), keySet.size() );
		assertTrue ( keySet.containsAll( halfList) );

		keySet = map1.keySet();
		assertEquals ( map1.size(), keySet.size() );
		assertTrue ( keySet.retainAll( list) );
		assertEquals ( map1.size(), keySet.size() );
		assertTrue(  keySet.isEmpty() );
	}
	
	/**
	 * Tests that the key set correctly returns true if it contains 
	 * all the keys in the given collection.
	 * Test method for {@link util.RobinHoodHashMap#keySet()}.
	 */
	@Test
	public void testKeySetContainsAll() {
		
		for ( int i = 0; i < array.length; i++ )
			mapd.put ( array[i], i );
		
		Set<String> keySet = mapd.keySet();
		
		assertTrue ( keySet.containsAll( halfList ) );
		
		assertEquals ( mapd.size(), keySet.size() );
		assertTrue ( keySet.containsAll( list ) );
		keySet.remove ( array[3] );
		assertFalse ( keySet.containsAll( list ) );
		
		keySet = map1.keySet();
		assertEquals ( map1.size(), keySet.size() );
		assertFalse ( keySet.containsAll( list ) );
	}
	
	
	/**
	 * Tests that the key set contains all the keys that have been
	 * inserted into the map and can correctly be converted to 
	 * an an array.
	 * Test method for {@link util.RobinHoodHashMap#keySet()}.
	 */
	@Test
	public void testKeySetToTArray() {
		Object[]  test = new String[ 10 ];
		test = mapfilled5.keySet().toArray(test);
		for ( int i=0; i<test.length && test[i] != null; i++ )
		{
			assertTrue ( mapfilled5.containsKey( test[i]) );
		}
	}
	
	/**
	 * Tests that the key set iterator correctly iterates over the
	 * keys in the underlying map.
	 * Test method for {@link util.RobinHoodHashMap#keySet()}.
	 */
	@Test
	public void testKeySetIterator() {
		int i =0;
		for ( String key: mapfilled5.keySet() )
		{
			assertTrue ( mapfilled5.containsKey(key) );
			i++;
		}
		assertEquals( mapfilled5.size(), i);
	}

	/**
	 * Tests that the value set contains all the values that have been
	 * inserted into the map.
	 * Test method for {@link util.RobinHoodHashMap#values()}.
	 */
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
