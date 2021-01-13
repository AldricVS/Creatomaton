package process.util;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * This utility class is useful when going through a list in reverse order is needed.
 * 
 * @author Aldric Vitali Silvestre <aldric.vitali@outlook.fr>
 * @param <T> the type of the list (generic)
 */
public class ReversedList<T> implements Iterable<T> {
	private final List<T> original;

	private ReversedList(List<T> original) {
		this.original = original;
	}

	public Iterator<T> iterator() {
		final ListIterator<T> i = original.listIterator(original.size());

		return new Iterator<T>() {
			public boolean hasNext() {
				return i.hasPrevious();
			}

			public T next() {
				return i.previous();
			}

			public void remove() {
				i.remove();
			}
		};
	}
	
	/**
	 * Create a reverted list that can be useful when triying to iterate with the Iterator pattern
	 */
	public static <T> ReversedList<T> revertList(List<T> original) {
        return new ReversedList<T>(original);
    }
}
