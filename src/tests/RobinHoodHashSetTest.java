package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
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
	private RobinHoodHashSet<String> setfilled5;
	
	private static final String [] array = { "AB", "AA", "BA", "CA", "LM", 
			 												  "MN", "NM", "KJ", "PO", "RL",
			 												  "RN", "EM", "ZA", "TO", "WL",
															  "OL", "XY", "ZY", "YZ",  "ZZ"};
	
	private static ArrayList<String> list;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		set1 = new RobinHoodHashSet<String> ( 1 );
		set5 = new RobinHoodHashSet<String> ( 5 );
		setd = new RobinHoodHashSet<String> (  );
		setfilled5 = new RobinHoodHashSet<String> (  );
		
		list = new ArrayList<String>(array.length);
		
		if ( list.size() == 0) {
			for ( int i=0; i<array.length;i++)
				list.add(array[i]);
		}
		
		if ( setfilled5.size() < 5 )
		{
			for ( int i=0; i<5;i++)
				setfilled5.add(array[i]);
		}
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
		
		int i=1;
		for ( String s: list )
		{
				assertTrue( setd.add( s ) );
				assertEquals(i++, setd.size() );
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
		assertTrue( setd.add( list.get(0)) );
		assertFalse( setd.isEmpty() );
		
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#isFull()}.
	 */
	@Test
	public void testIsFull() {
		assertFalse( set1.isFull() );
		assertFalse( set5.isFull() );
		assertFalse( setd.isFull() );
		assertTrue( set1.add( list.get(0)) );
		assertTrue( set1.isFull() );
		
	}
	
	/**
	 * Test method for {@link util.RobinHoodHashSet#contains(java.lang.Object)}.
	 */
	@Test
	public void testContains() {
		assertFalse( setd.contains("Not there") );
		for ( int i=0; i<list.size(); i++)
		{
				if  ( setd.add( list .get(i) ))
				{
					assertTrue( setd.contains( list .get(i)  ));
					for ( int j=0; j<i; j++)
						assertTrue( setd.contains( list .get(j) ));
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
		int i=0;
		for  (String s: setd )
		{
			i++;
			assertTrue(setd.contains(s));
		}
		assertEquals(setd.size(), i);
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#toArray()}.
	 */
	@Test
	public void testToArray() {
		assertTrue(  setd.addAll(list) );
		String[] test = new String[setd.size()];
		int i=0;
		for ( String s: setd)
		{
			test[i++]=s;
		}
		Object[] actual = setd.toArray();
		for (i=0;i<actual.length;i++)
		{
			assertFalse( actual[i] == null );
			assertEquals( test [ i ], actual [ i ] );
			assertTrue( setd.contains( actual[i].toString() ) );
		}
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#toArray(T[])}.
	 */
	@Test
	public void testToArrayTArray() {
		assertTrue(  setd.addAll(list) );
		String[] test = new String[setd.size()];
		int i=0;
		for ( String s: setd)
		{
			test[i++]=s;
		}
		Object[] actual = new Object[5];
		actual = setd.toArray(actual);
		for (i=0;i<actual.length;i++)
		{
			assertFalse( actual[i] == null );
			assertEquals( test [ i ], actual [ i ] );
			assertTrue( setd.contains( actual[i].toString() ) );
		}
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#add(java.lang.Object)}.
	 */
	@Test
	public void testAdd() {
		assertFalse( setd.add(null ) );
		for ( int i=0; i<list.size(); i++)
		{
			if ( i<1)
			{
				assertTrue( set1.add( list.get(i) ) );
				assertTrue( set5.add( list.get(i) ) );
				assertTrue( setd.add( list.get(i) ) );
			}
			else if (i<5)
			{
				assertFalse( set1.add( list.get(i) ) );
				assertTrue( set5.add( list.get(i) ) );
				assertTrue( setd.add( list.get(i) ) );				
			}
			else
			{
				assertFalse( set1.add( list.get(i) ) );
				assertFalse( set5.add( list.get(i) ) );
				assertTrue( setd.add( list.get(i) ) );	
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
		for (String s: list)
				setd.add( s);
		assertFalse( setd.remove("Not there") );
		for (String s: list)
			setd.remove( s);
		assertFalse( setd.remove(null) );
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#containsAll(java.util.Collection)}.
	 */
	@Test
	public void testContainsAll() {
		
		for ( String s: list)
		{
			assertFalse(setd.containsAll(list));
			setd.add( s );
		}
		assertTrue( setd.containsAll(list) );
		assertTrue( setd.containsAll(setfilled5) );
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#addAll(java.util.Collection)}.
	 */
	@Test
	public void testAddAll() {
		assertFalse( setd.containsAll(list) );
		assertTrue(  setd.addAll(list) );
		assertTrue( setd.containsAll(list) );
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#retainAll(java.util.Collection)}.
	 */
	@Test
	public void testRetainAll() {
		assertTrue(  setd.addAll(list) );
		assertTrue ( setd.retainAll(list) );
		assertTrue( setd.containsAll(list) );
		assertTrue ( setd.retainAll(setfilled5));
		assertTrue( setd.containsAll(setfilled5) );
		assertEquals( 5, setd.size() );
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#removeAll(java.util.Collection)}.
	 */
	@Test
	public void testRemoveAll() {
		assertTrue( setd.addAll(list) );
		assertTrue( setd.containsAll(list) );
		assertTrue ( setd.removeAll(list) );
		assertTrue ( setd.isEmpty() );
		assertTrue( setd.addAll(list) );
		assertTrue ( setd.removeAll(setfilled5) );
		assertEquals ( 15, setd.size() );
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#clear()}.
	 */
	@Test
	public void testClear() {
		
		assertTrue(  setd.addAll(list) );
		assertEquals( list.size(), setd.size() );
		setd.clear();
		assertEquals(0, setd.size() );
	}

	/**
	 * Test method for {@link util.RobinHoodHashSet#toString()}.
	 */
	@Test
	public void testToString() {
		assertEquals ( "RobinHoodHashSet:[]", set1.toString() );
		for ( int i=0; i<list.size(); i++)
		{
			assertTrue( setd.add( list.get(i) ) );
			assertTrue( setd.toString().contains( list.get(i) ));
		}
	}

}
