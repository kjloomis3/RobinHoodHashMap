package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import util.RobinHoodHashSet;

/**
 * Performs a series of tests on the RobinHoodHashSet.
 * 
 * @author Ken Loomis (https://github.com/kjloomis3)
 */
public class RobinHoodHashSetTest {

	private RobinHoodHashSet<String> set1;
	private RobinHoodHashSet<String> set5;
	private RobinHoodHashSet<String> setd;
	
	private static final String [] list = { "AB", "AA", "BA", "CA", "LM", 
			 												  "MN", "NM", "KJ", "PO", "RL",
			 												  "RN", "EM", "ZA", "TO", "WL",
															  "OL", "XY", "ZY", "YZ",  "ZZ"};
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		set1 = new RobinHoodHashSet<String> ( 1 );
		set5 = new RobinHoodHashSet<String> ( 5 );
		setd = new RobinHoodHashSet<String> (  );
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#RobinHoodHashSet()}.
	 */
	@Test
	public void testRobinHoodHashSet() 
	{
		assertNotNull (setd);
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#RobinHoodHashSet(int)}.
	 */
	@Test
	public void testRobinHoodHashSetInt() {
		assertNotNull (set1);
		assertNotNull (set5);
		
		RobinHoodHashSet<String> setBad;
		try {
			setBad = new RobinHoodHashSet<String>(-1);
			fail ("Cannot instantiate set with negative size.");
		} catch (IllegalArgumentException e) {
			assertEquals("Invalid size for set", e.getMessage());
		}
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#size()}.
	 */
	@Test
	public void testSize() {
		assertEquals(0, set1.size());
		assertEquals(0, set5.size());
		assertEquals(0, setd.size());
		
		for ( int i=0; i<list.length; i++)
		{
				assertTrue( setd.add( list[ i ] ) );
				assertEquals(i+1, setd.size() );
		}
		
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#isEmpty()}.
	 */
	@Test
	public void testIsEmpty() {
		assertTrue( set1.isEmpty() );
		assertTrue( set5.isEmpty() );
		assertTrue( setd.isEmpty() );
		assertTrue( setd.add( list[ 0 ] ) );
		assertFalse( setd.isEmpty() );
		
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#contains(java.lang.Object)}.
	 */
	@Test
	public void testContains() {
		assertFalse( setd.contains("Not there") );
		//for ( int i=0; i<list.length; i++)
		for ( int i=0; i<list.length; i++)
		{
				if  ( setd.add( list [ i ] ) )
				{
					assertTrue( setd.contains( list [ i ] ) );
					for ( int j=0; j<i; j++)
						assertTrue( setd.contains( list [ j ] ) );
				}
				
		}
		assertFalse( setd.contains("Not there") );
		assertFalse( setd.contains(null) );
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#iterator()}.
	 */
	@Test
	public void testIterator() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#toArray()}.
	 */
	@Test
	public void testToArray() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#toArray(T[])}.
	 */
	@Test
	public void testToArrayTArray() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#add(java.lang.Object)}.
	 */
	@Test
	public void testAdd() {
		assertFalse( setd.add(null ) );
		for ( int i=0; i<list.length; i++)
		{
			if ( i<1)
			{
				assertTrue( set1.add( list[ i ] ) );
				assertTrue( set5.add( list[ i ] ) );
				assertTrue( setd.add( list[ i ] ) );
			}
			else if (i<5)
			{
				assertFalse( set1.add( list[ i ] ) );
				assertTrue( set5.add( list[ i ] ) );
				assertTrue( setd.add( list[ i ] ) );				
			}
			else
			{
				assertFalse( set1.add( list[ i ] ) );
				assertFalse( set5.add( list[ i ] ) );
				assertTrue( setd.add( list[ i ] ) );	
			}
		}
		assertFalse( setd.add("Too Many") );
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#remove(java.lang.Object)}.
	 */
	@Test
	public void testRemove() 
	{
		assertFalse( setd.remove("Not there") );
		for ( int i=0; i<list.length; i++)
				setd.add( list [ i ] );
		assertFalse( setd.remove("Not there") );
		for ( int i=0; i<list.length; i++)
			setd.remove( list [ i ] );
		assertFalse( setd.remove(null) );
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#containsAll(java.util.Collection)}.
	 */
	@Test
	public void testContainsAll() {
		
		ArrayList<String> collection = new ArrayList<String>();
		Collections.addAll( collection, list );
		
		for ( int i=0; i<list.length; i++)
		{
			assertFalse(setd.containsAll(collection));
			setd.add( list [ i ] );
		}
		assertTrue( setd.containsAll(collection) );
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#addAll(java.util.Collection)}.
	 */
	@Test
	public void testAddAll() {
		ArrayList<String> collection = new ArrayList<String>();
		Collections.addAll( collection, list );
		
		assertTrue(  setd.addAll(collection) );
		assertTrue( setd.containsAll(collection) );
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#retainAll(java.util.Collection)}.
	 */
	@Test
	public void testRetainAll() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#removeAll(java.util.Collection)}.
	 */
	@Test
	public void testRemoveAll() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#clear()}.
	 */
	@Test
	public void testClear() {
		ArrayList<String> collection = new ArrayList<String>();
		Collections.addAll( collection, list );
		
		assertTrue(  setd.addAll(collection) );
		assertEquals( list.length, setd.size() );
		setd.clear();
		assertEquals(0, setd.size() );
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#toString()}.
	 */
	@Test
	public void testToString() {
		assertEquals ( "RobinHoodHash:[--]", set1.toString() );
		for ( int i=0; i<list.length; i++)
		{
			assertTrue( setd.add( list[ i ] ) );
			assertTrue( setd.toString().contains( list[i] ));
		}
	}

}
