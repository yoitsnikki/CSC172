/*
 * Niharika Agrawal
 * CSC 172
 * URArrayList from scratch
 */


import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class URArrayList<E> implements URList<E> {

    private E[] array;
    private int size;

    @SuppressWarnings("unchecked")
    public URArrayList() {
        array = (E[]) new Object[10]; // Initial capacity
        size = 0;
    }

    @SuppressWarnings("unchecked")
    public URArrayList(Collection<? extends E> c) {
        array = (E[]) new Object[c.size()];
        size = 0;
        for (E element : c) {
            add(element);
        }
    }

    private void ensureCapacity(int newCapacity) {
        if (newCapacity > array.length) {
            int newLength = Math.max(newCapacity, array.length * 2);
            @SuppressWarnings("unchecked")
            E[] newArray = (E[]) new Object[newLength];
            System.arraycopy(array, 0, newArray, 0, size);
            array = newArray;
        }
    }

    @Override
    public boolean add(E e) {
        ensureCapacity(size + 1);
        array[size++] = e;
        return true;
    }

    @Override
    public void add(int index, E element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        ensureCapacity(size + 1);
        System.arraycopy(array, index, array, index + 1, size - index);
        array[index] = element;
        size++;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) return false;
        ensureCapacity(size + c.size());
        for (E element : c) {
            array[size++] = element;
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (index < 0 || index > size || c == null) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        ensureCapacity(size + c.size());
        System.arraycopy(array, index, array, index + c.size(), size - index);
        int i = index;
        for (E element : c) {
            array[i++] = element;
        }
        size += c.size();
        return true;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) >= 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof URArrayList)) return false;
        URArrayList<?> that = (URArrayList<?>) o;
        if (this.size != that.size) return false;
        for (int i = 0; i < size; i++) {
            if (!array[i].equals(that.array[i])) return false;
        }
        return true;
    }

    @Override
    public E get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        return array[index];
    }

    @Override
    public int indexOf(Object o) {
        if (o == null) {
            for (int i = 0; i < size; i++) {
                if (array[i] == null) return i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(array[i])) return i;
            }
        }
        return -1;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                return array[currentIndex++];
            }
        };
    }

    @Override
    public E remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        E removedElement = array[index];
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(array, index + 1, array, index, numMoved);
        }
        array[--size] = null;
        return removedElement;
    }

    @Override
    public boolean remove(Object o) {
        int index = indexOf(o);
        if (index >= 0) {
            remove(index);
            return true;
        }
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object element : c) {
            while (remove(element)) {
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public E set(int index, E element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        E oldElement = array[index];
        array[index] = element;
        return oldElement;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public URList<E> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("fromIndex: " + fromIndex + ", toIndex: " + toIndex);
        }
        URArrayList<E> subList = new URArrayList<>();
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(array[i]);
        }
        return subList;
    }

    @Override
    public Object[] toArray() {
        @SuppressWarnings("unchecked")
        E[] result = (E[]) new Object[size];
        System.arraycopy(array, 0, result, 0, size);
        return result;
    }

}
