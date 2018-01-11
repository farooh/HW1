package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    // Adds an element to the end of the the list
    @Override
    public void add(T item) {
        if (size == 0) {
	        	front = new Node<T>(item);
	        	back = front;
	        	front.prev = null;
	    		back.next = null;
        } else {
	        	back.next = new Node<T>(item);
	        	back.next.prev = back;
	        	back = back.next;
        }
        size++;
    }

    // Removes an element from the end of the list and returns it's data
    @Override
    public T remove() {
    		if (size == 0) {
    			throw new EmptyContainerException();
    		} 
    		T result = back.data;
    		if (size == 1) {
    			back = null;
    			front = null;
    		} else {
    			back = back.prev;
    			back.next = null;
    		}
    		size --;
    		return result;
    }

    // Finds the element at the given index and returns it's data
    @Override
    public T get(int index) {
    		checkBounds(index, 1);
    		// If in the first half, start at front
    		if (index < size/2) {
    			Node<T> curr = front;
    			int currIndex = 0;
    			while (currIndex != index) {
    				curr = curr.next;
    				currIndex++;
    			}
    			return curr.data;
    		// Else start from the back.
    		} else {
    			Node<T> curr = back;
    			int currIndex = size-1;
    			while (currIndex != index) {
    				curr = curr.prev;
    				currIndex--;
    			}
    			return curr.data;
    		}
    }

    //Finds the element at the given index and modifies it's data to the given data
    //If no element exists at that index, it creates a new element with the given data
    @Override
    public void set(int index, T item) {
    		checkBounds(index, 1);
        Node<T> node = new Node<>(item);
        // If there is 1 or no nodes in the list
        if (size == 0 || size == 1) {
	        	front = node;
	        	back = front;
	    // Sets front if there are more than 1 element in the list
	    } else if (index == 0) {
		    	node.next = front.next;
		    	front.next.prev = node;
		    	front = node;
	    // Sets back if there are more than 1 element in the list
	    } else if (index == size - 1) {
		    	node.prev = back.prev;
		    	back.prev.next = node;
		    	back = node;
	    // Sets any other index
	    } else {
	    		// If in the first half, start at front
			if (index < size/2) {
				Node<T> curr = front;
				int currIndex = 0;
				while (currIndex != index) {
					curr = curr.next;
					currIndex++;
				}
				node.next = curr.next;
		        	curr.next.prev = node;
		        	node.prev = curr.prev;
		        	curr.prev.next = node;
			// Else start from the back.
			} else {
				Node<T> curr = back;
				int currIndex = size-1;
				while (currIndex != index) {
					curr = curr.prev;
					currIndex--;
				}
				node.next = curr.next;
		        	curr.next.prev = node;
		        	node.prev = curr.prev;
		        	curr.prev.next = node;
			}
        }   
    }

    // Inserts an element with the given data at the given index
    @Override
    public void insert(int index, T item) {
    		checkBounds(index, 2);
        // If list is empty or index is after the last element, 
    		// call the add method
        if (size == 0 | index == size) {
            add(item);
    		} else {
	        Node<T> node = new Node<>(item);
	        // Add to front of the list
	        if (index == 0) {
		        node.next = this.front;
		        front.prev = node;
		        front = node;
		        size++;
	        // If any other index in the list
		    // If in the first half, start at front
			} else if (index <= size / 2) {
	            Node<T> curr = front.next;
	            int currIndex = 1; 
	            while (currIndex != index) {
	                curr = curr.next;
	                currIndex++;
	            }
	            Node<T> previous = curr.prev;
	            node.prev = previous;
	            node.next = curr;
	            previous.next = node;
	            curr.prev = node;
	            size++;
	        // Else start from the back.
	        } else {
		        Node<T> curr = back;
		        int currIndex = size-1;
				while (currIndex != index) {
					curr = curr.prev;
					currIndex--;
				}
		        Node<T> previous = curr.prev;
		        node.prev = previous;
		        node.next = curr;
		        previous.next = node;
		        curr.prev = node;
		        size++;
	        }
    		}
    }

    // Deletes an element at the given index and returns it's data
    // Shifts all remaining elements in the list forward
    @Override
    public T delete(int index) {
    		checkBounds(index, 1);
    		T result = null;
        // If index is the last element in the list, call remove
        if (index == size - 1) {
            return remove();
        // If index is the front of the list
        } else if (index == 0) { 
	        	result = front.data;	
	        	front.next.prev = null;
            front = front.next;
        // If index is anywhere else in the list
        } else {
        		// If in the first half, start at front
        		if (index < size / 2) {
                Node<T> curr = front.next;
                int currIndex = 1;
                while (currIndex != index) {
                    curr = curr.next;
                    currIndex++;
                }
                result = curr.data;
                curr.prev.next = curr.next;
                curr.next.prev = curr.prev;
                curr.next = null;
                curr.prev = null;
            // Else start from the back.
            } else {
	    	        Node<T> curr = back.prev;
	    	        int currIndex = size -2;
	    			while (currIndex != index) {
	    				curr = curr.prev;
	    				currIndex--;
	    			}
	    			result = curr.data;
                curr.prev.next = curr.next;
                curr.next.prev = curr.prev;
                curr.next = null;
                curr.prev = null;
            }
        }
        size --;
        return result;
    }

    // Returns the first index of an element with the given data in the list
    @Override
    public int indexOf(T item) {
    		int index = 0;
        Node<T> curr = this.front;
        while (curr != null) {
        		// Allows for null to be searched
            if (item == null & curr.data == item) {
                return index;
            } else if (curr.data.equals(item)) {
                return index;
            }
            curr = curr.next;
            index++;
        }
        // If an element with that data is not found, returns -1
        return -1;  
    }

    // Returns the size of the list
    @Override
    public int size() {
    		return size;
    }

    // Checks the data for the given data and returns true if it 
    // is found in the list
    @Override
    public boolean contains(T other) {
        Node<T> curr = this.front;
        while (curr != null) {
	        // Allows for null to be searched    
	        	if (other == null & curr.data == other) {
                return true;
	        } else if (curr.data.equals(other)) {
	            return true;
	        }
	        curr = curr.next;
        }
        // If an element with that data is not found, returns false
        return false;    
    }
    
    // Private helper method that checks for the given index
    private void checkBounds(int index, int type) {
    		// Checks if given index is within list
    		if (type == 1) {
    			if (index < 0 || index >= size) {
    				throw new IndexOutOfBoundsException();
    			}
    		// Checks if give index is within list or the index after
    		// the list ends
    		} else {
    			if (index < 0 || index >= size +1 ) {
    				throw new IndexOutOfBoundsException();
	    		}
		}
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
        		if (current != null) {
        			return true;
        		} else {
        			return false;
        		}
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
        		if (!hasNext()) {
        			throw new NoSuchElementException();
        		}
            T currData = current.data;
            current = current.next;
            return currData;
        }
    }
}
