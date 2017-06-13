
package com.creedon.cordova.plugin.photobrowser.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class ActionSheet implements Serializable, Parcelable
{

    @SerializedName("action")
    @Expose
    private String action;
    @SerializedName("label")
    @Expose
    private String label;
    public final static Parcelable.Creator<ActionSheet> CREATOR = new Creator<ActionSheet>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ActionSheet createFromParcel(Parcel in) {
            ActionSheet instance = new ActionSheet();
            instance.action = ((String) in.readValue((String.class.getClassLoader())));
            instance.label = ((String) in.readValue((String.class.getClassLoader())));
            return instance;
        }

        public ActionSheet[] newArray(int size) {
            return (new ActionSheet[size]);
        }

    }
    ;
    private final static long serialVersionUID = -895171481586808629L;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(action);
        dest.writeValue(label);
    }

    public int describeContents() {
        return  0;
    }

}