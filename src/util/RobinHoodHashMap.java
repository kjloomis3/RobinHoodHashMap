package util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Implements a map collection (no duplicate keys) using the robinhood hashing technique.
 * Elements in the set are ordered in an array by hashed key for quick lookup.
 * Hash conflicts result in elements occupying subsequent positions in the array.
 * 
 * @author Ken Loomis (https://github.com/kjloomis3)
 *
 * @param <E> The element type for the set.
 */
public class RobinHoodHashMap<K, E> implements Map<K, E> {

	/** The default size of the array is 20 unless overridden by the constructor. **/
	private static final int DEFAULT_CAPACITY = 20;

	/** The underlying structure of this HashSet (Each slot should be instantiated before use) **/
	private ArrayList<RobinHoodEntry> array;

	/** The number of elements in the set **/
	private int size = 0;

	/** The internal representation of the key set for this map **/
	private RobinHoodKeySet keySet;

	/** The internal representation of the value set for this map **/
	private RobinHoodValueSet valueSet;

	/** The internal representation of the entry set for this map **/
	private RobinHoodEntrySet entrySet;

	/**
	 * Instantiates the RobinHoodHashMap with a small number of storage locations
	 * by default. Should only be used for testing purposes or when only a small map is needed.
	 */
	public RobinHoodHashMap( ) 
	{
		this( DEFAULT_CAPACITY );
	}

	/**
	 * Instantiates the RobinHoodHashMap with the given capacity.
	 * @param capacity: the maximum capacity of the map.
	 */
	public RobinHoodHashMap( int capacity  ) 
	{
		if ( capacity <= 0 ) throw new IllegalArgumentException("Invalid size for set");
		this.array = new ArrayList<RobinHoodEntry>(capacity);
		for ( int i=0; i<capacity; i++)
			array.add( new RobinHoodEntry());
		this.size = 0;
		this.keySet = new RobinHoodKeySet();
		this.valueSet = new RobinHoodValueSet();
		this.entrySet = new RobinHoodEntrySet();
	}

	/**
	 * Produces the hash value of the key to be added 
	 * @param element: the key to be added
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
	 * Returns true if this map is at capacity and cannot accept any new
	 * elements.
	 * @return true if the map is full: boolean
	 */
	public boolean isFull() {
		return size == array.size();
	}


	@Override
	public E get(Object key) {
		if ( size == 0 || key == null ) return null;
		int idx = hash(key);
		int last = 0;
		for ( int offset=0; offset<array.size(); offset++)
		{
			int  i = getIndex(idx, offset);
			if ( !array.get (i).isEmpty() && array.get(i).getKey().equals(key))
				return array.get(i).getValue();
			if ( !array.get(i).isEmpty() && array.get(i).getDistance() < last )
				return null;
		}
		return null;
	}

	@Override
	public boolean containsKey(Object key) {
		if ( size == 0 || key == null ) return false;
		int idx = hash(key);
		int last = 0;
		for ( int offset=0; offset<array.size(); offset++)
		{
			int  i = getIndex(idx, offset);
			if ( !array.get (i).isEmpty() && array.get(i).getKey().equals(key))
				return true;
			if ( !array.get(i).isEmpty() && array.get(i).getDistance() < last )
				return false;
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		if ( size == 0 || value == null ) return false;
		int idx = hash(value);
		int last = 0;
		for ( int offset=0; offset<array.size(); offset++)
		{
			int  i = getIndex(idx, offset);
			if ( !array.get (i).isEmpty() && array.get(i).getValue().equals(value))
				return true;
			if ( !array.get(i).isEmpty() && array.get(i).getDistance() < last )
				return false;
		}
		return false;
	}
	@Override
	public E put(K key, E value) {
		if (key == null || value == null )
		{
			return null;
		}

		int idx = hash(key);
		for ( int offset=0; offset<array.size(); offset++)
		{
			int  i = getIndex(idx, offset);
			if ( array.get(i).isEmpty() )
			{
				array.get(i).setKey(key);
				array.get(i).setValue(value);
				array.get(i).setDistance(offset);
				size++;
				return value;
			}
			else if ( array.get(i).getKey().equals( key ))
			{
				array.get(i).setValue(value);
				array.get(i).setDistance(offset);
				return value;
			}
		}
		return null;
	}

	@Override
	public E remove(Object key) {
		if ( size == 0 || key == null ) return null;
		E value = null;
		int idx = hash(key);
		int last = 0;
		for ( int offset=0; offset<array.size(); offset++)
		{
			int  i = getIndex(idx, offset);
			if ( !array.get (i).isEmpty() && array.get(i).getKey().equals(key))
			{
				value =  array.get(i).getValue();
				int j = getIndex(idx, offset+1);
				while(!array.get(j).isEmpty() && array.get(j).getDistance() > 0)
				{
					array.get(i).setKey(array.get(j).getKey());
					array.get(i).setValue(array.get(j).getValue());
					array.get(i).setDistance(array.get(j).getDistance() -1);
					i=j;
					j=getIndex(i, 1);
				}
				array.get(i).setEmpty();
				size--;
				return value;

			}
			if ( !array.get(i).isEmpty() && array.get(i).getDistance() < last )
				return null;
		}
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends E> m) 
	{
		if ( m == null ) return;
		for ( K key: m.keySet() )
		{
			this.put( key, m.get(key) );
		}
		return;
	}

	@Override
	public Set<K> keySet() {
		return keySet;
	}

	@Override
	public Collection<E> values() {
		return valueSet;
	}

	@Override
	public Set<Entry<K, E>> entrySet() {
		return entrySet;
	}

	@Override
	public void clear() 
	{
		int capacity = array.size();
		this.array = new ArrayList<RobinHoodEntry>(capacity);
		for ( int i=0; i<capacity; i++)
			array.add( new RobinHoodEntry());
		this.size = 0;
	}

	@Override
	public String toString() 
	{
		StringBuilder s = new StringBuilder("RobinHoodHashMap:[" );
		int i=0;
		for (E element: this.values())
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
	public class RobinHoodEntry implements Entry<K, E>
	{
		/** The key to the slot **/
		private K key;
		/** The element in the slot **/
		private E value;
		/** The offset distance from the hashed slot **/ 
		private int distance;
		/** A flag indicating the slot is empty even if it contain data **/
		private boolean empty;

		/**
		 * Instantiates a slot; sets all fields to default values. Should be
		 * performed to create the slot array within the RobinHoodHashSet
		 */
		public RobinHoodEntry() 
		{ 
			this.key = null;
			this.value = null; 
			this.distance = 0;  
			this.empty = true; 
		}

		@Override
		public String toString() {
			return value.toString() + "[" + distance + "]";
		}

		@Override
		public K getKey() {
			return key;
		}

		/**
		 * @param key the key to set
		 */
		protected void setKey(K key) {
			if ( key == null )
			{
				throw new IllegalArgumentException();
			}
			this.key = key;
		}
		
		@Override
		public E getValue() {
			return value;
		}

		@Override
		public E setValue(E value) {
			if ( value == null )
			{
				throw new IllegalArgumentException();
			}
			this.value = value;
			this.empty = false;
			return this.value;
		}

		/**
		 * @return the distance
		 */
		protected  int getDistance() {
			return distance;
		}

		/**
		 * @param distance the distance to set
		 */
		protected  void setDistance(int distance) {
			this.distance = distance;
		}

		/**
		 * @return the empty
		 */
		protected boolean isEmpty() {
			return empty;
		}

		/**
		 * @param empty the empty to set
		 */
		protected void setEmpty() {
			this.key = null;
			this.value = null; 
			this.distance = 0;  
			this.empty = true; 
		}

	}

	public class RobinHoodKeySet implements Set<K>
	{

		@Override
		public int size() {
			return RobinHoodHashMap.this.size;
		}

		@Override
		public boolean isEmpty() {
			return RobinHoodHashMap.this.isEmpty();
		}

		@Override
		public boolean contains(Object key) 
		{
			return RobinHoodHashMap.this.containsKey(key);
		}

		@Override
		public Iterator<K> iterator() {
			return new KeySetIterator();
		}

		@Override
		public Object[] toArray() {
			Object [] keys = new Object [size];
			int i=0;
			for ( K key : this )
				keys[i++] = key;
			return keys;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			if ( a.length < array.size() )
			{
				a = (T[]) new Object[array.size()];
			}
			int i=0;
			for ( K key : this )
				a[i++] = (T) key;
			a[i] = null;
			return a;
		}

		/**
		 * New keys cannot be added to this key set. To add a key, it must be 
		 * added but putting the key and value pair into the associated 
		 * RobinHoodHashMap.
		 * @return false
		 */
		@Override
		public boolean add(K k) {
			return false;
		}

		@Override
		public boolean remove(Object key) {
			return RobinHoodHashMap.this.remove(key) != null;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			for ( Object key: c )
			{
				if ( !RobinHoodHashMap.this.containsKey(key) )
					return false;
			}
			return true;
		}

		/**
		 * New keys cannot be added to this key set. To add a key, it must be 
		 * added but putting the key and value pair into the associated 
		 * RobinHoodHashMap.
		 * @return false
		 */
		@Override
		public boolean addAll(Collection<? extends K> c) {
			return false;
		}

		@Override
		public boolean retainAll(Collection<?> c) {
			ArrayList<Object> removeList = new ArrayList<Object>(this.size()); 
			for ( Object key: this )
				if ( !c.contains( key ) )
					removeList.add( key );
			
			for ( Object key: removeList )
				RobinHoodHashMap.this.remove(key);
			
			return true;
		}

		@Override
		public boolean removeAll(Collection<?> c) {
			for ( Object key: c )
			{
				RobinHoodHashMap.this.remove(key);
			}
			return true;
		}

		@Override
		public void clear() {
			RobinHoodHashMap.this.clear( );

		}

		public class KeySetIterator implements Iterator<K>
		{

			private int index;

			/**
			 *  Creates an iterator for a RobinHoodHashMap
			 */
			KeySetIterator(  )
			{
				for (index=0; index<array.size() && array.get(index).empty; index++);
			}

			@Override
			public boolean hasNext() {
				return index < array.size() && !array.get(index).isEmpty(); 
			}

			@Override
			public K next() {
				if ( index < 0 || index>= array.size())
					throw new IndexOutOfBoundsException();
				K key = (K) array.get(index).key;
				for (index++; index<array.size() && array.get(index).isEmpty(); index++);
				return key;
			}

		}

	}

	public class RobinHoodValueSet implements Set<E>
	{

		@Override
		public int size() {
			return RobinHoodHashMap.this.size;
		}

		@Override
		public boolean isEmpty() {
			return RobinHoodHashMap.this.size == 0;
		}

		@Override
		public boolean contains(Object value) {
			return RobinHoodHashMap.this.containsValue(value);
		}

		@Override
		public Iterator<E> iterator() {
			return new ValueSetIterator();
		}

		@Override
		public Object[] toArray() {
			Object [] values = new Object [size];
			int i=0;
			for ( E value : this )
				values[i++] = value;
			return values;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			if ( a.length < array.size() )
			{
				a = (T[]) new Object[array.size()];
			}
			int i=0;
			for ( E value : this )
				a[i++] = (T) value;
			return a;
		}

		/**
		 * New values cannot be added to this value set. To add a value, it must be 
		 * added but putting the key and value pair into the associated 
		 * RobinHoodHashMap.
		 * @return false
		 */
		@Override
		public boolean add(E e) {
			return false;
		}

		/**
		 * Values cannot be removed from this value set directly. To remove a value, it must be 
		 * the associated key to the value must be removed from RobinHoodHashMap or
		 * the key removed from the key set.
		 * @return false
		 */
		@Override
		public boolean remove(Object o) {
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			for ( Object value: c )
			{
				if ( !RobinHoodHashMap.this.containsKey(value) )
					return false;
			}
			return true;
		}

		/**
		 * New values cannot be added to this value set. To add a value, it must be 
		 * added but putting the key and value pair into the associated 
		 * RobinHoodHashMap.
		 * @return false
		 */
		@Override
		public boolean addAll(Collection<? extends E> c) {
			return false;
		}

		/**
		 * Values cannot be removed from this value set directly. To remove a value, it must be 
		 * the associated key to the value must be removed from RobinHoodHashMap or
		 * the key removed from the key set.
		 * @return false
		 */
		@Override
		public boolean retainAll(Collection<?> c) {
			return false;
		}

		/**
		 * Values cannot be removed from this value set directly. To remove a value, it must be 
		 * the associated key to the value must be removed from RobinHoodHashMap or
		 * the key removed from the key set.
		 * @return false
		 */
		@Override
		public boolean removeAll(Collection<?> c) {
			return false;
		}

		/**
		 * Values cannot be removed from this value set directly. To remove a value, it must be 
		 * the associated key to the value must be removed from RobinHoodHashMap or
		 * the key removed from the key set.
		 * @return false
		 */
		@Override
		public void clear() { }

		public class ValueSetIterator implements Iterator<E>
		{

			private int index;

			/**
			 *  Creates an iterator for a RobinHoodHashMap
			 */
			ValueSetIterator(  )
			{
				for (index=0; index<array.size() && array.get(index).empty; index++);
			}

			@Override
			public boolean hasNext() {
				return index < array.size() && !array.get(index).isEmpty(); 
			}

			@Override
			public E next() {
				if ( index < 0 || index>= array.size())
					throw new IndexOutOfBoundsException();
				E value = (E) array.get(index).getValue();
				for (index++; index<array.size() && array.get(index).isEmpty(); index++);
				return value;
			}

		}

	}

	public class RobinHoodEntrySet implements Set<Entry<K, E>>
	{

		@Override
		public int size() {
			return RobinHoodHashMap.this.size();
		}

		@Override
		public boolean isEmpty() {
			return RobinHoodHashMap.this.isEmpty();
		}

		@Override
		public boolean contains(Object object) {
			return RobinHoodHashMap.this.containsValue(object);
		}

		@Override
		public Iterator<Entry<K, E>> iterator() {
			return new EntrySetIterator();
		}

		@Override
		public Object[] toArray() {
			Object [] entries = new Object [size];
			int i=0;
			for ( Entry<K,E>entry : this )
				entries[i++] = entry;
			return entries;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T[] toArray(T[] a) {
			if ( a.length < array.size() )
			{
				a = (T[]) new Object[array.size()];
			}
			int i=0;
			for ( Entry<K,E>entry : this )
				a[i++] = (T) entry;
			return a;
		}

		/**
		 * New entries cannot be added to this entry set. To add an entry, it must be 
		 * added but putting the key and value pair into the associated 
		 * RobinHoodHashMap.
		 * @return false
		 */
		@Override
		public boolean add(Entry<K, E> e) {
			return false;
		}

		/**
		 * Entries cannot be removed from this entry set. To remove an entry, it must be 
		 * removed using the key from the associated RobinHoodHashMap. 
		 * @return false
		 */
		@Override
		public boolean remove(Object o) {
			return false;
		}

		@Override
		public boolean containsAll(Collection<?> c) {
			for ( Object value: c )
			{
				if ( !RobinHoodHashMap.this.containsValue(value) )
					return false;
			}
			return true;
		}

		/**
		 * New entries cannot be added to this entry set. To add an entry, it must be 
		 * added but putting the key and value pair into the associated 
		 * RobinHoodHashMap.
		 * @return false
		 */
		@Override
		public boolean addAll(Collection<? extends Entry<K, E>> c) {
			return false;
		}

		/**
		 * Entries cannot be removed from this entry set. To remove an entry, it must be 
		 * removed using the key from the associated RobinHoodHashMap. 
		 * @return false
		 */
		@Override
		public boolean retainAll(Collection<?> c) {
			return false;
		}

		/**
		 * Entries cannot be removed from this entry set. To remove an entry, it must be 
		 * removed using the key from the associated RobinHoodHashMap. 
		 * @return false
		 */
		@Override
		public boolean removeAll(Collection<?> c) {
			return false;
		}

		/**
		 * Entries cannot be removed from this entry set. To clear this entry set, it must be 
		 * the associated RobinHoodHashMap must be cleared instead.. 
		 * @return false
		 */
		@Override
		public void clear() {
			return;
		}
		
		public class EntrySetIterator implements Iterator<Entry<K,E>>
		{

			private int index;

			/**
			 *  Creates an iterator for a RobinHoodHashMap
			 */
			EntrySetIterator(  )
			{
				for (index=0; index<array.size() && array.get(index).empty; index++);
			}

			@Override
			public boolean hasNext() {
				return index < array.size() && !array.get(index).isEmpty(); 
			}

			@Override
			public Entry<K,E> next() {
				if ( index < 0 || index>= array.size())
					throw new IndexOutOfBoundsException();
				Entry<K,E> entry = array.get(index);
				for (index++; index<array.size() && array.get(index).isEmpty(); index++);
				return entry;
			}

		}


	}


}
