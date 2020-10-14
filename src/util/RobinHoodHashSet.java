package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Implements an set collection (no duplicates) using the robinhood hashing technique.
 * Elements in the set are ordered in an array by hashed value for quick lookup.
 * Hash conflicts result in elements occupying subsequent positions in the array.
 * 
 * @author Ken Loomis (https://github.com/kjloomis3)
 *
 * @param <E> The element type for the set.
 */
public class RobinHoodHashSet<E> implements Set<E>, Iterable<E> {

	/** The default size of the array is 20 unless overridden by the constructor. **/
	private static final int DEFAULT_CAPACITY = 20;
	
	/** The underlying structure of this HashSet (Each slot should be instantiated before use) **/
	private ArrayList<Slot> array;
	
	/** The number of elements in the set **/
	private int size = 0;
	
	/**
	 * Instantiates the RobinHoodHashSet with a small number of storage locations
	 * by default. Should only be used for testing purposes or when only a small set is needed.
	 */
	public RobinHoodHashSet( ) 
	{
		this.array = new ArrayList<Slot>(DEFAULT_CAPACITY);
		for ( int i=0; i<DEFAULT_CAPACITY; i++)
			array.add( new Slot());
		this.size = 0;
	}
	
	/**
	 * Instantiates the RobinHoodHashSet with the given capacity.
	 * @param capacity: the maximum capacity of the set.
	 */
	public RobinHoodHashSet( int capacity  ) 
	{
		if ( capacity <= 0 ) throw new IllegalArgumentException("Invalid size for set");
		this.array = new ArrayList<Slot>(capacity);
		for ( int i=0; i<capacity; i++)
			array.add( new Slot());
		this.size = 0;
	}
	
	/**
	 * Produces the hash value of the element to be added 
	 * @param element: the element to be added
	 * @return the hashed value: int
	 */
	private int hash(Object object)
	{
		return Math.abs(object.hashCode()) % array.size();
	}
	
	/**
	 * Produces a new index value allowing for the "wrap-around" of the
	 * underlying ArrayList.
	 * @param i
	 * @param offset
	 * @return
	 */
	private int getIndex ( int i, int offset )
	{
		return (i+offset) % array.size();
	}
	
	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size == 0;
	}
	
	/**
	 * Returns true if this set is at capacity and cannot accept any new
	 * elements.
	 * @return true if the set is full: boolean
	 */
	public boolean isFull() {
		return size == array.size();
	}

	@Override
	public boolean contains(Object object) 
	{
		if ( size == 0 || object == null ) return false;
		int idx = hash(object);
		int last = 0;
		for ( int offset=0; offset<array.size(); offset++)
		{
			int  i = getIndex(idx, offset);
			if ( !array.get (i).empty && array.get(i).data.equals(object))
				return true;
			if ( !array.get(i).empty && array.get(i).distance < last )
				return false;
		}
		return false;
	}

	@Override
	public Iterator<E> iterator() {
		return new SetIterator();
	}

	@Override
	public Object[] toArray() {
		Object[] objects = new Object[size];
		int i=0;
		for ( E element: this )
		{
			objects[i++] = element;
		}
		return objects;
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) 
	{
		if ( a.length < array.size() )
		{
			a = (T[]) new Object[array.size()];
		}
		int i=0;
		for ( E element: this )
		{
			a[i++] = (T) element;
		}
		return a;
	}

	@Override
	public boolean add(E element) {
		if ( size == array.size() || element == null )
		{
			return false;
		}

		int idx = hash(element);
		for ( int offset=0; offset<array.size(); offset++)
		{
			int  i = getIndex(idx, offset);
			if ( array.get(i).empty )
			{
				array.get(i).data = element;
				array.get(i).distance = offset;
				array.get(i).empty = false;
				size++;
				return true;
			}
			else if ( array.get(i).data == element )
			{
				return false;
			}
		}

		return false;
	}

	@Override
	public boolean remove(Object object) 
	{
		if ( size == 0 || object == null ) return false;
		
		int idx = hash(object);
		int last = 0;
		for ( int offset=0; offset<array.size(); offset++)
		{
			int  i = getIndex(idx, offset);
			if ( !array.get (i).empty && array.get(i).data.equals(object))
			{
				int j = getIndex(idx, offset+1);
				while(!array.get(j).empty && array.get(j).distance > 0)
				{
					array.get(i).data = array.get(j).data;
					array.get(i).distance = array.get(j).distance -1;
					i=j;
					j=getIndex(i, 1);
				}
				array.get(i).data = null;
				array.get(i).empty = true;
				size--;
				return true;
				
			}
			if ( !array.get(i).empty && array.get(i).distance < last )
				return false;
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		if ( c == null ) return true;
		for ( Object object: c)
		{
			if ( !this.contains( object ) )
				return false;
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends E> c) 
	{
		if ( c == null ) return false;
		for ( E element: c)
		{
			this.add( element );
		}
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		ArrayList<E> removeList = new ArrayList<E>(size);
		for ( E element: this )
		{
			if ( !c.contains(element) )
				removeList.add(element);
		}
		return removeAll(removeList);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		if ( c == null ) return true;
		for ( Object object: c)
		{
			this.remove( object );
		}
		return true;
	}

	@Override
	public void clear() 
	{
		int capacity = array.size();
		this.array = new ArrayList<Slot>(capacity);
		for ( int i=0; i<capacity; i++)
			array.add( new Slot());
		this.size = 0;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder s = new StringBuilder("RobinHoodHashSet:[" );
		int i=0;
		for (E element: this)
		{
			if ( i > 0 )
			{
				s.append( ", " );
			}
			i++;
			s.append( element.toString() );
		}
		s.append( "]" );
		return  s.toString();
	}

	/**
	 * Represents a storage location in the array of the RobinHoodHashSet.
	 * (Since this is a private class, the fields are set to public allowing for direct
	 * access of the data.)
	 * @param The element in the set
	 */
	private class Slot
	{
		/** The element in the slot **/
		public E data;
		/** The offset distance from the hashed slot **/ 
		public int distance;
		/** A flag indicating the slot is empty even if it contain data **/
		public boolean empty;
		
		/**
		 * Instantiates a slot; sets all fields to default values. Should be
		 * performed to create the slot array within the RobinHoodHashSet
		 */
		public Slot() 
		{ 
			this.data = null; 
			this.distance = 0;  
			this.empty = true; 
		}
		
		@Override
		public String toString() {
			return data.toString() + "[" + distance + "]";
		}

	}
	
	public class SetIterator implements Iterator<E>
	{

		private int index;
		
		/**
		 *  Creates an iterator for a RobinHoodHashSet
		 */
		SetIterator(  )
		{
			for (index=0; index<array.size() && array.get(index).empty; index++);
		}
		
		@Override
		public boolean hasNext() {
			return index < array.size() && array.get(index).data != null;
		}

		@Override
		public E next() {
			if ( index < 0 || index>= array.size())
				throw new IndexOutOfBoundsException();
			E element = array.get(index).data;
			for (index++; index<array.size() && array.get(index).empty; index++);
			return element;
		}
	
	}


}
