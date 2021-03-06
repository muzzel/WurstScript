
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

abstract class ParseqList<T> implements List<T> {
	
	private LinkedList<T> list = new LinkedList<T>();
	
	abstract protected void other_setParentToThis(T t);
	abstract protected void other_clearParent(T t);
	
	public boolean add(T t) {
		other_setParentToThis(t);
		return list.add(t);
	}

	@Override
	public void add(int index, T elem) {
		other_setParentToThis(elem);
		list.add(index, elem);		
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		for (T t: c) {
			other_setParentToThis(t);
		}
		return list.addAll(c);
	}

	@Override
	public boolean addAll(int pos, Collection<? extends T> c) {
		for (T t: c) {
			other_setParentToThis(t);
		}
		return list.addAll(pos, c);
	}

	@Override
	public void clear() {
		for (T t : list) {
			other_clearParent(t);
		}
		list.clear();
	}

	@Override
	public boolean contains(Object arg0) {
		return list.contains(arg0);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return list.containsAll(c);
	}

	@Override
	public T get(int index) {
		return list.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		if (list.remove(o)) {
			other_clearParent((T) o);
			return true;
		}
		return false;
	}

	@Override
	public T remove(int index) {
		T t = list.remove(index);
		other_clearParent(t);
		return t;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for (Object o : c) {
			changed = remove(o) || changed; // order important here
		}
		return true;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		ListIterator<T> li = list.listIterator();
		boolean changed = false;
		while (li.hasNext()) {
			T t = li.next();
			if (!c.contains(t)) {
				li.remove();
				other_clearParent(t);
				changed  = true;
			}
		}
		return changed;
	}

	@Override
	public T set(int index, T element) {
		other_setParentToThis(element);
		T t = list.set(index, element);		
		other_clearParent(t);
		return t;
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		return list.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <S> S[] toArray(S[] a) {
		return list.toArray(a);
	}
	
	
	
}
