// TODO: Make sure to make this class a part of the synthesizer package
// package <package name>;
package synthesizer;

import java.util.Iterator;

//TODO: Make sure to make this class and all of its methods public
//TODO: Make sure to make this class extend AbstractBoundedQueue<t>
public class ArrayRingBuffer<T>  extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new Arrayrb with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        // TODO: Create new array with capacity elements.
        //       first, last, and fillCount should all be set to 0.
        //       this.capacity should be set appropriately. Note that the local variable
        //       here shadows the field we inherit from AbstractBoundedQueue, so
        //       you'll need to use this.capacity to set the capacity.
		rb = (T[]) new Object[capacity];
		first = 0;
		last = 0;
		fillCount = 0;
		this.capacity = capacity;
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    public void enqueue(T x) {
		if(isFull())
		{
			throw new RunTimeExpection("Ring Buffer Overflow");
		}
		rb[last] = x;
		last = (last + 1)% capacity;
		fillCount += 1;
        // TODO: Enqueue the item. Don't forget to increase fillCount and update last.
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
		if(isEmpty())
		{
			throw new RuntimeException("Ring Buffer Underflow");
		}
		T item = rb[first];
		first = (first+1) % capacity;
		fillCount = fillCount -1;
		return item;
        // TODO: Dequeue the first item. Don't forget to decrease fillCount and update 
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
		T item = rb[first];
		return item;
        // TODO: Return the first item. None of your instance variables should change.
    }

    // TODO: When you get to part 5, implement the needed code to support iteration.
	
	 private class MapWizard implements Iterator<T> {
        private int currentIndex;
        public MapWizard() {
            currentIndex = first;
        }

        public boolean hasNext() {
            return !isEmpty();
        }

        public T next() {
            T currentThing = rb[currentIndex];
            currentIndex = (currentIndex + 1) % capacity;
            return currentThing;
        }
    }

    public Iterator<T> iterator() {
        return new MapWizard();
    }
}
