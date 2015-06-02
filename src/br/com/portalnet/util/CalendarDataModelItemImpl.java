package br.com.portalnet.util;

import java.util.HashMap;
import java.util.Map;

import org.richfaces.model.CalendarDataModelItem;


public class CalendarDataModelItemImpl implements CalendarDataModelItem, Comparable<CalendarDataModelItemImpl> {

    private Map<String,String> data;
    private String styleClass;
    private Object toolTip;
    private int day;
    private boolean enabled = true;
    
    
    public CalendarDataModelItemImpl() {
    	this.data = new HashMap<String, String>();
    }
    
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    /* (non-Javadoc)
     * @see org.richfaces.component.CalendarDataModelItem#getData()
     */
    public Map<String,String> getData() {
        return data;
    }

    /* (non-Javadoc)
     * @see org.richfaces.component.CalendarDataModelItem#getStyleClass()
     */
    public String getStyleClass() {
        return styleClass;
    }

    /* (non-Javadoc)
     * @see org.richfaces.component.CalendarDataModelItem#getToolTip()
     */
    public Object getToolTip() {
        return toolTip;
    }

    /* (non-Javadoc)
     * @see org.richfaces.component.CalendarDataModelItem#hasToolTip()
     */
    public boolean hasToolTip() {
        return getToolTip() != null;
    }

    /* (non-Javadoc)
     * @see org.richfaces.component.CalendarDataModelItem#isEnabled()
     */
    public boolean isEnabled() {
        return enabled;
    }
    
    public String getTitulo() {
    	return data.get("titulo");
    }
    
    public String getDescricao() {
    	return data.get("descricao");
    }

    /**
     * @param data the data to set
     */
    public void setData(Map<String,String> data) {
        this.data = data;
    }

    /**
     * @param styleClass the styleClass to set
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * @param toolTip the toolTip to set
     */
    public void setToolTip(Object toolTip) {
        this.toolTip = toolTip;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public void setTitulo(String titulo) {
    	data.remove("titulo");
    	data.put("titulo", "<b>"+titulo+"</b>");
    }
    
    public void setDescricao(String descricao) {
    	data.remove("descricao");
    	data.put("descricao", descricao);
    	this.setTitulo("");
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + day;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CalendarDataModelItemImpl other = (CalendarDataModelItemImpl) obj;
		if (day != other.day)
			return false;
		return true;
	}

	public int compareTo(CalendarDataModelItemImpl o) {
		return Integer.valueOf(day).compareTo(Integer.valueOf(o.getDay()));
	}
    
}
