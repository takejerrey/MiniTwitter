package observable;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject {

    List<Observer> observers;

    public Subject() {
        observers = new ArrayList<>();
    }

    public void attachObserver(Observer observer) {
        observers.add(observer);
    }

    public void detachObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObservers() {
        for(Observer observer : observers) {
            observer.update();
        }
    }

}