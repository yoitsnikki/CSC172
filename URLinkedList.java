/*
 * Niharika Agrawal
 * CSC 172
 * Lab 4 - Linked List
 */

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class URLinkedList<E> implements URList<E> {
    private URNode<E> head; // first node
    private URNode<E> tail; // last node
    private int size; // size of list

    // initiate list
    public URLinkedList() {
        head = null;
        tail = null;
        size = 0;
    }

    // append to end of list
    @Override
    public boolean add (E e) {
        URNode<E> newNode = new URNode<>(e, tail, null);
        
        // if list is empty
        if (tail == null) {
            head = newNode;
        } 
        
        // if list is not empty
        else {
            tail.setNext(newNode);
        }

        tail = newNode;
        size++;
        return true;
    }

    // insert element at specific position
    @Override
    public void add (int index, E element) {
        checkPositionIndex(index); // check position to insert

        // add at end
        if (index == size) {
            add(element);
        } else {
            URNode<E> current = getNode(index);
            URNode<E> newNode = new URNode<> (element, current.prev(), current);

            // add at beginnning

            if (current.prev() == null) {
                head = newNode;
            } else {
                current.prev().setNext(newNode);
            }
            current.setPrev(newNode);
            size++;
        }
    }

    // append all elements in collection to end of list
    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E element : c) {
            add(element);
        }
        return true;
    }

    // insert all elements in collection into specific position in list
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        checkPositionIndex(index); // check index position

        if (c.isEmpty()) return false; // check if collection is empty

        int currentIndex = index;
        for (E element : c) {
            add(currentIndex++, element);
        }
        return true;
    }

    // return all elements
    @Override
    public void clear() {
        head = tail = null;
        size = 0;
    }

    // return true if list has specific element
    @Override
    public boolean contains (Object o) {
        return indexOf(o) != -1;
    }

    // return true if list contains all elements in collection
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    // does object equal something in list?
    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (!(o instanceof URList)) return false;

        URList<?> other = (URList<?>) o;
        if (size() != other.size()) return false;

        Iterator<E> it1 = this.iterator();
        Iterator<?> it2 = other.iterator();

        while (it1.hasNext() && it2.hasNext()) {
            E e1 = it1.next();
            Object e2 = it2.next();
            if(!(e1 == null? e2 == null : e1.equals(e2))) {
                return false;
            }
        }
        return true;
    }

    // return element at specific position in list
    @Override
    public E get(int index) {
        checkElementIndex(index);
        return getNode(index).element();
    }

    // return index of first occurence after specified element
    @Override
    public int indexOf(Object o) {
        int index = 0;
        for (URNode<E> x = head; x != null; x = x.next()) {
            if (o == null ? x.element() == null : o.equals(x.element())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    // return true if list is empty
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // iterate over list 
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E> () {
            private URNode<E> current = head;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) throw new NoSuchElementException();
                E data = current.element();
                current = current.next();
                return data;
            }
        };
    }

    // remove element from list
    @Override
    public E remove (int index) {
        checkElementIndex(index);
        return unlink(getNode(index));
    }

    // remove first occurence of element from list
    @Override
    public boolean remove(Object o) {
        for (URNode<E> x = head; x != null; x = x.next()) {
            if (o == null ? x.element() == null : o.equals(x.element())) {
                unlink(x);
                return true;
            }
        }
        return false;
    }

    // remove all elements in collection from list
    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }

    // replace element in list with a different element
    @Override
    public E set(int index, E element) {
        checkElementIndex(index);
        URNode<E> x = getNode(index);
        E oldValue = x.element();
        x.setElement(element);
        return oldValue;
    }

    // return number of elements in list
    @Override
    public int size() {
        return size;
    }

    // return portion of list between fromIndex, inclusive, toIndex, exclusive
    @Override
    public URList<E> subList(int fromIndex, int toIndex) {
        checkPositionIndex(fromIndex);
        checkPositionIndex(toIndex);
        URLinkedList<E> subList = new URLinkedList<>();
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(get(i));
        }
        return subList;
    }

    // return array containing all elements in list
    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        int i = 0;
        for (URNode<E> x = head; x != null; x = x.next()) {
            result[i++] = x.element();
        }
        return result;
    }

    // LINKED LIST SPECIFIC METHODS

    // insert item at beginning of list
    public void addFirst(E e) {
        URNode<E> newNode = new URNode<>(e, null, head);
        if (head == null) {
            tail = newNode;
        } else {
            head.setPrev(newNode);
        }
        head = newNode;
        size++;
    }

    // append item to end of list
    public void addLast (E e) {
        add(e);
    }

    // retrieve first element of list
    public E peekFirst() {
        return (head == null) ? null : head.element();
    }

    // retrieve last element of list
    public E peekLast() {
        return (tail == null) ? null : tail.element();
    }

    // retrieve and remove first element of list
    public E pollFirst() {
        if (head == null) return null;
        return unlink(head);
    }

    // retrieve and remove last element of list
    public E pollLast() {
        if (tail == null) return null;
        return unlink(tail);
    }

    // helper method: unlink node
    public E unlink(URNode<E> x) {
        E element = x.element();
        URNode<E> next = x.next();
        URNode<E> prev = x.prev();

        if (prev == null) {
            head = next;
        } else {
            prev.setNext(next);
            x.setPrev(null);
        }

        if (next == null) {
            tail = prev;
        } else {
            next.setPrev(prev);
            x.setNext(null);
        }

        x.setElement(null);
        size--;
        return element;
    }

    // helper: getNode at index
    public URNode<E> getNode(int index) {
        URNode<E> x;
        if (index < (size >> 1)) {
            x = head;
            for (int i = 0; i < index; i++) {
                x = x.next();
            }
        }
        
        else {
            x = tail;
            for (int i = size - 1; i > index; i--) {
                x = x.prev();
            }
        }
        return x;
    }

    // helper: check if index is within list bounds
    public void checkElementIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    // helper: check if index is in valid position bounds
    public void checkPositionIndex(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    // out of bounds message
    public String outOfBoundsMsg(int index) {
        return "Index: " + index + ", Size: " + size;
    }






}
