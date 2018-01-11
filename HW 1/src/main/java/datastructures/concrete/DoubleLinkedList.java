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

    @Override
    public void add(T item) {
        if (size == 0) {
	        	front = new Node<T>(item);
	        	back = front;
	        	//needed? 
	        	front.prev = null;
	    		back.next = null;
        } else {
	        	back.next = new Node<T>(item);
	        	back.next.prev = back;
	        	back = back.next;
        }
        size++;
    }

    //Check if right exception or nosuch element except
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

    @Override
    public T get(int index) {
    		checkBounds(index, 1);
    		//if in the first half, start at front
    		if (index < size/2) {
    			Node<T> curr = front;
    			int currIndex = 0;
    			while (currIndex != index) {
    				curr = curr.next;
    				currIndex++;
    			}
    			return curr.data;
    		//else start from the back.
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

    @Override
    public void set(int index, T item) {
    		checkBounds(index, 1);
        Node<T> node = new Node<>(item);
        //if there is 1 or no nodes
        if (size == 0 || size == 1) {
	        	front = node;
	        	back = front;
	    // Case when setting front
	    } else if (index == 0) {
		    	node.next = front.next;
		    	front.next.prev = node;
		    	front = node;
	        // Case when setting back
	    } else if (index == size - 1) {
		    	node.prev = back.prev;
		    	back.prev.next = node;
		    	back = node;
	    } else {
	    		//if in the first half, start at front
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
			//else start from the back.
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
    
    //Private helper method that checks if the given index
    //is within bounds for setting or removing within the list
    //or the next index after the list ends.
    private void checkBounds(int index, int type) {
    		//checks if index is within list
    		if (type == 1) {
    			if (index < 0 || index >= this.size()) {
    				throw new IndexOutOfBoundsException();
    			}
    		//checks if index is within list or the index after
    		//the list ends
    		} else {
    			if (index < 0 || index >= this.size() +1) {
    				throw new IndexOutOfBoundsException();
	    		}
		}
    }

    @Override
    public void insert(int index, T item) {
    		checkBounds(index, 2);
        // If list is empty or index is after the last element then 
    		//call the add method
        if (size == 0 | index == size) {
            add(item);
        // Add to front of the list
        //If index is the end of the list
    	} else {
	        Node<T> node = new Node<>(item);
	    	if (index == 0) {
		        //Node<T> node = new Node<>(item);
		        node.next = this.front;
		        front.prev = node;
		        front = node;
		        size++;
	        // If anywhere else in the list
			} else if (index <= size / 2) {
	            Node<T> curr = front.next;
	            // Loop through the list until we get to the index we want
	            int currIndex = 1;
	            //current != null not neccesary? 
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

    @Override
    public T delete(int index) {
    		checkBounds(index, 1);
    		T result = null;
        // if index is the last element, call remove
        if (index == size - 1) {
            return remove();
            //may need to add one to size so that it isn't called in this and 
            //remove method
            // If index is front of the list
        } else if (index == 0) { 
        	result = front.data;	
        	front.next.prev = null;
            front = front.next;
            
//            Node<T> temp = front.next;
//            front.next.prev = null;
//            front.next = null;
//            front = temp;
        // If index is in the middle of the list
        } else {
        		if (index < size / 2) {
                Node<T> curr = front.next;
                int currIndex = 1;
                //current != null not neccesary? 
                while (currIndex != index) {
                    curr = curr.next;
                    currIndex++;
                }
                result = curr.data;
                curr.prev.next = curr.next;
                curr.next.prev = curr.prev;
                //check if these bottom two are necessary 
                curr.next = null;
                curr.prev = null;
            } else {
	    	        Node<T> curr = back.prev;
	    	        int currIndex = size-2;
	    			while (currIndex != index) {
	    				curr = curr.prev;
	    				currIndex--;
	    			}
	    			result = curr.data;
                curr.prev.next = curr.next;
                curr.next.prev = curr.prev;
                //check if these bottom two are necessary 
                curr.next = null;
                curr.prev = null;
            }
        }
        size --;
        return result;
    }

    @Override
    public int indexOf(T item) {
    		int index = 0;
        Node<T> curr = this.front;
        while (curr != null) {
//             If item is founds returns index
            if (item == null & curr.data == item) {
                return index;
            } else
            	if (curr.data.equals(item)) {
                return index;
            }
            curr = curr.next;
            index++;
        }
        // If item is not found, returns -1
        return -1;  
    }

    @Override
    public int size() {
    		return size;
    }

    @Override
    public boolean contains(T other) {
    		// Makes temporary pointer to front
        Node<T> curr = this.front;
        while (curr != null) {
            if (other == null & curr.data == other) {
                return true;
                // If other is found returns true
            } else 
            	if (curr.data.equals(other)) {
                return true;
            }
            curr = curr.next;
        }
        // If not found, return false
        return false;    
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
