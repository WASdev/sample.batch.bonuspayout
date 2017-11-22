package mypk;

import javax.batch.api.listener.StepListener;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

@Named("myList")
@Dependent
public class MyList implements StepListener {

    /**
     * Default constructor. 
     */
    public MyList() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see StepListener#beforeStep()
     */
    public void beforeStep() {
        // TODO Auto-generated method stub
    }

	/**
     * @see StepListener#afterStep()
     */
    public void afterStep() {
        // TODO Auto-generated method stub
    }

}
