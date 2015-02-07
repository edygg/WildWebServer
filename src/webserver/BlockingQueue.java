package webserver;

import java.util.LinkedList;

/**
 *
 * @author cbanegas
 */
public class BlockingQueue {

  private final LinkedList list = new LinkedList();//Lista que contendrá los elementos del pool
  private boolean closed = false;//Estado de la lista
  private boolean wait = false;
  
  synchronized public void enqueue(Object o) {
    if(closed){
      throw new ClosedException();
    }
    list.add(o);
    notify();
  }

  synchronized public Object dequeue() {
    while(!closed && list.size()==0){//Si la lista está abierta y no hay nada en cola
      try{//Atrapar la excepción
        wait();//Esperar hasta que haya una tarea
      }catch(InterruptedException e){//Ignorar la excepción
      }
    }
    if(list.size()==0){//Si no hay nada en cola
      return null;
    }
    return list.removeFirst();//Retornar el primer request en cola
  }
  
  synchronized public int size(){//Retornar tamaño de la lista
      return list.size();
  }
  
  synchronized public void close(){//Cierra la lista
    closed = true;
    notifyAll();
  }
  
  synchronized public void open(){//Abre la lista
      closed = false;
  }
  
  public static class ClosedException extends RuntimeException {
      ClosedException() {
          super("Queue closed.");
      }
  }
}
