package org.huxizhijian.simplerecipebook.greendao.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * 搜索历史
 *
 * @author huxizhijian 2017/5/10
 */
@Entity
public class SearchHistory implements SearchSuggestion {

    @Id
    private Long id;
    private String searchKey;
    private long saveTime;

    @Generated(hash = 328982365)
    public SearchHistory(Long id, String searchKey, long saveTime) {
        this.id = id;
        this.searchKey = searchKey;
        this.saveTime = saveTime;
    }

    @Generated(hash = 1905904755)
    public SearchHistory() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSearchKey() {
        return this.searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public long getSaveTime() {
        return this.saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    @Override
    public String getBody() {
        return searchKey;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(searchKey);
        dest.writeLong(saveTime);
    }

    public static final Parcelable.Creator<SearchHistory> CREATOR =
            new Parcelable.Creator<SearchHistory>() {
                public SearchHistory createFromParcel(Parcel in) {
                    return new SearchHistory(in);
                }

                public SearchHistory[] newArray(int size) {
                    return new SearchHistory[size];
                }
            };

    private SearchHistory(Parcel in) {
        id = in.readLong();
        searchKey = in.readString();
        saveTime = in.readLong();
    }

}
