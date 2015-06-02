package br.com.portalnet.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.ValueChangeEvent;

import org.richfaces.model.CalendarDataModel;
import org.richfaces.model.CalendarDataModelItem;


public class CalendarDataModelImpl implements CalendarDataModel {

    private List<CalendarDataModelItemImpl> listItems;
	private CalendarDataModelItemImpl[] items;
    
	private String titulo;
    private String currentDescription;
    private String currentShortDescription;
    private Date currentDate;
    private boolean currentDisabled;
    
    
    public CalendarDataModelImpl() {
    	listItems = new ArrayList<CalendarDataModelItemImpl>();
    }
    
    /* (non-Javadoc)
     * @see org.richfaces.model.CalendarDataModel#getData(java.util.Date[])
     */
    public CalendarDataModelItem[] getData(Date[] dateArray) {
        if (dateArray == null) {
            return null;
        }
        
        if (items == null) {  
        	items = new CalendarDataModelItemImpl[dateArray.length];
        	Collections.sort(listItems);
        	
			Calendar data = Calendar.getInstance();

			for(int i = 0, indList = 0; i < dateArray.length; i++) {
    			data.setTime(dateArray[i]);
    			
    			if (indList < listItems.size() && listItems.get(indList).getDay() == data.get(Calendar.DAY_OF_MONTH)) {
    				items[i] = listItems.get(indList);
    				indList++;
    			} else {
    				items[i] = createDataModelItem(dateArray[i]);
    			}
    		}
        }
        return items;
    }

    /**
     * @param date
     * @return CalendarDataModelItem for date
     */
    protected CalendarDataModelItemImpl createDataModelItem(Date date) {
        CalendarDataModelItemImpl item = new CalendarDataModelItemImpl();
        Map<String, String> data = new HashMap<String, String>();
        data.put("titulo", "");
        data.put("descricao", "");
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        item.setDay(c.get(Calendar.DAY_OF_MONTH));
        item.setEnabled(true);
        item.setStyleClass("rel-hol");
        item.setData(data); 
        return item;
    } 

    /* (non-Javadoc)
     * @see org.richfaces.model.CalendarDataModel#getToolTip(java.util.Date)
     */
    public Object getToolTip(Date date) {
    	return null;
    }

    /**
     * @return listItems
     */
    public List<CalendarDataModelItemImpl> getListItems() {
		return listItems;
	}

    /**
     * @param setter for listItems
     */
    public void setListItems(List<CalendarDataModelItemImpl> listItems) {
    	CalendarDataModelItemImpl[] arrayItems = new CalendarDataModelItemImpl[listItems.size()];
		for(int i = 0; i < listItems.size(); i++)
			arrayItems[i] = listItems.get(i);
		
		this.items = arrayItems;
		this.listItems = listItems;
	}

	/**
     * @return items
     */
    public CalendarDataModelItemImpl[] getItems() {
        return items;
    }

    /**
     * @param setter for items
     */
    public void setItems(CalendarDataModelItemImpl[] items) {
        this.items = items;
    }

    /**
     * @param valueChangeEvent handling
     */
    public void valueChanged(ValueChangeEvent event) {
        System.out.println(event.getNewValue()+"selected");
        setCurrentDate((Date)event.getNewValue());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getCurrentDate());
        setCurrentDescription(items[calendar.get(Calendar.DAY_OF_MONTH)-1].getData().get("descricao"));
        setCurrentShortDescription(items[calendar.get(Calendar.DAY_OF_MONTH)-1].getData().get("titulo"));
    }

    /**
     * Storing changes action
     */
    public void storeDayDetails() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getCurrentDate());
        (items[calendar.get(Calendar.DAY_OF_MONTH)-1].getData()).put("titulo", getCurrentShortDescription());
        (items[calendar.get(Calendar.DAY_OF_MONTH)-1].getData()).put("descricao", getCurrentDescription());
    }
    
    public void addItem(CalendarDataModelItemImpl item) {
    	if (listItems.contains(item)) {
    		for (CalendarDataModelItemImpl itemAux : listItems) {
    			if (itemAux.getDay() == item.getDay()) {
    				itemAux.setDescricao(itemAux.getDescricao()+"<br/>"+item.getDescricao());
    				itemAux.setStyleClass(item.getStyleClass());
    			}
    		}
    	} else {
    		listItems.add(item);
    	}
    	
    }
    
    public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	/**
     * @return currentDescription
     */
    public String getCurrentDescription() {
        return currentDescription;
    }

    /**
     * @param currentDescription
     */
    public void setCurrentDescription(String currentDescription) {
        this.currentDescription = currentDescription;
    }

    /**
     * @return currentDisabled
     */
    public boolean isCurrentDisabled() {
        return currentDisabled;
    }

    /**
     * @param currentDisabled
     */
    public void setCurrentDisabled(boolean currentDisabled) {
        this.currentDisabled = currentDisabled;
    }

    /**
     * @return currentShortDescription
     */
    public String getCurrentShortDescription() {
        return currentShortDescription;
    }

    /**
     * @param currentShortDescription
     */
    public void setCurrentShortDescription(String currentShortDescription) {
        this.currentShortDescription = currentShortDescription;
    }

    /**
     * @return currentDate
     */
    public Date getCurrentDate() {
        return currentDate;
    }

    /**
     * @param currentDate
     */
    public void setCurrentDate(Date currentDate) {
        this.currentDate = currentDate;
    }
    
}
