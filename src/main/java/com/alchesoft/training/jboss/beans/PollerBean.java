package com.alchesoft.training.jboss.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;

@Named
@ManagedBean
@ViewScoped
public class PollerBean implements Serializable {

    private boolean pollingActive = true;

    public void setPollingActive(boolean pollingActive) {
        this.pollingActive = pollingActive;
    }

    public boolean isPollingActive() {
        return pollingActive;
    }
}
